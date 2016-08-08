package com.vietek.taxioperation.common;

public enum EnumActionForm {
	THEMMOI(0), SUA(1), XOA(2);

	private final int value;

	EnumActionForm(final int newValue) {
		value = newValue;
	}

	public int getValue() {
		return value;
	}

}
