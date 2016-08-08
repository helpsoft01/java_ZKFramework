package com.vietek.taxioperation.ui.editor;

import java.sql.Time;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Timebox;

public class TimeEditor extends Editor {

	private Time value;

	public TimeEditor() {
		super();
		component = new Timebox();
		component.addEventListener(Events.ON_CHANGE, this);
		((Timebox) component).setFormat("HH:mm:ss");
		((Timebox) component).setHflex("1");
	}

	public TimeEditor(String placeholder) {
		super();
		component = new Timebox();
		((Timebox) component).setFormat("HH:mm:ss");
		((Timebox) component).setConstraint("no empty: Không được để trống");
		((Timebox) component).setHflex("1");
		((Timebox) component).setPlaceholder(placeholder);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		if (value != null && value instanceof Time) {
			((Timebox) component).setValue((Time) value);
		} else {
			((Timebox) component).setValue(null);
		}
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getName().equalsIgnoreCase(Events.ON_CHANGE)) {
			value = new Time(((Timebox) component).getValue().getTime());
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