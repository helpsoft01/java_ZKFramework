package com.vietek.taxioperation.ui.editor;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

public abstract class Editor implements EventListener<Event> {
	public static String EDITOR = "editor";
	protected Component component;
	String dataField;
	String lable;
	Object model;

	public static String TEXTSEARCH = Character.toString((char) 187) + " Tìm kiếm mở rộng "
			+ Character.toString((char) 187);
	private Component valueChangeListener;

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public void setValueChangeListener(Component valueChangeListener) {
		this.valueChangeListener = valueChangeListener;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public String getDataField() {
		return dataField;
	}

	public void setDataField(String dataField) {
		this.dataField = dataField;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public abstract Object getValue();

	public abstract void setValue(Object value);

	public abstract void setRows(Object value);

	public abstract void setEmpty();

	public void postEventChangeValue() {
		if (valueChangeListener != null)
			Events.sendEvent("onEditorChangeValue", valueChangeListener, this);
	}

	@Override
	public void onEvent(Event arg0) throws Exception {

	}
}
