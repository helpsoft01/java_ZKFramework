package com.vietek.trackingOnline.tracker;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Iterator;

import org.json.JSONObject;
import org.zkoss.gmaps.LatLng;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Timer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.controller.GPSOnlineController;
import com.vietek.taxioperation.model.GPSOnline;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;

public class TrackingOnline extends SelectorComposer<Component> implements Serializable, EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	Div map_tracking;
	@Wire
	Div div_tracking;
	private Menupopup optionMap;
	private Timer timerTracking;
	LatLng possionClicked;
	GPSOnlineController gpsOnlineController;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		init();
		div_tracking.addEventListener("onMapRightClick", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				onRightClick(arg0);
			}
		});
		div_tracking.addEventListener("onMapClick", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				Object data = arg0.getData();
				AppLogger.logDebug.info(data);
			}
		});
		Clients.evalJavaScript("moveToLocation(21.0031545, 105.8446598, 18)");
	}
	
	private void init(){
		initPopupMap();
		timerTracking = new Timer(10000);
		timerTracking.setRepeats(true);
		timerTracking.setParent(div_tracking);
		timerTracking.start();
		timerTracking.addEventListener(Events.ON_TIMER, this);
	}
	
	private void initPopupMap(){
		optionMap = new Menupopup();
		optionMap.setParent(div_tracking);
		Menuitem item = new Menuitem("Tìm xe trong vùng", "./themes/images/search_map.png");
		item.setAttribute("action", MapOption.TIM_XE_VUNG);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		item = new Menuitem("Tìm xe gần đây", "./themes/images/finder.png");
		item.setAttribute("action", MapOption.TIM_XE_DIEM);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		item = new Menuitem("Chọn vùng nhanh", "./themes/images/selection.png");
		item.setAttribute("action", MapOption.CHON_VUNG);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		Menuseparator separator = new Menuseparator();
		separator.setParent(optionMap);
		
		item = new Menuitem("Xem địa chỉ","./themes/images/map_info.png");
		item.setAttribute("action", MapOption.DIA_CHI);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		item = new Menuitem("Lấy tọa độ", "./themes/images/map_info.png");
		item.setAttribute("action", MapOption.TOA_DO);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		item = new Menuitem("Phóng to", "./themes/images/zoom_in.png");
		item.setAttribute("action", MapOption.PHONG_TO);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		item = new Menuitem("Thu nhỏ", "./themes/images/zoom_out.png");
		item.setAttribute("action", MapOption.THU_NHO);
		item.setParent(optionMap);
		
		item = new Menuitem("Đo khoảng cách", "./themes/images/ruler_kc.png");
		item.setAttribute("action", MapOption.KHOANG_CACH);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		item = new Menuitem("Chỉ hướng", "./themes/images/direction.png");
		item.setAttribute("action", MapOption.CHI_HUONG);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		separator = new Menuseparator();
		separator.setParent(optionMap);
		
		item = new Menuitem("Cấu hình khởi động", "./themes/images/process_info.png");
		item.setAttribute("action", MapOption.CAU_HINH);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		item = new Menuitem("Quản lý điểm riêng", "./themes/images/ruler.png");
		item.setAttribute("action", MapOption.QL_DIEM);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
		
		item = new Menuitem("Tạo điểm", "./themes/images/search_map.png");
		item.setAttribute("action", MapOption.TAO_DIEM);
		item.setParent(optionMap);
		item.addEventListener(Events.ON_CLICK, this);
	}
	
	private void getTrackingData() {
		try{
			Client rdsClient = null;
			String rds = ConfigUtil.getConfig("RDS_TRACKING_URL");
			if (rdsClient == null)
				rdsClient = Client.create();
			WebResource rdsResource = rdsClient.resource(rds);
			ClientResponse rdsResponse = rdsResource.get(ClientResponse.class);
			if (rdsResponse.getStatus() == 200) {
				String ret = rdsResponse.getEntity(String.class);
				
		        JSONObject jObject = new JSONObject(ret);
		        Iterator<?> keys = jObject.keys();
		        while( keys.hasNext() ){
		            String key = (String)keys.next();
		            JSONObject json = (JSONObject)jObject.get(key);
		            TrackingRDS2Json tracking = getTrackingItem(json);
		            if(tracking.getDeviceId()>0){
		            	if(gpsOnlineController==null)
							gpsOnlineController = (GPSOnlineController)ControllerUtils.getController(GPSOnlineController.class);
						GPSOnline gpsOnline = gpsOnlineController.get(GPSOnline.class, tracking.getDeviceId());
						GmarkerOnline gmarkerOnline = new GmarkerOnline(gpsOnline, tracking);
		            }
					
		        }
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		

			
//			JsonReader reader = Json.createReader(new StringReader(ret));
//			JsonArray arrayTrackingJson = reader.readArray();
//			for (int i = 0; i < arrayTrackingJson.size(); i ++) {
//				TrackingRDS2Json tracking = getTrackingItem(arrayTrackingJson.getJsonObject(i));
//				if(gpsOnlineController==null)
//					gpsOnlineController = (GPSOnlineController)ControllerUtils.getController(GPSOnlineController.class);
//				GPSOnline gpsOnline = gpsOnlineController.get(GPSOnline.class, tracking.getDeviceId());
//				GmarkerOnline gmarkerOnline = new GmarkerOnline(gpsOnline, tracking);
//				Clients.evalJavaScript("addMarker("+tracking.getLatitude()+","+tracking.getLongitude()+","+gmarkerOnline.setContent()+")");
//			}
		
	}
	
	private TrackingRDS2Json getTrackingItem(JSONObject json){
		TrackingRDS2Json rds2Json = new TrackingRDS2Json();
		try {
			rds2Json.setDeviceId(json.getInt("deviceId"));
			if(json.isNull("deviceId")){
				rds2Json.setDeviceId(0);
			}
			if(!json.isNull("timeLog")){
				long time = Long.parseLong(json.getString("timeLog"));
				rds2Json.setTimeLog(new Timestamp(time));
			}
			rds2Json.setDriverId(json.getInt("driverId"));
			rds2Json.setDriverName(json.getString("driverName"));
			if(json.isNull("driverName"))
				rds2Json.setDriverName("");
			rds2Json.setPhoneNumber(json.getString("phoneNumber"));
			if(json.isNull("phoneNumber"))
				rds2Json.setPhoneNumber("");
			rds2Json.setPhoneOffcice(json.getString("phoneOffcice"));
			if(json.isNull("phoneOffcice"))
				rds2Json.setPhoneOffcice("");
			rds2Json.setVehicleId(json.getInt("vehicleId"));
			rds2Json.setVehicleNumber(json.getString("vehicleNumber"));
			rds2Json.setVehicleGroup(0);
			rds2Json.setCarType(json.getInt("carType"));
			rds2Json.setInTrip(json.getInt("inTrip"));
			if(!json.isNull("longitude"))
				rds2Json.setLongitude(Double.parseDouble(json.getString("longitude")));
			if(!json.isNull("latitude"))
				rds2Json.setLatitude(Double.parseDouble(json.getString("latitude")));
			if(!json.isNull("angle"))
				rds2Json.setAngle(Double.parseDouble(json.getString("angle")));
			rds2Json.setGpsSpeed(json.getInt("gpsSpeed"));
			rds2Json.setMetterSpeed(json.getInt("metterSpeed"));
		} catch (Exception e) {
			AppLogger.logDebug.info("Loi class TrackingOnline.java - getTracking()");
		}
		return rds2Json;
	}
	
//	@Listen("onRightClick = map_tracking")
	public void onRightClick(Event event) {
		org.zkoss.json.JSONObject data = (org.zkoss.json.JSONObject)event.getData();
		org.zkoss.json.JSONObject coords = (org.zkoss.json.JSONObject) data.get("coords");
        Double lat = (Double)coords.get("latitude");
        Double lng = (Double)coords.get("longitude");
        possionClicked = new LatLng(lat, lng);
        if(optionMap!=null){
        	optionMap.open(div_tracking);
        }
        	
    }

	@Override
	public void onEvent(Event event) throws Exception {
		if(event.getTarget() instanceof Menuitem){
			Menuitem item = (Menuitem)event.getTarget();
			MapOption option = (MapOption)item.getAttribute("action");
			switch (option) {
			case TIM_XE_VUNG:
				Clients.evalJavaScript("drawZone()");
				break;
			case TIM_XE_DIEM:
				
				break;
			
			default:
				break;
			}
			
		} else if(event.getTarget().equals(timerTracking)){
			if(event.getName().equals(Events.ON_TIMER)){
				getTrackingData();
			}
		}
	}
	
	public enum MapOption{
		TIM_XE_VUNG, TIM_XE_DIEM, CHON_VUNG, DIA_CHI, TOA_DO,
		PHONG_TO, THU_NHO, KHOANG_CACH, CHI_HUONG, CAU_HINH, QL_DIEM, TAO_DIEM
	}
}
