package com.vietek.taxioperation.util;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.vietek.taxioperation.model.TelephoneExtensionTms;

public class JsonExtension {

	@JsonIgnore
	public static final String SUFFIXSTATUS_INUSE = "Inuse";
	public static final String SUFFIXSTATUS_NOTINUSE = "Notinuse";
	public static final String FREFIXSTATUS_REGISTERED = "registered";
	public static final String FREFIXSTATUS_UN_REGISTERED = "unregistered";
	private String extension;
	private String status;

	// private String callername;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	private TelephoneExtensionTms extensionTms;

	@JsonIgnore
	public TelephoneExtensionTms getExtensionTms() {
		return extensionTms;
	}

	@JsonIgnore
	public void setExtensionTms(TelephoneExtensionTms extensionTms) {
		this.extensionTms = extensionTms;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

}
