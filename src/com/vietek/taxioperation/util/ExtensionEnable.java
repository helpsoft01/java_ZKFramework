package com.vietek.taxioperation.util;

import java.sql.Timestamp;

import com.vietek.taxioperation.model.Customer;

public class ExtensionEnable {

	private Customer customer = null;
	private Timestamp timeCallIn = new Timestamp(0);
	private String extension = "";
	private int index=0;

	public Timestamp getTimeCallIn() {
		return timeCallIn;
	}

	public void setTimeCallIn(Timestamp time) {
		this.timeCallIn = time;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public ExtensionEnable() {
		resetExtensionEnable();
	}

	public String getExtension() {
		return extension;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public void resetExtensionEnable() {
		customer = null;
		timeCallIn = new Timestamp(0);
	}
}
