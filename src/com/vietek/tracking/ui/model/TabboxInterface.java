package com.vietek.tracking.ui.model;

public interface TabboxInterface {

	abstract void closeChildrenTab(int vehicleid);

	abstract void setSelectedTabChildrenTab(int vehicleid);
	abstract void updateData(int vehicleid, GenaralValue data);
	
}
