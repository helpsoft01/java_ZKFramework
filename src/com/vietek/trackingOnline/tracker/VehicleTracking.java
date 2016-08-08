package com.vietek.trackingOnline.tracker;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.controller.AgentController;
import com.vietek.taxioperation.controller.TaxiGroupController;
import com.vietek.taxioperation.googlemapSearch.SearchGooglePlaces;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.ui.util.ComboboxRender;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.ui.util.ListItemRenderer;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.MapUtils;

public class VehicleTracking extends Div implements EventListener<Event> {

	private static final long serialVersionUID = 1L;

	private Hlayout hlayout;
	private Div container;
	private Gmaps monitormap;
	// private Textbox txtsearching;
	private Label totalvehicle;
	private Vlayout layoutSearchVehicle;
	private Combobox cbxvehiclegroup;
	private Combobox cbxvehiclestatus;
	private Combobox cbxsearchmenu;
	private Combobox cbAgent;
	private Combobox cbxmaptype;
	private Listbox listbox;
	private Combobox cbSearch;

	// private Button btnsearching;
	private Button btnreload;
	// private Button btndecription;
	private Button btndeviceinfo;
	private Button btnvehicleinfo;
	// private Button btnwarninginfo;
	private Button btnSetting;
	private MapSetting mapSetting;
	private Gmarker markerAddress;

	public VehicleTracking() {
		this.setVflex("1");
		hlayout = new Hlayout();
		hlayout.setVflex("100%");
		hlayout.setParent(this);

		container = new Div();
		container.setVflex("1");
		container.setHflex("1");
		container.setStyle("border: none");
		container.setParent(hlayout);

		initMap(container);
	}

	public void initMap(Div mapcontainer) {
		monitormap = new Gmaps();
		monitormap.setId("vehicle_tracking_map");
		monitormap.addEventListener("onMapClick", this);
		monitormap.setParent(mapcontainer);
		monitormap.setHflex("1");
		monitormap.setVflex("1");
		monitormap.setVersion("3.9");
		monitormap.setShowSmallCtrl(true);
		monitormap.setShowOverviewCtrl(false);
		monitormap.setShowTypeCtrl(false);
		monitormap.setCenter(21.0031545, 105.8446598);
		monitormap.setZoom(15);
		monitormap.setZindex(0);
		this.intMapTypeComponent(mapcontainer);
		this.initSearchingBar(SearchingType.VEHICLE);
		getMapConfig();
	}

	/*
	 * Changes Searching Bar'initatied
	 * 
	 * @param int type input conditions [type]
	 * 
	 * @return void
	 * 
	 */
	private void initSearchingBar(SearchingType type) {
		Div searchdiv = new Div();
		searchdiv.setId("vehicle_tracking_searchdiv");
		searchdiv.setParent(this);
		searchdiv.setSclass("veh-tracking-searchdiv");
		searchdiv.setZindex(1800);

		String[] titles = { "Xe", "Địa chỉ", "Điểm", "Tọa độ" };
		String[] images = { "/themes/images/v_veh.png", "/themes/images/v_add.png", "/themes/images/v_plc.png",
				"/themes/images/v_pot.png" };
		int[] values = { 0, 1, 2, 3 };
		String sClass = "veh-tracking-searchtype";
		String style = "";
		ComboboxRender render = new ComboboxRender();
		cbxsearchmenu = render.ComboboxRendering(titles, values, images, style, sClass, 85, 0);
		cbxsearchmenu.setParent(searchdiv);
		cbxsearchmenu.setReadonly(true);
		cbxsearchmenu.addEventListener(Events.ON_CHANGE, this);

		cbSearch = new Combobox();
		cbSearch.setSclass("veh-tracking-searchinput");
		cbSearch.setWidth("200px");
		cbSearch.setHeight("30px");
		cbSearch.setButtonVisible(false);
		cbSearch.setParent(searchdiv);
		cbSearch.setVisible(false);
		cbSearch.addEventListener(Events.ON_CHANGING, this);
		cbSearch.addEventListener(Events.ON_CHANGE, this);
		layoutSearchVehicle = new Vlayout();
		layoutSearchVehicle.setId("vehicle_tracking_vlayout_searchdiv");
		layoutSearchVehicle.setWidth("100%");
		layoutSearchVehicle.setStyle("margin-top: 5px;");
		layoutSearchVehicle.setParent(searchdiv);
		cbAgent = new Combobox();
		cbAgent.setSclass("cbb_vehicletracking");
		cbAgent.setStyle("height: 30px !important;");
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
		initColumns();

		totalvehicle = new Label("Tổng số xe: 0");
		totalvehicle.setStyle("text-shadow: none; font-weight: bold;");
		totalvehicle.setParent(searchdiv);
		Clients.evalJavaScript("fixListbooxTracking(0)");
		getAgent();
	}

