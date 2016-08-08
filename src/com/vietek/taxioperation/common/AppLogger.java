package com.vietek.taxioperation.common;

import org.apache.log4j.Logger;

/**
 * 
 * @author DoVu Thiet lap va cau hinh cho log
 *
 */
public class AppLogger {
	public static Logger logUserAction = Logger.getLogger(CommonDefine.LOG_USER_ACTION);
	public static Logger logDebug = Logger.getLogger(CommonDefine.LOG_DEBUG);
	public static Logger logTaxiorder = Logger.getLogger(CommonDefine.LOG_DEBUG_TAXIORDER);
	public static Logger logTracking = Logger.getLogger(CommonDefine.LOG_TRACKING);
	public static Logger logMap = Logger.getLogger(CommonDefine.LOG_VMAP);
	public static Logger logVWarning = Logger.getLogger(CommonDefine.LOG_V_WARNING);
}
