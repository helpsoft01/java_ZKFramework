package com.vietek.taxioperation.ui.editor;

import java.io.Serializable;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Spinner;

public class IntSpinnerEditor extends Editor implements Serializable {

	/**
	 * @author hung
	 */
	private static final long serialVersionUID = 1L;

	private int value;

	public IntSpinnerEditor() {
		component = new Spinner();
		component.addEventListener(Events.ON_CHANGE, this);
		((Spinner) component).setHflex("1");
		((Spinner) component).setFormat(",###");
		((Spinner) component).setSclass("z-spinner-input-Editor");

	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		if (value != null) {
			this.value = (int) value;
			((Spinner) component).setValue((Integer) value);
		} else
			((Spinner) component).setValue(null);
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getName().equalsIgnoreCase(Events.ON_CHANGE)) {
			try {
				if (((Spinner) component).getValue() == null)
					((Spinner) component).setValue(0);
				value = ((Spinner) component).getValue();
			} catch (Exception error) {
				value = 0;
			}
			this.postEventChangeValue();
		}
	}

	public void setSclass(String cssClass) {
		((Spinner) component).setSclass(cssClass);
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