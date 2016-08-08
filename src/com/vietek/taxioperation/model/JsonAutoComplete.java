package com.vietek.taxioperation.model;

public class JsonAutoComplete {
	private MyPrediction[] predictions;
	private String status;
	public MyPrediction[] getPredictions() {
		return predictions;
	}
	public void setPredictions(MyPrediction[] predictions) {
		this.predictions = predictions;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
