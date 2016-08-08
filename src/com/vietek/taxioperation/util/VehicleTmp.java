package com.vietek.taxioperation.util;

/**
 *
 * Luu thong tin tam thoi cua xe duoc lay online
 * <p>
 * Jul 13, 2016
 * 
 * @author VuD
 *
 */

public class VehicleTmp {
	private int deviceId;
	private double longitude;
	private double latitude;
	private int driverId;
	private String driverName;
	private String phoneOffice;
	private int vehicleId;
	private String liciencePlace;
	private String vehicleNumber;
	private double distance;
	private boolean isApp;

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
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

	public String getPhoneOffice() {
		return phoneOffice;
	}

	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getLiciencePlace() {
		return liciencePlace;
	}

	public void setLiciencePlace(String liciencePlace) {
		this.liciencePlace = liciencePlace;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public boolean isApp() {
		return isApp;
	}

	public void setApp(boolean isApp) {
		this.isApp = isApp;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof VehicleTmp) {
			if (((VehicleTmp) obj).getDeviceId() == this.deviceId) {
				result = true;
			}
		}
		return result;
	}

}
