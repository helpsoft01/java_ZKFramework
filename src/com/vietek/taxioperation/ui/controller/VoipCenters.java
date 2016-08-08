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
 */
public class VoipCenters extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VoipCentersDetail detail = null;

	public VoipCenters() {
		super(true);
		this.setTitle("Danh mục tổng đài voip");
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<>();
		lstCols.add(new GridColumn("Mã", 100, String.class, "getValue", "value", getModelClass()));
		lstCols.add(new GridColumn("Tên", 200, String.class, "getName", "name", getModelClass()));
		lstCols.add(new GridColumn("URL", 300, String.class, "getUrl"));
		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class, "getIsActive"));
		lstCols.add(new GridColumn("Công ty", 300, SysCompany.class, "getSysCompany"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void loadData() {
		String sql = "from VoipCenter";
		this.setStrQuery(sql);
		this.setModelClass(VoipCenter.class);

	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detail == null) {
			detail = new VoipCentersDetail(this.getCurrentModel(), this);
		}
		return detail;
	}

}