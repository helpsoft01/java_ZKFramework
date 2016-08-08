package com.vietek.taxioperation.ui.util;

import java.io.Serializable;

import org.zkoss.zk.ui.Component;

public class GridRow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String label;
//	private Class<?> clazz;
	private Component component;
	
	public GridRow(String label, Component comp){
		super();
		this.label = label;
//		this.clazz = clazz;
		this.component = comp;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
//	public Class<?> getClazz() {
//		return clazz;
//	}
//	public void setClazz(Class<?> clazz) {
//		this.clazz = clazz;
//	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}
	
	

}
