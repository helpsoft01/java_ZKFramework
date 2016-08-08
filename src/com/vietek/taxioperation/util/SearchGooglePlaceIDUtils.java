package com.vietek.taxioperation.util;

import com.sun.jersey.api.client.Client;
import com.vietek.taxioperation.common.AppLogger;

public class SearchGooglePlaceIDUtils {

	public static Client client = Client.create();

	public SearchGooglePlaceIDUtils() {
	}

//	public Address getAddressByID(String placeID) {
//
//		Address relVal = new Address();
//		relVal.setPlaceID(placeID);
//
//		try {
//			// Look up place id
//			String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&key="
//					+ SearchGooglePlaceUtils.apiKey;
//			WebResource webResource = client.resource(url);
//			ClientResponse clientResponse = (ClientResponse) webResource.accept("application/json")
//					.get(ClientResponse.class);
//			String output = clientResponse.getEntity(String.class);
//			JsonReader reader = Json.createReader(new StringReader(output));
//			JsonObject jsonObj = reader.readObject();
//			JsonObject position = jsonObj.getJsonObject("result");
//			if (position != null) {
//				position = position.getJsonObject("geometry");
//				if (position != null) {
//					position = position.getJsonObject("location");
//				}
//			}
//
//			if (position != null) {
//
//				relVal.setLatitude(position.getJsonNumber("lat").doubleValue());
//				relVal.setLongitude(position.getJsonNumber("lng").doubleValue());
//			}
//		} catch (Exception e) {
//
//		}
//		return relVal;
//	}
}
