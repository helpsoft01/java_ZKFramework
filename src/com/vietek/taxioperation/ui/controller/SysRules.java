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
 */
public class SysRules extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysRulesDetail detail = null;

	public SysRules() {
		super(true);
		this.setTitle("Phân quyền dữ liệu");
	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<>();
		lstCols.add(new GridColumn("Mã", 100, String.class, "getValue"));
		lstCols.add(new GridColumn("Tên", 200, String.class, "getName"));
		lstCols.add(new GridColumn("Mô tả", 200, String.class, "getDescription"));
		lstCols.add(new GridColumn("Model", 100, String.class, "getModelName"));
		lstCols.add(new GridColumn("Độ ưu tiên", 100, Integer.class, "getPriority"));
		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class, "getIsActive"));
		lstCols.add(new GridColumn("Nhóm", 200, SysGroup.class, "getSetSysGroup"));
		lstCols.add(new GridColumn("Người dùng", 200, SysUser.class, "getSetSysUser"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		this.setModelClass(SysRule.class);
		this.setStrQuery("from SysRule");
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detail == null) {
			detail = new SysRulesDetail(this.getCurrentModel(), this);
		}
		return detail;
	}

}
