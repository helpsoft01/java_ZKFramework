package com.vietek.taxioperation.realtime.command.driver;

import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class DriverConfirmTrip extends AbstractCommand {
	public static final String COMMAND = "$driver_confirm_trip=";
	private String trip_id;

	public DriverConfirmTrip() {
		super(COMMAND);
	}

	@Override
	public void parseData() {
		super.parseData();
		trip_id = jsonObj.getString("trip_id");
	}

	@Override
	public void processData() {
		super.processData();
		Taxi taxi = (Taxi)this.getDevice();
		Trip trip = TripManager.sharedInstance.getTrip(trip_id);
		trip.driverConfirmTrip(taxi);
	}
}
