package com.vietek.taxioperation.model;

import java.sql.Timestamp;

public class TripInsideArrangement {
	private String vehicleId;
	private String vehicleNumber;
	private String licensePlate;
	private Timestamp beginTime;
	private String beginPosition;
	private Timestamp stopTime;
	private String stopPosition;
	private String money;
	private Double path;
	private String tripId;
	
	
	public Timestamp getStopTime() {
		return stopTime;
	}
	public void setStopTime(Timestamp stopTime) {
		this.stopTime = stopTime;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public Timestamp getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	public String getBeginPosition() {
		return beginPosition;
	}
	public void setBeginPosition(String beginPosition) {
		this.beginPosition = beginPosition;
	}
	public String getStopPosition() {
		return stopPosition;
	}
	public void setStopPosition(String stopPosition) {
		this.stopPosition = stopPosition;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public Double getPath() {
		return path;
	}
	public void setPath(Double path) {
		this.path = path;
	}
	public String getTripId() {
		return tripId;
	}
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	
	
}
