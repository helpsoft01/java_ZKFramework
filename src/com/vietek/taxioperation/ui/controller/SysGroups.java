package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.model.SysGroup;
import com.vietek.taxioperation.model.SysRule;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * 
 * @author VuD
 * 
 */

public class SysGroups extends AbstractWindowPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysGroupsDetail detailWindow = null;

	public SysGroups() {
		super(true);
	}

	@Override
	public void initLeftPanel() {
	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã nhóm", 100, String.class, "getValue","value",getModelClass()));
		lstCols.add(new GridColumn("Tên nhóm", 200, String.class, "getName","name",getModelClass()));
		lstCols.add(new GridColumn("Người dùng", 500, SysUser.class, "getSysUser"));
		lstCols.add(new GridColumn("Rule", 500, SysRule.class, "getSetSysRule"));
		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class, "getIsActive"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		this.setStrQuery("from SysGroup");
		this.setModelClass(SysGroup.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null || !(detailWindow instanceof SysGroupsDetail)) {
			detailWindow = new SysGroupsDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

}
