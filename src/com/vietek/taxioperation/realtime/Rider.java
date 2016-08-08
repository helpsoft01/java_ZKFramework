package com.vietek.taxioperation.realtime;

import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.NewCookie;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.controller.CustomerController;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.realtime.command.AbstractCommand;
import com.vietek.taxioperation.realtime.command.MobileCommand;
import com.vietek.taxioperation.realtime.command.rider.RiderCancelRequestCommand;
import com.vietek.taxioperation.realtime.command.rider.RiderRatingCommand;
import com.vietek.taxioperation.realtime.command.rider.RiderRequestTrackingCommand;
import com.vietek.taxioperation.realtime.command.rider.RiderRequestTripCommand;
import com.vietek.taxioperation.realtime.command.rider.RiderUpdateCarTypeCommand;
import com.vietek.taxioperation.realtime.command.rider.RiderUpdateLocationCommand;
import com.vietek.taxioperation.realtime.command.rider.RiderUpdateTripInfoCommand;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.MapUtils;
import com.vietek.taxioperation.util.TaxiUtils;

public class Rider extends RealtimeDevice implements Runnable {
	private String riderId;

	public static final String CMD_UPDATE_DRIVER_INFO = "$updateDriverInfo=";
	public static final String CMD_REQUEST_TRIP_FAILS = "$requestTripFails=";
	public static final String CMD_REQUEST_TRIP_SUCCESS = "$requestTripSuccess=";
	public static final String CMD_UPDATE_TRIP_STATE = "$updateTripState=";
	public static final String CMD_TRIP_UPDATE = "$trip_update=";
	public static final String KHACH_VANG_LAI = "KVL";

	private int MAX_DISTANCE_VIEW = ConfigUtil.getValueConfig("MAX_DISTANCE_VIEW", CommonDefine.MAX_DISTANCE_VIEW); // 1km
	public static ConcurrentHashMap<String, Rider> mapRider = new ConcurrentHashMap<>();
	static String STATUS_WAIT = "wait";

	/**
	 * Luu danh sach rider one line
	 */
	public static List<Rider> lstRiderOnline = new ArrayList<>();
	public static LinkedList<ArrayList<String>> lstCustomerByHour = new LinkedList<ArrayList<String>>();

	private static ScheduledThreadPoolExecutor updateTaxiInfoSchedule;
	// private Timer riderUpdateTimer;

	private double longtitute = 0;
	private double lattitute = 0;

	private Customer customer;
	private Trip trip;

	private int carType;

	public Rider(Customer customer, String id) {
		super();
		this.customer = customer;
		this.setRiderId(id);
		// riderUpdateTimer = new Timer("Timer for rider: "
		// + customer.getPhoneNumber(), true);
		// riderUpdateTimer.scheduleAtFixedRate(new UpdateTask(), 0, 2 * 1000);
	}

	static {
		for (int i = 0; i < 24; i++) {
			ArrayList<String> lstTmp = new ArrayList<>();
			lstCustomerByHour.addLast(lstTmp);
		}
	}

	public double getLongtitute() {
		return longtitute;
	}

	public void setLongtitute(double longtitute) {
		this.longtitute = longtitute;
	}

	public double getLattitute() {
		return lattitute;
	}

	public void setLattitute(double lattitute) {
		this.lattitute = lattitute;
	}

	public void updateLocation(double longitude, double latitude) {
		updateMapCellTaxi(true);
		this.setLongtitute(longitude);
		this.setLattitute(latitude);
		updateMapCellTaxi(false);
	}

	private void updateMapCellTaxi(boolean isRemove) {
		if (isRemove) {
			CellMapRider.removeMapCell(this);
		} else {
			CellMapRider.addMapCell(this);
		}
	}

	public static void addRider(Rider rider) {
		if (updateTaxiInfoSchedule == null) {
			updateTaxiInfoSchedule = new ScheduledThreadPoolExecutor(5);
			updateTaxiInfoSchedule.scheduleAtFixedRate(new UpdateWorker(), 0, 5, TimeUnit.SECONDS);
		}
		mapRider.put(rider.getRiderId(), rider);
	}

