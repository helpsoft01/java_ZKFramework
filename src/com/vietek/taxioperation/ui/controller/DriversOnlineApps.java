package com.vietek.taxioperation.ui.controller;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Vlayout;

public class DriversOnlineApps extends Div implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DriverAppLocations locations;
	DriverAppTrackings tracking;
	
	public DriversOnlineApps() {
		this.setStyle("width:100%; height: 100%");
		Vlayout vlayout = new Vlayout();
		vlayout.setStyle("width: 100%; height: 100%");
		vlayout.setParent(this);
		Div div = new Div();
		div.setSclass("div_title_driver_apps");
		div.setParent(vlayout);
		Label label = new Label("thông tin tài xế online app mobile");
		label.setSclass("title_driver_apps");
		label.setParent(div);
		Tabbox tabbox = new Tabbox();
		tabbox.setHflex("1");
		tabbox.setVflex("1");
		tabbox.setSclass("tbx_driver_apps");
		tabbox.setParent(vlayout);
		Tabs tabs = new Tabs();
		tabs.setParent(tabbox);
		Tabpanels tabpanels = new Tabpanels();
		tabpanels.setParent(tabbox);
		getDriverLocationDiv(tabs, tabpanels);
		getDriverTracking(tabs, tabpanels);
		tabbox.setSelectedIndex(0);
	}
	
	private void getDriverLocationDiv(Tabs tabs, Tabpanels tabpanels){
		Tab tab=new Tab("Danh sách xe đang online app");
		tab.setParent(tabs);
		tab.addEventListener(Events.ON_CLICK, this);
		tab.setSelected(true);
		Tabpanel tabpanel=new Tabpanel();
		tabpanel.setParent(tabpanels);
		locations=new DriverAppLocations();
		locations.setStyle("width:100%; height: 100%");
		locations.setParent(tabpanel);
	}
	
	private void getDriverTracking(Tabs tabs, Tabpanels tabpanels){
		Tab tab=new Tab("Tra  cứu hành trình");
		tab.setParent(tabs);
		tab.addEventListener(Events.ON_CLICK, this);
		Tabpanel tabpanel=new Tabpanel();
		tabpanel.setParent(tabpanels);
		tracking=new DriverAppTrackings();
		tracking.setStyle("width:100%; height: 100%");
		tracking.setParent(tabpanel);
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget() instanceof Tab) {
				Tab tab = (Tab) event.getTarget();
				if(tab.getIndex()==0){
					if(locations.getTimer()!=null){
						if(!locations.getTimer().isRunning()){
							locations.getTimer().start();
						}
					}
				} else {
					if(locations.getTimer()!=null){
						if(locations.getTimer().isRunning()){
							locations.getTimer().stop();
						}
					}
				}
			}
		}
	}

}
