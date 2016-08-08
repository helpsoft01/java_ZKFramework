package com.vietek.taxioperation.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApp;

import com.vietek.taxioperation.controller.TelephoneTableTmsController;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.SysFunction;
import com.vietek.taxioperation.model.SysGroup;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TelephoneExtensionTms;
import com.vietek.taxioperation.model.TelephoneTableTms;
import com.vietek.taxioperation.model.VoipCenter;
import com.vietek.taxioperation.ui.controller.HomePage;
import com.vietek.taxioperation.ui.controller.TaxiOrdersForm;

public class Env {

	public static String PROPERTIES_ATT = "properties_att";
	public static final String USER_OBJIECT = "user_Object";
	public static final String ISOPEN_TAB_TDV = "ISOPEN_TAB_TDV";
	public static final String COMPANY_ID = "$COMPANY_ID$";
	public static final String COMPANY_OBJECT = "$COMPANY_OBJECT$";
	public static final String COMPANY_VALUE = "$COMPANY_VALUE$";
	public static final String LIST_COMPANY = "$LIST_COMPANY$";
	public static final String LIST_COMPANY_ID = "$LIST_COMPANY_ID$";
	public static final String LIST_VEHICLE = "$LIST_VEHICLE$";
	public static final String USER_RULE = "USER_RULE";
	public static String USER_ID = "user_id";
	public static String USER_FULL_NAME = "user_full_name";
	public static String MAIN_APP = "main_app";
	public static String SERVER_PUSH_EVENT_EXTENSION_UN_REGISTED = "server_push_event_extension_un_registed";
	public static String SERVER_PUSH_EVENT_EXTENSION_REGISTED = "server_push_event_extensioin_registed";
	public static String EXT_NUM = "ext_number";
	public static String HOME_PAGE = "home_page";
	public static String USER_NAME = "user_name";
	public static String LST_CURRENT_FUCTION = "cur_function";
	public static String TAXI_ORDER_WINDOW = "taxi_order_window";
	public static String IS_LOGGED = "is_logged";

	public static WebApp WEBAPP = null;

	@SuppressWarnings("unchecked")
	public static Hashtable<String, Object> getCtx() {
		Hashtable<String, Object> properties = null;
		Session session = Sessions.getCurrent();
		if (session != null) {
			properties = (Hashtable<String, Object>) session.getAttribute(PROPERTIES_ATT);
			if (properties == null) {
				properties = new Hashtable<String, Object>();
				session.setAttribute(PROPERTIES_ATT, properties);
			}
		}
		return properties;
	}

	public static boolean isOnline(Session session) {
		Properties properties;
		properties = (Properties) session.getAttribute(PROPERTIES_ATT);
		if (properties != null) {
			String obj = properties.getProperty(USER_ID);
			if (obj != null) {
				Integer id = Integer.parseInt(obj);
				return id > 0;
			}
		}
		return false;
	}

	public static void setContext(String key, Object value) {
		if (getCtx() != null) {
			if (value != null) {
				getCtx().put(key, value);
				((HttpSession) Sessions.getCurrent().getNativeSession()).setAttribute(key, value + "");
			}
		}
	}

	public static Object getContext(String key) {
		if (getCtx() != null)
			return getCtx().get(key);
		else
			return null;
	}

	public static int getUserID() {
		if (getContext(USER_ID) == null)
			setContext(USER_ID, "-1");
		return Integer.parseInt((String) getCtx().get(USER_ID));
	}

	public static String getUserName() {
		return USER_NAME;
	}

	public static Locale getLocale() {
		if (Env.getContext("language") == null) {
			Env.setContext("language", "vi");
		}
		Locale locale = new Locale((String) Env.getContext("language"));
		return locale;
	}

	public void setLocale(Locale locale) {
		Env.setContext("language", locale.getLanguage());
	}

	public static void setHomePage(HomePage homepage) {
		Env.setContext(HOME_PAGE, homepage);
		if (WEBAPP == null) {
			WEBAPP = homepage.getDesktop().getWebApp();
		}
	}
	
	public static HomePage getHomePage() {
		return (HomePage) Env.getContext(HOME_PAGE);
	}
	
	public static Desktop getDesktop() {
		return getHomePage().getDesktop();
	}

	public static void setIsOpen_Tab_DTV(boolean isOpen) {
		Env.setContext(ISOPEN_TAB_TDV, isOpen);
	}

