package com.vietek.taxioperation.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietek.taxioperation.common.AppLogger;

public class GCMUtils {
	final int STATUS_ALL_APP = 1;
	final int STATUS_SPRCIFICE_DEVICES = 2;

	public static void main(String[] args) {

		// Android

		List<String> listReg = new LinkedList<String>();
		AppLogger.logDebug.info("Sending POST to GCM");

		String apiKey = "AIzaSyCE3jxxjccXtRGSmEx_He8enM-6slX5XCQ"; // Android
		// String apiKey = "AIzaSyCF6_dQY7d4KAcCbRjJqmForbQBNyngmZ4"; // IOSR
		String title = "MainLinhTaxi";
		String msg = "Hello from GCM FFFF !";
		listReg.add(
				"fQS1Uer7pkM:APA91bE1FApbC-wYokqS0s8y-MqdOC7KESqtfRtLLAqN8xHubukcjipcrsWAzXYSAQbVEQK8fCHOnu_oaixEvmUD8NE6XVmjrD0UaJ2gceraoJNWLM_Go32BHe1N6BZvgbQ2A9s6WPWL");
		// listReg.add("d2fXxcW201E:APA91bGj1580MbdyqpLh8WxKvsI01ccSBH7eC0OC7lVTyf0YAstr7YBE2fHp9cPByRml836LPJTl6v17obY1hR4BYmK20fOLJWjPRBF732fTlFuq0mX6Cy09osJleiKJRdC-CQnZIa67");
		// erhd_ApUEUI:APA91bGe4MEg6fnZhSO8PkS5SSeElPxqaSHfmWcTsISU5Vm8HxE8E2ZuKqgiLgNVOx76CzAV2Fwb9Cy52D8ikg9CFu-0bpCGPRxGjVjLeePidBRYffC9cFP74EfSEExVUXKxcaYWzGgf
		Content content = Content.createContent(msg, listReg);
		// post(apiKey, msg);

		sendToDevices(apiKey, content);

		// IOS
		// URL
		/*
		 * String apiKey = "AIzaSyCF6_dQY7d4KAcCbRjJqmForbQBNyngmZ4"; // Put
		 * here your API key String GCM_Token =
		 * "kt6ZqmfjTNg:APA91bFx_oi_cC5cnQXUpDYGow_iBPLUkGC5-X6Wa7x1nbNTfuRAjK4CWhj75B1RqPkMlG0EIZeKGHBYKjELsV5HAZKhjEMyQ03iE5EpKw_CIsXcecsCHvA4EJt1obUKkHN2AH4re1sq";
		 * // put the GCM Token you want to send to here String notification =
		 * "{\"sound\":\"default\",\"badge\":\"2\",\"title\":\"default\",\"body\":\"Test Push!\"}"
		 * ; // put the message you want to send here String messageToSend =
		 * "{\"to\":\"" + GCM_Token + "\",\"notification\":" + notification +
		 * ",\"priority\":\"high\"}"; // Construct the message.
		 * 
		 * try{ URL url = new URL("https://android.googleapis.com/gcm/send");
		 * 
		 * // Open connection HttpURLConnection conn = (HttpURLConnection)
		 * url.openConnection();
		 * 
		 * // Specify POST method conn.setRequestMethod("POST");
		 * 
		 * //Set the headers conn.setRequestProperty("Content-Type",
		 * "application/json"); conn.setRequestProperty("Authorization", "key="
		 * + apiKey); conn.setDoOutput(true);
		 * 
		 * //Get connection output stream DataOutputStream wr = new
		 * DataOutputStream(conn.getOutputStream());
		 * 
		 * byte[] data = messageToSend.getBytes("UTF-8"); wr.write(data);
		 * 
		 * //Send the request and close wr.flush(); wr.close();
		 * 
		 * //Get the response int responseCode = conn.getResponseCode();
		 * AppLogger.loggerDebug.info("\nSending 'POST' request to URL : " +
		 * url); AppLogger.loggerDebug.info("Response Code : " + responseCode);
		 * 
		 * BufferedReader in = new BufferedReader(new
		 * InputStreamReader(conn.getInputStream())); String inputLine;
		 * StringBuffer response = new StringBuffer();
		 * 
		 * while ((inputLine = in.readLine()) != null) {
		 * response.append(inputLine); } in.close();
		 * 
		 * //Print result AppLogger.loggerDebug.info(response.toString());
		 * //this is a good place to check for errors using the codes in
		 * http://androidcommunitydocs.com/reference/com/google/android/gcm/
		 * server/Constants.html
		 * 
		 * } catch (MalformedURLException e) { e.printStackTrace(); } catch
		 * (IOException e1) { e1.printStackTrace(); }
		 */
	}

