package com.vietek.taxioperation.model;

import java.sql.Timestamp;

public class RptGetShiftInfo {
	private int shiftId;
	private Timestamp timeBegin;
	private Timestamp timeEnd;
	private String licensePlate;
	private String vehicleNumber;
	private String driverName;
	private String staffCard;
	private String businessType;
	private int hourOfShift;
	private float path;
	private float tripPath;
	private int tripNumber;
	private int money;
	private int reduceMoney;
	private int realShiftMoney;
	private int confirm;
	private String confirmStr;
	private int moneyMeter;
	private int print;
	private int pulseCut;
	private int pulseLance;
	private int gpsLost;
	private int powerLost;

	public int getShiftId() {
		return shiftId;
	}

	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}

	public Timestamp getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(Timestamp timeBegin) {
		this.timeBegin = timeBegin;
	}

	public Timestamp getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Timestamp timeEnd) {
		this.timeEnd = timeEnd;
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

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public int getHourOfShift() {
		return hourOfShift;
	}

	public void setHourOfShift(int hourOfShift) {
		this.hourOfShift = hourOfShift;
	}

	public float getPath() {
		return path;
	}

	public void setPath(float path) {
		this.path = path;
	}

	public float getTripPath() {
		return tripPath;
	}

	public void setTripPath(float tripPath) {
		this.tripPath = tripPath;
	}

	public int getTripNumber() {
		return tripNumber;
	}

	public void setTripNumber(int tripNumber) {
		this.tripNumber = tripNumber;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getReduceMoney() {
		return reduceMoney;
	}

	public void setReduceMoney(int reduceMoney) {
		this.reduceMoney = reduceMoney;
	}

	public int getRealShiftMoney() {
		return realShiftMoney;
	}

	public void setRealShiftMoney(int realShiftMoney) {
		this.realShiftMoney = realShiftMoney;
	}

	public int getConfirm() {
		return confirm;
	}

	public void setConfirm(int confirm) {
		this.confirm = confirm;
	}

	public String getConfirmStr() {
		return confirmStr;
	}

	public void setConfirmStr(String confirmStr) {
		this.confirmStr = confirmStr;
	}

	public int getMoneyMeter() {
		return moneyMeter;
	}

	public void setMoneyMeter(int moneyMeter) {
		this.moneyMeter = moneyMeter;
	}

	public int getPrint() {
		return print;
	}

	public void setPrint(int print) {
		this.print = print;
	}

	public int getPulseCut() {
		return pulseCut;
	}

	public void setPulseCut(int pulseCut) {
		this.pulseCut = pulseCut;
	}

	public int getPulseLance() {
		return pulseLance;
	}

	public void setPulseLance(int pulseLance) {
		this.pulseLance = pulseLance;
	}

	public int getGpsLost() {
		return gpsLost;
	}

	public void setGpsLost(int gpsLost) {
		this.gpsLost = gpsLost;
	}

	public int getPowerLost() {
		return powerLost;
	}

	public void setPowerLost(int powerLost) {
		this.powerLost = powerLost;
	}

}
