package com.vietek.taxioperation.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.EnumCarTypeCommon;
import com.vietek.taxioperation.model.VehicleInfoJson;
import com.vietek.taxioperation.realtime.CellMapTaxi;
import com.vietek.taxioperation.realtime.Taxi;

public class TaxiUtils {

	/**
	 * Lay danh sach xe dang online bang app o gan voi nhung xe khong co trong
	 * list filter
	 * 
	 * @author VuD
	 * @param longitude
	 * @param latitude
	 * @param carType
	 *            <li>4 cho: 1
	 *            <li>7 cho: 2
	 *            <li>bat ky:0
	 * @param maxDistance
	 * @param maxNumber
	 * @param lstFiter
	 * @return
	 */
	public static List<Taxi> getAvaiableTaxiFilter(final double longitude, final double latitude, int carType,
			int maxDistance, int maxNumber, List<Taxi> lstFiter) {
		List<Taxi> lstResult = new ArrayList<>();
		ArrayList<Taxi> lstTmp = getAvaiableTaxi(longitude, latitude, carType, maxDistance, maxNumber);
		if (lstFiter != null) {
			for (Taxi taxi : lstTmp) {
				if (!lstFiter.contains(taxi)) {
					lstResult.add(taxi);
				}
			}
		}else {
			lstResult = lstTmp;
		}
		return lstResult;

	}

