package com.vietek.taxioperation.realtime.command.driver;

import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.command.AbstractCommand;
import com.vietek.taxioperation.realtime.command.MobileCommand;

public class DriverCancelTripCommand extends AbstractCommand {
	public static final String COMMAND = "$driver_cancel_trip=";
	private int reason;
	public DriverCancelTripCommand() {
		super(COMMAND);
	}

	@Override
	public void parseData() {
		super.parseData();
		reason = jsonObj.getInt("reason");
	}
	@Override
	public void processData() {
		super.processData();
		Taxi taxi = (Taxi)this.getDevice();
		taxi.sendToDevice("$return_driver_cancel_trip={}" + MobileCommand.END_CMD);
		if (taxi != null && taxi.getTrip() != null) {
			Trip trip = taxi.getTrip();
			trip.cancelTrip(reason);
		}
	}
}
