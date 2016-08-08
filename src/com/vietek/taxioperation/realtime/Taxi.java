package com.vietek.taxioperation.realtime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.EnumArrangementStatus;
import com.vietek.taxioperation.common.EnumCarTypeCommon;
import com.vietek.taxioperation.common.EnumOrderType;
import com.vietek.taxioperation.common.EnumStatus;
import com.vietek.taxioperation.common.ListCommon;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.model.ArrangementResult;
import com.vietek.taxioperation.model.ArrangementTaxi;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.DriverAppTracking;
import com.vietek.taxioperation.model.TaxiInTrip;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.model.VehicleInfoJson;
import com.vietek.taxioperation.realtime.command.AbstractCommand;
import com.vietek.taxioperation.realtime.command.MobileCommand;
import com.vietek.taxioperation.realtime.command.driver.DriverCancelRegisterMarketingPlace;
import com.vietek.taxioperation.realtime.command.driver.DriverCancelTripCommand;
import com.vietek.taxioperation.realtime.command.driver.DriverConfirmTrip;
import com.vietek.taxioperation.realtime.command.driver.DriverGetInfoMarketingPlace;
import com.vietek.taxioperation.realtime.command.driver.DriverInStartPositionCommand;
import com.vietek.taxioperation.realtime.command.driver.DriverPickUpGuestCommand;
import com.vietek.taxioperation.realtime.command.driver.DriverPostGuestCommand;
import com.vietek.taxioperation.realtime.command.driver.DriverRegisterMarketingPlace;
import com.vietek.taxioperation.realtime.command.driver.DriverUpdateLocationCommand;
import com.vietek.taxioperation.realtime.command.driver.DriverUpdateTripInfoCommand;
import com.vietek.taxioperation.realtime.command.driver.DriverUpdateTripStatusCommand;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.MapUtils;
import com.vietek.taxioperation.util.TaxiUtils;

public class Taxi extends RealtimeDevice {

	public static final String CMD_ASK_CONFIRM = "$ask_confirm_trip=";
	public static final String CMD_RIDER_CANCEL_TRIP = "$rider_cancel_trip=";
	public static final String CMD_TRIP_UPDATE = "$trip_update=";
	public static ConcurrentHashMap<String, Taxi> mapDriver = new ConcurrentHashMap<>();
	public static final List<Taxi> LST_TAXI_ONLINE = new ArrayList<>();
	public static final List<DriverAppTracking> LST_APPS_DRIVER_ONLINE = new ArrayList<>();
	public static final LinkedList<ArrayList<String>> LST_DRIVER_BY_HOUR = new LinkedList<>();

	static {
		for (int i = 0; i < 24; i++) {
			ArrayList<String> lstTmp = new ArrayList<>();
			LST_DRIVER_BY_HOUR.addLast(lstTmp);
		}
	}

	private Driver driver;
	private Vehicle vehicle;

	private double longitude;
	private double lattitute;
	private double angle; // rotate taxi icon

	private Timestamp lastUpdateTime;

	private Trip trip;
	private ArrangementTaxi arrangementTaxi = null;

	private ArrangementResult arrangementResult;

	public ArrangementResult getArrangementResult() {
		return arrangementResult;
	}

	public void setArrangementResult(ArrangementResult arrangementResult) {
		this.arrangementResult = arrangementResult;
	}

	public ArrangementTaxi getArrangementTaxi() {
		return arrangementTaxi;
	}

	public void setArrangementTaxi(ArrangementTaxi arrangementTaxi) {
		this.arrangementTaxi = arrangementTaxi;
	}

	public String getId() {
		return driver.getId() + "|" + vehicle.getId();
	}

	public String getDriverName() {
		if (this.driver == null)
			return "Do Vu";
		if (this.getDriver().getName() == null)
			return "Do Vu";
		return this.driver.getName();
	}

	public String getPhoneNumber() {
		if (this.driver == null)
			return "0988594179";
		if (this.getDriver().getPhoneOffice() == null)
			return "0988594179";
		return this.driver.getPhoneOffice();
	}

	public String getLicensePlate() {
		if (this.vehicle == null)
			return "CHUA KHAI BAO";
		return this.vehicle.getTaxiNumber();
	}

	public int getCarType() {
		if (this.vehicle == null)
			return 1;
		return this.vehicle.getTaxiType().getSeats().getId() == 23 ? 1 : 2;
	}

	public Taxi() {
		super();
	}

	// public static void addTaxi(Taxi taxi) {
	// mapDriver.put(taxi.getDriver().getId() + "|" + taxi.getVehicle().getId(),
	// taxi);
	// taxi.updateMapCellTaxi();
	//
	// }

