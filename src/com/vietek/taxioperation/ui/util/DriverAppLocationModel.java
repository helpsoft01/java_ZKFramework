package com.vietek.taxioperation.ui.util;

import org.zkoss.zul.Image;

import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.realtime.Taxi;

public class DriverAppLocationModel {
	private Image imgStatus;
	private Vehicle vehicle;
	private Driver driver;
	private Taxi taxi;
	
	public Image getImgStatus() {
		return imgStatus;
	}
	public void setImgStatus(Image imgStatus) {
		this.imgStatus = imgStatus;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public Taxi getTaxi() {
		return taxi;
	}
	public void setTaxi(Taxi taxi) {
		this.taxi = taxi;
	}
	
}