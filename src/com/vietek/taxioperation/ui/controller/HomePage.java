package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Nav;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import com.vietek.taxioperation.chat.ChatWindow;
import com.vietek.taxioperation.common.EnumLevelUser;
import com.vietek.taxioperation.common.EnumUserAction;
import com.vietek.taxioperation.common.ListCommon;
import com.vietek.taxioperation.controller.SysFunctionController;
import com.vietek.taxioperation.controller.SysUserController;
import com.vietek.taxioperation.dashboard.TMSDateTime;
import com.vietek.taxioperation.googlemapSearch.EventUserCallIn;
import com.vietek.taxioperation.model.SysFunction;
import com.vietek.taxioperation.model.SysGroup;
import com.vietek.taxioperation.model.SysGroupLine;
import com.vietek.taxioperation.model.SysMenu;
import com.vietek.taxioperation.model.SysRule;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.mq.LogInMQ;
import com.vietek.taxioperation.services.AuthenticationService;
import com.vietek.taxioperation.util.CheckOnlineUtils;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.ExecutorCallIn;
import com.vietek.taxioperation.util.SaveLogToQueue;
import com.vietek.taxioperation.webservice.CallInWS;

public class HomePage extends SelectorComposer<Component> implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4190744877618873422L;

	private int unknow = 0;
	@Wire
	Div hl_header;
	@Wire
	Div div_mnu_collapse;
	@Wire
	Navbar nb_menu;
	@Wire
	Tabbox tbMain;
	@Wire
	Menuitem mnu_account_info;
	@Wire
	Menuitem mnu_logout;
	@Wire
	Button bt_menu;
	@Wire
	Button btn_user_menu;
	@Wire
	Div divTab;
	@WireVariable
	private Desktop desktop;
	private TaxiOrdersForm taxiOrderWindow = null;
	private static HomePage instance = null;
	private Menubar menubar;

	SysUserController sysUserCtrl;
	SysUser sysUser = null;

	EventUserCallIn eventUserCallIn = new EventUserCallIn();
	List<SysMenu> lstMenuPermission = new ArrayList<>();

	static ExecutorCallIn threadPoolCallIn;
	static ExecutorCallIn threadPoolCallInHangup;

	public static HomePage getInstance() {
		return instance;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Env.setHomePage(this);
		init();
		comp.addEventListener(Events.ON_CLIENT_INFO, this);
		bt_menu.addEventListener(Events.ON_CLICK, this);
		this.afterInit();
		hl_header.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				div_mnu_collapse.setVisible(false);
			}
		});
		divTab.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				div_mnu_collapse.setVisible(false);
			}
		});
		Clients.evalJavaScript("initNavbar()");
	}

	private void init() {
		sysUser = Env.getUser();

		if (sysUser == null) {
			Executions.sendRedirect("/");
			return;
		}
		if (CallInWS.webApp == null)
			CallInWS.webApp = desktop.getWebApp();

		if (CheckOnlineUtils.webApp == null)
			CheckOnlineUtils.webApp = desktop.getWebApp();

		btn_user_menu.setLabel(sysUser.getName());
		initNavBar();
		instance = this;

		LogInMQ.subcrible(this);

		/*
		 * sonvh
		 */

		if (threadPoolCallIn == null || threadPoolCallInHangup == null) {

			threadPoolCallIn = new ExecutorCallIn();
			threadPoolCallInHangup = new ExecutorCallIn();

			threadPoolCallIn.setThreads(5);
			threadPoolCallInHangup.setThreads(5);
		}

		/*
		 * Habv hien thi ngay gio
		 */
		TMSDateTime dateTime = new TMSDateTime();
		dateTime.setParent(divTab);
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	private void initNavBar() {
		// SysMenuController controller = (SysMenuController)
		// ControllerUtils.getController(SysMenuController.class);
		// List<SysMenu> lstModel = controller.find("from SysMenu where isActive
		// = true order by sequence");
		this.setRulePermission(sysUser);
		List<SysMenu> lstModel = new ArrayList<>();
		lstModel.addAll(ListCommon.LIST_SYS_MENU);
		List<SysMenu> lstTreeMenu = new ArrayList<>();
		if (sysUser.getName().equalsIgnoreCase(EnumLevelUser.ADMIN.toString())
				|| sysUser.getName().equalsIgnoreCase(EnumLevelUser.SUPER_USER.toString())) {
			lstTreeMenu = lstModel;
		} else {
			this.lstMenuPermission = this.getMenu4Permission();
			lstTreeMenu = this.filterMenu(lstModel, this.lstMenuPermission);
			this.sortMenu(lstTreeMenu);
		}

		List<SysMenu> lstRoot = new ArrayList<SysMenu>();
		for (SysMenu sysMenu : lstTreeMenu) {
			if (sysMenu.getParentId() == 0) {
				lstRoot.add(sysMenu);
			}
		}

		for (SysMenu sysMenu : lstRoot) {
			if (sysMenu.getIsActive()) {
				this.createChildNabar(lstTreeMenu, null, sysMenu);
			}
		}
		initMenuCollapse(lstTreeMenu, lstRoot);
	}

	private void initMenuCollapse(List<SysMenu> lstModel, List<SysMenu> lstRoot) {
		menubar = new Menubar();
		menubar.setId("menubar");
		menubar.setOrient("vertical");
		menubar.setAutodrop(true);
		menubar.setParent(div_mnu_collapse);
		for (SysMenu sysMenu : lstRoot) {
			if (sysMenu.getIsActive()) {
				this.createChildMenu(lstModel, null, sysMenu);
			}
		}
	}

	private void createChildMenu(List<SysMenu> lstModel, Menupopup parent, SysMenu sysMenu) {
		List<SysMenu> lstChild = new ArrayList<SysMenu>();
		for (SysMenu child : lstModel) {
			if (child.getParentId() != 0 && child.getParentId() == sysMenu.getId()) {
				lstChild.add(child);
			}
		}

		if (lstChild.isEmpty()) {
			Menuitem item = new Menuitem();
			item.setLabel(sysMenu.getName());
			item.setAttribute("function", sysMenu.getFunction());
			item.addEventListener(Events.ON_CLICK, this);
			if (parent == null) {
				item.setParent(menubar);
			} else {
				item.setStyle(" font-style: italic");
				item.setParent(parent);
			}
		} else {
			Menu parentTmp = new Menu();
			parentTmp.setLabel(sysMenu.getName());
			parentTmp.setAttribute("function", sysMenu.getFunction());
			parentTmp.addEventListener(Events.ON_CLICK, this);
			if (parent == null) {
				parentTmp.setParent(menubar);
			} else {
				parentTmp.setParent(parent);
			}

			Menupopup mnpopup = new Menupopup();
			mnpopup.setParent(parentTmp);
			for (SysMenu child : lstChild) {
				this.createChildMenu(lstModel, mnpopup, child);
			}
		}
	}

	private void sortMenu(List<SysMenu> lstModel) {
		Collections.sort(lstModel, new Comparator<SysMenu>() {
			@Override
			public int compare(SysMenu o1, SysMenu o2) {
				if (o1.getSequence() > o2.getSequence()) {
					return 1;
				} else if (o1.getSequence() == o2.getSequence()) {
					if (o1.getName().compareTo(o2.getName()) == 1) {
						return 1;
					} else {
						return -1;
					}
				} else {
					return -1;
				}
			}
		});

	}

	@Override
	public void onEvent(Event event) throws Exception {

		if (event.getTarget() instanceof Menuitem) {
			if (event.getName().equals(Events.ON_CLICK)) {
				SysFunction function = (SysFunction) event.getTarget().getAttribute("function");
				if (function != null) {
					this.showSysFunction(function);
					div_mnu_collapse.setVisible(false);
				}
			}
		} else if (event.getTarget() instanceof Navitem) {
			if (event.getName().equals(Events.ON_CLICK)) {
				SysFunction function = (SysFunction) event.getTarget().getAttribute("function");
				if (function != null) {
					this.showSysFunction(function);
				}
			}
		} else if (event.getTarget() == bt_menu) {
			div_mnu_collapse.setVisible(!div_mnu_collapse.isVisible());
		} else if (event.getName().equals(LogInMQ.LOG_IN_EVENT)) {
			if (!Env.isLogged() || !Env.getHomePage().getDesktop().isAlive() || this.getPage() == null) {
				LogInMQ.unsubcrible(this);
				return;
			}
			SysUser user = (SysUser) event.getData();
			if (Env.getUser() != null) {
				if (user.equals(Env.getUser()) && !user.getName().equals("admin")) {
					AuthenticationService authService = new AuthenticationService();
					authService.logout();
					Executions.sendRedirect("/");
				}
			}
		}
	}

	private EventListener<Event> EVENT_CLOSETAB = new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {

			if (event.getTarget() != null) {
				Tab tab = (Tab) event.getTarget();
				if (tab.getAttribute("function") != null) {
					SysFunction funtion = (SysFunction) tab.getAttribute("function");
					if (funtion != null) {
						List<SysFunction> lstFunction = Env.getListCurrentFunction();
						if (lstFunction.contains(funtion)) {
							lstFunction.remove(funtion);
						}

					}
				}
			}
		}
	};

	private EventListener<Event> EVENT_SELECTTAB = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			if (event.getTarget() != null) {
				Tab tab = (Tab) event.getTarget();
				if (tab.getAttribute("window") != null) {
					AbstractComponent component = (AbstractComponent) tab.getAttribute("window");
					if (component != null) {
						if (component instanceof TaxiOrdersDD) {
							((TaxiOrdersDD) component).focusRowOfListbox();
						}
					}
				}
			}
		}
	};

	private void showSysFunction(SysFunction function) {

		// nb_menu.clearSelection();
		boolean isNew = true;
		List<Tab> lstTabs = tbMain.getTabs().getChildren();
		for (Tab tab : lstTabs) {
			if (tab.getValue() instanceof SysFunction) {
				SysFunction fucTmp = tab.getValue();
				if (function.getClazz() != null && function.getClazz().trim().length() > 0) {
					if (fucTmp.getClazz() != null && fucTmp.getClazz().trim().length() > 0) {
						if (fucTmp.getClazz().equals(function.getClazz())) {
							tbMain.setSelectedTab(tab);
							// nb_menu.clearSelection();
							isNew = false;
							break;
						}
					}
				} else if (function.getZulFile() != null && function.getZulFile().trim().length() > 0) {
					if (fucTmp.getZulFile() != null && fucTmp.getZulFile().trim().length() > 0) {
						if (fucTmp.getZulFile().equals(function.getZulFile())) {
							tbMain.setSelectedTab(tab);
							// nb_menu.clearSelection();
							isNew = false;
							break;
						}
					}
				}
			}
		}

		SaveLogToQueue savelog = new SaveLogToQueue(Env.getHomePage().getCurrentFunction(), EnumUserAction.VIEWING, 
				Env.getHomePage().getCurrentFunction(), Env.getUserID());
		savelog.start();
		
		if (isNew) {
			Tab tab = new Tab();
			tab.setAttribute("function", function);
			if (function.getClazz() != null && function.getClazz().trim().length() > 0) {
				try {
					AbstractComponent component = (AbstractComponent) Class
							.forName("com.vietek.taxioperation.ui.controller." + function.getClazz()).newInstance();
					tab.setAttribute("window", component);
					tab.setLabel(function.getName());
					tab.setValue(function);
					if (function.getClazz().equals("TaxiOrdersForm") || function.getClazz().equals("TaxiOrdersDD")
							|| function.getClazz().equals("TaxiOrdersDDMobile")) {
						tab.setClosable(false);
					} else {
						tab.setClosable(true);
					}
					tab.setSelected(true);
					tbMain.getTabs().appendChild(tab);
					Tabpanel tabPanel = new Tabpanel();
					tabPanel.appendChild(component);
					tbMain.getTabpanels().appendChild(tabPanel);
					List<SysFunction> lstFunction = Env.getListCurrentFunction();
					if (!lstFunction.contains(function)) {
						lstFunction.add(function);
					}
					if (function.getClazz().equals("TaxiOrdersForm")) {
						taxiOrderWindow = (TaxiOrdersForm) component;
						Env.setTaxiOrdersWindow(taxiOrderWindow);

						/*
						 * sonVH set is open tab for callInUser
						 */
						Env.setIsOpen_Tab_DTV(true);
					}
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else if (function.getZulFile() != null && function.getZulFile().trim().length() > 0) {
				Include inc = null;
				inc = new Include(function.getZulFile());
				inc.setAttribute("function", function);
				tab.setLabel(function.getName());
				tab.setValue(function);
				tab.setClosable(true);
				tab.setSelected(true);
				tbMain.getTabs().appendChild(tab);
				Tabpanel tabPanel = new Tabpanel();
				tabPanel.appendChild(inc);
				tbMain.getTabpanels().appendChild(tabPanel);
				List<SysFunction> lstFunction = Env.getListCurrentFunction();
				if (!lstFunction.contains(function)) {
					lstFunction.add(function);
				}
			}

			tab.addEventListener(Events.ON_CLOSE, EVENT_CLOSETAB);
			tab.addEventListener(Events.ON_SELECT, EVENT_SELECTTAB);
		}
	}

	private void createChildNabar(List<SysMenu> lstModel, Nav parent, SysMenu sysMenu) {
		List<SysMenu> lstChild = new ArrayList<SysMenu>();
		for (SysMenu child : lstModel) {
			if (child.getParentId() != 0 && child.getParentId() == sysMenu.getId()) {
				lstChild.add(child);
			}
		}
		if (lstChild.isEmpty()) {
			Navitem item = new Navitem();
			item.setLabel(sysMenu.getName());
			item.setAttribute("function", sysMenu.getFunction());
			item.addEventListener(Events.ON_CLICK, this);
			if (parent == null) {
				item.setId("nb_item" + unknow);
				unknow++;
				item.setParent(nb_menu);
			} else {

				item.setStyle(" font-style: italic");
				item.setParent(parent);
			}
		} else {
			Nav parentTmp = new Nav();
			parentTmp.setLabel(sysMenu.getName());
			parentTmp.setAttribute("function", sysMenu.getFunction());
			parentTmp.addEventListener(Events.ON_CLICK, this);
			if (parent == null) {
				parentTmp.setId("nb_item" + unknow);
				unknow++;
				parentTmp.setParent(nb_menu);
			} else {
				parentTmp.setParent(parent);
			}
			for (SysMenu child : lstChild) {
				this.createChildNabar(lstModel, parentTmp, child);
			}
		}
	}

	public void showNotification(String msg, String type) {
		Clients.showNotification(msg, type, null, "bottom_right", 3000);
	}

	public void showNotificationErrorSelect(String msg, String type) {
		Clients.showNotification(msg, type, null, "middle_center", 3000);
	}

	public void showValidateForm(String msg, String type) {
		Clients.showNotification(msg, type, null, "middle_center", 30000, true);
	}

	public TaxiOrdersForm getTaxiOrderWindow() {
		return taxiOrderWindow;
	}

	public void setTaxiOrderWindow(TaxiOrdersForm taxiOrderWindow) {
		this.taxiOrderWindow = taxiOrderWindow;
	}

	public Tabbox getTbMain() {
		return tbMain;
	}

	/**
	 * 
	 *
	 * @author VuD
	 * @param isClass
	 * @param zulFile
	 * @return Function co Class hoac ZulFile truyen vao
	 */
	private SysFunction getFunction(boolean isClass, String zulFile) {
		List<SysFunction> lstFunctions = null;
		SysFunctionController controller = (SysFunctionController) ControllerUtils
				.getController(SysFunctionController.class);
		if (isClass) {
			lstFunctions = controller.find(" from SysFunction where clazz = ?", zulFile);
		} else {
			lstFunctions = controller.find(" from SysFunction where zulFile = ?", zulFile);
		}
		if (lstFunctions != null && !lstFunctions.isEmpty()) {
			return lstFunctions.get(0);
		} else {
			return null;
		}
	}
	//
	// @Listen("addNewTaxiOrder")
	// private void eventcall(Call call) {
	// if (taxiOrderWindow != null)
	// taxiOrderWindow.callInShowPopup(call);
	// }

	@Listen("onClick=#mnu_account_info")
	public void showAccountInfo() {
		List<Tab> lstTabs = tbMain.getTabs().getChildren();
		boolean isNew = true;
		for (Tab tab : lstTabs) {
			SysFunction function = tab.getValue();
			if (function != null) {
				if (function.getClazz().equals("/zul/account_info.zul")) {
					tbMain.setSelectedTab(tab);
					isNew = false;
					break;
				}
			}
		}
		if (isNew) {
			SysFunction func = this.getFunction(false, "/zul/account_info.zul");
			if (func == null) {
				this.showNotification("Không có chức năng này", Clients.NOTIFICATION_TYPE_INFO);
			} else {
				Include inc = null;
				inc = new Include(func.getZulFile());
				inc.setAttribute("function", func);
				Tab tab = new Tab();
				tab.setLabel(func.getName());
				tab.setValue(func);
				tab.setClosable(true);
				tab.setSelected(true);
				tbMain.getTabs().appendChild(tab);
				Tabpanel tabPanel = new Tabpanel();
				tabPanel.appendChild(inc);
				tbMain.getTabpanels().appendChild(tabPanel);
				List<SysFunction> lstFunction = Env.getListCurrentFunction();
				if (!lstFunction.contains(func)) {
					lstFunction.add(func);
				}

			}
		}
	}

	@Listen("onClick=#mnu_logout")
	public void logout() {
		AuthenticationService authService = new AuthenticationService();
		CheckOnlineUtils.logout(sysUser.getUserName());
		ChatWindow.sendNotif(sysUser, false);
		authService.logout();
		Executions.sendRedirect("/");
	}

	private List<SysMenu> getMenu4Permission() {
		List<SysMenu> lstMenu = new ArrayList<SysMenu>();
		// SysUser sysUser = sysUserCtrl.get(SysUser.class, Env.getUserID());
		Set<SysGroup> setGroup = sysUser.getSysGroup();
		for (SysGroup sysGroup : setGroup) {
			if (sysGroup.getIsActive()) {
				Set<SysGroupLine> setGroupLine = sysGroup.getSysGroupLines();
				for (SysGroupLine sysGroupLine : setGroupLine) {
					SysFunction sysFunction = sysGroupLine.getSysFunction();
					Set<SysMenu> setSysMenu = sysFunction.getSysMenu();
					for (SysMenu sysMenu : setSysMenu) {
						if (sysMenu.getIsActive()) {
							lstMenu.add(sysMenu);
						}
					}
				}
			}
		}
		return lstMenu;
	}

	protected List<SysFunction> getFunction4Permission() {
		List<SysFunction> lstFunction = new ArrayList<SysFunction>();
		// SysUser sysUser = sysUserCtrl.get(SysUser.class, Env.getUserID());
		Set<SysGroup> setGroup = sysUser.getSysGroup();
		for (SysGroup sysGroup : setGroup) {
			if (sysGroup.getIsActive()) {
				Set<SysGroupLine> setGroupLine = sysGroup.getSysGroupLines();
				for (SysGroupLine sysGroupLine : setGroupLine) {
					SysFunction sysFunction = sysGroupLine.getSysFunction();
					lstFunction.add(sysFunction);
				}
			}
		}
		return lstFunction;
	}

	private List<SysMenu> filterMenu(List<SysMenu> lstModel, List<SysMenu> lstMenu4Permission) {
		List<SysMenu> lstResult = new ArrayList<SysMenu>();
		List<SysMenu> lstTmp = new ArrayList<SysMenu>();
		for (SysMenu sysMenu : lstMenu4Permission) {
			lstTmp.add(sysMenu);
			this.addParentMenu(sysMenu, lstTmp, lstModel);
		}
		List<Integer> lstId = new ArrayList<Integer>();
		for (SysMenu sysMenu : lstTmp) {
			if (!lstId.contains(sysMenu.getId())) {
				lstResult.add(sysMenu);
				lstId.add(sysMenu.getId());
			}
		}
		return lstResult;
	}

	private void addParentMenu(SysMenu sysMenu, List<SysMenu> lstResult, List<SysMenu> lstModel) {
		for (SysMenu sysMenuTmp : lstModel) {
			if (sysMenuTmp.getId() == sysMenu.getParentId()) {
				if (!lstResult.contains(sysMenuTmp)) {
					lstResult.add(sysMenuTmp);
				}
				this.addParentMenu(sysMenuTmp, lstResult, lstModel);
				break;
			}
		}
	}

	public Desktop getDesktop() {
		return desktop;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
	}

	private void afterInit() {
		this.loadOldMenu();
	}

	private void loadOldMenu() {
		List<SysFunction> lstFunction = Env.getListCurrentFunction();
		// List<SysFunction> lstFuntionPermission =
		// this.getFunction4Permission();

		List<SysFunction> lstFuntionDel = new ArrayList<SysFunction>();
		if ((!sysUser.getName().equals("admin") && !sysUser.getName().equals("SuperUser"))) {
			for (SysFunction sysFunction : lstFunction) {
				if (this.isContainPermission(sysFunction)) {
					showSysFunction(sysFunction);
				} else {
					lstFuntionDel.add(sysFunction);
				}
			}

			boolean isOpenDT = false;
			boolean isOpenDD = false;
			for (SysMenu sysMenu : lstMenuPermission) {
				if (!isOpenDD || !isOpenDT) {
					if (sysMenu.getFunction() != null) {
						String clazzFunction = sysMenu.getFunction().getClazz();
						if (clazzFunction != null) {
							if (clazzFunction.equals("TaxiOrdersForm")) {
								this.showSysFunction(sysMenu.getFunction());
								isOpenDT = true;
								/*
								 * sonVH
								 */
								Env.setIsOpen_Tab_DTV(true);

							} else if (sysMenu.getFunction().getClazz().equals("TaxiOrdersDD")) {
								this.showSysFunction(sysMenu.getFunction());
								isOpenDD = true;
							}
						}
					}
				}

			}
		} else {
			for (SysFunction sysFunction : lstFunction) {
				showSysFunction(sysFunction);
			}
		}
		for (SysFunction sysFunction : lstFuntionDel) {
			lstFunction.remove(sysFunction);
		}
	}

	private boolean isContainPermission(SysFunction function) {
		boolean result = false;
		if (function != null) {
			for (SysMenu sysMenu : lstMenuPermission) {
				if (sysMenu.getFunction() != null) {
					if (sysMenu.getFunction().getId() == function.getId()) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Loc ra danh sach phan quyen du lieu
	 * 
	 * @param sysUser
	 */
	private void setRulePermission(SysUser sysUser) {
		Map<String, SysRule> mapRule = new HashMap<>();
		for (SysGroup sysGroup : sysUser.getSysGroup()) {
			for (SysRule sysRule : sysGroup.getSetSysRule()) {
				if (sysRule.getIsActive()) {
					SysRule ruleTmp = mapRule.putIfAbsent(sysRule.getModelName(), sysRule);
					if (ruleTmp != null) {
						if (ruleTmp.getPriority() < sysRule.getPriority()) {
							mapRule.put(sysRule.getModelName(), sysRule);
						}
					}
				}
			}
		}
		for (SysRule sysRule : sysUser.getSetSysRule()) {
			if (sysRule.getIsActive()) {
				SysRule ruleTmp = mapRule.putIfAbsent(sysRule.getModelName(), sysRule);
				if (ruleTmp != null) {
					if (ruleTmp.getPriority() < sysRule.getPriority()) {
						mapRule.put(sysRule.getModelName(), sysRule);
					}
				}
			}
		}

		Iterator<String> ite = mapRule.keySet().iterator();
		while (ite.hasNext()) {
			String key = ite.next();
			SysRule rule = mapRule.get(key);
			String hql = rule.getHql();
			Env.getMapUserRule().put(key, hql);
		}

	}

	public Div getDivTab() {
		return divTab;
	}

	public void showFunction(String clazzName) {
		SysFunctionController controller = (SysFunctionController) ControllerUtils
				.getController(SysFunctionController.class);
		List<SysFunction> lstValue = controller.find("from SysFunction where clazz = ?", clazzName);
		if (lstValue != null && lstValue.size() > 0) {
			SysFunction sysFunction = lstValue.get(0);
			this.showSysFunction(sysFunction);
		}
	}

	public SysFunction getCurrentFunction() {
		SysFunction result = null;
		try {
			Tab tabpanel = tbMain.getSelectedTab();
			result = (SysFunction) tabpanel.getAttribute("function");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
