package com.vietek.trackingOnline.tracker;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

import com.vietek.taxioperation.ui.controller.VGoogleMaps;

public class TestVMaps extends Div {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TestVMaps() {
		this.setSclass("test_maps");
		this.setHflex("true");
		this.setVflex("true");
		Div div = new Div();
		div.setHflex("true");
		div.setVflex("true");
		div.setParent(this);
		VGoogleMaps vMaps = new VGoogleMaps();
		vMaps.setSclass("v_gmap");
		vMaps.setHflex("true");
		vMaps.setVflex("true");
		
		vMaps.setParent(div);
		vMaps.setAutoCompleteSearch(true, "TOP_CENTER");
		vMaps.addEventListener("", new EventListener<Event>() {
			public void onEvent(Event arg0) throws Exception {
				
			};
		});
	}

	public void fireEvent(String eventName, Object data) {
		Events.postEvent(new Event(eventName, this, data));
	}
}
