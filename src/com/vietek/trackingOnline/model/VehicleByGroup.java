package com.vietek.trackingOnline.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleByGroup {
	int VehicleID;
	String VehicleNumber;
	int VehicleGroupID;

	public int getVehicleID() {
		return VehicleID;
	}

	public void setVehicleID(int vehicleID) {
		VehicleID = vehicleID;
	}

	public String getVehicleNumber() {
		return VehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		VehicleNumber = vehicleNumber;
	}

	public int getVehicleGroupID() {
		return VehicleGroupID;
	}

	public void setVehicleGroupID(int vehicleGroupID) {
		VehicleGroupID = vehicleGroupID;
	}

	public VehicleByGroup(ResultSet rs) {
		try {
			setVehicleGroupID(rs.getInt("vehicleGroupID"));
			setVehicleNumber(rs.getString("vehicleNumber"));
			setVehicleID(rs.getInt("vehicleID"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
