package com.vietek.taxioperation.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorCallIn extends ThreadPoolExecutor {

	public ExecutorCallIn() {
		super(4, // core pool size
				4, // maximunm pook size
				5, // keep alive time
				TimeUnit.SECONDS, // init alive time
				new LinkedBlockingQueue<Runnable>());
		allowCoreThreadTimeOut(true);

	}

	public void setThreads(int n) {
		setMaximumPoolSize(Math.max(1, n));
		setCorePoolSize(Math.max(1, n));
	}

}