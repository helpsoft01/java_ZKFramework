package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.ConfigController;
import com.vietek.taxioperation.model.Config;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;

public class ConfigSystem extends Div implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Tabbox tabMain;
	
	private String title;
	private final int TYPE_EMAIL = 1;
	private final int TYPE_SMS = 2;
	private final int TYPE_APP = 3;
	private final int TYPE_SYSTEM = 4;
	
	/*-- Email --*/
	
	List<Config> lstEmail = new ArrayList<Config>();
	List<String> lstNameEmail = new ArrayList<String>();
	Map<String,String> mapEmail = new HashMap<String, String>();
	
	/*-- SMS --*/
	List<Config> lstSMS = new ArrayList<Config>();
	List<String> lstNameSMS = new ArrayList<String>();
	Map<String,String> mapSMS = new HashMap<String, String>();
	
	/*-- App --*/
	List<Config> lstApp = new ArrayList<Config>();
	List<String> lstNameApp = new ArrayList<String>();
	Map<String,String> mapApp = new HashMap<String, String>();
	
	/*-- Other --*/
	List<Config> lstSystem = new ArrayList<Config>();
	List<String> lstNameSystem = new ArrayList<String>();
	Map<String,String> mapSystem = new HashMap<String, String>();
	private ConfigController configController;

	public ConfigSystem() {
		setTitle("Cấu hình hệ thống");
		initValue();
		initUI();
	}

	private void initValue() {
		configController = (ConfigController) ControllerUtils
				.getController(ConfigController.class);
	}

	private void initUI() {
		Vlayout vlMain = new Vlayout();
		vlMain.setStyle("width:100%;");
		vlMain.setVflex("1");
		vlMain.setParent(this);
		Label lbTitle = new Label(title);
		lbTitle.setStyle("font-size: 20px; font-weight: bold; color: green; margin: 10px; margin-bottom: 22px;padding-right: 50px");
		lbTitle.setParent(vlMain);

		tabMain = new Tabbox();
		tabMain.setOrient("left");
		tabMain.setStyle("width:100%; height: 100%");
		tabMain.setParent(vlMain);
		initTabbox();
	}

	private void initTabbox() {
		// tabs
		Tabs tabs = new Tabs();
		tabs.setSclass("tabs_config");
		tabs.setStyle("width:200px");
		tabs.setParent(tabMain);
		Tab tab = new Tab();
		tab.setLabel("Cấu hình Email và SMS");
		tab.setSclass("tab_config");
		tab.setParent(tabs);
		tab = new Tab("Cấu hình điều hành app");
		tab.setSclass("tab_config");
		tab.setParent(tabs);
		tab = new Tab("Cấu hình hệ thống");
		tab.setSclass("tab_config");
		tab.setParent(tabs);

		/*-- Tab Email and SMS --*/
		Tabpanels tabpanels = new Tabpanels();
		tabpanels.setStyle("height:100%");
		tabpanels.setParent(tabMain);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setSclass("tab_panel_config");
		tabpanel.setParent(tabpanels);
		initEmailConfig(tabpanel);
		
		/*-- Tab Configuration app --*/
		tabpanel = new Tabpanel();
		tabpanel.setSclass("tab_panel_config");
		tabpanel.setParent(tabpanels);
		initConfigsApp(tabpanel);
		
		/*-- Tab Configuration Systems --*/
		tabpanel = new Tabpanel();
		tabpanel.setSclass("tab_panel_config");
		tabpanel.setParent(tabpanels);
		initConfigsSystem(tabpanel);
	}

	/*-- tab email configuration and SMS --*/
	
	private void initEmailConfig(Component comp) {
		lstEmail = getData(TYPE_EMAIL);
		Vlayout vlEmail = new Vlayout();
		vlEmail.setWidth("95%");
		vlEmail.setStyle("overflow:scroll;max-height:500px;display:block;");
		vlEmail.setParent(comp);
		
		/*-- danh sách cấu hình Email --*/
		Div div1 = new Div();
		div1.setSclass("div_title_list_config");
		div1.setParent(vlEmail);
		Label labelEmail = new Label("1. Danh sách cấu hình Email");
		labelEmail.setSclass("title_list_config");
		div1.appendChild(labelEmail);
		for (Config config : lstEmail) {
			Hlayout hlayout = new Hlayout();
			hlayout.setStyle("detail_config");
			hlayout.setParent(vlEmail);

			Div div = new Div();
			div.setSclass("left_config_email");
			div.setParent(hlayout);
			Label label = new Label("Email");
			label.setSclass("text_email_config");
			div.appendChild(label);

			Vlayout vlayout = new Vlayout();
			vlayout.setSclass("right_config_email");
			vlayout.setZclass("none");
			vlayout.setParent(hlayout);
			Textbox textbox = new Textbox();
			textbox.setStyle("width:50%");
			textbox.setAttribute("name", config.getName());
			textbox.setParent(vlayout);
			textbox.addEventListener(Events.ON_CHANGE,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
							String configName = (String) event.getTarget().getAttribute("name");
							if (configName.equals("EMAIL_SUPPORT")) {
								mapEmail.put("EMAIL_SUPPORT", ((Textbox) event.getTarget()).getValue());
							} else if (configName.equals("EMAIL_SUPPORT_PASS")) {
								mapEmail.put("EMAIL_SUPPORT_PASS", ((Textbox) event.getTarget()).getValue());
							} else if (configName.equals("EMAIL_SERVER_HOST")) {
								mapEmail.put("EMAIL_SERVER_HOST", ((Textbox) event.getTarget()).getValue());
							} else if (configName.equals("EMAIL_SERVER_PORT")) {
								mapEmail.put("EMAIL_SERVER_PORT", ((Textbox) event.getTarget()).getValue());
							}
						}
					});

			Label label2 = new Label();
			label2.setSclass("example_configemail");
			label2.setWidth("100%");
			label2.setClass("note_config");
			label2.setParent(vlayout);

			if (config.getName().equals("EMAIL_SUPPORT")) {
				label.setValue("Email");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			} else if (config.getName().equals("EMAIL_SUPPORT_PASS")) {
				label.setValue("Mật khẩu");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			} else if (config.getName().equals("EMAIL_SERVER_HOST")) {
				label.setValue("Email server");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			} else if (config.getName().equals("EMAIL_SERVER_PORT")) {
				label.setValue("Email port");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			} 
		}
		
		/*-- danh sách cấu hình SMS --*/
		
		lstSMS = getData(TYPE_SMS);
		List<Config> lstSMSTemp = new ArrayList<Config>();
		Div div2 = new Div();
		div2.setSclass("div_title_list_config");
		div2.setParent(vlEmail);
		Label labelSMS = new Label("2. Danh sách cấu hình SMS");
		labelSMS.setSclass("title_list_config");
		div2.appendChild(labelSMS);
		
		Vlayout vlayout = new Vlayout();
		Label label = new Label();
		Label label2 = new Label();
		Hlayout hlayout = new Hlayout();
		Div div = new Div();
		
		Config configSms = new Config();
		for (int i = 0; i < lstSMS.size(); i++) {
			Config bean = lstSMS.get(i);
			if(StringUtils.equals(bean.getName(),"SMS_NOTIFY_DRIVER_REGISTER")){
				configSms = bean;
			}else{
				lstSMSTemp.add(bean);
			}
		}
		
		for (Config config : lstSMSTemp) {
			hlayout = new Hlayout();
			hlayout.setStyle("detail_config");
			hlayout.setParent(vlEmail);

			div = new Div();
			div.setSclass("left_config_email");
			div.setParent(hlayout);
			label = new Label("Email");
			label.setSclass("text_email_config");
			div.appendChild(label);

			vlayout = new Vlayout();
			vlayout.setSclass("right_config_email");
			vlayout.setZclass("none");
			vlayout.setParent(hlayout);
			Textbox textbox = new Textbox();
			textbox.setStyle("width:50%");
			textbox.setAttribute("name", config.getName());
			textbox.setParent(vlayout);
			textbox.addEventListener(Events.ON_CHANGE,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
							String configName = (String) event.getTarget().getAttribute("name");
							if (configName.equals("SMS_USERNAME")) {
								mapSMS.put("SMS_USERNAME", ((Textbox) event.getTarget()).getValue());
							} else if (configName.equals("SMS_PASSWORD")) {
								mapSMS.put("SMS_PASSWORD", ((Textbox) event.getTarget()).getValue());
							} else if (configName.equals("SMS_BRANDNAME")) {
								mapSMS.put("SMS_BRANDNAME", ((Textbox) event.getTarget()).getValue());
							}
						}
					});

			Label label3 = new Label();
			label3.setSclass("example_configemail");
			label3.setWidth("100%");
			label3.setParent(vlayout);
			label3.setClass("note_config");
			if (config.getName().equals("SMS_USERNAME")) {
				label.setValue("Tên đăng nhập");
				label3.setValue(config.getNote());
				textbox.setValue(config.getValue());
			} else if (config.getName().equals("SMS_PASSWORD")) {
				label.setValue("Mật khẩu");
				label3.setValue(config.getNote());
				label3.setStyle("");
				textbox.setValue(config.getValue());
			} else if (config.getName().equals("SMS_BRANDNAME")) {
				label.setValue("Brand name");
				label3.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}
		}
		
		/*-- Create layout config send SMS driver --*/
		hlayout = new Hlayout();
		hlayout.setStyle("detail_config");
		hlayout.setParent(vlEmail);
		div = new Div();
		div.setSclass("left_config_email");
		div.setParent(hlayout);
		
		label = new Label();
		label.setSclass("text_email_config");
		div.appendChild(label);
		vlayout = new Vlayout();
		vlayout.setSclass("right_config_email");
		vlayout.setZclass("none");
		vlayout.setParent(hlayout);
		createUISendSMSDriverRegister(vlayout, configSms);
		label2 = new Label();
		label2.setSclass("example_configemail");
		label2.setWidth("100%");
		label2.setClass("note_config");
		label2.setParent(vlayout);
		label.setValue("Gửi tin nhắn tài xế đăng ký đón");
		label2.setValue(configSms.getNote());
		
		/*-- handle save data --*/
		Button btnSave = new Button("Cập nhật");
		btnSave.setImage("/themes/images/save_16.png");
		btnSave.setParent(vlEmail);
		btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				if (checkEmail()) {
					saveToDB(lstEmail, getListParams(lstNameEmail, TYPE_EMAIL),mapEmail);
					saveToDB(lstSMS, getListParams(lstNameSMS, TYPE_SMS),mapSMS);
					Clients.showNotification("Đã cập nhật thông tin !","info", null, "middle_center", 3000, true);
				} else
					Clients.showNotification("Địa chỉ mail không hợp lệ !","error", null, "middle_center", 3000, true);
			}
		});
	}
	
	/* -- Application mobile --*/
	
	private void initConfigsApp(Component comp){
		lstApp = getData(TYPE_APP);
		List<Config> lstAppTem = new ArrayList<Config>();
		Vlayout vlApp = new Vlayout();// overflow: scroll;
		vlApp.setWidth("95%");
		vlApp.setStyle("overflow:scroll;max-height:500px;display:block;");
		vlApp.setParent(comp);
		
		/*-- danh sách cấu hình App --*/
		Div div1 = new Div();
		div1.setSclass("div_title_list_config");
		div1.setParent(vlApp);
		Label labelEmail = new Label("Danh sách cấu hình ứng dụng Mobile");
		labelEmail.setSclass("title_list_config");
		div1.appendChild(labelEmail);
		Vlayout vlayout = new Vlayout();
		Label label = new Label();
		Label label2 = new Label();
		Hlayout hlayout = new Hlayout();
		Div div = new Div();
		Config configFindType = new Config();
		Config configOpen99 = new Config();
		for (int i = 0; i < lstApp.size(); i++) {
			Config bean = lstApp.get(i);
			if(StringUtils.equals(bean.getName(),"FIND_TAXI_TYPE")){
				configFindType = bean;
			}else if(StringUtils.equals(bean.getName(),"SHOW_OPEN99")){
				configOpen99 = bean;
			}else{
				lstAppTem.add(bean);
			}
		}
		
		for (Config config : lstAppTem) {
			hlayout = new Hlayout();
			hlayout.setStyle("detail_config");
			hlayout.setParent(vlApp);
			div = new Div();
			div.setSclass("left_config_email");
			div.setParent(hlayout);
			label = new Label();
			label.setSclass("text_email_config");
			div.appendChild(label);
			vlayout = new Vlayout();
			vlayout.setSclass("right_config_email");
			vlayout.setZclass("none");
			vlayout.setParent(hlayout);
			Textbox textbox = new Textbox();
			textbox.setStyle("width:50%");
			textbox.setAttribute("name", config.getName());
			textbox.setParent(vlayout);
			textbox.addEventListener(Events.ON_CHANGE,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
							String configName = (String) event.getTarget().getAttribute("name");
							/*if (configName.equals("FIND_TAXI_TYPE")) {
								mapApp.put("FIND_TAXI_TYPE", ((Textbox) event.getTarget()).getValue());
							} else */
								
							if (configName.equals("API_KEY_ANDROID")) {
								mapApp.put("API_KEY_ANDROID", ((Textbox) event.getTarget()).getValue());
							} else if (configName.equals("API_KEY_IOS")) {
								mapApp.put("API_KEY_IOS", ((Textbox) event.getTarget()).getValue());
							} else if (configName.equals("NOTIFICATION_RADIUS")) {
								mapApp.put("NOTIFICATION_RADIUS", ((Textbox) event.getTarget()).getValue());
							}else if (configName.equals("MAX_DRIVER_TO_ASK")) {
								mapApp.put("MAX_DRIVER_TO_ASK", ((Textbox) event.getTarget()).getValue());
							}else if (configName.equals("MAX_DISTANCE")) {
								mapApp.put("MAX_DISTANCE", ((Textbox) event.getTarget()).getValue());
							}else if (configName.equals("WAIT_TIME_PER_DRIVER")) {
								mapApp.put("WAIT_TIME_PER_DRIVER", ((Textbox) event.getTarget()).getValue());
							}else if (configName.equals("TRIP_WAIT_TIME_OUT")) {
								mapApp.put("TRIP_WAIT_TIME_OUT", ((Textbox) event.getTarget()).getValue());
							}
							/*else if (configName.equals("SHOW_OPEN99")) {
								mapApp.put("SHOW_OPEN99", ((Textbox) event.getTarget()).getValue());
							}*/
						}
					});

			label2 = new Label();
			label2.setSclass("example_configemail");
			label2.setWidth("100%");
			label2.setClass("note_config");
			label2.setParent(vlayout);

			/*if (config.getName().equals("FIND_TAXI_TYPE")) {
				label.setValue("Cấu hình nhận cuốc");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			} else */
			if (config.getName().equals("API_KEY_ANDROID")) {
				label.setValue("API app Android");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			} else if (config.getName().equals("API_KEY_IOS")) {
				label.setValue("API app IOS");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			} else if (config.getName().equals("NOTIFICATION_RADIUS")) {
				label.setValue("Phạm vi nhận thông báo");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if (config.getName().equals("MAX_DRIVER_TO_ASK")) {
				label.setValue("Số tài xế nhận cuốc");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if (config.getName().equals("MAX_DISTANCE")) {
				label.setValue("Khoảng cách nhận cuốc");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if (config.getName().equals("WAIT_TIME_PER_DRIVER")) {
				label.setValue("Thời gian nhận cuốc");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if (config.getName().equals("TRIP_WAIT_TIME_OUT")) {
				label.setValue("Thời gian chuyển cuốc");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}
			/*else if (config.getName().equals("SHOW_OPEN99")) {
				label.setValue("Hiển thị xe OPEN99");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}*/
		}
		
		/*-- Create layout config find type --*/
		hlayout = new Hlayout();
		hlayout.setStyle("detail_config");
		hlayout.setParent(vlApp);
		div = new Div();
		div.setSclass("left_config_email");
		div.setParent(hlayout);
		
		label = new Label();
		label.setSclass("text_email_config");
		div.appendChild(label);
		vlayout = new Vlayout();
		vlayout.setSclass("right_config_email");
		vlayout.setZclass("none");
		vlayout.setParent(hlayout);
		createUIDispatcher(vlayout, configFindType);
		label2 = new Label();
		label2.setSclass("example_configemail");
		label2.setWidth("100%");
		label2.setClass("note_config");
		label2.setParent(vlayout);
		label.setValue("Cấu hình nhận cuốc");
		label2.setValue(configFindType.getNote());
		
		/*-- Create layout show car Open99 --*/
		hlayout = new Hlayout();
		hlayout.setStyle("detail_config");
		hlayout.setParent(vlApp);
		div = new Div();
		div.setSclass("left_config_email");
		div.setParent(hlayout);
		
		label = new Label();
		label.setSclass("text_email_config");
		div.appendChild(label);
		vlayout = new Vlayout();
		vlayout.setSclass("right_config_email");
		vlayout.setZclass("none");
		vlayout.setParent(hlayout);
		createUIOpen99(vlayout, configOpen99);
		label2 = new Label();
		label2.setSclass("example_configemail");
		label2.setWidth("100%");
		label2.setClass("note_config");
		label2.setParent(vlayout);
		label.setValue("Hiển thị xe Open99");
		label2.setValue(configOpen99.getNote());
		
		
		/*-- handle save data --*/
		Button btnSave = new Button("Cập nhật");
		btnSave.setStyle("margin-bottom:15px;");
		btnSave.setImage("/themes/images/save_16.png");
		btnSave.setParent(vlApp);
		btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
					saveToDB(lstApp, getListParams(lstNameApp, TYPE_APP),mapApp);
					Clients.showNotification("Đã cập nhật thông tin !","info", null, "middle_center", 3000, true);
			}
		});
	}
	
	/* -- Configuration system --*/
	
	private void initConfigsSystem(Component comp){
		lstSystem = getData(TYPE_SYSTEM);
		Vlayout vlApp = new Vlayout();// overflow: scroll;
		vlApp.setWidth("95%");
		vlApp.setStyle("overflow:scroll;max-height:500px;display:block;");
		vlApp.setParent(comp);
		
		/*-- danh sách cấu hình Email --*/
		Div div1 = new Div();
		div1.setSclass("div_title_list_config");
		div1.setParent(vlApp);
		Label labelEmail = new Label("Danh sách cấu hình hệ thống");
		labelEmail.setSclass("title_list_config");
		div1.appendChild(labelEmail);
		for (Config config : lstSystem) {
			Hlayout hlayout = new Hlayout();
			hlayout.setStyle("detail_config");
			hlayout.setParent(vlApp);
			Div div = new Div();
			div.setSclass("left_config_email");
			div.setParent(hlayout);
			Label label = new Label("Email");
			label.setSclass("text_email_config");
			div.appendChild(label);

			Vlayout vlayout = new Vlayout();
			vlayout.setSclass("right_config_email");
			vlayout.setZclass("none");
			vlayout.setParent(hlayout);
			Textbox textbox = new Textbox();
			textbox.setStyle("width:50%");
			textbox.setAttribute("name", config.getName());
			textbox.setParent(vlayout);
			textbox.addEventListener(Events.ON_CHANGE,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event event) throws Exception {
							String configName = (String) event.getTarget().getAttribute("name");
							if (configName.equals("EXTRA_TIME")) {
								mapSystem.put("EXTRA_TIME", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("EXTRA_KM")){
								mapSystem.put("EXTRA_KM", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("PRICE_PER_EXTRA_TIME")){
								mapSystem.put("PRICE_PER_EXTRA_TIME", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("PRICE_PER_EXTRA_KM")){
								mapSystem.put("PRICE_PER_EXTRA_KM", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("CALL_CENTER_URL")){
								mapSystem.put("CALL_CENTER_URL", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("TIMEOUT_CALLINWINDOW")){
								mapSystem.put("TIMEOUT_CALLINWINDOW", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("TIMEOUT_REFFRESH_DATA_DTV")){
								mapSystem.put("TIMEOUT_REFFRESH_DATA_DTV", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("CALLIN_TIMECHECK")){
								mapSystem.put("CALLIN_TIMECHECK", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("TIMEOUT_CHECKFAILCALLINSTART")){
								mapSystem.put("TIMEOUT_CHECKFAILCALLINSTART", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("MAX_EXTENSION_IN_TABLE")){
								mapSystem.put("MAX_EXTENSION_IN_TABLE", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("APIKEY_GOOGLE")){
								mapSystem.put("APIKEY_GOOGLE", ((Textbox) event.getTarget()).getValue());
							}else if(configName.equals("MAX_DISTANCE_VIEW")){
								mapSystem.put("MAX_DISTANCE_VIEW", ((Textbox) event.getTarget()).getValue());
							}
						}
					});

			Label label2 = new Label();
			label2.setSclass("example_configemail");
			label2.setWidth("100%");
			label2.setClass("note_config");
			label2.setParent(vlayout);

			if (config.getName().equals("EXTRA_TIME")) {
				label.setValue("Số thời gian chờ");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("EXTRA_KM")){
				label.setValue("Số km vượt");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("PRICE_PER_EXTRA_TIME")){
				label.setValue("Giá thời gian chờ");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("PRICE_PER_EXTRA_KM")){
				label.setValue("Giá km vượt");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("CALL_CENTER_URL")){
				label.setValue("Cấu hình tổng đài");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("TIMEOUT_CALLINWINDOW")){
				label.setValue("Thời gian chờ cuộc gọi đến");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("TIMEOUT_REFFRESH_DATA_DTV")){
				label.setValue("Thời gian cập nhật");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("CALLIN_TIMECHECK")){
				label.setValue("Thời gian");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("TIMEOUT_CHECKFAILCALLINSTART")){
				label.setValue("Thời gian");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("MAX_EXTENSION_IN_TABLE")){
				label.setValue("Số lượng max extentions");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("APIKEY_GOOGLE")){
				label.setValue("API google map");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}else if(config.getName().equals("MAX_DISTANCE_VIEW")){
				label.setValue("Khoảng cách");
				label2.setValue(config.getNote());
				textbox.setValue(config.getValue());
			}
		}
		/*-- handle save data --*/
		Button btnSave = new Button("Cập nhật");
		btnSave.setStyle("margin-bottom:15px;");
		btnSave.setImage("/themes/images/save_16.png");
		btnSave.setParent(vlApp);
		btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
					saveToDB(lstSystem, getListParams(lstNameSystem, TYPE_SYSTEM),mapSystem);
					Clients.showNotification("Đã cập nhật thông tin !","info", null, "middle_center", 3000, true);
			}
		});
	}
	
	public void createUISendSMSDriverRegister(Vlayout panelChildren, Config beanTemp) {
		List<BeanConfig> lstBeanConfig = new ArrayList<BeanConfig>();
		BeanConfig bean1 = new BeanConfig();
		bean1.setTitle("-- Gửi tin nhắn --");
		bean1.setValue("Y");
		lstBeanConfig.add(bean1);
		BeanConfig bean2 = new BeanConfig();
		bean2.setTitle("-- Không gửi tin nhắn --");
		bean2.setValue("N");
		lstBeanConfig.add(bean2);
		
		Combobox comb = new Combobox();
		comb.setReadonly(true);
		comb.setStyle("line-height: 24px !important;font-size: 12px !important;padding:3px;");
		comb.setItemRenderer(new ComboitemRenderer<BeanConfig>() {
			@Override
			public void render(Comboitem paramComboitem, BeanConfig bean,int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				if(StringUtils.equals(beanTemp.getValue(), bean.getValue())){
					comb.setSelectedItem(paramComboitem);
				}
			}
			
		});
		comb.setModel(new ListModelList<BeanConfig>(lstBeanConfig));
		comb.setWidth("200px");
		comb.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				mapSMS.put("SMS_NOTIFY_DRIVER_REGISTER", comb.getSelectedItem().getValue());
			};
		});
		panelChildren.appendChild(comb);
	}
	
	public void createUIDispatcher(Vlayout panelChildren, Config beanTemp) {
		List<BeanConfig> lstBeanConfig = new ArrayList<BeanConfig>();
		BeanConfig bean1 = new BeanConfig();
		bean1.setTitle("-- Unicast --");
		bean1.setValue("U");
		lstBeanConfig.add(bean1);
		BeanConfig bean2 = new BeanConfig();
		bean2.setTitle("-- Multicast --");
		bean2.setValue("M");
		lstBeanConfig.add(bean2);
		
		Combobox comb = new Combobox();
		comb.setReadonly(true);
		comb.setStyle("line-height: 24px !important;font-size: 12px !important;padding:3px;");
		comb.setItemRenderer(new ComboitemRenderer<BeanConfig>() {
			@Override
			public void render(Comboitem paramComboitem, BeanConfig bean,int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				if(StringUtils.equals(beanTemp.getValue(), bean.getValue())){
					comb.setSelectedItem(paramComboitem);
				}
			}
			
		});
		comb.setModel(new ListModelList<BeanConfig>(lstBeanConfig));
		comb.setWidth("200px");
		comb.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				mapApp.put("FIND_TAXI_TYPE", comb.getSelectedItem().getValue());
			};
		});
		panelChildren.appendChild(comb);
	}
	
	public void createUIOpen99(Vlayout panelChildren, Config beanTemp) {
		List<BeanConfig> lstBeanConfig = new ArrayList<BeanConfig>();
		BeanConfig bean1 = new BeanConfig();
		bean1.setTitle("-- Hiển thị --");
		bean1.setValue("1");
		lstBeanConfig.add(bean1);
		BeanConfig bean2 = new BeanConfig();
		bean2.setTitle("-- Không hiển thị --");
		bean2.setValue("0");
		lstBeanConfig.add(bean2);
		
		Combobox comb = new Combobox();
		comb.setReadonly(true);
		comb.setStyle("line-height: 24px !important;font-size: 12px !important;padding:3px;");
		comb.setItemRenderer(new ComboitemRenderer<BeanConfig>() {
			@Override
			public void render(Comboitem paramComboitem, BeanConfig bean,int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				if(StringUtils.equals(beanTemp.getValue(), bean.getValue())){
					comb.setSelectedItem(paramComboitem);
				}
			}
			
		});
		comb.setModel(new ListModelList<BeanConfig>(lstBeanConfig));
		comb.setWidth("200px");
		comb.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				mapApp.put("SHOW_OPEN99", comb.getSelectedItem().getValue());
			};
		});
		panelChildren.appendChild(comb);
	}
	
	class BeanConfig{
		private String title;
		private String value;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		
	}

	private boolean checkEmail() {
		boolean check = true;
		if(mapEmail.get("EMAIL_SUPPORT") != null && !StringUtils.isValidEmail(mapEmail.get("EMAIL_SUPPORT"))){
			check = false;
		}
		return check;
	}
	
	private void saveToDB(List<Config> listCfg, List<String> lstName, Map<String, String> mapValue){
		for (String str : lstName) {
			for (Config config : listCfg) {
				if(mapValue.get(config.getName()) != null && StringUtils.equals(str, config.getName()) && !StringUtils.equals(mapValue.get(config.getName()), config.getValue())){
					config.setValue(mapValue.get(config.getName()));
					config.save();
					/*-- reload map configuration --*/
					ConfigUtil.reloadMap(config.getName(), mapValue.get(config.getName()));
					break;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Config> getData(int type){
		List<Config> lstConfig = new ArrayList<Config>();
		try {
			org.hibernate.Session session = ControllerUtils.getCurrentSession();
			List<String> lstParams = new ArrayList<String>();
			String sql = "from Config where name in (:IdList)";
			lstParams = getListParams(lstParams, type);
			lstConfig = session.createQuery(sql).setParameterList("IdList", lstParams).list();
			session.close();
		} catch (Exception e) {
		}
		return lstConfig;
	}
	
	private List<String> getListParams(List<String> lstParams, int type){
		if(type == TYPE_EMAIL){ // Email Configuration
			lstParams.add("EMAIL_SUPPORT");
			lstParams.add("EMAIL_SUPPORT_PASS");
			lstParams.add("EMAIL_SERVER_HOST");
			lstParams.add("EMAIL_SERVER_PORT");
		}else if(type == TYPE_SMS){ // SMS 
			lstParams.add("SMS_USERNAME");
			lstParams.add("SMS_PASSWORD");
			lstParams.add("SMS_BRANDNAME");
			lstParams.add("SMS_NOTIFY_DRIVER_REGISTER");
		}else if(type == 3){ // Application mobile
			lstParams.add("FIND_TAXI_TYPE");
//			lstParams.add("API_KEY_ANDROID");
//			lstParams.add("API_KEY_IOS");
			lstParams.add("NOTIFICATION_RADIUS");
			lstParams.add("MAX_DRIVER_TO_ASK");
			lstParams.add("MAX_DISTANCE");
			lstParams.add("WAIT_TIME_PER_DRIVER");
			lstParams.add("TRIP_WAIT_TIME_OUT");
			lstParams.add("SHOW_OPEN99");
		}else if(type == TYPE_SYSTEM){ // Systems
			lstParams.add("EXTRA_TIME");
			lstParams.add("EXTRA_KM");
			lstParams.add("PRICE_PER_EXTRA_TIME");
			lstParams.add("PRICE_PER_EXTRA_KM");
//			lstParams.add("CALL_CENTER_URL");
			lstParams.add("TIMEOUT_CALLINWINDOW");
			lstParams.add("TIMEOUT_REFFRESH_DATA_DTV");
			lstParams.add("CALLIN_TIMECHECK");
			lstParams.add("TIMEOUT_CHECKFAILCALLINSTART");
			lstParams.add("MAX_EXTENSION_IN_TABLE");
//			lstParams.add("APIKEY_GOOGLE");
			lstParams.add("MAX_DISTANCE_VIEW");
		}
		return lstParams;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
