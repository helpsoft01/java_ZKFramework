package com.vietek.taxioperation.model;

import java.sql.Timestamp;

public class Call {

	private String voipExtension;
	private String callUuid;
	private int agentId;
	private Timestamp time;
	private Customer customer;
	private int index = 0;

	public String getCallUuid() {
		return callUuid;
	}

	public void setCallUuid(String callUuid) {
		this.callUuid = callUuid;
	}

	public String getVoipExtension() {
		return voipExtension;
	}

	public void setVoipExtension(String extensionNumber) {
		this.voipExtension = extensionNumber;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