	public static Taxi getTaxi(Driver driver, Vehicle vehicle) {
		if (driver == null || vehicle == null)
			return null;
		Taxi taxi = mapDriver.get(driver.getId() + "|" + vehicle.getId());
		if (taxi == null) {
			taxi = new Taxi();
			mapDriver.put(driver.getId() + "|" + vehicle.getId(), taxi);
		}
		taxi.driver = driver;
		taxi.vehicle = vehicle;
		return taxi;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;

		// Save to DB
		Session session = ControllerUtils.getCurrentSession();
		Query query = session.createQuery("from TaxiInTrip where taxiId = '" + this.getId() + "'");
		TaxiInTrip taxiInTrip = null;
		@SuppressWarnings("unchecked")
		List<TaxiInTrip> tmp = query.list();
		if (tmp.size() > 0) {
			taxiInTrip = tmp.get(0);
		} else {
			taxiInTrip = new TaxiInTrip();
			taxiInTrip.setTaxiId(this.getId());
		}
		session.close();

		if (trip == null) {
			taxiInTrip.setTripId(null);
		} else {
			taxiInTrip.setTripId(trip.getId());
		}
		taxiInTrip.save();
	}

	private boolean isRestoredFromDB = false;

	public void restoreTripFromDB() {
		if (isRestoredFromDB) {
			return;
		}
		isRestoredFromDB = true;
		if (this.getTrip() != null)
			return;
		Trip trip = null;
		Session session = ControllerUtils.getCurrentSession();
		Query query = session.createQuery("from TaxiInTrip where taxiId = '" + this.getId() + "'");
		TaxiInTrip taxiInTrip = null;
		@SuppressWarnings("unchecked")
		List<TaxiInTrip> tmp = query.list();
		if (tmp.size() > 0) {
			taxiInTrip = tmp.get(0);
		}
		session.close();
		if (taxiInTrip != null) {
			String tripId = taxiInTrip.getTripId();
			if (tripId != null && tripId.trim().length() > 0) {
				trip = TripManager.sharedInstance.getTrip(tripId);
			}
		}
		if (trip != null) {
			this.setTrip(trip);
		}
	}

	public void riderCancelTrip() {
		String cmd = CMD_RIDER_CANCEL_TRIP + "{}" + MobileCommand.END_CMD;
		sendToDevice(cmd);
	}

