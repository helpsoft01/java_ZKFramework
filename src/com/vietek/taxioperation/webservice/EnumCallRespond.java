package com.vietek.taxioperation.webservice;

/**
 * Gia tri tra ve cho callcenter
 * 
 * @author VuD
 */
public enum EnumCallRespond {
	SUCCESS(200), BAD_REQUEST(400), INVALID_SCRET_KEY(401), SERVER_ERR(500);
	private int value;

	private EnumCallRespond(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
