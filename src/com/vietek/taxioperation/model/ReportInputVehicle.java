package com.vietek.taxioperation.model;

public class ReportInputVehicle {
	private int vehicleId;
	private int vehicleNumber;
	private String licensePlate;
	private int deviceId;
	
	
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public int getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(int vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
//	public int getVehicleGroupId() {
//		return vehicleGroupId;
//	}
//	public void setVehicleGroupId(int vehicleGroupId) {
//		this.vehicleGroupId = vehicleGroupId;
//	}
	
	@Override
	public String toString() {
		return "" + licensePlate;
	}
	
	

}
