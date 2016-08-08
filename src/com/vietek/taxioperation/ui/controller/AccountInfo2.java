package com.vietek.taxioperation.ui.controller;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.controller.SysUserController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.EditorFactory;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.SecurityUtils;

/**
 * 
 * @author VuD
 * 
 */

public class AccountInfo2 extends Div implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private HomePage homepage;
	// private Window window;
	private SysUser sysUser;
	private Button btn_save;
	private Button btn_cancel;
	private Editor editorCompany;
	private Editor editorSwitchBoard;
	private Editor editorChannel;

	public AccountInfo2() {
		super();
		// window = new Window();
		// window.setParent(this);
		// window.setTitle("Thông tin người dùng");
		// window.setVflex("1");
		// window.setHflex("1");
		this.setHflex("1");
		this.setVflex("1");
		this.setInfo();
		this.init();
		this.initAction();
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(btn_save)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				this.handleClickSave();
			}
		}
		if (event.getTarget().equals(btn_cancel)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				this.handleClickCancel();
			}
		}
	}

	
	@Listen("setDefaultValue")
	public void setDefaultValue(Event event) {
		editorCompany.setValue(AbstractModel.getValue(sysUser, "sysCompany"));
		editorChannel.setValue(AbstractModel.getValue(sysUser, "channel"));
		editorSwitchBoard.setValue(AbstractModel.getValue(sysUser, "switchboard"));
	}

	private void setInfo() {
		int userId = Env.getUserID();
		SysUserController controller = (SysUserController) ControllerUtils.getController(SysUserController.class);
		this.sysUser = controller.get(SysUser.class, userId);
	}

	private void init() {
		this.initMainInfo();
		this.initOtherInfo();
	}

	private void initMainInfo() {
		Panel pnel = new Panel();
		pnel.setParent(this);
		pnel.setSclass("panel-success");
		pnel.setTitle("Thông tin đăng nhập");
		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(pnel);
		Grid grid = new Grid();
		grid.setParent(panelchildren);
		this.initCols(grid);
		this.initRows(grid);

		this.initChangePassword(panelchildren);

	}

	private void initRows(Grid grid) {
		Rows rows = new Rows();
		rows.setParent(grid);
		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã"));
		Textbox tbValue = (Textbox) EditorFactory.getEditor(sysUser, String.class, "value").getComponent();
		tbValue.setValue((String) AbstractModel.getValue(sysUser, "value"));
		tbValue.setReadonly(true);
		// tbValue.setDisabled(true);
		row.appendChild(tbValue);
		row.appendChild(new Label("Tên đăng nhập"));
		Textbox tbUser = (Textbox) EditorFactory.getEditor(sysUser, String.class, "user").getComponent();
		tbUser.setValue((String) AbstractModel.getValue(sysUser, "user"));
		tbUser.setReadonly(true);
		// tbUser.setDisabled(true);
		row.appendChild(tbUser);

		// row = new Row();
		// row.setParent(rows);
		row.appendChild(new Label("Họ tên"));
		Cell cell = new Cell();
		cell.setParent(row);
		// cell.setColspan(3);
		Textbox tbName = (Textbox) EditorFactory.getEditor(sysUser, String.class, "name").getComponent();
		tbName.setValue((String) AbstractModel.getValue(sysUser, "name"));
		tbName.setReadonly(true);
		// tbName.setDisabled(true);
		cell.appendChild(tbName);

	}

	private void initCols(Grid grid) {
		Columns cols = new Columns();
		cols.setParent(grid);
		Column col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("23%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("23%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col = new Column();
		col.setParent(cols);
		col.setHflex("23%");

	}

	private void initChangePassword(Panelchildren panelchildren) {
		final Groupbox groupbox = new Groupbox();
		groupbox.setParent(panelchildren);
		groupbox.setTitle("Đổi mật khẩu");
		// groupbox.setMold("3d");
		groupbox.setOpen(false);

		Vlayout vlayout = new Vlayout();
		vlayout.setParent(groupbox);
		Hlayout hlayout = new Hlayout();
		hlayout.setParent(vlayout);
		Label label = new Label("Mật khẩu cũ");
		label.setParent(hlayout);
		label.setParent(hlayout);
		final Textbox tbOldPassword = new Textbox();
		tbOldPassword.setType("password");
		tbOldPassword.setParent(hlayout);

		// hlayout = new Hlayout();
		// hlayout.setParent(vlayout);
		label = new Label("Mật khẩu mới");
		label.setParent(hlayout);
		final Textbox tbNewPassword = new Textbox();
		tbNewPassword.setType("password");
		tbNewPassword.setParent(hlayout);
		label = new Label("Nhập lại mật khẩu");
		label.setParent(hlayout);
		final Textbox tbRePassword = new Textbox();
		tbRePassword.setType("password");
		tbRePassword.setParent(hlayout);

		hlayout = new Hlayout();
		hlayout.setParent(vlayout);
		Button btnChangePassorwd = new Button();
		btnChangePassorwd.setParent(hlayout);
		btnChangePassorwd.setSclass("btn-success");
		btnChangePassorwd.setLabel("Đôi mật khẩu");
		btnChangePassorwd.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (!this.checkOldPassword()) {
					Clients.showNotification("Bạn nhập sai mật khẩu mất rồi");
				} else {
					if (this.checkNewPassWord()) {
						String newPass = tbNewPassword.getValue();
						String newPassEncrypt = SecurityUtils.encryptMd5(newPass);
						AbstractModel.setValue(sysUser, "password", newPassEncrypt);
						sysUser.save();
						setInfo();
						Clients.showNotification("Đổi mật khẩu thành công");
						groupbox.setOpen(false);
					}
				}
				tbNewPassword.setValue("");
				tbOldPassword.setValue("");
				tbRePassword.setValue("");
			}

			private boolean checkNewPassWord() {
				String newPass = tbNewPassword.getValue();
				if (newPass == null || newPass.isEmpty()) {
					Clients.showNotification("Không được để trống mật khẩu mới");
					return false;
				}
				String rePass = tbRePassword.getValue();
				if (rePass == null || rePass.isEmpty()) {
					Clients.showNotification("Không được để trống nhập lại mật khẩu");
					return false;
				}
				if (newPass.equals(rePass)) {
					return true;
				} else {
					Clients.showNotification("Hai mật khẩu mới không giống nhau");
					return false;
				}
			}

			private boolean checkOldPassword() {
				boolean isResult = false;
				String oldPassword = tbOldPassword.getValue();
				if (oldPassword == null || oldPassword.isEmpty()) {
					return false;
				}
				String oldPassEncrypt = SecurityUtils.encryptMd5(oldPassword);
				String password = sysUser.getPassword();
				if (oldPassEncrypt.equals(password)) {
					isResult = true;
				}
				return isResult;
			}
		});

	}

	private void initOtherInfo() {
		Panel pnel = new Panel();
		pnel.setParent(this);
		pnel.setSclass("panel-success");
		pnel.setTitle("Thông tin mở rộng");
		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(pnel);
		Grid grid = new Grid();
		grid.setParent(panelchildren);
		this.setColOtherInfo(grid);
		this.setRowOtherInfo(grid);
	}

	private void setColOtherInfo(Grid grid) {
		Columns cols = new Columns();
		cols.setParent(grid);
		Column col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setAlign("center");
		col = new Column();
		col.setParent(cols);
		col.setHflex("23%");
		col.setAlign("center");
		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setAlign("center");
		col = new Column();
		col.setParent(cols);
		col.setHflex("23%");
		col.setAlign("center");
		col = new Column();
		col.setParent(cols);
		col.setHflex("10%");
		col.setAlign("center");
		col = new Column();
		col.setParent(cols);
		col.setHflex("23%");
		col.setAlign("center");
	}

	private void setRowOtherInfo(Grid grid) {
		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Công ty"));
		editorCompany = EditorFactory.getMany2OneEditor(sysUser, "sysCompany");
		Combobox comboCompany = (Combobox) editorCompany.getComponent();
		comboCompany.setReadonly(true);
		row.appendChild(comboCompany);
		row.appendChild(new Label("Tổng đài"));
		editorSwitchBoard = EditorFactory.getMany2OneEditor(sysUser, "switchboard");
		Combobox comboSwitchBoard = (Combobox) editorSwitchBoard.getComponent();
		comboSwitchBoard.setReadonly(true);
		row.appendChild(comboSwitchBoard);
		row.appendChild(new Label("Kênh"));
		editorChannel = EditorFactory.getMany2OneEditor(sysUser, "channel");
		Combobox comboChannel = (Combobox) editorChannel.getComponent();
		comboChannel.setReadonly(true);
		row.appendChild(comboChannel);

		Events.echoEvent("setDefaultValue", this, null);
	}

	private void initAction() {
		Hlayout hlayout = new Hlayout();
		hlayout.setParent(this);
		// hlayout.setHflex("1");
		btn_save = new Button();
		btn_save.setParent(hlayout);
		btn_save.setSclass("btn-success");
		btn_save.setLabel("Lưu thông tin");
		btn_save.addEventListener(Events.ON_CLICK, this);

		btn_cancel = new Button();
		btn_cancel.setParent(hlayout);
		btn_cancel.setSclass("btn-warning");
		btn_cancel.setLabel("Hủy bỏ");
		btn_cancel.addEventListener(Events.ON_CLICK, this);
	}

	private void handleClickSave() {
		Clients.showNotification("Cập nhật thông tin thành công");
	}

	private void handleClickCancel() {
		Clients.showNotification("");
	}

}
