package com.vietek.taxioperation.ui.editor;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublespinner;

public class DoubleSpinnerEditor extends Editor {

	private double value;

	public DoubleSpinnerEditor() {
		component = new Doublespinner();
		component.addEventListener(Events.ON_CHANGE, this);
		((Doublespinner) component).setHflex("1");
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public void setValue(Object value) {
		if (value != null) {
			this.value = (double) value;
			((Doublespinner) component).setValue((Double) value);
		} else {
			((Doublespinner) component).setValue(0.0);
		}
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getName().equalsIgnoreCase(Events.ON_CHANGE)) {
			value = ((Doublespinner) component).getValue();
			this.postEventChangeValue();
		}
	}

	@Override
	public void setRows(Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEmpty() {
		// TODO Auto-generated method stub
		
	}

}