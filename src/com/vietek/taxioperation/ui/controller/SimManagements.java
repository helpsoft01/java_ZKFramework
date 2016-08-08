package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;

import com.vietek.taxioperation.model.SimManagement;
import com.vietek.taxioperation.model.SimProvider;
import com.vietek.taxioperation.ui.editor.TextboxSearchHandler;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

public class SimManagements extends AbstractWindowPanel implements Serializable, TextboxSearchHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SimManagementDetail simManagementDetail;
	
	public SimManagements() {
		super(true);
		this.setTitle("Quản lý sim");
	}

	@Override
	public void onChanging(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initColumns() {
		// TODO Auto-generated method stub
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();	
		lstCol.add(new GridColumn("Mã Thiết bị",100,String.class,"getCodeDevice", "CodeDevice",getModelClass()));
		lstCol.add(new GridColumn("Số SIM", 150, String.class,
				"getSimNumber","SimNumber",getModelClass()));		
		lstCol.add(new GridColumn("Nhà mạng", 100, SimProvider.class, 
				"getSimProvider","simProvider", getModelClass()));
		lstCol.add(new GridColumn("Loại thuê bao", 100, Integer.class,
				"getSimType","SimType", getModelClass()));
		lstCol.add(new GridColumn("Ngày kích hoạt", 100, String.class,
				"getSimActiveDateString", "simActiveDate", getModelClass()));		
		lstCol.add(new GridColumn("Seri SIM", 350, String.class, 
				"getSeriSim", "seriSim", getModelClass()));
		lstCol.add(new GridColumn("Thông tin sim", 400, String.class,
				"getSimLogs"));
		
		setGridColumns(lstCol);
	}

	@Override
	public void loadData() {
//		// TODO Auto-generated method stub
//		SimManagementController simManagementController = (SimManagementController) ControllerUtils
//				.getController(SimManagementController.class);
//		List<SimManagement> lstvalue = simManagementController.find("from SimManagement");
//		setLstModel(lstvalue);
		setModelClass(SimManagement.class);
	}
	
	@Override
	public BasicDetailWindow modifyDetailWindow(){
		if (simManagementDetail == null) {
			simManagementDetail = new SimManagementDetail(getCurrentModel(), this);
		}
		
		return simManagementDetail;
	}

}
