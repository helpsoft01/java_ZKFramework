package com.vietek.taxioperation.model;

/**
 *
 * @author VuD
 */
public class VehicleInfoJson {
	private int deviceId;
	private int driverId;
	private int vehicleId;
	private String driverName;
	private String phoneNumber;
	private String phoneOffcice;
	private String vehicleNumber;
	private String licensePlace;
	private int carType;
	private double longitude;
	private double latitude;
	private double angle;
	private double rate;
	private double distance;

	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

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

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getLicensePlace() {
		return licensePlace;
	}

	public void setLicensePlace(String licensePlace) {
		this.licensePlace = licensePlace;
	}

	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getPhoneOffcice() {
		return phoneOffcice;
	}

	public void setPhoneOffcice(String phoneOffcice) {
		this.phoneOffcice = phoneOffcice;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

}
