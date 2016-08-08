package com.vietek.taxioperation.googlemapSearch;

import java.util.EventListener;

public interface IEventUserCallIn_Listener extends EventListener {
	public void eventOccurred(EventObjectTaxiOrder evt);
}
