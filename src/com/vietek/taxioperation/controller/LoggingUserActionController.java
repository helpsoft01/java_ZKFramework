package com.vietek.taxioperation.controller;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.vietek.taxioperation.model.LoggingUserAction;

@Repository
public class LoggingUserActionController extends BasicController<LoggingUserAction> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoggingUserActionController() {
		// TODO Auto-generated constructor stub
	}

}
