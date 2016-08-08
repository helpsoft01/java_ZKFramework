/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.napas.TSPServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONObject;

import com.vietek.taxioperation.common.AppLogger;

/**
 *
 * @author Thanh Tung
 */
public class TSPConnection {

	private static final String USER_AGENT = "Mozilla/5.0";

	public TSPConnection() {
	}

	public String PAYTransaction(String merchantId, String orderId, String transactionId, String json)
			throws Exception {
		String accessKey = getAccessKey();
		String url = getPAYTransactionRequestUrl(merchantId, orderId, transactionId);
		String jsonResponse = RestPutClient(url, accessKey, json);
		return jsonResponse;
	}

	public String CreateNewTokenTransaction(String merchantId, String json) throws Exception {
		String accessKey = getAccessKey();
		String url = getCreateTokenTransactionRequestUrl(merchantId);
		String jsonResponse = RestPutClient(url, accessKey, json);
		return jsonResponse;
	}

	public String createNewTokenTransaction(String merchantId, String json) throws Exception {
		String accessKey = getAccessKey();
		String url = getDataKeyURL(merchantId);
		String jsonResponse = RestPostClient(url, accessKey, json);
		return jsonResponse;
	}

	public String DeleteTransaction(String merchantId, String tokenId, String json) throws Exception {
		String accessKey = getAccessKey();
		String url = getDeleteTokenTransactionRequestUrl(merchantId, tokenId);
		String jsonResponse = RestDeleteClient(url, accessKey, json);
		return jsonResponse;
	}

	public String RestPostClient(String url, String access_token, String jsondata) {
		HttpClient httpClient = new HttpClient();
		StringBuilder result = new StringBuilder();
		try {
			PostMethod postMethod = new PostMethod(url);
			postMethod.setRequestHeader("User-Agent", USER_AGENT);
			postMethod.setQueryString(getQueryString(access_token));
			StringRequestEntity entity = new StringRequestEntity(jsondata, "application/json", "UTF-8");
			postMethod.setRequestEntity(entity);
			httpClient.executeMethod(postMethod);
			String body = postMethod.getResponseBodyAsString();
			result.append(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public String RestPutClient(String url, String access_token, String jsondata) {
		HttpClient httpClient = new HttpClient();
		StringBuilder result = new StringBuilder();
		try {
			PutMethod putMethod = new PutMethod(url);
			putMethod.setQueryString(getQueryString(access_token));
			StringRequestEntity entity = new StringRequestEntity(jsondata, "application/json", "UTF-8");
			putMethod.setRequestEntity(entity);
			httpClient.executeMethod(putMethod);
			String body = putMethod.getResponseBodyAsString();
			result.append(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public String RestDeleteClient(String url, String access_token, String jsondata) {
		HttpClient httpClient = new HttpClient();
		StringBuilder result = new StringBuilder();
		try {
			DeleteMethod deleteMethod = new DeleteMethod(url);
			deleteMethod.setQueryString(getQueryString(access_token));
			httpClient.executeMethod(deleteMethod);
			String body = deleteMethod.getResponseBodyAsString();
			result.append(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/*
	 * apiOperation: get Data key
	 */
	public String getDataKeyURL(String merchantId) {
		StringBuilder url = new StringBuilder(Configuration.TSP_GATEWAY_URL);
		url.append("/version/");
		url.append(Configuration.apiVersion);
		url.append("/merchant/");
		url.append(merchantId);
		url.append("/datakey");
		return url.toString();
	}

	/*
	 * apiOperation: PAY, AUTHORIZE, CAPTURE, VOID
	 */
	public String getPAYTransactionRequestUrl(String merchantId, String orderId, String transactionId) {
		StringBuilder url = new StringBuilder(Configuration.TSP_GATEWAY_URL);
		url.append("/version/");
		url.append(Configuration.apiVersion);
		url.append("/merchant/");
		url.append(merchantId);
		url.append("/order/");
		url.append(orderId);
		url.append("/transaction/");
		url.append(transactionId);
		return url.toString();
	}

	/*
	 * apiOperation: TOKEN
	 */
	public String getCreateTokenTransactionRequestUrl(String merchantId) {
		StringBuilder url = new StringBuilder(Configuration.TSP_GATEWAY_URL);
		url.append("/version/");
		url.append(Configuration.apiVersion);
		url.append("/merchant/");
		url.append(merchantId);
		url.append("/token");
		return url.toString();
	}

	/*
	 * apiOperation: DELETE_TOKEN
	 */
	private String getDeleteTokenTransactionRequestUrl(String merchantId, String tokenId) {
		StringBuilder url = new StringBuilder(Configuration.TSP_GATEWAY_URL);
		url.append("/version/");
		url.append(Configuration.apiVersion);
		url.append("/merchant/");
		url.append(merchantId);
		url.append("/token/");
		url.append(tokenId);
		return url.toString();
	}

	public String getAccessKey() throws Exception {
		PostMethod post = getPostMethod();
		String jsonOAuth = getAuthorize(post);
		String accessKey = "";
		if (!"".equals(jsonOAuth)) {
			JSONObject json = new JSONObject(jsonOAuth);
			accessKey = json.get("access_token").toString();
		}
		return accessKey;
	}

	private PostMethod getPostMethod() {
		PostMethod post = new PostMethod(Configuration.TSP_AUTHORIZE_URL);
		post.setRequestHeader("User-Agent", USER_AGENT);
		post.addParameter(new NameValuePair("grant_type", Configuration.grant_type));
		post.addParameter(new NameValuePair("client_id", Configuration.client_id));
		post.addParameter(new NameValuePair("client_secret", Configuration.client_secret));
		post.addParameter(new NameValuePair("username", Configuration.apiUsername));
		post.addParameter(new NameValuePair("password", Configuration.apiPassword));
		return post;
	}

	public String getAuthorize(PostMethod post) {
		HttpClient client = new HttpClient();
		BufferedReader br = null;
		String jsonResult = "";
		try {
			int returnCode = client.executeMethod(post);
			if (returnCode == HttpStatus.SC_OK) {
				br = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream()));
				String readLine = "";
				while (((readLine = br.readLine()) != null)) {
					jsonResult += readLine;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
			if (br != null) {
				try {
					br.close();
				} catch (Exception fe) {
				}
			}
		}
		return jsonResult;
	}

	private NameValuePair[] getQueryString(String access_token) {
		NameValuePair[] data = new NameValuePair[] { new NameValuePair("access_token", access_token) };
		return data;
	}

	public static void main(String[] args) {
		TSPConnection tsp = new TSPConnection();
		try {
			// String json = "{ " +
			// "\"apiOperation\": \"DATA_KEY\", " +
			// "\"inputParameters\": { " +
			// "\"clientIP\": \"192.168.1.1\"," +
			// "\"deviceId\": \"0945868885\", " +
			// "\"environment\": \"MobileApp\", " +
			// "\"cardScheme\": \"CreditCard\", " +
			// "\"enable3DSecure\": \"false\" " +
			// "} " +
			// "}";
			String json = "{ " + "\"apiOperation\": \"DELETE_TOKEN\"" + "}";
			AppLogger.logDebug.info(tsp.DeleteTransaction(Configuration.merchantId, "4005550814280019", json));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
