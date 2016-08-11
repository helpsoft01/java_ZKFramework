package com.vietek.taxioperation.ui.controller;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class LoggingUserActionDetail extends BasicDetailWindow{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoggingUserActionDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết hành động");
		this.setWidth("800px");
	}

	@Override
	public void createForm(){
		
	}
}
