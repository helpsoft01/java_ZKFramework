package com.vietek.taxioperation.webservice.payment;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.commons.io.IOUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vietek.taxioperation.controller.PaymentCardController;
import com.vietek.taxioperation.model.PaymentCard;
import com.vietek.taxioperation.util.ControllerUtils;

import vn.com.napas.TSPServer.Configuration;
import vn.com.napas.TSPServer.TSPConnection;

@Path("MobileWS/integrate/napas")
public class NapasIntergrate {
	
	@GET
	@Path("/get/card/user/{phone}")
	public String getCardOfUser(
			@PathParam("phone") String phone) {
		ArrayList<PaymentCard> list = PaymentCardController.getToken(phone);
		int count = list.size();
		JsonArrayBuilder cardsJson = Json.createArrayBuilder();
		for (PaymentCard card : list) {
			JsonObject cardJson = Json.createObjectBuilder()
									.add("id", card.getId())
									.add("token", card.getToken())
									.add("note", card.getNote() != null ?card.getNote() : "")
									.add("cardScheme", card.getCardScheme() != null ? card.getCardScheme() : "")
									.build();
			cardsJson.add(cardJson);
		}
		JsonObject retJson = Json.createObjectBuilder()
									.add("count", count)
									.add("cards", cardsJson.build())
									.build();
		return retJson.toString();
	}
	@DELETE
	@Path("/delete/card/token/{token}")
	public String deleteToken(@PathParam("token") String token) {
		//Delete in DB
		Session session = ControllerUtils.getCurrentSession();
		Query query = session.createQuery("delete from PaymentCard where token = '" + token + "'");
		query.executeUpdate();
		session.close();
		
		//Delete in napas server
		String json = "{ " +
				  "\"apiOperation\": \"DELETE_TOKEN\"" +
				"}";
		TSPConnection tsp = new TSPConnection();
		try {
			tsp.DeleteTransaction(Configuration.merchantId, "4005550814280019", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}
	
	@GET
	@Path("/gen/user/{phone}/card_scheme/{card_scheme}")
	public String genCardInputForm(
			@PathParam("phone") String phone, 
			@PathParam("card_scheme") String cardScheme) 
	{
		TSPConnection tsp = new TSPConnection();
		String json = "{ " +
				  "\"apiOperation\": \"DATA_KEY\", " +
				  "\"inputParameters\": { " +
				    "\"clientIP\": \"192.168.1.1\"," +
				    "\"deviceId\": \"0945868885\", " +
				    "\"environment\": \"MobileApp\", " +
				    "\"cardScheme\": \""+ cardScheme + "\", " +
				    "\"enable3DSecure\": \"false\" " +
				  "} " +
				"}";
		String dataKey = null;
		try {
			String responseString = tsp.createNewTokenTransaction(Configuration.merchantId, json);
			JsonReader reader = Json.createReader(new StringReader(responseString));
			JsonObject jsonObj = reader.readObject();
			dataKey = jsonObj.getString("dataKey");
		} catch (Exception e) {
			
		}
		if (dataKey == null)
			return "";
		String actionUrl = "<merchant-defined>";
		StringBuilder formString = new StringBuilder();
		formString.append("<form id=\"merchant-form\" action=\"" + actionUrl + "\" method=\"POST\">");
		formString.append("<div id=\"napas-widget-container\"></div>");
		formString.append("<script");
		formString.append("		type=\"text/javascript\"");
		formString.append("		id=\"napas-widget-script\"");
		formString.append("		src=\"http://test-gateway.banknetvn.com.vn/api/restjs/resources/js/napas.hostedform.min.js\"");
		formString.append("		merchantId=\"" + Configuration.merchantId + "\"");
		formString.append("		clientIP=\"192.168.1.1\"");
		formString.append("		deviceId=\"" + phone + "\"");
		formString.append("		environment=\"MobileApp\"");
		formString.append("		cardScheme=\"" + cardScheme +"\"");
		formString.append("		enable3DSecure=\"false\" ");
		formString.append("dataKey=\"");
		formString.append(dataKey + "\"");
		formString.append(">");
		formString.append("</script>");
		formString.append("</form>");
		
		return formString.toString();
	}
	
	@POST
	@Path("/gen/token/user/{phone}/card_scheme/{card_scheme}")
	public String didGenerateToken(@PathParam("phone") String phone, 
			@PathParam("card_scheme") String cardScheme, InputStream incomingData) {
		String retVal = "<h1>ok</h1>";
		String token = "";
		try {
			token = IOUtils.toString(incomingData, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		token = token.replaceFirst("token=", "");
		if (!PaymentCardController.createToken(phone, token, cardScheme, null)) {
			retVal = "<h1>error</h1>";
		}
		
		return retVal;
	}
}
