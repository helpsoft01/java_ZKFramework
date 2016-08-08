package com.vietek.trackingOnline.tracker;

import java.sql.Timestamp;

public class TrackingRDS2Json {
	private int deviceId;
	private Timestamp timeLog;
	private int driverId;
	private String driverName;
	private String phoneOffcice;
	private String phoneNumber;
	private int vehicleId;
	private String vehicleNumber;
	private int vehicleGroup;
	private int carType;
	private int inTrip;
	private double longitude;
	private double latitude;
	private double angle;
	private int gpsSpeed;
	private int metterSpeed;
	private int engine;
	private String licensePlate;

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public Timestamp getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(Timestamp timeLog) {
		this.timeLog = timeLog;
	}

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

	public String getPhoneOffcice() {
		return phoneOffcice;
	}

	public void setPhoneOffcice(String phoneOffcice) {
		this.phoneOffcice = phoneOffcice;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public int getVehicleGroup() {
		return vehicleGroup;
	}

	public void setVehicleGroup(int vehicleGroup) {
		this.vehicleGroup = vehicleGroup;
	}

	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

	public Integer getdeltaSpeed() {
		return gpsSpeed - metterSpeed;
	}

	public int getInTrip() {
		return inTrip;
	}

	public void setInTrip(int inTrip) {
		this.inTrip = inTrip;
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

	public int getGpsSpeed() {
		return gpsSpeed;
	}

	public void setGpsSpeed(int gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}

	public int getMetterSpeed() {
		return metterSpeed;
	}

	public void setMetterSpeed(int metterSpeed) {
		this.metterSpeed = metterSpeed;
	}

	public int getEngine() {
		return engine;
	}

	public void setEngine(int engine) {
		this.engine = engine;
	}

	public boolean isLostGPS() {
		boolean islostgps = false;
		if (latitude == 0 || longitude == 0) {
			islostgps = true;
		}
		return islostgps;
	}
	@Override
	public String toString() {
		return vehicleNumber + "|" + licensePlate + "|" + timeLog + "|" + latitude + "|" + longitude;
	}
}