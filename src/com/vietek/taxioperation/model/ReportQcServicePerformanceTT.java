package com.vietek.taxioperation.model;

import com.vietek.taxioperation.common.Searchable;

public class ReportQcServicePerformanceTT extends ReportModel {
	@Searchable
	private String shiftname;
	@Searchable
	private int customer;
	@Searchable
	private int requestnumber;
	@Searchable
	private int repeattime;
	@Searchable
	private int totalcall;
	@Searchable
	private int acceptcall;
	@Searchable
	private int declinecall;
	@Searchable
	private float percent;
	
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
