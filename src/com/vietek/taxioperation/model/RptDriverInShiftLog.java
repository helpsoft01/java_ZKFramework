package com.vietek.taxioperation.model;

import java.sql.Timestamp;

public class RptDriverInShiftLog {
	private Timestamp timeLog;
	private String licensePlate;
	private String vehicleNumber;
	private String staffCard;
	private String driverName;
	private String phoneNumber;
	private String driverState;
	private String groupName;
	private String typeName;
	private String parkingName;
	public Timestamp getTimeLog() {
		return timeLog;
	}
	public void setTimeLog(Timestamp timeLog) {
		this.timeLog = timeLog;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getStaffCard() {
		return staffCard;
	}
	public void setStaffCard(String staffCard) {
		this.staffCard = staffCard;
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
	public String getDriverState() {
		return driverState;
	}
	public void setDriverState(String driverState) {
		this.driverState = driverState;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getParkingName() {
		return parkingName;
	}
	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
	}
	
	
}
