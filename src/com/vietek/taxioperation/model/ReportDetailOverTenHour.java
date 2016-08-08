package com.vietek.taxioperation.model;

import java.sql.Timestamp;

public class ReportDetailOverTenHour extends ReportModel {
	
	private String driverName;
	private String staffCard;
	private String driverLicense;
	private Timestamp timeLog;
	private String groupName;
	private String timeOver;
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getStaffCard() {
		return staffCard;
	}
	public void setStaffCard(String staffCard) {
		this.staffCard = staffCard;
	}
	public String getDriverLicense() {
		return driverLicense;
	}
	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}
	public Timestamp getTimeLog() {
		return timeLog;
	}
	public void setTimeLog(Timestamp timeLog) {
		this.timeLog = timeLog;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getTimeOver() {
		return timeOver;
	}
	public void setTimeOver(String timeOver) {
		this.timeOver = timeOver;
	}
	
	

}
