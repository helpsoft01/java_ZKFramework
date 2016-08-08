package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import com.vietek.taxioperation.common.Searchable;

public class RptQcTruckStdStop extends ReportModel {
	
	@Searchable
	private String licensePlate;
	@Searchable
	private String driverName;
	@Searchable
	private String driverLicense;
	@Searchable
	private String typeName;
	@Searchable
	private Timestamp timeStart;
	@Searchable
	private Timestamp timeEnd;
	@Searchable
	private String timeOverStr;
	@Searchable
	private String location;
	@Searchable
	private String addr;
	
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
	public Timestamp getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(Timestamp timeStart) {
		this.timeStart = timeStart;
	}
	public Timestamp getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(Timestamp timeEnd) {
		this.timeEnd = timeEnd;
	}
	public String getTimeOverStr() {
		return timeOverStr;
	}
	public void setTimeOverStr(String timeOverStr) {
		this.timeOverStr = timeOverStr;
	}
	public String getLocation() {
		return location;
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
	
	

}
