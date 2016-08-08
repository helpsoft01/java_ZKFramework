package com.vietek.taxioperation.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.ui.controller.TaxiOrdersDetailForm;
import com.vietek.taxioperation.ui.controller.vmap.VMapEvents;
import com.vietek.taxioperation.ui.controller.vmap.VMaps;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;
import com.vietek.taxioperation.webservice.TaxiResult;

public class TaxiOrderGmap implements EventListener<Event> {
	public static int normalZoom = 15;
	public static int maxZoom = 19;
	private VMaps gmaps;
	private VMarker riderMarker;
	private VMarker markerTo;
//	private VMarker markerFrom;
	private TaxiOrdersDetailForm form;
	private ArrayList<VMarker> vehicleMarkers = new ArrayList<>();
	
	public TaxiOrderGmap(TaxiOrdersDetailForm form, Component parent) {
		this.form = form;
		initUI(parent);
	}
	
	public void setMarkerVehicle(List<TaxiResult> lst) {
		this.clearVehicleMarker();
		try {
			for (TaxiResult bean : lst) {
				if(bean.getLatitude() !=0 && bean.getLongitude() != 0){
					VMarker marker = new VMarker();
					marker.setParent(this.gmaps);
//					marker.setIconImage("./themes/images/marker7s_48.png");
//					marker.setContent(createContent(bean));
					marker.setIconImage(CommonDefine.TaxiIcon.ICON_7SEATS_NON_PROCESSING);
					marker.setLabel(bean.getVehicleNumber());
					marker.setAngle(bean.getAngle());
					marker.setPosition(new LatLng(bean.getLatitude(), bean.getLongitude()));
					vehicleMarkers.add(marker);
				}
			}
		} catch (Exception ex) {
			AppLogger.logDebug.error("", ex);
		}
	}
	
	public void cleanMap() {
		if (markerTo != null) {
			this.gmaps.removeChild(markerTo);
		}
	}
	
	private void initUI(Component parent) {
		gmaps = new VMaps(parent);
		gmaps.addEventListener(VMapEvents.ON_VMARKER_DRAG_END, (EventListener<Event>)this);
		gmaps.addEventListener(VMapEvents.ON_VMAP_CLICK, (EventListener<Event>)this);
		gmaps.addEventListener(VMapEvents.ON_VMARKER_CLICK, this);
	}
	
	public void markerAddrTo(LatLng latLong) {
		try {

			if (markerTo != null)
				if (latLong != null) {
					if (latLong.lat != 0 && latLong.lng != 0) {
						markerTo.setPosition(new LatLng(latLong.lat, latLong.lng));
						markerTo.setIconImage("./themes/images/pinmap_red.png"); // business_2-20-48.png
						markerTo.setParent(gmaps);
					} else {
						markerTo.setPosition(new LatLng(0.0, 0.0));
						markerTo.destroy();
					}
				} else {
					markerTo.setPosition(new LatLng(0.0, 0.0));
					markerTo.destroy();
				}
		} catch (Exception ex) {

		}
	}
	
	public void setRiderMarker(double lat, double lng) {
		if (riderMarker == null) {
			riderMarker = new VMarker();
			riderMarker.setDraggable(true);
			riderMarker.setAngle(0.0);
			riderMarker.setIconImage("./themes/images/passenger_48.png"); // map-object-blue-geo-point-32.png";
			riderMarker.setParent(gmaps);
		}
		try {
			if (lat != 0 && lng != 0) {
				gmaps.panTo(new LatLng(lat, lng));
				riderMarker.setIconImage("./themes/images/passenger_48.png");
				riderMarker.setPosition(new LatLng(lat, lng));
			} else {
//				riderMarker.detach();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void clearVehicleMarker() {
		for (VMarker marker : vehicleMarkers) {
			marker.destroy();
		}
		vehicleMarkers.clear();
	}
	
	/**
	 * @return the gmaps
	 */
	public VMaps getGmaps() {
		return gmaps;
	}

	/**
	 * @return the riderMarker
	 */
	public VMarker getRiderMarker() {
		return riderMarker;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(VMapEvents.ON_VMAP_CLICK)) {
			form.handbleGmap(event);
		} else if(event.getName().equals(VMapEvents.ON_VMARKER_DRAG_END)){
			form.handbleGmap(event);
		} else if(event.getName().equals(VMapEvents.ON_VMAP_CLICK)){
			form.handbleGmap(event);
		} 
	}
}
