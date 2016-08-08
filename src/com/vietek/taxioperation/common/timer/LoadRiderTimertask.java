package com.vietek.taxioperation.common.timer;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.MonitorCommon;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.LogRiderOnlineByHour;
import com.vietek.taxioperation.realtime.Rider;

/**
 *
 * @author VuD
 */
public class LoadRiderTimertask extends TimerTask {

	public static int oldeHour;

	public LoadRiderTimertask() {
		super();
	}

	@Override
	public void run() {
		LocalTime curTime = LocalTime.now();
		int hour = curTime.getHour();
		try {
			ArrayList<String> lstTmp;
			if (hour != oldeHour) {
				ArrayList<String> lstLast = Rider.lstCustomerByHour.getLast();
				InsertRiderOnlineByHour insertRiderOnlineByHour = new InsertRiderOnlineByHour(lstLast.size());
				insertRiderOnlineByHour.start();
				Rider.lstCustomerByHour.removeFirst();
				lstTmp = new ArrayList<>();
				Rider.lstCustomerByHour.addLast(lstTmp);
				oldeHour = hour;
			} else {
				lstTmp = Rider.lstCustomerByHour.getLast();
			}
			Rider.lstRiderOnline.clear();
			Iterator<String> ite = Rider.mapRider.keySet().iterator();
			while (ite.hasNext()) {
				String key = (String) ite.next();
				Rider value = Rider.mapRider.get(key);
				if (value != null) {
					if (value.isConnect()) {
						Rider.lstRiderOnline.add(value);
						Customer cus = value.getCustomer();
						if (cus != null) {
							if (!lstTmp.contains(cus.getPhoneNumber())) {
								lstTmp.add(cus.getPhoneNumber());
							}
						}
					}
				}

			}
		} catch (Exception e) {
			AppLogger.logDebug.error(MonitorCommon.getSystemCpuInfo(), e);
		} finally {
		}
	}

	private class InsertRiderOnlineByHour extends Thread {
		private final int value;

		public InsertRiderOnlineByHour(int value) {
			super();
			this.setName("InsertRiderOnlineByHour");
			this.value = value;
		}

		@Override
		public void run() {
			try {
				LogRiderOnlineByHour logRiderOnlineByHour = new LogRiderOnlineByHour();
				logRiderOnlineByHour.setValue(value);
				logRiderOnlineByHour.save();
			} catch (Exception e) {
				AppLogger.logUserAction.error("value:" + value, e);
			}
		}
	}
}