	public static Rider getRider(String phoneNumber) {
		Rider rider = mapRider.get(phoneNumber);

		if (rider == null) {
			Customer customer = CustomerController.getOrCreateCustomer(phoneNumber);
			if (!(customer.getId() > 0))
				customer.save();
			rider = new Rider(customer, phoneNumber);
			Rider.addRider(rider);
		} else {
			rider.setCustomer(CustomerController.getCustomer(phoneNumber));
		}
		return rider;
	}

	public static void removeRider(Rider rider) {
		mapRider.remove(rider.getCustomer().getId());
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public void sendTripUpdate() {
		if (this.getTrip() == null)
			return;
		String cmd = CMD_TRIP_UPDATE + this.getTrip().getTripInfo().build() + MobileCommand.END_CMD;
		sendToDevice(cmd);
		AppLogger.logDebug.debug("Return command: " + cmd);
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	private static Timestamp lastTimeInitRequest = new Timestamp(0);
	private static List<NewCookie> coockies = null;
	private static Client client = null;
	private JsonArray listCar;
	private Client rdsClient = null;

	private static boolean isShowOpen99Driver() {
		boolean isShow = ConfigUtil.getValueConfig("SHOW_OPEN99", CommonDefine.SHOW_OPEN99) >= 1;
		return isShow;
	}

	public void updateInfoToRider() {
		if (!this.isConnect())
			return;
		// trip == null => wait screen
		try {
			JsonArrayBuilder drivers = Json.createArrayBuilder();
			int waitingTime = 0;
			if (this.trip == null) {
				ArrayList<Taxi> lstDriver = (ArrayList<Taxi>) TaxiUtils.getAvaiableTaxiFilter(longtitute, lattitute, carType, MAX_DISTANCE_VIEW, 10, new ArrayList<Taxi>());
				for (int i = 0; i < lstDriver.size(); i++) {
					Taxi driver = lstDriver.get(i);

					double distance = MapUtils.distance(driver.getLongitude(), driver.getLattitute(), longtitute,
							lattitute);

					if (waitingTime == 0 || (waitingTime <= 0 || waitingTime > (int) (distance / 500))) {
						waitingTime = (int) (distance / 500);
						waitingTime++;
					}
					JsonObject jsonDriver = getDriverObj(driver);
					if (jsonDriver != null) {
						drivers.add(jsonDriver);
					}
				}
				// Webservice
				String rds = ConfigUtil.getConfig("RDS_URL") + "taxi/avaiable?longitude=" + this.getLongtitute()
						+ "&latitude=" + this.getLattitute() + "&ranger=" + MAX_DISTANCE_VIEW + "&numvehicle=" + 10
						+ "&cartype=" + this.getCarType();
				if (rdsClient == null)
					rdsClient = Client.create();
				WebResource rdsResource = rdsClient.resource(rds);
				ClientResponse rdsResponse = rdsResource.get(ClientResponse.class);
				if (rdsResponse.getStatus() == 200) {
					String ret = rdsResponse.getEntity(String.class);
					rdsResponse.close();
					JsonReader reader = Json.createReader(new StringReader(ret));
					JsonArray arrayTaxiJson = reader.readArray();
					for (int i = 0; i < arrayTaxiJson.size(); i++) {
						try {
							JsonObject jsonDriver = getDriverObjFromRDS(arrayTaxiJson.getJsonObject(i));
							boolean isDriverUsingApp = false;
							for (int j = 0; j < lstDriver.size(); j++) {
								if (lstDriver.get(j).getLastUpdateTime()
										.before(new Timestamp(System.currentTimeMillis() - 10 * 1000)))
									continue;
								if (jsonDriver.getInt("driverId") == lstDriver.get(j).getDriver().getId()) {
									isDriverUsingApp = true;
									break;
								}
							}
							if (isDriverUsingApp)
								continue;
							if (i == 0) {
								double distance = MapUtils.distance(jsonDriver.getJsonNumber("long").doubleValue(),
										jsonDriver.getJsonNumber("lat").doubleValue(), longtitute, lattitute);
								if (waitingTime <= 0 || waitingTime > (int) (distance / 500)) {
									waitingTime = (int) (distance / 500);
									waitingTime++;
								}
							}
							if (jsonDriver != null) {
								drivers.add(jsonDriver);
							}
						} catch (Exception e) {
							// ignore
						}
					}
				}
				if (isShowOpen99Driver()) {
					// CNS web service
					if (lastTimeInitRequest.before(new Timestamp(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
							|| coockies == null) {
						initCNSWebService();
					}

					String type = "all";
					if (this.getCarType() == 1) {
						type = "SEATS4";
					} else if (this.getCarType() == 2) {
						type = "SEATS7";
					}
					String postURL = "https://api.open99.vn/vietek/taxi";
					WebResource webResource = client.resource(postURL);
					JsonObject data = Json.createObjectBuilder().add("serviceName", "GET_LIST_VEHICLE")
							.add("latitude", this.getLattitute() + "").add("radius", MAX_DISTANCE_VIEW + "")
							.add("type", type).add("longitude", this.getLongtitute() + "").build();
					WebResource.Builder builder = webResource.getRequestBuilder();
					for (NewCookie c : coockies) {
						builder = builder.cookie(c);
					}
					ClientResponse response = builder.post(ClientResponse.class, data.toString());
					if (response.getStatus() == 200) {
						String ret = response.getEntity(String.class);
						response.close();
						JsonReader reader = Json.createReader(new StringReader(ret));
						JsonObject jsonObj = reader.readObject();
						if (jsonObj.getJsonObject("error").getInt("code") == 1000) {
							listCar = jsonObj.getJsonArray("vehicleList");
						} else {
							initCNSWebService();
						}
					}
					if (this.getCustomer().getPhoneNumber().equalsIgnoreCase("0945868885")) {
						AppLogger.logDebug.info("Dear ME!!!");
						AppLogger.logDebug.info(listCar);
					}

					if (listCar != null) {
						for (int i = 0; i < listCar.size(); i++) {
							JsonObject car = listCar.getJsonObject(i);
							JsonObject jsonDriver = getDriverObj(car);
							if (jsonDriver != null) {
								drivers.add(jsonDriver);
								double newLat = Double.parseDouble(car.getString("latitude"));
								double newLong = Double.parseDouble(car.getString("longitude"));
								double distance = MapUtils.distance(newLong, newLat, longtitute, lattitute);
								if (waitingTime <= 0 || (waitingTime <= 0 || waitingTime > (int) (distance / 500))) {
									waitingTime = (int) (distance / 500);
									waitingTime++;
								}
							}
						}
					}
				}
				updateDriversInfo(drivers.build(), waitingTime);
			}
		} catch (Exception e) {
			// ignore
			e.printStackTrace();
		}
	}

	static private void initCNSWebService() {
		ClientConfig clientConfig = new DefaultClientConfig();
		client = Client.create(clientConfig);
		String postURL = "https://api.open99.vn/vietek/init";
		WebResource webResource = client.resource(postURL);
		JsonObject data = Json.createObjectBuilder().add("appId", "vietek")
				.add("secretKey", "0487ff61-e32e-4724-891a-e27111d7e6d1").build();
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, data.toString());
		if (response.getStatus() == 200) {
			coockies = response.getCookies();
			lastTimeInitRequest = new Timestamp(System.currentTimeMillis());
		}
	}

	static private ConcurrentHashMap<String, JsonObject> cnsCarMap = new ConcurrentHashMap<>();

	static private double getAngle(JsonObject driver) {
		double retVal = 0.0;
		if (cnsCarMap.containsKey(driver.getString("carNo"))) {
			JsonObject oldPosition = cnsCarMap.get(driver.getString("carNo"));
			double oldLat = Double.parseDouble(oldPosition.getString("latitude"));
			double oldLong = Double.parseDouble(oldPosition.getString("longitude"));
			double newLat = Double.parseDouble(driver.getString("latitude"));
			double newLong = Double.parseDouble(driver.getString("longitude"));
			retVal = getBearing2Point(oldLat, oldLong, newLat, newLong);
		}
		cnsCarMap.put(driver.getString("carNo"), driver);
		return retVal;
	}

	/**
	 * Tinh goc bearing cua hai diem. Tinh theo do chinh xac tuyet doi voi he
	 * toa do cau
	 *
	 * @author VuD
	 * @param latitude1
	 * @param longitude1
	 * @param latitude2
	 * @param longitude2
	 * @return Goc bearing cua hai diem
	 */
	public static double getBearing2Point(double latitude1, double longitude1, double latitude2, double longitude2) {
		double lat1 = latitude1 * deg2Rad;
		double lon1 = longitude1 * deg2Rad;
		double lat2 = latitude2 * deg2Rad;
		double lon2 = longitude2 * deg2Rad;
		double deltaLon = lon2 - lon1;
		double x = Math.cos(lat2) * Math.sin(deltaLon);
		double y = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLon);
		double b = Math.atan2(x, y);
		return ((b * rad2Deg) + 360) % 360;
	}

	/**
	 * He do chuyen doi tu do sang radian
	 */
	public static final double deg2Rad = Math.PI / 180d;

	/**
	 * He so chuyen doi tu radian sang do
	 */
	public static final double rad2Deg = 180d / Math.PI;

	private JsonObject getDriverObj(JsonObject driver) {
		try {
			Integer carType = 1;
			if (!driver.getString("type").equalsIgnoreCase("SEATS4")) {
				carType = 2;
			}
			JsonObject jsonObj = Json.createObjectBuilder().add("driverId", driver.getString("mobile").hashCode())
					.add("driverName", driver.getString("fullName")).add("taxiNumber", driver.getString("carNo"))
					.add("phoneNumber", driver.getString("mobile")).add("licensePlate", driver.getString("carNo"))
					.add("carType", carType).add("long", Double.parseDouble(driver.getString("longitude")))
					.add("lat", Double.parseDouble(driver.getString("latitude"))).add("angle", Rider.getAngle(driver))
					.add("rate", (int) Double.parseDouble(driver.getString("rating"))).build();
			return jsonObj;
		} catch (Exception e) {
			AppLogger.logDebug.error("[ERROR] - " + e);
		}
		return null;
	}

	private JsonObject getDriverObjFromRDS(JsonObject driver) {
		try {
			JsonObject jsonObj = Json.createObjectBuilder().add("driverId", driver.getInt("driverId"))
					.add("driverName", driver.getString("driverName"))
					.add("taxiNumber", driver.getString("vehicleNumber"))
					.add("phoneNumber",
							driver.getString("phoneOffcice") == null ? "0" : driver.getString("phoneOffcice"))
					.add("licensePlate", driver.getString("licensePlace")).add("carType", driver.getInt("carType"))
					.add("long", driver.getJsonNumber("longitude").doubleValue())
					.add("lat", driver.getJsonNumber("latitude").doubleValue())
					.add("angle", driver.getJsonNumber("angle").doubleValue())
					.add("rate", driver.getJsonNumber("rate").doubleValue()).build();
			return jsonObj;
		} catch (Exception e) {
			AppLogger.logDebug.error("[ERROR] - " + e);
			AppLogger.logDebug.error(driver.toString());
		}
		return null;
	}

	private void updateDriversInfo(JsonArray drivers, int waitingTime) {
		JsonObject jsonObj = Json.createObjectBuilder().add("drivers", drivers).add("time_estimate", waitingTime)
				.build();
		String cmd = CMD_UPDATE_DRIVER_INFO + jsonObj + MobileCommand.END_CMD;
		sendToDevice(cmd);
	}

	// private JsonObject getDriverObj(TaxiResult driver) {
	// if (Integer.parseInt(driver.getTypeName()) != this.getCarType()
	// && this.getCarType() > 0)
	// return null;
	// try {
	// JsonObject jsonObj = Json.createObjectBuilder()
	// .add("driverId", driver.getId())
	// .add("driverName", driver.getDriverName())
	// .add("taxiNumber", driver.getVehicleNumber())
	// .add("phoneNumber", driver.getMobileNumber())
	// .add("licensePlate", driver.getLicensePlace())
	// .add("carType", Integer.parseInt(driver.getTypeName()))
	// .add("long", driver.getLongitude())
	// .add("lat", driver.getLatitude())
	// .add("angle", driver.getAngle()).add("rate", 5).build();
	// return jsonObj;
	// } catch (Exception e) {
	// logger.error("[ERROR] - " + e);
	// }
	// return null;
	// }

	private JsonObject getDriverObj(Taxi driver) {
		try {
			if (driver.getCarType() != this.getCarType() && this.getCarType() > 0)
				return null;
			JsonObject jsonObj = Json.createObjectBuilder().add("driverId", driver.getDriver().getId())
					.add("driverName", driver.getDriverName()).add("taxiNumber", driver.getVehicle().getValue())
					.add("phoneNumber", driver.getPhoneNumber()).add("licensePlate", driver.getLicensePlate())
					.add("carType", driver.getCarType()).add("long", driver.getLongitude())
					.add("lat", driver.getLattitute()).add("angle", driver.getAngle())
					.add("rate", driver.getDriver().getRate()).build();
			return jsonObj;
		} catch (Exception e) {
			AppLogger.logDebug.error("[ERROR] - " + e);
		}
		return null;
	}

	public void processCommand(String command) {
		AbstractCommand cmd = null;
		if (command.startsWith(RiderUpdateLocationCommand.COMMAND)) {
			cmd = new RiderUpdateLocationCommand();
		} else if (command.startsWith(RiderRequestTripCommand.COMMAND)) {
			cmd = new RiderRequestTripCommand();
		} else if (command.startsWith(RiderUpdateCarTypeCommand.COMMAND)) {
			cmd = new RiderUpdateCarTypeCommand();
		} else if (command.startsWith(RiderRequestTrackingCommand.COMMAND)) {
			cmd = new RiderRequestTrackingCommand();
		} else if (command.startsWith(RiderCancelRequestCommand.COMMAND)) {
			cmd = new RiderCancelRequestCommand();
		} else if (command.startsWith(RiderRatingCommand.COMMAND)) {
			cmd = new RiderRatingCommand();
		} else if (command.startsWith(RiderUpdateTripInfoCommand.COMMAND)) {
			cmd = new RiderUpdateTripInfoCommand();
		}
		if (cmd == null)
			return;
		try {
			cmd.setDevice(this);
			cmd.setData(command);
			cmd.parseData();
			cmd.processData();
		} catch (Exception e) {
			AppLogger.logDebug.error("[ERROR] Command-" + command + ":" + e);
		}
	}

	public String getRiderId() {
		return riderId;
	}

	public void setRiderId(String riderId) {
		this.riderId = riderId;
	}

	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

	@Override
	public void run() {
		this.updateInfoToRider();
	}

	class UpdateTask extends TimerTask {

		@Override
		public void run() {
			updateInfoToRider();
		}

	}

	/**
	 * Rider vang lai
	 * 
	 * @param taxi
	 * @return
	 */
	public static Rider getRiderVL(Driver driver) {
		Rider rider = null;
		if (driver == null) {
			Customer guest = new Customer();
			guest.setId(0);
			guest.setName("Khách vãng lai ");
			guest.setPhoneNumber("0");
			rider = new Rider(guest, Rider.KHACH_VANG_LAI);
		} else {
			Customer guest = new Customer();
			guest.setId(0);
			guest.setName("Khach vang lai - Tai xe bao: " + driver.getName() + " ");
			guest.setPhoneNumber(driver.getPhoneOffice());
			rider = new Rider(guest, Rider.KHACH_VANG_LAI);
		}
		return rider;
	}

	public static ArrayList<Rider> getAvaiableRider(final double longitude, final double latitude, int maxDistance) {
		try {

			ArrayList<Rider> lstResult = new ArrayList<>();
			ArrayList<Rider> lstTmp = new ArrayList<>();
			int numCell = ((int) (maxDistance / CellMapTaxi.RANGER_CELL));
			int centerLon = CellMapTaxi.getLonIndex(longitude);
			int centerLat = CellMapTaxi.getLatIndex(latitude);
			// Taxi.getListTaxiNearest(lstResult, centerLon, centerLat, 0,
			// carType,
			// maxNumber, numCell);
			Rider.getListRiderNearest(lstTmp, centerLon, centerLat, 0, numCell, false);
			for (Rider taxi : lstTmp) {
				if (!lstResult.contains(taxi)) {
					lstResult.add(taxi);
				}
			}
			if (lstResult.size() != lstTmp.size()) {
				
			}
			Collections.sort(lstResult, new Comparator<Rider>() {
				@Override
				public int compare(Rider o1, Rider o2) {
					double distance1 = MapUtils.distance(o1.getLongtitute(), o1.getLattitute(), longitude, latitude);
					double distance2 = MapUtils.distance(o2.getLongtitute(), o2.getLattitute(), longitude, latitude);
					if (distance1 < distance2)
						return -1;
					else if (distance1 > distance2)
						return 1;
					else
						return 0;
				}
			});
			return lstResult;
		} catch (Exception e) {
			AppLogger.logDebug.error("GetAvaiableTaxi|Longitude:" + longitude + "|Latitude:" + latitude, e);
			return new ArrayList<Rider>();
		}
	}

	private static void getListRiderNearest(List<Rider> lstModel, int centerLon, int centerLat, int delta, int numCell,
			boolean isfinal) {
		for (int i = centerLon - delta; i <= centerLon + delta; i++) {
			if (i < 0 || i >= CellMapTaxi.LON_INDEX_RANGER) {
				continue;
			}
			for (int j = centerLat - delta; j <= centerLat + delta; j++) {
				if (j < 0 || j >= CellMapTaxi.LAT_INDEX_RANGER) {
					continue;
				}
				if (isfinal) {
					if (Math.abs(i - centerLon) == delta && Math.abs(j - centerLat) == delta) {
						continue;
					}
				}
				if (Math.abs(i - centerLon) == delta || Math.abs(j - centerLat) == delta) {
					List<Rider> lstTmp = CellMapRider.getListVehicleNearest(i, j);
					lstModel.addAll(filterListRider(lstTmp));
				}
			}
		}
		if (delta <= numCell) {
			if (delta == numCell) {
				Rider.getListRiderNearest(lstModel, centerLon, centerLat, delta + 1, numCell, true);
			} else {
				Rider.getListRiderNearest(lstModel, centerLon, centerLat, delta + 1, numCell, false);
			}
		}
	}

	private static List<Rider> filterListRider(List<Rider> lstTaxi) {
		List<Rider> lstResult = new ArrayList<>();
		for (Rider taxi : lstTaxi) {
			if (taxi.isConnect() && taxi.getTrip() == null) {
				lstResult.add(taxi);
			}
		}
		return lstResult;
	}
}

class UpdateWorker extends TimerTask {
	@Override
	public void run() {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Iterator<String> it = Rider.mapRider.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Rider rider = Rider.mapRider.get(key);
			if (!rider.isConnect()) {
				it.remove();
			}
			else {
				executor.execute(rider);
			}
		}
		
		executor.shutdown();
	}
}
