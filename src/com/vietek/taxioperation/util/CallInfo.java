/**
 * @author tuanpa
 */
package com.vietek.taxioperation.util;

import java.sql.Timestamp;

public class CallInfo {
	
	public static int CALL_EVENT_DIALING = 1;
	public static int CALL_EVENT_ANSWER = 2;
	public static int CALL_EVENT_TRIM = 3;
	public static int CALL_EVENT_OTHER = 4;
	
	private String extension;
	private String callUuid;
	private Timestamp time;
	private String callerNumber;
	private String callCenterId;
	private int event;
	
	public CallInfo(String extension, String callUuid, Timestamp time, String callerNumber, String callCenterId,
			int event) {
		super();
		this.extension = extension;
		this.callUuid = callUuid;
		this.time = time;
		this.callerNumber = callerNumber;
		this.callCenterId = callCenterId;
		this.event = event;
	}
	public String getExtension() {
		return extension;
	}
	public String getCallUuid() {
		return callUuid;
	}
	public Timestamp getTime() {
		return time;
	}
	public String getCallerNumber() {
		return callerNumber;
	}
	public String getCallCenterId() {
		return callCenterId;
	}
	public int getEvent() {
		return event;
	}
	
	public void setEvent(int event) {
		this.event = event;
	}
	public String getVoipExtension() {
		return callCenterId + "_" + extension;
	}
}
