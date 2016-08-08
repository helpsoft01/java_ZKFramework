package com.vietek.taxioperation.common.timer;

import java.time.LocalTime;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.vietek.taxioperation.common.QueCommon;
import com.vietek.taxioperation.processor.LoadVehicleProcessor;
import com.vietek.trackingOnline.common.WarningTimerTask;

/**
 *
 * @author VuD
 */
public class RunCommonTimer extends Thread {
	@Override
	public void run() {
		try {
			MonitorProcessor monitorProcessor = new MonitorProcessor();
			monitorProcessor.start();
			LoadVehicleProcessor loadVehicleProcessor = new LoadVehicleProcessor();
			loadVehicleProcessor.start();
			LoadVehicleStatusDDProcessor loadVehicleStatusDDProcessor = new LoadVehicleStatusDDProcessor();
			loadVehicleStatusDDProcessor.start();
			ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1);
			LoadSysMenuTimertask loadSysMenuTimertask = new LoadSysMenuTimertask();
			timer.scheduleAtFixedRate(loadSysMenuTimertask, 0, 900, TimeUnit.SECONDS);
			LoadAgentTimerTask loadAgentTimerTask = new LoadAgentTimerTask();
			timer.scheduleAtFixedRate(loadAgentTimerTask, 0, 900, TimeUnit.SECONDS);
			LoadTaxiGroupTimerTask loadTaxiGroupTimerTask = new LoadTaxiGroupTimerTask();
			timer.scheduleAtFixedRate(loadTaxiGroupTimerTask, 0, 900, TimeUnit.SECONDS);
			LoadDriverInfoTimerTask loadDriverInfoTimerTask = new LoadDriverInfoTimerTask();
			timer.scheduleAtFixedRate(loadDriverInfoTimerTask, 0, 900, TimeUnit.SECONDS);
			SaveLogProcessingTimertask saveLogProcessingTimertack = new SaveLogProcessingTimertask(
					QueCommon.queueLoggingUserAction);
			timer.scheduleAtFixedRate(saveLogProcessingTimertack, 0, 120, TimeUnit.SECONDS);

			LocalTime curTime = LocalTime.now();
			int hour = curTime.getHour();
			LoadRiderTimertask.oldeHour = hour;
			LoadDriverTimertask.oldeHour = hour;
			LoadRiderTimertask loadRiderTimertask = new LoadRiderTimertask();
			timer.scheduleAtFixedRate(loadRiderTimertask, 0, 10, TimeUnit.SECONDS);
			LoadDriverTimertask loadDriverTimertask = new LoadDriverTimertask();
			timer.scheduleAtFixedRate(loadDriverTimertask, 0, 10, TimeUnit.SECONDS);
			LoadMarketinPlaceTimertask loadMarketinPlaceTimertask = new LoadMarketinPlaceTimertask();
			timer.scheduleAtFixedRate(loadMarketinPlaceTimertask, 0, 15, TimeUnit.MINUTES);
			LoadTripIsProcessingTimertask loadTripIsProcessingTimertask = new LoadTripIsProcessingTimertask();
			timer.scheduleAtFixedRate(loadTripIsProcessingTimertask, 0, 10, TimeUnit.SECONDS);
			RequestTrackingTimmerTask requestTrackingTimmerTask = new RequestTrackingTimmerTask();
			timer.scheduleAtFixedRate(requestTrackingTimmerTask, 0, 10, TimeUnit.SECONDS);
			InitWarningUpdateTask InitWarningUpdate = new InitWarningUpdateTask();
			timer.scheduleAtFixedRate(InitWarningUpdate, 0, 15, TimeUnit.MINUTES);
			WarningTimerTask cutsignalTask = new WarningTimerTask();
			timer.scheduleAtFixedRate(cutsignalTask, 0, 10, TimeUnit.SECONDS);
			LoadAllCustomerWorker loadAllCustomerWorker = new LoadAllCustomerWorker();
			loadAllCustomerWorker.start();
			UserNotificationListener userNotificationListener = new UserNotificationListener();
			userNotificationListener.StartListener();
			Thread.sleep(Long.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
