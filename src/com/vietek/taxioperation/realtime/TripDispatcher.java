package com.vietek.taxioperation.realtime;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonObject;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.realtime.command.MobileCommand;
import com.vietek.taxioperation.realtime.command.driver.DriverListAppFollowUpdateToDB;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.DateUtils;
import com.vietek.taxioperation.util.MapUtils;
import com.vietek.taxioperation.util.TaxiUtils;

public class TripDispatcher extends TimerTask {

	public static final String CMD_ASK_CONFIRM = "$ask_confirm_trip=";

	public static final String DISPATCH_TYPE_UNICAST = "U";
	public static final String DISPATCH_TYPE_MULTICAST = "M";

	public int MAX_DRIVER_TO_ASK = ConfigUtil.getValueConfig("MAX_DRIVER_TO_ASK", CommonDefine.MAX_DRIVER_TO_ASK);
	public int MAX_DISTANCE = ConfigUtil.getValueConfig("MAX_DISTANCE", CommonDefine.MAX_DISTANCE); // 1km
	public int WAIT_TIME_PER_DRIVER = ConfigUtil.getValueConfig("WAIT_TIME_PER_DRIVER",
			CommonDefine.WAIT_TIME_PER_DRIVER);// second
	public int TRIP_WAIT_TIME_OUT = ConfigUtil.getValueConfig("TRIP_WAIT_TIME_OUT", CommonDefine.TRIP_WAIT_TIME_OUT); // 10'

	private Trip trip;
	private ArrayList<Taxi> listDriver;
	String confirmedDriverId = null;
	Timer startTripTimer;
	ScheduledThreadPoolExecutor unicastDispatchSchedule;
	ScheduledThreadPoolExecutor multicastDispatchSchedule;
	private Rider rider;
	private TaxiOrder order;

	private String dispatchType = DISPATCH_TYPE_UNICAST;

	public TripDispatcher(Trip trip, Rider rider) {
		this.trip = trip;
		this.rider = rider;
		this.order = trip.getOrder();
		dispatchType = ConfigUtil.getValueConfig("FIND_TAXI_TYPE", DISPATCH_TYPE_UNICAST);
		startTripTimer = new Timer("TripDispatcher_timer: " + trip.toString());
		int minute = ConfigUtil.getConfig("MINUTE_AFTER_START_TRIP",
				CommonDefine.DEFAULT_MINUTE_AFTER_START_TRIP_AIRPORT);
		if (trip.getOrder().getAirStation() != null && trip.getOrder().getAirStation().booleanValue()) {
			if (DateUtils.dateAfterMiniture(trip.getOrder().getBeginOrderTime(), minute)) {
				startTripTimer.schedule(this, new Date());
			} else {
				startTripTimer.schedule(this,
						new Date(trip.getOrder().getBeginOrderTime().getTime() - minute * 60 * 1000));
			}
		} else {
			startTripTimer.schedule(this, trip.getOrder().getBeginOrderTime());
		}

	}

	private void sortDriverForRider() {
		/*
		 * int carType = this.getRider().getCarType(); if
		 * (order.getOrderCarType() > carType) { carType =
		 * order.getOrderCarType(); }
		 */
		int carType = 0;
		if (order.getOrderCarType() == 3) {
			carType = 0;
		} else {
			carType = order.getOrderCarType();
		}
		ArrayList<Taxi> lstQueue = Taxi.getListTaxiByType(carType, MAX_DRIVER_TO_ASK, order.getBeginLon(),
				order.getBeginOrderLat());
		if (lstQueue == null || lstQueue.size() == 0) { // Queue is empty
			listDriver = (ArrayList<Taxi>) TaxiUtils.getAvaiableTaxiFilter(order.getBeginOrderLon(),
					order.getBeginOrderLat(), carType, MAX_DISTANCE, MAX_DRIVER_TO_ASK, lstQueue);
		} else {
			if (lstQueue.size() == MAX_DRIVER_TO_ASK) { // Queue size =
														// MAX_DRIVER_TO_ASK
				listDriver = lstQueue;
			} else {
				ArrayList<Taxi> lstTemp1 = (ArrayList<Taxi>) TaxiUtils.getAvaiableTaxiFilter(order.getBeginOrderLon(),
						order.getBeginOrderLat(), carType, MAX_DISTANCE, MAX_DRIVER_TO_ASK, lstQueue);
				if (lstTemp1 == null || lstTemp1.size() == 0) { // list taxi
																// null
					listDriver = lstQueue;
				} else {
					if (lstTemp1.size() + lstQueue.size() <= MAX_DRIVER_TO_ASK) {
						// In case lstQueue and list taxi <= MAX_DRIVER_TO_ASK
						listDriver = lstQueue;
						listDriver.addAll(lstTemp1);
					} else {
						int temp = MAX_DRIVER_TO_ASK - lstQueue.size();
						listDriver = lstQueue;
						listDriver.addAll(lstTemp1.subList(0, temp));
					}
				}
			}
		}
		trip.setListDriver(listDriver);
	}

	int driverIndex = 0;

	private void findDriverForTrip() {

		sortDriverForRider();

		if (dispatchType.equals(DISPATCH_TYPE_UNICAST)) {
			unicastDispatch();
		} else if (dispatchType.equals(DISPATCH_TYPE_MULTICAST)) {
			multicastDispatch();
		} else {
			unicastDispatch();
		}
	}

