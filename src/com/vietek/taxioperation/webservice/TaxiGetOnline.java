package com.vietek.taxioperation.webservice;

public class TaxiGetOnline {
	private Integer idVehicle;
	private Double longitude;
	private Double latitude;
	private String vehicleNumber;
	private String licensePlate;
	/**
	 * 1. 4 cho 2. 7 cho
	 */
	private Integer typeName;
	private double distance;

	public Integer getIdVehicle() {
		return idVehicle;
	}

	public void setIdVehicle(Integer idVehicle) {
		this.idVehicle = idVehicle;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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

	public Integer getTypeName() {
		return typeName;
	}

	public void setTypeName(Integer typeName) {
		this.typeName = typeName;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
