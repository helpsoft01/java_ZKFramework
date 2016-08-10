package com.vietek.taxioperation.model;

import com.vietek.taxioperation.common.Searchable;

public class ReportQcTimeWaitOfCustomer extends ReportModel {

	private int id;
	@Searchable
	private String numberMobile;
	@Searchable
	private String beginOrderTime;
	@Searchable
	private String beginOrderAddress;
	@Searchable
	private String minute;
	@Searchable
	private String repeatTime;
	@Searchable
	private String status;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumberMobile() {
		return this.numberMobile;
	}

	public void setNumberMobile(String numberMobile) {
		this.numberMobile = numberMobile;
	}

	// public Timestamp getBeginOrderTime() {
	// return this.beginOrderTime;
	// }
	// public void setBeginOrderTime(Timestamp beginOrderTime) {
	// this.beginOrderTime = beginOrderTime;
	// }
	public String getBeginOrderAddress() {
		return this.beginOrderAddress;
	}

	public String getBeginOrderTime() {
		return this.beginOrderTime;
	}

	public void setBeginOrderTime(String beginOrderTime) {
		this.beginOrderTime = beginOrderTime;
	}

	public void setBeginOrderAddress(String beginOrderAddress) {
		this.beginOrderAddress = beginOrderAddress;
	}

	public String getMinute() {
		return this.minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getRepeatTime() {
		return this.repeatTime;
	}

	public void setRepeatTime(String repeatTime) {
		this.repeatTime = repeatTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
