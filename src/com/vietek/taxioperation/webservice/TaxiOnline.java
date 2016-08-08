package com.vietek.taxioperation.webservice;

import java.io.Serializable;

public class TaxiOnline implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int driver_id;
	private String driver_name;
	private String taxi_number;
	private String phoneNumber;
	private int carType;
	private double angle;
	private double longtitude;
	private double latitude;
	private double rate;
	public int getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(int driver_id) {
		this.driver_id = driver_id;
	}
	public String getDriver_name() {
		return driver_name;
	}
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}
	public String getTaxi_number() {
		return taxi_number;
	}
	public void setTaxi_number(String taxi_number) {
		this.taxi_number = taxi_number;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getCarType() {
		return carType;
	}
	public void setCarType(int carType) {
		this.carType = carType;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	
}
