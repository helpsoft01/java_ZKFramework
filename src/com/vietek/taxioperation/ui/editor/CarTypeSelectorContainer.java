/**
 * @author tuanpa
 */
package com.vietek.taxioperation.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

import com.vietek.taxioperation.common.EnumCarTypeCommon;

public class CarTypeSelectorContainer extends Div {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3836776304083870704L;
	public static int MAX_TYPE = 3;
	private Div divContainTyleVehicle;
	private Button btAddTypeVehicle;

	/**
	 * Khởi tạo, nếu danh sách loại xe trống => thêm loại xe mặc định
	 * @param carTypes
	 */
	public CarTypeSelectorContainer() {
		initUI();
	}
	
	public Div getContainTyleVehicle() {
		return divContainTyleVehicle;
	}
	
	private void initUI() {
		divContainTyleVehicle = new Div();
		divContainTyleVehicle.setParent(this);
		divContainTyleVehicle.setId("divContainTyleVehicle");
		divContainTyleVehicle.setSclass("divContainTyleVehicle");

		btAddTypeVehicle = new Button("(F7)");
		btAddTypeVehicle.setParent(this);
		btAddTypeVehicle.setClass("taxiOrder2BtAddSearCar");
		btAddTypeVehicle.setAutodisable("self");
		btAddTypeVehicle.setTabindex(-1);
		btAddTypeVehicle.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				addCarTypeSelector();
			}
		});
		setCarType(0);
	}
	
	public void setCarType(int code) {
		EnumCarTypeCommon carType = EnumCarTypeCommon.getValueOfCode(code);
		divContainTyleVehicle.getChildren().clear();
		CarTypeSelector carTypeSelector = new CarTypeSelector(new CarTypeValue(carType, 1));
		carTypeSelector.setParent(divContainTyleVehicle);
	}
	
	public void addCarTypeSelector() {
		if (divContainTyleVehicle.getChildren().size() >= MAX_TYPE) {

			Clients.showNotification("ĐƯỢC PHÉP NHẬP " + MAX_TYPE + " LOẠI XE!", Clients.NOTIFICATION_TYPE_INFO, btAddTypeVehicle,
					"BottomRight", 1500);
		} else {

			CarTypeSelector cTyleVehicle = new CarTypeSelector(null);
			divContainTyleVehicle.appendChild(cTyleVehicle);
			cTyleVehicle.getSpNumberCar().select();
			cTyleVehicle.getSpNumberCar().focus();
		}
	}
	
	/**
	 * @return danh sách loại xe
	 */
	public List<CarTypeValue> getCarTypes() {

		List<CarTypeValue> retVals = new ArrayList<CarTypeValue>();

		List<Component> list = divContainTyleVehicle.getChildren();
		for (Component child : list) {
			if (child instanceof CarTypeSelector) {
				CarTypeSelector carTypeSelector = (CarTypeSelector)child;
				retVals.add(carTypeSelector.getValue());
			}
		}
		return retVals;
	}
	
	public void disable() {
		List<Component> list = divContainTyleVehicle.getChildren();
		for (Component child : list) {
			if (child instanceof CarTypeSelector) {
				CarTypeSelector carTypeSelector = (CarTypeSelector)child;
				carTypeSelector.disable();
			}
		}
		btAddTypeVehicle.setVisible(false);
	}
	
	public void enable() {
		List<Component> list = divContainTyleVehicle.getChildren();
		for (Component child : list) {
			if (child instanceof CarTypeSelector) {
				CarTypeSelector carTypeSelector = (CarTypeSelector)child;
				carTypeSelector.enable();
			}
		}
		btAddTypeVehicle.setVisible(true);
	}
}
