package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vietek.taxioperation.ui.util.FixedTimestamp;
@Entity
@Table(name = "driver_cancel_trip")
public class DriverCancelTrip extends AbstractModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "Id")
	private int id;
	private int driverId;
	private int orderId;
	private int reason;
	@Column(name = "time_cancel")
	@FixedTimestamp(format="dd/MM/yyyy HH:mm:ss")
	private Timestamp timeCancel = new Timestamp(System.currentTimeMillis());
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getDriverId() {
		return driverId;
	}
	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	public Timestamp getTimeCancel() {
		return timeCancel;
	}
	public void setTimeCancel(Timestamp timeCancel) {
		this.timeCancel = timeCancel;
	}
	
}
