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
@Entity
@Table(name = "lst_driver_app_follow_trip_status")
public class DriverAppsFollowTripStatus extends AbstractModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "Id")
	private int id;
	@ManyToOne
	@JoinColumn(name = "order_id")
	private TaxiOrder order;
	@Column(name = "status")
	private Integer status;
	@Column(name = "lst_driver_id")
	private String lst_driver_id;
	@Column(name = "time")
	private Timestamp time = new Timestamp(System.currentTimeMillis());
	
	public DriverAppsFollowTripStatus() {
	}
	
	public DriverAppsFollowTripStatus(TaxiOrder order, int status, String lst_driver_id) {
		this.order = order;
		this.lst_driver_id = lst_driver_id;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public TaxiOrder getOrder() {
		return order;
	}
	public void setOrder(TaxiOrder order) {
		this.order = order;
	}
	public String getLst_driver_id() {
		return lst_driver_id;
	}
	public void setLst_driver_id(String lst_driver_id) {
		this.lst_driver_id = lst_driver_id;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
