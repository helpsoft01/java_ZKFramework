package com.vietek.taxioperation.model;

public class ReportInputDriver {
	private int driverId;
	private String driverName;
	
	public int getDriverId() {
		return driverId;
	}
	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	
	@Override
	public String toString() {
		return ""+driverName;
	}

}
