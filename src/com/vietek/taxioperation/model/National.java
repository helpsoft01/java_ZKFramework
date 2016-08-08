package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lst_national", schema = "txm_tracking")
public class National extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "NationalID")
	@GeneratedValue
	private int id;
	private String NationalCode;
	private String NationalName;
	@Column(name = "Active")
	private Boolean isActive = true;

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNationalCode() {
		return NationalCode;
	}

	public void setNationalCode(String nationalCode) {
		NationalCode = nationalCode;
	}

	public String getNationalName() {
		return NationalName;
	}

	public void setNationalName(String nationalName) {
		NationalName = nationalName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "" + this.NationalName;
	}

}
