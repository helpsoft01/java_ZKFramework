package com.vietek.taxioperation.googlemapSearch;

import java.util.EventListener;

public interface IEventTaxiOrder_Listener extends EventListener {
	public void eventOccurred(EventObjectTaxiOrder evt);
}