package com.vietek.taxioperation.common;

public enum EnumActionCallIn  {
	INCOMMING(0), OUTCOMMONG(1), UNDEFINED(-1);

	private final int value;

	EnumActionCallIn (final int newValue) {
		value = newValue;
	}

	public int getValue() {
		return value;
	}

}