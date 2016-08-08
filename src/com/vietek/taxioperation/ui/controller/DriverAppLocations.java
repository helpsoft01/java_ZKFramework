package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Timer;
import org.zkoss.zul.West;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.controller.AgentController;
import com.vietek.taxioperation.controller.TaxiGroupController;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.ui.util.DriverAppLocationModel;
import com.vietek.taxioperation.ui.util.DriverOnlineAppRenderer;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.MapUtils;

public class DriverAppLocations extends Div implements EventListener<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String URL_ICON_4_SEATS = "/themes/images/4seats_top(48x48).png";
	private final String URL_ICON_7_SEATS = "/themes/images/7seats_top(48x48).png";
	private final String ATT_AGENT = "attribute_agent";
	private final String ATT_TAXIGROUP = "attribute_taxigroup";
	private Gmaps gmaps;
	private Gmarker gmarker;
	private Combobox cbAgent;
	private Listbox listbox;
	private List<Agent> agents;
	private Agent selectedAgent;
	private List<TaxiGroup> lst_groups;

	private Timer timer;

	public DriverAppLocations() {
		Borderlayout borderlayout = new Borderlayout();
		borderlayout.setStyle("width:100%; height:100%");
		borderlayout.setParent(this);
		West west = new West();
		west.setSclass("west_driver_online_apps");
		west.setTitle("Danh sách xe");
		west.setSize("333px");
		west.setCollapsible(true);
		west.setParent(borderlayout);
		Center center = new Center();
		center.setParent(borderlayout);
		initPanelControls(west);
		initMaps(center);
		timer = new Timer();
		timer.setRepeats(true);
		timer.setDelay(10000);
		timer.setParent(this);
		timer.addEventListener(Events.ON_TIMER, this);
		timer.start();
	}

	private void initPanelControls(Component parent) {
		Div div = new Div();
		div.setStyle("width: 100%; height: 100%; padding: 10px");
		div.setParent(parent);

		Label label = new Label("Chọn chi nhánh");
		label.setStyle("font-weight: bold;");
		label.setParent(div);
		cbAgent = new Combobox();
		cbAgent.setSclass("cb_control_driver_apps");
		cbAgent.setAutocomplete(true);
		cbAgent.setAutodrop(true);
		cbAgent.setParent(div);
		cbAgent.addEventListener(Events.ON_CHANGE, this);
		cbAgent.setPlaceholder("Chọn chi nhánh");
		listbox = new Listbox();
		listbox.setParent(div);
		listbox.setNonselectableTags("");
		listbox.setMold("paging");
		listbox.setAutopaging(true);
		listbox.setVflex(true);
		listbox.setPagingPosition("bottom");
		listbox.addEventListener(Events.ON_DOUBLE_CLICK, this);
		setupCols();
		getListAgent();
	}

	private void initMaps(Component parent) {
		gmaps = new Gmaps();
		gmaps.setVersion("3.9");
		gmaps.setHflex("1");
		gmaps.setVflex("1");
		gmaps.setShowSmallCtrl(true);
		gmaps.setParent(parent);
		gmaps.setCenter(10.7774286, 106.7022481);
		gmaps.addEventListener("onMapClick", this);
	}

	private void setupCols() {
		Listhead head = new Listhead();
		head.setParent(listbox);
		head.setMenupopup("auto");
		Listheader header = new Listheader("");
		header.setWidth("50px");
		header.setStyle("text-align:center;");
		header.setParent(head);
		header = new Listheader("Phương tiện");
		header.setStyle("text-align:center;");
		header.setParent(head);
		header = new Listheader("Tài xế");
		header.setStyle("text-align:center;");
		header.setParent(head);
		DriverOnlineAppRenderer<DriverAppLocationModel> renderer = new DriverOnlineAppRenderer<>();
		listbox.setItemRenderer(renderer);
		getListVehicle(null, null);
	}

	private void getListAgent() {
		cbAgent.getChildren().clear();
		AgentController agentController = (AgentController) ControllerUtils.getController(AgentController.class);
		agents = agentController.find("from Agent");
		if (agents.size() > 0) {
			for (Agent agent : agents) {
				Comboitem item = new Comboitem();
				item.setAttribute(ATT_AGENT, agent);
				item.setLabel(agent.getAgentName());
				List<TaxiGroup> groups = getTaxiGroup(agent);
				item.setAttribute(ATT_TAXIGROUP, groups);
				cbAgent.appendChild(item);
			}
		}
	}

	private ListModel<DriverAppLocationModel> getListVehicle(Agent agent, List<TaxiGroup> groups) {
		List<DriverAppLocationModel> listVehicle = new ArrayList<DriverAppLocationModel>();
		DriverAppLocationModel driverLocation;
		if (agent != null) {
			pantoAgent(agent);
			if (groups == null) {
				TaxiGroupController groupController = (TaxiGroupController) ControllerUtils
						.getController(TaxiGroupController.class);
				groups = groupController.find("from TaxiGroup where agent=?", agent);
			}
			if (groups.size() > 0) {
				for (TaxiGroup taxiGroup : groups) {
					if (Taxi.LST_TAXI_ONLINE != null && Taxi.LST_TAXI_ONLINE.size() > 0) {
						for (int i = 0; i < Taxi.LST_TAXI_ONLINE.size(); i++) {
							Taxi taxi = Taxi.LST_TAXI_ONLINE.get(i);
							if (taxi.getVehicle().getTaxiGroup().equals(taxiGroup)) {
								driverLocation = new DriverAppLocationModel();
								Image image = new Image();
								image.setSclass("img_listbox_driver_apps");
								if (taxi.getCarType() == 1) { // 4 cho
									image.setSrc(URL_ICON_4_SEATS);
								} else {
									image.setSrc(URL_ICON_7_SEATS);
								}
								driverLocation.setImgStatus(image);
								driverLocation.setVehicle(taxi.getVehicle());
								driverLocation.setDriver(taxi.getDriver());
								driverLocation.setTaxi(taxi);
								listVehicle.add(driverLocation);
							}
						}
					}
				}
			}
		}
		return new ListModelList<>(listVehicle);
	}

	private List<TaxiGroup> getTaxiGroup(Agent agent) {
		TaxiGroupController groupController = (TaxiGroupController) ControllerUtils
				.getController(TaxiGroupController.class);
		List<TaxiGroup> groups = groupController.find("from TaxiGroup where agent=?", agent);
		return groups;
	}

	private void pantoAgent(Agent agent) {
		if (agent.getAgentAddress() != null) {
			LatLng latLng = MapUtils.convertAddresstoLatLng(agent.getAgentAddress());
			if (latLng != null) {
				if (latLng.lat > 0 && latLng.lng > 0) {
					gmaps.panTo(latLng.lat, latLng.lng);
				}
			}
		}
	}

	private void clearVehicleMarker() {
		List<AbstractComponent> listcomponent = gmaps.getChildren();
		if (listcomponent != null && listcomponent.size() > 0) {
			for (Iterator<AbstractComponent> it = listcomponent.iterator(); it.hasNext();) {
				AbstractComponent abscomponent = it.next();
				if (abscomponent instanceof Gmarker) {
					it.remove();
				}
			}
		}
	}

	private void renderGmaker(Gmaps gmaps) {
		if (cbAgent.getSelectedItem() != null) {
			Comboitem item = cbAgent.getSelectedItem();
			Agent agent = (Agent) item.getAttribute(ATT_AGENT);
			clearVehicleMarker();
			List<Taxi> taxis = getListTaxi(agent);
			if (taxis != null && taxis.size() > 0) {
				for (int i = 0; i < taxis.size(); i++) {
					Taxi taxi = taxis.get(i);
					if (taxi.getLattitute() > 0 && taxi.getLongitude() > 0) {
						gmarker = new Gmarker();
						gmarker.setId(taxi.getVehicle().getTaxiNumber() + taxi.getDriver().getName());
						gmarker.setLat(taxi.getLattitute());
						gmarker.setLng(taxi.getLongitude());
						gmarker.setIconImage("/themes/images/icon_car_32.png");
						if (taxi.getTrip() != null) {
							if (taxi.getTrip().isInProgress()) {
								if (taxi.getCarType() == 1) {
									gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEATS_PROCESSING);
								} else {
									gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_7SEATS_PROCESSING);
								}
							} else {
								if (taxi.getCarType() == 1) {
									gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEATS_NON_PROCESSING);
								} else {
									gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_7SEATS_NON_PROCESSING);
								}
							}
						} else {
							if (taxi.getCarType() == 1) {
								gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEATS_NON_PROCESSING);
							} else {
								gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_7SEATS_NON_PROCESSING);
							}
						}
						String status = "";
						if (taxi.getTrip() != null) {
							if (taxi.getTrip().getOrder() == null)
								status = "Không khách";
							else
								status = "Có khách";
						} else
							status = "Không khách";
						String content = "<table><tr><td style='width:65px; font-weight:bold'>Số tài</td><td style='font-weight: bold; color: green'>"
								+ taxi.getVehicle().getValue() + "</td></tr>"
								+ "<tr><td style='width:65px; font-weight:bold'>Tài xế</td><td style='font-weight: bold; color: green'>"
								+ taxi.getDriverName() + "</td></tr>"
								+ "<tr><td style='width:65px; font-weight:bold'>SĐT</td><td style='font-weight: bold; color: green'>"
								+ taxi.getDriver().getPhoneNumber() + "</td></tr>"
								+ "<tr><td style='width:65px; font-weight:bold'>Kinh độ</td><td style='font-weight: bold; color: green'>"
								+ taxi.getLattitute() + "</td></tr>"
								+ "<tr><td style='width:65px; font-weight:bold'>Vĩ độ</td><td style='font-weight: bold; color: green'>"
								+ taxi.getLongitude() + "</td></tr>"
								+ "<tr><td style='width:65px; font-weight:bold'>Trạng thái</td><td style='font-weight: bold; color: green'>"
								+ status + "</td></tr></table>";
						gmarker.setContent(content);
						gmarker.setParent(gmaps);
						gmaps.appendChild(gmarker);
					}
				}
			}
		}
	}

	private List<Taxi> getListTaxi(Agent agent) {
		if (lst_groups == null) {
			TaxiGroupController groupController = (TaxiGroupController) ControllerUtils
					.getController(TaxiGroupController.class);
			lst_groups = groupController.find("from TaxiGroup where agent=?", agent);
		}
		List<Taxi> taxis = new ArrayList<Taxi>();
		if (lst_groups.size() > 0) {
			for (TaxiGroup taxiGroup : lst_groups) {
				if (Taxi.LST_TAXI_ONLINE != null && Taxi.LST_TAXI_ONLINE.size() > 0) {
					for (int i = 0; i < Taxi.LST_TAXI_ONLINE.size(); i++) {
						Taxi taxi = Taxi.LST_TAXI_ONLINE.get(i);
						if (taxi.getVehicle().getTaxiGroup().equals(taxiGroup)) {
							taxis.add(taxi);
						}
					}
				}
			}
		}
		return taxis;
	}

	private void panToTaxi(Taxi taxi) {
		String id = taxi.getVehicle().getTaxiNumber() + taxi.getDriver().getName();
		if (gmaps.getChildren().size() > 0) {
			for (Component comp : gmaps.getChildren()) {
				if (comp instanceof Gmarker) {
					Gmarker gmarker = (Gmarker) comp;
					if (gmarker.getId().equals(id)) {
						gmaps.panTo(gmarker.getLat(), gmarker.getLng());
						gmarker.setOpen(true);
					}
				}
			}
		} else {
			gmarker = new Gmarker();
			gmarker.setId(id);
			gmarker.setLat(taxi.getLattitute());
			gmarker.setLng(taxi.getLongitude());
			gmarker.setIconImage("/themes/images/icon_car_32.png");
			if (taxi.getTrip() != null) {
				if (taxi.getTrip().isInProgress()) {
					if (taxi.getCarType() == 1) {
						gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEATS_PROCESSING);
					} else {
						gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_7SEATS_PROCESSING);
					}
				} else {
					if (taxi.getCarType() == 1) {
						gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEATS_NON_PROCESSING);
					} else {
						gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_7SEATS_NON_PROCESSING);
					}
				}
			} else {
				if (taxi.getCarType() == 1) {
					gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEATS_NON_PROCESSING);
				} else {
					gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_7SEATS_NON_PROCESSING);
				}
			}
			String status = "";
			if (taxi.getTrip() != null) {
				if (taxi.getTrip().getOrder() == null)
					status = "Không khách";
				else
					status = "Có khách";
			} else
				status = "Không khách";
			String content = "<table><tr><td style='width:65px; font-weight:bold'>Số tài</td><td style='font-weight: bold; color: green'>"
					+ taxi.getVehicle().getValue() + "</td></tr>"
					+ "<tr><td style='width:65px; font-weight:bold'>Tài xế</td><td style='font-weight: bold; color: green'>"
					+ taxi.getDriverName() + "</td></tr>"
					+ "<tr><td style='width:65px; font-weight:bold'>SĐT</td><td style='font-weight: bold; color: green'>"
					+ taxi.getDriver().getPhoneNumber() + "</td></tr>"
					+ "<tr><td style='width:65px; font-weight:bold'>Kinh độ</td><td style='font-weight: bold; color: green'>"
					+ taxi.getLattitute() + "</td></tr>"
					+ "<tr><td style='width:65px; font-weight:bold'>Vĩ độ</td><td style='font-weight: bold; color: green'>"
					+ taxi.getLongitude() + "</td></tr>"
					+ "<tr><td style='width:65px; font-weight:bold'>Trạng thái</td><td style='font-weight: bold; color: green'>"
					+ status + "</td></tr></table>";
			gmarker.setContent(content);
			gmarker.setParent(gmaps);
			gmaps.appendChild(gmarker);
			gmaps.panTo(taxi.getLattitute(), taxi.getLongitude());
			gmarker.setOpen(true);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CHANGE)) {
			if (event.getTarget().equals(cbAgent)) {
				if (cbAgent.getSelectedItem() != null) {
					Comboitem item = cbAgent.getSelectedItem();
					selectedAgent = (Agent) item.getAttribute(ATT_AGENT);
					lst_groups = (List<TaxiGroup>) item.getAttribute(ATT_TAXIGROUP);
					listbox.setModel(getListVehicle(selectedAgent, lst_groups));
				}
			}
		} else if (event.getName().equals(Events.ON_TIMER)) {
			if (selectedAgent != null) {
				listbox.setModel(getListVehicle(selectedAgent, lst_groups));
			}
			renderGmaker(gmaps);
		} else if (event.getName().equals(Events.ON_DOUBLE_CLICK)) {
			if (event.getTarget().equals(listbox)) {
				Listitem item = listbox.getSelectedItem();
				if (item != null) {
					DriverAppLocationModel att = (DriverAppLocationModel) item.getValue();
					Taxi taxi = att.getTaxi();
					panToTaxi(taxi);
				}
			}
		} else if (event.getName().equals("onMapClick")) {
			if (((MapMouseEvent) event).getReference() instanceof Gmarker) {
				Gmarker gmarker = (Gmarker) ((MapMouseEvent) event).getReference();
				gmarker.setOpen(!gmarker.isOpen());
			}
		}
	}

	public Timer getTimer() {
		return timer;
	}

}