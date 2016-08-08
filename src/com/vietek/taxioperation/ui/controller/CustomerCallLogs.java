package com.vietek.taxioperation.ui.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;

import com.vietek.taxioperation.controller.CustomerCallLogController;
import com.vietek.taxioperation.model.CustomerCallLog;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class CustomerCallLogs extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerCallLogs() {
		super(true);
		this.setTitle("Log");
		this.getBt_add().setVisible(false);
		this.getBt_delete().setVisible(false);
		this.getBt_edit().setVisible(false);
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Số khách hàng", 200, String.class, "getCustomerPhone"));
		lstCols.add(new GridColumn("Số điện thoại tài xế", 200, String.class, "getDriverPhone"));
		lstCols.add(new GridColumn("Số tài", 200, String.class, "getVehicleNumber"));
		lstCols.add(new GridColumn("Thời gian", 150, Timestamp.class, "getCreated"));
		setGridColumns(lstCols);
	}

	@Override
	public void loadData() {
		CustomerCallLogController customerCallLogController = (CustomerCallLogController) ControllerUtils.getController(CustomerCallLogController.class);
		List<CustomerCallLog> lstModel = customerCallLogController.find("from CustomerCallLog order by created desc");
		setLstModel(lstModel);
		setModelClass(CustomerCallLog.class);
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
	}

}
