package com.vietek.taxioperation.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

public class FixedComboboxEditor extends Editor {

	private List<Comboitem> lstItem;
	private Object value;

	public FixedComboboxEditor() {
		super();
		component = new Combobox();
		component.addEventListener(Events.ON_SELECT, this);
		((Combobox) component).setHflex("1");
		((Combobox) component).setAutodrop(true);
	}

	@Override
	public Object getValue() {
		// int index = ((Combobox) component).getSelectedIndex();
		// if (index >= 0) {
		// return lstItem.get(index).getValue();
		// }
		// return null;
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
		if (value == null) {
			((Combobox) component).setSelectedItem(null);
		} else {
			for (int i = 0; i < lstItem.size(); i++) {
				if (lstItem.get(i).getValue().equals(value)) {
					((Combobox) component).setSelectedIndex(i);
					break;
				}
			}
		}
	}

	public void setData(List<String> lstLabel, List<Integer> lstValue) {
		setMapValue(lstLabel, lstValue);
		for (int i = 0; i < lstItem.size(); i++) {
			lstItem.get(i).setParent(component);
		}
	}

	private void setMapValue(List<String> lstLabel, List<Integer> lstValue) {
		lstItem = new ArrayList<Comboitem>();
		for (int i = 0; i < lstLabel.size(); i++) {
			Comboitem comboitem = new Comboitem();
			comboitem.setLabel(lstLabel.get(i));
			comboitem.setValue(lstValue.get(i));
			lstItem.add(comboitem);
		}
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getName().equalsIgnoreCase(Events.ON_SELECT)) {
			int index = ((Combobox) component).getSelectedIndex();
			if (index >= 0) {
				value = lstItem.get(index).getValue();
			} else {
				value = null;
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