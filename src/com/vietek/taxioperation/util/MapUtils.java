package com.vietek.taxioperation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.maps.model.LatLng;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.googlemapSearch.GoogleResponseAddress;
import com.vietek.taxioperation.model.JsonAutoComplete;
import com.vietek.taxioperation.model.MyPrediction;
import com.vietek.taxioperation.ui.controller.vmap.LatLngBounds;
import com.vietek.taxioperation.ui.controller.vmap.VMaps;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;
import com.vietek.taxioperation.ui.controller.vmap.VPolyline;

public class MapUtils {
	/**
	 * Ban kinh trai dat. Tinh theo m
	 */
	public static final double R = 6372.8 * 1000; // In kilometers

	/**
	 * He do chuyen doi tu do sang radian
	 */
	public static final double deg2Rad = Math.PI / 180d;

	/**
	 * He so chuyen doi tu radian sang do
	 */
	public static final double rad2Deg = 180d / Math.PI;

	private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";

	/**
	 * Key hoi dia chi cua webservice
	 */
	public static final String IMAP_KEY = new String("WnjK32E24GChTuDxaXEStjlInrZaImdXyx2uHAhLt/k=");

	public static final String IMAP_URI = new String("http://imap.com.vn/");

	/**
	 * Geocode. Hoi dia chi
	 */
	public static final String GEO_TYPE_INFO = new String("reserve_geocode");

