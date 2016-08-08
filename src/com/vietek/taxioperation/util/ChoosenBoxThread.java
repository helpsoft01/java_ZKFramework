package com.vietek.taxioperation.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zul.Messagebox;

import com.vietek.taxioperation.common.ConstantValueSearch;
import com.vietek.taxioperation.common.ValueToFillCbb;
import com.vietek.taxioperation.model.AbstractModel;

public class ChoosenBoxThread  extends Thread {
	List<?> dataList;
	public ChoosenBoxThread(String dataField, String value) {

	}
	
	public ChoosenBoxThread(Object model, String dataField, String strFind){
		Class<?> clazz = AbstractModel.getGenericType(model, dataField);
		String key = "";
		if (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					try {
						ArrayList<Annotation> ann = new ArrayList<Annotation>(
								Arrays.asList(field.getAnnotations()));
						if (ann != null) {
							for (int j = 0; j < ann.size(); j++) {
								if (ann.get(j).annotationType().equals(ValueToFillCbb.class)) {
									key = field.getName();
									break;
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		String className = clazz.getName().substring(
				clazz.getName().lastIndexOf(".") + 1);
		String controllerClassName = clazz.getName().replace(
				"com.vietek.taxioperation.model",
				"com.vietek.taxioperation.controller")
				+ "Controller";
		try {
			dataList = ControllerUtils.getController(
					Class.forName(controllerClassName)).find(ConstantValueSearch.NUMBER_RECORD_OF_CBB,
					"FROM " + className + " WHERE " + key + " LIKE '%" + strFind + "%'");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		Messagebox.show("Thread find data for choosen running..");
	}

	public List<?> getDataList() {
		return dataList;
	}

	public void setDataList(List<?> dataList) {
		this.dataList = dataList;
	}

}
