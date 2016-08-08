package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.CallCenterController;
import com.vietek.taxioperation.model.CallCenter;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class CallCenters extends AbstractWindowPanel implements Serializable {

	public CallCenters() {
		super(true);
		this.setTitle("Danh sách CallCenter");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CallCenterDetail detailWindow = null;

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstcols = new ArrayList<GridColumn>();
		lstcols.add(new GridColumn("Tên tổng đài", 150, String.class,
				"getCallname","callname", getModelClass()));
		lstcols.add(new GridColumn("Số điện thoại", 200, String.class,
				"getPhonenmber","phonenmber",getModelClass()));
		lstcols.add(new GridColumn("Ghi chú", 200, String.class, "getNode"));
		this.setGridColumns(lstcols);
	}

	@Override
	public void loadData() {
		CallCenterController callcentercontroller = (CallCenterController) ControllerUtils
				.getController(CallCenterController.class);
		List<CallCenter> lstValue = callcentercontroller
				.find("from CallCenter");
		setLstModel(lstValue);
		setModelClass(CallCenter.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new CallCenterDetail(getCurrentModel(), this);
		}
		return detailWindow;

	}

}
