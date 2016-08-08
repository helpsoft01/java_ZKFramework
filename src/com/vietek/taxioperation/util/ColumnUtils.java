package com.vietek.taxioperation.util;
/**
 * 
 * @author Dungnm
 *
 */
public class ColumnUtils {
	public static String getVariableFromGetMethod(String methodName) {
		if(methodName != null && !("").equals(methodName) && methodName.length()>3) {
			if(("get").equals(methodName.substring(0,3))) {
				return methodName.substring(3).substring(0,1).toLowerCase() + methodName.substring(3).substring(1);
			}
		}
		return null;
	}
}
