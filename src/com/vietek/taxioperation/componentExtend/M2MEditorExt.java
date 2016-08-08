package com.vietek.taxioperation.componentExtend;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.collection.internal.PersistentSet;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.ListModelList;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.ui.editor.Editor;

public class M2MEditorExt extends Editor implements HandleWindowEditor {

	ModelInfor modelInfo;
	WindowSearchEditor windowFilter;
	private final String onEventLoad = "onEventLoadSource";
	private final String onEventFocus = "onEventFocus";
	private M2MEditorExt _this;
	Desktop desktop;
	private Object value;
	Chosenbox chosenbox;

	public M2MEditorExt(Object model, String nameField) {

		super();
		_this = this;
		desktop = Executions.getCurrent().getDesktop();
		try {
			this.modelInfo = new ModelInfor(model, nameField, true);

			initUI();
			initEvents();

			Events.postEvent(onEventLoad, component, "");
			Clients.showBusy(component, "Loading....");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initUI() {
		chosenbox = new Chosenbox();
		component = chosenbox;
		chosenbox.setHflex("1");
		chosenbox.setEmptyMessage("---Select Item-----");
		RenderItem_CSB render = new RenderItem_CSB();
		chosenbox.setItemRenderer(render);
	}

	private void initEvents() {
		chosenbox.addEventListener(onEventLoad, EVENTLOAD_SOURCE);
		chosenbox.addEventListener(onEventFocus, EVENTFOCUS);
		chosenbox.addEventListener("onSelect", EVENTSELECTED_ITEM);
		chosenbox.addEventListener("onSearching", EVENTSELECTING_ITEM);
	}

	private EventListener<Event> EVENTFOCUS = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			// setFocus((arg0.getData() == null ? "" :
			// arg0.getData().toString()));
		}

	};
	private EventListener<Event> EVENTLOAD_SOURCE = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			if (arg0.getData() != null) {

				List<Object> source = modelInfo.getSource(10, arg0.getData().toString());

				Set<Object> selectedObjects = chosenbox.getSelectedObjects();
				if (selectedObjects != null) {
					int index = 0;
					for (Object object : selectedObjects) {
						if (!source.contains(object)) {
							index = (source.size() - 1 < 0 ? 1 : source.size() - 1);
							source.add(index, object);
						}
					}
				}

				// Object o = source.get(0);
				// String model = o.getClass().toString();
				// model = model.substring("class ".length());
				//
				// Class<?> classController = Class.forName(model);
				Object obj = modelInfo.newInstance();// classController.newInstance();
				if (!source.contains(obj))
					source.add(obj);

				chosenbox.setModel(new ListModelList<>(source));
				if (selectedObjects != null)
					chosenbox.setSelectedObjects(selectedObjects);

				Clients.clearBusy(component);
				// setFocus(arg0.getData().toString());
				// Events.sendEvent(onEventFocus, _this,
				// arg0.getData().toString());

			}
		}
	};

	private EventListener<Event> EVENTSELECTING_ITEM = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			InputEvent input = (InputEvent) arg0;
			Events.postEvent(onEventLoad, component, input.getValue());
			Clients.showBusy(component, "Loading....");
		}
	};

	private EventListener<Event> EVENTSELECTED_ITEM = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			Iterator<Object> iter = chosenbox.getSelectedObjects().iterator();
			Set<Object> retVals = new HashSet<Object>();

			boolean isFind = false;
			while (iter.hasNext()) {
				Object obj = iter.next();
				Method setNameMethod = obj.getClass().getMethod("getId");
				int id = (int) setNameMethod.invoke(obj); // pass
															// arg

				if (id == 0) {
					isFind = true;

				} else
					retVals.add(obj);
			}
			List<Object> lstSel = new ArrayList<Object>(retVals);
			if (isFind) {
				windowFilter = new WindowSearchEditor(modelInfo, chosenbox.getParent(), _this);
				windowFilter.show(lstSel);
			}
			chosenbox.setSelectedObjects(retVals);

			value = retVals;
			postEventChangeValue();
		}
	};

	@Override
	public void onOk(Object retVal) {

		HashSet<?> lst = (HashSet<?>) retVal;
		if (lst.size() > 0) {
			List<Object> lstModel = new ArrayList<>();
			if (chosenbox.getModel() != null) {
				lstModel = ((ListModelList<Object>) chosenbox.getModel()).getInnerList();
			}
			try {

				Set<Object> selectedObjects = chosenbox.getSelectedObjects();
				boolean isExists = false;
				if (selectedObjects != null) {
					for (Object object : lst) {
						isExists = false;
						for (Object obCompare : selectedObjects) {
							if (obCompare.equals(object)) {
								isExists = true;
								break;
							}
						}
						if (!isExists)
							selectedObjects.add(object);

						isExists = false;
						for (Object obCompare : lstModel) {
							if (obCompare.equals(object)) {
								isExists = true;
								break;
							}
						}
						if (!isExists)
							lstModel.add(lstModel.size() - 1, object);
					}
				}

				Object o = lst.toArray()[0];
				String model = o.getClass().toString();
				model = model.substring("class ".length());

				Class<?> classController = Class.forName(model);
				Object obj = classController.newInstance();
				if (!lstModel.contains(obj))
					lstModel.add(obj);

				Set<Object> retVals = new HashSet<Object>();
				Set<Object> retValsselect = new HashSet<Object>();

				retVals.addAll(lstModel);
				retValsselect.addAll(selectedObjects);

				chosenbox.setModel(new ListModelList<>(retVals));
				if (selectedObjects != null) {
					chosenbox.setSelectedObjects(retValsselect);
					value = retValsselect;
					postEventChangeValue();
					Events.sendEvent(Events.ON_CHANGE, component, retValsselect);
				}
			} catch (Exception ex) {
				AppLogger.logDebug.error("", ex);
			}
		}
		// Events.sendEvent(onEventFocus, _this, "");
		setFocus("");
	}

	private void setFocus(String value) {
		Clients.evalJavaScript("chosenboxSetFocus('" + chosenbox.getUuid() + "','" + value + "')");
	}

	@Override
	public Object getValue() {
		return value;// chosenbox.getSelectedObjects();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setValue(Object sels) {

		try {

			Set<Object> retVals = new HashSet<Object>();

			Iterator<Object> iter = null;
			if (sels instanceof HashSet) {

				iter = ((HashSet) sels).iterator();
			} else if (sels instanceof PersistentSet) {

				iter = ((PersistentSet) sels).iterator();
			}
			while (iter.hasNext()) {
				Object obj = iter.next();
				retVals.add(obj);
			}
			Set<Object> selectVals = new HashSet<Object>();
			if (retVals.size() > 0) {

				// onSelectedSource((HashSet<?>) retVals);

				selectVals.addAll(retVals);

				Object o = retVals.toArray()[0];
				String model = o.getClass().toString();
				model = model.substring("class ".length());

				Class<?> classController;
				classController = Class.forName(model);

				Object obj = classController.newInstance();
				if (!retVals.contains(obj)) {
					retVals.add(obj);
					chosenbox.setModel(new ListModelList<>(retVals));
					for (Object object : selectVals) {
						chosenbox.addItemToSelection(object);
					}
					// chosenbox.addItemToSelection(arg0);//SelectedObjects(selectVals);
				}

			} else {
				chosenbox.setSelectedObjects(selectVals);
			}
			value = selectVals;
			postEventChangeValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRows(Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOk(Object obj, EnumAction actionF) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEmpty() {

		Set<Object> selectVals = new HashSet<Object>();
		chosenbox.setSelectedObjects(selectVals);
	}

}
