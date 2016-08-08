package com.vietek.taxioperation.common;

public enum EnumCarType {

	_4SIT(0), _7SIT(1);

	private final int value;

	EnumCarType(final int newValue) {
		value = newValue;
	}

	public int getValue() {
		return value;
	}
}