package com.vietek.taxioperation.model;

import java.io.Serializable;

/**
 * Bao lai class Vehicle. Phuc vu cho form dieu dam
 * 
 * @author VuD
 */
public class VehicleDD implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4971627761060618703L;
	/**
	 * 
	 */
	private int vehicleId;
	private Vehicle vehicle;
	private int seat;
	private String vehicleType;
	private String fullName;

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		if (vehicle != null) {
			return vehicle.toString();
		} else {
			return super.toString();
		}
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof VehicleDD) {
			if (((VehicleDD) obj).getVehicle() != null && vehicle != null) {
				if (((VehicleDD) obj).getVehicle().getId() == vehicle.getId()) {
					result = true;
				}
			}
		}
		return result;
	}
}