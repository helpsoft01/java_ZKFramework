package com.vietek.taxioperation.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author VuD
 */
public class FunctionCommon {
	

	public static String sendPost(String url, String entity, HashMap<String, String> params) throws Exception {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			String fullUrl = url.concat(entity);
			HttpPost post = new HttpPost(fullUrl);

			List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
			for (Entry<String, String> s : params.entrySet()) {
				urlParameters.add(new BasicNameValuePair(s.getKey(), s.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			HttpResponse response = client.execute(post);
			AppLogger.logDebug.info("\nSending 'POST' request to URL : " + fullUrl);
			AppLogger.logDebug.info("Post parameters : " + post.getEntity());
			AppLogger.logDebug.info("Response Code : " + response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (Exception e) {
			return "\"Err\"";
		}
	}
}
