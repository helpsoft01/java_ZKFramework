package com.vietek.taxioperation.webservice;

/**
 *
 * @author VuD
 */
public class CallDetailRecord {
	private String callStatus;
	private String callUuid;
	private String direction;
	private String caller;
	private String desCall;
	private String time;

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	public String getCallUuid() {
		return callUuid;
	}

	public void setCallUuid(String callUuid) {
		this.callUuid = callUuid;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String getDesCall() {
		return desCall;
	}

	public void setDesCall(String desCall) {
		this.desCall = desCall;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
