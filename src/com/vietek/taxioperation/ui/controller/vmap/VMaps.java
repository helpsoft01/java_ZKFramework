package com.vietek.taxioperation.ui.controller.vmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import com.google.maps.model.LatLng;


public class VMaps extends VComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LatLngBounds bounds;
	private LatLng center = new LatLng(16.3776538, 105.0040875);
	private String mapType = "ROADMAP";
	private int tilt = 0;
	private int zoom = 15;
	private LatLng mousePosition;
	protected Map<String, VMarker> markers;
	private VMaps self;
	private boolean isMapLoaded = false;
	
	public VMaps(Component parent) {
		super(true);
		setParent(parent);
		this.setHeight("100%");
		this.setWidth("100%");
		markers = new HashMap<>();
//		children = new ArrayList<>();
		this.self = this;
		listenMethod();
		Clients.showBusy(this, "loading");
	}
	
	

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.addJSScriptSynch("vietek.mapController.deleteMap('" + getId() + "')");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.deleteMap('" + getId() + "')"));
	}
	
	@Override
	public void setId(String arg0) {
		// TODO Auto-generated method stub
		super.setId(arg0);
	}

	@Override
	public void setParent(Component arg0) {
		super.setParent(arg0);
		this.addJSScriptSynch("vietek.mapController.createMap('" + getId() + "')");
	}
	
	public Map<String, VMarker> getAllMarker(){
		return this.markers;
	}
	
