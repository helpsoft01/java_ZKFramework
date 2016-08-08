/**
 * @author tuanpa
 */
package com.vietek.taxioperation.mq;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class ChatMQ {
	private static String CHAT_MQ_NAME = "chat";
	public static void publish(Event event) {
		GlobalMQ.publish(CHAT_MQ_NAME, event);
	}
	
	public static void subcrible(EventListener<Event> eventListener) {
		GlobalMQ.subcrible(CHAT_MQ_NAME, eventListener);
	}
	
	public static void unsubcrible(EventListener<Event> eventListener) {
		
		GlobalMQ.unsubcrible(CHAT_MQ_NAME, eventListener);
	}
}
