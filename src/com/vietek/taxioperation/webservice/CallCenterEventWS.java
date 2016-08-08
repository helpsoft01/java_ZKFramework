package com.vietek.taxioperation.webservice;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.zkoss.zk.ui.event.Event;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CallCenterCommon;
import com.vietek.taxioperation.mq.TaxiOrderMQ;
import com.vietek.taxioperation.util.CallInfo;

/**
 *
 * @author VuD
 */

@Path("CallEvent")
public class CallCenterEventWS {
	public static final String secretKey = new String("ed8f5b7e74398143b43a93dc753618ae");
	public static final String SUB_URL_GET_EXTENSION = new String("/service/webservice.php?getExtension");
	public static final String SUB_URL_GET_ONLINE = new String("/service/webservice.php?getOnline=");

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String action(@QueryParam("secret") String secret, @QueryParam("callstatus") String callStatus,
			@QueryParam("calluuid") String calluuid, @QueryParam("parentcalluuid") String parentcalluuid,
			@QueryParam("direction") String direction, @QueryParam("callernumber") String caller,
			@QueryParam("destinationnumber") String destination, @QueryParam("starttime") String startTime,
			@QueryParam("datereceived") String dateReceive, @QueryParam("causetxt") String causeTxt,
			@QueryParam("answertime") String answerTime, @QueryParam("endtime") String endTime,
			@QueryParam("billduration") String billduration, @QueryParam("totalduration") String totalduration,
			@QueryParam("callcenterid") String callCenterId) {

		String result = String.valueOf(EnumCallRespond.SUCCESS.getValue());
		if (callStatus.equals(EnumCallStatus.START.getValue())) {
			result = this.handleStartEvent(callCenterId, secret, callStatus, calluuid, direction, caller, destination);
		} else if (callStatus.equals(EnumCallStatus.DIALING.getValue())) {
			this.handleDialingEvent(callCenterId, secret, callStatus, calluuid, parentcalluuid, direction, caller,
					destination, startTime);
		} else if (callStatus.equals(EnumCallStatus.DIAL_ANSWER.getValue())) {
			result = this.handleDialAnswerEvent(callCenterId, secret, callStatus, calluuid, caller, destination);
		} else if (callStatus.equals(EnumCallStatus.HANGUP.getValue())) {
			this.handleHangUpEvent(callCenterId, secret, callStatus, calluuid, dateReceive, causeTxt, destination);
		} else if (callStatus.equals(EnumCallStatus.TRIM.getValue())) {
			this.handleTrimEvent(callCenterId, secret, callStatus, calluuid, parentcalluuid);
		} else if (callStatus.equals(EnumCallStatus.CDR.getValue())) {
			this.handleCdrEvent(secret, calluuid, startTime, answerTime, endTime, billduration, totalduration);
		}
		return String.valueOf(result);
	}

	private void handleDialingEvent(String callCenterId, String secret, String callStatus, String calluuid,
			String parentcalluuid, String direction, String caller, String destination, String startTime) {

		AppLogger.logTaxiorder.info(
				"2. Dialing|PhoneNumber:" + caller + "|parentcalluuid :" + parentcalluuid + "|Calluuid:" + calluuid);
		if (parentcalluuid == "") {
			AppLogger.logTaxiorder.info(" Error Dialing| Publish: uuid is empty");
			return;
		}

		publishCallCenterEvent(destination, parentcalluuid, new Timestamp(System.currentTimeMillis()), caller,
				callCenterId, CallInfo.CALL_EVENT_DIALING);

		AppLogger.logTaxiorder.info("2.1 Start| Publish:" + caller + "|extension:" + destination);
	}

	private String handleStartEvent(String callCenterId, String secret, String callStatus, String calluuid,
			String direction, String caller, String destination) {

		AppLogger.logTaxiorder.info("1. Start|PhoneNumber:" + caller + "|Calluuid:" + calluuid);

		return "";
	}

	private String handleDialAnswerEvent(String callCenterId, String secret, String callStatus, String calluuid,
			String caller, String destination) {

		AppLogger.logTaxiorder.info("3. DialAnswer|PhoneNumber:" + caller + "|Calluuid:" + calluuid);

		if (calluuid == "") {
			AppLogger.logTaxiorder.info(" Error Answer| Publish: uuid is empty");
			return "";
		}

		publishCallCenterEvent(destination, calluuid, new Timestamp(System.currentTimeMillis()), caller, callCenterId,
				CallInfo.CALL_EVENT_ANSWER);

		AppLogger.logTaxiorder.info("3.1 DialAnswer| Publish:" + caller + "|Calluuid:" + calluuid);

		return "";
	}

	private void handleTrimEvent(String callCenterId, String secret, String callStatus, String calluuid,
			String parentcalluuid) {

		AppLogger.logTaxiorder.info("4. Trim|Calluuid:" + parentcalluuid + "|Status:" + callStatus);
		if (parentcalluuid == null || parentcalluuid.trim() == "") {
			AppLogger.logTaxiorder.info(" Error Trim| Publish: uuid is empty");
			return;
		}

		publishCallCenterEvent("", parentcalluuid, new Timestamp(System.currentTimeMillis()), "", callCenterId,
				CallInfo.CALL_EVENT_TRIM);

		AppLogger.logTaxiorder.info("4.1. trim publish: calluuid:");
	}

	private String handleHangUpEvent(String callCenterId, String secret, String callStatus, String calluuid,
			String dateReceive, String causeTxt, String destination) {

		AppLogger.logTaxiorder.info("5. HangUp|Calluuid:" + calluuid + "|Status:" + callStatus);
		if (calluuid == null || calluuid.trim() == "") {
			AppLogger.logTaxiorder.info(" Error Hangup| Publish: uuid is empty");
			return "";
		}

		publishCallCenterEvent("", calluuid, new Timestamp(System.currentTimeMillis()), "", callCenterId,
				CallInfo.CALL_EVENT_TRIM);

		AppLogger.logTaxiorder.info("5.1 HangUp publish:" + calluuid + "|Status:" + callStatus);
		return "";

	}

	private void handleCdrEvent(String secret, String calluuid, String startTime, String answerTime, String endTime,
			String billduration, String totalduration) {
	}

	public static String makeACall(String number, String ext) {
		String result = "400";
		String curUrl = CallCenterCommon.getCurrentUrlCallCenter();
		if (curUrl != null) {
			String url = curUrl + "externalcrm/makecall2.php";
			Map<String, Object> mapParam = new HashMap<>();
			mapParam.put("callernum", ext);
			mapParam.put("destnum", number);
			mapParam.put("secrect", secretKey);
			result = CallInWS.doGet(url, mapParam);
		}
		return result;
	}

	public static String transferCall(String uuid, String ext) {
		String result = "400";
		String curUrl = CallCenterCommon.getCurrentUrlCallCenter();
		if (curUrl != null) {
			String url = curUrl + "externalcrm/transfercall2.php";
			Map<String, Object> mapParam = new HashMap<>();
			mapParam.put("uuid", uuid);
			mapParam.put("targetextension", ext);
			mapParam.put("type", "blind ");
			mapParam.put("secrect", secretKey);
			result = CallInWS.doGet(url, mapParam);
		}
		return result;
	}

	private void publishCallCenterEvent(String extension, String callUuid, Timestamp time, String callerNumber,
			String callCenterId, int event) {
		CallInfo callInfo = new CallInfo(extension, callUuid, time, callerNumber, callCenterId, event);
		TaxiOrderMQ.pushCallInDial(new Event(TaxiOrderMQ.SERVER_PUSH_EVENT_QUEUE_DIAL, null, callInfo));
	}
}