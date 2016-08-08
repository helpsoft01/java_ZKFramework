package com.vietek.taxioperation.realtime.command.driver;

import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class DriverGetInfoMarketingPlace extends AbstractCommand{
	public static final String COMMAND = "$diver_get_info_marketing_place=";
	
	
	public DriverGetInfoMarketingPlace() {
		super(COMMAND);
	}
	
	@Override
	public void parseData(){
		super.parseData();
	}
	
	@Override
	public void processData() {
		super.processData();
		Taxi taxi = (Taxi) this.getDevice();
		taxi.driverGetMarketingPlaceInfo();
	}
}
