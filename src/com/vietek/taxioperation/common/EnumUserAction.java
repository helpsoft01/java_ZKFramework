package com.vietek.taxioperation.common;

public enum EnumUserAction {
	LOGIN(0), LOGOUT(1), UPDATE(2), INSERT(3), DELETE(4), VIEWING(5);
	
	private final int value;
	
	EnumUserAction (final int newValue) {
		value = newValue;
	}

	public int getValue() {
		return value;
	}

}
