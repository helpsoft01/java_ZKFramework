package com.vietek.taxioperation.realtime;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.zkoss.zk.ui.event.Event;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.EnumStatus;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.CustomerController;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.DriverCancelTrip;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.model.VehicleStatusDD;
import com.vietek.taxioperation.mq.TaxiOrderMQ;
import com.vietek.taxioperation.realtime.command.MobileCommand;
import com.vietek.taxioperation.realtime.command.driver.DriverListAppFollowUpdateToDB;
import com.vietek.taxioperation.realtime.command.driver.DriverUpdateTripStatusCommand;
import com.vietek.taxioperation.realtime.command.rider.RiderCancelRequestCommand;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.Content;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.GCMUtils;
import com.vietek.taxioperation.util.NotificationUtils;

public class Trip extends TimerTask {
	public static final String CMD_ASK_CONFIRM_RETURN = "$driver_confirm_trip_return=";
	public static final String UPDATE_DRIVER_INFO_CMD = "$update_driver_info=";
	public static final String DRIVER_IN_START_POSITION = "$driver_in_start_position=";
	private TaxiOrder order;
	private Taxi taxi;
	private Taxi taxiTmp;
	private Driver driver;
	private Vehicle vehicle;
	private Rider rider;
	private ArrayList<Taxi> listDriver;

	private Taxi postGuestTaxi;
	private ScheduledThreadPoolExecutor updateTimer;

	public Trip() {
		super();
		// updateTimer = new ScheduledThreadPoolExecutor(1);
		// updateTimer.scheduleAtFixedRate(this, 0, 10, TimeUnit.SECONDS);
	}

	public Driver getDriver() {
		if (driver == null) {
			driver = order.getDriver();
		}
		return driver;
	}

	public Vehicle getVehicle() {
		if (vehicle == null) {
			vehicle = order.getVehicle();
		}
		return vehicle;
	}

	private String riderId;

	public Rider getRider() {
		if (riderId == null) {
			riderId = order.getCustomer().getPhoneNumber();
		}
		Rider rider = this.rider;
		if (rider == null) {
			rider = Rider.getRider(riderId);
		}
		if (rider != null && !rider.getRiderId().equalsIgnoreCase(Rider.KHACH_VANG_LAI)) {
			rider.setCustomer(CustomerController.getCustomer(riderId));
		}
		if (rider.getCustomer() == null) {
			Customer guest = new Customer();
			guest.setId(0);
			guest.setName("Khách vãng lai");
			guest.setPhoneNumber("0");
			rider.setCustomer(guest);
		}
		return rider;
	}

	private TripDispatcher tripDispatcher;

	public String getId() {
		if (getOrder() == null)
			return "";
		return "" + getOrder().getId();
	}

	public int getStatus() {
		return this.getOrder().getStatus();
	}

	public TaxiOrder getOrder() {
		return order;
	}

	public void setOrder(TaxiOrder order) {
		this.order = order;
	}

	public JsonObjectBuilder getTripInfo() {
		JsonObjectBuilder jsonObj = null;
		jsonObj = Json.createObjectBuilder().add("trip_id", this.getOrder().getId() + "")
				.add("trip_status", this.getStatus())
				.add("beginAddr", this.getOrder().getBeginAddress() == null ? "" : this.getOrder().getBeginAddress())
				.add("beginLat", this.getOrder().getBeginAddress() == null ? 0 : this.getOrder().getBeginLat())
				.add("beginLong", this.getOrder().getBeginAddress() == null ? 0 : this.getOrder().getBeginLon())
				.add("endAddr", this.getOrder().getEndAddress() == null ? "" : this.getOrder().getEndAddress())
				.add("endLat", this.getOrder().getEndAddress() == null ? 0 : this.getOrder().getEndLat())
				.add("endLong", this.getOrder().getEndAddress() == null ? 0 : this.getOrder().getEndLon())
				.add("currentLat", getTaxi() == null ? 0 : getTaxi().getLattitute())
				.add("currentLong", getTaxi() == null ? 0 : getTaxi().getLongitude())
				.add("currentAngle", getTaxi() == null ? 0 : getTaxi().getAngle())
				.add("reason", this.getOrder().getCancelReason());
		return jsonObj;
	}

