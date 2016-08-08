package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.TelephoneExtensionTmsController;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.TelephoneExtensionTms;
import com.vietek.taxioperation.model.TelephoneTableTms;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class TelephoneExtensionTmss extends AbstractWindowPanel implements Serializable {

	TelephoneExtensionTmsDetail detailForm;

	public TelephoneExtensionTmss() {

		super(true);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6485470670196208478L;

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {

		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Extension phone", 150, String.class, "getExtension", "extension", getModelClass()));
		lstCol.add(new GridColumn("Bàn điện thoại", 200, TelephoneTableTms.class, "getTelephoneTable", "telephoneTable",
				getModelClass()));
		lstCol.add(new GridColumn("Kênh", 200, ChannelTms.class, "getChannel", "channel", getModelClass()));
		setGridColumns(lstCol);
	}

	@Override
	public void loadData() {

		TelephoneExtensionTmsController teleTablecontroller = (TelephoneExtensionTmsController) ControllerUtils
				.getController(TelephoneExtensionTmsController.class);
		List<TelephoneExtensionTms> lstValue = teleTablecontroller.find("from TelephoneExtensionTms");
		setLstModel(lstValue);
		setModelClass(TelephoneExtensionTms.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {

		if (detailForm == null) {

			detailForm = new TelephoneExtensionTmsDetail(getCurrentModel(), this);
		}
		return detailForm;// super.modifyDetailWindow();
	}
}