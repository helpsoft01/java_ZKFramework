package com.vietek.taxioperation.model;

public class Customer_TaxiOrder {

	Customer customer;
	TaxiOrder taxiOrder;

	public Customer_TaxiOrder() {
		customer = null;
		taxiOrder = null;
	}

	public Customer_TaxiOrder(Customer customer, TaxiOrder taxiOrder) {
		this.customer = customer;
		this.taxiOrder = taxiOrder;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public TaxiOrder getTaxiOrder() {
		return taxiOrder;
	}

	public void setTaxiOrder(TaxiOrder taxiOrder) {
		this.taxiOrder = taxiOrder;
	}

}
