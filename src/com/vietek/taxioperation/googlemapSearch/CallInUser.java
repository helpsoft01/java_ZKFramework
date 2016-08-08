package com.vietek.taxioperation.googlemapSearch;

import java.io.Serializable;
import java.sql.Timestamp;

import org.zkoss.zk.ui.Desktop;

import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.VoipCenter;

public class CallInUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2796605617106460723L;

	Desktop desktop;
	SysUser user;
	private Customer customer;
	private Timestamp timeCallIn;
	private int index = 0;
	private String extension;
	private VoipCenter voip;
	private String calluuid;

	public CallInUser() {
		this(null);
	}

	public VoipCenter getVoip() {
		return voip;
	}

	public void setVoip(VoipCenter voip) {
		this.voip = voip;
	}

	public CallInUser(Desktop desktop) {
		this.desktop = desktop;
	}

	public SysUser getUser() {
		return user;
	}

	public Desktop getDesktop() {
		return desktop;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Timestamp getTimeCallIn() {
		return timeCallIn;
	}

	public void setTimeCallIn(Timestamp timeCallIn) {
		this.timeCallIn = timeCallIn;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getCalluuid() {
		return calluuid;
	}

	public void setCalluuid(String calluuid) {
		this.calluuid = calluuid;
	}

}
