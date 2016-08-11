package com.vietek.trackingOnline.tracker;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.EnumLevelUser;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.common.VehicleApi;
import com.vietek.taxioperation.controller.TaxiGroupController;
import com.vietek.taxioperation.database.MongoAction;
import com.vietek.taxioperation.googlemapSearch.GoogleMapUntil;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.ArrangementTaxi;
import com.vietek.taxioperation.model.SysAction;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.SysFunction;
import com.vietek.taxioperation.model.SysGroup;
import com.vietek.taxioperation.model.SysGroupLine;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.controller.ArrangementTaxisDetail;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvent;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvents;
import com.vietek.taxioperation.ui.controller.vmap.VMaps;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;
import com.vietek.taxioperation.ui.controller.vmap.VPolyline;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.tracking.ui.utility.TrackingHistory;

public class TrackingOnlines extends Div implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VMaps vMaps;
	private Menupopup optionMap;
	private Div divMap;
	private Timer timer;
	private Map<Integer, VMarker> markers;

	// private Textbox txtsearching;
	private Label totalvehicle;
	private Vlayout layoutSearchVehicle;
	private Combobox cbxvehiclegroup;
	private Combobox cbxvehiclestatus;
	private Combobox cbxsearchmenu;
	private Combobox cbAgent;
	private Listbox listbox;
	private Combobox cbSearch;

	// private Button btnsearching;
	private Button btnreload;
	// private Button btndecription;
	private Button btndeviceinfo;
	private Button btnvehicleinfo;
	// private Button btnwarninginfo;
	// private Button btnSetting;
	private MapSetting mapSetting;
	private int currGroup;
	private static boolean isRender = false;
	private Menupopup listboxMenu;

	public TrackingOnlines() {
		this.setHflex("true");
		this.setVflex("true");
		markers = new HashMap<>();
		currGroup = -1;
		initMaps();
		initLeftPanel();
		initPopupMap();
		initTimer();
	}

	private void initMaps() {
		double lat = (Env.getUser().getMapLat() == null) ? 20.9683714 : Env.getUser().getMapLat();
		double lng = (Env.getUser().getMapLng() == null) ? 105.834901 : Env.getUser().getMapLng();
		int zoom = (Env.getUser().getMapZoom() == null) ? 15 : Env.getUser().getMapZoom();
		// int type = (Env.getUser().getMapType()==null) ? 0 :
		// Env.getUser().getMapType();
		divMap = new Div();
		divMap.setHflex("true");
		divMap.setVflex("true");
		divMap.setParent(this);
		vMaps = new VMaps(divMap);
		vMaps.setSclass("v_gmap");
		vMaps.setWidth("100%");
		vMaps.setHeight("100%");;
		vMaps.setCenter(new LatLng(lat, lng));
		vMaps.setZoom(zoom);
		vMaps.addEventListener(VMapEvents.ON_VMAP_RIGHT_CLICK, this);
		vMaps.addEventListener(VMapEvents.ON_VMARKER_CLICK, this);
		vMaps.addEventListener(VMapEvents.ON_VMAP_CLICK, this);
	}

	// Timer hien thi marker tren maps
	private void initTimer() {
		timer = new Timer();
		timer.setDelay(10000);
		timer.setRepeats(true);
		timer.setParent(this);
		timer.start();
		timer.addEventListener(Events.ON_TIMER, this);
	}

	private void initPopupMap() {
		optionMap = new Menupopup();
		optionMap.setParent(divMap);

		Menuitem item = new Menuitem("Xem địa chỉ", "./themes/images/map_info.png");
		item.setAttribute("action1", MapOption.DIA_CHI);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);

		item = new Menuitem("Lấy tọa độ", "./themes/images/map_info.png");
		item.setAttribute("action1", MapOption.TOA_DO);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);

		Menuseparator separator = new Menuseparator();
		separator.setParent(optionMap);

		item = new Menuitem("Cấu hình khởi động", "./themes/images/process_info.png");
		item.setAttribute("action1", MapOption.CAU_HINH);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		String funcClazz = "ArrangementTaxis";
		List<SysAction> actions = checkPermissionAction(funcClazz);
		if(actions.size()>0){
			item = new Menuitem("Quản lý điểm tiếp thị", "./themes/images/ruler.png");
			item.setAttribute("action1", MapOption.QL_DIEM);
			item.setParent(optionMap);
			item.addEventListener(Events.ON_CLICK, this);
			for(SysAction action : actions){
				if(action.getValue().equals("Add")){
					item = new Menuitem("Tạo điểm", "./themes/images/search_map.png");
					item.setAttribute("action1", MapOption.TAO_DIEM);
					item.setParent(optionMap);
					item.addEventListener(Events.ON_CLICK, this);
				}
			}
		}
		listboxMenu = new Menupopup();
		listboxMenu.setParent(listbox.getParent());
		
		Menu menu = new Menu("Xem hành trình");
		menu.setParent(listboxMenu);
		Menupopup menupopup = new Menupopup();
		menupopup.setParent(menu);
		item = new Menuitem("Trong 1 giờ");
		item.setAttribute("action2", "History");
		item.setAttribute("val", 1);
		item.setParent(menupopup);
		item.addEventListener(Events.ON_CLICK, this);
		item = new Menuitem("Trong 2 giờ");
		item.setAttribute("action2", "History");
		item.setAttribute("val", 2);
		item.setParent(menupopup);
		item.addEventListener(Events.ON_CLICK, this);
		item = new Menuitem("Trong 4 giờ");
		item.setAttribute("action2", "History");
		item.setAttribute("val", 4);
		item.setParent(menupopup);
		item.addEventListener(Events.ON_CLICK, this);
		item = new Menuitem("Trong 8 giờ");
		item.setAttribute("action2", "History");
		item.setAttribute("val", 8);
		item.setParent(menupopup);
		item.addEventListener(Events.ON_CLICK, this);
		item = new Menuitem("Tùy chọn");
		item.setAttribute("action2", "History");
		item.setAttribute("val", 0);
		item.setParent(menupopup);
		item.addEventListener(Events.ON_CLICK, this);
	}

	private void initLeftPanel() {
		Div searchdiv = new Div();
		searchdiv.setId("vehicle_tracking_searchdiv");
		searchdiv.setParent(this);
		searchdiv.setSclass("veh-tracking-searchdiv");
		searchdiv.setZindex(1);
		initComboboxSearch(searchdiv);

		cbSearch = new Combobox();
		cbSearch.setSclass("veh-tracking-searchinput");
		cbSearch.setWidth("200px");
		cbSearch.setHeight("30px");
		cbSearch.setButtonVisible(false);
		cbSearch.setAutodrop(true);
		cbSearch.setAutocomplete(true);
		cbSearch.setParent(searchdiv);
//		cbSearch.addEventListener(Events.ON_CHANGING, this);
		cbSearch.addEventListener(Events.ON_CHANGE, this);
		layoutSearchVehicle = new Vlayout();
		layoutSearchVehicle.setId("vehicle_tracking_vlayout_searchdiv");
		layoutSearchVehicle.setWidth("100%");
		layoutSearchVehicle.setStyle("margin-top: 5px;");
		layoutSearchVehicle.setParent(searchdiv);
		cbAgent = new Combobox();
		cbAgent.setSclass("cbb_vehicletracking");
		cbAgent.setStyle("height: 24px !important;");
		cbAgent.setHflex("true");
		cbAgent.setPlaceholder("-- Chọn chi nhánh --");
		cbAgent.setParent(layoutSearchVehicle);
		cbAgent.addEventListener(Events.ON_CHANGE, this);
		cbxvehiclegroup = new Combobox();
		cbxvehiclegroup.setSclass("cbb_vehicletracking");
		cbxvehiclegroup.setHflex("true");
		cbxvehiclegroup.setPlaceholder("-- Chọn đội xe --");
		cbxvehiclegroup.setParent(layoutSearchVehicle);
		cbxvehiclegroup.addEventListener(Events.ON_CHANGE, this);
		initCoboboxStatus(searchdiv);

		Div div = new Div();
		div.setStyle("width:100%; margin-top:5px; margin-bottom: 5px;");
		div.setParent(searchdiv);
		Label label = new Label("Danh sách xe");
		label.setStyle("font-size: 16px; font-weight: bold; color: green; text-shadow: none; margin-left: 10px;");
		label.setParent(div);
		Hlayout hlayout = new Hlayout();
		hlayout.setStyle("float: right");
		hlayout.setParent(div);
		btnreload = new Button();
		btnreload.setSclass("btn_detail_vehicle_tracking");
		btnreload.setImage("./themes/images/Refresh_24.png");
		btnreload.setParent(hlayout);
		btnreload.addEventListener(Events.ON_CLICK, this);
		btnvehicleinfo = new Button();
		btnvehicleinfo.setSclass("btn_detail_vehicle_tracking");
		btnvehicleinfo.setImage("./themes/images/car-32.png");
		btnvehicleinfo.setParent(hlayout);
		btnvehicleinfo.addEventListener(Events.ON_CLICK, this);
		btndeviceinfo = new Button();
		btndeviceinfo.setSclass("btn_detail_vehicle_tracking");
		btndeviceinfo.setImage("./themes/images/info_32.png");
		btndeviceinfo.setParent(hlayout);
		btndeviceinfo.addEventListener(Events.ON_CLICK, this);

		listbox = new Listbox();
		listbox.setId("listbox_vehicle_tracking");
		listbox.setSclass("listbox_vehicle_tracking");
		listbox.setWidth("100%");
		listbox.setVflex("true");
		listbox.setParent(searchdiv);
		listbox.addEventListener(Events.ON_CLICK, this);
		listbox.addEventListener(Events.ON_RIGHT_CLICK, this);
		initColumns();

		totalvehicle = new Label("Tổng số xe: 0");
		totalvehicle.setStyle("text-shadow: none; font-weight: bold;");
		totalvehicle.setParent(searchdiv);
		Clients.evalJavaScript("fixListbooxTracking(0)");
		getAgent();
	}
	
	private void initComboboxSearch(Component parent){
		cbxsearchmenu = new Combobox();
		cbxsearchmenu.setSclass("veh-tracking-searchtype");
		cbxsearchmenu.setWidth("90px");
		cbxsearchmenu.setParent(parent);
		cbxsearchmenu.setReadonly(true);
		cbxsearchmenu.addEventListener(Events.ON_CHANGE, this);
//		String[] titles = {"Xe", "Địa chỉ", "Điểm", "Tọa độ"};
//		String[] images = {
//						"/themes/images/v_veh.png", 
//						"/themes/images/v_add.png",
//						"/themes/images/v_plc.png",
//						"/themes/images/v_pot.png"
//						};
		
		Comboitem item = new Comboitem("Xe", "/themes/images/v_veh.png");
		item.setParent(cbxsearchmenu);
		item = new Comboitem("Điểm", "/themes/images/v_plc.png");
		item.setParent(cbxsearchmenu);
		cbxsearchmenu.setSelectedIndex(0);
		Events.postEvent(Events.ON_CHANGE, cbxsearchmenu, null);
	}

	private void initCoboboxStatus(Component parent) {
		cbxvehiclestatus = new Combobox();
		cbxvehiclestatus.setSclass("cbb_vehicletracking");
		cbxvehiclestatus.setStyle("margin: 5px 0px 5px 0px; width: 100%");
		cbxvehiclestatus.setAutocomplete(true);
		cbxvehiclestatus.setReadonly(true);
		cbxvehiclestatus.setParent(parent);
		Comboitem item = new Comboitem(TrackingStatus.TAT_CA.getValue());
		item.setValue(TrackingStatus.TAT_CA);
		cbxvehiclestatus.appendChild(item);
		cbxvehiclestatus.setSelectedItem(item);
		item = new Comboitem(TrackingStatus.BAT_MAY.getValue());
		item.setValue(TrackingStatus.BAT_MAY);
		cbxvehiclestatus.appendChild(item);
		item = new Comboitem(TrackingStatus.TAT_MAY.getValue());
		item.setValue(TrackingStatus.TAT_MAY);
		cbxvehiclestatus.appendChild(item);
		item = new Comboitem(TrackingStatus.DI_CHUYEN.getValue());
		item.setValue(TrackingStatus.DI_CHUYEN);
		cbxvehiclestatus.appendChild(item);
		item = new Comboitem(TrackingStatus.DUNG_DO.getValue());
		item.setValue(TrackingStatus.DUNG_DO);
		cbxvehiclestatus.appendChild(item);
		item = new Comboitem(TrackingStatus.MAT_TIN_HIEU.getValue());
		item.setValue(TrackingStatus.MAT_TIN_HIEU);
		cbxvehiclestatus.appendChild(item);
		cbxvehiclestatus.addEventListener(Events.ON_CHANGE, this);
	}

	private void initColumns() {
		Listhead head = new Listhead();
		head.setParent(listbox);
		head.setMenupopup("auto");
		head.setSizable(true);
		Listheader header = new Listheader("");
		header.setStyle("text-align:center;");
		header.setWidth("18px");
		header.setParent(head);
		header = new Listheader("Biển số");
		header.setStyle("text-align:center;");
		header.setWidth("108px");
		header.setParent(head);
		header = new Listheader("V (km/h)");
		header.setWidth("60px");
		header.setStyle("text-align:center;");
		header.setParent(head);
		header = new Listheader("Thời gian");
		header.setStyle("text-align:center;");
		header.setWidth("90px");
		header.sort(true);
		header.setParent(head);

	}

	@SuppressWarnings("unchecked")
	private void getAgent() {
		List<SysCompany> companies = (List<SysCompany>) Env.getContext(Env.LIST_COMPANY);
		if(companies != null && companies.size()>0){
			for (int i = 0; i < companies.size(); i++) {
				List<Agent> agents = VehicleApi.getListAgent(companies.get(i));
				if(agents.size() > 0){
					for (Agent agent : agents) {
						Comboitem item = new Comboitem(agent.getAgentName());
						item.setValue(agent);
						item.setAttribute("agent", agent);
						cbAgent.appendChild(item);
					}
					cbAgent.setSelectedIndex(0);
					Events.postEvent(Events.ON_CHANGE, cbAgent, null);
				}
			}
		}
	}

	private void clickMenuitem(MapOption option) throws InstantiationException, IllegalAccessException {
		switch (option) {
		case TIM_XE_VUNG:
			vMaps.startDrawPolygon();
			break;
		case TIM_XE_DIEM:

			break;
		case CHON_VUNG:

			break;
		case DIA_CHI:
			Window window = new Window();
			window.setSclass("tracking_window");
			window.setParent(this);
			window.setTitle("Địa chỉ");
			window.setClosable(true);
			String address = GoogleMapUntil.convertLatLongToAddrest(vMaps.getMousePosition().lat, 
					vMaps.getMousePosition().lng);
			Label label = new Label(address);
			label.setParent(window);

			window.doModal();
			break;
		case TOA_DO:
			window = new Window();
			window.setSclass("tracking_window");
			window.setParent(this);
			window.setTitle("Tọa độ");
			window.setClosable(true);
			Vlayout vlayout = new Vlayout();
			vlayout.setParent(window);
			String str = "Vĩ độ: " + vMaps.getMousePosition().lat;
			label = new Label(str);
			label.setParent(vlayout);
			str = "Kinh độ: " + vMaps.getMousePosition().lng;
			label = new Label(str);
			label.setParent(vlayout);
			window.doModal();
			break;
		case PHONG_TO:

			break;
		case THU_NHO:

			break;
		case KHOANG_CACH:

			break;
		case CHI_HUONG:

			break;
		case CAU_HINH:
			mapSetting = new MapSetting();
			mapSetting.setParent(this);
			mapSetting.doModal();
			break;
		case QL_DIEM:

			break;
		case TAO_DIEM:
			ArrangementTaxi model = ArrangementTaxi.class.newInstance();
			model.setAddress(GoogleMapUntil.convertLatLongToAddrest(vMaps.getMousePosition().lat, vMaps.getMousePosition().lng));
			model.setLatitude(vMaps.getMousePosition().lat);
			model.setLongitude(vMaps.getMousePosition().lng);
			model.setAgent(cbAgent.getSelectedItem().getValue());
			model.setIsActive(true);
			model.setIsMarketing(true);
			ArrangementTaxisDetail arrangementTaxisDetail = new ArrangementTaxisDetail(model, null);
			arrangementTaxisDetail.setModel(model);
			arrangementTaxisDetail.setTitle("Tạo điểm tiếp thị");
			arrangementTaxisDetail.setParent(this);
			arrangementTaxisDetail.doModal();
			break;
		}
	}

	private void getGroup(Comboitem itemAgent) {
		Agent agent = (Agent) itemAgent.getAttribute("agent");
		cbxvehiclegroup.getChildren().clear();
		if (itemAgent.getAttribute("groups") == null) {
			TaxiGroupController controller = (TaxiGroupController) ControllerUtils
					.getController(TaxiGroupController.class);
			List<TaxiGroup> groups = controller.find("from TaxiGroup where AgentID=?", agent.getId());
			itemAgent.setAttribute("groups", groups);
			if (groups.size() > 0) {
				for (TaxiGroup taxiGroup : groups) {
					Comboitem item = new Comboitem(taxiGroup.getName());
					item.setValue(taxiGroup);
					item.setAttribute("group", taxiGroup);
					cbxvehiclegroup.appendChild(item);
				}
				cbxvehiclegroup.setSelectedIndex(0);
				Events.postEvent(Events.ON_CHANGE, cbxvehiclegroup, null);
			}
		} else {
			@SuppressWarnings("unchecked")
			List<TaxiGroup> groups = (List<TaxiGroup>) itemAgent.getAttribute("groups");
			if (groups.size() > 0) {
				for (TaxiGroup taxiGroup : groups) {
					Comboitem item = new Comboitem(taxiGroup.getName());
					item.setAttribute("group", taxiGroup);
					cbxvehiclegroup.appendChild(item);
				}
				cbxvehiclegroup.setSelectedIndex(0);
				Events.postEvent(Events.ON_CHANGE, cbxvehiclegroup, null);
			}
		}
	}

	private void getGroupVehicles(TaxiGroup group) {
		clearListitem();
		List<Vehicle> vehicles = VehicleApi.getVehicle(group);
		if (vehicles != null && vehicles.size() > 0) {
			int totalVehicles = 0;
			for (int i = 0; i < vehicles.size(); i++) {
				Vehicle vehicle = vehicles.get(i);
				if (vehicle.getDeviceID() != null) {
					TrackingRDS2Json json = MapCommon.TRACKING_RDS.get(vehicle.getDeviceID());
					if (json != null) {
						totalVehicles++;
						TrackingOnlineModel model = new TrackingOnlineModel();
						model.setDeviceId(vehicle.getDeviceID());
						model.setTaxiNumber(vehicle.getTaxiNumber());
						model.setTime(json.getTimeLog());
						model.setSpeed(json.getMetterSpeed());
						Image image = OnlineInfo.getTrackingInfo(json).getImage();
						image.setStyle("width: 16px; height: 16px");
						model.setImage(image);
						addListitem(model, json);
					}
				}
			}
			GoogleMapUntil.scaleMap(vMaps);
			totalvehicle.setValue("Tổng số xe: " + String.valueOf(totalVehicles));
		}
	}

	private void addListitem(TrackingOnlineModel model, TrackingRDS2Json json) {
		
		Listitem item = new Listitem();
		TrackingVehicleInfo info = OnlineInfo.getTrackingInfo(json);
		item.setAttribute("engine", json.getEngine());
		item.setAttribute("speed", json.getGpsSpeed());
		item.setAttribute("deviceId", model.getDeviceId());
		item.setAttribute("vehicleId", json.getVehicleId());
		item.setAttribute("info", info);
		
		Vehicle vehicle = null;
		if(cbxsearchmenu.getSelectedIndex() == 0){
			if(cbSearch.getSelectedItem()!=null){
				vehicle = cbSearch.getSelectedItem().getValue();
				if(vehicle.getId() == json.getVehicleId()){
					listbox.insertBefore(listbox.getFirstChild(), item);
				} else {
					item.setParent(listbox);
				}
			} else {
				item.setParent(listbox);
			}
		} else
			item.setParent(listbox);

		Listcell listcell = new Listcell();
		listcell.appendChild(model.getImage());
		item.appendChild(listcell);

		listcell = new Listcell();
		listcell.appendChild(new Label(model.getTaxiNumber()));
		item.appendChild(listcell);

		listcell = new Listcell();
		listcell.appendChild(new Label(model.getSpeed().toString()));
		item.appendChild(listcell);

		String strDate = StringUtils.valueOfTimestamp(model.getTime(), "HH:mm");
		listcell = new Listcell();
		listcell.appendChild(new Label(strDate));
		item.appendChild(listcell);
		renderMarker(model, json);
	}

	private void clearListitem() {
		if (timer != null)
			timer.stop();
		if (!isRender) {
			while (listbox.getItemCount() > 0) {
				listbox.removeItemAt(listbox.getItemCount() - 1);
			}
		}
		if (timer != null)
			timer.start();
	}

	private void updateListbox() {
		if (currGroup >= 0) {
			isRender = true;
			for(Component comp: listbox.getChildren()){
				TrackingStatus status = cbxvehiclestatus.getSelectedItem().getValue();
				if(comp instanceof Listitem){
					int deviceId = (Integer)comp.getAttribute("deviceId");
					TrackingRDS2Json rds2Json = MapCommon.TRACKING_RDS.get(deviceId);
					TrackingVehicleInfo info = OnlineInfo.getTrackingInfo(rds2Json);
					comp.setAttribute("info", info);
					if(rds2Json!=null){
						comp.setAttribute("engine", rds2Json.getEngine());
						comp.setAttribute("speed", rds2Json.getGpsSpeed());
						switch (status) {
						case TAT_CA:
							comp.setVisible(true);
							markers.get(deviceId).setVisible(true);
							break;
						case BAT_MAY:
							if(rds2Json.getEngine()==0){
								comp.setVisible(false);
								markers.get(deviceId).setVisible(false);
							} else {
								comp.setVisible(true);
								markers.get(deviceId).setVisible(true);
							}
							break;
						case TAT_MAY:
							if(rds2Json.getEngine()==0){
								comp.setVisible(true);
								markers.get(deviceId).setVisible(true);
							} else {
								comp.setVisible(false);
								markers.get(deviceId).setVisible(false);
							}
							break;
						case DI_CHUYEN:
							if(rds2Json.getGpsSpeed()>=5){
								comp.setVisible(true);
								markers.get(deviceId).setVisible(true);
							} else {
								comp.setVisible(false);
								markers.get(deviceId).setVisible(false);
							}
							break;
						case DUNG_DO:
							if(rds2Json.getGpsSpeed()>=5){
								comp.setVisible(false);
								markers.get(deviceId).setVisible(false);
							} else {
								comp.setVisible(true);
								markers.get(deviceId).setVisible(true);
							}
							break;
						case MAT_TIN_HIEU:
							if(info.isLostDigital()){
								comp.setVisible(true);
								markers.get(deviceId).setVisible(true);
							} else {
								comp.setVisible(false);
								markers.get(deviceId).setVisible(false);
							}
							break;
						}
						Listcell listcell = (Listcell)comp.getChildren().get(0);
						listcell.getChildren().clear();
						Image image = info.getImage();
						listcell.appendChild(image);

						listcell = (Listcell) comp.getChildren().get(2);
						listcell.getChildren().clear();
						if (rds2Json.getGpsSpeed() > 5)
							listcell.appendChild(new Label(String.valueOf(rds2Json.getGpsSpeed())));
						else
							listcell.appendChild(new Label("0"));

						listcell = (Listcell) comp.getChildren().get(3);
						listcell.getChildren().clear();
						String strDate = StringUtils.valueOfTimestamp(rds2Json.getTimeLog(), "HH:mm");
						listcell.appendChild(new Label(strDate));
						TrackingOnlineModel model = new TrackingOnlineModel();
						model.setImage(image);
						renderMarker(new TrackingOnlineModel(), rds2Json);
					}
				}
			}
			isRender = false;
		}
	}

	private void updateListbox(TrackingStatus status) {
		switch (status) {
		case TAT_CA:
			int i = 0;
			for (Component comp : listbox.getChildren()) {
				if (comp instanceof Listitem) {
					comp.setVisible(true);
					int deviceId = Integer.parseInt(comp.getAttribute("deviceId") + "");
					TrackingVehicleInfo info = (TrackingVehicleInfo)comp.getAttribute("info");
					markers.get(deviceId).setVisible(true);
					markers.get(deviceId).setIconImage(info.getImageSrc());
					i++;
				}
			}
			totalvehicle.setValue("Tổng số xe: " + i);
			break;
		case BAT_MAY:
			i = 0;
			for (Component comp : listbox.getChildren()) {
				if (comp instanceof Listitem) {
					int deviceId = Integer.parseInt(comp.getAttribute("deviceId") + "");
					int engine = Integer.parseInt(comp.getAttribute("engine")+"");
					TrackingVehicleInfo info = (TrackingVehicleInfo)comp.getAttribute("info");
					markers.get(deviceId).setIconImage(info.getImageSrc());
					if(engine ==1){
						comp.setVisible(true);
						markers.get(deviceId).setVisible(true);
						i++;
					} else {
						comp.setVisible(false);
						markers.get(deviceId).setVisible(false);
					}
				}
			}
			totalvehicle.setValue("Tổng số xe: " + i);
			break;
		case TAT_MAY:
			i = 0;
			for (Component comp : listbox.getChildren()) {
				if (comp instanceof Listitem) {
					int deviceId = Integer.parseInt(comp.getAttribute("deviceId") + "");
					int engine = Integer.parseInt(comp.getAttribute("engine") + "");
					TrackingVehicleInfo info = (TrackingVehicleInfo)comp.getAttribute("info");
					markers.get(deviceId).setIconImage(info.getImageSrc());
					if (engine == 0) {
						comp.setVisible(true);
						markers.get(deviceId).setVisible(true);
						i++;
					} else {
						comp.setVisible(false);
						markers.get(deviceId).setVisible(false);
					}
				}
			}
			totalvehicle.setValue("Tổng số xe: " + i);
			break;
		case DI_CHUYEN:
			i = 0;
			for (Component comp : listbox.getChildren()) {
				if (comp instanceof Listitem) {
					int deviceId = Integer.parseInt(comp.getAttribute("deviceId") + "");
					int speed = Integer.parseInt(comp.getAttribute("speed") + "");
					TrackingVehicleInfo info = (TrackingVehicleInfo)comp.getAttribute("info");
					markers.get(deviceId).setIconImage(info.getImageSrc());
					if (speed >= 5) {
						comp.setVisible(true);
						markers.get(deviceId).setVisible(true);
						i++;
					} else {
						comp.setVisible(false);
						markers.get(deviceId).setVisible(false);
					}
				}
			}
			totalvehicle.setValue("Tổng số xe: " + i);
			break;
		case DUNG_DO:
			i = 0;
			for (Component comp : listbox.getChildren()) {
				if (comp instanceof Listitem) {
					int deviceId = Integer.parseInt(comp.getAttribute("deviceId") + "");
					int speed = Integer.parseInt(comp.getAttribute("speed") + "");
					TrackingVehicleInfo info = (TrackingVehicleInfo)comp.getAttribute("info");
					markers.get(deviceId).setIconImage(info.getImageSrc());
					if (speed < 5) {
						comp.setVisible(true);
						markers.get(deviceId).setVisible(true);
						i++;
					} else {
						comp.setVisible(false);
						markers.get(deviceId).setVisible(false);
					}
				}
			}
			totalvehicle.setValue("Tổng số xe: " + i);
			break;
		case MAT_TIN_HIEU:
			i = 0;
			for (Component comp : listbox.getChildren()) {
				if (comp instanceof Listitem) {
					int deviceId = Integer.parseInt(comp.getAttribute("deviceId") + "");
					TrackingVehicleInfo info = (TrackingVehicleInfo) comp.getAttribute("info");
					markers.get(deviceId).setIconImage(info.getImageSrc());
					if (info.isLostDigital()) {
						comp.setVisible(true);
						markers.get(deviceId).setVisible(true);
						i++;
					} else {
						comp.setVisible(false);
						markers.get(deviceId).setVisible(false);
					}
				}
			}
			totalvehicle.setValue("Tổng số xe: " + i);
			break;
		}
	}

	private void renderMarker(TrackingOnlineModel model, TrackingRDS2Json json) {
		int deviceId = json.getDeviceId();
		VMarker marker = markers.get(deviceId);
		if (marker == null) {
			marker = new VMarker(json.getLatitude(), json.getLongitude());
			marker.setId("vehicle_" + deviceId);
			marker.setLabel(model.getTaxiNumber());
			marker.setLabelClass("vmarker_label");
			marker.setIconImage(model.getImage().getSrc());
			marker.setParent(vMaps);
			if(json.getLatitude()== 0 && json.getLongitude()==0){
				marker.setVisible(false);
			}
			markers.put(deviceId, marker);
		}
		marker.setAngle(json.getAngle());
		if(json.getLatitude()!= 0 && json.getLongitude()!=0){
			marker.setPosition(new LatLng(json.getLatitude(), json.getLongitude()));
			marker.setVisible(true);
		}
		else {
			marker.setVisible(false);
		}
		if(model.getImage()!=null)
			marker.setIconImage(model.getImage().getSrc());
		if(marker.isOpen()){
			OnlineInfo info = new OnlineInfo(deviceId, marker);
			marker.setContent(info.getContent());
			vMaps.panTo(json.getLatitude(), json.getLongitude());
		}
	}
	
	private void insertBefore(Vehicle vehicle){
		try{
			int deviceId = -1;
			if(!isRender){
				if(listbox.getChildren().size()>0){
					for(Component comp : listbox.getChildren()){
						if(comp instanceof Listitem){
							int vehicleId = Integer.parseInt(comp.getAttribute("vehicleId")+"");
							if(vehicle.getId() == vehicleId){
								listbox.insertBefore(comp, listbox.getFirstChild());
								deviceId = Integer.parseInt(comp.getAttribute("deviceId")+"");
								break;
							}
						}
					}
				}
			}
			vMaps.closeAllInfo();
			if(deviceId>0){
				vMaps.setZoom(19);
				OnlineInfo info = new OnlineInfo(deviceId, markers.get(deviceId));
				markers.get(deviceId).setContent(info.getContent());
				markers.get(deviceId).setOpen(true);
				vMaps.setCenter(markers.get(deviceId).getPosition());
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	public List<SysAction> checkPermissionAction(String funcClazz) {
		SysUser user = Env.getUser();
		List<SysAction> lstAction = new ArrayList<SysAction>();
		if (!user.getUserName().equals(EnumLevelUser.ADMIN.toString())
				&& !user.getUserName().equals(EnumLevelUser.SUPER_USER.toString())) {
			Set<SysGroup> setGroup = user.getSysGroup();
			if (setGroup.size() > 0) {
				List<SysGroupLine> lstGroupLine = new ArrayList<SysGroupLine>();
				for (SysGroup sysGroup : setGroup) {
					Set<SysGroupLine> setLine = sysGroup.getSysGroupLines();
					for (SysGroupLine sysGroupLine : setLine) {
						SysFunction function = sysGroupLine.getSysFunction();
						String fuctionClazz = "com.vietek.taxioperation.ui.controller." + function.getClazz();
						if (fuctionClazz.equals(funcClazz)) {
							lstGroupLine.add(sysGroupLine);
						}
					}
				}
				
				for (SysGroupLine sysGroupLine : lstGroupLine) {
					Set<SysAction> setAction = sysGroupLine.getSysAction();
					lstAction.addAll(setAction);
				}
			} 

		} else {
			SysAction action = new SysAction();
			action.setValue("Add");
			lstAction.add(action);
			action = new SysAction();
			action.setValue("Edit");
			lstAction.add(action);
			action = new SysAction();
			action.setValue("Del");
			lstAction.add(action);
		}
		return lstAction;
	}
	
	List<Component> components;
	private void drawHistoryTracking(int hour, int deviceId){
		long currentTime = System.currentTimeMillis();
		components = new MongoAction().getViewHistory((currentTime - (1000*60*60*hour)), currentTime, deviceId);
		for(Component comp : components){
			comp.setParent(vMaps);
		}
	}
	
	VPolyline polyline;
	@Override
	public void onEvent(Event event) throws Exception {
		if(event.getName().equals(VMapEvents.ON_VMAP_RIGHT_CLICK)){
			if(components != null){
				for(Component comp : components){
					comp.setParent(null);
				}
			}
			optionMap.open(divMap);
//			polyline.setVisible(false);
		} else if(event.getName().equals(VMapEvents.ON_VMARKER_CLICK)){
			VMapEvent mapEvent = (VMapEvent)event;
			Iterator<?> keys = markers.keySet().iterator();
			while (keys.hasNext()) {
				int deviceId = (int)keys.next();
				VMarker marker = markers.get(deviceId);
				if(marker.equals(mapEvent.getComponent())){
					OnlineInfo info = new OnlineInfo(deviceId, marker);
					markers.get(deviceId).setContent(info.getContent());
					markers.get(deviceId).setOpen(true);
					break;
				}
			}
		} else if(event.getName().equals(VMapEvents.ON_VMAP_CLICK)) {
//			polyline = new VPolyline();
//			ArrayList<LatLng> latLngs = new ArrayList<>();
//			latLngs.add(new LatLng(20.971896, 105.838889));
//			latLngs.add(new LatLng(20.971675, 105.837479));
//			latLngs.add(new LatLng(20.971541, 105.835901));
//			latLngs.add(new LatLng(20.973290, 105.835867));
//			polyline.setPath(latLngs);
//			polyline.setColor("#33cc33");
//			polyline.setOpacity(0.8);
//			polyline.setWeight(3);
//			polyline.setParent(vMaps);
		} else if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget() instanceof Menuitem) {
				Menuitem item = (Menuitem) event.getTarget();
				if(item.getAttribute("action1") != null){
					MapOption option = (MapOption) item.getAttribute("action1");
					clickMenuitem(option);
				}
				else {
					if(item.getAttribute("action2").equals("History")){
						int i = Integer.parseInt(item.getAttribute("val")+"");
						if(listbox.getSelectedItem() != null){
							int vehicleId = Integer.parseInt(listbox.getSelectedItem().getAttribute("vehicleId")+"");
							if(i==0){
								TrackingHistory history = new TrackingHistory(new Date(System.currentTimeMillis() - (1000*60*60*24)), new Date(System.currentTimeMillis()), vehicleId);
								Window window = new Window();
								window.setTitle("Lịch sử hành trình");
								window.setClosable(true);
								window.setWidth("90%");
								window.setHeight("80%");
								history.setParent(window);
								window.setParent(this);
								window.doModal();
							} else {
								int deviceId = Integer.parseInt(listbox.getSelectedItem().getAttribute("deviceId")+"");
								drawHistoryTracking(i, deviceId);
							}
							
						}
						
					}
				}
			} else if (event.getTarget().equals(listbox)) {
				Listitem item = listbox.getSelectedItem();
				if(item!=null){
					vMaps.closeAllInfo();
					int deviceId = Integer.parseInt(item.getAttribute("deviceId") + "");
					VMarker marker = markers.get(deviceId);
					if(marker.getPosition().lat > 0 && marker.getPosition().lng > 0){
						vMaps.setZoom(19);
						OnlineInfo info = new OnlineInfo(deviceId, marker);
						TrackingVehicleInfo trackingVehicleInfo = (TrackingVehicleInfo)item.getAttribute("info");
						marker.setIconImage(trackingVehicleInfo.getImageSrc());
						marker.setContent(info.getContent());
						vMaps.panTo(marker.getPosition());
						marker.setOpen(true);
					}
				}
			} else if(event.getTarget().equals(btnreload)){
				if(cbxvehiclegroup.getSelectedItem() != null){
					TaxiGroup group = cbxvehiclegroup.getSelectedItem().getValue();
					getGroupVehicles(group);
				}
			}
		} else if(event.getName().equals(Events.ON_RIGHT_CLICK)) {
			if(event.getTarget() instanceof Listbox){
				Listitem item = listbox.getSelectedItem();
				if(item!=null){
					listboxMenu.open(listbox);
				}
			}
		} else if (event.getName().equals(Events.ON_TIMER)) {
			updateListbox();
		} else if (event.getName().equals(Events.ON_CHANGE)) {
			if(event.getTarget().equals(cbxsearchmenu)){
				int index = cbxsearchmenu.getSelectedIndex();
				if(index == 0){
					cbSearch.setPlaceholder("Chọn xe");
					@SuppressWarnings("unchecked")
					List<SysCompany> lstSysCompany = (List<SysCompany>) Env.getContext(Env.LIST_COMPANY);
					if(lstSysCompany != null && lstSysCompany.size() > 0){
						SysCompany[] arr = lstSysCompany.toArray(new SysCompany[lstSysCompany.size()]);
						List<Vehicle> listVehicle = VehicleApi.getVehicle(arr);
						cbSearch.setModel(new ListModelArray<>(listVehicle));
						cbSearch.setItemRenderer(new ComboitemRendererVehicles<>());
						
					}
				}
				else {
					cbSearch.setPlaceholder("Chọn điểm tiếp thị");
				}
			} else if (event.getTarget().equals(cbAgent)) {
				cbxvehiclegroup.setValue("");
				Comboitem item = cbAgent.getSelectedItem();
				if(item!=null){
					currGroup = -1;
					clearListitem();
					getGroup(item);
				}
			} else if (event.getTarget().equals(cbxvehiclegroup)) {
				vMaps.hideAllMarkers(true);
				Comboitem item = cbxvehiclegroup.getSelectedItem();
				TaxiGroup group = ((TaxiGroup) item.getAttribute("group"));
				currGroup = group.getId();
				getGroupVehicles(group);
			} else if (event.getTarget().equals(cbxvehiclestatus)) {
				TrackingStatus status = (TrackingStatus) cbxvehiclestatus.getSelectedItem().getValue();
				updateListbox(status);
				GoogleMapUntil.scaleMap(vMaps);
			} else if(event.getTarget().equals(cbSearch)){
				Comboitem item = cbSearch.getSelectedItem();
				if(item != null){
					if(cbxsearchmenu.getSelectedIndex() == 0){	// Tim xe
						Vehicle vehicle = item.getValue();
						TaxiGroup group = cbxvehiclegroup.getSelectedItem().getValue();
						if(!vehicle.getTaxiGroup().equals(group)){
							TaxiGroup vehicleGroup = vehicle.getTaxiGroup();
							if(!vehicleGroup.getAgent().equals(cbAgent.getSelectedItem().getValue())){
								if (!isRender) {
									if (listbox.getChildren().size() > 0) {
										for(Component comp : cbAgent.getChildren()){
											if(comp instanceof Comboitem){
												Agent agent = ((Comboitem)comp).getValue();
												if(vehicleGroup.getAgent().equals(agent)){
													cbAgent.setSelectedItem((Comboitem)comp);
													Events.postEvent(Events.ON_CHANGE, cbAgent, null);
													break;
												}
											}
										}
									}
								}
							}
						} else {
							if (!isRender) {
								if (listbox.getChildren().size()>0) {
									for(Component comp : listbox.getChildren()){
										if(comp instanceof Listitem){
											int id = Integer.parseInt(comp.getAttribute("vehicleId")+"");
											if(vehicle.getId() == id){
												listbox.insertBefore(comp, listbox.getFirstChild());
												break;
											}
										}
									}
								}
							}
						}
						insertBefore(vehicle);
//						if(vehicle.getTaxiGroup().equals(obj))
					} else {	// Tim diem tiep thi
						
					}
				}
			}
		}
	}

	public enum MapOption {
		TIM_XE_VUNG, TIM_XE_DIEM, CHON_VUNG, DIA_CHI, TOA_DO, PHONG_TO, THU_NHO, KHOANG_CACH, CHI_HUONG, CAU_HINH, QL_DIEM, TAO_DIEM
	}

	public enum TrackingStatus {
		TAT_CA("Trạng thái xe"), BAT_MAY("Bật máy"), TAT_MAY("Tắt máy"), DI_CHUYEN("Di chuyển"), DUNG_DO(
				"Dừng đỗ"), MAT_TIN_HIEU("Mất tín hiệu");
		private String value;

		private TrackingStatus(String val) {
			this.value = val;
		}

		public String getValue() {
			return value;
		}
	}

	private class MapSetting extends Window implements EventListener<Event> {
		private static final long serialVersionUID = 1L;
		private Combobox cbMapType;
		private Textbox txtLat;
		private Textbox txtLng;
		private Intbox zoom;
		private Button btnOK;
		private Button btnCancel;

		public MapSetting() {
			this.setTitle("Cấu hình hiển thị bản đồ");
			this.setWidth("300px");
			this.setClosable(true);
			this.setMinimized(false);
			this.setSclass("vehicle_tracking_map_setting");
			Hlayout hlayout = new Hlayout();
			hlayout.setStyle("margin: 5px 0px 5px 0px;");
			hlayout.setParent(this);
			Div div = new Div();
			div.setWidth("80px");
			div.setParent(hlayout);
			Label label = new Label("Loại bản đồ");
			label.setParent(div);
			cbMapType = new Combobox();
			cbMapType.setSclass("vehicle_tracking_cb_maptype");
			cbMapType.setHflex("true");
			cbMapType.setHeight("24px");
			cbMapType.setReadonly(true);
			cbMapType.setParent(hlayout);
			Comboitem item = new Comboitem("Bản đồ");
			cbMapType.appendChild(item);
			cbMapType.setSelectedItem(item);
			item = new Comboitem("Vệ tinh");
			cbMapType.appendChild(item);
			item = new Comboitem("Kết hợp");
			cbMapType.appendChild(item);
			item = new Comboitem("Địa hình");
			cbMapType.appendChild(item);
			cbMapType.addEventListener(Events.ON_CHANGE, this);

			hlayout = new Hlayout();
			hlayout.setStyle("margin: 5px 0px 5px 0px;");
			hlayout.setParent(this);
			div = new Div();
			div.setWidth("80px");
			div.setParent(hlayout);
			label = new Label("Vĩ độ");
			label.setParent(div);
			txtLat = new Textbox();
			txtLat.setValue(String.valueOf(CommonDefine.GoogleMap.MAP_LAT));
			txtLat.setHflex("true");
			txtLat.setParent(hlayout);
			txtLat.addEventListener(Events.ON_CHANGE, this);

			hlayout = new Hlayout();
			hlayout.setStyle("margin: 5px 0px 5px 0px;");
			hlayout.setParent(this);
			div = new Div();
			div.setWidth("80px");
			div.setParent(hlayout);
			label = new Label("Kinh độ");
			label.setParent(div);
			txtLng = new Textbox();
			txtLng.setValue(String.valueOf(CommonDefine.GoogleMap.MAP_LNG));
			txtLng.setConstraint("no empty,no negative");
			txtLng.setHflex("true");
			txtLng.setParent(hlayout);
			txtLng.addEventListener(Events.ON_CHANGE, this);
			hlayout = new Hlayout();
			hlayout.setStyle("margin: 5px 0px 5px 0px;");
			hlayout.setParent(this);
			div = new Div();
			div.setWidth("80px");
			div.setParent(hlayout);
			label = new Label("Mức zoom bản đồ");
			label.setParent(div);
			zoom = new Intbox(14);
			zoom.setHflex("true");
			zoom.setParent(hlayout);
			zoom.addEventListener(Events.ON_CHANGE, this);

			hlayout = new Hlayout();
			hlayout.setStyle("margin: 5px 0px 5px 85px;");
			hlayout.setParent(this);
			btnOK = new Button("Lưu", "./themes/images/save_16.png");
			btnOK.setSclass("btn_vehicle_tracking_map_config");
			btnOK.setStyle("background: green; color: white;");
			btnOK.setParent(hlayout);
			btnOK.addEventListener(Events.ON_CLICK, this);
			btnCancel = new Button("Hủy", "./themes/images/DeleteRed_16.png");
			btnCancel.setSclass("btn_vehicle_tracking_map_config");
			btnCancel.setStyle("color: white; background: salmon;");
			btnCancel.setParent(hlayout);
			btnCancel.addEventListener(Events.ON_CLICK, this);
		}

		private boolean validate(Textbox txtLat, Textbox txtLng, Intbox zoom) {
			boolean ok = false;
			String val = txtLat.getText();
			try {
				Double.parseDouble(val);
				ok = true;
			} catch (Exception ex) {
				txtLat.setErrorMessage("");
				ok = false;
			}
			if (ok) {
				val = txtLng.getText();
				try {
					Double.parseDouble(val);
					ok = true;
				} catch (Exception ex) {
					txtLng.setErrorMessage("");
					ok = false;
				}
			}
			if (ok) {
				if (zoom.getValue() != null) {
					int zom = zoom.getValue();
					if (zom < 1 || zom > 19) {
						zoom.setErrorMessage("Chọn mức zoom từ 1 - 19");
						ok = false;
					}
				} else {
					zoom.setErrorMessage("Chọn mức zoom từ 1 - 19");
					ok = false;
				}
			}
			return ok;
		}

		@Override
		public void onEvent(Event event) throws Exception {
			if (event.getName().equals(Events.ON_CLICK)) {
				if (event.getTarget().equals(btnOK)) {
					if (validate(txtLat, txtLng, zoom)) {
						SysUser user = Env.getUser();
						double lat = Double.parseDouble(txtLat.getText());
						user.setMapLat(lat);
						double lng = Double.parseDouble(txtLng.getText());
						user.setMapLng(lng);
						user.setMapZoom(zoom.getValue());
						user.setMapType(cbMapType.getSelectedIndex());
						user.save();
						Env.setUser(user);
						Clients.showNotification("Đã lưu cấu hình bản đồ", "info", null, "middle_center", 3000, true);
						this.onClose();
					}
				} else if (event.getTarget().equals(btnCancel)) {
					this.onClose();
				}
			} else if (event.getName().equals(Events.ON_CHANGE)) {
				if (event.getTarget().equals(txtLat) || event.getTarget().equals(txtLng)) {
					Textbox textbox = (Textbox) event.getTarget();
					String val = textbox.getText();
					try {
						Double.parseDouble(val);
					} catch (Exception ex) {
						textbox.setErrorMessage("Nhập không đúng định dạng số");
					}
				} else if (event.getTarget().equals(zoom)) {
					if (zoom.getValue() != null) {
						int val = zoom.getValue();
						if (val < 1 || val > 19) {
							zoom.setErrorMessage("Chọn mức zoom từ 1 - 19");
						}
					} else {
						zoom.setErrorMessage("Chọn mức zoom từ 1 - 19");
					}
				}
			}
		}
	}
}