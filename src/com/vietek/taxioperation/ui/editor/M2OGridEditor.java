package com.vietek.taxioperation.ui.editor;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

import com.vietek.taxioperation.model.AbstractModel;

public class M2OGridEditor extends Editor {
	private Div component;
	private List<AbstractModel> data;
	private AbstractModel value;

	public M2OGridEditor(List<AbstractModel> data) {
		this.data = data;
		initUI();
	}

	@Override
	public Component getComponent() {
		return component;
	}

	private void initUI() {
		component = new Div();
		component.setWidth("100%");
		component.setHeight("100%");
		component.setStyle("margin-bottom: 10px");
		for (int i = 0; i < data.size(); i++) {
			AbstractModel bean = data.get(i);
			Button btn = new Button(bean.toString());
			btn.setId(String.valueOf(bean.getId()));
			btn.setStyle("margin-right: 10px;");
			btn.setAttribute("model", bean);
			btn.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					setValue(btn.getAttribute("model"));
				}
			});
			btn.setParent(component);
		}
		this.setValue(data.get(0));
	}

	public void setValue(AbstractModel model) {
		this.value = model;
		for (Component comp : component.getChildren()) {
			if (comp instanceof Button) {
				Button btn = (Button) comp;
				if (model.equals(btn.getAttribute("model"))) {
					btn.setStyle("background-color: #357ebd;color: white;margin-right: 10px;");
				} else {
					btn.setStyle("margin-right: 10px;background-color: #fff;color: #333;");
				}
			}
		}
		if (this.getValue() == null) {
			setValue(data.get(0));
		}
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setRows(Object value) {

	}

	@Override
	public void setValue(Object value) {
		if (value instanceof AbstractModel) {
			setValue((AbstractModel) value);
		} else {
			// not implement yet
		}
	}

	@Override
	public void setEmpty() {
		// TODO Auto-generated method stub
		
	}

}
