package com.vietek.taxioperation.mq;

import java.util.concurrent.ConcurrentHashMap;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.util.Env;

public class GlobalMQ {
	private static ConcurrentHashMap<String, Integer> mapCount = new ConcurrentHashMap<>();
	
	static public void publish(String mqName, Event event) {
		if (Env.WEBAPP == null)
			return;
		EventQueues.lookup(mqName, Env.WEBAPP, true).publish(event);
	}
	
	static public void subcrible(String mqName, EventListener<Event> eventListener) {
		if (Env.WEBAPP == null)
			return;
		Integer count = mapCount.get(mqName);
		if (count == null) {
			count = 0;
		}
		count ++;
		mapCount.put(mqName, count);
		AppLogger.logTaxiorder.info("[Tuanpa]Unsubscribed: Count " + mqName + ": " + count);
		
		EventQueues.lookup(mqName, Env.WEBAPP, true).subscribe(eventListener);
	}
	
	static public void unsubcrible(String mqName, EventListener<Event> eventListener) {
		if (Env.WEBAPP == null)
			return;
		if (EventQueues.lookup(mqName, Env.WEBAPP, true).unsubscribe(eventListener)) {
			Integer count = mapCount.get(mqName);
			if (count == null) {
				count = 0;
			}
			count --;
			mapCount.put(mqName, count);
			AppLogger.logTaxiorder.info("[Tuanpa]Unsubscribed: Count " + mqName + ": " + count);
		}
	}
}