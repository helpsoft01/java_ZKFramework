package com.vietek.taxioperation.model;

import com.vietek.taxioperation.common.Searchable;

public class RptQcTruckStdActivityByVehicle extends ReportModel {
	
	@Searchable
	private String nameDriver;
	@Searchable
	private String driverLicense;
	@Searchable
	private int overFourHour;
	@Searchable
	private String licensePlate;
	@Searchable
	private String typeName;
	@Searchable
	private int kmGps;
	@Searchable
	private float fivePerPath;
	@Searchable
	private int fiveTime;
	@Searchable
	private float tenPerPath;
	@Searchable
	private int tenTime;
	@Searchable
	private float twentyPerPath;
	@Searchable
	private int twentyTime;
	@Searchable
	private float thirthPerPath;
	@Searchable
	private int thirthTime;
	@Searchable
	private int stopCounting;
	@Searchable
	private String note;
	
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getKmGps() {
		return kmGps;
	}
	public void setKmGps(int kmGps) {
		this.kmGps = kmGps;
	}
	public float getFivePerPath() {
		return fivePerPath;
	}
	public void setFivePerPath(float fivePerPath) {
		this.fivePerPath = fivePerPath;
	}
	public int getFiveTime() {
		return fiveTime;
	}
	public void setFiveTime(int fiveTime) {
		this.fiveTime = fiveTime;
	}
	public float getTenPerPath() {
		return tenPerPath;
	}
	public void setTenPerPath(float tenPerPath) {
		this.tenPerPath = tenPerPath;
	}
	public int getTenTime() {
		return tenTime;
	}
	public void setTenTime(int tenTime) {
		this.tenTime = tenTime;
	}
	public float getTwentyPerPath() {
		return twentyPerPath;
	}
	public void setTwentyPerPath(float twentyPerPath) {
		this.twentyPerPath = twentyPerPath;
	}
	public int getTwentyTime() {
		return twentyTime;
	}
	public void setTwentyTime(int twentyTime) {
		this.twentyTime = twentyTime;
	}
	public float getThirthPerPath() {
		return thirthPerPath;
	}
	public void setThirthPerPath(float thirthPerPath) {
		this.thirthPerPath = thirthPerPath;
	}
	public int getThirthTime() {
		return thirthTime;
	}
	public void setThirthTime(int thirthTime) {
		this.thirthTime = thirthTime;
	}
	public int getStopCounting() {
		return stopCounting;
	}
	public void setStopCounting(int stopCounting) {
		this.stopCounting = stopCounting;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNameDriver() {
		return nameDriver;
	}
	public void setNameDriver(String nameDriver) {
		this.nameDriver = nameDriver;
	}
	public String getDriverLicense() {
		return driverLicense;
	}
	public void setDriverLicense(String driverLicense) {
		this.driverLicense = driverLicense;
	}
	public int getOverFourHour() {
		return overFourHour;
	}
	public void setOverFourHour(int overFourHour) {
		this.overFourHour = overFourHour;
	}
	
	
}
