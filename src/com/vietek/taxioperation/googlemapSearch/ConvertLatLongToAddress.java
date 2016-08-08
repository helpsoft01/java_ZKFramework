package com.vietek.taxioperation.googlemapSearch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.vietek.taxioperation.ui.controller.TaxiOrdersDetailForm;
import com.vietek.taxioperation.util.Address;
import com.vietek.taxioperation.util.GoogleKey;

public class ConvertLatLongToAddress implements Runnable {
	private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";

	Desktop desktop;
	double lat;
	double log;
	private TaxiOrdersDetailForm form;
	public ConvertLatLongToAddress(double lat, double log, Desktop desktop, TaxiOrdersDetailForm form) {
		this.desktop = desktop;
		this.lat = lat;
		this.log = log;
		this.form = form;
	}

	@Override
	public void run() {
		String latlongString = "";
		URL url;
		URLConnection conn;
		InputStream in;
		try {

			latlongString = Double.toString(this.lat) + "," + Double.toString(this.log);

			// Open the Connection
			url = new URL(URL + "?latlng=" + latlongString + "&key=" + GoogleKey.getKey());
			conn = url.openConnection();
			in = conn.getInputStream();

			JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(in, "UTF-8")));
			GoogleResponseAddress r = (new Gson().fromJson(reader, GoogleResponseAddress.class));

			if (r.getStatus().equals("OK"))

				if (r.getResults().length > 0) {

					Address address = new Address();

					address.setName(r.getResults()[0].getFormatted_address());
					address.setLatitude(r.getResults()[0].getGeometry().getLocation().lat);
					address.setLongitude(r.getResults()[0].getGeometry().getLocation().lng);
					
					if (!desktop.isServerPushEnabled()) {
						desktop.enableServerPush(true);
					}
					Executions.schedule(desktop, new EventListener<Event>() {

						@Override
						public void onEvent(Event arg0) throws Exception {
							form.didUpdateStartAddressName(address.getName());
						}
					}, null);
				}

			in.close();
			// return response;
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			
		}
	}
}
