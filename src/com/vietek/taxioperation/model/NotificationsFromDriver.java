package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "notifications_from_driver")
public class NotificationsFromDriver extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "driver_id", nullable = false)
	@Validation(nullable = false)
	private Driver driver;
	// 1: trafficjam; 2: raining
	private Integer typeNotification;
	private Double latitude;
	private Double longtitude;
	private Timestamp timeSend;

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

	public Integer getTypeNotification() {
		return typeNotification;
	}

	public void setTypeNotification(Integer typeNotification) {
		this.typeNotification = typeNotification;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

	public Timestamp getTimeSend() {
		return timeSend;
	}

	public void setTimeSend(Timestamp timeSend) {
		this.timeSend = timeSend;
	}

}
