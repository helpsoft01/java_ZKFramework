package com.vietek.taxioperation.webservice;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.zkoss.zk.ui.WebApp;

import com.vietek.taxioperation.json.JsonCustomer;

@Path("/getCustomerInfo")
public class CustomerInfoWS {
	public static WebApp webApp;
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonCustomer getCustomerInfo(@QueryParam("taxiNumber") String taxiNumber){
		JsonCustomer customer = new JsonCustomer();
		customer.setId(1);
		customer.setName("Pham Anh Tuan");
		customer.setEmail("tuanpa@vietek.com.vn");
		customer.setIsVip(true);
		customer.setPhoneNumber("0945868885");
		return customer;
	}
}