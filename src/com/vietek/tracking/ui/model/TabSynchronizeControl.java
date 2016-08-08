package com.vietek.tracking.ui.model;

public class TabSynchronizeControl {

	ListboxTab tabfrm;
	GenaralTab genfrm;

	public TabSynchronizeControl(ListboxTab tab, GenaralTab gen) {
		this.tabfrm = tab;
		this.genfrm = gen;
	}
	public void refresh(){
		if (tabfrm.getMapTabList().size() > 0 && genfrm.getMapGenaral().size() > 0) {
			tabfrm.refresh();
			genfrm.refresh();
		}
	}
	

}
