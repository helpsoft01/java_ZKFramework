package com.vietek.taxioperation.mq;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class TaxiOrderMQ {

	/**
	 * Tao moi taxiorder tu DTV
	 */
	private static String TAXI_ORDER_NEW_SAVED = "taxi_order_new_saved";
	public static String TAXI_ORDER_NEW_SAVED_EVENT = "taxi_order_new_saved_event";

	public static void pushCreated(Event event) {
		GlobalMQ.publish(TAXI_ORDER_NEW_SAVED, event);
	}

	public static void subcribleCreated(EventListener<Event> eventListener) {
		GlobalMQ.subcrible(TAXI_ORDER_NEW_SAVED, eventListener);
	}

	public static void unSubcribleCreated(EventListener<Event> eventListener) {
		GlobalMQ.unsubcrible(TAXI_ORDER_NEW_SAVED, eventListener);
	}

	/**
	 * Update taxiorder
	 */

	private static String TAXI_ORDER_UPDATED = "taxi_order_updated";

	public static String TAXI_ORDER_UPDATED_EVENT = "taxi_order_updated_event";

	public static void pushUpdated(Event event) {
		GlobalMQ.publish(TAXI_ORDER_UPDATED, event);
	}

	public static void subcribleUpdated(EventListener<Event> eventListener) {
		GlobalMQ.subcrible(TAXI_ORDER_UPDATED, eventListener);
	}

	public static void unSubcribleUpdated(EventListener<Event> eventListener) {
		GlobalMQ.unsubcrible(TAXI_ORDER_UPDATED, eventListener);
	}

	/**
	 * call in
	 */
	public final static String SERVER_PUSH_EVENT_QUEUE_DIAL = "server_push_event_queue_dial";

	public static void pushCallInDial(Event event) {
		GlobalMQ.publish(SERVER_PUSH_EVENT_QUEUE_DIAL, event);
	}

	public static void subcribleCallInDial(EventListener<Event> eventListener) {
		GlobalMQ.subcrible(SERVER_PUSH_EVENT_QUEUE_DIAL, eventListener);
	}

	public static void unSubcribleCallInDial(EventListener<Event> eventListener) {
		GlobalMQ.unsubcrible(SERVER_PUSH_EVENT_QUEUE_DIAL, eventListener);
	}
}