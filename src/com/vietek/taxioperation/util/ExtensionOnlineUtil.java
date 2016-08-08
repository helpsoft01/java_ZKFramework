package com.vietek.taxioperation.util;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

import com.vietek.taxioperation.controller.TelephoneExtensionTmsController;
import com.vietek.taxioperation.controller.VoipCenterController;
import com.vietek.taxioperation.model.Call;
import com.vietek.taxioperation.model.TelephoneExtensionTms;
import com.vietek.taxioperation.model.VoipCenter;

public class ExtensionOnlineUtil {
	private JsonExtension[] users;
	private static ConcurrentHashMap<String, List<JsonExtension>> lstVoipCenter_Extensions = new ConcurrentHashMap<>();
	private static List<VoipCenter> lstVoipCenter = new ArrayList<>();
	public static String _urlExtensionOnlineAll = "service/webservice.php?getOnlineAll";
	public static String _urlExtensionOnline = "service/webservice.php?getOnline";
	@JsonIgnore
	static URL url = null;
	@JsonIgnore
	static URLConnection conn = null;
	@JsonIgnore
	static InputStream in = null;
	@JsonIgnore
	ObjectMapper mapper = new ObjectMapper();
	@JsonIgnore
	ExtensionOnlineUtil response = null;

	public JsonExtension[] getUsers() {
		return users;
	}

	public void setUsers(JsonExtension[] users) {
		this.users = users;
	}

	public static List<VoipCenter> getLstVoipCenter() {

		if (lstVoipCenter.size() == 0) {
			VoipCenterController controller = (VoipCenterController) ControllerUtils
					.getController(VoipCenterController.class);
			lstVoipCenter = controller.find("from VoipCenter");
		}
		return lstVoipCenter;
	}

	public static void setLstVoipCenter(List<VoipCenter> lstVoipCenter) {
		ExtensionOnlineUtil.lstVoipCenter = lstVoipCenter;
	}

	public static void setLstVoipCenter(ConcurrentHashMap<String, List<JsonExtension>> lstVoipCenter) {
		ExtensionOnlineUtil.lstVoipCenter_Extensions = lstVoipCenter;
	}

	public static List<JsonExtension> getListExtension_FormVoipCenter(VoipCenter voipCenter) {
		ObjectMapper mapper = new ObjectMapper();
		List<JsonExtension> result = lstVoipCenter_Extensions.get(voipCenter.getValue().trim());
		if (result == null) {
			result = new ArrayList<>();
			lstVoipCenter_Extensions.put(voipCenter.getValue(), result);
		}
		if (result.size() == 0) {

			try {
				url = new URL(voipCenter.getUrl() + _urlExtensionOnlineAll);
				conn = url.openConnection();
				in = conn.getInputStream();
				List<JsonExtension> resultDaft = Arrays.asList(mapper.readValue(in, JsonExtension[].class));
				for (JsonExtension jsonExtension : resultDaft) {
					result.add(jsonExtension);
				}
				TelephoneExtensionTmsController controller = (TelephoneExtensionTmsController) ControllerUtils
						.getController(TelephoneExtensionTmsController.class);
				String sql = "from TelephoneExtensionTms where extension = ?";
				for (JsonExtension extension : result) {
					List<TelephoneExtensionTms> lst = controller.find(sql, extension.getExtension());
					if (lst.size() > 0)
						extension.setExtensionTms(lst.get(0));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@SuppressWarnings("deprecation")
	public static boolean getExtension_Notuse(VoipCenter voipCenter, String extension, boolean isExtensionRegisted,
			int userID) {

		boolean reVal = true;
		String _url = voipCenter.getUrl() + _urlExtensionOnline + "=" + extension;
		URL url = null;
		URLConnection conn = null;
		InputStream in = null;

		try {

			url = new URL(_url);
			conn = url.openConnection();
			in = conn.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			String tmp = dis.readLine();
			if (tmp != null)

				if (tmp.startsWith(JsonExtension.FREFIXSTATUS_UN_REGISTERED)) {

					if (isExtensionRegisted == true) {
							Call call = new Call();
							call.setVoipExtension(extension);
							call.setAgentId(userID);
							EventQueues
									.lookup(Env.SERVER_PUSH_EVENT_EXTENSION_UN_REGISTED, Env.WEBAPP, true)
									.publish(new Event(Env.SERVER_PUSH_EVENT_EXTENSION_UN_REGISTED, null, call));
					}
					reVal = false;
				} else if (tmp.startsWith(JsonExtension.FREFIXSTATUS_REGISTERED)) {
					if (isExtensionRegisted == true && tmp.endsWith(JsonExtension.SUFFIXSTATUS_NOTINUSE)) {
						reVal = true;
					} else if (isExtensionRegisted == true && tmp.endsWith(JsonExtension.SUFFIXSTATUS_INUSE)) {
						reVal = false;
					} else {
						if (isExtensionRegisted == false) {
//							CheckOnlineUtils.setRegisted_Extension(extension, true);
							Call call = new Call();
							call.setVoipExtension(extension);
							call.setAgentId(userID);
							EventQueues
									.lookup(Env.SERVER_PUSH_EVENT_EXTENSION_REGISTED, Env.WEBAPP,
											true)
									.publish(new Event(Env.SERVER_PUSH_EVENT_EXTENSION_REGISTED, null, call));
							reVal = true;
						} else
							reVal = false;
					}

				}

		} catch (Exception e)

		{

			e.printStackTrace();
			reVal = false;
		}

		return reVal;

	}

	@SuppressWarnings("deprecation")
	public static String getExtension_Registed(VoipCenter voipCenter, String extension) {

		String reVal = "";
		String _url = voipCenter.getUrl() + "service/webservice.php?getOnline=" + extension;
		URL url = null;
		URLConnection conn = null;
		InputStream in = null;

		try {

			url = new URL(_url);
			conn = url.openConnection();
			in = conn.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			String tmp = dis.readLine();
			if (tmp != null)

				if (tmp.startsWith(JsonExtension.FREFIXSTATUS_REGISTERED)) {

					reVal = JsonExtension.FREFIXSTATUS_REGISTERED;
				} else
					reVal = JsonExtension.FREFIXSTATUS_UN_REGISTERED;

		} catch (Exception e) {

			e.printStackTrace();
			reVal = "";
		}

		return reVal;
	}

	public static boolean getExtension_Exists(VoipCenter voipCenter, String extension) {

		boolean reVal = true;
		String _url = voipCenter.getUrl() + "service/webservice.php?getOnline=" + extension;
		URL url = null;
		URLConnection conn = null;
		InputStream in = null;

		try {

			url = new URL(_url);
			conn = url.openConnection();
			in = conn.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			@SuppressWarnings("deprecation")
			String tmp = dis.readLine();
			if (tmp != null)
				reVal = true;
			else
				reVal = false;

		} catch (Exception e) {

			e.printStackTrace();
			reVal = false;
		}

		return reVal;
	}

	class ReceiveNearExtensionNumber {
		public ReceiveNearExtensionNumber() {

		}
	}
}
