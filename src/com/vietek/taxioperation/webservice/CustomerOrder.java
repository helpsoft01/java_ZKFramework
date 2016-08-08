package com.vietek.taxioperation.webservice;

import java.io.Serializable;
import java.sql.Timestamp;

public class CustomerOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int customerId;
	private String name;
	private String phoneNumber;
	private double beginOrderLat;
	private double beginOrderLong;
	private Timestamp beginOrderTime;
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public double getBeginOrderLat() {
		return beginOrderLat;
	}
	public void setBeginOrderLat(double beginOrderLat) {
		this.beginOrderLat = beginOrderLat;
	}
	public double getBeginOrderLong() {
		return beginOrderLong;
	}
	public void setBeginOrderLong(double beginOrderLong) {
		this.beginOrderLong = beginOrderLong;
	}
	public Timestamp getBeginOrderTime() {
		return beginOrderTime;
	}
	public void setBeginOrderTime(Timestamp beginOrderTime) {
		this.beginOrderTime = beginOrderTime;
	}
	
}
