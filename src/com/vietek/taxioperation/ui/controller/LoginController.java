package com.vietek.taxioperation.ui.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.mq.LogInMQ;
import com.vietek.taxioperation.services.AuthenticationService;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.Localizable;
import com.vietek.taxioperation.util.LocalizationUtils;

public class LoginController extends SelectorComposer<Component>implements EventListener<Event>, Runnable, Localizable {
	private static final long serialVersionUID = 1L;


	@Wire
	Div login_page;

	private Vlayout vl_main;
	private Vlayout vl_login;
	private Combobox cbbShift;
	private Div titleDiv;
	private Textbox tbUser;
	private Textbox tbPass;
	private Button btnLogin;
	private Label lbTitle;
	private Hlayout hlCopyRight;
	// services
	AuthenticationService authService = new AuthenticationService();
	SysUser user;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		init();
		comp.addEventListener(Events.ON_CLIENT_INFO, this);
		Events.echoEvent("focus", tbUser, null);
	}

	public void init() {
		Div div = new Div();
		div.setSclass("div_appname");
		div.setParent(login_page);
		lbTitle = new Label("Hệ thống điều hành Taxi thông minh");
		lbTitle.setSclass("app_name");
		lbTitle.setParent(div);
		vl_main = new Vlayout();
		vl_main.setId("vl_main");
		vl_main.setParent(login_page);
		vl_main.setZclass("none");
		vl_main.setSclass("vl_main");
		vl_main.addEventListener(Events.ON_OK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				long start = System.currentTimeMillis();
				doLogin();
				AppLogger.logDebug.info("TimeLogin:" + (System.currentTimeMillis() - start));
			}
		});
		titleDiv = new Div();
		titleDiv.setId("login_title");
		titleDiv.setSclass("login_title");
		titleDiv.setParent(vl_main);
		initTitle();
		initInput();
		// initShift();
		initButton();
		initLogo();
		initInfo();
		initCopyRight();
	}

	private void initTitle() {
		Hlayout hlayout = new Hlayout();
		hlayout.setId("login_hlTitle");
		hlayout.setSclass("login_hlTitle");
		hlayout.setParent(titleDiv);

		Image image = new Image("./themes/images/logo_58x58.png");
		image.setId("login_icon");
		image.setSclass("login_icon");
		image.setParent(hlayout);
		Label label = new Label("Đăng nhập hệ thống");
		label.setSclass("login_label");
		label.setParent(hlayout);
	}

	private void initInput() {
		vl_login = new Vlayout();
		vl_login.setId("login_vlform");
		vl_login.setSclass("login_vlform");
		vl_login.setParent(vl_main);

		Hlayout hlayout = new Hlayout();
		hlayout.setId("login_hlUser");
		hlayout.setSclass("login_hlInput");
		hlayout.setParent(vl_login);

		Image image = new Image("./themes/images/logo_user.png");
		image.setSclass("login_uicon");
		image.setParent(hlayout);
		tbUser = new Textbox();
		tbUser.setId("tb_user");
		tbUser.setPlaceholder("Tài khoản ...");
		tbUser.setSclass("tb_input");
		tbUser.setParent(hlayout);

		hlayout = new Hlayout();
		hlayout.setSclass("login_hlInput");
		hlayout.setParent(vl_login);

		image = new Image("./themes/images/logo_pass.png");
		image.setSclass("login_uicon");
		image.setParent(hlayout);
		tbPass = new Textbox();
		tbPass.setId("tb_pass");
		tbPass.setSclass("tb_input");
		tbPass.setType("password");
		tbPass.setPlaceholder("Mật khẩu ...");
		tbPass.setParent(hlayout);
	}

	public void initShift() {
		Div div = new Div();
		div.setId("div_shift");
		div.setSclass("div_shift");
		div.setParent(vl_login);

		Hlayout hlayout = new Hlayout();
		hlayout.setStyle("float: right; height: 24px;");
		hlayout.setParent(div);
		Label label = new Label("Ca làm việc >>");
		label.setSclass("lb_shift");
		label.setParent(hlayout);
		label.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				cbbShift.setVisible(!cbbShift.isVisible());
			}
		});

		cbbShift = new Combobox();
		cbbShift.setWidth("80px");
		cbbShift.setSclass("cb_shift");
		cbbShift.setParent(hlayout);
		cbbShift.appendItem("Ca 1");
		cbbShift.appendItem("Ca 2");
		cbbShift.appendItem("Ca 3");
		cbbShift.setReadonly(true);
		cbbShift.setVisible(false);
	}

	private void initButton() {
		btnLogin = new Button("Đăng nhập");
		btnLogin.setId("login_btn");
		btnLogin.setSclass("login_btn");
		btnLogin.setParent(vl_main);
		btnLogin.addEventListener(Events.ON_CLICK, this);
	}

	private void initLogo() {

		try {

			Hlayout hlayout = new Hlayout();
			hlayout.setId("login_mlvt");
			hlayout.setSclass("login_mlvt");
			hlayout.setParent(login_page);
			Image image = new Image("./themes/images/logo_ml.png");
			image.setSclass("logo_comp");
			image.setParent(hlayout);
			image = new Image("./themes/images/logo_vt.png");
			image.setSclass("logo_comp");
			image.setStyle("margin-left: 20px;");
			image.setParent(hlayout);
		} catch (Exception ex) {

		}
	}

	private void initInfo() {
		Vlayout vlayout = new Vlayout();
		vlayout.setSclass("comp_info");
		vlayout.setParent(login_page);
		Label label = new Label("CÔNG TY CỔ PHẦN TẬP ĐOÀN MAI LINH");
		label.setSclass("company_info");
		label.setParent(vlayout);
		label = new Label("Địa chỉ: 64-68 Hai Bà Trưng, phường Bến Nghé, Quận 1, Tp. Hồ Chí Minh");
		label.setSclass("company_info");
		label.setParent(vlayout);
		label = new Label("Điện thoại: 08.38 29 8888");
		label.setSclass("company_info");
		label.setParent(vlayout);
		label = new Label("Fax: 08.8 224496");
		label.setSclass("company_info");
		label.setParent(vlayout);
	}

	private void initCopyRight() {
		hlCopyRight = new Hlayout();
		hlCopyRight.setSclass("hl_copyright");
		hlCopyRight.setParent(login_page);
		Div div = new Div();
		div.setStyle("margin-top: 15px;");
		div.setParent(hlCopyRight);
		Label label = new Label("Copyright © Vietek JSC - vietek.com.vn");
		label.setSclass("copy_right");
		label.setParent(div);
		Image image = new Image("./themes/images/logo_vt.png");
		image.setStyle("height: 30px;");
		image.setParent(hlCopyRight);
	}

	public void doLogin() {
		btnLogin.setDisabled(true);
		String nm = tbUser.getValue();
		String pd = tbPass.getValue();
		if (nm == "" || pd == "") {
			Clients.showNotification("Chưa nhập đủ thông tin đăng nhập", "error", null, "bottom_center", 2000, true);
			btnLogin.setDisabled(false);
		} else {
			//SysUser user = authService.login(nm, pd);
			user = authService.login(nm, pd);
			if (user == null) {
				AppLogger.logUserAction.info("Login|False|User:" + nm);
				String message = "Thông tin đăng nhập không đúng";
				Clients.showNotification(message, "error", null, "middle_center", 3000, true);
				btnLogin.setDisabled(false);
			} else {
				if (!user.getIsActive()) {
					String message = "Tài khoản của bạn đã bị khóa. Liên lạc với admin để biết thêm chi tiết";
					Clients.showNotification(message, "error", null, "middle_center", 3000, true);
					btnLogin.setDisabled(false);
				} else {
					Executions.sendRedirect("/");
					Env.setIsLogged(true);
					LogInMQ.publish(new Event(LogInMQ.LOG_IN_EVENT, null, user));
				}
			}
		}
	}

	@Override
	public String getString(String key, String... values) {
		return LocalizationUtils.getString(key, values);
	}

	@Override
	public void run() {
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CLIENT_INFO)) {
			Clients.evalJavaScript("initLogin()");
			ClientInfoEvent clientInfoEvent = (ClientInfoEvent) event;
			if (clientInfoEvent.getDesktopWidth() > clientInfoEvent.getDesktopHeight())
				login_page.setSclass("login_page_landscape");
			else
				login_page.setSclass("login_page_portraint");
			if (clientInfoEvent.getDesktopWidth() < 800) {
				lbTitle.setSclass("app_name_small");
			} else {
				lbTitle.setSclass("app_name");
			}
			if (clientInfoEvent.getDesktopWidth() < 500) {
				hlCopyRight.setVisible(false);
			} else {
				hlCopyRight.setVisible(true);
			}
		} else if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget().equals(btnLogin)) {
				long start = System.currentTimeMillis();
				doLogin();
				AppLogger.logDebug.info("TimeLogin:" + (System.currentTimeMillis() - start));
			}
		}
	}
}
