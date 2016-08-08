package com.vietek.taxioperation.model;

public class ReportQcTimeWaitOfCustomer {
	
	private int id;
	private String numberMobile;
//	private Timestamp beginOrderTime;
	private String beginOrderTime;
	private String beginOrderAddress;
	private String minute;
	private String repeatTime;
	private String status;
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumberMobile() {
		return this.numberMobile;
	}
	public void setNumberMobile(String numberMobile) {
		this.numberMobile = numberMobile;
	}
//	public Timestamp getBeginOrderTime() {
//		return this.beginOrderTime;
//	}
//	public void setBeginOrderTime(Timestamp beginOrderTime) {
//		this.beginOrderTime = beginOrderTime;
//	}
	public String getBeginOrderAddress() {
		return this.beginOrderAddress;
	}
	public String getBeginOrderTime() {
		return this.beginOrderTime;
	}
	public void setBeginOrderTime(String beginOrderTime) {
		this.beginOrderTime = beginOrderTime;
	}
	public void setBeginOrderAddress(String beginOrderAddress) {
		this.beginOrderAddress = beginOrderAddress;
	}
	public String getMinute() {
		return this.minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getRepeatTime() {
		return this.repeatTime;
	}
	public void setRepeatTime(String repeatTime) {
		this.repeatTime = repeatTime;
	}
	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
