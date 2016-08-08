package com.vietek.taxioperation.util;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.WebApp;

import com.vietek.taxioperation.googlemapSearch.CallInUser;
import com.vietek.taxioperation.googlemapSearch.TableOnline;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TelephoneExtensionTms;
import com.vietek.taxioperation.model.TelephoneTableTms;
import com.vietek.taxioperation.model.VoipCenter;

public class CheckOnlineUtils {

	public static WebApp webApp = null;
	private static ConcurrentHashMap<String, CallInUser> extensionOnlineMap = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, TableOnline> tableOnlineMap = new ConcurrentHashMap<>();

	static public void logout(String user) {
		remove_ExtensionTable_ByUser(user);
	}

	static public void setExtensions_VoipCenter_ForUser(SysUser user, List<TelephoneExtensionTms> lstExtension,
			VoipCenter voip, Desktop desktop) {

		int index = 0;
		for (TelephoneExtensionTms extensionEnable : lstExtension) {
			CallInUser userCallIn = new CallInUser();
			userCallIn.setUser(user);
			userCallIn.setVoip(voip);
			userCallIn.setDesktop(desktop);
			userCallIn.setExtension(extensionEnable.getExtension());
			userCallIn.setIndex(index);
			++index;

			String key = getKeyExtension(voip.getValue(), extensionEnable.getExtension());
			extensionOnlineMap.put(key, userCallIn);
		}
	}

	static public String getKeyExtension(String voip, String extension) {

		return voip + "_" + extension;
	}

	static public CallInUser setCustomerByVoipExtension(String voipExtension, String calluuid, Customer cust) {

		CallInUser item = extensionOnlineMap.get(voipExtension);
		if (item != null) {
			item.setCustomer(cust);
			item.setTimeCallIn(new Timestamp(System.currentTimeMillis()));
			item.setCalluuid(calluuid);
			return item;
		}
		return null;
	}

	static public CallInUser getCustomerByVoipExtension(String voipExtension) {

		CallInUser item = extensionOnlineMap.get(voipExtension);
		if (item != null) {
			return item;
		}
		return null;

	}

	static public CallInUser getCustomerByUuId(String uuid) {

		for (CallInUser item : extensionOnlineMap.values()) {
			if (item.getCalluuid() != null)
				if (item.getCalluuid().equals(uuid)) {
					return item;
				}
		}

		return null;
	}

	static public int getPositionByExteVoipnsion(String voipExtension) {

		CallInUser item = extensionOnlineMap.get(voipExtension);
		if (item != null) {
			return item.getIndex();
		}
		return 0;
	}

	static public void addTelephoneTableForUser(TelephoneTableTms tableTms, SysUser user, Desktop desktop) {

		String key = tableTms.getName();

		TableOnline item = tableOnlineMap.get(key);
		if (item != null)
			tableOnlineMap.remove(key);

		TableOnline obj = new TableOnline();
		obj.setDesktop(desktop);
		obj.setTable(tableTms);
		obj.setUser(user);
		tableOnlineMap.put(key, obj);
	}

	static public void remove_ExtensionTable_ByDesktop(Desktop desktop) {

		String session = desktop.getSession().toString();
		/*
		 * remove table
		 */
		for (TableOnline item : tableOnlineMap.values()) {
			if (item.getDesktop().getSession().toString().equals(session)) {
				tableOnlineMap.remove(item.getTable().getName());
			}
		}

		/*
		 * remove extension
		 */
		for (CallInUser element : extensionOnlineMap.values()) {
			if (element.getDesktop().getSession().toString().equals(session)) {
				String key = getKeyExtension(element.getVoip().getValue(), element.getExtension());
				extensionOnlineMap.remove(key);
			}
		}

	}

	static public void remove_ExtensionTable_ByUser(String userName) {

		/*
		 * remove table
		 */
		for (TableOnline item : tableOnlineMap.values()) {
			if (item.getUser().getUserName().equals(userName)) {
				tableOnlineMap.remove(item.getTable().getName());

			}
		}

		/*
		 * remove extension
		 */
		for (CallInUser element : extensionOnlineMap.values()) {
			if (element.getUser().getUserName().equals(userName)){
				String key = getKeyExtension(element.getVoip().getValue(), element.getExtension());
				extensionOnlineMap.remove(key);
			}
		}
	}

	public static TelephoneTableTms checkTableTms_InUse(TelephoneTableTms telephoneTableTms) {

		TelephoneTableTms reVal = null;
		for (TableOnline item : tableOnlineMap.values()) {
			if (item.getTable().getId() == telephoneTableTms.getId())
				reVal = item.getTable();
			break;
		}
		return reVal;
	}
}