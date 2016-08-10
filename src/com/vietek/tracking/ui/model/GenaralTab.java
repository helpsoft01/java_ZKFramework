package com.vietek.tracking.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import com.vietek.taxioperation.model.Vehicle;

public class GenaralTab extends Tabbox implements TabboxInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<Integer, Tab> mapGenaral;
	List<TabboxInterface> listTabLink;

	public GenaralTab() {
		mapGenaral = new HashMap<>();
		listTabLink = new ArrayList<TabboxInterface>();
		Tabs tabs = new Tabs();
		tabs.setParent(this);
		Tabpanels tabpanels = new Tabpanels();
		tabpanels.setParent(this);
		this.addEventListener(Events.ON_SELECT, LISTEN_TAB_ON_SELECTED);
		this.setTabscroll(true);
	}

	public void putGenaral(Vehicle vehicle) {
		if (mapGenaral.containsKey(vehicle.getId())) {
			((GenaralTabUnit) mapGenaral.get(vehicle.getId()).getValue()).refresh();
		} else {
			GenaralTabUnit genaralunit = new GenaralTabUnit(vehicle);
			Tab tab = new Tab();
			tab.setAttribute("vehicleID", vehicle.getId());
			tab.setClosable(true);
			tab.setParent(this.getTabs());
			tab.addEventListener(Events.ON_CLOSE, LISTENER_TAB_ON_CLOSE);
			tab.setLabel(vehicle.getTaxiNumber());
			tab.setDisabled(false);
			tab.setValue(genaralunit);
			Tabpanel tabpanel = new Tabpanel();
			tabpanel.setParent(this.getTabpanels());
			tabpanel.appendChild(genaralunit);
			mapGenaral.put(vehicle.getId(), tab);
		}
		this.setSelectedTabChildrenTab(vehicle.getId());
	}

	private EventListener<Event> LISTENER_TAB_ON_CLOSE = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			Integer vehicleid = (Integer) event.getTarget().getAttribute("vehicleID");
			mapGenaral.remove(vehicleid);
			for (TabboxInterface childrenTabPanel : listTabLink) {
				childrenTabPanel.closeChildrenTab(vehicleid);
			}
		}
	};
	private EventListener<Event> LISTEN_TAB_ON_SELECTED = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			Tab tab = (Tab) event.getTarget();
			if (tab != null) {
				Integer vehicleid = (Integer) tab.getAttribute("vehicleID");
				for (TabboxInterface childrenTabPanel : listTabLink) {
					childrenTabPanel.setSelectedTabChildrenTab(vehicleid);
				}
			}

		}
	};

	public void refresh() {
		Iterator<Tab> ite = mapGenaral.values().iterator();
		while (ite.hasNext()) {
			Tab tab = ite.next();
			tab.close();
		}
		mapGenaral.clear();
	}

	public Map<Integer, Tab> getMapGenaral() {
		return mapGenaral;
	}

	public void setMapGenaral(Map<Integer, Tab> mapGenaral) {
		this.mapGenaral = mapGenaral;
	}

	public void addChildrenLink(TabboxInterface children) {
		listTabLink.add(children);
	}

	@Override
	public void closeChildrenTab(int vehicleid) {
		Tab tab = mapGenaral.get(vehicleid);
		if (tab != null) {
			tab.close();
			mapGenaral.remove(vehicleid);
		}
	}

	@Override
	public void setSelectedTabChildrenTab(int vehicleid) {
		Tab tab = mapGenaral.get(vehicleid);
		if (tab != null) {
			this.setSelectedTab(tab);
		}

	}

	@Override
	public void updateData(int vehicleid, GenaralValue data) {
		Tab tabgenaral = mapGenaral.get(vehicleid);
		if (tabgenaral != null) {
			((GenaralTabUnit) tabgenaral.getValue()).updateGenaral(data);
			for (TabboxInterface childrenTabPanel : listTabLink) {
				childrenTabPanel.updateData(vehicleid, data);
			}
		}
	}

}
