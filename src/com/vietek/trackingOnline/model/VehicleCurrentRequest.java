package com.vietek.trackingOnline.model;

import java.util.List;

public class VehicleCurrentRequest {
	List<Vehicle> list;
	int currentRequest;

	public VehicleCurrentRequest() {
		// TODO Auto-generated constructor stub
	}
	public List<Vehicle> getList() {
		return list;
	}

	public void setList(List<Vehicle> list) {
		this.list = list;
	}

	public int getCurrentRequest() {
		return currentRequest;
	}

	public void setCurrentRequest(int numberRequest) {
		this.currentRequest = numberRequest;
	}

}
