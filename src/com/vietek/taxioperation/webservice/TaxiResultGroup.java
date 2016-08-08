package com.vietek.taxioperation.webservice;

import java.io.Serializable;
import java.util.List;

public class TaxiResultGroup implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private List<TaxiResult> lstTaxiResults;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<TaxiResult> getLstTaxiResults() {
		return lstTaxiResults;
	}
	public void setLstTaxiResults(List<TaxiResult> lstTaxiResults) {
		this.lstTaxiResults = lstTaxiResults;
	}
	
}
