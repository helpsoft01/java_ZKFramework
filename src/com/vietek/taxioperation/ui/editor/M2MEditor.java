package com.vietek.taxioperation.ui.editor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.collection.internal.PersistentSet;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.ListModelList;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.ui.util.M2MItemRenderer;
import com.vietek.taxioperation.util.ChosenBoxWorker;

public class M2MEditor extends Editor {

	private List<?> list;
	private Object value;
	ChosenBoxWorker t;
	private Object model;
	private String dataField;
	private int loadPages = 1;
	private String inputText = "";

	public M2MEditor() {
		super();
		component = new Chosenbox();
		((Chosenbox) component).setHflex("1");
		((Chosenbox) component).addEventListener(Events.ON_CHANGING, this);
		((Chosenbox) component).addEventListener(Events.ON_OPEN, this);
		((Chosenbox) component).addEventListener(Events.ON_CHANGE, this);
		((Chosenbox) component).addEventListener("onSearching", this);
		((Chosenbox) component).addEventListener(Events.ON_SELECT, this);
		((Chosenbox) component).addEventListener(Events.ON_BLUR, this);

	}

	public void setData() {
//		Class<?> clazz = AbstractModel.getGenericType(model, dataField);
//		String className = clazz.getName().substring(
//				clazz.getName().lastIndexOf(".") + 1);
//		((Chosenbox) component).setModel((new PagingListModel<>(className)));
		((Chosenbox) component).setModel((new ListModelList<>(list)));
		((Chosenbox) component).setItemRenderer(new M2MItemRenderer<>());
	}

	@Override
	public Object getValue() {
		return value;
		// Set<Object> retVals = new HashSet<Object>();
		// Set<Object> sels = ((Chosenbox) component).getSelectedObjects();
		// Iterator<Object> iter = sels.iterator();
		// while (iter.hasNext()) {
		// Object val = iter.next();
		// retVals.add(val);
		// }
		// return retVals;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object sels) {
		value = sels;
		try {
			if (sels instanceof HashSet) {
				@SuppressWarnings("rawtypes")
				Iterator<Object> iter = ((HashSet) sels).iterator();
				Set<Object> retVals = new HashSet<Object>();
				while (iter.hasNext()) {
					Object obj = iter.next();
					retVals.add(obj);
				}
				((Chosenbox) component).setSelectedObjects(retVals);
				component.invalidate();
			} else if (sels instanceof PersistentSet) {
				Iterator<Object> iter = ((PersistentSet) sels).iterator();
				Set<Object> retVals = new HashSet<Object>();
				while (iter.hasNext()) {
					Object obj = iter.next();
					retVals.add(obj);
				}
				((Chosenbox) component).setSelectedObjects(retVals);
				component.invalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			((Chosenbox) component).setSelectedIndex(-1);
		}
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
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
			AppLogger.logDebug.info("Hon ca ok");
		} else if (event.getName().equals("onSearching")) {
			InputEvent inputEvent = (InputEvent)event;
			inputText = inputEvent.getValue();
			t = new ChosenBoxWorker(model, dataField, inputText, loadPages);
			list = (List<Object>)t.getDataList();
			setData();
		} else if (event.getName().equals(Events.ON_SELECT)) {
			Set<Object> retVals = new HashSet<Object>();
			Set<Object> sels = ((Chosenbox) component).getSelectedObjects();
			Iterator<Object> iter = sels.iterator();
			while (iter.hasNext()) {
				Object val = iter.next();
				retVals.add(val);
			}
			value = retVals;
			postEventChangeValue();
		} else if (event.getName().equals(Events.ON_BLUR)) {
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
