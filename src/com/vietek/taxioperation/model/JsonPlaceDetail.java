package com.vietek.taxioperation.model;

import java.util.Map;


public class JsonPlaceDetail {
	private String[] html_attributions;
	private Map<String, Object> result;
	private String status;
	public String[] getHtml_attributions() {
		return html_attributions;
	}
	public void setHtml_attributions(String[] html_attributions) {
		this.html_attributions = html_attributions;
	}
	
	public Map<String, Object> getResult() {
		return result;
	}
	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
