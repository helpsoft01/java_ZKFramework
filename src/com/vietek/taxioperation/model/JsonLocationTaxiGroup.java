package com.vietek.taxioperation.model;


public class JsonLocationTaxiGroup {
	private int error;
	private String message;
	private JsonLocationTaxi[] result;
	private Integer requestid;
	private String defaultloc;


	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public JsonLocationTaxi[] getResult() {
		return result;
	}

	public void setResult(JsonLocationTaxi[] result) {
		this.result = result;
	}

	public Integer getRequestid() {
		return requestid;
	}

	public void setRequestid(Integer requestid) {
		this.requestid = requestid;
	}


	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getDefaultloc() {
		return defaultloc;
	}

	public void setDefaultloc(String defaultloc) {
		this.defaultloc = defaultloc;
	}

}
