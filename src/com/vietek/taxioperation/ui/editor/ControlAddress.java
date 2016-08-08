package com.vietek.taxioperation.ui.editor;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

import com.vietek.taxioperation.util.Address;

public class ControlAddress extends Div {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7937308563039282512L;
	Address address = null;
	Button btAddress = null;
	Div dvContain = null;
	ControlNearAddressHandler nearAddressHandler;
	ControlAddress _this;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Button getBtAddress() {
		return btAddress;
	}

	public void setBtAddress(Button btAddress) {
		this.btAddress = btAddress;
	}

	public void setCSS_Default() {
		btAddress.setSclass("ControlAddress_Common");
	}

	public void setCSS_Select() {
		btAddress.setSclass("ControlAddress_Select btn-primary");
	}

	public void addToParent(Component parent) {
		dvContain.setParent(parent);
	}

	public ControlAddress(Address address, ControlNearAddressHandler handler) {
		setAddress(address);
		btAddress = new Button();
		btAddress.setLabel(address.getName());
		btAddress.addEventListener(Events.ON_CLICK, ON_CLICK);
		dvContain = new Div();
		btAddress.setParent(dvContain);
		setCSS_Default();
		dvContain.setSclass("ControlAddress_DVCommon");
		this.nearAddressHandler = handler;
		_this = this;
	}

	private EventListener<Event> ON_CLICK = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			nearAddressHandler.onChangeAddress(_this);
		}

	};

}
