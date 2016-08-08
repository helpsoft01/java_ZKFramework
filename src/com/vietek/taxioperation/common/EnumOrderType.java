package com.vietek.taxioperation.common;

public enum EnumOrderType {
	CREATING(-1), GOI_TONG_DAI(1), APP_SMARTPHONE(2), NHAP_TU_WEB(3), NHAP_TU_SMS(4), NHAP(5);

	private final int value;

	EnumOrderType(final int newValue) {
		value = newValue;
	}

	public int getValue() {
		return value;
	}
}
