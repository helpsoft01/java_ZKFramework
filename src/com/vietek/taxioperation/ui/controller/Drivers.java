package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;

import com.vietek.taxioperation.common.DriverUploadImageWindow;
import com.vietek.taxioperation.common.EnumKeyCode;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * 
 * @author VuD
 * 
 */

public class Drivers extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DriversDetail detailWindow = null;
	public static Map<String, String> map = new HashMap<String, String>();
	public static StringBuilder str = new StringBuilder();
	private DriverUploadImageWindow uploadForm = null;

	public Drivers() {
		super(true);
		this.setCtrlKeys("^p^i");
		this.addEventListener(Events.ON_CTRL_KEY, this);
	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Họ tên lái xe", 200, String.class, "getName", "name", getModelClass()));
		lstCols.add(new GridColumn("MSNV", 100, String.class, "getStaffCard", "staffCard", getModelClass()));
		lstCols.add(new GridColumn("Số cá nhân", 150, String.class, "getPhoneNumber", "phoneNumber", getModelClass()));
		lstCols.add(new GridColumn("Văn hóa số", 150, String.class, "getPhoneOffice", "phoneOffice", getModelClass()));
		lstCols.add(new GridColumn("Đơn vị", 300, String.class, "getAgent", "agent", getModelClass()));
		lstCols.add(new GridColumn("UUID", 180, String.class, "getMobileUUID", "mobileUUID", getModelClass()));
		lstCols.add(new GridColumn("Đ.ký App?", 100, Boolean.class, "getIsAppRegister"));
		lstCols.add(new GridColumn("Đánh giá", 100, Double.class, "getRate"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		this.setModelClass(Driver.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new DriversDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CTRL_KEY)) {
			KeyEvent keyEvent = (KeyEvent) event;
			int keyCode = keyEvent.getKeyCode();
			if (keyCode == EnumKeyCode.KEY_P.getValue()) {
				uploadForm = new DriverUploadImageWindow();
				uploadForm.setParent(this);
				uploadForm.doModal();
			} else if (keyCode == EnumKeyCode.KEY_I.getValue()) {
				ImportDriversByExcel importviewer = new ImportDriversByExcel(this);
				importviewer.showModal();
			}
		} else {
			super.onEvent(event);
		}
	}
}