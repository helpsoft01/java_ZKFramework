package com.vietek.taxioperation.util;

public class ExtensionStatus {
	private String extension;
	private String status;

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return extension + "|" + status;
	}

}
