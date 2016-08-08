/**
 * @author tuanpa
 */
package com.vietek.taxioperation.ui.editor;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Spinner;

import com.vietek.taxioperation.common.EnumCarTypeCommon;

public class CarTypeSelector extends Div {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5894517544603857538L;
	private Combobox cbSeatCar;
	private Spinner spNumberCar;
	private CarTypeValue value;
	Button btRomveDiv;
	int numberCar;

	public CarTypeValue getValue() {
		value = new CarTypeValue(cbSeatCar.getSelectedItem().getValue(), spNumberCar.getValue());
		return value;
	}
	
	public void setValue(CarTypeValue value) {
		this.value = value;
		if (value == null)
			return;
		for (Component comp : cbSeatCar.getChildren()) {
			if (comp instanceof Comboitem) {
				Comboitem item = (Comboitem)comp;
				if (((EnumCarTypeCommon)item.getValue()).getValue() == value.getCarType().getValue()) {
					cbSeatCar.setSelectedItem(item);
					break;
				}
			}
		}
		spNumberCar.setValue(value.getNumber());
	}
	
	public CarTypeSelector(CarTypeValue value) {
		super();
		this.value = value;
		create();
		initCarTypeCombobox();
		setValue(value);
	}
	
	public void create() {
		// divContain = new Div();
		this.setClass("taxiOrder2divContain");

		spNumberCar = new Spinner();
		spNumberCar.setParent(this);

		cbSeatCar = new Combobox();
		cbSeatCar.setSclass("cbSeatCar");
		cbSeatCar.setParent(this);
		cbSeatCar.setCols(5);
		// cbSeatCar.setTabindex(-1);

		spNumberCar.setWidth("60px");
		spNumberCar.setConstraint("no negative,min 0 max 20");
		spNumberCar.setValue(1);

		btRomveDiv = new Button();
		btRomveDiv.setParent(this);
		btRomveDiv.setClass("taxiOrder2btRomveDivCar");
		btRomveDiv.setTabindex(-1);

		// events
		btRomveDiv.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				Component parent = arg0.getTarget().getParent().getParent();
				if (parent != null)
					if (parent.getChildren().size() > 1) {
						arg0.getTarget().getParent().detach();
					}
				// spinner
				List<Component> listComp = parent.getChildren();
				if (listComp.size() > 0) {
					List<Component> div = listComp.get(0).getChildren();
					Spinner spinner = (Spinner) div.get(0);
					spinner.select();
					spinner.setFocus(true);
				}

			}
		});
		spNumberCar.addEventListener(Events.ON_FOCUS, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				((Spinner) arg0.getTarget()).select();
			}
		});
		cbSeatCar.addEventListener(Events.ON_FOCUS, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				((Combobox) arg0.getTarget()).select();
			}

		});
	}
	
	private void initCarTypeCombobox() {
		for (EnumCarTypeCommon carType : EnumCarTypeCommon.values()) {
			Comboitem item = cbSeatCar.appendItem(carType.getLabel());
			item.setValue(carType);
		}
		cbSeatCar.setSelectedIndex(0);
	}
	
	public Combobox getCbTypeCar() {
		return cbSeatCar;
	}

	public void setCbSeatCar(Combobox cbSeatCar) {
		this.cbSeatCar = cbSeatCar;
	}

	public Spinner getSpNumberCar() {
		return spNumberCar;
	}

	public void setSpNumberCar(Spinner spNumberCar) {
		this.spNumberCar = spNumberCar;
	}


	public int getNumberCar() {
		return numberCar;

	}

	public void setNumberCar(int numberCar) {
		this.numberCar = numberCar;
		spNumberCar.setValue(numberCar);
	}
	
	public void disable() {
		spNumberCar.setDisabled(true);
		btRomveDiv.setVisible(false);
	}
	
	public void enable() {
		spNumberCar.setDisabled(false);
		btRomveDiv.setVisible(true);
	}
}