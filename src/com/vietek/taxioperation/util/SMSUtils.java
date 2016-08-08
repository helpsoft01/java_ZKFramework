package com.vietek.taxioperation.util;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.tempuri.BsmswsSoap;
import org.tempuri.BsmswsSoapProxy;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;

public class SMSUtils {
	@SuppressWarnings("unused")
	private static Boolean isSendSmsOk(String phoneNumber, String message) {
		String url = "http://center.fibosms.com/Service.asmx/SendSMS?clientNo=CL5627&clientPass=85qCDag2S&phoneNumber={phoneNumber}&smsMessage={message}&smsGUID=0&serviceType=6438";
		url = url.replace("{phoneNumber}", phoneNumber);
		url = url.replace("{message}", message);
		Client client = Client.create();
		WebResource webResource = null;
		try {
			webResource = client.resource(URIUtil.encodeQuery(url));
		} catch (URIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			return false;
		}

		String output = response.getEntity(String.class);
		response.close();
		if (output.indexOf("Sending...") > 0)
			return true;
		return false;
	}

	private static Boolean isSendSMSVNPT(String phoneNumber, String message) {
		try {
			BsmswsSoap soap = new BsmswsSoapProxy();
			soap.sendBrandSms(ConfigUtil.getValueConfig("SMS_USERNAME", CommonDefine.SMS_USERNAME),
					ConfigUtil.getValueConfig("SMS_PASSWORD", CommonDefine.SMS_PASSWORD), phoneNumber, message,
					ConfigUtil.getValueConfig("SMS_BRANDNAME", CommonDefine.SMS_BRANDNAME), 1);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void sendSMS(String phoneNumber, String message) {
		if (!isSendSMSVNPT(phoneNumber, message)) {

		}
	}

	public static void main(String[] args) {
		boolean isOk = isSendSMSVNPT("84979914389", "Test WS");
		if (isOk) {
			AppLogger.logDebug.info("Sending okie!");
		} else {
			AppLogger.logDebug.info("Sending fails!");
		}
	}
}
