package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.VoipCenter;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * 
 * @author VuD
 * 
 */

public class SysCompanys extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysCompanysDetail detailWindow = null;

	public SysCompanys() {
		super(true);
		this.setTitle("Danh mục công ty");
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã", 200, String.class, "getValue"));
		lstCols.add(new GridColumn("Tên", 300, String.class, "getName"));
		lstCols.add(new GridColumn("Công ty cha", 300, Integer.class, "getParentId"));
		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class, "getIsActive"));
		lstCols.add(new GridColumn("Tổng đài", 300, VoipCenter.class, "getVoipCenter"));
		lstCols.add(new GridColumn("Ghi chú", 300, String.class, "getNote"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
//		SysCompanyController controller = (SysCompanyController) ControllerUtils
//				.getController(SysCompanyController.class);
//		List<SysCompany> lstModel = controller.find("from SysCompany");
		this.setStrQuery("from SysCompany");
		this.setModelClass(SysCompany.class);
//		this.setLstModel(lstModel);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new SysCompanysDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

}
