package com.vietek.taxioperation.ui.util;

import java.io.Serializable;
import java.lang.reflect.Field;

public class GridColumn implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8070935758621343190L;
	private String header;
	private int width;
	private String widthHflex;
	private Class<?> clazz;
	private String getDataMethod;
	private String fieldName;
	private Class<?> modelClazz;
	private Field field;

	public GridColumn(String header, int width, Class<?> clazz, String getDataMethod) {
		super();
		this.header = header;
		this.width = width;
		this.clazz = clazz;
		this.setGetDataMethod(getDataMethod);
		this.field = null;
	}

	public GridColumn(String header, String widthHflex, Class<?> clazz, String getDataMethod, String fieldName,
			Class<?> modelClazz) {
		super();
		this.header = header;
		this.widthHflex = widthHflex;
		this.clazz = clazz;
		this.fieldName = fieldName;
		this.modelClazz = modelClazz;
		this.setGetDataMethod(getDataMethod);
		this.field = null;
	}

	public GridColumn(String header, int width, Class<?> clazz, String getDataMethod, String fieldName,
			Class<?> modelClazz) {
		super();
		this.header = header;
		this.width = width;
		this.clazz = clazz;
		this.fieldName = fieldName;
		this.modelClazz = modelClazz;
		this.setGetDataMethod(getDataMethod);
		this.field = null;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getGetDataMethod() {
		return getDataMethod;
	}

	public void setGetDataMethod(String getDataMethod) {
		this.getDataMethod = getDataMethod;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class<?> getModelClazz() {
		return modelClazz;
	}

	public void setModelClazz(Class<?> modelClazz) {
		this.modelClazz = modelClazz;
	}

	public String getWidthHflex() {
		return widthHflex;
	}

	public void setWidthHflex(String widthHflex) {
		this.widthHflex = widthHflex;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

}
