package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import com.vietek.taxioperation.common.Searchable;

public class RptTotalViolateQc31 extends ReportModel {
	
	@Searchable
	private String driverName;
	private int driverId;
	@Searchable
	private String staffCard;
	@Searchable
	private String driverLicense;
	@Searchable
	private Timestamp timeLog;
	@Searchable
	private String groupName;
	@Searchable
	private int overFourHour;
	@Searchable
	private int overTenHour;
	@Searchable
	private int overSpeed;
	
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public int getDriverId() {
		return driverId;
	}
	public void setDriverId(int driverId) {
		this.driverId = driverId;
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
	public int getOverFourHour() {
		return overFourHour;
	}
	public void setOverFourHour(int overFourHour) {
		this.overFourHour = overFourHour;
	}
	public int getOverTenHour() {
		return overTenHour;
	}
	public void setOverTenHour(int overTenHour) {
		this.overTenHour = overTenHour;
	}
	public int getOverSpeed() {
		return overSpeed;
	}
	public void setOverSpeed(int overSpeed) {
		this.overSpeed = overSpeed;
	}
	
	

}
