package com.vietek.taxioperation.model;

public class MyPrediction {
	String description;
	String place_id;
	String id;
	String[] matched_substrings;
	String reference;
	String[] terms;
	String[] types;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPlace_id() {
		return place_id;
	}
	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String[] getMatched_substrings() {
		return matched_substrings;
	}
	public void setMatched_substrings(String[] matched_substrings) {
		this.matched_substrings = matched_substrings;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String[] getTerms() {
		return terms;
	}
	public void setTerms(String[] terms) {
		this.terms = terms;
	}
	public String[] getTypes() {
		return types;
	}
	public void setTypes(String[] types) {
		this.types = types;
	}
	
}
