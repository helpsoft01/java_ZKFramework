package com.vietek.taxioperation.ui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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
	private Set<Object> before;
	
	public VChosenbox() {
		super();
		initChosenbox();
	}

	private void initChosenbox() {
		this.addEventListener("onSearching", EVENT_ON_SEARCH);
		this.addEventListener("onOpen", EVENT_ON_OPEN);
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
			if (lstTemp.size() > 0) {
				if (before != null) {
					for (Object object1 : before) {
						boolean needPlus = true;
						for (Object object2 : lstTemp) {
							if (object1.equals(object2)) {
								needPlus = false;
								break;
							}
						}
						if (needPlus) {
							lstTemp.add(object1);
						}
					}
				}				
				setlstModel();
				if (before != null) {
					if (before.size() > 0) {
						((Chosenbox) event.getTarget()).setSelectedObjects(before);
					}
				}				
			}
		}
	};
	
	private EventListener<Event> EVENT_ON_OPEN = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			// TODO Auto-generated method stub	
			before = ((Chosenbox) event.getTarget()).getSelectedObjects(); 
			if (lstTemp.size() != lstAllModel.size()) {
				setLstAllModel(getLstAllModel());
			}		
			if (lstTemp.size() > 0) {
				((Chosenbox) event.getTarget()).setSelectedObjects(before);
			}			
			lstTemp = new ArrayList<>(); 
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
