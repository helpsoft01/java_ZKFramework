package com.vietek.taxioperation.googlemapSearch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.ui.controller.vmap.VMaps;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;
import com.vietek.taxioperation.ui.controller.vmap.VPolyline;
import com.vietek.taxioperation.util.GoogleKey;
import com.vietek.tracking.ui.model.GpsTrackingMsg;

public class GoogleMapUntil {

	private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";

	public static String convertLatLongToAddrest(Double lati, Double longi) {
		String address = "";
		try {

			String latlongString = Double.toString(lati) + "," + Double.toString(longi);
			URL url = new URL(URL + "?latlng=" + latlongString + "&key=" + GoogleKey.getKey());
			URLConnection conn = url.openConnection();

			InputStream in = conn.getInputStream();

			JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in, "UTF-8")));
			GoogleResponseAddress r = (new Gson().fromJson(reader, GoogleResponseAddress.class));
			if (r.getStatus().equals("OK"))
				if (r.getResults().length > 0) {
					address = r.getResults()[0].getFormatted_address();
				}
			in.close();
		} catch (Exception e1) {
			AppLogger.logDebug.error("", e1);
		}
		return address;
	}

	public static LatLng convertAddresstoLatLng(String fulladdress) {
		LatLng result = null;
		double lati = 0;
		double longi = 0;
		try {

			URL url = new URL(
					URL + "?address=" + URLEncoder.encode(fulladdress, "UTF-8") + "&key=" + GoogleKey.getKey());
			URLConnection conn = url.openConnection();

			InputStream in = conn.getInputStream();

			JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in, "UTF-8")));
			GoogleResponseAddress r = (new Gson().fromJson(reader, GoogleResponseAddress.class));

			if (r.getStatus().equals("OK"))

				if (r.getResults().length > 0) {

					lati = (double) r.getResults()[0].getGeometry().getLocation().getLat();
					longi = (double) r.getResults()[0].getGeometry().getLocation().getLng();
				}

			in.close();
		} catch (Exception e1) {
			e1.printStackTrace();
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

		// Work out the minimum and maximmum latitude and longitude
		//
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

		// No markers found, so don't bother scaling
		if (minLat == null) {
			return;
		}

		// Calculate the centre Lat and Long
		//
		Double ctrLng = (maxLng + minLng) / 2;
		Double ctrLat = (maxLat + minLat) / 2;

		// The next calculation is sourced here
		// http://aiskahendra.blogspot.com/2009/01/set-zoom-level-of-google-map-base-on.html
		// I have no idea what it's actually doing !!!
		//
		Double interval = 0.0;

		int mapDisplay = 600; // Minimum of height or width of map in pixels

		// Some sort of tweak !
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

		// Note ... original calc used 8, but I found it worked better with 7
		Double zoom = Math.floor(7 - Math.log(1.6446 * dist / Math.sqrt(2 * (mapDisplay * mapDisplay))) / Math.log(2));

		// Centre the map
		map.setCenter(ctrLat, ctrLng);

		// Set appropriate zoom
		map.setZoom(zoom.intValue());
	}
	
	public static void scaleMap(VMaps map) {
		ArrayList<com.google.maps.model.LatLng> latLngs = new ArrayList<>();
		for (Object o : map.getChildren()) {
			if (o instanceof VMarker) {
				VMarker g = (VMarker) o;
				if(g.getVisible())
					latLngs.add(g.getPosition());
			}
		}
		map.fitBounds(latLngs);
	}
	public static void drawVpolyline(VMaps vmapViewer, List<LatLng> lstpath, int linestate) {
		VPolyline gpline = new VPolyline();
		gpline.setSclass("z-polyline-history");
		gpline.setWidth("2px");
		gpline.setPath((ArrayList<LatLng>) lstpath);
		if (linestate == GpsTrackingMsg.CO_KHACH) {
			gpline.setColor("#009900");
		} else if (linestate == GpsTrackingMsg.KHONG_KHACH) {
			gpline.setColor("#fc060c");
		}
		vmapViewer.appendChild(gpline);
	}
}