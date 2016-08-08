package com.vietek.taxioperation.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ItemAddressPlace {

	String formatted_address;
	String place_id;
	Bounds[] geometry;

	@JsonIgnore
	Object[] address_components;
	@JsonIgnore
	String partial_match;
	@JsonIgnore
	Object[] types;

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public Object[] getAddress_components() {
		return address_components;
	}

	public void setAddress_components(Object[] address_components) {
		this.address_components = address_components;
	}

	public String getPartial_match() {
		return partial_match;
	}

	public void setPartial_match(String partial_match) {
		this.partial_match = partial_match;
	}

	public Object[] getTypes() {
		return types;
	}

	public void setTypes(Object[] types) {
		this.types = types;
	}

	class Bounds {
		Location location;
		Northeast[] northeast;
		String location_type;
		Viewport[] viewport;
	}

	class Northeast {
		double lat;
		double lng;

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

	}

	class Location {
		double lat;
		double lng;

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
	}

	class Viewport {
		Northeast northeast;
		Southwest southwest;
	}

	class Southwest {
		double lat;
		double lng;

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
	}
}
