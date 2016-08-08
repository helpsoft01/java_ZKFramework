package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.TelephoneTableTmsController;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.TelephoneTableTms;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class TelephoneTableTmss extends AbstractWindowPanel implements Serializable {

	TelephoneTableTmsDetail detailForm;

	public TelephoneTableTmss() {

		super(true);

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {

		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Mã bàn", 150, String.class, "getValue", "value", getModelClass()));
		lstCol.add(new GridColumn("Tên bàn", 200, String.class, "getName", "name", getModelClass()));
		lstCol.add(new GridColumn("Công ty", 200, SysCompany.class, "getSysCompany", "sysCompany", getModelClass()));
		setGridColumns(lstCol);
	}

	@Override
	public void loadData() {

		TelephoneTableTmsController teleTablecontroller = (TelephoneTableTmsController) ControllerUtils
				.getController(TelephoneTableTmsController.class);
		List<TelephoneTableTms> lstValue = teleTablecontroller.find("from TelephoneTableTms");
		setLstModel(lstValue);
		setModelClass(TelephoneTableTms.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {

		if (detailForm == null) {

			detailForm = new TelephoneTableTmsDetail(getCurrentModel(), this);
		}
		return detailForm;// super.modifyDetailWindow();
	}
}