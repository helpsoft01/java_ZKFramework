package com.vietek.taxioperation.model;

public class ReportInputVehicleID {
	private int VehicleID;
	private String VehicleNumber;
	private String LicensePlate;
	private int VehicleGroupID;
	
	
	public int getVehicleGroupID() {
		return VehicleGroupID;
	}
	public void setVehicleGroupID(int vehicleGroupID) {
		VehicleGroupID = vehicleGroupID;
	}
	public int getVehicleID() {
		return VehicleID;
	}
	public void setVehicleID(int vehicleID) {
		VehicleID = vehicleID;
	}
	public String getVehicleNumber() {
		return VehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		VehicleNumber = vehicleNumber;
	}
	public String getLicensePlate() {
		return LicensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		LicensePlate = licensePlate;
	}
}
