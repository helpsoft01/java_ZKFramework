package com.vietek.taxioperation.webservice;

/**
 * Cac trang thai cuoc goi tu callcenter
 * 
 * @author VuD
 */
public enum EnumCallStatus {
	START("Start"), DIALING("Dialing"), DIAL_ANSWER("DialAnswer"), HANGUP("HangUp"), TRIM("Trim"), CDR("CDR");
	private String callStatus;

	private EnumCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getValue() {
		return callStatus;
	}
}
