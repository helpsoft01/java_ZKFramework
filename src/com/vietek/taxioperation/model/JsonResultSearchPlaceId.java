package com.vietek.taxioperation.model;

public class JsonResultSearchPlaceId {
	String[] address_components;
	String adr_address;
	String formatted_address;
	LocationJsonSearchPlaceId location;
	String icon;
	String id;
	String name;
	String place_id;
	String reference;
	String scope;
	String[] types;
	String url;
	String vicinity;
	public String[] getAddress_components() {
		return address_components;
	}
	public void setAddress_components(String[] address_components) {
		this.address_components = address_components;
	}
	public String getAdr_address() {
		return adr_address;
	}
	public void setAdr_address(String adr_address) {
		this.adr_address = adr_address;
	}
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	public LocationJsonSearchPlaceId getLocation() {
		return location;
	}
	public void setLocation(LocationJsonSearchPlaceId location) {
		this.location = location;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlace_id() {
		return place_id;
	}
	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String[] getTypes() {
		return types;
	}
	public void setTypes(String[] types) {
		this.types = types;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVicinity() {
		return vicinity;
	}
	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
}
