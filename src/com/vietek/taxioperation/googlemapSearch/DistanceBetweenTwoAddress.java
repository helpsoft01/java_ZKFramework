package com.vietek.taxioperation.googlemapSearch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.googlemapSearch.GoogleResponseDirection.Element;
import com.vietek.taxioperation.googlemapSearch.GoogleResponseDirection.Item;
import com.vietek.taxioperation.util.GoogleKey;

public class DistanceBetweenTwoAddress {

	String strDistance = "";
	String strDuration = "";

	private static URL url;
	private InputStream in = null;
	private HttpURLConnection conn = null;

	public String getStrDistance() {
		return strDistance;
	}

	public void setStrDistance(String strDistance) {
		this.strDistance = strDistance;
	}

	public String getStrDuration() {
		return strDuration;
	}

	public void setStrDuration(String strDuration) {
		this.strDuration = strDuration;
	}

	public DistanceBetweenTwoAddress() {

	}

	public void getDistance(String addressFrom, String addressTo) {
		// TODO Auto-generated method stub

		// URL url = new
		// URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Victoria+BC");
		// -----get detail
		// URL("https://maps.googleapis.com/maps/api/directions/json?origin=Bangalore,India&destination=Belgaum,India&sensor=false")
		// -----get total
		// URL("https://maps.googleapis.com/maps/api/distancematrix/json?" +
		// "origins=" + addressFrom
		// + "&destinations=" + addressTo + "&sensor=false");

		try {
			url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?" + "origins="
					+ URLEncoder.encode(addressFrom, "UTF-8") + "&destinations=" + URLEncoder.encode(addressTo, "UTF-8")
					+ "&language=vi" + "&mode=driving" + "&key=" + GoogleKey.getKey());
			// url = new
			// URL("https://maps.googleapis.com/maps/api/directions/json?" +
			// "origin=" + addressFrom
			// + "&destination=" + addressTo + "&sensor=false");
			conn = (HttpURLConnection) url.openConnection();

			// 4xx: client error, 5xx: server error. See:
			// http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html.
			boolean isError = conn.getResponseCode() >= 400;
			// The normal input stream doesn't work in error-cases.
			// in = isError ? conn.getErrorStream() : conn.getInputStream();
			in = conn.getInputStream();

			if (!isError) {
				JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in, "UTF-8")));
				GoogleResponseDirection r = (new Gson().fromJson(reader, GoogleResponseDirection.class));

				if (r.getRows().length > 0)
					for (Item s : r.getRows()) {
						if (s.getElements().length > 0) {
							Element item = s.getElements()[0];
							if (item.status.equals("OK")) {
								strDistance = s.getElements()[0].getDistance().getText();
								strDuration = s.getElements()[0].getDuration().getText();

							} else {
								strDistance = "Không xác định";
								strDuration = " - (60 Km/H)";

							}

						} else {
							strDistance = "0";
							strDuration = " - " + " 0H " + " (60 Km/H)";
						}
					}
				else {
					strDistance = "0";
					strDuration = " - " + " 0H " + " (60 Km/H)";
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		} finally {
		}
	}

}
