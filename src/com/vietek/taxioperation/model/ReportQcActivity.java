package com.vietek.taxioperation.model;

public class ReportQcActivity {
	
	public int driverId;
	public String driverName;
	public String driverCard;
	public String groupName;
	public int over4hour;
	public int over10hour;
	public int overspeed;
	
	public ReportQcActivity() {
		// TODO Auto-generated constructor stub
	}

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

	public String getDriverCard() {
		return driverCard;
	}

	public void setDriverCard(String driverCard) {
		this.driverCard = driverCard;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getOver4hour() {
		return over4hour;
	}

	public void setOver4hour(int over4hour) {
		this.over4hour = over4hour;
	}

	public int getOver10hour() {
		return over10hour;
	}

	public void setOver10hour(int over10hour) {
		this.over10hour = over10hour;
	}

	public int getOverspeed() {
		return overspeed;
	}

	public void setOverspeed(int overspeed) {
		this.overspeed = overspeed;
	}	
}
