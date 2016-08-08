package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.ui.util.FixedTimestamp;

@Entity
@Table(name = "driver_app_tracking")
public class DriverAppTracking extends AbstractModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "Id")
	private int id;
	@ManyToOne
	@JoinColumn(name = "DriverID")
	private Driver driver;
	@Column(name = "latitude")
	private double latitude;
	@Column(name = "longitude")
	private double longitude;
	@Column(name = "angle")
	private double angle;
	@ManyToOne
	@JoinColumn(name = "order_id")
	private TaxiOrder order;
	@Column(name = "status")
	@FixedCombobox(label={"Không khách", "Trong cuốc", "Trong cuốc", "Trong cuốc", "Trong cuốc", "Trong cuốc"}, value={0, 1, 2, 3, 4, 5})
	private Integer status;
	@Column(name = "time")
	@FixedTimestamp(format="dd/MM/yyyy HH:mm:ss")
	private Timestamp time = new Timestamp(System.currentTimeMillis());
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public TaxiOrder getOrder() {
		return order;
	}
	public void setOrder(TaxiOrder order) {
		this.order = order;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp timestamp) {
		this.time = timestamp;
	}
	
}
