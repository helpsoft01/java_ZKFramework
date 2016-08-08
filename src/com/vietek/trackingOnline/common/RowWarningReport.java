package com.vietek.trackingOnline.common;

import java.sql.Timestamp;

import com.vietek.taxioperation.util.MapUtils;

public class RowWarningReport {
	private int id;
	private String vehiclenumber;
	private String LicensePlate;
	private String vehicleGroup;
	private Timestamp begintime;
	private Timestamp endtime;
	private String BeginAddress;
	private Double beginlati;
	private Double BeginLongi;
	private String endAddress;
	private Double endLati;
	private Double endLongi;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getBegintime() {
		return begintime;
	}

	public String getVehiclenumber() {
		return vehiclenumber;
	}

	public void setVehiclenumber(String vehiclenumber) {
		this.vehiclenumber = vehiclenumber;
	}

	public String getLicensePlate() {
		return LicensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		LicensePlate = licensePlate;
	}

	public String getVehicleGroup() {
		return vehicleGroup;
	}

	public void setVehicleGroup(String vehicleGroup) {
		this.vehicleGroup = vehicleGroup;
	}

	public void setBegintime(Timestamp begintime) {
		this.begintime = begintime;
	}

	public Timestamp getEndtime() {
		return endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}

	public String getBeginAddress() {
		if (BeginAddress == null) {
			BeginAddress = MapUtils.getAddressFromIMap(BeginLongi, beginlati);
		}
		return BeginAddress;
	}

	public void setBeginAddress(String beginAddress) {
		BeginAddress = beginAddress;
	}

	public Double getBeginlati() {
		return beginlati;
	}

	public void setBeginlati(Double beginlati) {
		this.beginlati = beginlati;
	}

	public Double getBeginLongi() {
		return BeginLongi;
	}

	public void setBeginLongi(Double beginLongi) {
		BeginLongi = beginLongi;
	}

	public String getEndAddress() {
		if (endAddress == null) {
			endAddress = MapUtils.getAddressFromIMap(endLongi, endLati);
		}
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

	public Double getEndLati() {
		return endLati;
	}

	public void setEndLati(Double endLati) {
		this.endLati = endLati;
	}

	public Double getEndLongi() {
		return endLongi;
	}

	public void setEndLongi(Double endLongi) {
		this.endLongi = endLongi;
	}

}
