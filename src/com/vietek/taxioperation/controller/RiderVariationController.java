package com.vietek.taxioperation.controller;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.vietek.taxioperation.model.RiderVariationOrder;

@Repository
public class RiderVariationController extends BasicController<RiderVariationOrder> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public RiderVariationController(){
		
	}
}