	public static boolean getIsOpen_Tab_DTV() {
		if (Sessions.getCurrent() != null)
			return (boolean) Env.getContext(ISOPEN_TAB_TDV);
		return false;
	}

	public static boolean isLogged() {
		boolean result = false;
		if (Sessions.getCurrent() != null) {
			if (Env.getContext(IS_LOGGED) != null) {
				result = (boolean) Env.getContext(IS_LOGGED);
			}
		}
		return result;
	}
	
	public static boolean isLogged(Session session) {
		boolean result = false;
		if (session != null) {
			if (session.getAttribute(Env.PROPERTIES_ATT) != null) {
				result = true;
			}
		}
		return result;
	}

	public static void setIsLogged(boolean isLogged) {
		Env.setContext(IS_LOGGED, isLogged);
	}

	public static void setUser(SysUser user) {
		Env.setContext(USER_OBJIECT, user);
	}

	public static SysUser getUser() {
//		if (Sessions.getCurrent() != null)
//			
//		return null;
		return (SysUser) Env.getContext(USER_OBJIECT);
	}

	public static List<SysFunction> getListCurrentFunction() {
		@SuppressWarnings("unchecked")
		List<SysFunction> lstFunction = (List<SysFunction>) Env.getContext(LST_CURRENT_FUCTION);
		if (lstFunction == null) {
			lstFunction = new ArrayList<SysFunction>();
			Env.setContext(LST_CURRENT_FUCTION, lstFunction);
		}
		return lstFunction;
	}

	public static void setTaxiOrdersWindow(TaxiOrdersForm taxiOrder) {
		Env.setContext(TAXI_ORDER_WINDOW, taxiOrder);
	}

	public static TaxiOrdersForm getTaxiOrdersWindow() {
		return (TaxiOrdersForm) Env.getContext(TAXI_ORDER_WINDOW);
	}

	public static Boolean getIsDTV(SysUser user) {
		boolean isDTV = false;
		Set<SysGroup> groups = user.getSysGroup();
		for (SysGroup group : groups) {
			if (group.getId() == 1) {
				isDTV = true;
				break;
			}
		}
		return isDTV;
	}

	public static String getCompanyValue() {
		return (String) getContext(Env.COMPANY_VALUE);
	}

	public static void setCompany(SysCompany company) {
		Env.setContext(Env.COMPANY_OBJECT, company);
	}

	public static SysCompany getCompany() {
		if (Sessions.getCurrent() != null) {
			return (SysCompany) Env.getContext(Env.COMPANY_OBJECT);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getMapUserRule() {
		Map<String, String> mapResult = null;
		if (Sessions.getCurrent() != null) {
			if (Env.getContext(Env.USER_RULE) != null) {
				mapResult = (Map<String, String>) Env.getContext(Env.USER_RULE);
			} else {
				mapResult = new HashMap<>();
				Env.setContext(Env.USER_RULE, mapResult);
			}
		}
		return mapResult;
	}

	private static String TELEPHONE_TABLE_TMS = "table_tms";
	private static String TELEPHONE_EXTENTION_TMS = "ext_tms";
	static Hashtable<String, Session> mapTableTmsSession = new Hashtable<>();

	public static void setTableTms(TelephoneTableTms tableTms) {
		mapTableTmsSession.put(tableTms.getId() + "", Sessions.getCurrent());
		Env.setContext(TELEPHONE_TABLE_TMS, tableTms);
		Env.setContext(TELEPHONE_EXTENTION_TMS, TelephoneTableTmsController.getExtensionsForTable(tableTms));
	}

	public static boolean isResponsibleForCallEvent(CallInfo callInfo) {
		if (Sessions.getCurrent() != null) {
			TelephoneTableTms tableTms = (TelephoneTableTms) Env.getContext(TELEPHONE_TABLE_TMS);
			if (tableTms == null || mapTableTmsSession.get(tableTms.getId() + "") != Sessions.getCurrent())
				return false;
			for (VoipCenter callCenter : tableTms.getSysCompany().getVoipCenter()) {
				if (callCenter.getValue().trim().equalsIgnoreCase(callInfo.getCallCenterId().trim())) {
					@SuppressWarnings("unchecked")
					List<TelephoneExtensionTms> exts = (List<TelephoneExtensionTms>) Env
							.getContext(TELEPHONE_EXTENTION_TMS);
					for (TelephoneExtensionTms ext : exts) {
						if (ext.getExtension().trim().equals(callInfo.getExtension().trim())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
