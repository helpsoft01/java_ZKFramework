package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.RegionController;
import com.vietek.taxioperation.model.Region;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class Regions extends AbstractWindowPanel implements Serializable {

	private static final long serialVersionUID = 1L;
	private RegionsDetail regiondetail = null;

	/**
	 * @MPV
	 */

	public Regions() {
		super(true);
		this.setTitle("Danh mục vùng miền");
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Mã vùng miền", 250, String.class,
				"getValue", "value", getModelClass()));
		lstCol.add(new GridColumn("Tên vùng miền", 250, String.class,
				"getRegionName", "RegionName", getModelClass()));
		setGridColumns(lstCol);
	}

	@Override
	public void loadData() {
		RegionController regioncontroller = (RegionController) ControllerUtils
				.getController(RegionController.class);
		List<Region> lstvalue = regioncontroller.find("from Region");
		setLstModel(lstvalue);
		setModelClass(Region.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (regiondetail == null) {
			regiondetail = new RegionsDetail(getCurrentModel(), this);
		}
		return regiondetail;
	}

}
