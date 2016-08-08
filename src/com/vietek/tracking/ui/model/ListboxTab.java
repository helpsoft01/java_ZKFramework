package com.vietek.tracking.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.controller.vmap.VMaps;

public class ListboxTab extends Tabbox implements TabboxInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Integer, Tab> mapTabList;
	List<TabboxInterface> listTabLink;
	private Desktop desktop;

	public ListboxTab() {
		this.setTabscroll(true);
		mapTabList = new HashMap<>();
		listTabLink = new ArrayList<TabboxInterface>();
		initUI();
	}

	private void initUI() {
		this.setSclass("tab_Listbox_frm");
		Tabs tabs = new Tabs();
		tabs.setParent(this);
		Tabpanels tabpanels = new Tabpanels();
		tabpanels.setParent(this);
		this.addEventListener(Events.ON_SELECT, LISTEN_TAB_ON_SELECTED);
	}

	public void putTabList(Vehicle vehicle, List<GpsTrackingMsg> lsthis, VMaps vmap, boolean isShow) {
		if (mapTabList.containsKey(vehicle.getId())) {
			((ListBoxTabUnit) (mapTabList.get(vehicle.getId()).getValue())).refresh();
			((ListBoxTabUnit) (mapTabList.get(vehicle.getId()).getValue())).drawHistory(lsthis, isShow);
			((ListBoxTabUnit) (mapTabList.get(vehicle.getId()).getValue())).setLstmodel(lsthis);
			((ListBoxTabUnit) (mapTabList.get(vehicle.getId()).getValue())).getMarkerrun().setBeginRun(true);
		} else {
			ListBoxTabUnit tablist = new ListBoxTabUnit(vehicle);
			Tab tab = new Tab();
			tab.setAttribute("vehicleID", vehicle.getId());
			tab.setClosable(true);
			tab.setParent(this.getTabs());
			tab.addEventListener(Events.ON_CLOSE, LISTENER_TAB_ON_CLOSE);
			tab.setLabel(vehicle.getTaxiNumber());
			tab.setValue(tablist);
			Tabpanel tabpanel = new Tabpanel();
			tabpanel.setParent(this.getTabpanels());
			tabpanel.setStyle("height:280px");
			tabpanel.setHflex("1");
			tablist.getToolControl().setParent(tabpanel);
			tablist.setvmapViewer(vmap);
			tablist.drawHistory(lsthis, isShow);
			tablist.setParent(tabpanel);
			mapTabList.put(vehicle.getId(), tab);
			renderTabListBox(tabpanel, tablist, lsthis);
		}
		this.setSelectedTabChildrenTab(vehicle.getId());
	}

	private void renderTabListBox(Tabpanel tabpanel, ListBoxTabUnit tablist, List<GpsTrackingMsg> lsthis) {
		tablist.setLstmodel(lsthis);
	}

	public ListBoxTabUnit getSelectedTabUnit() {
		ListBoxTabUnit listboxunit = null;
		if (this.getSelectedTab() != null) {
			listboxunit = (ListBoxTabUnit) this.getSelectedTab().getValue();
			Events.postEvent(Events.ON_CLOSE, this, listboxunit.getVehicle().getId());
		}
		return listboxunit;
	}

	private EventListener<Event> LISTENER_TAB_ON_CLOSE = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			Integer vehicleid = (Integer) event.getTarget().getAttribute("vehicleID");
			((ListBoxTabUnit) ((Tab) event.getTarget()).getValue()).refreshVmap();
			mapTabList.remove(vehicleid);
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

	public boolean isBeginRun() {
		boolean check = false;
		for (Tab tab : mapTabList.values()) {
			if (((ListBoxTabUnit) tab.getValue()).getMarkerrun() != null) {
				check = check || ((ListBoxTabUnit) tab.getValue()).getMarkerrun().isBeginRun();
			} else {
				check = true;
			}

		}
		return check;
	}

	public void setBeginRun(boolean isBegin) {
		for (Tab tab : mapTabList.values()) {
			((ListBoxTabUnit) tab.getValue()).getMarkerrun().setBeginRun(isBegin);
		}
	}

	public void addChildrenLink(TabboxInterface children) {
		listTabLink.add(children);
	}

	public void refresh() {
		List<Tab> lsttab = new ArrayList<Tab>(mapTabList.values());
		if (lsttab != null) {
			Iterator<Tab> ite = lsttab.iterator();
			while (ite.hasNext()) {
				Tab tab = ite.next();
				tab.close();
			}
			mapTabList.clear();
		}

	}

	public void setParentMarkerRun(Component comp) {
		for (Tab tab : mapTabList.values()) {
			if (((ListBoxTabUnit) tab.getValue()).getMarkerrun() != null) {
				((ListBoxTabUnit) tab.getValue()).getMarkerrun().setParent(comp);
			}

		}
	}

	public Map<Integer, Tab> getMapTabList() {
		return mapTabList;
	}

	public void setMapTabList(Map<Integer, Tab> mapTabList) {
		this.mapTabList = mapTabList;
	}

	@Override
	public void closeChildrenTab(int vehicleid) {
		Tab tab = mapTabList.get(vehicleid);
		if (tab != null) {
			((ListBoxTabUnit) tab.getValue()).refreshVmap();
			tab.close();
			mapTabList.remove(vehicleid);
		}

	}

	public List<LatLng> getBoundsRunIcon() {
		List<LatLng> bounds = new ArrayList<>();
		for (Tab tab : mapTabList.values()) {
			VehicleMarker mkrun = ((ListBoxTabUnit) tab.getValue()).getMarkerrun();
			bounds.add(mkrun.getPosition());
		}
		return bounds;
	}

	@Override
	public void setSelectedTabChildrenTab(int vehicleid) {
		Tab tab = mapTabList.get(vehicleid);
		if (tab != null) {
			this.setSelectedTab(tab);
		}
	}

	public Desktop getDesktop() {
		return desktop;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
	}

	class RenderListboxWorker extends Thread {
		Tabpanel tabpanel;
		ListBoxTabUnit listbox;
		List<GpsTrackingMsg> lsthistory;

		public RenderListboxWorker(Tabpanel tabp, ListBoxTabUnit listbox, List<GpsTrackingMsg> lsthis) {
			this.tabpanel = tabp;
			this.listbox = listbox;
			this.lsthistory = lsthis;
		}

		@Override
		public void run() {
			Executions.schedule(desktop, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					listbox.setLstmodel(lsthistory);
					Clients.clearBusy(tabpanel);
				}
			}, null);
		}
	}

	@Override
	public void updateData(int vehicleid, GenaralValue data) {
		Tab tablistbox = mapTabList.get(vehicleid);
		if (tablistbox != null) {
			((ListBoxTabUnit) tablistbox.getValue()).updateListbox(data);
		}
	}
}
