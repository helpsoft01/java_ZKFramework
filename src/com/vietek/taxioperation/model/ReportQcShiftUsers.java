package com.vietek.taxioperation.model;

import java.sql.Timestamp;
import java.util.Date;

import com.vietek.taxioperation.common.Searchable;

public class ReportQcShiftUsers extends ReportModel {
	private int userid;
	@Searchable
	private Timestamp ordertime;
	@Searchable
	private String username;
	@Searchable
	private String shiftname;
	@Searchable
	private String callnumber;
	@Searchable
	private Date logintime;
	@Searchable
	private Date logouttime;
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
	public Timestamp getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Timestamp ordertime) {
		this.ordertime = ordertime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getShiftname() {
		return shiftname;
	}

	public void setShiftname(String shiftname) {
		this.shiftname = shiftname;
	}

	public String getCallnumber() {
		return callnumber;
	}

	public void setCallnumber(String callnumber) {
		this.callnumber = callnumber;
	}

	public Date getLogintime() {
		return logintime;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}

	public Date getLogouttime() {
		return logouttime;
	}

	public void setLogouttime(Date logouttime) {
		this.logouttime = logouttime;
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
