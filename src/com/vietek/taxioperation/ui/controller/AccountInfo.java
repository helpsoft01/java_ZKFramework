package com.vietek.taxioperation.ui.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.controller.SysUserController;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.SecurityUtils;

public class AccountInfo extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3959936315616983167L;

//	MainApp mainApp;
//	private User user;
//	private UserController userCtrl;
	private SysUserController sysUserCtrl;
	private SysUser sysUser;
	HomePage homePage;
	StringBuffer warningMessage = new StringBuffer();
	@Wire
	Textbox txt_fullName;
	@Wire
	Textbox txt_accountName;
	@Wire
	Textbox txt_password;
	@Wire
	Textbox txt_retypePassword;
	@Wire
	Textbox txt_extNumber;
	@Wire
	Textbox txt_phoneNumber;
	@Wire
	Textbox txt_email;
	@Wire
	Textbox txt_address;
	@Wire
	Datebox db_birthDay;
	@Wire
	Textbox txt_description;
	@Wire
	Panel panel_other_info;

	public AccountInfo() {
		super();
		init();
	}

	public void init() {
//		userCtrl = (UserController) ControllerUtils
//				.getController(UserController.class);
//		setUser(userCtrl.get(User.class, Env.getUserID()));
		sysUserCtrl = (SysUserController) ControllerUtils
				.getController(SysUserController.class);
		this.setSysUser(sysUserCtrl.get(SysUser.class, Env.getUserID()));

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		initAfterCompose();
	}

	private void initAfterCompose() {
//		mainApp = Env.getMainApp();
		homePage = Env.getHomePage();
	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	private boolean validate() {
		warningMessage = new StringBuffer();
		if (!CommonUtils.checkNotNull(txt_fullName.getValue())) {
			warningMessage.append("Hãy nhập họ và tên!\n");
		}
		if (!CommonUtils.checkNotNull(txt_accountName.getValue())) {
			warningMessage.append("Hãy nhập tên đăng nhập!\n");
		}
		if (CommonUtils.checkNotNull(txt_password.getValue())) {
			if (!txt_password.getValue().equals(txt_retypePassword.getValue())) {
				warningMessage.append("Gõ chính xác lại mật khẩu!\n");
			}
		}
		if (warningMessage.length() > 0)
			return false;
		return true;
	}

	@Listen("onClick=#btn_save")
	public void saveAccountInfo() {
		if (!validate()) {
			Clients.showNotification(warningMessage.toString(), "warning",
					null, "bottom_right", 1000);
			return;
		}
		// Save
//		user.setFullName(txt_fullName.getValue());
//		user.setAccountName(txt_accountName.getValue());
//		if (CommonUtils.checkNotNull(txt_password.getValue())) {
//			user.setPassword(txt_password.getValue());
//		}
//		user.setExtNumber(txt_extNumber.getValue());
//		user.setEmail(txt_email.getValue());
//		user.setPhoneNumber(txt_phoneNumber.getValue());
//		user.setAddress(txt_address.getValue());
//		user.setBirthDay(new Timestamp(db_birthDay.getValue().getTime()));
//		user.setDescription(txt_description.getValue());
//		userCtrl.saveOrUpdate(user);
		
		sysUser.setName(txt_fullName.getValue());
		sysUser.setUser(txt_accountName.getValue());
		if (CommonUtils.checkNotNull(txt_password.getValue())) {
			String encryptPass = SecurityUtils.encryptMd5(txt_password.getValue());
			sysUser.setPassword(encryptPass);
		}
		sysUserCtrl.saveOrUpdate(sysUser);
		
		Clients.showNotification("Đã cập nhật thông tin tài khoản!", "info",
				null, "bottom_right", 1000);
//		mainApp.goHomeFunction();
	}

	@Listen("onClick=#btn_other_info")
	public void toggleOtherInfo() {
		panel_other_info.setVisible(!panel_other_info.isVisible());
	}

	@Listen("onClick=#btn_cancel")
	public void onCancel() {
//		mainApp.goHomeFunction();
		
	}
	
}
