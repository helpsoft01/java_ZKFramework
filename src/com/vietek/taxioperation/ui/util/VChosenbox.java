package com.vietek.taxioperation.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.ListModelList;

public class VChosenbox extends Chosenbox implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<?> lstAllModel;
	private List<Object> lstTemp;

	public VChosenbox() {
		super();
		initChosenbox();
	}

	private void initChosenbox() {
		this.addEventListener("onSearch", EVENT_ON_SEARCH);
	}

	private EventListener<Event> EVENT_ON_SEARCH = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			InputEvent input = (InputEvent) event;
			String strTemp = input.getValue();
			lstTemp = new ArrayList<>();
			if (lstAllModel != null) {
				for (Object object : lstAllModel) {
					if (object.toString().toLowerCase().contains(strTemp.toLowerCase())) {
						lstTemp.add(object);
					}
				}
			}
			Events.echoEvent("ahdhad", event.getTarget(), null);
		}
	};

	public List<?> getLstAllModel() {
		return lstAllModel;
	}

	@SuppressWarnings("unchecked")
	public void setLstAllModel(List<?> lstAllModel) {
		this.lstAllModel = lstAllModel;
		this.lstTemp = (List<Object>) lstAllModel;
		this.setlstModel();
	}

	public void setlstModel() {
		 this.setModel(new ListModelList<>(lstTemp));
	}
   public void ahdhad(){
	   this.setSelectedObjects(lstTemp);
   }
	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
