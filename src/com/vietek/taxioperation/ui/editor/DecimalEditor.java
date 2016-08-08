package com.vietek.taxioperation.ui.editor;

import java.math.BigDecimal;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Decimalbox;

public class DecimalEditor extends Editor {

	private BigDecimal value;
	public DecimalEditor() {
		component = new Decimalbox();
		component.addEventListener(Events.ON_CHANGE, this);
		((Decimalbox) component).setHflex("1");
	}

	// DUNGNM_START_ADD_04082015
	public DecimalEditor(String placeholder) {
		super();
		component = new Decimalbox();
		((Decimalbox) component).setHflex("1");
		((Decimalbox) component).setPlaceholder(placeholder);
	}

	// DUNGNM_END_ADD_04082015

	@Override
	public Object getValue() {
		return value; //((Decimalbox) component).getValue();
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof BigDecimal) {
			this.value = (BigDecimal) value;
			((Decimalbox) component).setValue((BigDecimal) value);
		}
		else
			((Decimalbox) component).setValue("");
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getName().equalsIgnoreCase(Events.ON_CHANGE)) {
			value = ((Decimalbox) component).getValue();
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
