package com.vietek.taxioperation.common;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

/**
 *
 * @author VuD
 */
public class MonitorCommon {
	
	private static OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

	public static double getSystemCpuUsage() {
		return Math.round(osBean.getSystemCpuLoad() * 10000d) / 100d;
	}

	public static double getJavaCpuUsage() {
		return Math.round(osBean.getProcessCpuLoad() * 10000d) / 100d;
	}

	public static String getSystemCpuInfo() {
		return "System:" + Math.round(osBean.getSystemCpuLoad() * 10000d) / 100d + "|Java:"
				+ Math.round(osBean.getProcessCpuLoad() * 10000d) / 100d + "|";
	}
}
