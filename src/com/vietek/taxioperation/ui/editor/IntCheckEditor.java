package com.vietek.taxioperation.ui.editor;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;

public class IntCheckEditor extends YesNoEditor {

	private Object value;

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
		Checkbox checkbox = (Checkbox) component;
		if (value == null) {
			checkbox.setChecked(false);
			return;
		}
		if ((Integer) this.value == 1) {
			checkbox.setChecked(true);
		} else if ((Integer) this.value == 0) {
			checkbox.setChecked(false);
		}
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getName().equals(Events.ON_CHECK)) {
			Checkbox checkbox = (Checkbox) component;
			if (checkbox.isChecked()) {
				value = 1;
			} else {
				value = 0;
			}
			postEventChangeValue();
		}
	}

	public void setSclass(String cssclass) {
		((Checkbox) component).setSclass(cssclass);
	}
}
