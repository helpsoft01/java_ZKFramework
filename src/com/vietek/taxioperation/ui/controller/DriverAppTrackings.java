package com.vietek.taxioperation.ui.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.Gpolyline;
import org.zkoss.gmaps.LatLng;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.West;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.controller.AgentController;
import com.vietek.taxioperation.controller.DriverAppTrackingController;
import com.vietek.taxioperation.controller.DriverController;
import com.vietek.taxioperation.controller.VehicleController;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.DriverAppTracking;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.ui.util.ListItemRenderer;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.MapUtils;

public class DriverAppTrackings extends Div implements EventListener<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Combobox cbAgent;
	private Combobox cbDriver;
	private Datebox dbFromTime;
	private Datebox dbToTime;
	private Gmaps gmaps;
	private Button btnView;
	private List<Agent> agents;
	private Agent selectedAgent;
	private Listbox listbox;
	private Driver selectedDriver;
	AbstractListModel<?> listModel;
	DriverAppTrackingController trackingController;
	DriverController driverController;
	VehicleController vehicleController;
	private int day;

	public DriverAppTrackings() {
		getDayProperties();
		Borderlayout borderlayout = new Borderlayout();
		borderlayout.setStyle("width:100%; height:100%");
		borderlayout.setParent(this);
		West west = new West();
		west.setSclass("west_driver_online_apps");
		west.setTitle("Theo dõi hành trình");
		west.setSize("333px");
		west.setCollapsible(true);
		west.setParent(borderlayout);
		initControls(west);
		Center center = new Center();
		center.setParent(borderlayout);
		initMaps(center);
		getListAgent();
		Clients.evalJavaScript("fixDriverAppTrackings()");
	}

	/**
	 * Lay khoang thoi gian max dc xem hanh trinh
	 */
	private void getDayProperties() {
		String strDay = new ConfigUtil().getPropValues("DAY_SEARCH_APP_TRACKING");
		try {
			day = Integer.parseInt(strDay);
		} catch (Exception ex) {
			day = 7;
		}

	}

	private void initControls(Component parent) {
		Div div = new Div();
		div.setId("div_app_tracking");
		div.setHflex("true");
		div.setVflex("true");
		div.setStyle("padding: 10px");
		div.setParent(parent);

		Div div4 = new Div();
		div4.setId("comp_div_app_tracking");
		div4.setWidth("100%");
		div4.setParent(div);
		cbAgent = new Combobox();
		cbAgent.setSclass("cb_control_driver_apps");
		cbAgent.setAutocomplete(true);
		cbAgent.setAutodrop(true);
		cbAgent.setParent(div4);
		cbAgent.addEventListener(Events.ON_CHANGE, this);
		cbAgent.setPlaceholder("Chọn chi nhánh");
		cbDriver = new Combobox();
		cbDriver.setSclass("cb_control_driver_apps");
		cbDriver.setAutocomplete(true);
		cbDriver.setAutodrop(true);
		cbDriver.setParent(div4);
		cbDriver.addEventListener(Events.ON_CHANGE, this);
		cbDriver.setPlaceholder("Chọn tài xế");

		Div div2 = new Div();
		div2.setStyle("text-align: center; margin-bottom: 10px;");
		div2.setParent(div4);
		Label label = new Label("Chọn thời gian");
		label.setStyle("font-weight: bold;");
		label.setParent(div2);
		Hlayout hlayout = new Hlayout();
		hlayout.setWidth("100%");
		hlayout.setStyle("margingetListAgent();-bottom: 10px;");
		hlayout.setParent(div4);
		div2 = new Div();
		div2.setStyle("width: 30px; line-height: 24px;");
		div2.setParent(hlayout);
		label = new Label("Từ");
		label.setStyle("margin-right: 5px; font-weight: bold;");
		label.setParent(div2);
		dbFromTime = new Datebox();
		dbFromTime.setWidth("275px");
		dbFromTime.setFormat("dd/MM/yyyy HH:mm");
		dbFromTime.setValue(new Date(System.currentTimeMillis() - (60l * 60l * 1000)));
		dbFromTime.setSclass("datebox_driver_app_tracking");
		dbFromTime.setParent(hlayout);
		dbFromTime.addEventListener(Events.ON_CHANGE, this);
		hlayout = new Hlayout();
		hlayout.setWidth("100%");
		hlayout.setStyle("margin-bottom: 10px; margin-top: 5px;");
		hlayout.setParent(div4);
		div2 = new Div();
		div2.setStyle("width: 30px; line-height: 24px;");
		div2.setParent(hlayout);
		label = new Label("Đến");
		label.setStyle("margin-right: 5px;font-weight: bold;");
		label.setParent(div2);
		dbToTime = new Datebox(new Date(System.currentTimeMillis()));
		dbToTime.setFormat("dd/MM/yyyy HH:mm");
		dbToTime.setWidth("275px");
		dbToTime.setSclass("datebox_driver_app_tracking");
		dbToTime.setParent(hlayout);
		dbToTime.addEventListener(Events.ON_CHANGE, this);
		btnView = new Button("Xem hành trình");
		btnView.setSclass("btn_driver_app_tracking");
		btnView.setParent(div4);
		btnView.addEventListener(Events.ON_CLICK, this);
		initListbox(div);
	}

	private void initMaps(Component parent) {
		gmaps = new Gmaps();
		gmaps.setVersion("3.9");
		gmaps.setHflex("1");
		gmaps.setVflex("1");
		gmaps.setShowSmallCtrl(true);
		gmaps.setParent(parent);
		gmaps.setCenter(new LatLng(10.7774286, 106.7022481));
	}

	private void initListbox(Component parent) {
		listbox = new Listbox();
		listbox.setId("list_app_tracking");
		listbox.setWidth("100%");
		// listbox.setStyle("margin-top: 10px;");
		listbox.setVflex(true);
		listbox.setAutopaging(false);
		listbox.setParent(parent);
		Listhead head = new Listhead();
		head.setParent(listbox);
		head.setMenupopup("auto");
		ArrayList<GridColumn> columns = new ArrayList<GridColumn>();
		columns.add(new GridColumn("Thời gian", 80, Timestamp.class, "getTime", "time", DriverAppTracking.class));
		columns.add(new GridColumn("Kinh độ", 50, Long.class, "getLatitude"));
		columns.add(new GridColumn("Vĩ độ", 50, Long.class, "getLongitude"));
		columns.add(new GridColumn("Trạng thái", 50, Integer.class, "getStatus", "status", DriverAppTracking.class));
		Listheader header = null;
		for (int i = 0; i < columns.size(); i++) {
			GridColumn gridCol = columns.get(i);
			header = new Listheader(gridCol.getHeader());
			if (i == columns.size() - 1 && i < 5) {
				header.setHflex("1");
			} else {
				header.setWidth(gridCol.getWidth() + "px");
			}
			header.setStyle("text-align:center;");
			header.setParent(head);
		}
		ListItemRenderer<DriverAppTracking> renderer = new ListItemRenderer<DriverAppTracking>(columns);
		listbox.setItemRenderer(renderer);
		listbox.addEventListener(Events.ON_DOUBLE_CLICK, this);
	}

	private void getListAgent() {
		cbAgent.getChildren().clear();
		AgentController agentController = (AgentController) ControllerUtils.getController(AgentController.class);
		agents = agentController.find("from Agent");
		if (agents.size() > 0) {
			for (Agent agent : agents) {
				Comboitem item = new Comboitem();
				item.setAttribute("agent", agent);
				item.setLabel(agent.getAgentName());
				cbAgent.appendChild(item);
			}
		}
	}

	private List<Driver> getListDrivers(Agent agent) {
		List<Driver> drivers = null;
		if (agent != null) {
			if (driverController == null)
				driverController = (DriverController) ControllerUtils.getController(DriverController.class);
			drivers = driverController.find("from Driver where agent=? and isAppRegister=true", agent);
		}
		return drivers;
	}

	private void addDrivers(List<Driver> drivers) {
		if (cbDriver.getSelectedItem() != null)
			cbDriver.removeChild(cbDriver.getSelectedItem());
		cbDriver.getChildren().clear();
		cbDriver.setText("");
		if (drivers.size() > 0) {
			for (Driver driver : drivers) {
				Comboitem item = new Comboitem();
				item.setLabel(driver.getStaffCard() + " - " + driver.getName());
				item.setAttribute("driver", driver);
				cbDriver.appendChild(item);
			}
		}
	}

	private List<DriverAppTracking> getTracking(Driver driver, Timestamp fromTime, Timestamp toTime) {
		if (trackingController == null) {
			trackingController = (DriverAppTrackingController) ControllerUtils
					.getController(DriverAppTrackingController.class);
		}
		List<DriverAppTracking> trackings = trackingController.find(
				"from DriverAppTracking where driver=? AND latitude>0 AND longitude>0 AND time>=? AND time<=? ORDER BY time ASC",
				driver, fromTime, toTime);
		return trackings;
	}

	@SuppressWarnings("deprecation")
	private void showTracking(List<DriverAppTracking> trackings) {
		gmaps.getChildren().clear();
		StringBuffer points = new StringBuffer();
		DriverAppTracking tracking;
		int pointNum = 0;
		int oldStatus = 0;
		LatLng startPoint = new LatLng(0, 0);
		LatLng endPoint = new LatLng(0, 0);
		for (int i = 0; i < trackings.size(); i++) {
			tracking = trackings.get(i);
			if (tracking.getLatitude() > 0 && tracking.getLongitude() > 0) {
				if (pointNum == 0) {
					startPoint = new LatLng(tracking.getLatitude(), tracking.getLongitude());
					oldStatus = tracking.getStatus();
				}
				if (equalStatus(tracking.getStatus(), oldStatus)) { // Status
																	// khong doi
					points.append(tracking.getLatitude()).append(", ").append(tracking.getLongitude()).append(", 3")
							.append(", ");
					pointNum++;
				} else { // Status thay doi
					if (pointNum >= 4) {
						points.setLength(points.length() - 2);
						Gpolyline gpolyline = new Gpolyline();
						gpolyline.setPoints(points.toString());
						if (oldStatus == 0)
							gpolyline.setColor("#ff3300");
						else
							gpolyline.setColor("#009933");
						gpolyline.setParent(gmaps);
					}
					points.setLength(0);
					oldStatus = tracking.getStatus();
					pointNum = 1;
					points.append(tracking.getLatitude()).append(", ").append(tracking.getLongitude()).append(", 3")
							.append(", ");
				}
				endPoint = new LatLng(tracking.getLatitude(), tracking.getLongitude());
			}
		}
		Gmarker gmarker = new Gmarker("", startPoint);
		gmarker.setId("startPoint");
		gmarker.setIconImage("./themes/images/marker_start_32.png");
		gmarker.setParent(gmaps);
		gmarker = new Gmarker("", endPoint);
		gmarker.setId("endPoint");
		gmarker.setIconImage("./themes/images/marker_end_32.png");
		gmarker.setParent(gmaps);
		MapUtils.scaleMap(gmaps);
	}

	private void clearMaps() {
		List<AbstractComponent> listcomponent = gmaps.getChildren();
		if (listcomponent != null && listcomponent.size() > 0) {
			for (Iterator<AbstractComponent> it = listcomponent.iterator(); it.hasNext();) {
				AbstractComponent abscomponent = it.next();
				if (abscomponent instanceof Gmarker) {
					if (!abscomponent.getId().equals("startPoint") && !abscomponent.getId().equals("endPoint")) {
						it.remove();
					}
				}
			}
		}
	}

	private void clearAllMaps() {
		gmaps.getChildren().clear();
	}

	private boolean equalStatus(Integer int1, Integer int2) {
		boolean equal = false;
		if (int1 == 0 && int2 == 0)
			equal = true;
		else if (int1 > 0 && int2 > 0) {
			equal = true;
		}
		return equal;
	}

	private void panTo(DriverAppTracking tracking) {
		if (tracking.getLatitude() > 0 && tracking.getLongitude() > 0) {
			clearMaps();
			Gmarker gmarker = new Gmarker();
			gmarker.setLat(tracking.getLatitude());
			gmarker.setLng(tracking.getLongitude());
			String status = "";
			if (tracking.getStatus() > 0) {
				status = "Có khách";
				gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEATS_PROCESSING);
			} else {
				status = "Không khách";
				gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEATS_NON_PROCESSING);
			}
			String content = "<table><tr><td style='width: 80px;'>Kinh độ</td><td style='color: green'>"
					+ tracking.getLatitude() + "</td></tr>"
					+ "<tr><td style='width: 80px;'>Vĩ độ</td><td style='color: green'>" + tracking.getLongitude()
					+ "</td></tr>" + "<tr><td style='width: 80px;'>Trạng thái</td><td style='color: green'>" + status
					+ "</td></tr></table>";
			gmarker.setContent(content);
			gmarker.setOpen(true);
			gmarker.setParent(gmaps);
			gmaps.panTo(tracking.getLatitude(), tracking.getLongitude());
		}
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget().equals(btnView)) {
				clearAllMaps();
				if (selectedDriver != null) {
					Timestamp fromTime = new Timestamp(dbFromTime.getValue().getTime());
					Timestamp toTime = new Timestamp(dbToTime.getValue().getTime());
					List<DriverAppTracking> trackings = getTracking(selectedDriver, fromTime, toTime);
					listModel = new ListModelList<>(trackings);
					listbox.setModel(listModel);
					if (trackings.size() >= 4) {
						showTracking(trackings);
					}
				}
			}
		} else if (event.getName().equals(Events.ON_CHANGE)) {
			if (event.getTarget().equals(cbAgent)) {
				if (cbAgent.getSelectedItem() != null) {
					Comboitem item = cbAgent.getSelectedItem();
					selectedAgent = (Agent) item.getAttribute("agent");
					@SuppressWarnings("unchecked")
					List<Driver> drivers = (List<Driver>) item.getAttribute("drivers");
					if (drivers == null || drivers.size() == 0)
						drivers = getListDrivers(selectedAgent);
					item.setAttribute("drivers", drivers);
					addDrivers(drivers);
				}
			} else if (event.getTarget().equals(cbDriver)) {
				if (cbDriver.getSelectedItem() != null) {
					Comboitem item = cbDriver.getSelectedItem();
					selectedDriver = (Driver) item.getAttribute("driver");
				}
			} else if (event.getTarget().equals(dbFromTime)) {
				if (dbFromTime.getValue() == null)
					dbFromTime.setValue(new Date(System.currentTimeMillis() - (1000 * 60 * 60)));
				else {
					long fromTime = dbFromTime.getValue().getTime();
					long toTime = System.currentTimeMillis();
					if (fromTime > (toTime - (1000 * 60 * 5))) {
						fromTime = toTime - (1000 * 60 * 5);
						dbFromTime.setValue(new Date(fromTime));
					}
					if (dbToTime.getValue() == null) {
						dbToTime.setValue(new Date(System.currentTimeMillis()));
					} else {
						toTime = dbToTime.getValue().getTime();
					}
					if (toTime - fromTime > (1000 * 60 * 60 * 24 * day)) {
						long time = 1000 * 60 * 60 * 24 * day + fromTime;
						dbToTime.setValue(new Date(time));
					}
				}
			} else if (event.getTarget().equals(dbToTime)) {
				if (dbToTime.getValue() == null)
					dbToTime.setValue(new Date(System.currentTimeMillis()));
				else {
					long fromTime = dbFromTime.getValue().getTime();
					long toTime = dbToTime.getValue().getTime();

					if (dbToTime.getValue().getTime() > System.currentTimeMillis()) {
						dbToTime.setValue(new Date(System.currentTimeMillis()));
					}
					if (fromTime >= toTime) {
						toTime = fromTime + (1000 * 60 * 5);
						dbFromTime.setValue(new Date(fromTime));
					}
					if (toTime - fromTime > (1000 * 60 * 60 * 24 * day)) {
						long time = toTime - (1000 * 60 * 60 * 24 * day);
						dbFromTime.setValue(new Date(time));
					}
				}
			}
		} else if (event.getName().equals(Events.ON_DOUBLE_CLICK)) {
			if (event.getTarget().equals(listbox)) {
				Listitem item = listbox.getSelectedItem();
				if (item != null) {
					DriverAppTracking tracking = (DriverAppTracking) item.getValue();
					panTo(tracking);
				}
			}
		}
	}

}