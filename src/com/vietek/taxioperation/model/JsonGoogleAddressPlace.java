package com.vietek.taxioperation.model;

public class JsonGoogleAddressPlace {
	private ItemAddressPlace[] results;
	private String status;
	
	
	public ItemAddressPlace[] getResults() {
		return results;
	}
	public void setResults(ItemAddressPlace[] results) {
		this.results = results;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
