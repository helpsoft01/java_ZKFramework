package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema="txm_tracking", name="gps_online")
public class GPSOnline extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private int id;
	
	@Column(name="TimeLog")
	private Timestamp timeLog;
	
	@Column(name="DeviceID")
	private Integer deviceID;
	
	@Column(name="VehicleID")
	private Integer vehicleID;
	
	@Column(name="LicensePlate")
	private String licensePlate;
	
	@Column(name="VehicleNumber")
	private String vehicleNumber;
	
	@Column(name="AgentID")
	private Integer agentID;
	
	@Column(name="GroupID")
	private Integer groupID;
	
	@Column(name="Longi")
	private Double lng;
	
	@Column(name="Lati")
	private Double lat;
	
	@Column(name="LastGPSSpeed")
	private Integer lastGPSSpeed;
	
	@Column(name="MeterSpeed")
	private Integer meterSpeed;
	
	@Column(name="InTrip")
	private Integer inTrip;
	
	@Column(name="PinPower")
	private Integer pinPower;
	
	@Column(name="AcquyPower")
	private Integer acquyPower;
	
	@Column(name="Engine")
	private Integer engine;
	
	@Column(name="Door")
	private Integer door;
	
	@Column(name="AirConditioner")
	private Integer airConditioner;
	
	@Column(name="GSM")
	private Integer GSM;
	
	@Column(name="GPS")
	private Integer GPS;
	
	@Column(name="SDCard")
	private Integer SDCard;
	
	@Column(name="PathContinuous")
	private Double pathContinuous;
	
	@Column(name="PathPerday")
	private Double pathPerday;
	
	@Column(name="TimeDrivingPerday")
	private Integer timeDrivingPerday;
	
	@Column(name="TimeDrivingContinuous")
	private Integer timeDrivingContinuous;

	@Column(name="DrivingTimeCounting")
	private Integer drivingTimeCounting;
	
	@Column(name="StayTimeCounting")
	private Integer stayTimeCounting;
	
	@Column(name="SpeedTimeCounting")
	private Integer speedTimeCounting;
	
	@Column(name="DoorTimeCounting")
	private Integer doorTimeCounting;
	
	@Column(name="LostSignalTimeCounting")
	private Integer lostSignalTimeCounting;
	
	@Column(name="LostGpsTimeCounting")
	private Integer lostGpsTimeCounting;
	
	@Column(name="DetalSpeed")
	private Integer detalSpeed;
	
	@Column(name="TotalMoneyShift")
	private Integer totalMoneyShift;
	
	@Column(name="TotalMoney")
	private Integer totalMoney;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(Timestamp timeLog) {
		this.timeLog = timeLog;
	}

	public Integer getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(Integer deviceID) {
		this.deviceID = deviceID;
	}

	public Integer getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Integer vehicleID) {
		this.vehicleID = vehicleID;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public Integer getAgentID() {
		return agentID;
	}

	public void setAgentID(Integer agentID) {
		this.agentID = agentID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Integer getLastGPSSpeed() {
		return lastGPSSpeed;
	}

	public void setLastGPSSpeed(Integer lastGPSSpeed) {
		this.lastGPSSpeed = lastGPSSpeed;
	}

	public Integer getMeterSpeed() {
		return meterSpeed;
	}

	public void setMeterSpeed(Integer meterSpeed) {
		this.meterSpeed = meterSpeed;
	}

	public Integer getInTrip() {
		return inTrip;
	}

	public void setInTrip(Integer inTrip) {
		this.inTrip = inTrip;
	}

	public Integer getPinPower() {
		return pinPower;
	}

	public void setPinPower(Integer pinPower) {
		this.pinPower = pinPower;
	}

	public Integer getAcquyPower() {
		return acquyPower;
	}

	public void setAcquyPower(Integer acquyPower) {
		this.acquyPower = acquyPower;
	}

	public Integer getEngine() {
		return engine;
	}

	public void setEngine(Integer engine) {
		this.engine = engine;
	}

	public Integer getDoor() {
		return door;
	}

	public void setDoor(Integer door) {
		this.door = door;
	}

	public Integer getAirConditioner() {
		return airConditioner;
	}

	public void setAirConditioner(Integer airConditioner) {
		this.airConditioner = airConditioner;
	}

	public Integer getGSM() {
		return GSM;
	}

	public void setGSM(Integer gSM) {
		GSM = gSM;
	}

	public Integer getGPS() {
		return GPS;
	}

	public void setGPS(Integer gPS) {
		GPS = gPS;
	}

	public Integer getSDCard() {
		return SDCard;
	}

	public void setSDCard(Integer sDCard) {
		SDCard = sDCard;
	}

	public Double getPathContinuous() {
		return pathContinuous;
	}

	public void setPathContinuous(Double pathContinuous) {
		this.pathContinuous = pathContinuous;
	}

	public Double getPathPerday() {
		return pathPerday;
	}

	public void setPathPerday(Double pathPerday) {
		this.pathPerday = pathPerday;
	}

	public Integer getTimeDrivingPerday() {
		return timeDrivingPerday;
	}

	public void setTimeDrivingPerday(Integer timeDrivingPerday) {
		this.timeDrivingPerday = timeDrivingPerday;
	}

	public Integer getTimeDrivingContinuous() {
		return timeDrivingContinuous;
	}

	public void setTimeDrivingContinuous(Integer timeDrivingContinuous) {
		this.timeDrivingContinuous = timeDrivingContinuous;
	}

	public Integer getDrivingTimeCounting() {
		return drivingTimeCounting;
	}

	public void setDrivingTimeCounting(Integer drivingTimeCounting) {
		this.drivingTimeCounting = drivingTimeCounting;
	}

	public Integer getStayTimeCounting() {
		return stayTimeCounting;
	}

	public void setStayTimeCounting(Integer stayTimeCounting) {
		this.stayTimeCounting = stayTimeCounting;
	}

	public Integer getSpeedTimeCounting() {
		return speedTimeCounting;
	}

	public void setSpeedTimeCounting(Integer speedTimeCounting) {
		this.speedTimeCounting = speedTimeCounting;
	}

	public Integer getDoorTimeCounting() {
		return doorTimeCounting;
	}

	public void setDoorTimeCounting(Integer doorTimeCounting) {
		this.doorTimeCounting = doorTimeCounting;
	}

	public Integer getLostSignalTimeCounting() {
		return lostSignalTimeCounting;
	}

	public void setLostSignalTimeCounting(Integer lostSignalTimeCounting) {
		this.lostSignalTimeCounting = lostSignalTimeCounting;
	}

	public Integer getLostGpsTimeCounting() {
		return lostGpsTimeCounting;
	}

	public void setLostGpsTimeCounting(Integer lostGpsTimeCounting) {
		this.lostGpsTimeCounting = lostGpsTimeCounting;
	}

	public Integer getDetalSpeed() {
		return detalSpeed;
	}

	public void setDetalSpeed(Integer detalSpeed) {
		this.detalSpeed = detalSpeed;
	}

	public Integer getTotalMoneyShift() {
		return totalMoneyShift;
	}

	public void setTotalMoneyShift(Integer totalMoneyShift) {
		this.totalMoneyShift = totalMoneyShift;
	}

	public Integer getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	
	
	

}
