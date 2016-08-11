package com.vietek.taxioperation.model;

import com.vietek.taxioperation.common.Searchable;

public class ReportQcShiftUsersTT extends ReportModel {
	private int userid;
	@Searchable
	private String username;
	@Searchable
	private int totalcall;
	@Searchable
	private int acceptcall;
	@Searchable
	private int declinecall;
	@Searchable
	private float percent;
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
