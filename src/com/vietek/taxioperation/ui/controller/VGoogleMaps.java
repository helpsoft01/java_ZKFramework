package com.vietek.taxioperation.ui.controller;

import java.security.SecureRandom;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Script;

import com.vietek.taxioperation.util.GoogleKey;

public class VGoogleMaps extends Div {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id="";
	private String APIKey = "";
	private String library="";
	Script scriptCode;
	Script scriptSource;
	
	public VGoogleMaps() {
		setHflex("true");
		setVflex("true");
		String MAP_URL = "http://maps.googleapis.com/maps/api/js";
		StringBuffer src = new StringBuffer();
		src.append(MAP_URL);
		if(APIKey.length()==0)
			APIKey = GoogleKey.getKey();
		src.append("?key=").append("AIzaSyAocRnSkbbpbc2yed6l-JnO28vmLo5Z0n0");
		library = "places";
		if(library.length()>0){
			src.append("&amp;libraries=").append(library);
		}
		scriptCode = new Script();
		scriptCode.setSrc("./VGoogleMaps/VGoogleMaps.js");
		
		scriptSource = new Script();
		scriptSource.setSrc(src.toString());
		scriptCode.setParent(this);
		scriptCode.setVisible(false);
		scriptSource.setParent(this);
		scriptSource.setVisible(false);
		
	}
	
	@Override
	public void setId(String arg0) {
		id=arg0;
		super.setId(arg0);
	}
	
	@Override
	public void setParent(Component arg0) {
//		scriptCode.setParent(arg0);
//		scriptSource.setParent(arg0);
		
		if(id.length()==0)
			id = generateID();
		setId(id);
		super.setParent(arg0);
		Clients.evalJavaScript("setId('"+id+"')");
		Clients.evalJavaScript("initialize()");
	}
	
	protected String generateID(){
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
		StringBuilder sb = new StringBuilder(7);
		for( int i = 0; i < 7; i++ ) 
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		return sb.toString();
		
	}
	
	/**
	 * Hien thi tim kiem dia chi, vi tri ...
	 *  
	 * @param isAutoCompleteSearch
	 * @param possion 
	 * values: TOP_CENTER, TOP_LEFT, TOP_RIGHT, LEFT_TOP, RIGHT_TOP, LEFT_CENTER, RIGHT_CENTER, LEFT_BOTTOM, RIGHT_BOTTOM, BOTTOM_CENTER, BOTTOM_LEFT, BOTTOM_RIGHT
	 */
	public void setAutoCompleteSearch(boolean isAutoCompleteSearch, String possion){
		if(isAutoCompleteSearch){
			Clients.evalJavaScript("initAutocompleteSearch('"+id+"')");
		}
	}
	

	

}