//	private void checkMarkerID(VComponent child){
//		if(child instanceof VMarker){
//			if(markers.get(child.getId())!=null){
//				((VMarker)child).reGerateID();
//				checkMarkerID(child);
//			}
//		}
//	}
//	
	
	@Override
	public boolean appendChild(Component child) {
		if(child instanceof VMarker){
			markers.put(child.getId(), (VMarker)child);
		}
		child.setParent(this);
		return super.appendChild(child);
	}
	
	public String getMapTypeId(){
		return mapType;
	}
	
	public void startDrawPolygon(){
		this.addJSScriptSynch("vietek.mapController.drawPolygon('" + getId() + "')");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.drawPolygon('" + getId() + "')"));
	}
	
	/**
	 * Set map type for map
	 * @param mapType : ROADMAP, SATELLITE, HYBRID, TERRAIN
	 */
	public void setMapType(MapType mapType){
		this.mapType = mapType.getValue();
		this.addJSScriptSynch("vietek.mapController.setMapTypeId('" + getId() + "', '" + this.mapType + "')");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.setMapTypeId('" + getId() + "', '" + this.mapType + "')"));
	}
	
	/**
	 * Returns the current angle of incidence of the map, in degrees from the viewport plane to the map plane.
	 * The result will be 0 for imagery taken directly overhead or 45 for 45° imagery. 
	 * 45° imagery is only available for SATELLITE and HYBRID map types, within some locations, and at some zoom levels. 
	 * @return
	 */
	public int getTilt(){
		return this.tilt;
	}
	
	/**
	 * 
	 * @param tilt: number| 0 - 45
	 */
	public void setTilt(int tilt) {
		this.tilt = tilt;
		this.addJSScriptSynch("vietek.mapController.setTilt('" + getId() + "'," + tilt + ")");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.setTilt('" + getId() + "'," + tilt + ")"));
	}
	
	public int getZoom(){
		return this.zoom;
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
		this.addJSScriptSynch("vietek.mapController.setZoom('" + getId() + "'," + zoom + ")");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.setZoom('" + getId() + "'," + zoom + ")"));
	}

	/**
	 * Returns the position displayed at the center of the map
	 * @return
	 */
	public LatLng getCenter() {
		return center;
	}
	
	public LatLng getMousePosition(){
		return mousePosition;
	}

	/**
	 * Set position display at the center of the map
	 * @param center
	 */
	public void setCenter(LatLng center) {
		this.center = center;
		this.addJSScriptSynch("vietek.mapController.setCenter('" + getId() + "'," + center.lat + "," + center.lng + ")");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.setCenter('" + getId() + "'," + center.lat + "," + center.lng + ")"));
	}
	
	public void setCenter(double latitude, double longitude) {
		this.center = new LatLng(latitude, longitude);
		this.addJSScriptSynch("vietek.mapController.setCenter('" + getId() + "'," + latitude + "," + longitude + ")");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.setCenter('" + getId() + "'," + latitude + "," + longitude + ")"));
	}

	/**
	 * Returns the lat/lng bounds of the current viewport
	 */
	public LatLngBounds getBounds() {
		return bounds;
	}
	
	public void setBounds(LatLngBounds bounds){
		this.bounds = bounds;
	}
	
	/**
	 * Changes the center of the map by the given distance in pixels.
	 * If the distance is less than both the width and height of the map, the transition will be smoothly animated.
	 * Note that the map coordinate system increases from west to east (for x values) and north to south (for y values).
	 * @param x
	 * @param y
	 */
	public void panBy(int x, int y){
		this.addJSScriptSynch("vietek.mapController.panBy('" + getId() + "'," + x + "," + y + ")");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.panBy('" + getId() + "'," + x + "," + y + ")"));
	}
	
	/**
	 * Changes the center of the map to the given LatLng.
	 * If the change is less than both the width and height of the map, the transition will be smoothly animated.
	 * @param possion
	 */
	public void panTo(LatLng possion){
		double lat = possion.lat;
		double lng = possion.lng;
		this.addJSScriptSynch("vietek.mapController.panTo('" + getId() + "'," + lat + "," + lng + ")");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.panTo('" + getId() + "'," + lat + "," + lng + ")"));
	}
	
	public void panTo(double latitude, double longitude){
		this.center = new LatLng(latitude, longitude);
		this.addJSScriptSynch("vietek.mapController.panTo('" + getId() + "'," + latitude + "," + longitude + ")");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.panTo('" + getId() + "'," + latitude + "," + longitude + ")"));
	}
	
	public void closeAllInfo(){
		String jsScript = "vietek.mapController.closeAllInfo('" + getId() + "')";
		this.addJSScriptSynch(jsScript);
	}

	/**
	 * Pans the map by the minimum amount necessary to contain the given LatLngBounds
	 * @param bounds
	 */
	public void panToBounds(LatLngBounds bounds){
		if(bounds.southWest == null || bounds.southWest == null){
			String msg = "You must set value for both NorthEast and SouthWest of LatLngBounds";
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
		} else {
			JSONObject jsonObject = new JSONObject();
			JSONObject jsonValue = new JSONObject();
			jsonValue.put("NorthEastLat", bounds.northEast.lat);
			jsonValue.put("NorthEastLng", bounds.northEast.lng);
			jsonValue.put("SouthWestLat", bounds.southWest.lat);
			jsonValue.put("SouthWestLng", bounds.southWest.lng);
			jsonObject.put("bounds", jsonValue);
			this.addJSScriptSynch("vietek.mapController.panToBounds('" + getId() + "','" + jsonObject + "')");
//			Events.postEvent(new Event("runJSScript", this, "vietek.mapController.panToBounds('" + getId() + "','" + jsonObject + "')"));
		}
	}
	
	/**
	 * Sets the viewport to contain the given bounds
	 * @param latLngs
	 */
	public void fitBounds(List<LatLng> latLngs) {
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonValue;
		for (int i = 0; i < latLngs.size(); i++) {
			jsonValue = new JSONObject();
			jsonValue.put("lat", latLngs.get(i).lat);
			jsonValue.put("lng", latLngs.get(i).lng);
			jsonObject.put(i, jsonValue);
		}
		this.addJSScriptSynch("vietek.mapController.fitBounds('" + getId() + "','" + jsonObject + "'," + latLngs.size() + ")");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.fitBounds('" + getId() + "','" + jsonObject + "'," + latLngs.size() + ")"));
	}
	
	public boolean hideAllMarkers(boolean flag){
		for (VMarker marker : markers.values()) {
			marker.setVisible(!flag);
		}
//		this.addJSScriptSynch("vietek.mapController.hideAllMarker('" + getId() + "',flag)");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.hideAllMarker('" + getId() + "')"));
		return flag;
	}

	private void listenMethod() {
		this.addEventListener("onDidLoadMap", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if(event.getTarget().getId().equals(getId())){
					Clients.clearBusy(self);
					isMapLoaded = true;
				}
			}
		});
		
		this.addEventListener("onClickVMap", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				VMapEvent mapEvent = new VMapEvent(VMapEvents.ON_VMAP_CLICK);
				mapEvent.maps = self;
				mapEvent.component = self;
				if(mousePosition != null)
					mapEvent.setPosition(mousePosition.lat, mousePosition.lng);
				Events.postEvent(self, mapEvent);
				arg0.stopPropagation();
			}
		});
		this.addEventListener("onMouseMoveVMap", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				JSONObject jsonObject =(JSONObject)event.getData();
				JSONObject object = (JSONObject)jsonObject.get("data");
				Double lat = Double.valueOf(object.get("latitude")+"");
				Double lng = Double.valueOf(object.get("longtitude")+"");
				mousePosition = new LatLng(lat, lng);
				VMapEvent event2 = new VMapEvent(VMapEvents.ON_VMAP_MOUSE_MOVE);
				event2.component = self;
				event2.maps = self;
				event2.setPosition(lat, lng);
				Events.postEvent(self, event2);
				event.stopPropagation();
			}
		});
		this.addEventListener("onRightClickVMap", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if(event.getTarget().getId().equals(getId())){
					JSONObject jsonObject =(JSONObject)event.getData();
					JSONObject object = (JSONObject)jsonObject.get("data");
					Double lat = (Double)object.get("latitude");
					Double lng = (Double)object.get("longtitude");
					mousePosition = new LatLng(lat, lng);
					VMapEvent mapEvent = new VMapEvent(VMapEvents.ON_VMAP_RIGHT_CLICK);
					mapEvent.component = self;
					mapEvent.maps = self;
					mapEvent.setPosition(lat, lng);
					Events.postEvent(self, mapEvent);
					event.stopPropagation();
				}
			}
		});
		this.addEventListener("onZoomChangedVmap", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				zoom = (int)event.getData();
				VMapEvent mapEvent = new VMapEvent(VMapEvents.ON_VMAP_ZOOM_CHANGED);
				mapEvent.component = self;
				mapEvent.maps = self;
				if(mousePosition != null)
					mapEvent.setPosition(mousePosition.lat, mousePosition.lng);
				Events.postEvent(self, mapEvent);
				event.stopPropagation();
			}
		});
		this.addEventListener("onDragVmap", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				VMapEvent mapEvents = new VMapEvent(VMapEvents.ON_VMAP_DRAG);
				mapEvents.component = self;
				if(mousePosition != null)
					mapEvents.setPosition(mousePosition.lat, mousePosition.lng);
				Events.postEvent(self, mapEvents);
				arg0.stopPropagation();
			}
		});
		this.addEventListener("onDragStartVmap", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				VMapEvent mapEvents = new VMapEvent(VMapEvents.ON_VMAP_DRAG_START);
				mapEvents.component = self;
				mapEvents.maps = self;
				if(mousePosition != null)
					mapEvents.setPosition(mousePosition.lat, mousePosition.lng);
				Events.postEvent(self, mapEvents);
				arg0.stopPropagation();
			}
		});
		this.addEventListener("onDragEndVmap", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				VMapEvent mapEvents = new VMapEvent(VMapEvents.ON_VMAP_DRAG_END);
				mapEvents.component = self;
				mapEvents.maps = self;
				if(center != null)
					mapEvents.setPosition(center);
				Events.postEvent(self, mapEvents);
				arg0.stopPropagation();
			}
		});
		this.addEventListener("onBoundsChangedVMap", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				JSONObject jsonObject = (JSONObject) event.getData();
				JSONObject object = (JSONObject) jsonObject.get("bounds");
				Double northEastLat = Double.parseDouble(object.get("NorthEastLat")+"");
				Double northEastLng = Double.parseDouble(object.get("NorthEastLng")+"");
				Double southWestLat = Double.parseDouble(object.get("SouthWestLat")+"");
				Double southWestLng = Double.parseDouble(object.get("SouthWestLng")+"");
				LatLng southWest = new LatLng(southWestLat, southWestLng);
				LatLng northEast = new LatLng(northEastLat, northEastLng);
				bounds = new LatLngBounds(southWest, northEast);
				self.bounds = new LatLngBounds(southWest, northEast);
				VMapEvent mapEvent = new VMapEvent(VMapEvents.ON_VMAP_BOUNDS_CHANGED);
				mapEvent.maps = self;
				if(mousePosition != null){
					mapEvent.setPosition(mousePosition.lat, mousePosition.lng);
				}
				mapEvent.component = self;
				Events.postEvent(self, mapEvent);
				event.stopPropagation();
			}
		});
		this.addEventListener("onCenterChangeVMap", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				JSONObject jsonObject = (JSONObject) event.getData();
				JSONObject object = (JSONObject) jsonObject.get("center");
				Double lat = Double.parseDouble(object.get("latitude")+"");
				Double lng = Double.parseDouble(object.get("longtitude")+"");
				center = new LatLng(lat, lng);
				VMapEvent mapEvent = new VMapEvent(VMapEvents.ON_VMAP_CENTER_CHANGE);
				mapEvent.component = self;
				mapEvent.maps = self;
				mapEvent.setPosition(lat, lng);
				Events.postEvent(self, mapEvent);
				event.stopPropagation();
			}
		});
		this.addEventListener("onTypeChangedVMap", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				self.mapType = ((String)event.getData()).toUpperCase();
				VMapEvent mapEvent = new VMapEvent(VMapEvents.ON_VMAP_TYPE_CHANGED);
				mapEvent.component = self;
				mapEvent.maps = self;
				mapEvent.setPosition(mousePosition);
				Events.postEvent(self, mapEvent);
				event.stopPropagation();
			}
		});
		this.addEventListener("onVMarkerClick", new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				String markerId = (String)arg0.getData();
				VMarker marker = markers.get(markerId);
				VMapEvent mapEvents = new VMapEvent(VMapEvents.ON_VMARKER_CLICK);
				mapEvents.component = marker;
				mapEvents.maps = self;
				mapEvents.setPosition(marker.getPosition());
				Events.postEvent(self, mapEvents);
				arg0.stopPropagation();
			}
		});
		this.addEventListener("onVMarkerDrag", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				JSONObject jsonObject = (JSONObject) event.getData();
				JSONObject object = (JSONObject) jsonObject.get("data");
				String markerId = String.valueOf(object.get("markerId")+"");
				Double lat = Double.parseDouble(object.get("latitude")+"");
				Double lng = Double.parseDouble(object.get("longtitude")+"");
				markers.get(markerId).setLatLng(new LatLng(lat, lng));
				VMapEvent mapEvent = new VMapEvent(VMapEvents.ON_VMARKER_DRAG);
				mapEvent.setPosition(lat, lng);
				mapEvent.maps = self;
				mapEvent.component = markers.get(markerId);
				Events.postEvent(self, mapEvent);
			}
		});
		this.addEventListener("onVMarkerDragStart", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				JSONObject jsonObject = (JSONObject) event.getData();
				JSONObject object = (JSONObject) jsonObject.get("data");
				String markerId = String.valueOf(object.get("markerId")+"");
				Double lat = Double.parseDouble(object.get("latitude")+"");
				Double lng = Double.parseDouble(object.get("longtitude")+"");
				markers.get(markerId).setLatLng(new LatLng(lat, lng));
				VMapEvent mapEvent = new VMapEvent(VMapEvents.ON_VMARKER_DRAG_START);
				mapEvent.setPosition(lat, lng);
				mapEvent.maps = self;
				mapEvent.component = markers.get(markerId);
				Events.postEvent(self, mapEvent);
				event.stopPropagation();
			}
		});
		this.addEventListener("onVMarkerDragEnd", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				JSONObject jsonObject = (JSONObject)event.getData();
				JSONObject object = (JSONObject)jsonObject.get("data");
				String markerId = (String)object.get("markerId");
				Double lat = Double.valueOf(object.get("latitude")+"");
				Double lng = Double.valueOf(object.get("longtitude")+"");
				markers.get(markerId).setLatLng(new LatLng(lat, lng));
				VMapEvent mapEvent = new VMapEvent(VMapEvents.ON_VMARKER_DRAG_END);
				mapEvent.component = markers.get(markerId);
				mapEvent.maps = self;
				mapEvent.setPosition(lat, lng);
				Events.postEvent(self, mapEvent);
				event.stopPropagation();
			}
		});
		/**
		 * 
		 */
		addEventListener("onErrorMap", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				String msg = (String)event.getData();
				 Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
				 event.stopPropagation();
			}
		});
	}
	
	public void removeAllMarker(){
		this.addJSScriptSynch("vietek.mapController.removeAllMarker('" + getId() + "')");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.removeAllMarker('" + getId() + "')"));
	}
	
	@Override
	public boolean removeChild(Component child) {
		this.addJSScriptSynch("vietek.mapController.removeChild('" + getId() + "','" + child.getId() + "')");
		return super.removeChild(child);
	}
	public void removeAllChild(){
		this.addJSScriptSynch("vietek.mapController.removeAllChild('" + getId() + "')");
//		Events.postEvent(new Event("runJSScript", this, "vietek.mapController.removeAllChild('" + getId() + "')"));
	}

	public boolean isMapLoaded() {
		return isMapLoaded;
	}
}
