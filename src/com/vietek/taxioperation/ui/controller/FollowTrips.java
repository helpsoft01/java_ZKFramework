package com.vietek.taxioperation.ui.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.Gpolyline;
import org.zkoss.gmaps.LatLng;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.West;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.DriverController;
import com.vietek.taxioperation.controller.TaxiOrderController;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.DriverAppTracking;
import com.vietek.taxioperation.model.DriverAppsFollowTripStatus;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.MapUtils;

public class FollowTrips extends Div implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Gmaps gmaps;
	private Textbox tbOrder;
	private Div divInfo;
	private Label lbCustomerName;
	private Hlayout hlCusPhone;
	private Label lbCustomerPhone;
	private Label lbTimeOrder;
	private Label lbBeginTime;
	private Label lbBeginOrderAddress;
	private Label lbBeginAddress;
	private Label lbEndOrderAddress;
	private Hlayout hlEndAddress;
	private Label lbRegisterTaxis;
	private Hlayout hlPickedTaxi;
	private Label lbPickTaxi;
	private Label lbDriverStaff;
	private Label lbDriverName;
	private Button btnBeginOrderVehicle;
	private Button btnRegisterOrderVehicle;
	private Label lbStatus;
	private Gmarker startMarker;
	private Gmarker endMarker;
	TaxiOrder order;
	Timestamp pickedTime;
	Map<String, List<Integer>> driversTrip;
	Map<Integer, HashMap<String, List<?>>> map;
	TaxiOrderController orderController;
	private final String PICKED_DRIVER = "picked_driver";
	private final String DRIVERS_BEGIN = "drivers_begin_order";
	private final String DRIVERS_START = "drivers_start_order";
	private final String DRIVER_STATUS_BEGIN = "driver_status_begin_order";
	private final String DRIVER_STATUS_START = "driver_status_start_order";
	private final String TRIP_TRACKING = "trip_tracking";

	public FollowTrips() {
		this.setStyle("width:100%; height: 100%");
		driversTrip = new HashMap<>();
		map = new HashMap<>();
		init();
		order = null;
	}

	public void init() {
		Borderlayout borderlayout = new Borderlayout();
		borderlayout.setWidth("100%");
		borderlayout.setHeight("100%");
		borderlayout.setParent(this);

		West west = new West();
		west.setSize("350px");
		west.setParent(borderlayout);
		Center center = new Center();
		center.setParent(borderlayout);
		initControls(west);
		initMaps(center);
	}

	private void initControls(Component parent) {
		Div div = new Div();
		div.setStyle("width: 100%; height: 100%; padding: 5px;");
		div.setParent(parent);
		Hlayout hlayout = new Hlayout();
		hlayout.setWidth("width: 100%; margin-top: 10px;");
		hlayout.setParent(div);
		Label label = new Label("Mã cuốc");
		label.setStyle("line-height: 24px;");
		label.setSclass("follow_trip_text_controls");
		label.setParent(hlayout);
		tbOrder = new Textbox();
		tbOrder.setHflex("true");
		tbOrder.setSclass("follow_trip_search");
		tbOrder.setPlaceholder("Ấn Enter để tìm kiếm");
		tbOrder.setParent(hlayout);
		tbOrder.addEventListener(Events.ON_OK, this);
		divInfo = new Div();
		divInfo.setWidth("100%");
		divInfo.setParent(div);
		Div div2 = new Div();
		div2.setStyle("width:100%; margin-top:20px; margin-bottom:10px; text-align:center;");
		div2.setParent(divInfo);
		label = new Label("Thông tin cuốc khách");
		label.setSclass("follow_trip_title_info");
		label.setParent(div2);
		hlayout = new Hlayout();
		hlayout.setStyle("width: 100%; margin-top:10px;");
		hlayout.setParent(divInfo);
		div2 = new Div();
		div2.setSclass("follow_trip_left_info");
		div2.setParent(hlayout);
		label = new Label("Khách hàng");
		label.setSclass("follow_trip_text_controls");
		label.setParent(div2);
		Div divComp = new Div();
		divComp.setHflex("true");
		divComp.setParent(hlayout);
		lbCustomerName = new Label();
		lbCustomerName.setSclass("follow_trip_text_info");
		lbCustomerName.setParent(divComp);

		hlCusPhone = new Hlayout();
		hlCusPhone.setStyle("width: 100%; margin-top:10px;");
		hlCusPhone.setParent(divInfo);
		div2 = new Div();
		div2.setSclass("follow_trip_left_info");
		div2.setParent(hlCusPhone);
		label = new Label("Số điện thoại");
		label.setSclass("follow_trip_text_controls");
		label.setParent(div2);
		divComp = new Div();
		divComp.setHflex("true");
		divComp.setParent(hlCusPhone);
		lbCustomerPhone = new Label();
		lbCustomerPhone.setSclass("follow_trip_text_info");
		lbCustomerPhone.setParent(divComp);

		hlayout = new Hlayout();
		hlayout.setStyle("width: 100%; margin-top:10px;");
		hlayout.setParent(divInfo);
		div2 = new Div();
		div2.setSclass("follow_trip_left_info");
		div2.setParent(hlayout);
		label = new Label("Thời gian đặt");
		label.setSclass("follow_trip_text_controls");
		label.setParent(div2);
		divComp = new Div();
		divComp.setHflex("true");
		divComp.setParent(hlayout);
		lbTimeOrder = new Label();
		lbTimeOrder.setSclass("follow_trip_text_info");
		lbTimeOrder.setParent(divComp);
		hlayout = new Hlayout();
		hlayout.setStyle("width: 100%; margin-top:10px;");
		hlayout.setParent(divInfo);
		div2 = new Div();
		div2.setSclass("follow_trip_left_info");
		div2.setParent(hlayout);
		label = new Label("Điểm đặt");
		label.setSclass("follow_trip_text_controls");
		label.setParent(div2);
		divComp = new Div();
		divComp.setHflex("true");
		divComp.setParent(hlayout);
		lbBeginOrderAddress = new Label();
		lbBeginOrderAddress.setSclass("follow_trip_text_info");
		lbBeginOrderAddress.setParent(divComp);
		btnBeginOrderVehicle = new Button("Xe quanh điểm đặt");
		btnBeginOrderVehicle.setSclass("btn_follow_trip_info");
		btnBeginOrderVehicle.setParent(divInfo);
		btnBeginOrderVehicle.addEventListener(Events.ON_CLICK, this);
		hlayout = new Hlayout();
		hlayout.setStyle("width: 100%; margin-top:10px;");
		hlayout.setParent(divInfo);
		div2 = new Div();
		div2.setSclass("follow_trip_left_info");
		div2.setParent(hlayout);
		label = new Label("Thời gian đón");
		label.setSclass("follow_trip_text_controls");
		label.setParent(div2);
		divComp = new Div();
		divComp.setHflex("true");
		divComp.setParent(hlayout);
		lbBeginTime = new Label();
		lbBeginTime.setSclass("follow_trip_text_info");
		lbBeginTime.setParent(divComp);

		hlayout = new Hlayout();
		hlayout.setStyle("width: 100%; margin-top:10px;");
		hlayout.setParent(divInfo);
		div2 = new Div();
		div2.setSclass("follow_trip_left_info");
		div2.setParent(hlayout);
		label = new Label("Điểm đón");
		label.setSclass("follow_trip_text_controls");
		label.setParent(div2);
		divComp = new Div();
		divComp.setHflex("true");
		divComp.setParent(hlayout);
		lbBeginAddress = new Label();
		lbBeginAddress.setSclass("follow_trip_text_info");
		lbBeginAddress.setParent(divComp);

		btnRegisterOrderVehicle = new Button("Xe quanh điểm đón");
		btnRegisterOrderVehicle.setSclass("btn_follow_trip_info");
		btnRegisterOrderVehicle.setParent(divInfo);
		btnRegisterOrderVehicle.addEventListener(Events.ON_CLICK, this);

		hlEndAddress = new Hlayout();
		hlEndAddress.setWidth("width: 100%; margin-top:10px;");
		hlEndAddress.setParent(divInfo);
		div2 = new Div();
		div2.setSclass("follow_trip_left_info");
		div2.setParent(hlEndAddress);
		label = new Label("Điểm trả");
		label.setSclass("follow_trip_text_controls");
		label.setParent(div2);
		divComp = new Div();
		divComp.setHflex("true");
		divComp.setParent(hlEndAddress);
		lbEndOrderAddress = new Label();
		lbEndOrderAddress.setSclass("follow_trip_text_info");
		lbEndOrderAddress.setParent(divComp);

		hlayout = new Hlayout();
		hlayout.setStyle("width: 100%; margin-top:10px;");
		hlayout.setParent(divInfo);
		div2 = new Div();
		div2.setSclass("follow_trip_left_info");
		div2.setParent(hlayout);
		label = new Label("Xe đăng ký đón");
		label.setSclass("follow_trip_text_controls");
		label.setParent(div2);
		divComp = new Div();
		divComp.setHflex("true");
		divComp.setParent(hlayout);
		lbRegisterTaxis = new Label();
		lbRegisterTaxis.setSclass("follow_trip_text_info");
		lbRegisterTaxis.setParent(divComp);

		hlPickedTaxi = new Hlayout();
		hlPickedTaxi.setStyle("width: 100%; margin-top:10px;");
		hlPickedTaxi.setParent(divInfo);
		div2 = new Div();
		div2.setSclass("follow_trip_left_info");
		div2.setParent(hlPickedTaxi);
		label = new Label("Xe đón");
		label.setSclass("follow_trip_text_controls");
		label.setParent(div2);
		Vlayout vlayout = new Vlayout();
		vlayout.setHflex("true");
		vlayout.setParent(hlPickedTaxi);
		lbPickTaxi = new Label();
		lbPickTaxi.setSclass("follow_trip_text_info");
		lbPickTaxi.setParent(vlayout);
		lbDriverStaff = new Label();
		lbDriverStaff.setSclass("follow_trip_text_info");
		lbDriverStaff.setParent(vlayout);
		lbDriverName = new Label();
		lbDriverName.setSclass("follow_trip_text_info");
		lbDriverName.setParent(vlayout);

		hlayout = new Hlayout();
		hlayout.setStyle("width: 100%; margin-top:5px;");
		hlayout.setParent(divInfo);
		div2 = new Div();
		div2.setSclass("follow_trip_left_info");
		div2.setParent(hlayout);
		label = new Label("Trạng thái");
		label.setSclass("follow_trip_text_controls");
		label.setParent(div2);
		lbStatus = new Label();
		lbStatus.setSclass("follow_trip_text_info");
		lbStatus.setParent(hlayout);
	}

	private void initMaps(Component parent) {
		gmaps = new Gmaps();
		gmaps.setVersion("3.9");
		gmaps.setHflex("1");
		gmaps.setVflex("1");
		gmaps.setShowSmallCtrl(true);
		gmaps.setParent(parent);
		gmaps.setCenter(new LatLng(10.7774286, 106.7022481));
		gmaps.addEventListener("onMapClick", this);
	}

	private void viewInfo(TaxiOrder taxiOrder) {
		String startAddress = "";
		String endAddress = "";
		LatLng startPoint = null;
		LatLng endPoint = null;
		if (!divInfo.isVisible())
			divInfo.setVisible(true);
		if (taxiOrder.getCustomer() != null) {
			Customer customer = taxiOrder.getCustomer();
			lbCustomerName.setValue(customer.getName());
			lbCustomerPhone.setValue(customer.getPhoneNumber());
		} else {
			lbCustomerName.setValue("Khách vãng lai");
			if (taxiOrder.getPhoneNumber() != null) {
				lbCustomerPhone.setValue(taxiOrder.getPhoneNumber());
			}
		}
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String strDate = dateFormat.format(new Date(taxiOrder.getBeginOrderTime().getTime()));
		lbTimeOrder.setValue(strDate);
		if (StringUtils.isNotEmpty(taxiOrder.getBeginOrderAddress())) {
			startAddress = taxiOrder.getBeginOrderAddress();
			lbBeginOrderAddress.setValue(startAddress);
			if (taxiOrder.getBeginOrderLat() > 0 && taxiOrder.getBeginOrderLon() > 0) {
				startPoint = new LatLng(taxiOrder.getBeginOrderLat(), taxiOrder.getBeginOrderLon());
			}
		} else {
			if (taxiOrder.getBeginOrderLat() > 0 && taxiOrder.getBeginOrderLon() > 0) {
				startPoint = new LatLng(taxiOrder.getBeginOrderLat(), taxiOrder.getBeginOrderLon());
				String add = MapUtils.convertLatLongToAddrest(taxiOrder.getEndLat(), taxiOrder.getEndLon());
				;
				if (StringUtils.isNotEmpty(add)) {
					lbBeginOrderAddress.setValue(add);
					startAddress = add;
				}
			}
		}
		if (StringUtils.isNotEmpty(taxiOrder.getBeginAddress())) {
			startAddress = taxiOrder.getBeginAddress();
			lbBeginAddress.setValue(startAddress);
			if (taxiOrder.getBeginLat() > 0 && taxiOrder.getBeginLon() > 0) {
				startPoint = new LatLng(taxiOrder.getBeginLat(), taxiOrder.getBeginLon());
			}
		} else {
			if (taxiOrder.getBeginLat() > 0 && taxiOrder.getBeginLon() > 0) {
				startPoint = new LatLng(taxiOrder.getBeginLat(), taxiOrder.getBeginLon());
				String add = MapUtils.convertLatLongToAddrest(taxiOrder.getEndLat(), taxiOrder.getEndLon());
				if (StringUtils.isNotEmpty(add)) {
					lbBeginAddress.setValue(add);
					startAddress = add;
				}
			}
		}
		if (taxiOrder.getBeginTime() != null) {
			strDate = dateFormat.format(new Date(taxiOrder.getBeginTime().getTime()));
			lbBeginTime.setValue(strDate);
		}
		if (StringUtils.isNotEmpty(taxiOrder.getEndOrderAddress())) {
			lbEndOrderAddress.setValue(taxiOrder.getEndOrderAddress());
			endAddress = taxiOrder.getEndOrderAddress();
			if (taxiOrder.getEndOrderLat() > 0 && taxiOrder.getEndOrderLon() > 0) {
				endPoint = new LatLng(taxiOrder.getEndOrderLat(), taxiOrder.getEndOrderLon());
			}
		} else {
			if (taxiOrder.getEndOrderLat() > 0 && taxiOrder.getEndOrderLon() > 0) {
				endPoint = new LatLng(taxiOrder.getEndOrderLat(), taxiOrder.getEndOrderLon());
				String add = MapUtils.convertLatLongToAddrest(taxiOrder.getEndLat(), taxiOrder.getEndLon());
				if (StringUtils.isNotEmpty(add)) {
					lbEndOrderAddress.setValue(add);
					endAddress = add;
				}
			}
		}
		if (StringUtils.isNotEmpty(taxiOrder.getEndAddress())) {
			lbEndOrderAddress.setValue(taxiOrder.getEndAddress());
			endAddress = taxiOrder.getEndAddress();
			if (taxiOrder.getEndLat() > 0 && taxiOrder.getEndLon() > 0) {
				endPoint = new LatLng(taxiOrder.getEndLat(), taxiOrder.getEndLon());
			}
		} else {
			if (taxiOrder.getEndLat() > 0 && taxiOrder.getEndLon() > 0) {
				endPoint = new LatLng(taxiOrder.getEndLat(), taxiOrder.getEndLon());
				String add = MapUtils.convertLatLongToAddrest(taxiOrder.getEndLat(), taxiOrder.getEndLon());
				if (StringUtils.isNotEmpty(add)) {
					lbEndOrderAddress.setValue(add);
					endAddress = add;
				}
			}
		}
		if (taxiOrder.getRegistedTaxis().size() > 0) {
			StringBuffer vehicles = new StringBuffer();
			for (Vehicle vehicle : taxiOrder.getRegistedTaxis()) {
				vehicles.append(vehicle.getValue()).append(" - ").append(vehicle.getTaxiNumber()).append(", ");
			}
			if (vehicles.toString().length() > 2)
				vehicles.setLength(vehicles.length() - 2);
			lbRegisterTaxis.setValue(vehicles.toString());
		}

		if (taxiOrder.getPickedTaxi() != null) {
			hlPickedTaxi.setVisible(true);
			lbPickTaxi.setValue(taxiOrder.getPickedTaxi().getValue());
		} else if (taxiOrder.getVehicle() != null) {
			hlPickedTaxi.setVisible(true);
			lbPickTaxi.setValue(taxiOrder.getVehicle().getValue() + " - " + taxiOrder.getVehicle().getTaxiNumber());
		} else {
			hlPickedTaxi.setVisible(false);
		}
		if (taxiOrder.getDriver() != null) {
			lbDriverStaff.setValue("Mã NV: " + taxiOrder.getDriver().getStaffCard().toString());
			lbDriverName.setValue(taxiOrder.getDriver().getName());
		}
		String status = "";
		switch (taxiOrder.getStatus()) {
		case 0:
			status = "Hủy";
			break;
		case 1:
			status = "Mới";
			break;
		case 2:
			status = "Đăng ký đón";
			break;
		case 3:
			status = "Đã đón khách";
			break;
		case 4:
			status = "Đã trả khách";
			break;
		case 5:
			status = "Đọc đàm";
			break;
		}
		lbStatus.setValue(status);
		renderGmarker(startAddress, startPoint, endAddress, endPoint);
	}

	private void renderGmarker(String startAddress, LatLng startPoint, String endAddress, LatLng endPoint) {
		gmaps.getChildren().clear();
		if (startPoint != null) {
			if (startMarker == null) {
				startMarker = new Gmarker();
			}
			startMarker.setIconImage("./themes/images/marker_start_32.png");
			startMarker.setLat(startPoint.getLatitude());
			startMarker.setLng(startPoint.getLongitude());
			startMarker.setContent(startAddress);
			startMarker.setParent(gmaps);
		}
		if (endPoint != null) {
			if (endMarker == null) {
				endMarker = new Gmarker();
			}
			endMarker.setIconImage("./themes/images/marker_end_32.png");
			endMarker.setLat(endPoint.getLatitude()); // 10.7774286, 106.7022481
			endMarker.setLng(endPoint.getLongitude());
			endMarker.setContent(endAddress);
			endMarker.setParent(gmaps);
		}
		MapUtils.scaleMap(gmaps);
	}

	private void clearGmaps() {
		gmaps.getChildren().clear();
		if (startMarker != null)
			startMarker.setParent(gmaps);
		if (endMarker != null)
			endMarker.setParent(gmaps);
	}

	private void renderDriverMarker(List<DriverAppTracking> trackings, int tripID, int numberNonProcess) {
		clearGmaps();
		if (trackings.size() > 0) {
			for (int i = 0; i < trackings.size(); i++) {
				DriverAppTracking tracking = trackings.get(i);
				Gmarker gmarker = new Gmarker(
						"<p style='color: green; font-weight: bold; margin: 0px;'>" + tracking.getDriver().getName()
								+ "</p><br/><p style='text-align: center; font-weight: bold; margin: 0px;'>"
								+ tracking.getDriver().getPhoneNumber() + "</p>",
						tracking.getLatitude(), tracking.getLongitude());
				gmarker.setId(String.valueOf(tracking.getDriver().getId()));
				gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEATS_NON_PROCESSING);
				if (numberNonProcess > 0) {
					@SuppressWarnings("unchecked")
					List<Integer> driversID = (List<Integer>) map.get(order.getId()).get(DRIVER_STATUS_BEGIN);
					int index = driversID.indexOf(tracking.getDriver().getId());
					if (index < numberNonProcess)
						gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEARS_CANCEL_TRIP); // ko
																								// nhan
																								// cuoc
					else
						gmarker.setIconImage(CommonDefine.TaxiMarker.ICON_4SEATS_NON_PROCESSING); // chua
																									// nhan
																									// duoc
																									// cuoc
				}
				gmarker.setParent(gmaps);
				gmarker.setOpen(true);
			}
		} else {
			Clients.showNotification("Không có dữ liệu", "error", null, "middle_center", 3000, true);
		}

	}

	// private void showVehiclesOnConfirmTrip(TaxiOrder taxiOrder){
	// clearGmaps();
	// if(taxiOrder.getBeginOrderLat()>0 && taxiOrder.getBeginOrderLon()>0){
	// try {
	// Session session = ControllerUtils.getCurrentSession();
	// Query query = session.createQuery("from DriverAppsFollowTripStatus where
	// order.id="+taxiOrder.getId()+" AND status=2");
	// @SuppressWarnings("unchecked")
	// List<DriverAppsFollowTripStatus> appsFollowTripStatus =query.list();
	// if(appsFollowTripStatus!=null){
	// if(appsFollowTripStatus.size()>0){
	// DriverAppsFollowTripStatus followTripStatus =
	// appsFollowTripStatus.get(0);
	// if(followTripStatus.getLst_driver_id().length()>0){
	// List<String> list = new
	// ArrayList<String>(Arrays.asList(followTripStatus.getLst_driver_id().split(",")));
	// List<Integer> driversWhilePick = new ArrayList<Integer>();
	// for(int i=0; i<list.size();i++){
	// driversWhilePick.add(Integer.valueOf(list.get(i)));
	// }
	// driversTrip.put(taxiOrder.getId()+"-2", driversWhilePick);
	// List<Integer> driversBeginTrip=new ArrayList<>();
	// if(driversTrip.get(taxiOrder.getId())==null){
	// query = session.createQuery("from DriverAppsFollowTripStatus where
	// order.id="+taxiOrder.getId()+" AND status=1");
	// @SuppressWarnings("unchecked")
	// List<DriverAppsFollowTripStatus> driverFollowTrip = query.list();
	// if(driverFollowTrip!=null){
	// DriverAppsFollowTripStatus followTripStatus1 = driverFollowTrip.get(0);
	// list = new
	// ArrayList<String>(Arrays.asList(followTripStatus1.getLst_driver_id().split(",")));
	// for(int i=0; i<list.size();i++){
	// driversBeginTrip.add(Integer.valueOf(list.get(i)));
	// }
	// }
	// }
	// driversTrip.put(taxiOrder.getId()+"-1",driversBeginTrip);
	// if(driversWhilePick.size()>0){
	// List<DriverAppTracking> trackings=new ArrayList<>();
	// for(int i=0; i<driversWhilePick.size(); i++){
	// query = session.createQuery("from DriverAppTracking where
	// driver.id=(:DriverID) AND time<=(:timeOrder) AND latitude>0 AND longitude
	// >0 ORDER BY time DESC");
	// query.setParameter("DriverID", driversWhilePick.get(i));
	// query.setParameter("timeOrder", followTripStatus.getTime());
	// query.setMaxResults(1);
	// @SuppressWarnings("unchecked")
	// List<DriverAppTracking> listTracking = query.list();
	// if(listTracking !=null && listTracking.size()>0){
	// trackings.add(listTracking.get(0));
	// }
	// }
	// int pickNum = getPickDriverNumber(driversBeginTrip, driversWhilePick);
	// if(trackings.size()>0){
	// renderDriverMarker(trackings, taxiOrder.getId(), pickNum);
	// }
	// }
	// }
	// }
	// }
	// } catch (Exception e) {viewInfo(order);
	//
	// }
	// }
	// }

	private void getOtherInfo(TaxiOrder taxiOrder) {
		if (taxiOrder.getBeginOrderLat() > 0 && taxiOrder.getBeginOrderLon() > 0) {
			HashMap<String, List<?>> info;
			if (map.get(taxiOrder.getId()) != null) {
				info = map.get(taxiOrder.getId());
			} else {
				info = new HashMap<>();
				Session session = ControllerUtils.getCurrentSession();
				try {
					Query query = session.createQuery(
							"from DriverAppsFollowTripStatus where order.id=" + taxiOrder.getId() + " AND status=1");
					@SuppressWarnings("unchecked")
					List<DriverAppsFollowTripStatus> appsFollowTripStatus1 = query.list();
					query = session.createQuery(
							"from DriverAppsFollowTripStatus where order.id=" + taxiOrder.getId() + " AND status=2");
					@SuppressWarnings("unchecked")
					List<DriverAppsFollowTripStatus> appsFollowTripStatus2 = query.list();
					if (appsFollowTripStatus1 != null) {
						if (appsFollowTripStatus1.size() > 0) {
							DriverAppsFollowTripStatus followTripStatus = appsFollowTripStatus1.get(0);
							if (followTripStatus.getLst_driver_id().length() > 0) {
								List<String> list = new ArrayList<String>(
										Arrays.asList(followTripStatus.getLst_driver_id().split(",")));
								List<Integer> drivers = new ArrayList<Integer>();
								for (int i = 0; i < list.size(); i++) {
									drivers.add(Integer.valueOf(list.get(i)));
								}
								info.put(DRIVERS_BEGIN, drivers);
								if (drivers.size() > 0) {
									List<DriverAppTracking> trackings = new ArrayList<>();
									for (int i = 0; i < drivers.size(); i++) {
										query = session.createQuery(
												"from DriverAppTracking where driver.id=(:DriverID) AND time<=(:timeOrder) AND latitude>0 AND longitude >0 ORDER BY time DESC");
										query.setParameter("DriverID", drivers.get(i));
										query.setParameter("timeOrder", followTripStatus.getTime());
										query.setMaxResults(1);
										@SuppressWarnings("unchecked")
										List<DriverAppTracking> listTracking = query.list();
										if (listTracking != null && listTracking.size() > 0) {
											trackings.add(listTracking.get(0));
										}
									}
									info.put(DRIVER_STATUS_BEGIN, trackings);
								}
							}
						}
					}
					if (appsFollowTripStatus2 != null) {
						if (appsFollowTripStatus2.size() > 0) {
							DriverAppsFollowTripStatus followTripStatus = appsFollowTripStatus2.get(0);
							if (followTripStatus.getLst_driver_id().length() > 0) {
								List<String> list = new ArrayList<String>(
										Arrays.asList(followTripStatus.getLst_driver_id().split(",")));
								List<Integer> driversWhilePick = new ArrayList<Integer>();
								for (int i = 0; i < list.size(); i++) {
									driversWhilePick.add(Integer.valueOf(list.get(i)));
								}
								info.put(DRIVERS_START, driversWhilePick);
								@SuppressWarnings("unchecked")
								List<Integer> driversBeginTrip = (List<Integer>) info.get(DRIVERS_BEGIN);
								if (driversWhilePick.size() > 0) {
									List<DriverAppTracking> trackings = new ArrayList<>();
									for (int i = 0; i < driversWhilePick.size(); i++) {
										query = session.createQuery(
												"from DriverAppTracking where driver.id=(:DriverID) AND time<=(:timeOrder) AND latitude>0 AND longitude >0 ORDER BY time DESC");
										query.setParameter("DriverID", driversWhilePick.get(i));
										query.setParameter("timeOrder", followTripStatus.getTime());
										query.setMaxResults(1);
										@SuppressWarnings("unchecked")
										List<DriverAppTracking> listTracking = query.list();
										if (listTracking != null && listTracking.size() > 0) {
											trackings.add(listTracking.get(0));
										}
									}
									info.put(DRIVER_STATUS_START, trackings);
									int pickNum = getPickDriverNumber(driversBeginTrip, driversWhilePick);
									int driverID = driversBeginTrip.get(pickNum);
									Driver driver = getDriver(driverID);
									List<Driver> drivers = new ArrayList<>();
									drivers.add(driver);
									info.put(PICKED_DRIVER, drivers);
									getLineTrip(driver, order, session, info, followTripStatus.getTime());
								}
							}
						}
					}
					session.close();
				} catch (Exception ex) {
					AppLogger.logDebug.error("Loi cmnr", ex);
				}
			}
			map.put(taxiOrder.getId(), info);
		} else {
			Clients.showNotification("Không lấy được vị trí điểm đặt", "warm", null, "middle-center", 3000, true);
		}
	}

	private Driver getDriver(int id) {
		DriverController controller = (DriverController) ControllerUtils.getController(DriverController.class);
		Driver driver = controller.get(Driver.class, id);
		return driver;
	}

	private int getPickDriverNumber(List<Integer> drivers1, List<Integer> drivers2) {
		int num = -1;
		for (int i = 0; i < drivers1.size(); i++) {
			if (!drivers2.contains(drivers1.get(i))) {
				num = i;
				break;
			}
		}
		return num;
	}

	private void showBeginTrip() {
		if (order != null) {
			if (map.get(order.getId()) != null) {
				if (map.get(order.getId()).get(DRIVER_STATUS_BEGIN) != null) {
					@SuppressWarnings("unchecked")
					List<DriverAppTracking> trackingBegin = (List<DriverAppTracking>) map.get(order.getId())
							.get(DRIVER_STATUS_BEGIN);

					renderDriverMarker(trackingBegin, order.getId(), -1);
				} else {
					Clients.showNotification("Không có dữ liệu hiển thị", "error", null, "middle_center", 3000, true);
				}
			} else {
				Clients.showNotification("Không có dữ liệu hiển thị", "error", null, "middle_center", 3000, true);
			}
		}
	}

	private void showPickedTrip() {
		if (order != null) {
			if (map.get(order.getId()) != null) {
				if (map.get(order.getId()).get(DRIVER_STATUS_START) != null) {
					@SuppressWarnings("unchecked")
					List<DriverAppTracking> trackingBegin = (List<DriverAppTracking>) map.get(order.getId())
							.get(DRIVER_STATUS_START);
					if (map.get(order.getId()).get(DRIVER_STATUS_BEGIN) != null) {
						@SuppressWarnings("unchecked")
						List<Integer> driversID = (List<Integer>) map.get(order.getId()).get(DRIVER_STATUS_BEGIN);
						int index = 0;
						try {
							@SuppressWarnings("unchecked")
							Driver driver = ((List<Driver>) map.get(order.getId()).get(PICKED_DRIVER)).get(0);
							index = driversID.indexOf(driver.getId());
						} catch (Exception ex) {
							index = -1;
						}
						renderDriverMarker(trackingBegin, order.getId(), index);
					} else {
						Clients.showNotification("Không có dữ liệu hiển thị", "error", null, "middle_center", 3000,
								true);
					}
				} else {
					Clients.showNotification("Không có dữ liệu hiển thị", "error", null, "middle_center", 3000, true);
				}
			} else {
				Clients.showNotification("Không có dữ liệu hiển thị", "error", null, "middle_center", 3000, true);
			}
		}
	}

	private void clearText() {
		lbCustomerName.setValue("");
		;
		lbCustomerPhone.setValue("");
		lbTimeOrder.setValue("");
		lbBeginTime.setValue("");
		lbBeginOrderAddress.setValue("");
		lbBeginAddress.setValue("");
		lbEndOrderAddress.setValue("");
		lbRegisterTaxis.setValue("");
		lbPickTaxi.setValue("");
		lbDriverStaff.setValue("");
		lbDriverName.setValue("");
		lbStatus.setValue("");
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_OK)) {
			if (event.getTarget().equals(tbOrder)) {
				if (StringUtils.isInteger(tbOrder.getText().trim())) {
					clearText();
					int orderID = Integer.parseInt(tbOrder.getText().trim());
					if (order != null) {
						if (orderID != order.getId()) {
							if (orderController == null)
								orderController = (TaxiOrderController) ControllerUtils
										.getController(TaxiOrderController.class);
							order = orderController.get(TaxiOrder.class, orderID);
							if (order != null) {
								viewInfo(order);
								getOtherInfo(order);
							} else {
								Clients.showNotification("Không tìm thấy", "warm", null, "middle-center", 3000, true);
							}
						}
					} else {
						if (orderController == null)
							orderController = (TaxiOrderController) ControllerUtils
									.getController(TaxiOrderController.class);
						order = orderController.get(TaxiOrder.class, orderID);
						if (order != null) {
							viewInfo(order);
							getOtherInfo(order);
						} else {
							Clients.showNotification("Không tìm thấy", "warm", null, "middle-center", 3000, true);
						}
					}
				}
			}
		} else if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget().equals(btnBeginOrderVehicle)) {
				if (order != null) {
					// getVehiclesOnBeginTrip(order);
					showBeginTrip();
				}
			} else if (event.getTarget().equals(btnRegisterOrderVehicle)) {
				if (order != null) {
					// showVehiclesOnConfirmTrip(order);
					showPickedTrip();
				}
			}
		} else if (event.getName().equals("onMapClick")) {
			if (((MapMouseEvent) event).getReference() instanceof Gmarker) {
				Gmarker gmarker = (Gmarker) ((MapMouseEvent) event).getReference();
				gmarker.setOpen(true);
			}
		}
	}

	private void getLineTrip(Driver driver, TaxiOrder order, Session session, HashMap<String, List<?>> map,
			Timestamp time) {
		Query query = session.createQuery(
				"from DriverAppTracking where driver.id=(:DriverID) AND order.id=(:OrderID) AND time>=(:OrderTime) AND latitude>0 AND longitude >0 ORDER BY time ASC");
		query.setParameter("DriverID", driver.getId());
		query.setParameter("OrderID", order.getId());
		query.setParameter("OrderTime", time);
		@SuppressWarnings("unchecked")
		List<DriverAppTracking> list = query.list();
		map.put(TRIP_TRACKING, list);
		renderTracking(list);
	}

	@SuppressWarnings("deprecation")
	private void renderTracking(List<DriverAppTracking> trackings) {
		if (trackings.size() > 3) {
			StringBuffer points = new StringBuffer();
			if (startMarker != null)
				points.append(startMarker.getLat()).append(",").append(startMarker.getLng()).append(",3").append(", ");
			else
				points.append(order.getBeginOrderLat()).append(",").append(order.getBeginOrderLon()).append(",3")
						.append(", ");
			for (int i = 0; i < trackings.size(); i++) {
				points.append(trackings.get(i).getLatitude()).append(", ").append(trackings.get(i).getLongitude())
						.append(",3").append(", ");
			}
			points.setLength(points.length() - 2);
			Gpolyline gpolyline = new Gpolyline();
			gpolyline.setPoints(points.toString());
			gpolyline.setColor("#009933");
			gpolyline.setParent(gmaps);
		}

	}

}
