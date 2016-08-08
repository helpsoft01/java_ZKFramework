package com.vietek.taxioperation.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vietek.taxioperation.controller.BasicController;
import com.vietek.taxioperation.util.ControllerUtils;

public class AbstractModel {

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().toString().equals(this.getClass().toString())) {
			return AbstractModel.getValue(this, "id").equals(AbstractModel.getValue(obj, "id"));
		} else {
			return false;
		}
	}

	public BasicController<?> getControler() {
		String controllerClassName = this.getClass().getName().replace("com.vietek.taxioperation.model",
				"com.vietek.taxioperation.controller") + "Controller";
		BasicController<?> controller = null;
		try {
			controller = ControllerUtils.getController(Class.forName(controllerClassName));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return controller;
	}

	public void save() {
		getControler().saveOrUpdate(this);
	}

	public void delete() {
		getControler().delete(this);
	}

	public void refresh() {
		getControler().refresh(this);
	}

	public Integer saveOrUpdate(AbstractModel model) {
		return model.saveOrUpdate(model);
	}

	public void merge(AbstractModel model) {
		getControler().merge(model);
	}

	public static void bulkUpdate(List<?> lst, Class<?> model){
		String controllerClassName = model.getName().replace("com.vietek.taxioperation.model",
				"com.vietek.taxioperation.controller") + "Controller";
		BasicController<?> controller = null;
		try {
			controller = ControllerUtils.getController(Class.forName(controllerClassName));
			controller.bulkUpdate(lst);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void bulkInsert(List<?> lst, Class<?> model){
		String controllerClassName = model.getName().replace("com.vietek.taxioperation.model",
				"com.vietek.taxioperation.controller") + "Controller";
		BasicController<?> controller = null;
		try {
			controller = ControllerUtils.getController(Class.forName(controllerClassName));
			controller.bulkInsert(lst);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void updateCustomer() {
		getControler().updateStatusCustomer((Customer) this);
	}

	public static Object getValue(Object obj, String fieldName) {
		Object retVal = null;
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			String methodName;
			if (field.getType().equals(boolean.class)) {
				methodName = "is" + fieldName;
			} else {
				methodName = "get" + fieldName;
			}
			Method method = obj.getClass().getDeclaredMethod(methodName);
			retVal = method.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retVal;
	}

	public static void setValue(Object obj, String fieldName, Object value) {
		try {

			fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			String methodName = "set" + fieldName;
			Method method = null;
			if (value != null) {
				method = obj.getClass().getDeclaredMethod(methodName, value.getClass());
			} else {
				Method[] arrMethod = obj.getClass().getDeclaredMethods();
				for (int i = 0; i < arrMethod.length; i++) {
					Method methodTmp = arrMethod[i];
					if (methodTmp.getName().equals(methodName)) {
						method = methodTmp;
						break;
					}
				}
			}

			method.invoke(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Class<?> getDataType(Object obj, String fieldName) {
		Class<?> retVal = null;
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			retVal = field.getType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public static Class<?> getGenericType(Object obj, String fieldName) {
		Class<?> retVal = null;
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			Type type = field.getGenericType();
			if (type instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) type;
				for (Type t : pt.getActualTypeArguments()) {
					retVal = (Class<?>) t;
					break;
				}
			} else
				// sonvh
				retVal = (Class<?>) type;
			// ---end sonvh
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public static List<Field> getAllFields(Class<?> clazz) {
	    List<Field> fields = new ArrayList<Field>();

	    fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

	    Class<?> superClazz = clazz.getSuperclass();
	    if(superClazz != null){
	        fields.addAll(getAllFields(superClazz));
	    }
	    
	    return fields;
	}
	
	public static List<Field> getInheritedPrivateFields(Class<?> type) {
	    List<Field> result = new ArrayList<Field>();

	    Class<?> i = type;
	    while (i != null && i != Object.class) {
	        for (Field field : i.getDeclaredFields()) {
	            if (!field.isSynthetic()) {
	                result.add(field);
	            }
	        }
	        i = i.getSuperclass();
	    }

	    return result;
	}
	
	public int getId(){
		return 0;
	}
}
