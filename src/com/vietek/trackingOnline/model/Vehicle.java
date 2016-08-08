package com.vietek.trackingOnline.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Vehicle {

	int ID;
	String TimeLog;
	String VehicleNumber;
	String LicensePlate;
	float Longi;
	float Lati;
	int LastGPSSpeed;
	String SmallUrlIcon;
	int VehicleID;
	float Angle;
	int GroupID;

	public Vehicle(ResultSet rs) {
		Date date;
		try {
			setTimeLog(rs.getString("timeLog"));

			setID(rs.getInt("ID"));
			setVehicleNumber(rs.getString("vehicleNumber"));
			setLicensePlate(rs.getString("licensePlate"));
			setLongi(rs.getFloat("longi"));
			setLati(rs.getFloat("lati"));
			setLastGPSSpeed(rs.getInt("lastGPSSpeed"));
			setVehicleID(rs.getInt("vehicleID"));
			setSmallUrlIcon(rs.getString("smallUrlIcon"));
			setAngle(rs.getFloat("angle"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Vehicle() {
		// TODO Auto-generated constructor stub
		setID(-1);
		setVehicleID(-1);
		setVehicleNumber("");

	}

	public int getGroupID() {
		return GroupID;
	}

	public void setGroupID(int groupID) {
		GroupID = groupID;
	}

	public float getAngle() {
		return Angle;
	}

	public void setAngle(float angle) {
		Angle = angle;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTimeLog() {
		return TimeLog;
	}

	public void setTimeLog(String timeLog) {
		TimeLog = timeLog;
	}

	public String getVehicleNumber() {
		return VehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		VehicleNumber = vehicleNumber;
	}

	public String getLicensePlate() {
		return LicensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		LicensePlate = licensePlate;
	}

	public float getLongi() {
		return Longi;
	}

	public void setLongi(float longi) {
		Longi = longi;
	}

	public float getLati() {
		return Lati;
	}

	public void setLati(float lati) {
		Lati = lati;
	}

	public int getLastGPSSpeed() {
		return LastGPSSpeed;
	}

	public void setLastGPSSpeed(int lastGPSSpeed) {
		LastGPSSpeed = lastGPSSpeed;
	}

	public String getSmallUrlIcon() {
		return SmallUrlIcon;
	}

	public void setSmallUrlIcon(String smallUrlIcon) {
		SmallUrlIcon = smallUrlIcon;
	}

	public int getVehicleID() {
		return VehicleID;
	}

	public void setVehicleID(int vehicleID) {
		VehicleID = vehicleID;
	}

}
