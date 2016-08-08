package com.vietek.trackingOnline.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonOnline {

	
	String parStringGroup;
	
	int parIntState;
	
	int start;
	
	int limit;
	
	int currentReq;

	
	@JsonProperty("currentReq")
	public int getCurrentReq() {
		return currentReq;
	}

	public void setCurrentReq(int currentReq) {
		this.currentReq = currentReq;
	}

	@JsonProperty("parStringGroup")
	public String getParStringGroup() {
		return parStringGroup;
	}

	public void setParStringGroup(String parStringGroup) {
		this.parStringGroup = parStringGroup;
	}

	
	@JsonProperty("parIntState")
	public int getParIntState() {
		return parIntState;
	}

	public void setParIntState(int parIntState) {
		this.parIntState = parIntState;
	}

	
	@JsonProperty("start")
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	@JsonProperty("limit")
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
