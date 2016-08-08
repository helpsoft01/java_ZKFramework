package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import com.vietek.taxioperation.common.ApplyEditor;
import com.vietek.taxioperation.common.Searchable;

public class RptTripSearchingOnline extends ReportModel {
	private int tripId = 0;
	private int shiftId = 0;
	private int vehicleId;
	@Searchable
	private String groupName;
	@Searchable
	private String vehicleNumber;
	@Searchable
	private String licensePlate;
	@Searchable
	private String vehicleType;
	@Searchable
	private Timestamp timeStart;
	@Searchable
	private Timestamp timeFinish;
	@Searchable
	private String placeStart;
	@Searchable
	private String placeFinish;
	@Searchable
	private int clock;
	@ApplyEditor(classNameEditor = "Textbox1")
	@Searchable
	private int reduce;
	@ApplyEditor(classNameEditor = "Textbox2")
	@Searchable
	private int priceTrip;
	@Searchable
	private int reason;
	@Searchable
	private float km;
	@Searchable
	private float empKm;
	@Searchable
	private String driver;
	@Searchable
	private String staffCard = "";
	@Searchable
	private String tripDetail;
	private Boolean isHistory;
	
	
	
	public int getTripId() {
		return tripId;
	}
	public void setTripId(int tripId) {
		this.tripId = tripId;
	}
	public int getShiftId() {
		return shiftId;
	}
	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public Timestamp getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(Timestamp timeStart) {
		this.timeStart = timeStart;
	}
	public Timestamp getTimeFinish() {
		return timeFinish;
	}
	public void setTimeFinish(Timestamp timeFinish) {
		this.timeFinish = timeFinish;
	}
	public String getPlaceStart() {
		return placeStart;
	}
	public void setPlaceStart(String placeStart) {
		this.placeStart = placeStart;
	}
	public String getPlaceFinish() {
		return placeFinish;
	}
	public void setPlaceFinish(String placeFinish) {
		this.placeFinish = placeFinish;
	}
	public int getClock() {
		return clock;
	}
	public void setClock(int clock) {
		this.clock = clock;
	}
	public int getReduce() {
		return reduce;
	}
	public void setReduce(int reduce) {
		this.reduce = reduce;
	}
	public int getPriceTrip() {
		return priceTrip;
	}
	public void setPriceTrip(int priceTrip) {
		this.priceTrip = priceTrip;
	}
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	public float getKm() {
		return km;
	}
	public void setKm(float km) {
		this.km = km;
	}
	
	public float getEmpKm() {
		return empKm;
	}
	public void setEmpKm(float empKm) {
		this.empKm = empKm;
	}
	public String getDriver() {
		return driver;
	}
	
	public String getStaffCard() {
		return staffCard;
	}
	public void setStaffCard(String staffCard) {
		this.staffCard = staffCard;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getTripDetail() {
		return tripDetail;
	}
	public void setTripDetail(String tripDetail) {
		this.tripDetail = tripDetail;
	}
	public Boolean getIsHistory() {
		return isHistory;
	}
	public void setIsHistory(Boolean isHistory) {
		this.isHistory = isHistory;
	}
	
	
	
}
