package com.vietek.taxioperation.ui.controller.vmap;

import org.zkoss.zk.ui.Component;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.util.IDGenerator;

public class VMarker extends VComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1334323495061490893L;
	/**
	 * 
	 */
	private String content;
	private String image;
	private Double angle;
	private String label;
	private LatLng position;
	private boolean clickable;
	private boolean draggable;
//	private int opacity = 1;
	private boolean visible = true;
//	private String animation = "DROP";
	private VMaps maps;
	private static String ICON_URL = "./themes/images/gmaps_marker.png";
	
	public VMarker() {
		super(false);
		init();
	}
	
	public VMarker(double latitude, double longitude) {
		super(false);
		init();
		this.setPosition(new LatLng(latitude, longitude));
	}
	
	private void init() {
		this.visible = false;
		super.setId(IDGenerator.generateStringID());
		content="";
		image = ICON_URL;
		angle = 0.0;
		label = "";
		clickable = true;
		draggable = false;
		visible = true;
		if(position == null)
			position = new LatLng(0.0, 0.0);
		String script = "vietek.mapController.addMarker('" + getId() + "', "
				+ "{image:'" + image +"', "
				+ "position:{lat : " + position.lat + ", lng : " + position.lng + "}})";
		this.addJSScriptSynch(script);
	}
	
	public void destroy() {
		this.setParent(null);
		String script = "vietek.mapController.removeMarker('" + getId() + "')";
		this.addJSScriptSynch(script);
	}
	
	public String getContent() {
		return content;
	}

	public String getImage() {
		return image;
	}

	public Double getAngle() {
		return angle;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public double getLat(){
		return this.position.lat;
	}
	
	public double getLng(){
		return this.position.lng;
	}
	
	public VMaps getMaps() {
		return maps;
	}
	
	@Override
	public void setId(String arg0) {
		String id = super.getId();
		String script = "vietek.mapController.setIdMarker('"+ id + "','" + arg0 + "')";
		this.addJSScriptSynch(script);
		super.setId(arg0);
	}
	
	public void setLabelClass(String sclass){
		String script = "vietek.mapController.setLabelClass('"+ this.getId() + "','" + sclass + "')";
		this.addJSScriptSynch(script);
	}
	
	public void setContent(String content) {
		if(!this.content.equals(content)){
			this.content = content;
			String cont = this.content;
			if(this.content.contains("'")){
				cont = this.content.replaceAll("'", "&#39;");
			}
			if(cont.contains("\\")){
				cont = cont.replaceAll("\\", "&#92;");
			}
			if(cont.contains("\"")){
				cont = cont.replaceAll("\"", "&quot;");
			}
			
				String script = "vietek.mapController.setContent('"+ this.getId() + "','" + cont + "')";
				this.addJSScriptSynch(script);
		}
	}
	
	public void setContent(VInfoWindow infoWindow){
		
	}
	
	public boolean setOpen(boolean flag){
		if(this.maps != null){
			String script = "vietek.mapController.setOpenContent('" + this.maps.getId() + "','"+ this.getId() + "'," + flag + ")";
			this.addJSScriptSynch(script);
			return true;
		}
		return flag;
	}
	
	public void autoPan(boolean flag){
		String jsScript = "vietek.mapController.autoPanVMarker('" + getId() + "', " + !flag + ")";
		this.addJSScriptSynch(jsScript);
	}

	public void setIconImage(String image) {
		try{
			if (!this.image.equals(image)) {
				this.image = image;
				String script = "vietek.mapController.setIcon('" + getId() + "','" + image + "')";
				this.addJSScriptSynch(script);
			}
		} catch(Exception ex){
			AppLogger.logDebug.error("", ex);
		}
		
	}


	public void setAngle(Double angle) {
		if(!this.angle.equals(angle)){
			this.angle = angle;
			String script = "vietek.mapController.setRotate('" + this.getId() + "'," + angle + ")";
			this.addJSScriptSynch(script);
		}
	}
	
	
	public void setLabel(String arg0){
		if(!this.label.equals(arg0)){
			this.label = arg0;
			String script = "vietek.mapController.setLabel('" + this.getId() + "','" + this.label + "')";
			this.addJSScriptSynch(script);
		}
	}

	public void setClickable(boolean flag){
		if(this.clickable != flag){
			this.clickable = flag;
			if(this.maps != null){
				String script = "vietek.mapController.setClickable('" +this.maps.getId() + "','" + this.getId() + "'," + this.clickable + ")";
				this.addJSScriptSynch(script);
			}
		}
	}
	
	public void setDraggable(boolean flag){
		if(this.draggable != flag){
			this.draggable = flag;
				String script = "vietek.mapController.setDraggable('" + this.getId() + "'," + this.draggable + ")";
				this.addJSScriptSynch(script);
		}
	}
	
	/**
	 * Change animation for marker
	 * @param arg0 : DROP or BOUNCE
	 */
//	public void setAnimation(Animation animation){
//		if(!this.animation.equals(animation.getValue())){
//			this.animation = animation.getValue();
//			if(this.maps != null){
//				String script = "vietek.mapController.setAnimation('" + this.maps.getId() + "','" + this.getId() + "','" + this.animation + "')";
//				Events.postEvent(new Event("runJSScript", this, script));
//			}
//		}
//	}
	
//	public void setOpacity(int arg0){
//		if(this.opacity != arg0){
//			this.opacity = arg0;
//			if(this.maps != null){
//				String script = "vietek.mapController.setOpacity('" + this.maps.getId() + "','" + this.getId() + "'," + this.opacity + ")";
//				Events.postEvent(new Event("runJSScript", this, script));
//			}
//		}
//	}
	
	public boolean setVisible(boolean flag){
		if(this.visible != flag){
			this.visible = flag;
			String script = "vietek.mapController.setVisible('" + this.getId() + "'," + this.visible + ")";
			this.addJSScriptSynch(script);
		}
		return flag;
	}
	
	public void setLabelAnchor(int x, int y){
		String script = "vietek.mapController.setLabelAnchor('" + this.getId() + "'," + x + "," + y + ")";
		this.addJSScriptSynch(script);
	}
	
	public boolean getVisible(){
		return this.visible;
	}
	
	@Override
	public void setParent(Component maps) {
		if (maps != null && maps instanceof VMaps) {
			this.maps = (VMaps)maps;
			this.maps.markers.put(getId(), this);
			String script = "vietek.mapController.setMap('" + maps.getId() + "','" + getId() + "')";
			this.addJSScriptSynch(script);
		} 
//		if(getParent()!=null){
//			if(!getParent().equals(maps))
				super.setParent(maps);
//		}
		
	}
	
	/**
	 * @return the position
	 */
	public LatLng getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(LatLng position) {
		this.position = position;
		String script = "vietek.mapController.setPosition('" + getId() + "', " + position.lat + ", " + position.lng + ")";
		this.addJSScriptSynch(script);
	}
	
	protected void setLatLng(LatLng latLng){
		this.position = latLng;
	}
}
