package com.vietek.taxioperation.componentExtend;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.South;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class WindowSearchEditor extends WindowEditor implements HandlFilterListbox {

	ModelInfor modelVar;
	WindowSearchEditor _this;
	ListViewExt lisbox;
	private final String onEventLoad = "onEventLoadSource";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1177932652786292985L;
	private HandleWindowEditor windowFilterHandler;

	public WindowSearchEditor(ModelInfor modelVar, Component parent, HandleWindowEditor windowFilterHandler) {

		super(parent);
		_this = this;

		this.modelVar = modelVar;
		this.parent = parent;
		this.windowFilterHandler = windowFilterHandler;
		lisbox.setModelInfor(modelVar);
		initEvents();
	}

	private EventListener<Event> EVENTLOAD_SOURCE = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			lisbox.init(null);
			Clients.clearBusy(arg0.getTarget());
		}
	};

	@Override
	public void onSelectItems(List<Object> lst) {
	}

	@Override
	public void initUIAfter() {
	}

	@Override
	public void initEvents() {
		this.addEventListener(onEventLoad, EVENTLOAD_SOURCE);
	}

	@Override
	public void initDetailUI(Component parent) {
		lisbox = new ListViewExt(null, this);
		lisbox.setParent(parent);
	}

	public void show(Object obj) {
		Clients.showBusy(this, "Loading....");
		lisbox.init((List<Object>) obj);
		Clients.clearBusy(this);
		super.show();
	}

	@Override
	public void eventOnClose_Window() {
		windowFilterHandler.onCancel();
		super.eventOnClose_Window();
	}

	@Override
	public void eventOnCLick_OK() {
		List<?> list = (List<?>) lisbox.getCheckItems();
		Set<Object> retVals = new HashSet<Object>();
		retVals.addAll(list);
		windowFilterHandler.onOk(retVals);
		super.eventOnCLick_OK();
	}
}
