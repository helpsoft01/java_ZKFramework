package com.vietek.taxioperation.util;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.JsonGoogleResponsePlace;
import com.vietek.taxioperation.model.ResultGoogleResponsePlace;

public class SearchGooglePlaceUtils {

	public static SearchGooglePlaceUtils sharedInstance = new SearchGooglePlaceUtils();
	public static Client client = Client.create();
	// public static String apiKey = "AIzaSyCYek91t8uzrzwxgIsEEno22VRhwRANTdE";
	static String url = "";
	static WebResource webResource;
	static ClientResponse clientResponse;
	static String output;
	static ObjectMapper om;
	static JsonGoogleResponsePlace jsonAutoComplete;
	private ChannelTms channel = null;

	public ChannelTms getChannel() {
		return channel;
	}

	public void setChannel(ChannelTms channel) {
		this.channel = channel;
	}

	public ArrayList<Address> getAddress_ByPlace(String searchText, ChannelTms channel) {
		ArrayList<Address> retVal = new ArrayList<>();
		try {
			ResultGoogleResponsePlace[] myPre = new ResultGoogleResponsePlace[0];
			// String url =
			// "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
			// + URLEncoder.encode(searchText.trim(), "UTF-8") +
			// "&components=country:vn&language=vn" + "&key="
			// + apiKey;
			if (channel.getLatitude() > 0 && channel.getLongitude() > 0) {
				double delta = 0.001;
				String bounds = (channel.getLatitude() - delta) + "," + (channel.getLongitude() - delta) + "|"
						+ (channel.getLatitude() + delta) + "," + (channel.getLongitude() + delta);

				url = "https://maps.googleapis.com/maps/api/geocode/json?address="
						+ URLEncoder.encode(searchText.trim(), "UTF-8") + "&bounds="
						+ URLEncoder.encode(bounds, "UTF-8") + "&components=country:vn&language=vn" + "&key="
						+ GoogleKey.getKey();
			} else {
				url = "https://maps.googleapis.com/maps/api/geocode/json?address="
						+ URLEncoder.encode(searchText.trim(), "UTF-8") + "&components=country:vn&language=vn" + "&key="
						+ GoogleKey.getKey();
			}
			webResource = client.resource(url);
			client.setReadTimeout(10000);
			client.setConnectTimeout(10000);

			clientResponse = (ClientResponse) webResource.accept("application/json").get(ClientResponse.class);
			output = clientResponse.getEntity(String.class);
			om = new ObjectMapper();

			jsonAutoComplete = om.readValue(output, JsonGoogleResponsePlace.class);

			if (jsonAutoComplete.getStatus().equalsIgnoreCase("OK")) {
				myPre = jsonAutoComplete.getResults();
			}
			if (jsonAutoComplete.getStatus().equalsIgnoreCase("OVER_QUERY_LIMIT")) {

			}

			if (myPre.length > 0) {

				if (myPre.length == 1 && myPre[0].getFormatted_address().trim().equals("Vietnam")) {
					Address address = new Address();
					address.setName(searchText.trim());
					address.setByGoogle(true);
					retVal.add(address);
				} else
					for (int i = 0; i < myPre.length; i++) {

						String nameAddress = standardString(myPre[i].getFormatted_address());

						Address address = new Address();
						address.setName(nameAddress);
						address.setPlaceID(myPre[i].getPlace_id());
						address.setLatitude(Double.valueOf(myPre[i].getGeometry().getLocation().getLat()));
						address.setLongitude(Double.valueOf(myPre[i].getGeometry().getLocation().getLng()));
						address.setByGoogle(true);
						retVal.add(address);
					}
				retVal = sortAddress(retVal, channel);
			} else {

				Address address = new Address();
				address.setName(searchText.trim());
				address.setPlaceID("");
				address.setLatitude(0.0);
				address.setLongitude(0.0);
				retVal.add(address);
			}

		} catch (Exception e) {

			Address address = new Address();
			address.setName(searchText.trim());
			address.setPlaceID("");
			address.setLatitude(0.0);
			address.setLongitude(0.0);

			retVal.add(address);

			e.printStackTrace();
		}
		// Address[] tmp = new Address[retVal.size()];
		// retVal.toArray(tmp);
		return retVal;
	}

	public Address getAddress_ByPlaceID(String placeID) {

		Address relVal = new Address();
		relVal.setPlaceID(placeID);

		try {
			// Look up place id
			String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&key="
					+ GoogleKey.getKey();
			WebResource webResource = client.resource(url);
			ClientResponse clientResponse = (ClientResponse) webResource.accept("application/json")
					.get(ClientResponse.class);
			String output = clientResponse.getEntity(String.class);
			JsonReader reader = Json.createReader(new StringReader(output));
			JsonObject jsonObj = reader.readObject();
			JsonObject position = jsonObj.getJsonObject("result");
			if (position != null) {
				position = position.getJsonObject("geometry");
				if (position != null) {
					position = position.getJsonObject("location");
				}
			}

			if (position != null) {

				relVal.setLatitude(position.getJsonNumber("lat").doubleValue());
				relVal.setLongitude(position.getJsonNumber("lng").doubleValue());
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("*** Error Look up place id: ", e);
		}
		return relVal;
	}

	private ArrayList<Address> sortAddress(ArrayList<Address> lstAddress, final ChannelTms channel) {

		Collections.sort(lstAddress, new Comparator<Address>() {

			@Override
			public int compare(Address o1, Address o2) {
				int retVal = 0;

				double distance1 = MapUtils.distance(channel.getLongitude(), channel.getLatitude(), o1.getLongitude(),
						o1.getLatitude());
				double distance2 = MapUtils.distance(channel.getLongitude(), channel.getLatitude(), o2.getLongitude(),
						o2.getLatitude());
				if (distance1 < distance2)
					retVal = -1;
				else if (distance1 > distance2)
					retVal = 1;
				return retVal;
			}
		});
		return lstAddress;
	}

	public static String getKeyAPI() {
		return GoogleKey.getKey();
	}

	public String standardString(String src) {
		String reVal = src.replaceAll(", Vietnam", "");
		reVal = reVal.replaceAll("Quận", "Q.");
		reVal = reVal.replaceAll("Phường", "P.");
		reVal = reVal.replaceAll("tp.", "TP.");

		return reVal;
	}
	// public static void main(String args[]) {
	// sharedInstance.searchAddress("64 hai ba trung");
	// }
}