	public void updateLocation(double lati, double longi, double angle) {
		try {
			boolean isSameCell = this.isSameCell(this.getLongitude(), this.getLattitute(), longi, lati);
			if (!isSameCell) {
				this.updateMapCellTaxi(true);
			}
			this.setLattitute(lati);
			this.setLongitude(longi);
			this.setAngle(angle);
			this.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
			// TODO: Update to trip management
			if (this.getTrip() != null) {
				this.getTrip().updateTripInfoToRider();
			}
			if (!isSameCell) {
				this.updateMapCellTaxi(false);
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("Taxi|UpdateLocation|" + vehicle.getFullValue() + "|" + driver.toString(), e);
		}
	}

	public Timestamp getLastUpdateTime() {
		if (lastUpdateTime == null)
			lastUpdateTime = new Timestamp(0);
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void processCommand(String command) {
		AbstractCommand cmd = null;
		if (command.startsWith(DriverUpdateLocationCommand.COMMAND)) {
			cmd = new DriverUpdateLocationCommand();
		} else if (command.startsWith(DriverUpdateTripStatusCommand.COMMAND)) {
			cmd = new DriverUpdateTripStatusCommand();
		} else if (command.startsWith(DriverCancelTripCommand.COMMAND)) {
			cmd = new DriverCancelTripCommand();
		} else if (command.startsWith(DriverConfirmTrip.COMMAND)) {
			cmd = new DriverConfirmTrip();
		} else if (command.startsWith(DriverUpdateTripInfoCommand.COMMAND)) {
			cmd = new DriverUpdateTripInfoCommand();
		} else if (command.startsWith(DriverPostGuestCommand.COMMAND)) {
			cmd = new DriverPostGuestCommand();
		} else if (command.startsWith(DriverInStartPositionCommand.COMMAND)) {
			cmd = new DriverInStartPositionCommand();
		} else if (command.startsWith(DriverPickUpGuestCommand.COMMAND)) {
			cmd = new DriverPickUpGuestCommand();
		} else if (command.startsWith(DriverRegisterMarketingPlace.COMMAND)) {
			cmd = new DriverRegisterMarketingPlace();
		} else if (command.startsWith(DriverCancelRegisterMarketingPlace.COMMAND)) {
			cmd = new DriverCancelRegisterMarketingPlace();
		} else if (command.startsWith(DriverGetInfoMarketingPlace.COMMAND)) {
			cmd = new DriverGetInfoMarketingPlace();
		}
		if (cmd == null) {
			// getHandler().setDevice(null);
			return;
		}
		try {
			cmd.setDevice(this);
			cmd.setData(command);
			cmd.parseData();
			cmd.processData();
			this.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) {
			AppLogger.logDebug.error("[ERROR] Command-" + command, e);
		}
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public double getLongitude() {
		if (!this.isConnect()) {
			updateLocationFromRDS();
		}
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLattitute() {
		if (!this.isConnect()) {
			updateLocationFromRDS();
		}
		return lattitute;
	}

	public void setLattitute(double lattitute) {
		this.lattitute = lattitute;
	}

	public double getAngle() {
		if (!this.isConnect()) {
			updateLocationFromRDS();
		}
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void sendTripUpdate() {
		if (this.getTrip() == null)
			return;
		String cmd = CMD_TRIP_UPDATE + this.getTrip().getTripInfo().build() + MobileCommand.END_CMD;
		sendToDevice(cmd);
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (this.getDriver() != null || this.getVehicle() != null) {
			String curKey = driver.getId() + "|" + vehicle.getId();
			if (obj instanceof Taxi) {
				if (((Taxi) obj).getVehicle() != null && ((Taxi) obj).getDriver() != null) {
					String key = ((Taxi) obj).getDriver().getId() + "|" + ((Taxi) obj).getVehicle().getId();
					if (curKey.equals(key)) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	private Timestamp lastTimeUpdateLocationFromRDS = new Timestamp(0);

	public void updateLocationFromRDS() {
		Timestamp now = new Timestamp(System.currentTimeMillis() - 5000);
		if (now.after(lastTimeUpdateLocationFromRDS)) {
			lastTimeUpdateLocationFromRDS = new Timestamp(System.currentTimeMillis());
			VehicleInfoJson msgJson = TaxiUtils.getVehicleInfoFromRDS(vehicle.getId());
			if (msgJson != null) {
				this.longitude = msgJson.getLongitude();
				this.lattitute = msgJson.getLatitude();
				this.angle = msgJson.getAngle();
			}

		}
	}

	/**
	 * Neu isRemove = true thi xoa taxi khoi cell. Neu isRemove = false thi them
	 * taxi vao cell
	 * 
	 * @param isRemove
	 */
	private void updateMapCellTaxi(boolean isRemove) {
		if (isRemove) {
			CellMapTaxi.removeMapCell(this);
		} else {
			CellMapTaxi.addMapCell(this);
		}
	}

	private boolean isSameCell(double oldLongitude, double oldLatitude, double newLongitude, double newLatitude) {
		boolean result = false;
		int oldLonIndex = CellMapTaxi.getLatIndex(oldLatitude);
		int newLonIndex = CellMapTaxi.getLonIndex(newLongitude);
		if (oldLonIndex == newLonIndex) {
			int oldLatIndex = CellMapTaxi.getLatIndex(oldLatitude);
			int newLatIndex = CellMapTaxi.getLatIndex(newLatitude);
			if (oldLatIndex == newLatIndex) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Neu ko trong cuoc khach => Neu dang connect => Tao cuoc khach => Dong bo
	 * 
	 * @param trip_id
	 * @param shift_id
	 */
	public void onGPSEvent_TripStarted(int trip_id, int shift_id) {
		synchTripWithGPS(trip_id, shift_id);
		if (this.isConnect() && this.getTrip() == null) {
			this.pickupRider("", this.getLattitute(), this.getLongitude());
		}
	}

	public void onGPSEvent_TripEnd(int trip_id, int shift_id) {
		synchTripWithGPS(trip_id, shift_id);
		if (this.getTrip() != null) {
			this.getTrip().updateStatus(EnumStatus.TRA_KHACH.getValue(), 0);
		}
	}

	private void synchTripWithGPS(int trip_id, int shift_id) {
		AppLogger.logDebug.debug("TRIP_SYNCH: START....");
		if (this.getTrip() != null) {
			TaxiOrder order = this.getTrip().getOrder();
			order.setBlackBoxTripId(trip_id);
			order.setBlackBoxShipId(shift_id);
			order.save();
			AppLogger.logDebug.debug("TRIP_SYNCH: DONE - TaxiOrder_id = " + order.getId());
		} else {
			AppLogger.logDebug.debug("TRIP_SYNCH: FAILS");
		}
	}

	public void pickupRider(String address, double lat, double lon) {
		if (this.getTrip() != null) {
			return;
		}
		Rider rider = Rider.getRiderVL(null);
		TaxiOrder order = TaxiOrder.createNewTaxiOrder(null, address, lat, lon, "", 0, 0, "", null,
				EnumOrderType.APP_SMARTPHONE.getValue(), false);
		if (order != null) {
			Trip trip = new Trip();
			trip.setOrder(order);
			trip.setRider(rider);
			trip.driverPickUpRider(this);
			rider.setTrip(trip);
			JsonObjectBuilder tripJsonObj = Json.createObjectBuilder();
			JsonObjectBuilder riderJsonObj = Json.createObjectBuilder();
			if (this.getTrip() != null) {
				tripJsonObj = this.getTrip().getTripInfo();
				riderJsonObj = this.getTrip().getRiderInfo();
			}
			String dispatchType = ConfigUtil.getConfig("FIND_TAXI_TYPE", TripDispatcher.DISPATCH_TYPE_UNICAST);
			if (!dispatchType.equalsIgnoreCase(TripDispatcher.DISPATCH_TYPE_UNICAST)
					&& !dispatchType.equalsIgnoreCase(TripDispatcher.DISPATCH_TYPE_MULTICAST)) {
				dispatchType = TripDispatcher.DISPATCH_TYPE_UNICAST;
			}
			JsonObject json = Json.createObjectBuilder().add("status", 0).add("trip", tripJsonObj)
					.add("rider", riderJsonObj).add("dispatch_type", dispatchType).build();
			this.getHandler().getCtx().writeAndFlush("$login_success=" + json + MobileCommand.END_CMD);
		}
	}

	public static ArrayList<Taxi> getListTaxiByType(int cartype, int number, double longitude, double latitude) {
		ArrayList<Taxi> lst = new ArrayList<Taxi>();
		ArrangementTaxi arrangementTaxi = findMarketingPlace(longitude, latitude);
		if (arrangementTaxi == null) {
			return null;
		}
		// Check out of range arrangement taxi and status connect
		if (MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId()) != null
				&& MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId()).size() > 0) {
			for (Taxi taxi : MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId())) {
				ArrangementTaxi temp = findMarketingPlace(taxi.longitude, taxi.lattitute);
				if (!arrangementTaxi.equals(temp) || !taxi.isConnect()) {
					MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId()).remove(taxi);
				}
			}
		}
		if (MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId()) != null
				&& MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId()).size() > 0) {
			int i = 0;
			for (Taxi taxi : MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId())) {
				// In case car type is any
				if (cartype == EnumCarTypeCommon.ALL.getValue()) {
					if (taxi.isConnect()) {
						i++;
						if (i <= number) {
							lst.add(taxi);
						} else {
							break;
						}
					}
				} else {
					if (taxi.getVehicle().getCarType() == cartype && taxi.isConnect()) {
						i++;
						if (i <= number) {
							lst.add(taxi);
						} else {
							break;
						}
					}
				}
			}
		}
		return lst;
	}

	/**
	 * Xu ly khi tai xe dang ky diem sap tai
	 * 
	 * @param marketingPlace
	 */
	@SuppressWarnings("unused")
	private void goToTaxiQueue(ArrangementTaxi marketingPlace) {
		AppLogger.logDebug.debug("GoToTaxiQueue|Driver" + driver + "|Vehicle:" + vehicle + "|MarketingPlace:" + marketingPlace);
		if (marketingPlace != null) {
			if (MapCommon.MAP_TAXI_LIST.get(marketingPlace.getId()) != null) {
				if (MapCommon.MAP_TAXI_LIST.get(marketingPlace.getId()).size() < marketingPlace.getMaxCar()) {
					MapCommon.MAP_TAXI_LIST.get(marketingPlace.getId()).addLast(this);
				}
			}
		}
	}

	/**
	 * Xu ly khi tai xe ra khoi diem sap tai
	 * 
	 * @param marketingPlace
	 */
	public void goOutTaxiQueue() {
		AppLogger.logDebug.debug("GoOutTaxiQueue|Driver" + driver + "|Vehicle:" + vehicle + "|MarketingPlace:" + arrangementTaxi);
		if (this.arrangementTaxi != null) {
			arrangementTaxi = (ArrangementTaxi) MapCommon.MAP_ARRANGEMENT.get(arrangementTaxi.getId() + "");
			if (arrangementTaxi != null) {
				List<Taxi> lstTmp = MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId());
				if (lstTmp != null) {
					if (lstTmp.size() <= arrangementTaxi.getMaxCar()) {
						int curIndex = lstTmp.indexOf(this);
						boolean isRemove = lstTmp.remove(this);
						if (isRemove) {
							this.arrangementTaxi = null;
							this.arrangementResult = null;
							sendNotifyOtherTaxi();
							for (int i = curIndex; i < lstTmp.size(); i++) {
								// TODO: Gui thong tin thay doi index cua cac
								// tai xe
								// khac
								lstTmp.get(i).driverGetMarketingPlaceInfo();
							}
						}
					}
				}

			}
		}
	}

	private void sendNotifyOtherTaxi() {
		JsonObjectBuilder jsonObj = Json.createObjectBuilder().add("status", "success");
		String cmd = DriverCancelRegisterMarketingPlace.COMMAND + jsonObj.build() + MobileCommand.END_CMD;
		this.sendToDevice(cmd);
	}

	public static ArrangementTaxi findMarketingPlace(double longitude, double latitude) {
		ArrangementTaxi result = null;
		for (ArrangementTaxi marketingPlace : ListCommon.LIST_ARRANGAMENT_PLACE) {
			double distance = MapUtils.distance(longitude, latitude, marketingPlace.getLongitude(),
					marketingPlace.getLatitude());
			if (distance < marketingPlace.getRadius()) {
				result = marketingPlace;
				break;
			}
		}
		return result;
	}

	public ArrangementResult registerArrangementPlace(double longitude, double latitude) {
		arrangementResult = new ArrangementResult();
		ArrangementTaxi arrangementTaxi = findMarketingPlace(longitude, latitude);
		if (arrangementTaxi != null) {
			if (MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId()) != null) {
				LinkedList<Taxi> lstTmp = MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId());
				if (lstTmp.contains(this)) {
					arrangementResult.setStatus(EnumArrangementStatus.FALSE.getVALUE());
				} else {
					if (lstTmp.size() < arrangementTaxi.getMaxCar()) {
						if (!lstTmp.contains(this)) {
							lstTmp.addLast(this);
							int sequence = MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId()).size();
							arrangementResult.setArrangamentTaxi(arrangementTaxi);
							arrangementResult.setSequence(sequence);
							arrangementResult.setStatus(EnumArrangementStatus.THANH_CONG.getVALUE());
							this.arrangementTaxi = arrangementTaxi;
						} else {
							arrangementResult.setStatus(EnumArrangementStatus.FALSE.getVALUE());
						}
					} else {
						arrangementResult.setArrangamentTaxi(arrangementTaxi);
						arrangementResult.setStatus(EnumArrangementStatus.QUE_DAY.getVALUE());
					}
				}
			} else {
				arrangementResult.setArrangamentTaxi(arrangementTaxi);
				arrangementResult.setStatus(EnumArrangementStatus.FALSE.getVALUE());
			}
		} else {
			arrangementResult.setStatus(EnumArrangementStatus.NGOAI_VUNG.getVALUE());
		}
		// send return to driver
		String cmd = DriverRegisterMarketingPlace.COMMAND + this.getMarketingPlaceInfo().build()
				+ MobileCommand.END_CMD;
		this.sendToDevice(cmd);
		AppLogger.logDebug.debug(
				"RegisterTaxiQueue|Driver" + driver + "|Vehicle:" + vehicle + "|MarketingPlace:" + arrangementTaxi);
		return arrangementResult;
	}

