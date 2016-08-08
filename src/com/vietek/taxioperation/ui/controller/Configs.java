package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;

import com.vietek.taxioperation.model.Config;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

public class Configs extends AbstractWindowPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigsDetail configsDetail = null;

	public Configs() {
		super(true);
		this.setTitle("Cấu hình hệ thống");
		
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Tên", 250, String.class, "getName"));
		lstCol.add(new GridColumn("Giá trị", 350, String.class, "getValue"));
		lstCol.add(new GridColumn("Ghi chú", 300, String.class, "getNote"));
		setGridColumns(lstCol);
	}

	@Override
	public void loadData() {
//		ConfigController configController = (ConfigController) ControllerUtils.getController(ConfigController.class);
//		List<Config> lstValue = configController.find("from Config");
		this.setStrQuery("from Config");
//		setLstModel(lstValue);
		this.setModelClass(Config.class);
	}
	
	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (configsDetail == null) {
			configsDetail = new ConfigsDetail(getCurrentModel(), this);
		}
		return configsDetail;
	}
}
