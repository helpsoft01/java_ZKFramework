package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//@Entity
//@Table(name="menu")
public class Menu extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8289073093536166681L;
	
	@Id
	@GeneratedValue
	private int id;
	@Column(nullable=false)
	private String name; //store the key, will be translated in localization file
	
	@ManyToOne
	@JoinColumn(name="function_id")
	private Function function;
	private boolean isActive;
	private int type;//Loai Menu: 1: Setting - 2: Nav bar
	
	static public int TYPE_SETTING = 1;
	static public int TYPE_NAVBAR = 2;
	
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
	public Function getFunction() {
		return function;
	}
	public void setFunction(Function function) {
		this.function = function;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
