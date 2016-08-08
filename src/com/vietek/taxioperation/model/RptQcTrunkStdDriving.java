package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import com.vietek.taxioperation.common.Searchable;

public class RptQcTrunkStdDriving extends ReportModel {
	private int vehicleId;
	private int driverId;
	@Searchable
	private String licensePlate;
	@Searchable
	private String driverName;
	@Searchable
	private String driverLicense;
	@Searchable
	private String typeName;
	@Searchable
	private Timestamp beginTime;
	@Searchable
	private String beginLocation;
	@Searchable
	private String beginAddr;
	@Searchable
	private Timestamp endTime;
	@Searchable
	private String endLocation;
	@Searchable
	private String endAddr;
	@Searchable
	private String timeOverStr;
	@Searchable
	private String stateTrip;
	@Searchable
	private float kmGPS;
	
	private Boolean isHistory;
	
	
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public int getDriverId() {
		return driverId;
	}
	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
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
	public Timestamp getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	public String getBeginLocation() {
		return beginLocation;
	}
	public void setBeginLocation(String beginLocation) {
		this.beginLocation = beginLocation;
	}
	public String getBeginAddr() {
		return beginAddr;
	}
	public void setBeginAddr(String beginAddr) {
		this.beginAddr = beginAddr;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public String getEndLocation() {
		return endLocation;
	}
	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}
	public String getEndAddr() {
		return endAddr;
	}
	public void setEndAddr(String endAddr) {
		this.endAddr = endAddr;
	}
	public String getTimeOverStr() {
		return timeOverStr;
	}
	public void setTimeOverStr(String timeOverStr) {
		this.timeOverStr = timeOverStr;
	}
	public Boolean getIsHistory() {
		return isHistory;
	}
	public void setIsHistory(Boolean isHistory) {
		this.isHistory = isHistory;
	}
	public float getKmGPS() {
		return kmGPS;
	}
	public void setKmGPS(float kmGPS) {
		this.kmGPS = kmGPS;
	}
	public String getStateTrip() {
		return stateTrip;
	}
	public void setStateTrip(String stateTrip) {
		this.stateTrip = stateTrip;
	}
	
	
	

}
