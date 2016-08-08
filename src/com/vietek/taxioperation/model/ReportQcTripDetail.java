package com.vietek.taxioperation.model;

import java.sql.Timestamp;

public class ReportQcTripDetail {
	private int id;
	private String beginOrderAddress;
	private Timestamp beginOrderTime;
	private String phoneNumber;
	private Timestamp startRegisterTime;
	private String listRegisterVeh;
	private Timestamp timeIsUpdated;
	private int taxi_id;
	private String selectedVeh;
	private String orderCarType;
	private String cancelReason;
	private String OrderStatus;
	
	
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBeginOrderAddress() {
		return this.beginOrderAddress;
	}
	public void setBeginOrderAddress(String beginOrderAddress) {
		this.beginOrderAddress = beginOrderAddress;
	}
	public Timestamp getBeginOrderTime() {
		return this.beginOrderTime;
	}
	public void setBeginOrderTime(Timestamp beginOrderTime) {
		this.beginOrderTime = beginOrderTime;
	}
	public String getOrderCarType() {
		return orderCarType;
	}
	public void setOrderCarType(String orderCarType) {
		this.orderCarType = orderCarType;
	}
	public String getPhoneNumber() {
		if(this.phoneNumber == null){
			return "";
		}
		return this.phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Timestamp getStartRegisterTime() {	
		return this.startRegisterTime;
	}
	public void setStartRegisterTime(Timestamp startRegisterTime) {
		this.startRegisterTime = startRegisterTime;
	}
	public String getListRegisterVeh() {
		if(this.listRegisterVeh == null){
			return "";
		}
		return this.listRegisterVeh;
	}
	public void setListRegisterVeh(String listRegisterVeh) {
		this.listRegisterVeh = listRegisterVeh;
	}
	public Timestamp getTimeIsUpdated() {
		return this.timeIsUpdated;
	}
	public void setTimeIsUpdated(Timestamp timeIsUpdated) {
		this.timeIsUpdated = timeIsUpdated;
	}
	public int getTaxi_id() {
		return this.taxi_id;
	}
	public void setTaxi_id(int taxi_id) {
		this.taxi_id = taxi_id;
	}
	public String getSelectedVeh() {
		if(this.selectedVeh == null){
			return "";
		}
		return this.selectedVeh;
	}
	public void setSelectedVeh(String selectedVeh) {
		this.selectedVeh = selectedVeh;
	}
	
	

}
