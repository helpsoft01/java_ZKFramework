package com.vietek.taxioperation.googlemapSearch;

public class PointMap {
	public static final String AddressDefault = null;
	double lat = 0;
	double lng = 0;
	String address = AddressDefault;

	public PointMap() {
		address = AddressDefault;
	}

	public PointMap(double lat, double lng, String address) {
		this.lat = lat;
		this.lng = lng;
		this.address = address;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}