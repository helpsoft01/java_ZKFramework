package com.vietek.trackingOnline.common;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.trackingOnline.tracker.TrackingRDS2Json;

public class TrackingCommon {
	public static List<TrackingRDS2Json> getListTrackingByListVehicle(List<Vehicle> lstVehicle){
		List<TrackingRDS2Json> lst = new ArrayList<TrackingRDS2Json>();
		if(!MapCommon.TRACKING_RDS.isEmpty() && lstVehicle != null && lstVehicle.size() > 0){
			for (Vehicle vehicle : lstVehicle) {
				TrackingRDS2Json bean = new TrackingRDS2Json();
				bean = MapCommon.TRACKING_RDS.get(vehicle.getDeviceID());
				if(bean != null){
					lst.add(bean);
				}
			}
		}
		return lst; 
	}
	
}
