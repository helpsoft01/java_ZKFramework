package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import com.vietek.taxioperation.common.Searchable;

public class RptDriverInShift extends ReportModel {
	private Timestamp issueDate;
	@Searchable
	private String licensePlate;
	@Searchable
	private String vehicleNumber;
	@Searchable
	private String staffCard;
	@Searchable
	private String driverName;
	@Searchable
	private String phoneNumber;
	@Searchable
	private String driverState;
	@Searchable
	private String timeDrivingContinuous;
	@Searchable
	private String timeDrivingPerDay;
	@Searchable
	private String parkingName;
	@Searchable
	private String groupName;
	@Searchable
	private String typeName;
	@Searchable
	private Timestamp timeLog;
	
	private int inworkshop;
	private int inaccident;
	private int inparking;
	@Searchable
	private String note;
	
	
	
	
	public int getInworkshop() {
		return inworkshop;
	}
	public void setInworkshop(int inworkshop) {
		this.inworkshop = inworkshop;
	}
	public int getInaccident() {
		return inaccident;
	}
	public void setInaccident(int inaccident) {
		this.inaccident = inaccident;
	}
	public int getInparking() {
		return inparking;
	}
	public void setInparking(int inparking) {
		this.inparking = inparking;
	}
	public Timestamp getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Timestamp issueDate) {
		this.issueDate = issueDate;
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
	public String getTimeDrivingContinuous() {
		return timeDrivingContinuous;
	}
	public void setTimeDrivingContinuous(String timeDrivingContinuous) {
		this.timeDrivingContinuous = timeDrivingContinuous;
	}
	public String getTimeDrivingPerDay() {
		return timeDrivingPerDay;
	}
	public void setTimeDrivingPerDay(String timeDrivingPerDay) {
		this.timeDrivingPerDay = timeDrivingPerDay;
	}
	public String getParkingName() {
		return parkingName;
	}
	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Timestamp getTimeLog() {
		return timeLog;
	}
	public void setTimeLog(Timestamp timeLog) {
		this.timeLog = timeLog;
	}
	
	

}
