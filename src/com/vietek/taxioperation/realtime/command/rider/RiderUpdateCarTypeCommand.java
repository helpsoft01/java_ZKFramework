package com.vietek.taxioperation.realtime.command.rider;

import org.apache.log4j.Logger;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class RiderUpdateCarTypeCommand extends AbstractCommand {
	final static Logger logger = Logger.getLogger(CommonDefine.LOG_DEBUG);
	public static final String COMMAND = "$rider_update_car_type=";
	public RiderUpdateCarTypeCommand() {
		super(COMMAND);
	}
	
	private int carType;

	@Override
	public void parseData() {
		super.parseData();
		carType = jsonObj.getInt("carType");
	}

	@Override
	public void processData() {
		super.processData();
		((Rider)this.getDevice()).setCarType(carType);
		((Rider)this.getDevice()).updateInfoToRider();
	}
}

