package com.vietek.taxioperation.webservice;

import java.util.List;

import com.vietek.taxioperation.webservice.MobileWS.NotificationFromDriver;

public class GroupInfoDriver {
	private String status; // Use to check info of Object 
	private List<NotificationFromDriver> lstNotification;
	private List<TaxiOnline> lstTaxi;
	private List<CustomerOrder> lstCustomer;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<NotificationFromDriver> getLstNotification() {
		return lstNotification;
	}
	public void setLstNotification(List<NotificationFromDriver> lstNotification) {
		this.lstNotification = lstNotification;
	}
	public List<TaxiOnline> getLstTaxi() {
		return lstTaxi;
	}
	public void setLstTaxi(List<TaxiOnline> lstTaxi) {
		this.lstTaxi = lstTaxi;
	}
	public List<CustomerOrder> getLstCustomer() {
		return lstCustomer;
	}
	public void setLstCustomer(List<CustomerOrder> lstCustomer) {
		this.lstCustomer = lstCustomer;
	}
	
	
}
