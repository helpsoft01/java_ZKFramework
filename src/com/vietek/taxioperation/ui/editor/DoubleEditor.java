package com.vietek.taxioperation.ui.editor;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;

/**
 * 
 * @author VuD
 * 
 */

public class DoubleEditor extends Editor {
	private double value;

	public DoubleEditor() {
		component = new Doublebox();
		component.addEventListener(Events.ON_CHANGE, this);
		((Doublebox) component).setHflex("1");

	}

	@Override
	public Object getValue() {
		return value;// ((Doublebox) component).getValue();
	}

	@Override
	public void setValue(Object value) {
		if (value != null) {
			this.value = (double) value;
			((Doublebox) component).setValue((Double) value);
		} else {
			((Doublebox) component).setValue(0);
		}
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getName().equalsIgnoreCase(Events.ON_CHANGE)) {
			if (((Doublebox) component).getValue() == null) {
				((Doublebox) component).setValue(0.0);
			}
			value = ((Doublebox) component).getValue();
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