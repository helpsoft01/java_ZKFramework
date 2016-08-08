package com.vietek.trackingOnline.tracker;

import org.zkoss.zul.Image;

public class TrackingVehicleInfo {
	private Image image;
	private String imageSrc;
	private boolean isLostDigital;
	
	public TrackingVehicleInfo(){
		this.image = null;
		this.imageSrc = "";
		this.isLostDigital=false;
	}
	
	public TrackingVehicleInfo(Image image, String src, boolean isLostDigital) {
		this.image = image;
		this.imageSrc = src;
		this.isLostDigital = isLostDigital;
	}


	public Image getImage() {
		return image;
	}


	public void setImage(Image image) {
		this.image = image;
	}


	public String getImageSrc() {
		return imageSrc;
	}


	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}


	public boolean isLostDigital() {
		return isLostDigital;
	}


	public void setLostDigital(boolean isLostDigital) {
		this.isLostDigital = isLostDigital;
	}
}
