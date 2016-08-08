package com.vietek.taxioperation.componentExtend;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.editor.Editor;

public class M2OEditorExt extends Editor implements HandleWindowEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1301094916723039702L;
	List<?> source;
	ModelInfor modelInfo;
	private Object value;
	WindowSearchEditor windowFilter;
	private final String onEventLoad = "onEventLoadSource";
	private M2OEditorExt _this;
	private Object oldSelectedItem = null;
	Combobox combobox;

	public M2OEditorExt(Object model, String nameField) {

		super();
		_this = this;
		this.modelInfo = new ModelInfor(model, nameField, false);
		initUI();
		initEvents();

		Events.postEvent(onEventLoad, component, "");
		Clients.showBusy(component, "Loading....");
	}

	private void initUI() {
		combobox = new Combobox();
		component = combobox;
		combobox.setPlaceholder("---Select Item-----");
		combobox.setAutodrop(true);
		combobox.setHflex("1");

		RenderItem_CBB render = new RenderItem_CBB();
		combobox.setItemRenderer(render);
	}

	private void initEvents() {
		combobox.addEventListener(Events.ON_SELECT, EVENTCLICK_ITEM);
		combobox.addEventListener(Events.ON_CHANGING, EVENTCHANGING_ITEM);
		combobox.addEventListener(onEventLoad, EVENTLOAD_SOURCE);
	}

	// public void setParent(Component arg0) {
	//
	// super.setParent(arg0);
	// // windowFilter = new ExtFilterWindow(this.modelInfo, arg0, this);
	// }
	private void setSource(ListModelList<?> source) {
		if (!source.contains(getValue())) {
			List<Object> lstModel = (List<Object>) source.getInnerList();
			if (getValue() != null)
				lstModel.add(getValue());
		}
		combobox.setModel(source);
	}

	private EventListener<Event> EVENTLOAD_SOURCE = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			ListModelList<?> source = new ListModelList<>(modelInfo.getSource(10, arg0.getData().toString()));
			setSource(source);
			// if (!source.contains(getValue())) {
			// List<Object> lstModel = (List<Object>) source.getInnerList();
			// if (getValue() != null)
			// lstModel.add(getValue());
			// }
			// combobox.setModel(source);
			Clients.clearBusy(arg0.getTarget());
		}
	};
	private EventListener<Event> EVENTCHANGING_ITEM = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			InputEvent input = (InputEvent) arg0;
			Events.postEvent(onEventLoad, component, input.getValue());
			Clients.showBusy(component, "Loading....");

			if (input.getValue().isEmpty()) {
				setValue(null);
			}
		}
	};

	private EventListener<Event> EVENTCLICK_ITEM = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			Combobox target = (Combobox) arg0.getTarget();
			if (target.getSelectedItem() != null) {
				Comboitem selectedItem = combobox.getSelectedItem();

				Object obj = selectedItem.getValue();
				Method setNameMethod = obj.getClass().getMethod("getId");
				int id = (int) setNameMethod.invoke(obj); // pass arg

				if (id == 0) {

					if (windowFilter == null)
						windowFilter = new WindowSearchEditor(modelInfo, component.getParent(), _this);
					List<Object> lstSel = new ArrayList<Object>();
					lstSel.add(oldSelectedItem);
					windowFilter.show(lstSel);

					List<Comboitem> lstItem = combobox.getItems();
					Comboitem cbItem = null;
					if (oldSelectedItem != null) {
						for (Comboitem comboitem : lstItem) {
							if (comboitem.getValue().equals(oldSelectedItem)) {
								cbItem = comboitem;
								break;
							}
						}
					}

					if (cbItem != null)
						combobox.setSelectedItem(cbItem);
					else
						combobox.setSelectedIndex(-1);

					combobox.setDisabled(true);
					Clients.showBusy(component, "Đang tải....");
				} else {
					value = obj;
					// modelInfo.changeValueModel(value);
					postEventChangeValue();
					oldSelectedItem = obj;
				}
			}
		}
	};

	@Override
	public void onOk(Object retVal) {

		HashSet<?> lst = (HashSet<?>) retVal;
		onCancel();

		// Messagebox.show(lst.toString());
		if (lst.size() > 0) {

			Object obj = lst.toArray()[0];
			setValue(obj);
		}
	}

	@Override
	public void onCancel() {
		combobox.setDisabled(false);
		Clients.clearBusy(component);
	}

	@Override
	public Object getValue() {
		if (combobox.getSelectedItem() != null)
			value = combobox.getSelectedItem().getValue();

		return value;
	}

	@Override
	public void setValue(Object value) {

		this.value = value;
		if (value == null) {
			combobox.setSelectedIndex(-1);
			combobox.setSelectedItem(null);
			combobox.setValue(null);
			// return;
		} else {

			Comboitem item = new Comboitem(value.toString());
			item.setValue(value);

			if (combobox.getModel() == null) {
				ListModelList<?> source = new ListModelList<>(modelInfo.getSource(10, ""));
				combobox.setModel(source);
				for (Object object : source) {
					Comboitem itemCB = new Comboitem(object.toString());
					itemCB.setValue(value);
					combobox.getItems().add(itemCB);
				}
			}
			List<Object> lstModel = ((ListModelList<Object>) combobox.getModel()).getInnerList();

			if (!lstModel.contains(value)) {

				int index = (combobox.getItems().size() == 0 ? 0 : combobox.getItems().size() - 1);

				combobox.getItems().add(index, item);
				lstModel.add(value);
			}
			for (Comboitem object : combobox.getItems()) {
				if (object.getValue().equals(value)) {
					combobox.setSelectedItem(object);
					break;
				}
			}
		}

		// modelInfo.changeValueModel(value);
		postEventChangeValue();
		if (combobox.getSelectedItem() != null)
			oldSelectedItem = combobox.getSelectedItem().getValue();
		else
			oldSelectedItem = null;
	}

	@Override
	public void setRows(Object value) {

	}

	@Override
	public void onOk(Object obj, EnumAction actionF) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEmpty() {

		combobox.setSelectedIndex(-1);
		combobox.setSelectedItem(null);
	}
}
