package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "lst_common", schema = "txm_tracking")
@Where(clause = "CodeType = 'VEHICLEFIRM'")
public class CarSupplier extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1950591208257217359L;

	@Id
	@GeneratedValue
	@Column(name = "CommonID")
	private int id;
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "Name")
	private String name;
	@Column(name = "CodeType")
	private String type;
	@Column(name = "Active")
	private Boolean isActive = true;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return name;
	}

}