package com.vietek.taxioperation.realtime.command.rider;

import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class RiderUpdateLocationCommand extends AbstractCommand {
	public static final String COMMAND = "$rider_update_loc=";
	private double longtitute;
	private double lattitute;
	public RiderUpdateLocationCommand() {
		super(COMMAND);
	}
	
	/**
	 * data should be: longtitute&lattitute
	 */
	@Override
	public void parseData() {
		super.parseData();
		longtitute = jsonObj.getJsonNumber("long").doubleValue();
		lattitute = jsonObj.getJsonNumber("lat").doubleValue();
	}


	@Override
	public void processData() {
		super.processData();
		Rider rider = (Rider)this.getDevice();
//		rider.setLongtitute(longtitute);
//		rider.setLattitute(lattitute);
		rider.updateLocation(longtitute, lattitute);
		rider.updateInfoToRider();
	}
}
