package com.vietek.taxioperation.realtime.command.driver;

import java.util.concurrent.BlockingQueue;

import com.vietek.taxioperation.model.DriverAppTracking;
import com.vietek.taxioperation.realtime.Taxi;

public class DriverUpdateLocationAppTracking implements Runnable{
	@SuppressWarnings("unused")
	private BlockingQueue<DriverAppTracking> tracking;
	private Taxi taxi;
	public DriverUpdateLocationAppTracking(BlockingQueue<DriverAppTracking> queue,Taxi temp) {
		this.tracking = queue;
		this.taxi = temp;
	}
	@Override
	public void run() {
		updateQueue(taxi);
	}
	
	private void updateQueue(Taxi taxi){
		DriverAppTracking bean = new DriverAppTracking();
		if(taxi != null){
			bean.setDriver(taxi.getDriver());
			bean.setAngle(taxi.getAngle());
			bean.setLatitude(taxi.getLattitute());
			bean.setLongitude(taxi.getLongitude());
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
