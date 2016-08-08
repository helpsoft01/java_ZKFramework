package com.vietek.taxioperation.realtime.command.rider;

import javax.json.Json;
import javax.json.JsonObject;

import com.vietek.taxioperation.common.EnumOrderType;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.realtime.command.AbstractCommand;
import com.vietek.taxioperation.realtime.command.MobileCommand;

public class RiderRequestTripCommand extends AbstractCommand {
	public static final String COMMAND = "$rider_request_trip=";
	public static final String COMMAND_RETURN = "$rider_request_trip_return=";
	public RiderRequestTripCommand() {
		super(COMMAND);
	}
	
	private String beginOrderAddress;
	private double beginOrderLat;
	private double beginOrderLong;
	private String endOrderAddress;
	private double endOrderLat;
	private double endOrderLong;
	private String beginOrderTime;
	private String note;
	private boolean isAirport;
	
	@Override
	public void parseData() {
		super.parseData();
		beginOrderAddress = jsonObj.getString("beginOrderAddr");
		beginOrderLat = jsonObj.getJsonNumber("beginOrderLat").doubleValue();
		beginOrderLong = jsonObj.getJsonNumber("beginOrderLong").doubleValue();
		endOrderAddress = jsonObj.getString("endOrderAddr");
		endOrderLat = jsonObj.getJsonNumber("endOrderLat").doubleValue();
		endOrderLong = jsonObj.getJsonNumber("endOrderLong").doubleValue();
		beginOrderTime = jsonObj.getString("beginOrderTime");
		try {
			note = jsonObj.getString("note");
		}
		catch (Exception e) {
			note = "";
		}
		try {
			isAirport = jsonObj.getBoolean("isAirport");
		}
		catch (Exception e) {
			isAirport = false;
		}
	}

	@Override
	public void processData() {
		Rider rider = (Rider)this.getDevice();
		TaxiOrder order = TaxiOrder.createNewTaxiOrder(
				rider.getCustomer(),
				beginOrderAddress, 
				beginOrderLat, 
				beginOrderLong, 
				endOrderAddress, 
				endOrderLong,
				endOrderLat, 
				beginOrderTime,
				note,
				EnumOrderType.APP_SMARTPHONE.getValue(),
				isAirport);
		if (order != null) {
			Trip trip = new Trip();
//			if(rider.getCarType() == 3){ // In case order air plane 
//				order.setOrderCarType(0);
//			}else{
			order.setOrderCarType(rider.getCarType());
//			}
			trip.setOrder(order);
			rider.setTrip(trip);
			TripManager.sharedInstance.addTrip(trip, rider);
			JsonObject jsonObj = Json.createObjectBuilder()
					.add("status", 0)
					.add("trip_id", trip.getId())
					.build();
			String cmd = COMMAND_RETURN + jsonObj + MobileCommand.END_CMD;
			rider.sendToDevice(cmd);
		}
	}
}
