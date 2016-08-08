package com.vietek.taxioperation.realtime.command.rider;

import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class RiderRequestTrackingCommand extends AbstractCommand {
	public static final String COMMAND = "$rider_request_tracking=";

	public RiderRequestTrackingCommand() {
		super(COMMAND);
	}

	private String trip_id;

	@Override
	public void parseData() {
		super.parseData();
		trip_id = jsonObj.getString("trip_id");
	}

	@Override
	public void processData() {
		super.processData();
		if (trip_id.trim().length() > 0) {
			Trip trip = TripManager.sharedInstance.getTrip(trip_id);
			Rider rider = (Rider) this.getDevice();
			rider.setTrip(trip);
			trip.setRider(rider);
			trip.sendDriverInfo();
			rider.sendTripUpdate();
		} else {
			Rider rider = (Rider) this.getDevice();
			rider.setTrip(null);
		}
	}
}
