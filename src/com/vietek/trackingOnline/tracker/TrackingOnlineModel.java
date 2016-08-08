package com.vietek.trackingOnline.tracker;

import java.sql.Timestamp;

import org.zkoss.zul.Image;

public class TrackingOnlineModel {
	private Integer deviceId;
	private Image image;
	private String taxiNumber;
	private Integer speed;
	private Timestamp time;
	
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public String getTaxiNumber() {
		return taxiNumber;
	}
	public void setTaxiNumber(String taxiNumber) {
		this.taxiNumber = taxiNumber;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
}
