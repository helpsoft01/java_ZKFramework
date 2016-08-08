package com.vietek.taxioperation.ui.editor;

import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.codehaus.jackson.map.ObjectMapper;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.googlemapSearch.GoogleResponse;
import com.vietek.taxioperation.googlemapSearch.PointMap;
import com.vietek.taxioperation.googlemapSearch.Result;

public class SearchAddressEditor extends Combobox implements
		EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7109121297499114345L;

	private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json";
	private PointMap pMap;

	GoogleResponse response = null;

	private String plateholder;

	public String getPlateholder() {
		return plateholder;
	}

	public void setPlateholder(String plateholder) {
		this.plateholder = plateholder;
	}

	public GoogleResponse getResponse() {
		return response;
	}

	public void setResponse(GoogleResponse response) {
		this.response = response;
	}

	public SearchAddressEditor() {
		pMap = new PointMap();
		init();
	}

	public SearchAddressEditor(PointMap pMap) {
		this.pMap = pMap;
		init();
	}

	private void init() {
		this.addEventListener(Events.ON_CHANGING, this);
		this.addEventListener(Events.ON_CHANGE, this);
		this.addEventListener(Events.ON_OK, this);
		this.addEventListener(Events.ON_BLUR, this);
	}

	public PointMap getPointMap() {

		PointMap pMap = new PointMap();
		java.net.URL url = null;
		// Open the Connection
		URLConnection conn = null;
		InputStream in = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			/*
			 * Create an java.net.URL object by passing the request URL in
			 * constructor. Here you can see I am converting the fullAddress
			 * String in UTF-8 format. You will get Exception if you don't
			 * convert your address in UTF-8 format. Perhaps google loves UTF-8
			 * format. :) In parameter we also need to pass "sensor" parameter.
			 * sensor (required parameter) — Indicates whether or not the
			 * geocoding request comes from a device with a location sensor.
			 * This value must be either true or false.
			 */

			String fullAddress = getValue().trim();

			url = new java.net.URL(URL + "?address="
					+ URLEncoder.encode(fullAddress, "UTF-8") + "&sensor=false");
			conn = url.openConnection();
			in = conn.getInputStream();
			response = (GoogleResponse) mapper.readValue(in,
					GoogleResponse.class);

			if (response.getStatus().equals("OK")) {
				for (Result result : response.getResults()) {
					pMap.setLat(Double.parseDouble(result.getGeometry()
							.getLocation().getLat()));
					pMap.setLng(Double.parseDouble(result.getGeometry()
							.getLocation().getLng()));
				}
			} else {
				AppLogger.logDebug.info(" - response.getStatus(): "
						+ response.getStatus());
			}

			in.close();

		} catch (Exception e1) {
			AppLogger.logDebug.error("", e1);
		}

		this.pMap = pMap;
		return pMap;

	}

	public PointMap getAddress() {
		return this.pMap;
	}

	public void setValue(PointMap pMap) {
		this.pMap = pMap;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {

	}
}
