package com.vietek.taxioperation.webservice;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.TaxiOrderController;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.util.ControllerUtils;

@Path("/WebAppService")
public class WebPassengerSV {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/postOrderToTMS")
	public PostTaxiOrderResult postTaxiOrder(
			@QueryParam("orderid") String orderId,
			@QueryParam("phonenumber") String phoneNumber,
			@QueryParam("bOAddress") String beginOrderAddress,
			@QueryParam("bOLatitude") String beginOrderLatitude,
			@QueryParam("bOLongitude") String beginOrderLongitude,
			@QueryParam("eOAddress") String endOrdeAddress,
			@QueryParam("eOLongitude") String endOrderLongitude,
			@QueryParam("eOLatitude") String endOrderLatitude,
			@QueryParam("beginOrderTime") String beginOrderTime,
			@QueryParam("twoWay") String twoWay,
			@QueryParam("fixedPrice") String fixedPrice,
			@QueryParam("somePerson") String SomePerson,
			@QueryParam("airStation") String airstation,
			@QueryParam("vhType1") String vehicletype1,
			@QueryParam("vhType2") String vehicletype2,
			@QueryParam("note") String note) {
		TaxiOrder order = TaxiOrder.createNewTaxiOrder(orderId, phoneNumber,
				beginOrderAddress, beginOrderLatitude, beginOrderLongitude,
				endOrdeAddress, endOrderLongitude, endOrderLatitude,
				beginOrderTime, twoWay, fixedPrice, SomePerson, airstation,
				vehicletype1, vehicletype2, note);

		Map<String, Integer> mapvehicletype1 = StringUtils
				.mapFromString(vehicletype1);
		Rider rider = Rider.getRider(order.getCustomer().getPhoneNumber());
		rider.setCarType(mapvehicletype1.get("Typeid"));
		Trip trip = new Trip();
		trip.setRider(rider);
		trip.setOrder((TaxiOrder) order);
		TripManager.sharedInstance.addTrip(trip, rider);
		return new PostTaxiOrderResult(order.getId());
	}

	class PostTaxiOrderResult {
		private int tripID;

		public PostTaxiOrderResult(int tripID) {
			this.tripID = tripID;
		}

		public int getTripID() {
			return tripID;
		}

		public void setTripID(int tripID) {
			this.tripID = tripID;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/PostCancelOrder")
	public int PostCancelOrder(@QueryParam("orderid") int orderid) {
		int result = 0;
		TaxiOrderController taxiordercontroller = (TaxiOrderController) ControllerUtils
				.getController(TaxiOrderController.class);

		List<TaxiOrder> lsttaxiorder = taxiordercontroller.find(
				"from TaxiOrder where id = ?", orderid);
		if (lsttaxiorder != null & lsttaxiorder.size() > 0) {
			TaxiOrder order = lsttaxiorder.get(0);
			result = 1;
		}
		return result;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sendRate")
	public int sendRate(@QueryParam("tripid") int tripid,
			@QueryParam("rate") int rate) {
		int result = 0;
		TaxiOrderController taxiordercontroller = (TaxiOrderController) ControllerUtils
				.getController(TaxiOrderController.class);

		List<TaxiOrder> lsttaxiorder = taxiordercontroller.find(
				"from TaxiOrder where id = ?", tripid);
		if (lsttaxiorder != null && lsttaxiorder.size() > 0) {
			TaxiOrder order = lsttaxiorder.get(0);
			order.setRate(rate);
			order.save();
			result = 1;
		}
		return result;
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getRate")
	public int getRate(@QueryParam("tripid") int tripid) {
		int result = 0;
		TaxiOrderController taxiordercontroller = (TaxiOrderController) ControllerUtils
				.getController(TaxiOrderController.class);

		List<TaxiOrder> lsttaxiorder = taxiordercontroller.find(
				"from TaxiOrder where id = ?", tripid);
		if (lsttaxiorder != null && lsttaxiorder.size() > 0) {
			TaxiOrder order = lsttaxiorder.get(0);
			result = order.getRate();
		}
		return result;
	}

}