	private void unicastDispatch() {
		updateListDriverToDB();
		unicastDispatchSchedule = new ScheduledThreadPoolExecutor(1);
		unicastDispatchSchedule.scheduleAtFixedRate(new AskDriverTask(), 0, WAIT_TIME_PER_DRIVER, TimeUnit.SECONDS);
	}

	private void multicastDispatch() {
		updateListDriverToDB();
		for (Taxi taxi : listDriver) {
			askDriverConfirmTrip(taxi);
		}
		multicastDispatchSchedule = new ScheduledThreadPoolExecutor(1);
		multicastDispatchSchedule.schedule(new TripTimeout(), TRIP_WAIT_TIME_OUT, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		startTripTimer.cancel();
		findDriverForTrip();
	}

	private void askDriverConfirmTrip(Taxi driver) {
		if (rider.getCustomer() == null) {
			this.cancel();
		}
		JsonObject json = Json.createObjectBuilder().add("trip_id", this.trip.getId())
				.add("customer_id", rider.getCustomer().getId())
				.add("phone_number", rider.getCustomer().getPhoneNumber())
				.add("name", rider.getCustomer().getName() == null ? "" : rider.getCustomer().getName())
				.add("wait_time",
						dispatchType.equals(DISPATCH_TYPE_MULTICAST) ? TRIP_WAIT_TIME_OUT : WAIT_TIME_PER_DRIVER)
				.add("start_addr", trip.getOrder().getBeginOrderAddress())
				.add("start_long", trip.getOrder().getBeginOrderLon())
				.add("start_lat", trip.getOrder().getBeginOrderLat())
				.add("end_addr",
						trip.getOrder().getEndOrderAddress() == null ? "" : trip.getOrder().getEndOrderAddress())
				.add("end_long", trip.getOrder().getEndOrderAddress() == null ? 0 : trip.getOrder().getEndOrderLon())
				.add("end_lat", trip.getOrder().getEndOrderAddress() == null ? 0 : trip.getOrder().getEndOrderLat())
				.add("note", trip.getOrder().getNote() == null ? "" : trip.getOrder().getNote())
				.add("airport", trip.getOrder().getAirStation() == null ? false
						: trip.getOrder().getAirStation().booleanValue())
				.build();
		String cmd = CMD_ASK_CONFIRM + json + MobileCommand.END_CMD;
		driver.sendToDevice(cmd);
	}

	private void requestTripFails() {
		AppLogger.logDebug
				.info("TripAuto|RequestFalse|Phone:" + trip.getOrder().getPhoneNumber() + "|TimeLog:" + new Date());
		trip.stopAutoOperation();
		this.cancel();
	}

	public Rider getRider() {
		return rider;
	}

	public void setRider(Rider rider) {
		this.rider = rider;
	}

	class TripTimeout extends TimerTask {

		@Override
		public void run() {
			trip.stopAutoOperation();
			for (Taxi taxi : listDriver) {
				taxi.sendToDevice("$confirm_timeout={\"trip_id\": \"" + trip.getId() + "\"}" + MobileCommand.END_CMD);
			}
		}
	}

	Taxi driver = null;

	class AskDriverTask extends TimerTask {
		@Override
		public void run() {
			trip.setTaxiTmp(null);
			if (driver != null) {
				driver.sendToDevice("$confirm_timeout={\"trip_id\": \"" + trip.getId() + "\"}" + MobileCommand.END_CMD);
				driver.setTrip(null);
			}
			if (driverIndex >= listDriver.size()) {
				requestTripFails();
				return;
			}
			driver = listDriver.get(driverIndex);
			driverIndex++;
			while (driver.getTrip() != null || driver.equals(trip.getPostGuestTaxi())) {
				if (driverIndex >= listDriver.size()) {
					requestTripFails();
					return;
				}
				driver = listDriver.get(driverIndex);
				driverIndex++;
			}
			double distance = MapUtils.distance(driver.getLongitude(), driver.getLattitute(),
					trip.getOrder().getBeginOrderLon(), trip.getOrder().getBeginOrderLat());
			if (!driver.isConnect()) {
				distance = Double.MAX_VALUE;
			}
			if (distance > MAX_DISTANCE) {
				requestTripFails();
				return;
			}
			trip.setTaxiTmp(driver);
			driver.setTrip(trip);
			askDriverConfirmTrip(driver);

			StringBuilder sb = new StringBuilder();
			sb.append("TripAuto|SendToDriver|Phone:").append(trip.getOrder().getPhoneNumber());
			sb.append("|Driver:").append(driver.getDriverName());
			sb.append("|Vehicle:").append(driver.getVehicle().getFullValue());
			sb.append("|TimeLog:").append(new Date());
			AppLogger.logDebug.info(sb.toString());
		}
	}

	@Override
	public boolean cancel() {
		if (unicastDispatchSchedule != null)
			unicastDispatchSchedule.shutdownNow();
		if (multicastDispatchSchedule != null) {
			multicastDispatchSchedule.shutdownNow();
			for (Taxi taxi : listDriver) {
				taxi.sendToDevice("$confirm_timeout={\"trip_id\": \"" + trip.getId() + "\"}" + MobileCommand.END_CMD);
			}
		}
		return super.cancel();
	}

	private void updateListDriverToDB() {
		DriverListAppFollowUpdateToDB thread = new DriverListAppFollowUpdateToDB(listDriver, order, trip.getStatus(),
				order.getBeginOrderLon(), order.getBeginOrderLat());
		thread.run();
	}

}
