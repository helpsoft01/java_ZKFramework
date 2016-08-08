package com.vietek.taxioperation.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.vietek.taxioperation.model.GpsServerPickupMsg;
import com.vietek.taxioperation.model.GpsServerTripDoneMsg;
import com.vietek.taxioperation.model.LoggingUserAction;

/**
 *
 * @author VuD
 */
public class QueCommon {

	/**
	 * Queue chua ban tin don khach tu GpsServer day sang
	 */
	public static final BlockingQueue<GpsServerPickupMsg> quePickupMsg = new ArrayBlockingQueue<>(200000);

	/**
	 * Queue chua ban tin tra khach tu GpsServer day sang
	 */
	public static final BlockingQueue<GpsServerTripDoneMsg> queTripDoneMsg = new ArrayBlockingQueue<>(200000);
	
	public static final BlockingQueue<LoggingUserAction> queueLoggingUserAction = new ArrayBlockingQueue<>(200000);
}
