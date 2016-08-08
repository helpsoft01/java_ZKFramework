package com.vietek.taxioperation.realtime.command.driver;

import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class DriverPickUpGuestCommand extends AbstractCommand{
	public static final String COMMAND = "$pick_up_guest=";
	private double longtitute;
	private double lattitute;
	private String address;
	public DriverPickUpGuestCommand() {
		super(COMMAND);
	}
	
	@Override
	public void parseData() {
		super.parseData();
		longtitute = jsonObj.getJsonNumber("beginOrderLong").doubleValue();
		lattitute = jsonObj.getJsonNumber("beginOrderLat").doubleValue();
		address = jsonObj.getString("beginOrderAddr");
	}
	
	@Override
	public void processData() {
		super.processData();
		Taxi taxi = (Taxi) this.getDevice();
		taxi.pickupRider(address, lattitute, longtitute);
	}
}
