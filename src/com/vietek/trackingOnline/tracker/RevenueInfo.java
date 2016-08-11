package com.vietek.trackingOnline.tracker;

import java.sql.Timestamp;
import java.text.DecimalFormat;

public class RevenueInfo {
	private int deviceId;
	private Timestamp timeLog;
	private double pinPower;
	private double acquyPower;
	private int digitalStatus;
	private int engine;
	private int door;
	private int airConditioner;
	private int SOS;
	private int digital1;
	private int digital2;
	private int GSM;
	private int GPS;
	private int SDCard;
	private String meterConection;
	private String timeDrivingPerday;
	private String timeDrivingContinuous;
	private int tolalStopCount;
	private int totalDoorCount;
	private int totalSpeedCount;
	private double totalMoney;
	private double totalMoneyShift;
	private int emptyPath;
	private int tripPath;
	private int totalTrip;
	private double moneyTrip;
	private int pathTrip;
	private int taxiStatus;
	private int linkedDevice;
	private int inTrip;
	private int irState;
	private int printState;
	private int irBreak;
	private int shiftID;
	private int stayTimeCounting;
	private String stayTimeString;
	private int doorTimeCounting;
	private int allPath;
	private int allTripPath;
	private Timestamp lastShiftBegin;
	
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public Timestamp getTimeLog() {
		return timeLog;
	}
	public void setTimeLog(Timestamp timeLog) {
		this.timeLog = timeLog;
	}
	public double getPinPower() {
		return pinPower;
	}
	public void setPinPower(int pinPower) {
		double x = pinPower * 6.6 / 65535;
		DecimalFormat df = new DecimalFormat("###.#");
		this.pinPower = Double.parseDouble(df.format(x)+"");
	}
	public double getAcquyPower() {
		return acquyPower;
	}
	public void setAcquyPower(int acquyPower) {
		double x = acquyPower * 3.3 * (4700 + 330) / 65535 / 330;
		DecimalFormat df = new DecimalFormat("###.#");
		this.acquyPower = Double.parseDouble(df.format(x)+"");
	}
	public int getDigitalStatus() {
		return digitalStatus;
	}
	public void setDigitalStatus(int digitalStatus) {
		this.digitalStatus = digitalStatus;
	}
	public int getEngine() {
		return engine;
	}
	public void setEngine(int engine) {
		this.engine = engine;
	}
	public int getDoor() {
		return door;
	}
	public void setDoor(int door) {
		this.door = door;
	}
	public int getAirConditioner() {
		return airConditioner;
	}
	public void setAirConditioner(int airConditioner) {
		this.airConditioner = airConditioner;
	}
	public int getSOS() {
		return SOS;
	}
	public void setSOS(int sOS) {
		SOS = sOS;
	}
	public int getDigital1() {
		return digital1;
	}
	public void setDigital1(int digital1) {
		this.digital1 = digital1;
	}
	public int getDigital2() {
		return digital2;
	}
	public void setDigital2(int digital2) {
		this.digital2 = digital2;
	}
	public int getGSM() {
		return GSM;
	}
	public void setGSM(int gSM) {
		GSM = gSM;
	}
	public int getGPS() {
		return GPS;
	}
	public void setGPS(int gPS) {
		GPS = gPS;
	}
	public int getSDCard() {
		return SDCard;
	}
	public void setSDCard(int sDCard) {
		SDCard = sDCard;
	}
	public String getMeterConection() {
		return meterConection;
	}
	public void setMeterConection(String meterConection) {
		this.meterConection = meterConection;
	}
	public String getTimeDrivingPerday() {
		return timeDrivingPerday;
	}
	public void setTimeDrivingPerday(String timeDrivingPerday) {
		this.timeDrivingPerday = timeDrivingPerday;
	}
	public String getTimeDrivingContinuous() {
		return timeDrivingContinuous;
	}
	public void setTimeDrivingContinuous(String timeDrivingContinuous) {
		this.timeDrivingContinuous = timeDrivingContinuous;
	}
	public int getTolalStopCount() {
		return tolalStopCount;
	}
	public void setTolalStopCount(int tolalStopCount) {
		this.tolalStopCount = tolalStopCount;
	}
	public int getTotalDoorCount() {
		return totalDoorCount;
	}
	public void setTotalDoorCount(int totalDoorCount) {
		this.totalDoorCount = totalDoorCount;
	}
	public int getTotalSpeedCount() {
		return totalSpeedCount;
	}
	public void setTotalSpeedCount(int totalSpeedCount) {
		this.totalSpeedCount = totalSpeedCount;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public double getTotalMoneyShift() {
		return totalMoneyShift;
	}
	public void setTotalMoneyShift(double totalMoneyShift) {
		this.totalMoneyShift = totalMoneyShift;
	}
	public int getEmptyPath() {
		return emptyPath;
	}
	public void setEmptyPath(int emptyPath) {
		this.emptyPath = emptyPath;
	}
	public int getTripPath() {
		return tripPath;
	}
	public void setTripPath(int tripPath) {
		this.tripPath = tripPath;
	}
	public int getTotalTrip() {
		return totalTrip;
	}
	public void setTotalTrip(int totalTrip) {
		this.totalTrip = totalTrip;
	}
	public double getMoneyTrip() {
		return moneyTrip;
	}
	public void setMoneyTrip(double moneyTrip) {
		this.moneyTrip = moneyTrip;
	}
	public int getPathTrip() {
		return pathTrip;
	}
	public void setPathTrip(int pathTrip) {
		this.pathTrip = pathTrip;
	}
	public int getTaxiStatus() {
		return taxiStatus;
	}
	public void setTaxiStatus(int taxiStatus) {
		this.taxiStatus = taxiStatus;
	}
	public int getLinkedDevice() {
		return linkedDevice;
	}
	public void setLinkedDevice(int linkedDevice) {
		this.linkedDevice = linkedDevice;
	}
	public int getInTrip() {
		return inTrip;
	}
	public void setInTrip(int inTrip) {
		this.inTrip = inTrip;
	}
	public int getIrState() {
		return irState;
	}
	public void setIrState(int irState) {
		this.irState = irState;
	}
	public int getPrintState() {
		return printState;
	}
	public void setPrintState(int printState) {
		this.printState = printState;
	}
	public int getShiftID() {
		return shiftID;
	}
	public void setShiftID(int shiftID) {
		this.shiftID = shiftID;
	}
	public int getIrBreak() {
		return irBreak;
	}
	public void setIrBreak(int irBreak) {
		this.irBreak = irBreak;
	}
	public int getStayTimeCounting() {
		return stayTimeCounting;
	}
	public void setStayTimeCounting(int stayTimeCounting) {
		this.stayTimeCounting = stayTimeCounting;
	}
	public String getStayTimeString() {
		return stayTimeString;
	}
	public void setStayTimeString(String stayTimeString) {
		this.stayTimeString = stayTimeString;
	}
	public int getDoorTimeCounting() {
		return doorTimeCounting;
	}
	public void setDoorTimeCounting(int doorTimeCounting) {
		this.doorTimeCounting = doorTimeCounting;
	}
	public int getAllPath() {
		return allPath;
	}
	public void setAllPath(int allPath) {
		this.allPath = allPath;
	}
	public int getAllTripPath() {
		return allTripPath;
	}
	public void setAllTripPath(int allTripPath) {
		this.allTripPath = allTripPath;
	}
	public Timestamp getLastShiftBegin() {
		return lastShiftBegin;
	}
	public void setLastShiftBegin(Timestamp lastShiftBegin) {
		this.lastShiftBegin = lastShiftBegin;
	}
	
	
}
