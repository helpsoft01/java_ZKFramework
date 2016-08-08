package com.vietek.taxioperation.common.timer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServlet;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.realtime.command.driver.DriverAppTrackingUpdateToDB;
import com.vietek.taxioperation.realtime.command.driver.DriverUpdateLocationCommand;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.SearchAbbreviationUtils;

/**
 *
 * @author VuD
 */
public class RunCommon extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RunCommon() {
		RunCommonTimer runCommonTimer = new RunCommonTimer();
		runCommonTimer.start();
		// Tuanpa
		SearchAbbreviationUtils.loadBVT();
		ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1);
		DriverAppTrackingUpdateToDB updateAppDriverTrackingTask = new DriverAppTrackingUpdateToDB(
				DriverUpdateLocationCommand.LST_APPS_DRIVER_ONLINE);
		timer.scheduleAtFixedRate(updateAppDriverTrackingTask, 60, 60, TimeUnit.SECONDS);

		// Load all order from DB in 45 min
		LoadAllTripFromDB allTrip = new LoadAllTripFromDB();
		allTrip.start();
		// Thread refresh map trip
		TripManagerWorker tripManager = new TripManagerWorker(TripManager.mapTrips,
				ConfigUtil.getConfig("TIMEOUT_REFFRESH_DATA_DTV", CommonDefine.DURATION_VALID_TAXIORDER));
		timer.scheduleAtFixedRate(tripManager, 0, 300, TimeUnit.SECONDS);	
	}
}
