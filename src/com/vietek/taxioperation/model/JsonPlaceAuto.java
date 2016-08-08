package com.vietek.taxioperation.model;

public class JsonPlaceAuto {
	private String id;
	private String[] matched_substrings;
	private String place_id;
	private String reference;
	private String[] terms;
	private String[] types;
//	private String[] predictions;
	private String description;

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

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
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

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(id);
		sb.append(matched_substrings);
		sb.append(place_id);
		sb.append(reference);
		sb.append(reference);
		sb.append(terms);
		sb.append(types);
		return sb.toString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