	public JsonObjectBuilder getRiderInfo() {

		JsonObjectBuilder jsonObj = Json.createObjectBuilder();
		if (this.getRider() != null) {
			jsonObj = Json.createObjectBuilder().add("customer_id", getRider().getCustomer().getId())
					.add("name", getRider().getCustomer().getName() == null ? "" : getRider().getCustomer().getName())
					.add("phone_number", getRider().getCustomer().getPhoneNumber() == null ? "0"
							: getRider().getCustomer().getPhoneNumber());
		}
		return jsonObj;
	}

	public JsonObjectBuilder getTaxiInfo() {
		JsonObjectBuilder jsonObj = Json.createObjectBuilder();
		if (this.getTaxi() != null) {
			jsonObj = Json.createObjectBuilder().add("driver_id", driver.getId()).add("driver_name", driver.getName())
					.add("driver_phone", driver.getPhoneOffice() == null ? "0" : driver.getPhoneOffice())
					.add("rate", driver.getRate())
					.add("driver_avatar", new String(driver.getAvatar(), StandardCharsets.UTF_8))
					.add("vehicle_id", vehicle.getId()).add("licened_place", vehicle.getTaxiNumber())
					.add("taxi_number", vehicle.getValue()).add("car_type", vehicle.getCarType());
		}
		return jsonObj;
	}

	public void updateTripInfoToRider() {
		Rider rider = this.getRider();
		if (rider != null && rider.getTrip() != null && rider.getTrip().equals(this)) {
			rider.sendTripUpdate();
		}
	}

	public void updateTripInfoToDriver() {
		Taxi taxi = this.getTaxi();
		if (taxi != null) {
			taxi.sendTripUpdate();
		}
	}

	public void sendDriverInfo() {
		String cmd = UPDATE_DRIVER_INFO_CMD + getTaxiInfo().build() + MobileCommand.END_CMD;
		if (this.getRider() != null) {
			this.getRider().sendToDevice(cmd);
			// logger.debug("Return command: " + cmd);
		}
	}

	public void sendDriverInStartPositionNotify() {
		String cmd = DRIVER_IN_START_POSITION + "{}" + MobileCommand.END_CMD;
		if (this.getRider() != null) {
			this.getRider().sendToDevice(cmd);
			AppLogger.logDebug.debug("Return command: " + cmd);
		}

	}

	public void requestFails() {
		String cmd = Rider.CMD_REQUEST_TRIP_FAILS + "{\"trip_id\":\"" + this.getId() + "\"}" + MobileCommand.END_CMD;
		if (this.getRider() != null) {
			this.getRider().sendToDevice(cmd);
			AppLogger.logDebug.debug("Return command: " + cmd);
		}
	}

	/**
	 * Driver confirm trip by radio call center
	 */
	public void driverRegisterInDD(Taxi taxi) {
		this.driverConfirmTripFromDD(taxi);
	}

	/**
	 * Driver already pick up rider
	 * 
	 * @param taxi
	 */
	public void driverPickUpRider(Taxi taxi) {
		this.setTaxi(taxi);
		taxi.setTrip(this);
		sendDriverInfo();
		this.updateStatus(EnumStatus.XE_DA_DON.getValue(), 0);
		// Remove in queue arrangement 
		if(taxi.getArrangementTaxi() != null){
			taxi.goOutTaxiQueue();
		}
		//Update trip status to taxi
		//TODO: truong hop nhan khach qua dieu dam, CAN SUA LAI
//		if (!isNeedUpdate) {
			String dispatchType = ConfigUtil.getValueConfig("FIND_TAXI_TYPE", TripDispatcher.DISPATCH_TYPE_UNICAST);
			if (!dispatchType.equalsIgnoreCase(TripDispatcher.DISPATCH_TYPE_UNICAST) 
					&& !dispatchType.equalsIgnoreCase(TripDispatcher.DISPATCH_TYPE_MULTICAST)) {
				dispatchType = TripDispatcher.DISPATCH_TYPE_UNICAST;
			}
			
			JsonObjectBuilder tripJsonObj = Json.createObjectBuilder();
			JsonObjectBuilder riderJsonObj = Json.createObjectBuilder();
			
			tripJsonObj = this.getTripInfo();
			riderJsonObj = this.getRiderInfo();
			JsonObject json = Json.createObjectBuilder()
					.add("status", 0)
					.add("trip", tripJsonObj)
					.add("rider", riderJsonObj)
					.add("dispatch_type", dispatchType)
					.build();
			taxi.sendToDevice("$login_success=" + json + MobileCommand.END_CMD);
//		}
	}