	/**
	 * Calculate distance of two point
	 * 
	 * @param lon1
	 * @param lat1
	 * @param lon2
	 * @param lat2
	 * @return
	 */
	public static double distance(double lon1, double lat1, double lon2, double lat2) {
		double cosd = Math.sin(lat1 * Math.PI / (double) 180) * Math.sin(lat2 * Math.PI / (double) 180)
				+ Math.cos(lat1 * Math.PI / (double) 180) * Math.cos(lat2 * Math.PI / (double) 180)
						* Math.cos((lon1 - lon2) * Math.PI / (double) 180);
		if (cosd >= 1) {
			return 0.0;
		}
		double d = Math.acos(cosd);
		double distance = d * R;
		return distance;
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

	public static MyPrediction[] getPredictions(String input) throws UnsupportedEncodingException {
		MyPrediction[] myPre = null;
		// String apiKey = "AIzaSyDT1Lycs32ZMJsKxTAqygcpPTxDl_2AFPU";
		WebResource webResource;
		Client client = Client.create();
		input = input.replaceAll(" ", "+");
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
				+ URLEncoder.encode(input.trim(), "UTF-8") + "&components=country:vn&language=vn" + "&key="
				+ GoogleKey.getKey();
		webResource = client.resource(url);
		ClientResponse clientResponse = webResource.accept("application/json").get(ClientResponse.class);
		String output = clientResponse.getEntity(String.class);
		ObjectMapper om = new ObjectMapper();
		JsonAutoComplete jsonAutoComplete;
		try {
			jsonAutoComplete = om.readValue(output, JsonAutoComplete.class);

			if (jsonAutoComplete.getStatus().equalsIgnoreCase("OK")) {
				myPre = jsonAutoComplete.getPredictions();
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myPre;
	}

	/**
	 * 
	 * @author VuD
	 * @param longitude
	 * @param latitude
	 * @param isGoogleFirst
	 *            <li><i>true</i> neu hoi google truoc
	 *            <li><i>false</i> neu hoi imap truoc
	 * @return
	 */
	public static String getAddress(double longitude, double latitude, boolean isGoogleFirst) {
		String result = null;
		try {
			if (isGoogleFirst) {
				result = convertLatLongToAddrest(latitude, longitude);
				if (result == null || result.length() <= 0) {
					result = getAddressFromIMap(longitude, latitude);
				}
			} else {
				result = getAddressFromIMap(longitude, latitude);
				if (result == null || result.length() <= 0) {
					result = convertLatLongToAddrest(latitude, longitude);
				}
			}
		} catch (Exception e) {
			AppLogger.logUserAction.error("GoogleMapUntil|getAddress|longitude:" + longitude + "|latitude:" + latitude,
					e);
		}
		return result;
	}

	public static String convertLatLongToAddrest(double latitude, double longitude) {
		String address = "";
		URLConnection conn = null;
		InputStream in = null;
		try {
			// String latlongString = Double.toString(latitude) + "," +
			// Double.toString(longitude);
			String latlongString = latitude + "," + longitude;
			URL url = new URL(URL + "?latlng=" + latlongString + "&key=" + GoogleKey.getKey());
			conn = url.openConnection();
			in = conn.getInputStream();
			JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in, "UTF-8")));
			GoogleResponseAddress r = (new Gson().fromJson(reader, GoogleResponseAddress.class));
			if (r.getStatus().equals("OK"))
				if (r.getResults().length > 0) {
					address = r.getResults()[0].getFormatted_address();
				}
		} catch (Exception e) {
			AppLogger.logUserAction.error(
					"GoogleMapUntil|convertLatLongToAddrest|longitude:" + longitude + "|latitude:" + latitude, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					AppLogger.logUserAction.error(
							"GoogleMapUntil|convertLatLongToAddrest|longitude:" + longitude + "|latitude:" + latitude,
							e);
				}
			}
		}
		return address;
	}

	public static LatLng convertAddresstoLatLng(String fulladdress) {
		LatLng result = null;
		double lati = 0;
		double longi = 0;
		InputStream in = null;
		try {
			URL url = new URL(
					URL + "?address=" + URLEncoder.encode(fulladdress, "UTF-8") + "&key=" + GoogleKey.getKey());
			URLConnection conn = url.openConnection();
			in = conn.getInputStream();
			JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in, "UTF-8")));
			GoogleResponseAddress r = (new Gson().fromJson(reader, GoogleResponseAddress.class));
			if (r.getStatus().equals("OK"))
				if (r.getResults().length > 0) {
					lati = (double) r.getResults()[0].getGeometry().getLocation().getLat();
					longi = (double) r.getResults()[0].getGeometry().getLocation().getLng();
				}
		} catch (Exception e) {
			AppLogger.logUserAction.error("GoogleMapUntil|convertAddresstoLatLng|Address:" + fulladdress, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					AppLogger.logUserAction.error("GoogleMapUntil|convertAddresstoLatLng|Address:" + fulladdress, e);
				}
			}
		}
		if (lati > 0 & longi > 0) {
			result = new LatLng(lati, longi);
		}
		return result;
	}

	public static void scaleMap(Gmaps map) {
		Double minLat = null;
		Double maxLat = null;
		Double minLng = null;
		Double maxLng = null;
		for (Object o : map.getChildren()) {
			if (o instanceof Gmarker) {
				Gmarker g = (Gmarker) o;
				if ((minLat == null) || (g.getLat() < minLat)) {
					minLat = g.getLat();
				}
				if ((maxLat == null) || (g.getLat() > maxLat)) {
					maxLat = g.getLat();
				}
				if ((minLng == null) || (g.getLng() < minLng)) {
					minLng = g.getLng();
				}
				if ((maxLng == null) || (g.getLng() > maxLng)) {
					maxLng = g.getLng();
				}
			}
		}

		if (minLat == null) {
			return;
		}
		Double ctrLng = (maxLng + minLng) / 2;
		Double ctrLat = (maxLat + minLat) / 2;
		Double interval = 0.0;
		int mapDisplay = 600; // Minimum of height or width of map in pixels
		if ((maxLat - minLat) > (maxLng - minLng)) {
			interval = (maxLat - minLat) / 2;
			minLng = ctrLng - interval;
			maxLng = ctrLng + interval;
		} else {
			interval = (maxLng - minLng) / 2;
			minLat = ctrLat - interval;
			maxLat = ctrLat + interval;
		}
		Double dist = (6371
				* Math.acos(Math.sin(minLat / 57.2958) * Math.sin(maxLat / 57.2958) + (Math.cos(minLat / 57.2958)
						* Math.cos(maxLat / 57.2958) * Math.cos((maxLng / 57.2958) - (minLng / 57.2958)))));
		Double zoom = Math.floor(7 - Math.log(1.6446 * dist / Math.sqrt(2 * (mapDisplay * mapDisplay))) / Math.log(2));
		map.setCenter(ctrLat, ctrLng);
		map.setZoom(zoom.intValue());
	}

	public static void setCenter(List<LatLng> points, VMaps vmap, Double padding) {
		Double minLat = null;
		Double maxLat = null;
		Double minLng = null;
		Double maxLng = null;
		for (LatLng point : points) {
			if ((minLat == null) || (point.lat < minLat)) {
				minLat = point.lat;
			}
			if ((maxLat == null) || (point.lat > maxLat)) {
				maxLat = point.lat;
			}
			if ((minLng == null) || (point.lng < minLng)) {
				minLng = point.lng;
			}
			if ((maxLng == null) || (point.lng > maxLng)) {
				maxLng = point.lng;
			}
		}

		if (minLat == null) {
			return;
		}
		LatLng southWest = new LatLng(minLat - padding, minLng - padding);
		LatLng northEast = new LatLng(maxLat + padding, maxLng + padding);
		LatLngBounds bound = new LatLngBounds(southWest, northEast);
		vmap.setBounds(bound);
		// vmap.setCenter(new LatLng(ctrLat, ctrLng));
	}

	public static void scaleMap(VMaps map) {
		Double minLat = null;
		Double maxLat = null;
		Double minLng = null;
		Double maxLng = null;
		for (Object o : map.getChildren()) {
			if (o instanceof VMarker) {
				VMarker g = (VMarker) o;
				if ((minLat == null) || (g.getLat() < minLat)) {
					minLat = g.getLat();
				}
				if ((maxLat == null) || (g.getLat() > maxLat)) {
					maxLat = g.getLat();
				}
				if ((minLng == null) || (g.getLng() < minLng)) {
					minLng = g.getLng();
				}
				if ((maxLng == null) || (g.getLng() > maxLng)) {
					maxLng = g.getLng();
				}
			} else if (o instanceof VPolyline) {
				VPolyline line = (VPolyline) o;
				for (LatLng point : line.getPath()) {
					if ((minLat == null) || (point.lat < minLat)) {
						minLat = point.lat;
					}
					if ((maxLat == null) || (point.lat > maxLat)) {
						maxLat = point.lat;
					}
					if ((minLng == null) || (point.lng < minLng)) {
						minLng = point.lng;
					}
					if ((maxLng == null) || (point.lng > maxLng)) {
						maxLng = point.lng;
					}
				}
			}
		}

		if (minLat == null) {
			return;
		}
		Double ctrLng = (maxLng + minLng) / 2;
		Double ctrLat = (maxLat + minLat) / 2;
		Double interval = 0.0;
		int mapDisplay = 300; // Minimum of height or width of map in pixels
		if ((maxLat - minLat) > (maxLng - minLng)) {
			interval = (maxLat - minLat) / 2;
			minLng = ctrLng - interval;
			maxLng = ctrLng + interval;
		} else {
			interval = (maxLng - minLng) / 2;
			minLat = ctrLat - interval;
			maxLat = ctrLat + interval;
		}
		Double dist = (6371
				* Math.acos(Math.sin(minLat / 57.2958) * Math.sin(maxLat / 57.2958) + (Math.cos(minLat / 57.2958)
						* Math.cos(maxLat / 57.2958) * Math.cos((maxLng / 57.2958) - (minLng / 57.2958)))));
		Double zoom = Math.floor(7 - Math.log(1.6446 * dist / Math.sqrt(2 * (mapDisplay * mapDisplay))) / Math.log(2));
		map.setCenter(ctrLat, ctrLng);
		map.setZoom(zoom.intValue());
	}

	/**
	 * Hoi dia chi tu imap
	 * 
	 * @author VuD
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public static String getAddressFromIMap(double longitude, double latitude) {
		String value = "";
		try {
			SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
			SOAPConnection sconn = scf.createConnection();
			String url = ConfigUtil.getConfig("MAP_URL", "http://192.168.1.106/geoservice/geocoding.asmx");
			SOAPMessage respond = sconn.call(createSoapRequest(longitude, latitude), url);
			sconn.close();
			SOAPBody sbody = respond.getSOAPBody();
			SOAPBodyElement responseElement = (SOAPBodyElement) sbody.getChildElements().next();
			SOAPBodyElement returnElement = (SOAPBodyElement) responseElement.getChildElements().next();
			SOAPBodyElement returnfinishElement = (SOAPBodyElement) returnElement.getChildElements().next();
			return value = returnfinishElement.getValue();
		} catch (Exception e) {
			AppLogger.logUserAction
					.error("GoogleMapUntil|getAddressFromIMap|longitude:" + longitude + "|latitude:" + latitude, e);
		}
		return value;
	}

	private static SOAPMessage createSoapRequest(double longitude, double latitude) throws SOAPException, IOException {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("imap", IMAP_URI);
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("GetGeocodeInfor", "imap");
		SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("key", "imap");
		soapBodyElem1.addTextNode(IMAP_KEY);
		SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("geoType", "imap");
		soapBodyElem2.addTextNode(GEO_TYPE_INFO);
		SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("coords", "imap");
		soapBodyElem3.addTextNode(longitude + "|" + latitude);
		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", IMAP_URI + "GetGeocodeInfor");
		soapMessage.saveChanges();
		return soapMessage;
	}

	public static void setCenterMap(VMaps map) {
		Double minLat = null;
		Double maxLat = null;
		Double minLng = null;
		Double maxLng = null;
		for (Object o : map.getChildren()) {
			if (o instanceof VMarker) {
				VMarker g = (VMarker) o;
				if ((minLat == null) || (g.getLat() < minLat)) {
					minLat = g.getLat();
				}
				if ((maxLat == null) || (g.getLat() > maxLat)) {
					maxLat = g.getLat();
				}
				if ((minLng == null) || (g.getLng() < minLng)) {
					minLng = g.getLng();
				}
				if ((maxLng == null) || (g.getLng() > maxLng)) {
					maxLng = g.getLng();
				}
			}
		}

		if (minLat == null) {
			return;
		}
		Double ctrLng = (maxLng + minLng) / 2;
		Double ctrLat = (maxLat + minLat) / 2;
		map.setCenter(ctrLat, ctrLng);
	}
}
