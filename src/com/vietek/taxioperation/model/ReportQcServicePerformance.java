package com.vietek.taxioperation.model;

import java.util.Date;

public class ReportQcServicePerformance {
	private Date shiftday;
	private String shiftname;
	private int customer;
	private int requestnumber;
	private int repeattime;
	private int totalcall;
	private int acceptcall;
	private int declinecall;
	private float percent;

	public Date getShiftday() {
		return shiftday;
	}

	public void setShiftday(Date shiftday) {
		this.shiftday = shiftday;
	}

	public String getShiftname() {
		return shiftname;
	}

	public void setShiftname(String shiftname) {
		this.shiftname = shiftname;
	}

	public int getCustomer() {
		return customer;
	}

	public void setCustomer(int customer) {
		this.customer = customer;
	}

	public int getRequestnumber() {
		return requestnumber;
	}

	public void setRequestnumber(int requestnumber) {
		this.requestnumber = requestnumber;
	}

	public int getRepeattime() {
		return repeattime;
	}

	public void setRepeattime(int repeattime) {
		this.repeattime = repeattime;
	}

	public int getTotalcall() {
		return totalcall;
	}

	public void setTotalcall(int totalcall) {
		this.totalcall = totalcall;
	}

	public int getAcceptcall() {
		return acceptcall;
	}

	public void setAcceptcall(int acceptcall) {
		this.acceptcall = acceptcall;
	}

	public int getDeclinecall() {
		return declinecall;
	}

	public void setDeclinecall(int declinecall) {
		this.declinecall = declinecall;
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

}