	/**
	 * 
	 * @param taxi
	 */
	public void driverDoneTrip(Taxi taxi) {
		this.setTaxi(taxi);
		this.updateStatus(EnumStatus.TRA_KHACH.getValue(), 0);
	}
	
	/**
	 * Driver confirm trip from DD
	 * 
	 * @param taxi
	 */
	synchronized public void driverConfirmTripFromDD(Taxi taxi) {

			this.getOrder().setStatus(2); // Xe dang ky don
			this.setTaxi(taxi);
			if (getOrder().getIsAutoOperation()) {
				taxi.setTrip(this);
			}
			// Notify to rider
			if (rider != null && rider.getTrip() != null && rider.getTrip().equals(this)) {
				sendDriverInfo();
			}
			/*-- Send notify to smart phone app customer --*/
			pushNotifyToSmartPhone(2);

			/* -- send SMS to customer -- */
			String isSendSms = ConfigUtil.getValueConfig("SMS_NOTIFY_DRIVER_REGISTER",
					CommonDefine.SMS_NOTIFY_DRIVER_REGISTER);
			if (StringUtils.equals(isSendSms, CommonDefine.SMS_NOTIFY_DRIVER_REGISTER)) {
				String msg = StringUtils.getContentMsg(this.getOrder(), this.getTaxi(), 2);
				NotificationUtils.sendNotification(this.getOrder().getCustomer().getPhoneNumber(), this.getOrder().getCustomer().getEmail(),
						CommonDefine.TITLE_NOTIFY_CUSTOMER_DRIVER_REGISTER, msg);
			}
			order.getRegistedTaxis().add(taxi.getVehicle());
			saveToDataBase();
	}
	

	/**
	 * Driver confirm trip from App driver
	 * 
	 * @param taxi
	 */
	synchronized public void driverConfirmTrip(Taxi taxi) {

		int status = 0;
		Rider rider = this.getRider();
		String message = "";
		if (this.getStatus() == 0) {
			status = -2;
			message = "Rider has cancel this trip";
			this.setTaxi(null);
			taxi.setTrip(null);
		} else {
			if (getOrder().getIsAutoOperation() && this.getTaxi() == null) {
				this.getOrder().setStatus(2); // Xe dang ky don
				/*-- update lst driver to DB --*/
				updateListDriverToDB(taxi);
				/*-- end update --*/
				this.setTaxi(taxi);
				if (getOrder().getIsAutoOperation()) {
					taxi.setTrip(this);
				}
				// Notify to rider
				if (rider != null && rider.getTrip() != null && rider.getTrip().equals(this)) {
					sendDriverInfo();
				}
				/*-- Send notify to smart phone app customer --*/
				pushNotifyToSmartPhone(2);

				/* -- send SMS to customer -- */
				String isSendSms = ConfigUtil.getValueConfig("SMS_NOTIFY_DRIVER_REGISTER",
						CommonDefine.SMS_NOTIFY_DRIVER_REGISTER);
				if (StringUtils.equals(isSendSms, CommonDefine.SMS_NOTIFY_DRIVER_REGISTER)) {
					String msg = StringUtils.getContentMsg(this.getOrder(), this.getTaxi(), 2);
					if(this.getOrder().getCustomer() != null){
						NotificationUtils.sendNotification(this.getOrder().getCustomer().getPhoneNumber(), this.getOrder().getCustomer().getEmail(),
								CommonDefine.TITLE_NOTIFY_CUSTOMER_DRIVER_REGISTER, msg);
					}
				}
			} else {
				status = -1;
				message = "This trip have register driver already!";
				taxi.setTrip(null);
			}
		}
		JsonObject json = Json.createObjectBuilder().add("status", status).add("message", message).build();
		String cmd = CMD_ASK_CONFIRM_RETURN + json + MobileCommand.END_CMD;
		taxi.sendToDevice(cmd);
		if (this.getTripDispatcher() != null) {
			this.getTripDispatcher().cancel();
		}
		order.getRegistedTaxis().add(taxi.getVehicle());
		saveToDataBase();
		// Remove in queue arrangement 
		if(taxi.getArrangementTaxi() != null){
			taxi.goOutTaxiQueue();
		}
	}

