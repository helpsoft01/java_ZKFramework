package com.vietek.taxioperation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import com.vietek.taxioperation.common.AppLogger;

/**
 *
 * @author VuD
 */
public class WebserviceUtils {

	/**
	 * @author BaTT
	 * @param strUrl
	 * @param params
	 * @return Chuoi ket qua json
	 */
	public static String doGet(String strUrl, Map<String, Object> params) {
		String result = "";
		InputStream inputStream = null;
		HttpURLConnection conn = null;

		try {
			URL url;
			if (params == null)
				url = new URL(strUrl);
			else
				url = new URL(strUrl + "?" + getQuery(params));
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
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

	private static String readInputStream(InputStream stream) throws IOException {

		BufferedReader reader = null;
		StringBuffer result = new StringBuffer();
		String inputLine;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			while ((inputLine = reader.readLine()) != null) {
				result.append(inputLine);
			}
		} catch (Exception ex) {
			AppLogger.logUserAction.error("", ex);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
			}
		}
		return result.toString();
	}

	private static String getQuery(Map<String, Object> mapParams) throws UnsupportedEncodingException {
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
}
