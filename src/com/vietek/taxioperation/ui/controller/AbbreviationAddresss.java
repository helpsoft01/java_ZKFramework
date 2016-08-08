package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.AbbreviationAddressController;
import com.vietek.taxioperation.model.AbbreviationAddress;
import com.vietek.taxioperation.ui.editor.TextboxSearchHandler;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class AbbreviationAddresss extends AbstractWindowPanel implements Serializable, TextboxSearchHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AbbreviationAddresssDetail abbreviationaddresssDetail = null;

	public AbbreviationAddresss() {
		super(true);
	}

	@Override
	protected void initLeftPanelTmp() {
		return;
	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Địa chỉ viết tắt", 100, String.class, "getValue", "value", getModelClass()));
		lstCols.add(
				new GridColumn("Địa chỉ đầy đủ", 300, String.class, "getDescription", "description", getModelClass()));
		lstCols.add(new GridColumn("Mô tả", 300, String.class, "getNote"));
		lstCols.add(new GridColumn("Vĩ độ", 150, Double.class, "getLati"));
		lstCols.add(new GridColumn("Kinh độ", 150, Double.class, "getLongi"));
		lstCols.add(new GridColumn("Chi nhánh", 300, String.class, "getAgent", "agent", getModelClass()));
		setGridColumns(lstCols);
	}

	@Override
	public void loadData() {
		AbbreviationAddressController abbreviationaddresscontroller = (AbbreviationAddressController) ControllerUtils
				.getController(AbbreviationAddressController.class);
		List<AbbreviationAddress> lstValue = abbreviationaddresscontroller.findPermission(AbbreviationAddress.class,
				"from AbbreviationAddress");
		setLstModel(lstValue);
		setModelClass(AbbreviationAddress.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (abbreviationaddresssDetail == null)
			abbreviationaddresssDetail = new AbbreviationAddresssDetail(getCurrentModel(), this);

		return abbreviationaddresssDetail;
	}

	@Override
	public void onChanging(String value) {

	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

}