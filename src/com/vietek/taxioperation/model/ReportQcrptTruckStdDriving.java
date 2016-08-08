package com.vietek.taxioperation.model;

import java.util.Date;

public class ReportQcrptTruckStdDriving {
	private int rptID;
	private String companyName;
	private String groupName;
	private String vehicletruckNumber;
	private String driverName;
	private String licensePlate;
	private int vehicleNumber;
	private String driverLicense;
	private String typeName;
	private Date beginTime;
	private Date endTime;
	private int timeOver;
	private String timeOverStr;
	private String beginLocation;
	private String endLocation;
	private String beginAddr;
	private String finishAddr;

	public int getRptID() {
		return rptID;
	}

	public void setRptID(int rptID) {
		this.rptID = rptID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getVehicletruckNumber() {
		return vehicletruckNumber;
	}

	public void setVehicletruckNumber(String vehicletruckNumber) {
		this.vehicletruckNumber = vehicletruckNumber;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public int getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(int vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getDriverLicense() {
		return driverLicense;
	}

	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getTimeOver() {
		return timeOver;
	}

	public void setTimeOver(int timeOver) {
		this.timeOver = timeOver;
	}

	public String getTimeOverStr() {
		return timeOverStr;
	}

	public void setTimeOverStr(String timeOverStr) {
		this.timeOverStr = timeOverStr;
	}

	public String getBeginLocation() {
		return beginLocation;
	}

	public void setBeginLocation(String beginLocation) {
		this.beginLocation = beginLocation;
	}

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	public String getBeginAddr() {
		return beginAddr;
	}

	public void setBeginAddr(String beginAddr) {
		this.beginAddr = beginAddr;
	}

	public String getFinishAddr() {
		return finishAddr;
	}

	public void setFinishAddr(String finishAddr) {
		this.finishAddr = finishAddr;
	}
}
