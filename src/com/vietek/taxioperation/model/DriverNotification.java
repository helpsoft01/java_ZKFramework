package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.vietek.taxioperation.common.Searchable;

@Entity
@Table(name = "driver_notification")
public class DriverNotification  extends AbstractModel implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	//habv_add propertype
	public int getId() {
		return id;
	}
	//end add
	@Searchable(placehoder="Nội dung thông báo")
	private String message;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "driver_notification_customer", joinColumns = {
			@JoinColumn(name = "driver_notification_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "driver_id", nullable = false, updatable = false) })
	private Set<Driver> drivers = new HashSet<Driver>();
	private Boolean isActive = true;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Set<Driver> getDrivers() {
		return drivers;
	}
	public void setDrivers(HashSet<Driver> drivers) {
		this.drivers = drivers;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
