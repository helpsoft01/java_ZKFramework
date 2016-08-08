package com.vietek.taxioperation.ui.util;

import java.lang.reflect.Method;

import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.Vehicle;

@SuppressWarnings("hiding")
public class DriverOnlineAppRenderer<DriverAppLocationModel> implements ListitemRenderer<DriverAppLocationModel> {

	public DriverOnlineAppRenderer() {
		super();
	}
	@Override
	public void render(Listitem item, DriverAppLocationModel data, int index) throws Exception {
		item.setValue(data);
		Method method = data.getClass().getMethod("getImgStatus");
		Object val = method.invoke(data);
		Listcell lstCell = new Listcell();
		lstCell.setParent(item);
		lstCell.appendChild((Image)val);
		
		method = data.getClass().getMethod("getVehicle");
		val=method.invoke(data);
		lstCell = new Listcell();
		lstCell.setParent(item);
		String vehicle=((Vehicle)val).getValue() + " - " + ((Vehicle)val).getTaxiNumber();
		Label label=new Label(vehicle);
		lstCell.appendChild(label);
		
		method = data.getClass().getMethod("getDriver");
		val=method.invoke(data);
		lstCell = new Listcell();
		lstCell.setParent(item);
		String driver = ((Driver)val).getName();
		label=new Label(driver);
		lstCell.appendChild(label);
	}

}
