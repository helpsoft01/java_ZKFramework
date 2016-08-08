package com.vietek.taxioperation.model;

public class TmpPlace {
	String des;
	String placeId;
	Double lat;
	Double lng;
	int type;
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    final TmpPlace other = (TmpPlace) obj;
	    if (this.des.equals(other.getDes())) return true;
	    else return false;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return des;
	}
}
