package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.model.SysAction;
import com.vietek.taxioperation.model.SysFunction;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * 
 * @author VuD
 * 
 * 
 */

public class SysFunctions extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysFunctionsDetail detailWindow = null;

	public SysFunctions() {
		super(true);
		this.setTitle("Chức năng");
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Tên chức năng", 200, String.class,
				"getName"));
		lstCols.add(new GridColumn("Class", 200, String.class, "getClazz"));
		lstCols.add(new GridColumn("zul file", 200, String.class, "getZulFile"));
		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class,
				"getIsActive"));
		lstCols.add(new GridColumn("Hành động", 300, SysAction.class,
				"getSysAction"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
//		SysFunctionController controller = (SysFunctionController) ControllerUtils
//				.getController(SysFunctionController.class);
//		List<SysFunction> lstModel = controller.find("from SysFunction");
		this.setStrQuery("from SysFunction");
		this.setModelClass(SysFunction.class);
//		this.setLstModel(lstModel);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new SysFunctionsDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

}
