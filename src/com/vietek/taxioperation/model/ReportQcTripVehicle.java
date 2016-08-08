package com.vietek.taxioperation.model;

import com.vietek.taxioperation.common.Searchable;

public class ReportQcTripVehicle extends ReportModel {
	@Searchable
	private String timeLog;
	@Searchable
	private String location;
	@Searchable
	private String addr;
	@Searchable
	private String note;
	@Searchable
	private String gpsSpeed;
	
	
	public String getGpsSpeed() {
		return gpsSpeed;
	}
	public void setGpsSpeed(String gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}
	public String getLocation() {
		return location;
	}
	public String getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(String timeLog) {
		this.timeLog = timeLog;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
