package com.vietek.taxioperation.realtime.command.driver;

import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class DriverUpdateTripInfoCommand extends AbstractCommand {
	public static final String COMMAND = "$update_trip_info=";
	private String trip_id;
	private String beginAddr;
	private double beginLat;
	private double beginLong;
	private String endAddr;
	private double endLat;
	private double endLong;
	public DriverUpdateTripInfoCommand() {
		super(COMMAND);
	}

	@Override
	public void parseData() {
		super.parseData();
		trip_id = jsonObj.getString("trip_id");
		beginAddr = jsonObj.getString("beginAddr");
		beginLat = jsonObj.getJsonNumber("beginLat").doubleValue();
		beginLong = jsonObj.getJsonNumber("beginLong").doubleValue();
		endAddr = jsonObj.getString("endAddr");
		endLat = jsonObj.getJsonNumber("endLat").doubleValue();
		endLong = jsonObj.getJsonNumber("endLong").doubleValue();
	}
	@Override
	public void processData() {
		super.processData();
		Taxi taxi = (Taxi)this.getDevice();
		if (taxi == null)
			return;
		Trip trip = taxi.getTrip();
		if (trip == null)
			return;
		if (!trip.getId().equalsIgnoreCase(trip_id))
			return;
		trip.getOrder().setBeginOrderAddress(beginAddr);
		trip.getOrder().setBeginOrderLat(new Double (beginLat));
		trip.getOrder().setBeginOrderLon(new Double (beginLong));
		trip.getOrder().setEndOrderAddress(endAddr);
		trip.getOrder().setEndOrderLat(new Double (endLat));
		trip.getOrder().setEndOrderLon(new Double (endLong));
		trip.getOrder().setBeginAddress(beginAddr);
		trip.getOrder().setBeginLat(new Double (beginLat));
		trip.getOrder().setBeginLon(new Double (beginLong));
		trip.getOrder().setEndAddress(endAddr);
		trip.getOrder().setEndLat(new Double (endLat));
		trip.getOrder().setEndLon(new Double (endLong));
		trip.updateTripInfoToRider();
		trip.getOrder().save();
	}
}
