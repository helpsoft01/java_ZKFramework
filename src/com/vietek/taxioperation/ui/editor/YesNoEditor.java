package com.vietek.taxioperation.ui.editor;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;

public class YesNoEditor extends Editor {
	private Object value;
	public YesNoEditor() {
		super();
		component = new Checkbox();
		component.addEventListener(Events.ON_CHECK, this);
		((Checkbox) component).setHflex("1");

	}
	// DUNGNM_START_ADD_04082015
	public YesNoEditor(String placeholder) {
			super();
			component = new Checkbox();
			((Checkbox) component).setHflex("1");
			((Checkbox) component).setLabel(placeholder);
		}


	@Override
	public Object getValue() {
		return value;
//		Checkbox checkbox = (Checkbox) component;
//		if (checkbox.isChecked()) {
//			return true;
//		} else {
//			return false;
//		}
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
		Checkbox checkbox = (Checkbox) component;
		if (value == null) {
			checkbox.setChecked(false);
			return;
		}
		boolean b = (boolean) value;
		if (b) {
			checkbox.setChecked(true);
		}else {
			checkbox.setChecked(false);
		}
	}
	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getName().equals(Events.ON_CHECK)) {
			Checkbox checkbox = (Checkbox) component;
			if (checkbox.isChecked()) {
				value = true;
			} else {
				value = false;
			}
			postEventChangeValue();
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