	/**
	 * notification GCM to smart phone
	 */
	private void pushNotifyToSmartPhone(int status) {
		PushNotifyWorkder worker = new PushNotifyWorkder();
		worker.trip = this;
		worker.status = status;
		Thread thread = new Thread(worker);
		thread.start();
	}

	class PushNotifyWorkder implements Runnable {
		public Trip trip;
		public int status;

		@Override
		public void run() {
			try {
				String msg = StringUtils.getContentMsg(trip.getOrder(), trip.getTaxi(), status);
				/*-- for Android app --*/
				Content content = new Content();
				List<String> listReg = new LinkedList<String>();
				listReg.add(trip.getOrder().getCustomer().getRegId());
				content = Content.createContent(msg, listReg);
				String apiAndroid = ConfigUtil.getValueConfig("API_KEY_ANDROID", CommonDefine.API_KEY_ANDROID);
				if (StringUtils.isNotEmpty(apiAndroid)) {
					GCMUtils.sendToDevices(apiAndroid, content);
				}
				/*-- for IOS app --*/
				String apiIOS = ConfigUtil.getValueConfig("API_KEY_IOS", CommonDefine.API_KEY_IOS);
				if (StringUtils.isNotEmpty(apiIOS)) {
					GCMUtils.sendToDevicesIOS(apiIOS, trip.getOrder().getCustomer().getRegId(), msg);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public TripDispatcher getTripDispatcher() {
		return tripDispatcher;
	}

	public void setTripDispatcher(TripDispatcher tripDispatcher) {
		this.tripDispatcher = tripDispatcher;
	}

	public Taxi getTaxi() {
		if (taxi == null) {
			taxi = Taxi.getTaxi(getDriver(), getVehicle());
		}
		return taxi;
	}

	public void setTaxi(Taxi taxi) {
		this.taxi = taxi;
		if (taxi != null) {
			order.setDriver(taxi.getDriver());
			order.setVehicle(taxi.getVehicle());
			driver = taxi.getDriver();
			vehicle = taxi.getVehicle();
		} else {
			driver = null;
			vehicle = null;
			order.setDriver(null);
			order.setVehicle(null);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Trip))
			return false;
		Trip trip2 = (Trip) obj;

		return this.getId().equalsIgnoreCase(trip2.getId());
	}

	public String getRiderId() {
		return riderId;
	}

	private void setRiderId(String riderId) {
		this.riderId = riderId;
	}

	public Taxi getTaxiTmp() {
		return taxiTmp;
	}

	public void setTaxiTmp(Taxi taxiTmp) {
		this.taxiTmp = taxiTmp;
	}

	public Taxi getPostGuestTaxi() {
		return postGuestTaxi;
	}

	public void setPostGuestTaxi(Taxi postGuestTaxi) {
		this.postGuestTaxi = postGuestTaxi;
		this.getOrder().setDriverIntroduced(postGuestTaxi.getDriver());
	}

	public void notifyFromManualOperator(int status, Customer customer, Driver driver, Vehicle vehicle) {
		if (status == 2) {
			String contentMsg = "Tai xe: " + customer.getName() + ", So xe: " + vehicle.getTaxiNumber() + ", "
					+ " den don quy khach trong vong :  4 phut.";
			// Send SMS to Customer
			NotificationUtils.sendNotification(customer.getPhoneNumber(), customer.getEmail(), CommonDefine.TITLE_NOTIFY_CUSTOMER_DRIVER_REGISTER,
					contentMsg);
		}
	}

	/**
	 * Cancel trip
	 * 
	 * @param reason
	 *            : 1 - rider cancel trip >= 2 - driver cancel trip
	 * @return null if everything is ok, != null if something happen
	 */
	synchronized public String cancelTrip(int reason) {
		String retVal = null;
		if (reason == 1) { // Rider cancel request
			this.getOrder().setStatus(EnumStatus.HUY.getValue());
			this.getOrder().setCancelReason(reason);
			this.updateTripInfoToDriver();
			if(this.getTripDispatcher() != null){
				this.getTripDispatcher().cancel();
			}
			if (this.getTaxiTmp() != null) {
				this.getTaxiTmp().setTrip(null);
				this.getTaxiTmp().sendToDevice(RiderCancelRequestCommand.COMMAND_RETURN + "{}" + MobileCommand.END_CMD);
				AppLogger.logDebug.debug(
						"Return command: " + RiderCancelRequestCommand.COMMAND_RETURN + "{}" + MobileCommand.END_CMD);
			}
			this.saveToDataBase();
			// }
		} else { // Driver cancel request
			StringBuilder sb = new StringBuilder();
			sb.append("CancelTrip|Phone:").append(this.getOrder().getPhoneNumber());
			sb.append("|Driver:").append(taxi.getDriverName());
			sb.append("|Venhicle:").append(taxi.getVehicle().getFullValue());
			sb.append("|Status:Huy");
			sb.append("|TimeLog:").append(new Date());
			AppLogger.logDebug.info(sb.toString());
			taxi.setTrip(null);
			this.stopAutoOperation();
			// Save driver cancel trip
			DriverCancelOrderWorker thread = new DriverCancelOrderWorker(this.getOrder().getDriver().getId(), this.getOrder().getId(), reason);
			thread.run();
		}
		// update status vehicle on map
		if(this.getOrder().getVehicle() != null){
			VehicleStatusDD.updateStatusPickup(this.getOrder().getVehicle().getId());
		}
		if(this.getOrder().getRegistedTaxis() != null && this.getOrder().getRegistedTaxis().size() >0){
			for (Vehicle vehicle : this.getOrder().getRegistedTaxis()) {
				VehicleStatusDD.updateStatusPickup(vehicle.getId());
			}
		}
		return retVal;
	}

	/**
	 * 
	 * @param status
	 * @return
	 */
	synchronized public String updateStatus(int status, int reason) {
		String retVal = null;
		if (status > 0) {
			if (status != this.getStatus() + 1)
				retVal = "ERROR: status is illegal!";
		}
		if (status == EnumStatus.XE_DA_DON.getValue()) {
			this.getOrder().setBeginLat(new Double(getTaxi().getLattitute()));
			this.getOrder().setBeginLon(new Double(getTaxi().getLongitude()));
			if(this.getOrder().getPickedTaxi() == null){
				this.getOrder().setPickedTaxi(getTaxi().getVehicle());
			}
			//Update total order success
			int totalSuccess = this.getOrder().getCustomer().getTotalSuccessOrder();
			this.getOrder().getCustomer().setTotalSuccessOrder(totalSuccess + 1);
			this.getOrder().getCustomer().save();
			// update status vehicle on map
			if(this.getOrder().getVehicle() != null){
				VehicleStatusDD.updateStatusPickup(this.getOrder().getVehicle().getId());
			}
			// this.getOrder()
			// .setBeginAddress(AddressWebService.getAddressService(taxi.getLongitude(),
			// taxi.getLattitute()));
			pushNotifyToSmartPhone(3);
		}
		if (status == EnumStatus.TRA_KHACH.getValue()) {
			this.getOrder().setEndLat(new Double(getTaxi().getLattitute()));
			this.getOrder().setEndLon(new Double(getTaxi().getLongitude()));
			// this.getOrder()
			// .setEndAddress(AddressWebService.getAddressService(taxi.getLongitude(),
			// taxi.getLattitute()));
			pushNotifyToSmartPhone(4);
			// update status vehicle on map
			if(this.getOrder().getVehicle() != null){
				VehicleStatusDD.updateStatusPickup(this.getOrder().getVehicle().getId());
			}
		}

		this.getOrder().setStatus(status);
		if (status == EnumStatus.HUY.getValue() && reason > 0) {
			this.getOrder().setCancelReason(reason);
			if (this.getOrder().getIsAutoOperation()) {
				this.stopAutoOperation();
			}
		}
		// Return to driver
		String cmd = DriverUpdateTripStatusCommand.COMMAND + this.getTripInfo().build() + MobileCommand.END_CMD;
		taxi.sendToDevice(cmd);
		// update to rider
		if (this.getRider() != null && this.getRider().isConnect() && this.getRider().getTrip() != null
				&& this.getRider().getTrip().equals(this)) {
			cmd = Rider.CMD_TRIP_UPDATE + this.getTripInfo().build() + MobileCommand.END_CMD;
			this.getRider().sendToDevice(cmd);
		}
		if (status >= EnumStatus.TRA_KHACH.getValue() || status == EnumStatus.HUY.getValue()) {
			taxi.setTrip(null);
		}
//		if (isNeedSave) {
			saveToDataBase();
//		}

		return retVal;
	}

	/**
	 * Save taxi order to database
	 */
	synchronized public void saveToDataBaseSynched() {
		this.getOrder().save();
		TripManager.mapTrips.put(this.getId(), this);
		if (this.getOrder() != null) {
			if (Env.WEBAPP != null) { // In case create new order and send event only to DTV
				if(this.getOrder().getIsAutoOperation()){
					TaxiOrderMQ.pushCreated(new Event(TaxiOrderMQ.TAXI_ORDER_NEW_SAVED_EVENT, null, order));
				}else{
					TaxiOrderMQ.pushUpdated(new Event(TaxiOrderMQ.TAXI_ORDER_UPDATED_EVENT, null, order));
				}
		}
				
		}
	}

	/**
	 * 
	 */
	public void saveToDataBase() {
		Thread thread = new Thread(new PersitWorker(this));
		thread.run();
	}
	
	/**
	 * Stop auto operation => go to manual
	 */
	public void stopAutoOperation() {

		this.getOrder().getRegistedTaxis().clear();
		this.getOrder().setIsAutoOperation(false);
		this.getOrder().setStatus(EnumStatus.MOI.getValue());
		this.getOrder().save();

		if (Env.WEBAPP != null) {
			TaxiOrderMQ.pushUpdated(new Event(TaxiOrderMQ.TAXI_ORDER_UPDATED_EVENT, null, order));
		}
	}

	class PersitWorker implements Runnable {
		Trip trip = null;

		public PersitWorker(Trip trip) {
			this.trip = trip;
		}

		@Override
		public void run() {
			trip.saveToDataBaseSynched();
		}
	}
	
	/*-- Use for driver cancel Trip -- */
	class DriverCancelOrderWorker implements Runnable{
		private int driverId;
		private int tripId;
		private int reason;
		
		public DriverCancelOrderWorker(int driverId, int tripId, int reason){
			this.driverId = driverId;
			this.tripId = tripId;
			this.reason = reason;
		}
		@Override
		public void run() {
			DriverCancelTrip bean = new DriverCancelTrip();
			bean.setDriverId(driverId);
			bean.setOrderId(tripId);
			bean.setReason(reason);
			bean.setTimeCancel(new Timestamp(System.currentTimeMillis()));
			bean.save();
		}
	}
	

	@Override
	public void run() {
		if (taxi != null && !taxi.isConnect() && (this.getStatus() == EnumStatus.XE_DANG_KY_DON.getValue()
				|| this.getStatus() == EnumStatus.XE_DA_DON.getValue())) {
			taxi.updateLocationFromRDS();
			this.updateTripInfoToRider();
		}
		try {

			if (this.getStatus() == EnumStatus.HUY.getValue() || this.getStatus() == EnumStatus.TRA_KHACH.getValue()) {
				updateTimer.shutdownNow();
			}
		} catch (Exception e) {
			if (this.getOrder() == null) {
				AppLogger.logDebug.info("Trip nay co order null");
			}
		}
	}

	public void setRider(Rider rider) {
		this.setRiderId(rider.getRiderId());
		this.rider = rider;
	}

	/**
	 * @return the listDriver
	 */
	public ArrayList<Taxi> getListDriver() {
		return listDriver;
	}

	/**
	 * @param listDriver
	 *            the listDriver to set
	 */
	public void setListDriver(ArrayList<Taxi> listDriver) {
		this.listDriver = listDriver;
	}

	public boolean isInProgress() {
		boolean retVal = false;
		if (this.getOrder().getStatus() == EnumStatus.XE_DANG_KY_DON.getValue()
				|| this.getOrder().getStatus() == EnumStatus.XE_DA_DON.getValue()) {
			retVal = true;
		}
		return retVal;
	}
	private void updateListDriverToDB(Taxi taxi){
		try {
			DriverListAppFollowUpdateToDB thread = new DriverListAppFollowUpdateToDB(listDriver, order, order.getStatus(),taxi.getLongitude(),taxi.getLattitute());
			thread.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
