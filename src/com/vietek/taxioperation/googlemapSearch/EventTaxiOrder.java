package com.vietek.taxioperation.googlemapSearch;

import javax.swing.event.EventListenerList;

public class EventTaxiOrder {
	public static EventListenerList listenerList = new EventListenerList();

	public void addEventListener(IEventTaxiOrder_Listener listener) {
		listenerList.add(IEventTaxiOrder_Listener.class, listener);
	}

	public void removeEventListener(IEventTaxiOrder_Listener listener) {
		listenerList.remove(IEventTaxiOrder_Listener.class, listener);
	}

	public void fireMyEvent(EventObjectTaxiOrder evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i + 2) {
			if (listeners[i] == IEventTaxiOrder_Listener.class) {
				((IEventTaxiOrder_Listener) listeners[i + 1]).eventOccurred(evt);
			}
		}
	}

}
