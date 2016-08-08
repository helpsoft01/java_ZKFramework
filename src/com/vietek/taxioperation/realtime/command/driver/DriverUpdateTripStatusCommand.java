package com.vietek.taxioperation.realtime.command.driver;

import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class DriverUpdateTripStatusCommand extends AbstractCommand {
	public static final String COMMAND = "$update_trip_status=";
	private int status;
	private int reason;
	private String trip_id;
	public DriverUpdateTripStatusCommand() {
		super(COMMAND);
	}

	@Override
	public void parseData() {
		super.parseData();
		trip_id = jsonObj.getString("trip_id");
		status = jsonObj.getInt("status");
		reason = jsonObj.getInt("reason");
	}
	@Override
	public void processData() {
		super.processData();
		Taxi taxi = (Taxi)this.getDevice();
		if (taxi == null || taxi.getTrip() == null)
			return;
		Trip trip = taxi.getTrip();
		if (!trip.getId().equalsIgnoreCase(trip_id))
			return;
		trip.setTaxi(taxi);
		if (trip != null) {
			trip.updateStatus(status, reason);
		}
	}
}