	private void getMapConfig() {
		SysUser user = Env.getUser();
		if (user.getMapLat() != null && user.getMapLng() != null) {
			if (user.getMapLat() > 0 && user.getMapLng() > 0) {
				monitormap.setCenter(user.getMapLat(), user.getMapLng());
			}
		}
		if (user.getMapType() != null) {
			cbxmaptype.setSelectedIndex(user.getMapType());
			switch (user.getMapType()) {
			case 0:
				monitormap.setMapType("normal");
				break;
			case 1:
				monitormap.setMapType("satellite");
				break;
			case 2:
				monitormap.setMapType("hybrid");
				break;
			case 3:
				monitormap.setMapType("physical");
				break;
			}
		}
		if (user.getMapZoom() != null) {
			int zoom = user.getMapZoom();
			if (zoom >= 1 && zoom <= 19)
				monitormap.setZoom(zoom);
		}
	}

	private void initColumns() {
		ArrayList<GridColumn> columns = new ArrayList<GridColumn>();
		columns.add(new GridColumn("Biển số", 150, String.class, ""));
		columns.add(new GridColumn("V (km/h)", 60, String.class, ""));
		columns.add(new GridColumn("Thời gian", 100, String.class, ""));

		Listhead head = new Listhead();
		head.setParent(listbox);
		head.setMenupopup("auto");
		head.setSizable(true);
		Listheader header = new Listheader("Biển số");
		header.setStyle("text-align:center;");
		header.setWidth("108px");
		header.setParent(head);
		header = new Listheader("V (km/h)");
		header.setWidth("60px");
		header.setStyle("text-align:center;");
		header.setParent(head);
		header = new Listheader("Thời gian");
		header.setStyle("text-align:center;");
		header.setWidth("120px");
		header.setParent(head);
		ListItemRenderer<Class<?>> renderer = new ListItemRenderer<Class<?>>(columns);
		listbox.setItemRenderer(renderer);
	}

	private void initCoboboxStatus(Component parent) {
		cbxvehiclestatus = new Combobox();
		cbxvehiclestatus.setSclass("cbb_vehicletracking");
		cbxvehiclestatus.setStyle("margin: 5px 0px 5px 0px; width: 100%");
		cbxvehiclestatus.setAutocomplete(true);
		cbxvehiclestatus.setReadonly(true);
		cbxvehiclestatus.setParent(parent);
		Comboitem item = new Comboitem("Trạng thái xe");
		cbxvehiclestatus.appendChild(item);
		cbxvehiclestatus.setSelectedItem(item);
		item = new Comboitem("Bật máy");
		cbxvehiclestatus.appendChild(item);
		item = new Comboitem("Tắt máy");
		cbxvehiclestatus.appendChild(item);
		item = new Comboitem("Di chuyển");
		cbxvehiclestatus.appendChild(item);
		item = new Comboitem("Dừng đỗ");
		cbxvehiclestatus.appendChild(item);
		item = new Comboitem("Mất tín hiệu");
		cbxvehiclestatus.appendChild(item);
	}

	public void intMapTypeComponent(Div parent) {
		Div div = new Div();
		div.setStyle(
				"position: absolute !important; right: 10px !important; top: 10px !important; width: 150px; border: 1px green solid;");
		div.setParent(parent);
		btnSetting = new Button();
		btnSetting.setSclass("btn_vehicle_tracking_setting");
		btnSetting.setImage("./themes/images/setting_32.png");
		btnSetting.setParent(div);
		btnSetting.addEventListener(Events.ON_CLICK, this);
		cbxmaptype = new Combobox();
		// cbxmaptype.setHflex("true");
		cbxmaptype.setWidth("120px");
		cbxmaptype.setSclass("veh-tracking-maptype");
		cbxmaptype.setParent(div);
		cbxmaptype.setReadonly(true);
		Comboitem item = new Comboitem("Bản đồ");
		item.setValue(0);
		cbxmaptype.appendChild(item);
		item = new Comboitem("Vệ tinh");
		item.setValue(1);
		cbxmaptype.appendChild(item);
		item = new Comboitem("Kết hợp");
		item.setValue(2);
		cbxmaptype.appendChild(item);
		item = new Comboitem("Địa hình");
		item.setValue(3);
		cbxmaptype.appendChild(item);
		cbxmaptype.addEventListener(Events.ON_CHANGE, this);
	}

	private void getAgent() {
		AgentController agentController = (AgentController) ControllerUtils.getController(AgentController.class);
		List<Agent> agents = agentController.find("from Agent");
		if (agents.size() > 0) {
			for (Agent agent : agents) {
				Comboitem item = new Comboitem(agent.getAgentName());
				item.setAttribute("agent", agent);
				cbAgent.appendChild(item);
			}
		}
	}

