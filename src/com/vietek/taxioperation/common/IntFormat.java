package com.vietek.taxioperation.common;

import java.text.DecimalFormat;

public class IntFormat {
	public static String formatTypeInt(String pattern, int value){
		DecimalFormat format = new DecimalFormat(pattern);
		String output = format.format(value);
		return output;
	}

}
