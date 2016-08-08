package com.vietek.trackingOnline.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonInt {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1500297714606787476L;

	
	int numb;

	@JsonProperty("numb")
	public int getNumb() {
		return numb;
	}

	public void setNumb(int numb) {
		this.numb = numb;
	}
}
