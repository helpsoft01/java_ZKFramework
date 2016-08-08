package com.vietek.taxioperation.realtime.command.driver;

import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class DriverInStartPositionCommand extends AbstractCommand {
	public static final String COMMAND = "$driver_in_start_position=";
	public DriverInStartPositionCommand() {
		super(COMMAND);
	}

	@Override
	public void parseData() {
		super.parseData();
	}
	@Override
	public void processData() {
		super.processData();
		Taxi taxi = (Taxi)this.getDevice();
		if (taxi != null && taxi.getTrip() != null) {
			Trip trip = taxi.getTrip();
			trip.sendDriverInStartPositionNotify();
		}
	}
}
