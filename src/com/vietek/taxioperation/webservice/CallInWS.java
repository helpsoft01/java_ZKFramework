package com.vietek.taxioperation.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.zkoss.zk.ui.WebApp;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CallCenterCommon;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.googlemapSearch.EventUserCallIn;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.VoipCenter;
import com.vietek.taxioperation.ui.controller.HomePage;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ExtensionEnable;
import com.vietek.taxioperation.util.ExtensionOnlineUtil;
import com.vietek.taxioperation.util.ExtensionStatus;
import com.vietek.taxioperation.util.GetNearChannel;
import com.vietek.taxioperation.util.JsonExtension;

@Path("/callIn")
public class CallInWS {
	public static WebApp webApp;
	public static HomePage homePage;
	public static final String SUB_URL_GET_EXTENSION = new String("/service/webservice.php?getExtension");
	public static final String SUB_URL_GET_ONLINE = new String("/service/webservice.php?getOnline=");

	public static List<ChannelTms> lstChannel = null;

	EventUserCallIn eventUserCallIn = new EventUserCallIn();

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String callIn(@QueryParam("number") String number, @Context HttpServletRequest request) {
		AppLogger.logDebug.info("CallInWS|callIn" + request.getRemoteAddr());
		AppLogger.logDebug.info("Call from number: " + number);
		return "8004";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String callInGet(@QueryParam("number") String number, @QueryParam("callcenterid") String callCenterId,
			@QueryParam("calluuid") String calluuid, @Context HttpServletRequest request) {
		AppLogger.logTaxiorder.info("1. CallIn-Calluuid:" + calluuid + "|PhoneNumber:" + number);

		// TODO: get String URL Callcenter
		VoipCenter voipCenter = CallCenterCommon.getVoipCenter_By_CallCenterId(callCenterId);

		if (number == null)
			return "";
		if (number.trim().isEmpty())
			return "";
		if (voipCenter == null)
			return "";

		/*
		*
		*/
		String phoneNumber = number.trim();
		BigDecimal uuid = new BigDecimal(calluuid);
		/*
		 * 1. lấy tất cả cấc extension
		 * http://113.161.68.65:8080/service/webservice.php?getExtension
		 */

		List<JsonExtension> lstExtensionRegisted_InVoipCenter = ExtensionOnlineUtil
				.getListExtension_FormVoipCenter(voipCenter);
		/*
		 * 2. tìm trong danh sách extension nào còn chống và hợp lý nhất
		 */
		GetNearChannel nearChannel = new GetNearChannel(phoneNumber);
		ExtensionEnable extensionEnable = nearChannel.getBetterExtension(lstExtensionRegisted_InVoipCenter, phoneNumber,
				voipCenter, uuid);

		/*
		 * 3. trả về số extensionNumber
		 */
		String extensionNumber = "";
		// if (extensionEnable != null)
		// extensionNumber =
		// extensionEnable.getTelephoneExtensionTms().getExtension().trim();

		AppLogger.logTaxiorder.info("2.CallIn Return:calluuid:" + calluuid + "-extension:" + extensionNumber);
		return extensionNumber;
	}

	public static String doGet(String strUrl, Map<String, Object> params) {

		String result = "";
		InputStream inputStream = null;
		HttpURLConnection conn = null;

		try {

			URL url = new URL(strUrl + "?" + getQuery(params));
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);

			// Start query
			conn.connect();

			// Get return
			inputStream = conn.getInputStream();
			result = readInputStream(inputStream);
			return result;
		} catch (Exception ex) {
			result = ex.getMessage();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (conn != null)
					conn.disconnect();
			} catch (Exception ex) {
			}
		}

		return result;
	}

	public static String readInputStream(InputStream stream) throws IOException {

		BufferedReader reader = null;
		StringBuffer result = new StringBuffer();
		String inputLine;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			while ((inputLine = reader.readLine()) != null) {
				result.append(inputLine);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
			}
		}

		return result.toString();
	}

	public static String getQuery(Map<String, Object> mapParams) throws UnsupportedEncodingException {
		if (mapParams == null)
			return "";
		StringBuilder result = new StringBuilder();
		boolean first = true;
		Set<Map.Entry<String, Object>> paramSet = mapParams.entrySet();
		for (Map.Entry<String, Object> param : paramSet) {
			if (first)
				first = false;
			else
				result.append("&");
			result.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			result.append('=');
			result.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}

		return result.toString();
	}

	public static List<ExtensionStatus> getListExtensionFree() {
		String url = ConfigUtil.getValueConfig("CALL_CENTER_URL", CommonDefine.CALL_CENTER_URL)
				+ "service/webservice.php?getOnlineAll";
		String respond = doGet(url, null);
		List<ExtensionStatus> lstResult = new ArrayList<>();
		try {
			JSONParser jsonParser = new JSONParser();
			JSONArray jsonarray = (JSONArray) jsonParser.parse(respond);
			@SuppressWarnings("rawtypes")
			Iterator i = jsonarray.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				ExtensionStatus result = new ExtensionStatus();
				result.setExtension(innerObj.get("extension").toString());
				result.setStatus(innerObj.get("status").toString());
				lstResult.add(result);
			}
			for (ExtensionStatus object : lstResult) {
				AppLogger.logDebug.info(object.toString());
			}
		} catch (Exception e) {
			lstResult = null;
		}

		return lstResult;

	}

}
