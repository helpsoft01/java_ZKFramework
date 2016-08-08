package com.vietek.taxioperation.realtime.command.driver;

import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class DriverCancelRegisterMarketingPlace extends AbstractCommand{
	public static final String COMMAND = "$diver_cancel_marketing_place=";
	public DriverCancelRegisterMarketingPlace() {
		super(COMMAND);
	}
	
	@Override
	public void processData() {
		super.processData();
		Taxi taxi = (Taxi) this.getDevice();
		taxi.goOutTaxiQueue();
		taxi.driverGetMarketingPlaceInfo();
	}
}
