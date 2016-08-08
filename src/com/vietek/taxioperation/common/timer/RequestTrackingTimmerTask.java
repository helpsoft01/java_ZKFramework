package com.vietek.taxioperation.common.timer;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.TimerTask;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.WebserviceUtils;
import com.vietek.trackingOnline.tracker.TrackingRDS2Json;

/**
 *
 * Jul 1, 2016
 *
 * @author habv
 *
 */

public class RequestTrackingTimmerTask extends TimerTask {

	@Override
	public void run() {
		// TODO Lay du lieu tracking
		try{
			String url = new ConfigUtil().getPropValues("RDS_URL") + "taxi/lasttracking";
			
			String respond = WebserviceUtils.doGet(url,null);
			if (respond != null && respond.length() > 0) {
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(respond);
				Iterator<?> keys = jsonObject.keySet().iterator();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					JSONObject json = (JSONObject) jsonObject.get(key);
					if (json != null) {
						TrackingRDS2Json tracking = getTrackingItem(json);
						if (tracking != null) {
							if (MapCommon.TRACKING_RDS.get(tracking.getDeviceId()) != null)
								MapCommon.TRACKING_RDS.replace(tracking.getDeviceId(), tracking);
							else
								MapCommon.TRACKING_RDS.put(tracking.getDeviceId(), tracking);
						}
					}
				}
			}
		} catch(Exception e){
			AppLogger.logTracking.error("Loi lay du lieu tracking tu RDS", e);
		}
	}

	private TrackingRDS2Json getTrackingItem(JSONObject json) {
		TrackingRDS2Json rds2Json = null;
		try {
			if (!json.isEmpty()) {
				int deviceId = Integer.valueOf(json.get("deviceId")+""); //json.getInt("deviceId");
				if (deviceId > 0) {
					rds2Json = new TrackingRDS2Json();
					rds2Json.setDeviceId(deviceId);
					if (json.get("timeLog")!=null) {
						long time = Long.valueOf((Long)json.get("timeLog"));
						if (time > 0)
							rds2Json.setTimeLog(new Timestamp(time));
						else
							rds2Json.setTimeLog(null);
					} else {
						rds2Json.setTimeLog(null);
					}
					if (json.get("licensePlace")!=null) {
						rds2Json.setLicensePlate((String) json.get("licensePlace"));
					}
					if (json.get("driverId")!=null) {
						rds2Json.setDriverId(Integer.valueOf(json.get("driverId")+""));
					}
					if (json.get("driverName")!=null)
						rds2Json.setDriverName((String) json.get("driverName"));
					else
						rds2Json.setDriverName("");

					if (json.get("phoneNumber") == null)
						rds2Json.setPhoneNumber("");
					else
						rds2Json.setPhoneNumber((String) json.get("phoneNumber"));

					if (json.get("phoneOffcice")== null)
						rds2Json.setPhoneOffcice("");
					else
						rds2Json.setPhoneOffcice((String) json.get("phoneOffcice"));
					if (json.get("vehicleId")!=null) {
						rds2Json.setVehicleId(Integer.valueOf(json.get("vehicleId")+""));
					} else {
						rds2Json.setVehicleId(0);
					}
					if (json.get("vehicleNumber")!=null)
						rds2Json.setVehicleNumber((String) json.get("vehicleNumber"));
					else
						rds2Json.setVehicleNumber("");

					if (json.get("carType")!=null) {
						rds2Json.setCarType(Integer.valueOf(json.get("carType")+""));
					} else
						rds2Json.setCarType(1);
					if (json.get("inTrip")!=null) {
						rds2Json.setInTrip(Integer.valueOf(json.get("inTrip")+""));
					} else
						rds2Json.setInTrip(0);
					if (json.get("longitude")!=null)
						rds2Json.setLongitude(Double.valueOf(json.get("longitude")+""));
					if (json.get("latitude")!=null)
						rds2Json.setLatitude(Double.valueOf(json.get("latitude")+""));
					if (json.get("angle")!=null)
						rds2Json.setAngle(Double.valueOf(json.get("angle")+""));
					if (json.get("gpsSpeed")!=null) {
						rds2Json.setGpsSpeed(Integer.valueOf(json.get("gpsSpeed")+""));
					} else
						rds2Json.setGpsSpeed(0);
					if (json.get("metterSpeed")!=null)
						rds2Json.setMetterSpeed(Integer.valueOf(json.get("metterSpeed")+""));
					else
						rds2Json.setMetterSpeed(0);
					if(json.get("engine")!=null)
						rds2Json.setEngine(Integer.parseInt(json.get("engine")+""));
					else
						rds2Json.setEngine(0);
				}
			}
		} catch (Exception e) {
			AppLogger.logTracking.error("Loi convert ban tin tracking", e);
		}
		return rds2Json;
	}

}