	/**
	 * Lay cac xe taxi dang online bang app o gan mot diem
	 * 
	 * @author VuD
	 * @param longitude
	 * @param latitude
	 * @param carType
	 *            <li>4 cho: 1
	 *            <li>7 cho: 2
	 *            <li>bat ky:0
	 * @param maxDistance
	 *            <i> Don vi la m</i>
	 * @param numVehicle
	 * @return
	 */
	public static ArrayList<Taxi> getAvaiableTaxi(final double longitude, final double latitude, int carType,
			int maxDistance, int numVehicle) {
		try {

			ArrayList<Taxi> lstResult = new ArrayList<>();
			ArrayList<Taxi> lstTmp = new ArrayList<>();
			int numCell = ((int) (maxDistance / CellMapTaxi.RANGER_CELL));
			int centerLon = CellMapTaxi.getLonIndex(longitude);
			int centerLat = CellMapTaxi.getLatIndex(latitude);
			if (carType != EnumCarTypeCommon.BON_CHO.getValue() && carType != EnumCarTypeCommon.BAY_CHO.getValue()) {
				carType = EnumCarTypeCommon.ALL.getValue();
			}
			getListTaxiNearest(lstTmp, centerLon, centerLat, 0, carType, numVehicle, numCell, false);
			for (Taxi taxi : lstTmp) {
				if (!lstResult.contains(taxi)) {
					lstResult.add(taxi);
				}
			}
			if (lstResult.size() != lstTmp.size()) {
			}
			Collections.sort(lstResult, new Comparator<Taxi>() {
				@Override
				public int compare(Taxi o1, Taxi o2) {
					double distance1 = MapUtils.distance(o1.getLongitude(), o1.getLattitute(), longitude, latitude);
					double distance2 = MapUtils.distance(o2.getLongitude(), o2.getLattitute(), longitude, latitude);
					if (distance1 < distance2)
						return -1;
					else if (distance1 > distance2)
						return 1;
					else
						return 0;
				}
			});
			if (numVehicle < 0 || lstResult.size() <= numVehicle) {
				return lstResult;
			} else {
				ArrayList<Taxi> lst = new ArrayList<>(lstResult.subList(0, numVehicle));
				return (ArrayList<Taxi>) lst;
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("GetAvaiableTaxi|Longitude:" + longitude + "|Latitude:" + latitude, e);
			return new ArrayList<Taxi>();
		}
	}

	private static void getListTaxiNearest(List<Taxi> lstModel, int centerLon, int centerLat, int delta, int carType,
			int numVehicle, int numCell, boolean isfinal) {
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
					List<Taxi> lstTmp = CellMapTaxi.getListVehicleNearest(i, j);
					lstModel.addAll(filterListTaxi(lstTmp, carType));
				}
			}
		}
		if (numCell == -1) {
			if (delta <= numCell) {
				if (delta == numCell) {
					getListTaxiNearest(lstModel, centerLon, centerLat, delta + 1, carType, numVehicle, numCell, true);
				} else {
					getListTaxiNearest(lstModel, centerLon, centerLat, delta + 1, carType, numVehicle, numCell, false);
				}
			} else {
				if (!isfinal) {
					getListTaxiNearest(lstModel, centerLon, centerLat, delta + 1, carType, numVehicle, numCell, true);
				}
			}
		} else {
			if (lstModel.size() <= numVehicle) {
				if (delta <= numCell) {
					if (delta == numCell) {
						getListTaxiNearest(lstModel, centerLon, centerLat, delta + 1, carType, numVehicle, numCell,
								true);
					} else {
						getListTaxiNearest(lstModel, centerLon, centerLat, delta + 1, carType, numVehicle, numCell,
								false);
					}
				} else {
					if (!isfinal) {
						getListTaxiNearest(lstModel, centerLon, centerLat, delta + 1, carType, numVehicle, numCell,
								true);
					}
				}
			}
		}
	}

	private static List<Taxi> filterListTaxi(List<Taxi> lstTaxi, int carType) {
		List<Taxi> lstResult = new ArrayList<>();
		if (carType == EnumCarTypeCommon.ALL.getValue()) {
			for (Taxi taxi : lstTaxi) {
				if (taxi.isConnect() && taxi.getTrip() == null) {
					lstResult.add(taxi);
				}
			}
		} else {
			for (Taxi taxi : lstTaxi) {
				if (taxi.isConnect() && taxi.getTrip() == null) {
					if (taxi.getCarType() == carType) {
						lstResult.add(taxi);
					}
				}
			}
		}
		return lstResult;
	}

	/**
	 * Lay danh sach xe o gan mot diem tu APP va RDS
	 * 
	 * @author VuD
	 * @param longitude
	 * @param latitude
	 * @param carType
	 *            <li>4 cho: 1
	 *            <li>7 cho: 2
	 *            <li>bat ky:0
	 * @param numVehicle
	 * @param ranger
	 *            <i>Don vi tinh la m</i>
	 * @param speed
	 * @return
	 */
	public static List<VehicleTmp> getListVehicleAroundPoint(final double longitude, final double latitude, int carType,
			int numVehicle, int ranger, int speed) {
		List<VehicleTmp> lstResult = new ArrayList<>();
		List<Taxi> lstTaxi = getAvaiableTaxi(longitude, latitude, carType, ranger, numVehicle);
		for (Taxi taxi : lstTaxi) {
			VehicleTmp msg = new VehicleTmp();
			msg.setDeviceId(taxi.getVehicle().getDeviceID());
			double taxiLongitude = taxi.getLongitude();
			msg.setLongitude(taxiLongitude);
			double taxiLatitude = taxi.getLattitute();
			msg.setLatitude(taxiLatitude);
			msg.setDriverId(taxi.getDriver().getId());
			msg.setDriverName(taxi.getDriverName());
			msg.setPhoneOffice(taxi.getPhoneNumber());
			msg.setVehicleId(taxi.getVehicle().getId());
			msg.setLiciencePlace(taxi.getLicensePlate());
			msg.setVehicleNumber(taxi.getVehicle().getValue());
			msg.setApp(true);
			msg.setDistance(MapUtils.distance(longitude, latitude, taxiLongitude, taxiLatitude));
			if (!lstResult.contains(msg)) {
				lstResult.add(msg);
			}
		}
		List<VehicleInfoJson> lstRDS = getVehicleInfoFromRDS(longitude, latitude, ranger, numVehicle, carType, -1, -1);
		for (VehicleInfoJson vehicleInfoJson : lstRDS) {
			VehicleTmp msg = new VehicleTmp();
			msg.setDeviceId(vehicleInfoJson.getDeviceId());
			double taxiLongitude = vehicleInfoJson.getLongitude();
			msg.setLongitude(taxiLongitude);
			double taxiLatitude = vehicleInfoJson.getLatitude();
			msg.setLatitude(taxiLatitude);
			msg.setDriverId(vehicleInfoJson.getDriverId());
			msg.setDriverName(vehicleInfoJson.getDriverName());
			msg.setPhoneOffice(vehicleInfoJson.getPhoneOffcice());
			msg.setVehicleId(vehicleInfoJson.getVehicleId());
			msg.setLiciencePlace(vehicleInfoJson.getLicensePlace());
			msg.setVehicleNumber(vehicleInfoJson.getVehicleNumber());
			msg.setApp(false);
			msg.setDistance(MapUtils.distance(longitude, latitude, taxiLongitude, taxiLatitude));
			if (!lstResult.contains(msg)) {
				lstResult.add(msg);
			}
		}
		Collections.sort(lstResult, new Comparator<VehicleTmp>() {
			@Override
			public int compare(VehicleTmp o1, VehicleTmp o2) {
				int result = 0;
				if (o1.getDistance() < o2.getDistance()) {
					result = -1;
				} else if (o1.getDistance() > o2.getDistance()) {
					result = 1;
				}
				return result;
			}
		});
		if (numVehicle > 0) {
			lstResult = lstResult.subList(0, numVehicle);
		}
		return lstResult;
	}

	public static int getDriverIdWs(String deviceid) {
		int result = -1;
		if (deviceid != null) {

			String url = new ConfigUtil().getPropValues("RDS_URL") + "driver/getdriverid";
			// String url =
			// "http://192.168.1.109:8081/RDS/rest/driver/getdriverid";
			Map<String, Object> mapParam = new HashMap<>();
			mapParam.put("deviceid", deviceid);
			String respond = WebserviceUtils.doGet(url, mapParam);
			try {
				result = Integer.parseInt(respond);
			} catch (Exception e) {
				result = -1;
			}
		}
		return result;
	}

	public static VehicleInfoJson getVehicleInfoFromRDS(int vehicleId) {
		VehicleInfoJson result = null;
		try {
			String url = ConfigUtil.getConfig("RDS_URL") + "taxi/info";
			Map<String, Object> mapParam = new HashMap<>();
			mapParam.put("vehicleid", vehicleId + "");
			String respond = WebserviceUtils.doGet(url, mapParam);
			if (respond != null && respond.length() > 0) {
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(respond);
				result = new VehicleInfoJson();
				result.setVehicleId(vehicleId);
				result.setLongitude(Double.valueOf(jsonObject.get("longitude") + ""));
				result.setLatitude(Double.valueOf(jsonObject.get("latitude") + ""));
				result.setAngle(Double.valueOf(jsonObject.get("angle") + ""));
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("GetVehicleInfoFromRDS", e);
			result = null;
		}
		return result;
	}


	/**
	 * 
	 * @author VuD
	 * @param longitude
	 * @param latitude
	 * @param ranger
	 *            <i> Don vi tinh la m </i>
	 * @param numVehicle
	 * @param carType
	 *            <li>4 cho: 1
	 *            <li>7 cho: 2
	 *            <li>bat ky: 0</span>
	 * @return
	 */
	public static List<VehicleInfoJson> getTaxiAvaiableFromRDS(double longitude, double latitude, int ranger,
			int numVehicle, int carType) {
		List<VehicleInfoJson> lstResult = new ArrayList<>();
		String url = new ConfigUtil().getPropValues("RDS_URL") + "taxi/avaiable";
		Map<String, Object> mapParam = new HashMap<>();
		mapParam.put("longitude", longitude);
		mapParam.put("latitude", latitude);
		mapParam.put("ranger", ranger);
		mapParam.put("numvehicle", numVehicle);
		mapParam.put("cartype", carType);
		String respond = WebserviceUtils.doGet(url, mapParam);
		try {
			JSONParser jsonParser = new JSONParser();
			JSONArray jsonarray = (JSONArray) jsonParser.parse(respond);
			@SuppressWarnings("rawtypes")
			Iterator i = jsonarray.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				VehicleInfoJson msg = new VehicleInfoJson();
				msg.setDeviceId(Integer.valueOf(innerObj.get("deviceId") + ""));
				msg.setDriverId(Integer.valueOf(innerObj.get("driverId") + ""));
				msg.setVehicleId(Integer.valueOf(innerObj.get("vehicleId") + ""));
				msg.setDriverName(innerObj.get("driverName") + "");
				msg.setPhoneNumber(innerObj.get("phoneNumber") + "");
				msg.setVehicleNumber(innerObj.get("vehicleNumber") + "");
				msg.setCarType(Integer.valueOf(innerObj.get("carType") + ""));
				msg.setLicensePlace(innerObj.get("licensePlace") + "");
				msg.setDistance(Double.valueOf(innerObj.get("distance") + ""));
				msg.setLongitude(Double.valueOf(innerObj.get("longitude") + ""));
				msg.setLatitude(Double.valueOf(innerObj.get("latitude") + ""));
				msg.setAngle(Double.valueOf(innerObj.get("angle") + ""));
				lstResult.add(msg);
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("GetTaxiAvaiable", e);
		}
		return lstResult;
	}

	public static List<VehicleInfoJson> getVehicleInfoFromRDS(double longitude, double latitude, int ranger,
			int numvehicle, int cartype, int speed, int intrip) {
		List<VehicleInfoJson> lstResult = new ArrayList<>();
		String url = new ConfigUtil().getPropValues("RDS_URL") + "taxi/avaiable";
		// String url = "http://192.168.1.109:8081/RDS/rest/taxi/avaiable";
		Map<String, Object> mapParam = new HashMap<>();
		mapParam.put("longitude", longitude);
		mapParam.put("latitude", latitude);
		mapParam.put("ranger", ranger);
		mapParam.put("numvehicle", numvehicle);
		mapParam.put("cartype", cartype);
		mapParam.put("speed", speed);
		mapParam.put("intrip", intrip);
		String respond = WebserviceUtils.doGet(url, mapParam);
		try {
			JSONParser jsonParser = new JSONParser();
			// JSONObject jsonObject = (JSONObject)
			// jsonParser.parse(respond);
			JSONArray jsonarray = (JSONArray) jsonParser.parse(respond);
			@SuppressWarnings("rawtypes")
			Iterator i = jsonarray.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				VehicleInfoJson msg = new VehicleInfoJson();
				msg.setDriverId(Integer.valueOf(innerObj.get("driverId") + ""));
				msg.setDeviceId(Integer.valueOf(innerObj.get("deviceId") + ""));
				msg.setVehicleId(Integer.valueOf(innerObj.get("vehicleId") + ""));
				msg.setDriverName(innerObj.get("driverName") + "");
				msg.setPhoneNumber(innerObj.get("phoneNumber") + "");
				msg.setVehicleNumber(innerObj.get("vehicleNumber") + "");
				msg.setCarType(Integer.valueOf(innerObj.get("carType") + ""));
				msg.setLicensePlace(innerObj.get("licensePlace") + "");
				msg.setDistance(Double.valueOf(innerObj.get("distance") + ""));
				msg.setLatitude(Double.valueOf(innerObj.get("latitude") + ""));
				msg.setLongitude(Double.valueOf(innerObj.get("longitude") + ""));
				lstResult.add(msg);
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("getVehicleInfoFromRDS", e);
		}
		return lstResult;
	}

}