	public int getSequenceArrangement() {
		int result = -1;
		if (arrangementTaxi != null) {
			LinkedList<Taxi> lstTmp = MapCommon.MAP_TAXI_LIST.get(arrangementTaxi.getId());
			for (int i = 0; i < lstTmp.size(); i++) {
				Taxi tmp = lstTmp.get(i);
				if (tmp.getId().equals(this.getId())) {
					result = i + 1;
					break;
				}
			}
		}

		return result;
	}

	public void driverGetMarketingPlaceInfo() {
		String cmd = DriverGetInfoMarketingPlace.COMMAND + this.getMarketingPlaceInfo().build() + MobileCommand.END_CMD;
		this.sendToDevice(cmd);
	}

	public JsonObjectBuilder getMarketingPlaceInfo() {
		JsonObjectBuilder jsonObj = null;
		if (this.arrangementTaxi == null) {
			jsonObj = Json.createObjectBuilder()
					.add("status", this.getArrangementResult() == null ? -1 : this.getArrangementResult().getStatus())
					.add("sequence", "").add("address", "");
		} else {
			jsonObj = Json.createObjectBuilder().add("status", this.getArrangementResult().getStatus())
					.add("sequence", this.getSequenceArrangement()).add("address", arrangementTaxi.getAddress());
		}
		return jsonObj;
	}
}
