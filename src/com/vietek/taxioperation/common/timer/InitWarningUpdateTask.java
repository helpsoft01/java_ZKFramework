package com.vietek.taxioperation.common.timer;

import java.util.TimerTask;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.trackingOnline.common.VietekWarning;

public class InitWarningUpdateTask extends TimerTask {

	@Override
	public void run() {
		try {
			int count = 0;
			int countupdate = 0;

			for (Vehicle vehicle : MapCommon.MAP_VEHICLE_ID.values()) {
				Integer vehicleid = vehicle.getId();
				if (MapCommon.MAP_VIETEK_WARNING.containsKey(vehicleid)) {
					MapCommon.MAP_VIETEK_WARNING.get(vehicleid).updateConfigVietekWarning(vehicle);;
					countupdate++;
				} else {
					VietekWarning vwarning = new VietekWarning(vehicle);
					MapCommon.MAP_VIETEK_WARNING.put(vehicleid, vwarning);
					count++;
				}
			}
			AppLogger.logVWarning.info("Adds:" + count + "| Updates : " + countupdate);
			AppLogger.logVWarning.info("Size Info:" + "| listVehicle:" + MapCommon.MAP_VEHICLE_ID.size()
					+ "| list Warning : " + MapCommon.MAP_VIETEK_WARNING.size());

		} catch (Exception e) {
			AppLogger.logVWarning.error(e);
		}
	}

}
