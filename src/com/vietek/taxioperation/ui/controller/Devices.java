package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.Device;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

public class Devices extends AbstractWindowPanel implements Serializable {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private DeviceDetail detailWindow = null;

	public Devices() {
		super(true);
		this.setTitle("Danh mục thiết bị");
	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Biển số xe", 100, String.class, "getDeviceCode", "DeviceCode", getModelClass()));
		lstCol.add(new GridColumn("Imei", 150, String.class, "getImei", "Imei", getModelClass()));
		lstCol.add(new GridColumn("Ngày kích hoạt", 80, String.class, "getInsertDate", "InsertDate", getModelClass()));
		lstCol.add(new GridColumn("Ngày sử dụng", 80, Date.class, "getUsingDate", "UsingDate", getModelClass()));
		lstCol.add(new GridColumn("Chi nhánh", 300, Agent.class, "getAgent", "agent", getModelClass()));
		lstCol.add(new GridColumn("Số Sim", 150, String.class, "getSimNumber", "SimNumber", getModelClass()));
		lstCol.add(new GridColumn("Đèn mào", 80, Boolean.class, "getLampStatus", "Lamp", getModelClass()));
		lstCol.add(new GridColumn("Khóa điện", 80, Boolean.class, "getACLockStatus"));
		lstCol.add(new GridColumn("Hiện lỗi", 80, Boolean.class, "getDisplayErrorStatus", "DisplayError",
				getModelClass()));
		lstCol.add(new GridColumn("Đăng nhập", 80, Boolean.class, "getLoginStatus"));
		lstCol.add(new GridColumn("Kích hoạt", 80, Boolean.class, "getActiveStatus"));

		setGridColumns(lstCol);

	}

	@Override
	public void loadData() {
//		DeviceController devicecontroller = (DeviceController) ControllerUtils.getController(DeviceController.class);
//		List<Device> lstvalue = devicecontroller.find("from Device");
//		setLstModel(lstvalue);
		setModelClass(Device.class);

	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new DeviceDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

}