package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.DriverRegister;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * 
 * @author VuD
 * 
 */

public class DriverRegisters extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DriverRegisterDetail detailWindow = null;

	public DriverRegisters() {
		super(true);
		this.setTitle("Đăng ký thiết bị của lái xe");
		this.getBt_add().setVisible(false);
		this.getBt_delete().setVisible(false);
	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Số thẻ lái xe", 200, String.class,
				"getStaffNumber"));
		lstCols.add(new GridColumn("Tài xế", 200, Driver.class,
				"getDriver"));
		lstCols.add(new GridColumn("Id của điện thoại", 200, String.class,
				"getUuid"));
//		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class,
//				"getIsActive"));
//		lstCols.add(new GridColumn("Xử lý", 100, Boolean.class,
//				"getIsProcessed"));
//		lstCols.add(new GridColumn("Approve", 100, Boolean.class,
//				"getIsApproved"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
//		DriverRegisterController controller = (DriverRegisterController) ControllerUtils
//				.getController(DriverRegisterController.class);
//		List<DriverRegister> lstModel = controller.find("from DriverRegister ");
		this.setStrQuery("from DriverRegister");
		this.setModelClass(DriverRegister.class);
//		this.setLstModel(lstModel);

	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new DriverRegisterDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

}
