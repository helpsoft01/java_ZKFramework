package com.vietek.taxioperation.webservice;

import java.io.Serializable;
import java.sql.Timestamp;

public class TaxiResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Timestamp timeLog;
	private Double longitude;
	private Double latitude;
	// private Blob lonDiff;
	// private Blob latDiff;
	private Integer lastGpsSpeed;
	private Integer meterSpeed;
	private String licensePlace;
	private String vehicleNumber;
	private String vinNumber;
	private String groupName;
	private String typeName;
	private String driverName;
	private String registerNumber;
	private String mobileNumber;
	private Integer totalMoney;
	private Integer totalMoneyShift;
	private Integer emptyPath;
	private Integer tripPath;
	private Integer totalTrip;
	private Integer moneyTrip;
	private Integer pathTrip;
	private Integer linkedDevice;
	private Integer inTrip;
	private double dis;
	private double angle;
	
	private int carType;
	
	
	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(Timestamp timeLog) {
		this.timeLog = timeLog;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	// public Blob getLonDiff() {
	// return lonDiff;
	// }
	//
	// public void setLonDiff(Blob lonDiff) {
	// this.lonDiff = lonDiff;
	// }
	//
	// public Blob getLatDiff() {
	// return latDiff;
	// }
	//
	// public void setLatDiff(Blob latDiff) {
	// this.latDiff = latDiff;
	// }

	public Integer getLastGpsSpeed() {
		return lastGpsSpeed;
	}

	public void setLastGpsSpeed(Integer lastGpsSpeed) {
		this.lastGpsSpeed = lastGpsSpeed;
	}

	public Integer getMeterSpeed() {
		return meterSpeed;
	}

	public void setMeterSpeed(Integer meterSpeed) {
		this.meterSpeed = meterSpeed;
	}

	public String getLicensePlace() {
		return licensePlace;
	}

	public void setLicensePlace(String licensePlace) {
		this.licensePlace = licensePlace;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getVinNumber() {
		return vinNumber;
	}

	public void setVinNumber(String vinNumber) {
		this.vinNumber = vinNumber;
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

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getRegisterNumber() {
		return registerNumber;
	}

	public void setRegisterNumber(String registerNumber) {
		this.registerNumber = registerNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Integer getTotalMoneyShift() {
		return totalMoneyShift;
	}

	public void setTotalMoneyShift(Integer totalMoneyShift) {
		this.totalMoneyShift = totalMoneyShift;
	}

	public Integer getEmptyPath() {
		return emptyPath;
	}

	public void setEmptyPath(Integer emptyPath) {
		this.emptyPath = emptyPath;
	}

	public Integer getTripPath() {
		return tripPath;
	}

	public void setTripPath(Integer tripPath) {
		this.tripPath = tripPath;
	}

	public Integer getTotalTrip() {
		return totalTrip;
	}

	public void setTotalTrip(Integer totalTrip) {
		this.totalTrip = totalTrip;
	}

	public Integer getMoneyTrip() {
		return moneyTrip;
	}

	public void setMoneyTrip(Integer moneyTrip) {
		this.moneyTrip = moneyTrip;
	}

	public Integer getPathTrip() {
		return pathTrip;
	}

	public void setPathTrip(Integer pathTrip) {
		this.pathTrip = pathTrip;
	}

	public Integer getLinkedDevice() {
		return linkedDevice;
	}

	public void setLinkedDevice(Integer linkedDevice) {
		this.linkedDevice = linkedDevice;
	}

	public Integer getInTrip() {
		return inTrip;
	}

	public void setInTrip(Integer inTrip) {
		this.inTrip = inTrip;
	}

	public double getDis() {
		return dis;
	}

	public void setDis(double dis) {
		this.dis = dis;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

}
