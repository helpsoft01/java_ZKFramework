package com.vietek.taxioperation.model;

import java.sql.Timestamp;

public class ReportCustomerRelative {
	private int id;
	private String phone;
	private int callAmount;
	private int callSuccess;
	private Timestamp lastOrderTime;
	private String lastOrderAddress;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getCallAmount() {
		return callAmount;
	}
	public void setCallAmount(int callAmount) {
		this.callAmount = callAmount;
	}
	public int getCallSuccess() {
		return callSuccess;
	}
	public void setCallSuccess(int callSuccess) {
		this.callSuccess = callSuccess;
	}
	public Timestamp getLastOrderTime() {
		return lastOrderTime;
	}
	public void setLastOrderTime(Timestamp lastOrderTime) {
		this.lastOrderTime = lastOrderTime;
	}
	public String getLastOrderAddress() {
		return lastOrderAddress;
	}
	public void setLastOrderAddress(String lastOrderAddress) {
		this.lastOrderAddress = lastOrderAddress;
	}
	
	
}
