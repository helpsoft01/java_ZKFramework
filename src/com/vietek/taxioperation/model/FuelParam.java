package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "lst_fuelparam")
public class FuelParam extends AbstractModel implements Serializable {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@JoinColumn(name = "VehicleID", referencedColumnName = "VehicleID", foreignKey = @ForeignKey(name = "FK_lst_fuelparam_lst_vehicle_VehicleID"))
	private Vehicle vehicle;
	private Double FuelMax;
	private Double FuelForm;
	private Double FuelTo;
	private Integer PulseFrom;
	private Integer PulseTo;
	private Double FuelMean;
	private Timestamp TimeLog;
	private Integer UserAutoID;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public Double getFuelMax() {
		return FuelMax;
	}
	public void setFuelMax(Double fuelMax) {
		FuelMax = fuelMax;
	}
	public Double getFuelForm() {
		return FuelForm;
	}
	public void setFuelForm(Double fuelForm) {
		FuelForm = fuelForm;
	}
	public Double getFuelTo() {
		return FuelTo;
	}
	public void setFuelTo(Double fuelTo) {
		FuelTo = fuelTo;
	}
	public Integer getPulseFrom() {
		return PulseFrom;
	}
	public void setPulseFrom(Integer pulseFrom) {
		PulseFrom = pulseFrom;
	}
	public Integer getPulseTo() {
		return PulseTo;
	}
	public void setPulseTo(Integer pulseTo) {
		PulseTo = pulseTo;
	}
	public Double getFuelMean() {
		return FuelMean;
	}
	public void setFuelMean(Double fuelMean) {
		FuelMean = fuelMean;
	}
	public Timestamp getTimeLog() {
		return TimeLog;
	}
	public void setTimeLog(Timestamp timeLog) {
		TimeLog = timeLog;
	}
	public Integer getUserAutoID() {
		return UserAutoID;
	}
	public void setUserAutoID(Integer userAutoID) {
		UserAutoID = userAutoID;
	}
	
}
