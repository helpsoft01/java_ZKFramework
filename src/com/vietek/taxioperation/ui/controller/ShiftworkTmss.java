package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.ShiftworkTmsController;
import com.vietek.taxioperation.model.ShiftworkTms;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class ShiftworkTmss extends AbstractWindowPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ShiftworkTmssDetail detailWindow = null;

	public ShiftworkTmss() {
		super(true);
		this.setTitle("Danh mục ca");
	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Mã ca", 250, String.class, "getValue", "value", getModelClass()));
		lstCol.add(new GridColumn("Tên ca", 250, String.class, "getName", "name", getModelClass()));
		lstCol.add(new GridColumn("Vào ca", 400, Time.class, "getStartshift"));
		lstCol.add(new GridColumn("Rời ca", 400, Time.class, "getStopshift"));
		setGridColumns(lstCol);

	}

	@Override
	public void loadData() {
		ShiftworkTmsController shiftworktmscontroller = (ShiftworkTmsController) ControllerUtils
				.getController(ShiftworkTmsController.class);
		List<ShiftworkTms> lstValue = shiftworktmscontroller.find("from ShiftworkTms");
		setLstModel(lstValue);
		setModelClass(ShiftworkTms.class);

	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new ShiftworkTmssDetail(getCurrentModel(), this);

		}
		return detailWindow;
	}

}