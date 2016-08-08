package com.vietek.tracking.ui.model;

import java.sql.Timestamp;

public class FindingResult {
 private int TripID;
 private int VehicleID;
 private String VehicleNumber;
 private String LicensePlate;
 private Timestamp BeginTime;
 private Timestamp StopTime;
 private Float Money;
 private String BeginPosition;
 private String StopPosition;
 private String FullPath;
public int getTripID() {
	return TripID;
}
public void setTripID(int tripID) {
	TripID = tripID;
}
public int getVehicleID() {
	return VehicleID;
}
public void setVehicleID(int vehicleID) {
	VehicleID = vehicleID;
}
public String getVehicleNumber() {
	return VehicleNumber;
}
public void setVehicleNumber(String vehicleNumber) {
	VehicleNumber = vehicleNumber;
}
public String getLicensePlate() {
	return LicensePlate;
}
public void setLicensePlate(String licensePlate) {
	LicensePlate = licensePlate;
}
public Timestamp getBeginTime() {
	return BeginTime;
}
public void setBeginTime(Timestamp beginTime) {
	BeginTime = beginTime;
}
public Timestamp getStopTime() {
	return StopTime;
}
public void setStopTime(Timestamp stopTime) {
	StopTime = stopTime;
}
public Float getMoney() {
	return Money;
}
public void setMoney(Float money) {
	Money = money;
}
public String getBeginPosition() {
	return BeginPosition;
}
public void setBeginPosition(String beginPosition) {
	BeginPosition = beginPosition;
}
public String getStopPosition() {
	return StopPosition;
}
public void setStopPosition(String stopPosition) {
	StopPosition = stopPosition;
}
public String getFullPath() {
	return FullPath;
}
public void setFullPath(String fullPath) {
	FullPath = fullPath;
}
 
 
}
