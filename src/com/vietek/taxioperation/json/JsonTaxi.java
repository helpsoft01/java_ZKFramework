package com.vietek.taxioperation.json;

public class JsonTaxi {
	private int id;
	private String driverName;
	private String phoneNumber;
	private String vehicleNumber;
	private String licensePlate;
	private String taxiType;
	private Double latitude;
	private Double longitude;
	private Double angle;
	private Double rate;
	
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getTaxiType() {
		return taxiType;
	}
	public void setTaxiType(String taxiType) {
		this.taxiType = taxiType;
	}
	public Double getAngle() {
		return angle;
	}
	public void setAngle(Double angle) {
		this.angle = angle;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
}
