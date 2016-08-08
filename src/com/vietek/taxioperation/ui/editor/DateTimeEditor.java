package com.vietek.taxioperation.ui.editor;

import java.sql.Timestamp;
import java.util.Date;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;

public class DateTimeEditor extends Editor {
	
	private Timestamp value;

	public DateTimeEditor() {
		component = new Datebox();
		component.addEventListener(Events.ON_CHANGE, this);
		((Datebox)component).setFormat("dd/MM/yyyy HH:mm:ss");
		((Datebox)component).setHflex("1");
	}
	DateTimeEditor(String placeholder) {
			super();
			component = new Datebox();
			((Datebox)component).setFormat("dd/MM/yyyy HH:mm:ss");
			((Datebox)component).setHflex("1");
			((Datebox)component).setPlaceholder(placeholder);
		}
	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		if (value != null && value instanceof Timestamp) {
			this.value = (Timestamp) value;
			((Datebox)component).setValue(new Date(((Timestamp)value).getTime()));
		}
		else {
			((Datebox)component).setValue(null);
		}
	}
	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getName().equalsIgnoreCase(Events.ON_CHANGE)) {
			if(((Datebox)component).getValue()!=null)
				value = new Timestamp(((Datebox)component).getValue().getTime());
			else
				value=null;
			this.postEventChangeValue();
		}
	}
	
	public void setStyle(String style) {
		((Datebox) component).setStyle(style);
	}
	
	public void setSclass(String cssClass) {
		((Datebox) component).setSclass(cssClass);
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
