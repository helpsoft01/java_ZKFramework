package com.vietek.taxioperation.ui.editor;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Textbox;

public class TextboxSearch implements EventListener<Event> {

	String value = "";
	Textbox component = null;
	TextboxSearchHandler handler;
	private boolean focus = false;

	public TextboxSearch(String placeHolder, Component parent) {

		component = new Textbox();
		component.addEventListener(Events.ON_CHANGING, this);
		component.addEventListener(Events.ON_OK, this);
		component.setPlaceholder(placeHolder);
		component.setParent(parent);
		component.setWidth("100%");
		component.setSclass("txtSearchcommon form-control");
		component.setInstant(false);
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
		component.setFocus(focus);

	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TextboxSearchHandler getHandler() {
		return handler;
	}

	public void setHandler(TextboxSearchHandler handler) {
		this.handler = handler;
	}

	public Textbox getComponent() {
		return component;
	}

	public void setComponent(Textbox component) {
		this.component = component;
	}

	@Override
	public void onEvent(Event event) throws Exception {

		if (event instanceof InputEvent) {

			InputEvent inputEvent = (InputEvent) event;

			if (inputEvent.getName().equalsIgnoreCase(Events.ON_CHANGING)) {
				// setValue(textInput);
				// changing();
			}
		} else {
			if (event.getName().equalsIgnoreCase(Events.ON_OK)) {
				setValue(component.getValue());
				changing();
			}
		}
	}

	public void changing() {

		handler.onChanging(getValue());
	}

	@Override
	public String toString() {
		return value.trim();
	}
}
