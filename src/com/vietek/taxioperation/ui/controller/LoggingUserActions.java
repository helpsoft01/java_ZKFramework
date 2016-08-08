package com.vietek.taxioperation.ui.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.model.LoggingUserAction;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.GridColumn;

public class LoggingUserActions extends AbstractWindowPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoggingUserActions() {
		// TODO Auto-generated constructor stub
		super(true);
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initColumns() {
		// TODO Auto-generated method stub
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Thời điểm", 200, Timestamp.class, "getTimelog", "timelog", getModelClass()));
		lstCols.add(new GridColumn("Form", 100, String.class, "getFormname", "formname", getModelClass()));
		lstCols.add(new GridColumn("Model", 100, String.class, "getModelname", "modelname", getModelClass()));
		lstCols.add(new GridColumn("Hành động", 80, Integer.class, "getAction", "action", getModelClass()));
		lstCols.add(new GridColumn("Fields", 300, String.class, "getFieldsdetail", "fields", getModelClass()));
		lstCols.add(new GridColumn("Content", 300, String.class, "getValuesdetail", "vals", getModelClass()));
		lstCols.add(new GridColumn("Host", 100, String.class, "getHostname", "hostname", getModelClass()));
		lstCols.add(new GridColumn("IP", 100, String.class, "getIpaddress", "ipaddress", getModelClass()));
		lstCols.add(new GridColumn("User", 80, String.class, "getUserid", "userid", getModelClass()));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		this.setModelClass(LoggingUserAction.class);
	}

}
