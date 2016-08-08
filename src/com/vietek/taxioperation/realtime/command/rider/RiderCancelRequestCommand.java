package com.vietek.taxioperation.realtime.command.rider;

import javax.json.Json;
import javax.json.JsonObject;

import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.realtime.command.AbstractCommand;
import com.vietek.taxioperation.realtime.command.MobileCommand;

public class RiderCancelRequestCommand extends AbstractCommand {
	public static final String COMMAND = "$rider_cancel=";
	public static final String COMMAND_RETURN = "$rider_cancel_return=";
	private String trip_id;
	public RiderCancelRequestCommand() {
		super(COMMAND);
	}

	@Override
	public void parseData() {
		super.parseData();
		trip_id = jsonObj.getString("trip_id");
	}

	@Override
	public void processData() {
		Rider rider = (Rider)this.getDevice();
		Trip trip = TripManager.sharedInstance.getTrip(trip_id);
		int status = 0;
		String message = trip.cancelTrip(1);
		if (message != null) {
			status  = -1;
		}
		else {
			message = "";
		}
		
		JsonObject jsonObj = Json.createObjectBuilder()
				.add("status", status)
				.add("trip_id", trip.getId())
				.add("message", message)
				.build();
		
		String cmd = COMMAND_RETURN + jsonObj + MobileCommand.END_CMD;
		rider.sendToDevice(cmd);
	}
}
