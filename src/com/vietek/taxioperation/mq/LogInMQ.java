package com.vietek.taxioperation.mq;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class LogInMQ {
	public static String LOG_IN_EVENT = "log_in_event";
	private static String LOG_IN_MQ_NAME = "log_in_mq";
	public static void publish(Event event) {
		GlobalMQ.publish(LOG_IN_MQ_NAME, event);
	}
	
	public static void subcrible(EventListener<Event> eventListener) {
		GlobalMQ.subcrible(LOG_IN_MQ_NAME, eventListener);
	}
	
	public static void unsubcrible(EventListener<Event> eventListener) {
		
		GlobalMQ.unsubcrible(LOG_IN_MQ_NAME, eventListener);
	}
}
