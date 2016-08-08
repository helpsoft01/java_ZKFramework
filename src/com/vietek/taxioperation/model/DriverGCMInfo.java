package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.Validation;
@Entity
@Table(name = "driver_gcm_info")

public class DriverGCMInfo extends AbstractModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "driver_id", nullable = false)
	@Validation(nullable = false)
	private Driver driver;
	private String regId;
	public int getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}

	
	
}
