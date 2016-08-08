package com.vietek.taxioperation.realtime.command.driver;

import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class DriverRegisterMarketingPlace extends AbstractCommand{
	public static final String COMMAND = "$diver_register_marketing_place=";
	
	private double lattitude;
	private double longtitude;
	
	public DriverRegisterMarketingPlace() {
		super(COMMAND);
	}
	
	@Override
	public void parseData(){
		super.parseData();
		longtitude = jsonObj.getJsonNumber("longtitude").doubleValue();
		lattitude = jsonObj.getJsonNumber("lattitude").doubleValue();
	}
	
	@Override
	public void processData() {
		super.processData();
		Taxi taxi = (Taxi) this.getDevice();
		taxi.registerArrangementPlace(longtitude, lattitude);
	}
}
