package com.vietek.taxioperation.realtime;

import java.io.StringReader;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.NewCookie;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.vietek.taxioperation.common.AppLogger;

public class TestWS {

	public static void main(String[] args) {
		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		String postURL = "https://api.open99.vn/api/init";
		WebResource webResource = client.resource(postURL);
		JsonObject data = Json.createObjectBuilder()
				.add("appId", "vietek")
				.add("secretKey", "0487ff61-e32e-4724-891a-e27111d7e6d1")
				.build();
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, data.toString());
		
		if (response.getStatus() == 200) {
			List<NewCookie> coockies = response.getCookies();
			postURL = "https://api.open99.vn/api/taxi";
			webResource = client.resource(postURL);
			data = Json.createObjectBuilder()
					.add("serviceName", "GET_LIST_VEHICLE")
					.add("latitude", "20.9960144")
					.add("radius", "5")
					.add("type", "all")
					.add("longitude", "105.8619591")
					.build();
			WebResource.Builder builder = webResource.getRequestBuilder();
			for (NewCookie c : coockies) {
			    builder = builder.cookie(c);
			}
			response = builder.post(ClientResponse.class, data.toString());
			if (response.getStatus() == 200) {
				coockies = response.getCookies();
				String ret = response.getEntity(String.class);
				JsonReader reader = Json.createReader(new StringReader(ret));
				JsonObject jsonObj = reader.readObject();
				AppLogger.logDebug.info(jsonObj.toString());
			}
		}
	}

}
