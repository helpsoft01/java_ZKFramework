package com.vietek.taxioperation.ui.editor;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Intbox;

public class IntEditor extends Editor {
	private int value;
	public IntEditor() {
		component = new Intbox();
		component.addEventListener(Events.ON_CHANGE, this);
		((Intbox)component).setHflex("1");
	}
	//DUNGNM_START_ADD_04082015
	IntEditor(String placeholder) {
			super();
			component = new Intbox();
			((Intbox)component).setHflex("1");
			((Intbox)component).setPlaceholder(placeholder);
		}
		//DUNGNM_END_ADD_04082015
	@Override
	public Object getValue() {
		return value;//((Intbox)component).getValue();
	}

	@Override
	public void setValue(Object value) {
		if (value != null) {
			this.value = (int) value;
			((Intbox)component).setValue((Integer)value);
		}
		else
			((Intbox)component).setValue(null);
	}
	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getName().equalsIgnoreCase(Events.ON_CHANGE)) {
			try {
				if(((Intbox) component).getValue()==null)
					((Intbox) component).setValue(0);
				value = ((Intbox)component).getValue();
			}
			catch (Exception error){
				value = 0;
			}
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
