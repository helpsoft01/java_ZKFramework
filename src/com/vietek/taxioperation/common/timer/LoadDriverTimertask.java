package com.vietek.taxioperation.common.timer;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.model.LogDriverOnlineByHour;
import com.vietek.taxioperation.realtime.Taxi;

/**
 *
 * @author VuD
 */
public class LoadDriverTimertask extends TimerTask {
	public static int oldeHour;

	public LoadDriverTimertask() {
		super();
	}

	@Override
	public void run() {
		LocalTime curTime = LocalTime.now();
		int hour = curTime.getHour();
		try {
			Taxi.LST_TAXI_ONLINE.clear();
			ArrayList<String> lstTmp;
			if (hour != oldeHour) {
				ArrayList<String> lstLast = Taxi.LST_DRIVER_BY_HOUR.getLast();
				InsertLogDriverOnlineByHour insertLogDriverOnlineByHour = new InsertLogDriverOnlineByHour(
						lstLast.size());
				insertLogDriverOnlineByHour.start();
				Taxi.LST_DRIVER_BY_HOUR.removeFirst();
				lstTmp = new ArrayList<>();
				Taxi.LST_DRIVER_BY_HOUR.addLast(lstTmp);
				oldeHour = hour;
			} else {
				lstTmp = Taxi.LST_DRIVER_BY_HOUR.getLast();
			}
			Iterator<String> ite = Taxi.mapDriver.keySet().iterator();
			while (ite.hasNext()) {
				String key = (String) ite.next();
				Taxi taxi = Taxi.mapDriver.get(key);
				if (taxi != null) {
					if (taxi.isConnect()) {
						Taxi.LST_TAXI_ONLINE.add(taxi);
						if (!lstTmp.contains(key)) {
							lstTmp.add(key);
						}
					}
				}

			}
		} catch (Exception e) {
			AppLogger.logDebug.error("LoadDriverTimertask", e);
		} finally {
		}
	}

	private class InsertLogDriverOnlineByHour extends Thread {
		private final int value;

		public InsertLogDriverOnlineByHour(int value) {
			super();
			this.setName("InsertLogDriverOnlineByHour");
			this.value = value;
		}

		@Override
		public void run() {
			try {
				LogDriverOnlineByHour logDriverOnlineByHour = new LogDriverOnlineByHour();
				logDriverOnlineByHour.setValue(value);
				logDriverOnlineByHour.save();
			} catch (Exception e) {
				AppLogger.logUserAction.error("value: " + value, e);
			}
		}
	}
}
