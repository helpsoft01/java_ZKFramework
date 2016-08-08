package com.vietek.taxioperation.util;

import java.math.BigDecimal;

public class BigdecimalUtils {
	
	public static boolean isString2Bigdecimal(String value) {
		try {
			new BigDecimal(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
