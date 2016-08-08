package com.vietek.taxioperation.ui.controller;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.controller.DriverController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.DriverRegister;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

/**
 * 
 * @author VuD
 * 
 */

public class DriverRegisterDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DriverController driverController;
	private Button btnActive;
	private Button btnInactive;

	public DriverRegisterDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Duyệt tài xế đăng ký app");
		this.driverController = (DriverController) ControllerUtils
				.getController(DriverController.class);
	}

	@Override
	public void createForm() {

		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);
		Column col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("40%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("40%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Số thẻ lái xe"));
		Editor editor = this.getMapEditor().get("staffNumber");
		Textbox texbox = (Textbox) editor.getComponent();
		texbox.setReadonly(true);
		row.appendChild(texbox);
		row.appendChild(new Label("Tài xế"));
		editor = this.getMapEditor().get("driver");
		Combobox combobox = (Combobox) editor.getComponent();
		combobox.setReadonly(true);
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Id điện thoại"));
		editor = this.getMapEditor().get("uuid");
		texbox = (Textbox) editor.getComponent();
		texbox.setReadonly(true);
		row.appendChild(texbox);
//		row.appendChild(new Label("Kích hoạt"));
//		editor = this.getMapEditor().get("isActive");
//		row.appendChild(editor.getComponent());
//
//		row = new Row();
//		row.setParent(rows);
//		row.appendChild(new Label("Đã xử lý"));
//		editor = this.getMapEditor().get("isProcessed");
//		Checkbox checkbox = (Checkbox) editor.getComponent();
//		checkbox.setDisabled(true);
//		row.appendChild(checkbox);
//		row.appendChild(new Label("Duyệt"));
//		editor = this.getMapEditor().get("isApproved");
//		checkbox = (Checkbox) editor.getComponent();
//		checkbox.setDisabled(true);
//		row.appendChild(checkbox);

	}

	@Override
	public void initUI() {
		super.initUI();
		this.getBtn_save().setVisible(false);
		this.getBtn_cancel().setVisible(false);
		btnActive = new Button();
		btnActive.setParent(this.getBottonLayout());
		btnActive.setClass("btn-success");
		btnActive.setLabel("Kích hoạt");
		btnActive.addEventListener(Events.ON_CLICK, this);
		btnInactive = new Button();
		btnInactive.setParent(this.getBottonLayout());
		btnInactive.setLabel("Khóa");
		btnInactive.addEventListener(Events.ON_CLICK, this);
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(btnActive)) {
			Editor editor = this.getMapEditor().get("isActive");
			AbstractModel
					.setValue(this.getModel(), editor.getDataField(), true);
			editor = this.getMapEditor().get("isProcessed");
			AbstractModel
					.setValue(this.getModel(), editor.getDataField(), true);
			this.updateDriver(this.getModel(), true);
			this.getModel().save();
			Env.getHomePage().showNotification("Đã cập nhật thông tin!",
					Clients.NOTIFICATION_TYPE_INFO);
			this.setVisible(false);
			this.getListWindow().refresh();
		}
		if (event.getTarget().equals(btnInactive)) {
			Editor editor = this.getMapEditor().get("isActive");
			AbstractModel.setValue(this.getModel(), editor.getDataField(),
					false);
			editor = this.getMapEditor().get("isProcessed");
			AbstractModel
					.setValue(this.getModel(), editor.getDataField(), true);
			this.updateDriver(this.getModel(), false);
			this.getModel().save();
			Env.getHomePage().showNotification("Đã cập nhật thông tin!",
					Clients.NOTIFICATION_TYPE_INFO);
			this.setVisible(false);
			this.getListWindow().refresh();
		}
		super.onEvent(event);
	}

	private boolean updateDriver(AbstractModel model, boolean isActive) {
		DriverRegister dRegister = (DriverRegister) model;
		boolean isUpdated = false;
		List<Driver> lstDriver = driverController.find(
				"from Driver where staffCard = ?",
				Integer.parseInt(dRegister.getStaffNumber()));
		if (lstDriver.size() > 0) {
			Driver driver = lstDriver.get(0);
			driver.setMobileUUID(dRegister.getUuid());
			if (isActive) {
				driver.setIsAppRegister(true);
			} else {
				driver.setIsAppRegister(false);
			}
			driver.setPassword(dRegister.getPassword());
			driver.save();
			isUpdated = true;
		}
		return isUpdated;
	}

}
