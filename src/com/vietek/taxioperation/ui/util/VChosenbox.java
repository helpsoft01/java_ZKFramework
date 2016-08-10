package com.vietek.taxioperation.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.ListModelList;

public class VChosenbox extends Chosenbox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<?> lstAllModel;

	public VChosenbox() {
		super();
		initChosenbox();
	}

	private void initChosenbox() {
		this.addEventListener(Events.ON_CHANGING, EVENT_ON_CHANGING);
	}

	private EventListener<Event> EVENT_ON_CHANGING = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			String strTemp = (String) event.getData();
			List<Object> lstTemp = new ArrayList<>();
			if (lstAllModel != null) {
				for (Object object : lstAllModel) {
					if (object.toString().toLowerCase().contains(strTemp.toLowerCase())) {
						lstTemp.add(object);
					}
				}
				setModel(lstTemp);
			}
		}
	};

	public List<?> getLstAllModel() {
		return lstAllModel;
	}

	@SuppressWarnings("unchecked")
	public void setLstAllModel(List<?> lstAllModel) {
		this.lstAllModel = lstAllModel;
		this.setModel((List<Object>) lstAllModel);
	}

	private void setModel(List<Object> lst) {
		super.setModel(new ListModelList<>(lst));
	}

}
