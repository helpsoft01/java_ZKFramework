package com.vietek.taxioperation.model;

public class JsonPlaceAutoGroup {
	private JsonPlaceAuto[] predictions;
	private String status;

	public JsonPlaceAuto[] getPredictions() {
		return predictions;
	}

	public void setPredictions(JsonPlaceAuto[] predictions) {
		this.predictions = predictions;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
