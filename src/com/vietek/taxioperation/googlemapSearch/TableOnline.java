package com.vietek.taxioperation.googlemapSearch;

import org.zkoss.zk.ui.Desktop;

import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TelephoneTableTms;

public class TableOnline {
	Desktop desktop;
	TelephoneTableTms table;
	SysUser user;

	public Desktop getDesktop() {
		return desktop;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
	}

	public TelephoneTableTms getTable() {
		return table;
	}

	public void setTable(TelephoneTableTms table) {
		this.table = table;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

}
