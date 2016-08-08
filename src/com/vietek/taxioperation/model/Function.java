package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//@Entity
//@Table(name="function")
public class Function extends AbstractModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7685162295045657599L;
	
	@Id
	@GeneratedValue
	private int id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String clazz; // zul path, or java class
	private boolean isActive = true;
	
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
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
