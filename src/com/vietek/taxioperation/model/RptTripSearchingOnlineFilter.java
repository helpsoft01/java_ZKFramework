package com.vietek.taxioperation.model;

public class RptTripSearchingOnlineFilter {
	private String groupName = "";
	private String vehicleNumber = "";
	private String licensePlate = "";
	private String placeStart = "";
	private String placeFinish = "";
	private String nameDriver = "";
	private String typeVehicle = "";
	private String strdatefinishfilter = "";
	private String strdatestartfilter = "";
	
	
	
	
	public String getStrdatestartfilter() {
		return strdatestartfilter;
	}
	public void setStrdatestartfilter(String strdatestartfilter) {
		this.strdatestartfilter = strdatestartfilter;
	}
	public String getStrdatefinishfilter() {
		return strdatefinishfilter;
	}
	public void setStrdatefinishfilter(String strdatefinishfilter) {
		this.strdatefinishfilter = strdatefinishfilter;
	}
	public String getTypeVehicle() {
		return typeVehicle;
	}
	public void setTypeVehicle(String typeVehicle) {
		this.typeVehicle = typeVehicle;
	}
	public String getPlaceStart() {
		return placeStart;
	}
	public void setPlaceStart(String placeStart) {
		this.placeStart = placeStart;
	}
	public String getPlaceFinish() {
		return placeFinish;
	}
	public void setPlaceFinish(String placeFinish) {
		this.placeFinish = placeFinish;
	}
	public String getNameDriver() {
		return nameDriver;
	}
	public void setNameDriver(String nameDriver) {
		this.nameDriver = nameDriver;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName==null?"":groupName.trim();
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber==null?"":vehicleNumber.trim();
	}
	
	

}
