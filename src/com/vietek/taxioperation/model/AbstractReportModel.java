package com.vietek.taxioperation.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AbstractReportModel {
	
	public static Object getValue(Object obj, String fieldName) {
		Object retVal = null;
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			fieldName = fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1);
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

}