	private void getGroup(Comboitem itemAgent) {
		Agent agent = (Agent) itemAgent.getAttribute("agent");
		cbxvehiclegroup.getChildren().clear();
		if (itemAgent.getAttribute("groups") == null) {
			TaxiGroupController controller = (TaxiGroupController) ControllerUtils
					.getController(TaxiGroupController.class);
			List<TaxiGroup> groups = controller.find("from TaxiGroup where agent=?", agent);
			itemAgent.setAttribute("groups", groups);
			if (groups.size() > 0) {
				for (TaxiGroup taxiGroup : groups) {
					Comboitem item = new Comboitem(taxiGroup.getName());
					item.setAttribute("group", taxiGroup);
					cbxvehiclegroup.appendChild(item);
				}
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
			}
		}
	}

	public void renderVehicle() {

	}

	@SuppressWarnings("unused")
	private String getSrcImage(double angle, int inTrip) {
		StringBuffer src = new StringBuffer("./themes/images/vehicles/");
		String deg = "000";
		int i = 0;
		while (i <= 360) {
			if (angle > i && angle < (i + 5)) {
				if ((angle - i) <= ((i + 5) - angle)) {
					deg = String.valueOf(i);
				} else
					deg = String.valueOf(i + 5);
				break;
			}
			i = +5;
		}
		if (inTrip == 1)
			src.append("intrip_").append(deg).append("deg");
		else {
			src.append("notrip_").append(deg).append("deg");
		}
		src.append(".png");
		return src.toString();
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CHANGE)) {
			if (event.getTarget().equals(cbxsearchmenu)) {
				cbSearch.setValue("");
				int index = cbxsearchmenu.getSelectedIndex();
				if (index == 0) {
					layoutSearchVehicle.setVisible(true);
					cbSearch.setVisible(false);
					Clients.evalJavaScript("fixListbooxTracking(0)");
				} else {
					layoutSearchVehicle.setVisible(false);
					cbSearch.setVisible(true);
					Clients.evalJavaScript("fixListbooxTracking(1)");
				}
			} else if (event.getTarget().equals(cbAgent)) {
				Comboitem item = cbAgent.getSelectedItem();
				getGroup(item);
			} else if (event.getTarget().equals(cbxmaptype)) {
				int index = cbxmaptype.getSelectedIndex();
				switch (index) {
				case 0:
					monitormap.setMapType("normal");
					break;
				case 1:
					monitormap.setMapType("satellite");
					break;
				case 2:
					monitormap.setMapType("hybrid");
					break;
				case 3:
					monitormap.setMapType("physical");
					break;
				}
			} else if (event.getTarget().equals(cbSearch)) {
				if (cbxsearchmenu.getSelectedIndex() == 1) {
					String val = cbSearch.getValue();
					LatLng latlng = MapUtils.convertAddresstoLatLng(val);
					if (latlng != null) {
						monitormap.setCenter(latlng.lat, latlng.lng);
						if (markerAddress == null) {
							markerAddress = new Gmarker();
							markerAddress.setStyle("transform: rotate(45deg);");
							markerAddress.setIconImage("./themes/images/4seats_top(48x48).png");
						}
						markerAddress.setLat(latlng.lat);
						markerAddress.setLng(latlng.lng);
						markerAddress.setParent(monitormap);
					}
				}
			}
		} else if (event.getName().equals(Events.ON_CHANGING)) {
			if (event.getTarget().equals(cbSearch)) {
				if (cbxsearchmenu.getSelectedIndex() == 1) {
					InputEvent inputEvent = (InputEvent) event;
					String dataSearch = inputEvent.getValue();
					SearchGooglePlaces a1 = new SearchGooglePlaces(cbSearch, dataSearch, cbSearch.getDesktop());
					Thread t1 = new Thread(a1);
					t1.setPriority(Thread.MAX_PRIORITY);
					t1.start();
				}
			}
		} else if (event.getName().equals("onMapClick")) {
			if (event.getTarget().equals(monitormap)) {

			}
		} else if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget().equals(btnSetting)) {
				if (mapSetting == null)
					mapSetting = new MapSetting();
				mapSetting.setParent(this);
				mapSetting.doModal();
			} else if (event.getTarget().equals(btnvehicleinfo)) {

			} else if (event.getTarget().equals(btndeviceinfo)) {
				Popup popup = new Popup();
				popup.setParent(this);
				Vlayout vlayout = new Vlayout();
				vlayout.setParent(popup);
				Hlayout hlayout = new Hlayout();
				hlayout.setParent(vlayout);
				Image image = new Image();
				image.setSrc("");
				image.setParent(hlayout);

			}
		}
	}

	public enum SearchingType {
		VEHICLE, ADDRESS, PLACES, LOCATION
	}

	public class MapSetting extends Window implements EventListener<Event> {
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