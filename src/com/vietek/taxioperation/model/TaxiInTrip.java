package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "taxi_in_trip")
public class TaxiInTrip extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8251189960416044880L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	@Column(name = "taxi_id")
	private String taxiId;
	
	@Column(name = "trip_id")
	private String tripId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaxiId() {
		return taxiId;
	}

	public void setTaxiId(String taxiId) {
		this.taxiId = taxiId;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
}
