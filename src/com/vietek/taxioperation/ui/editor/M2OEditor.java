package com.vietek.taxioperation.ui.editor;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;

import com.vietek.taxioperation.ui.util.M2OComboItemRenderer;
import com.vietek.taxioperation.util.ComboboxThread;

public class M2OEditor extends Editor {
	
	public static String LOAD_MORE = "load_more";
	private List<Object> list;
	private Object value;
	ComboboxThread t;
	private Object model;
	private String dataField;
	private String inputText = "";
	private int loadPages = 1;
	public M2OEditor() {
		component = new Combobox();
		((Combobox) component).setPlaceholder(" ---- Select item ------");
		((Combobox) component).setHflex("1");
		((Combobox) component).setAutodrop(true);
		((Combobox) component).addEventListener(Events.ON_CHANGING, this);
		((Combobox) component).addEventListener(Events.ON_OPEN, this);
		((Combobox) component).addEventListener(Events.ON_CHANGE, this);
		((Combobox) component).addEventListener(Events.ON_FULFILL, this);
		((Combobox) component).addEventListener(Events.ON_MOVE, this);
		((Combobox) component).addEventListener(Events.ON_SELECT, this);
		((Combobox) component).addEventListener(Events.ON_SELECTION, this);
		((Combobox) component).addEventListener(Events.ON_BLUR, this);
		((Combobox) component).addEventListener("onInitRenderLater", this);
	}

	public void setData() {
		((Combobox) component).setModel(new ListModelList<>(list));
		((Combobox) component).setItemRenderer(new M2OComboItemRenderer<>());
		((Combobox) component).setAutodrop(true);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setValue(Object value) {
		this.value = value;
		if (value == null) {
			((Combobox) component).setSelectedItem(null);
			((Combobox) component).setValue(null);
			return;
		}
		int status = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null && list.get(i).equals(value)) {
				if(((Combobox) component).getItemCount()>i)
					((Combobox) component).setSelectedIndex(i);
				status = 1;
				break;
			}
		}
		
		if (status == 0) {
			list.add(0, value);
			((ListModelList)((Combobox) component).getModel()).add(0, value);
			if(((Combobox) component).getItemCount()>0)
				((Combobox) component).setSelectedIndex(0);
		}
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public String getDataField() {
		return dataField;
	}

	public void setDataField(String dataField) {
		this.dataField = dataField;
	}

	@SuppressWarnings("unchecked") 
	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_MOVE)) {
			
		} else if (event.getName().equals(Events.ON_CHANGING)) {
			InputEvent inputEvent = (InputEvent) event;
			if (inputEvent.isChangingBySelectBack()) {
				inputText = "";
				return;
			}
			inputText = inputEvent.getValue();
			t = new ComboboxThread(model, dataField, inputText, loadPages);
			list = (List<Object>)t.getDataList();
			setData();
		} else if (event.getName().equals(Events.ON_SELECT)) {
			Comboitem item = ((Combobox) component).getSelectedItem();
			if (item == null) {
				setValue(null);
			}
			else if (item.getValue() != null && item.getValue().equals(LOAD_MORE)) {
				loadPages++;
				t = new ComboboxThread(model, dataField, inputText, loadPages);
				list = (List<Object>)t.getDataList();
				setData();
				setValue(null);
				((Combobox) component).open();
			}
			else if (((Combobox) component).getSelectedIndex() >= 0)
				value = list.get(((Combobox) component).getSelectedIndex());
			else
				value = null;
			postEventChangeValue();
		}else if (event.getName().equals(Events.ON_BLUR)) {
			if (((Combobox) component).getSelectedIndex() == -1 && inputText != null){
				((Combobox) component).setValue(null);
			}
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
