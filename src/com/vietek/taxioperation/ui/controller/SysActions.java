package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.model.SysAction;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * 
 * @author VuD
 * 
 */

public class SysActions extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysActionsDetail detailWindow = null;

	public SysActions() {
		super(true);
		this.setTitle("Danh mục hành động");
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã", 100, String.class, "getValue"));
		lstCols.add(new GridColumn("Tên", 100, String.class, "getName"));
		lstCols.add(new GridColumn("Kích hoạt", 100, String.class, "getIsActive"));
		lstCols.add(new GridColumn("Ghi chú", 100, String.class, "getNote"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		// SysActionController controller = (SysActionController)
		// ControllerUtils
		// .getController(SysActionController.class);
		// List<SysAction> lstModel = new ArrayList<SysAction>();
		// lstModel = controller.find("from SysAction");
		this.setStrQuery("from SysAction");
		this.setModelClass(SysAction.class);
		// this.setLstModel(lstModel);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new SysActionsDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

}
