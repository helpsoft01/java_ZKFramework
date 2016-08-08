package com.vietek.taxioperation.realtime.command.driver;

import java.sql.Timestamp;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.vietek.taxioperation.model.DriverAppTracking;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class DriverUpdateLocationCommand extends AbstractCommand {
	public static final String COMMAND = "$driver_update_loc=";
	public static final BlockingQueue<DriverAppTracking> LST_APPS_DRIVER_ONLINE = new LinkedBlockingQueue<DriverAppTracking>();
	private double longtitute;
	private double lattitute;
	private double angle;
	boolean check = true;
	public DriverUpdateLocationCommand() {
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
		angle = jsonObj.getJsonNumber("angle").doubleValue();
	}


	@Override
	public void processData() {
		super.processData();
		Taxi taxi = (Taxi)this.getDevice();
		if (taxi != null) {
			taxi.updateLocation(lattitute, longtitute, angle);
		}
		updateQueue(taxi);
	}
	
	private void updateQueue(Taxi taxi){
		DriverAppTracking bean = new DriverAppTracking();
		if(taxi != null && taxi.getLattitute() > 0 && taxi.getLongitude() > 0){
			bean.setDriver(taxi.getDriver());
			bean.setAngle(taxi.getAngle());
			bean.setLatitude(taxi.getLattitute());
			bean.setLongitude(taxi.getLongitude());
			bean.setTime(new Timestamp(System.currentTimeMillis()));
			if(taxi.getTrip() == null){
				bean.setStatus(0);
				bean.setOrder(null);
			}else{
				bean.setOrder(taxi.getTrip().getOrder());
				bean.setStatus(taxi.getTrip().getOrder().getStatus());
			}
			DriverUpdateLocationCommand.LST_APPS_DRIVER_ONLINE.add(bean);
		}
	}
	
	
	
	
	
}
