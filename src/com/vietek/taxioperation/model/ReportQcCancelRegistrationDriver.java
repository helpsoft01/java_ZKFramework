package com.vietek.taxioperation.model;

import java.sql.Timestamp;

public class ReportQcCancelRegistrationDriver {
	private String phoneCustomer;
	private Timestamp timeorder;
	private String placeorder;
	private String nameDriver;
	private int vehicleNumber;
	private String licensePlate;
//	private Timestamp timeRegister;
//	private Timestamp timeCancel;
	private String timeRegister;
	private String timeCancel;
	private String reasonCancel;
	public String getPhoneCustomer() {
		return this.phoneCustomer;
	}
	public void setPhoneCustomer(String phoneCustomer) {
		this.phoneCustomer = phoneCustomer;
	}
	public Timestamp getTimeorder() {
		return this.timeorder;
	}
	public void setTimeorder(Timestamp timeorder) {
		this.timeorder = timeorder;
	}
	public String getPlaceorder() {
		return this.placeorder;
	}
	public void setPlaceorder(String placeorder) {
		this.placeorder = placeorder;
	}
	public String getNameDriver() {
		return this.nameDriver;
	}
	public void setNameDriver(String nameDriver) {
		this.nameDriver = nameDriver;
	}
	public int getVehicleNumber() {
		return this.vehicleNumber;
	}
	public void setVehicleNumber(int vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getLicensePlate() {
		return this.licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
//	public Timestamp getTimeRegister() {
//		return this.timeRegister;
//	}
//	public void setTimeRegister(Timestamp timeRegister) {
//		this.timeRegister = timeRegister;
//	}
//	public Timestamp getTimeCancel() {
//		return this.timeCancel;
//	}
//	public void setTimeCancel(Timestamp timeCancel) {
//		this.timeCancel = timeCancel;
//	}
	public String getReasonCancel() {
		return this.reasonCancel;
	}
	public String getTimeRegister() {
		return this.timeRegister;
	}
	public void setTimeRegister(String timeRegister) {
		this.timeRegister = timeRegister;
	}
	public String getTimeCancel() {
		return this.timeCancel;
	}
	public void setTimeCancel(String timeCancel) {
		this.timeCancel = timeCancel;
	}
	public void setReasonCancel(String reasonCancel) {
		this.reasonCancel = reasonCancel;
	}
	
	

}
