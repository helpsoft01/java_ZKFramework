package com.vietek.tracking.ui.model;

import java.util.List;

import org.apache.commons.collections.Predicate;

public class PredicateUtil implements Predicate {
    public static final int PRE_AND = 1;
    public static final int PRE_OR = 0 ;
	private List<PredicateSearch> Predicates;
	private int type;
	
	public PredicateUtil(List<PredicateSearch> lsttmp, int pretypr) {
		this.Predicates = lsttmp;
		this.type = pretypr;
	}
	@Override
	public boolean evaluate(Object arg0) {
		boolean result = true;
		if (type == PRE_AND) {
			for (PredicateSearch predicate : Predicates) {
				result = result && predicate.evaluate(arg0);
			}
		}else if (type == PRE_OR) {
			for (PredicateSearch predicate : Predicates) {
				result = result || predicate.evaluate(arg0);
			}
		}
		return result;
	}
	
	public List<PredicateSearch> getPredicates() {
		return Predicates;
	}
	public void setPredicates(List<PredicateSearch> predicates) {
		Predicates = predicates;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
