package com.vietek.trackingOnline.tracker;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import com.vietek.taxioperation.model.Vehicle;

public class ComboitemRendererVehicles<T> implements ComboitemRenderer<T> {

	@Override
	public void render(Comboitem item, T data, int index) throws Exception {
		// TODO Auto-generated method stub
//		item.setLabel(data.);
		if(data instanceof Vehicle){
			Vehicle vehicle = (Vehicle)data;
			String label = vehicle.getValue() + " - " + vehicle.getTaxiNumber();
			item.setLabel(label);
			item.setValue(data);
		}
	}

}