	public static void sendToDevicesIOS(String apiKey, String regId, String content) {
		IOSNotifyWorker worker = new IOSNotifyWorker();
		worker.apiKey = apiKey;
		worker.regId = regId;
		worker.content = content;
		Thread thread = new Thread(worker);
		thread.start();
	}

	/**
	 * Use to send notify to specific devices Android
	 */
	public static void sendToDevices(String apiKey, Content content) {
		AndroidNotifyWorker worker = new AndroidNotifyWorker();
		worker.apiKey = apiKey;
		worker.content = content;
		Thread thread = new Thread(worker);
		thread.start();
	}

	/**
	 * Use to send notify to all devices
	 */
	public static void post(String apiKey, String content) {
		try {
			// Prepare JSON containing the GCM message content. What to send and
			// where to send.
			JSONObject jGcmData = new JSONObject();
			JSONObject jData = new JSONObject();
			// jData.put("message", args[0].trim());
			jData.put("message", content);
			// Where to send GCM message.
			// if (content.length > 1 && args[1] != null) {
			// jGcmData.put("to", args[1].trim());
			// } else {
			// jGcmData.put("to", "/topics/global");
			// }
			jGcmData.put("to", "/topics/global");
			jGcmData.put("priority", "high");

			// What to send in GCM message.
			jGcmData.put("data", jData);

			// Create connection to send GCM Message request.
			URL url = new URL("https://android.googleapis.com/gcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Authorization", "key=" + apiKey);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			// Send GCM message content.
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(jGcmData.toString().getBytes());
			AppLogger.logDebug.info("Response Code: " + conn.getResponseCode());
			// Read GCM response.
			InputStream inputStream = conn.getInputStream();
			String resp = IOUtils.toString(inputStream);
			AppLogger.logDebug.info(resp);
			AppLogger.logDebug.info("Check your device/emulator for notification or logcat for "
					+ "confirmation of the receipt of the GCM message.");
		} catch (IOException e) {
			AppLogger.logDebug.error("Unable to send GCM message.", e);
			AppLogger.logDebug.error("Please ensure that API_KEY has been replaced by the server "
					+ "API key, and that the device's registration token is correct (if specified).", e);
		}
	}
}

class AndroidNotifyWorker implements Runnable {
	String apiKey;
	Content content;

	@Override
	public void run() {
		try {
			URL url = new URL("https://android.googleapis.com/gcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + apiKey);
			conn.setDoOutput(true);
			ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			mapper.writeValue(wr, content);
			wr.flush();
			wr.close();
			int responeCode = conn.getResponseCode();
			AppLogger.logDebug.info("\n Sending 'POST' request to URL: " + url);
			AppLogger.logDebug.info("\n Respone code: " + responeCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			AppLogger.logDebug.info("respone content: " + response.toString());

		} catch (MalformedURLException e) {
			// TODO: handle exception
		} catch (IOException e1) {

		}
	}
}

class IOSNotifyWorker implements Runnable {
	String apiKey;
	String content;
	String regId;

	@Override
	public void run() {
		String notification = "{\"sound\":\"default\",\"badge\":\"2\",\"title\":\"default\",\"body\":\"" + content
				+ "\"}";
		String messageToSend = "{\"to\":\"" + regId + "\",\"notification\":" + notification + ",\"priority\":\"high\"}"; // Construct
																															// the
																															// message.
		// String messageToSend = "{\"to\":\"" + apiKey + "\",\"notification\":"
		// + notification + "}"; // Construct the message.
		try {
			URL url = new URL("https://android.googleapis.com/gcm/send");
			AppLogger.logDebug.info(messageToSend);
			// Open connection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// Specify POST method
			conn.setRequestMethod("POST");

			// Set the headers
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + apiKey);
			conn.setDoOutput(true);

			// Get connection output stream
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

			byte[] data = messageToSend.getBytes("UTF-8");
			wr.write(data);

			// Send the request and close
			wr.flush();
			wr.close();

			// Get the response
			int responseCode = conn.getResponseCode();
			AppLogger.logDebug.info("\nSending 'POST' request to URL : " + url);
			AppLogger.logDebug.info("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Print result
			AppLogger.logDebug.info(response.toString()); // this is a good
																// place to
			// check for errors
			// using the codes in
			// http://androidcommunitydocs.com/reference/com/google/android/gcm/server/Constants.html

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
