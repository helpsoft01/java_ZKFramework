package com.vietek.taxioperation.common.timer;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TimerTask;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.EnumStatus;
import com.vietek.taxioperation.common.MonitorCommon;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;

/**
 *
 * @author VuD
 */
public class LoadTripIsProcessingTimertask extends TimerTask {

	public LoadTripIsProcessingTimertask() {
		super();
	}

	@Override
	public void run() {
		try {
			TripManager.LST_TRIP_PROCESSING.clear();
			Iterator<String> ite = TripManager.sharedInstance.getMapTrips().keySet().iterator();
			while (ite.hasNext()) {
				String key = (String) ite.next();
				Trip trip = TripManager.sharedInstance.getMapTrips().get(key);
				if (trip != null) {
					if (trip.getOrder() != null) {
						if ((trip.getStatus() == EnumStatus.MOI.getValue()
								|| trip.getStatus() == EnumStatus.XE_DANG_KY_DON.getValue()
								|| trip.getStatus() == EnumStatus.XE_DA_DON.getValue())
								&& trip.getOrder().getIsAutoOperation()) {
							if (trip.getOrder().getPhoneNumber() != null
									&& trip.getOrder().getPhoneNumber().length() > 0) {
								TripManager.LST_TRIP_PROCESSING.add(trip);
							}
						}
					}
				}
			}
			Collections.sort(TripManager.LST_TRIP_PROCESSING, new Comparator<Trip>() {
				@Override
				public int compare(Trip o1, Trip o2) {
					try {
						int result = 0;
						if (o1.getOrder().getBeginOrderTime().getTime() > o2.getOrder().getBeginOrderTime().getTime()) {
							result = 1;
						} else if (o1.getOrder().getBeginOrderTime().getTime() < o2.getOrder().getBeginOrderTime()
								.getTime()) {
							result = -1;
						} else {
							result = 0;
						}
						return result;
					} catch (Exception e) {
						return -1;
					}
				}
			});
		} catch (Exception e) {
			AppLogger.logDebug.error(MonitorCommon.getSystemCpuInfo(), e);
		} finally {
		}
	}

}
