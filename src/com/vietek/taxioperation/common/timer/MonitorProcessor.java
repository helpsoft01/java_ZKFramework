package com.vietek.taxioperation.common.timer;

import java.lang.management.ManagementFactory;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sun.management.OperatingSystemMXBean;
import com.vietek.taxioperation.common.CommonDefine;

public class MonitorProcessor extends Thread {
	public static final Date startAppTime = new Date();
	private final String name;
	private Logger logger;
	private final int mb = 1024 * 1024;
	private Runtime runtime;
	private OperatingSystemMXBean osBean;
	private long timeSleep;

	public MonitorProcessor() {
		super();
		this.name = "MonitorProcessor";
		this.timeSleep = 300000;
		// this.timeSleep = 10000;
		this.setName(name);
		this.logger = Logger.getLogger(CommonDefine.LOG_USER_ACTION);
		this.osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	}

	@Override
	public void run() {
		logger.info("StartSystem: " + startAppTime);
		while (!interrupted()) {
			try {
				runtime = Runtime.getRuntime();
				logger.info("{" + name + "}--->Start");
				logger.info("[" + name + "] - System usage CPU: "
						+ (Math.round(osBean.getSystemCpuLoad() * 10000d) / 100d));
				logger.info(
						"[" + name + "] - Java usage CPU: " + (Math.round(osBean.getProcessCpuLoad() * 10000d) / 100d));
				logger.info("[" + name + "] - Active thread: " + Thread.activeCount());
				logger.info(
						"[" + name + "] - System total memory: " + (osBean.getTotalPhysicalMemorySize() / mb) + "MB");
				logger.info("[" + name + "] - Java maximum memory: " + (runtime.maxMemory() / mb) + "MB");
				logger.info("[" + name + "] - Java total memory: " + (runtime.totalMemory() / mb) + "MB");
				logger.info("[" + name + "] - Java used memory: "
						+ ((runtime.totalMemory() - runtime.freeMemory()) / mb) + "MB");
				logger.info("[" + name + "] - Java free memory: " + (runtime.freeMemory() / mb) + "MB");
				logger.info("------------------------------------------------------------------");
			} catch (Exception e) {
				logger.error("", e);
			} finally {
				try {
					sleep(timeSleep);
				} catch (InterruptedException e) {
					logger.error("Sleep", e);
				}
			}
		}

	}

}
