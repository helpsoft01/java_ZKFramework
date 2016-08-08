package com.vietek.taxioperation.webservice;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.util.ConfigUtil;

public class GetTaxiOnline {
	public static List<TaxiOnline> getTaxi(double longtitude, double latitude, int distance, int carNo, int carType) {
		List<TaxiOnline> lst = new ArrayList<TaxiOnline>();
		Client rdsClient = null;
		String rds = ConfigUtil.getConfig("RDS_URL") + "taxi/avaiable?longitude=" + longtitude + "&latitude=" + latitude
				+ "&ranger=" + distance + "&numvehicle=" + carNo + "&cartype=" + carType;
		if (rdsClient == null)
			rdsClient = Client.create();
		WebResource rdsResource = rdsClient.resource(rds);
		ClientResponse rdsResponse = rdsResource.get(ClientResponse.class);
		if (rdsResponse.getStatus() == 200) {
			String ret = rdsResponse.getEntity(String.class);
			JsonReader reader = Json.createReader(new StringReader(ret));
			JsonArray arrayTaxiJson = reader.readArray();
			for (int i = 0; i < arrayTaxiJson.size(); i++) {
				TaxiOnline taxi = getDriverObjFromRDS(arrayTaxiJson.getJsonObject(i));
				if (taxi != null) {
					lst.add(taxi);
				}
			}
		}
		return lst;
	}

	private static TaxiOnline getDriverObjFromRDS(JsonObject driver) {
		TaxiOnline taxi = new TaxiOnline();
		try {
			taxi.setDriver_id(driver.getInt("driverId"));
			if (driver.isNull("driverName")) {
				taxi.setDriver_name("");
			} else {
				taxi.setDriver_name(driver.getString("driverName"));
			}
			if (driver.isNull("vehicleNumber")) {
				taxi.setTaxi_number("");
			} else {
				taxi.setTaxi_number(driver.getString("vehicleNumber"));
			}
			if (driver.isNull("phoneNumber")) {
				taxi.setPhoneNumber("");
			} else {
				taxi.setPhoneNumber(driver.getString("phoneNumber"));
			}
			taxi.setCarType(driver.getInt("carType"));
			taxi.setLatitude(driver.getJsonNumber("latitude").doubleValue());
			taxi.setLongtitude(driver.getJsonNumber("longitude").doubleValue());
			taxi.setAngle(driver.getJsonNumber("angle").doubleValue());
			taxi.setRate(driver.getJsonNumber("rate").doubleValue());
			return taxi;
		} catch (Exception e) {
			AppLogger.logDebug.error(driver.getString("phoneNumber"), e);
		}
		return null;
	}
}
