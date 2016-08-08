package com.vietek.taxioperation.realtime.command.rider;

import javax.json.Json;
import javax.json.JsonObject;

import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.realtime.command.AbstractCommand;
import com.vietek.taxioperation.realtime.command.MobileCommand;

public class RiderRatingCommand extends AbstractCommand {
	public static final String COMMAND = "$rider_rating=";
	public static final String COMMAND_RETURN = "$rider_rating_result=";
	private String trip_id;
	private int rate;
	public RiderRatingCommand() {
		super(COMMAND);
	}

	@Override
	public void parseData() {
		super.parseData();
		trip_id = jsonObj.getString("trip_id");
		rate = jsonObj.getJsonNumber("rate").intValue();
	}

	@Override
	public void processData() {
		Rider rider = (Rider)this.getDevice();
		Trip trip = TripManager.sharedInstance.getTrip(trip_id);
		trip.getOrder().setRate(rate);
		int status = 0;

		JsonObject jsonObj = Json.createObjectBuilder()
				.add("trip_id", trip.getId())
				.add("status", status)
				.build();
		String cmd = COMMAND_RETURN + jsonObj + MobileCommand.END_CMD;
		rider.sendToDevice(cmd);
		trip.getOrder().save();
	}
}
