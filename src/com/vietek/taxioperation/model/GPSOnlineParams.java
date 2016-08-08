package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema="txm_tracking", name="gps_online_params")
public class GPSOnlineParams extends AbstractModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DeviceID")
	private int id;
	
	@Column(name="DriverID")
	private Integer driverID;
	
	@Column(name="DriverName")
	private String driverName;
	
	@Column(name="RegisterNumber")
	private String registerNumber;
	
	@Column(name="MobileNumber")
	private String mobileNumber;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getDriverID() {
		return driverID;
	}

	public void setDriverID(Integer driverID) {
		this.driverID = driverID;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getRegisterNumber() {
		return registerNumber;
	}

	public void setRegisterNumber(String registerNumber) {
		this.registerNumber = registerNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getRFIDCard() {
		return RFIDCard;
	}

	public void setRFIDCard(Integer rFIDCard) {
		RFIDCard = rFIDCard;
	}

	public Integer getLastTripId() {
		return lastTripId;
	}

	public void setLastTripId(Integer lastTripId) {
		this.lastTripId = lastTripId;
	}

	public Timestamp getLastTripTime() {
		return lastTripTime;
	}

	public void setLastTripTime(Timestamp lastTripTime) {
		this.lastTripTime = lastTripTime;
	}

	public Timestamp getLastTripFinish() {
		return lastTripFinish;
	}

	public void setLastTripFinish(Timestamp lastTripFinish) {
		this.lastTripFinish = lastTripFinish;
	}

	public Integer getLastTripID() {
		return lastTripID;
	}

	public void setLastTripID(Integer lastTripID) {
		this.lastTripID = lastTripID;
	}

	public Timestamp getLastWorkingDay() {
		return lastWorkingDay;
	}

	public void setLastWorkingDay(Timestamp lastWorkingDay) {
		this.lastWorkingDay = lastWorkingDay;
	}

	public Double getLatiWorking() {
		return latiWorking;
	}

	public void setLatiWorking(Double latiWorking) {
		this.latiWorking = latiWorking;
	}

	public Double getLongiWorking() {
		return longiWorking;
	}

	public void setLongiWorking(Double longiWorking) {
		this.longiWorking = longiWorking;
	}

	public Integer getIsWorking() {
		return isWorking;
	}

	public void setIsWorking(Integer isWorking) {
		this.isWorking = isWorking;
	}

	public Integer getInWorkshop() {
		return inWorkshop;
	}

	public void setInWorkshop(Integer inWorkshop) {
		this.inWorkshop = inWorkshop;
	}

	public Integer getInAccident() {
		return inAccident;
	}

	public void setInAccident(Integer inAccident) {
		this.inAccident = inAccident;
	}

	public Integer getInParking() {
		return inParking;
	}

	public void setInParking(Integer inParking) {
		this.inParking = inParking;
	}

	public Integer getAllPath() {
		return allPath;
	}

	public void setAllPath(Integer allPath) {
		this.allPath = allPath;
	}

	public Integer getAllTripPath() {
		return allTripPath;
	}

	public void setAllTripPath(Integer allTripPath) {
		this.allTripPath = allTripPath;
	}

	@Column(name="PhoneNumber")
	private String phoneNumber;
	
	@Column(name="RFIDCard")
	private Integer RFIDCard;
	
	@Column(name="LastTripId")
	private Integer lastTripId;
	
	@Column(name="LastTripTime")
	private Timestamp lastTripTime;
	
	@Column(name="LastTripFinish")
	private Timestamp lastTripFinish;
	
	@Column(name="LastTripCounter")
	private Integer lastTripID;
	
	@Column(name="LastWorkingDay")
	private Timestamp lastWorkingDay;
	
	@Column(name="LatiWorking")
	private Double  latiWorking;
	
	@Column(name="LongiWorking")
	private Double longiWorking;
	
	@Column(name="IsWorking")
	private Integer isWorking;
	
	@Column(name="InWorkshop")
	private Integer inWorkshop;
	
	@Column(name="InAccident")
	private Integer inAccident;
	
	@Column(name="InParking")
	private Integer inParking;
	
	@Column(name="AllPath")
	private Integer allPath;
	
	@Column(name="AllTripPath")
	private Integer allTripPath;
	
}
