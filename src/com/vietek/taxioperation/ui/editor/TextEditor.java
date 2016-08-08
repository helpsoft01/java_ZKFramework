package com.vietek.taxioperation.ui.editor;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Textbox;

public class TextEditor extends Editor {
	private String value;
	TextEditor() {
		super();
		component = new Textbox();
		component.addEventListener(Events.ON_CHANGING, this);
		((Textbox)component).setHflex("1");
	}
	//DUNGNM_START_ADD_04082015
	TextEditor(String placeholder) {
		super();
		component = new Textbox();
		((Textbox)component).setHflex("1");
		((Textbox)component).setPlaceholder(placeholder);
	}
	//DUNGNM_END_ADD_04082015

	@Override
	public Object getValue() {
		return value;
	}
	
	@Override
	public void setValue(Object value) {
		if (value instanceof String) {
			this.value = (String) value;
			((Textbox)component).setValue((String)value);
		}
		else {
			this.value = null;
			((Textbox)component).setValue(this.value);
			//ignore
		}
	}
	@Override
	public void onEvent(Event e) throws Exception {
		if (e instanceof InputEvent) {
			InputEvent event = (InputEvent) e;
			value = event.getValue();
			this.postEventChangeValue();
		}
	}
	@Override
	public void setRows(Object value) {
		// TODO Auto-generated method stub
		((Textbox)component).setRows((Integer)value);
	}
	@Override
	public void setEmpty() {
		// TODO Auto-generated method stub
		
	}
}
