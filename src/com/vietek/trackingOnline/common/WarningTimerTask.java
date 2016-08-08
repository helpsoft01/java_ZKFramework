package com.vietek.trackingOnline.common;

import java.util.TimerTask;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.trackingOnline.tracker.TrackingRDS2Json;

public class WarningTimerTask extends TimerTask {

	@Override
	public void run() {
		try {
			for (Integer vehicleid : MapCommon.MAP_VIETEK_WARNING.keySet()) {
				TrackingRDS2Json msg = MapCommon.TRACKING_RDS.get(vehicleid);
				if (msg != null && msg.getTimeLog() != null) {
					try {
						VietekWarning warning = MapCommon.MAP_VIETEK_WARNING.get(vehicleid);
						warning.executeVietekWarning(msg);
					} catch (Exception e) {
						AppLogger.logVWarning.info(e);
					}

				} else {
				}
			}
		} catch (Exception e) {
			AppLogger.logVWarning.info(e);
		}

	}

}
