package com.vietek.taxioperation.googlemapSearch;

import javax.swing.event.EventListenerList;

public class EventUserCallIn {
	public static EventListenerList listenerList = new EventListenerList();

	public void addEventListener(IEventUserCallIn_Listener listener) {
		listenerList.add(IEventUserCallIn_Listener.class, listener);
	}

	public void removeEventListener(IEventUserCallIn_Listener listener) {
		listenerList.remove(IEventUserCallIn_Listener.class, listener);
	}

	public void fireMyEvent(EventObjectTaxiOrder evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i + 2) {
			if (listeners[i] == IEventUserCallIn_Listener.class) {
				((IEventUserCallIn_Listener) listeners[i + 1]).eventOccurred(evt);
			}
		}
	}
}
