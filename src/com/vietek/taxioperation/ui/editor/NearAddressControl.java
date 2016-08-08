package com.vietek.taxioperation.ui.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.Div;

import com.vietek.taxioperation.util.Address;

public class NearAddressControl extends Div implements ControlNearAddressHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6739266131867544029L;
	Address value = null;
	List<Address> lstAddress = new ArrayList<>();
	List<ControlAddress> lstControlAddress = new ArrayList<>();
	NearAddressControlHandler controlAddressHandler;

	public Address getValue() {
		return value;
	}

	public Address getExsistValue(Address add) {
		boolean isEqual = false;
		for (ControlAddress item : lstControlAddress) {
			Address addItem = item.getAddress();
			if (addItem.getName().equals(add.getName()) && addItem.getLatitude() == add.getLatitude()
					&& addItem.getLongitude() == add.getLongitude()) {

				isEqual = true;
			}
		}
		if (!isEqual) {
			return add;
		} else
			return value;
	}

	public List<ControlAddress> getLstControlAddress() {
		return lstControlAddress;
	}

	public void setLstControlAddress(List<ControlAddress> lstControlAddress) {
		this.lstControlAddress = lstControlAddress;
	}

	public void setValue(Address value) {
		this.value = value;
	}

	public List<Address> getLstAddress() {
		return lstAddress;
	}

	public void setLstAddress(List<Address> lstAddress) {
		this.lstAddress = lstAddress;
	}

	public NearAddressControl(List<Address> lstAddress, NearAddressControlHandler controlAddressHandler) {
		setLstAddress(lstAddress);
		this.controlAddressHandler = controlAddressHandler;
		init();
	}

	public void init() {

		// sortLstAdd(lstAddress);
		// lstAddress = new ArrayList<>();
		lstControlAddress = new ArrayList<>();
		for (Address add : lstAddress) {

			ControlAddress cAdd = new ControlAddress(add, this);
			cAdd.addToParent(this);
			lstControlAddress.add(cAdd);
		}

	}

	public List<Address> sortLstAdd(List<Address> lst) {
		Collections.sort(lst, new Comparator<Address>() {

			@Override
			public int compare(Address arg0, Address arg1) {
				if (arg0.getName().length() > arg1.getName().length())
					return -1;
				else
					return 1;
			}

		});
		return lst;
	}

	public void selectAddress(Address address) {

		for (int i = 0; i < lstControlAddress.size(); ++i) {
			Address add = lstControlAddress.get(i).getAddress();
			if (add.getName().equals(address.getName()) && add.getLatitude() == address.getLatitude()
					&& add.getLongitude() == address.getLongitude()) {
				lstControlAddress.get(i).setCSS_Select();
				setValue(add);
			} else
				lstControlAddress.get(i).setCSS_Default();
		}
	}

	@Override
	public void onChangeAddress(ControlAddress address) {

		setValue(address.getAddress());
		selectAddress(address.getAddress());
		controlAddressHandler.onChangeAddress(address.getAddress());
	}
}
