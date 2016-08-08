package com.vietek.taxioperation.model;

public class ReportQcParkingArea {
	private int pakingareaId;
	private String parkingareaCode;
	private String parkingareaName;
	private int vehicleGroupId;
	public int getPakingareaId() {
		return pakingareaId;
	}
	public void setPakingareaId(int pakingareaId) {
		this.pakingareaId = pakingareaId;
	}
	public String getParkingareaCode() {
		return parkingareaCode;
	}
	public void setParkingareaCode(String parkingareaCode) {
		this.parkingareaCode = parkingareaCode;
	}
	public String getParkingareaName() {
		return parkingareaName;
	}
	public void setParkingareaName(String parkingareaName) {
		this.parkingareaName = parkingareaName;
	}
	public int getVehicleGroupId() {
		return vehicleGroupId;
	}
	public void setVehicleGroupId(int vehicleGroupId) {
		this.vehicleGroupId = vehicleGroupId;
	}
	
	@Override
	public String toString() {
		return "" + parkingareaName;
	}

}
