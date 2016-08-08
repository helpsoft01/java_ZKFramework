package com.vietek.taxioperation.model;

import java.sql.Timestamp;

public class RiderVariationModel {
	private String timeCode;
	private Timestamp timelog;
	private int normalOrder;
	private int webOrder;
	private int mobileOrder;
	private int otherOrder;
		
	
	
	public String getTimeCode() {
		return timeCode;
	}



	public void setTimeCode(String timeCode) {
		this.timeCode = timeCode;
	}



	public Timestamp getTimelog() {
		return timelog;
	}



	public void setTimelog(Timestamp timelog) {
		this.timelog = timelog;
	}



	public int getNormalOrder() {
		return normalOrder;
	}



	public void setNormalOrder(int normalOrder) {
		this.normalOrder = normalOrder;
	}



	public int getWebOrder() {
		return webOrder;
	}



	public void setWebOrder(int webOrder) {
		this.webOrder = webOrder;
	}



	public int getMobileOrder() {
		return mobileOrder;
	}



	public void setMobileOrder(int mobileOrder) {
		this.mobileOrder = mobileOrder;
	}



	public int getOtherOrder() {
		return otherOrder;
	}



	public void setOtherOrder(int otherOrder) {
		this.otherOrder = otherOrder;
	}


	public int getGeneralValue(int input) {
		int output = 0;
		switch (input) {
		case 0:
			output = this.getNormalOrder();
			break;
		case 1:
			output = this.getMobileOrder();
			break;
		case 2:
			output = this.getWebOrder();
			break;
		case 3:
			output = this.getOtherOrder();
			break;
		default:
			output = 0;
			break;
		}
		return output;
	}

	public RiderVariationModel() {
		timeCode = "";
		timelog = null;
		normalOrder = 0;
		webOrder = 0;
		mobileOrder = 0;
		otherOrder = 0;
	}
}
