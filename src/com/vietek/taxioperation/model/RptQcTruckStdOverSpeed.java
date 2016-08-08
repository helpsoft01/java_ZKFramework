package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import com.vietek.taxioperation.common.Searchable;

public class RptQcTruckStdOverSpeed extends ReportModel  {
	private int vehicleId;
	@Searchable
	private String licensePlate;
	@Searchable
	private String typeName;
	@Searchable
	private int speed;
	@Searchable
	private int speedLimit;
	@Searchable
	private Timestamp timeStop;
	@Searchable
	private Timestamp timeStart;
	@Searchable
	private String time;
	@Searchable
	private String addrBegin;
	@Searchable
	private String addrFinish;
	@Searchable
	private Float km;
	private Boolean isHistory;
	
	
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
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
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getSpeedLimit() {
		return speedLimit;
	}
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}
	public Timestamp getTimeStop() {
		return timeStop;
	}
	public void setTimeStop(Timestamp timeStop) {
		this.timeStop = timeStop;
	}
	public Timestamp getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(Timestamp timeStart) {
		this.timeStart = timeStart;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAddrBegin() {
		return addrBegin;
	}
	public void setAddrBegin(String addrBegin) {
		this.addrBegin = addrBegin;
	}
	public String getAddrFinish() {
		return addrFinish;
	}
	public void setAddrFinish(String addrFinish) {
		this.addrFinish = addrFinish;
	}
	public Float getKm() {
		return km;
	}
	public void setKm(Float km) {
		this.km = km;
	}
	public Boolean getIsHistory() {
		return isHistory;
	}
	public void setIsHistory(Boolean isHistory) {
		this.isHistory = isHistory;
	}
	
	
	

}
