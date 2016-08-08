package com.vietek.taxioperation.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.editor.M2OEditor;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class ComboboxSearch extends Combobox implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String EVENT_ON_SELECT_VALUE = "onSelectValue";
	private List<AbstractModel> lstmodel;
	private List<AbstractModel> cache;
	private Integer countmax = 10;
	private Integer loadpage = 1;
	private String sqlquery;
	private Class<?> modelclazz;

	public ComboboxSearch(Class<?> modelClazz, String query) {
		super();
		initCombobox();
		loadCache(modelClazz, query);
		loadModel(cache);
	}

	public ComboboxSearch(List<AbstractModel> lstmodel) {
		super();
		initCombobox();
		cache = (List<AbstractModel>) lstmodel;
	}

	private void initCombobox() {
		this.setButtonVisible(false);
		this.addEventListener(Events.ON_CHANGING, EVENT_ON_CHANGING);
		this.addEventListener(Events.ON_CLICK, this);
		this.addEventListener(Events.ON_SELECT, EVENT_ON_SELECT);
		this.addEventListener(Events.ON_CHANGE, EVENT_ON_CHANGE);
		this.setItemRenderer(new M2OComboItemRenderer<>());
		this.setAutodrop(true);
	}

	public List<?> getCache() {
		return cache;
	}

	public void setCache(List<AbstractModel> cache) {
		this.cache = cache;
	}

	private String inputText;

	public List<?> getLstmodel() {
		return lstmodel;
	}

	public void setLstmodel(List<AbstractModel> lstmodel) {
		this.cache = lstmodel;
		loadModel(lstmodel);
	}

	public ComboboxSearch() {
		initCombobox();
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CLICK)) {
			this.open();
			this.setText("");
			this.setSelectedIndex(-1);
			loadModel(cache);
		}

	}

	private void loadModel(List<AbstractModel> lsttmp) {
		if (lsttmp != null) {
			if (lsttmp.size() > (countmax * loadpage)) {
				ListModelList<AbstractModel> model = new ListModelList<AbstractModel>(
						lsttmp.subList(0, (countmax * loadpage)));
				model.add(null);
				this.setModel(model);

			} else {
				this.setModel(new ListModelList<AbstractModel>(lsttmp));
			}
		}

	}

	private EventListener<Event> EVENT_ON_CHANGE = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			InputEvent inputevent = (InputEvent) event;
			if (((Combobox) event.getTarget()).getSelectedItem() == null) {
				if (inputevent.getValue().equals("")) {
					postEventOnSearch(null);
				} else {
					Env.getHomePage().showNotificationErrorSelect("Không tìm thấy dữ liệu bạn cần! ",
							Clients.NOTIFICATION_TYPE_INFO);
				}
			}

		}

	};

	private EventListener<Event> EVENT_ON_SELECT = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			Comboitem item = ((Combobox) event.getTarget()).getSelectedItem();
			if (item != null) {
				if (item.getValue().equals(M2OEditor.LOAD_MORE)) {
					loadpage++;
					loadModel(cache);
					((Combobox) event.getTarget()).setSelectedItem(null);
					((Combobox) event.getTarget()).setValue(null);
					open();
				} else {
					AbstractModel data = (AbstractModel) item.getValue();
					postEventOnSearch(data);
				}
			}
		}
	};
	private EventListener<Event> EVENT_ON_CHANGING = new EventListener<Event>() {

		@Override
		public void onEvent(Event event) throws Exception {
			InputEvent inputEvent = (InputEvent) event;
			if (inputEvent.isChangingBySelectBack()) {
				setInputText("");
				return;
			}
			setInputText(inputEvent.getValue());
			if (cache != null && cache.size() > 0) {
				if (lstmodel == null) {
					lstmodel = new ArrayList<AbstractModel>();
				}
				lstmodel.clear();
				int value = 0;
				for (int i = 0; i < cache.size(); i++) {
					AbstractModel cachemodel = cache.get(i);
					if (cachemodel.toString().toLowerCase().contains(inputText.toLowerCase()) && value < countmax) {
						lstmodel.add(cache.get(i));
						value++;
					}
				}

			}
			loadModel(lstmodel);
			((Combobox) event.getTarget()).setOpen(true);
			((Combobox) event.getTarget()).open();
		}
	};

	private void postEventOnSearch(AbstractModel data) {
		Event eventpost = new Event(EVENT_ON_SELECT_VALUE, this, data);
		Events.postEvent(this, eventpost);
	}

	@SuppressWarnings("unchecked")
	public void loadCache(Class<?> modelClazz, String sqltmp) {
		this.sqlquery = sqltmp;
		this.modelclazz = modelClazz;
		if (cache != null) {
			cache.clear();
		}
		String controllerClassName = modelclazz.getName().replace("com.vietek.taxioperation.model",
				"com.vietek.taxioperation.controller") + "Controller";
		sqlquery = sqlquery + " Where Active = 1 ";

		try {
			cache = (List<AbstractModel>) ControllerUtils.getController(Class.forName(controllerClassName))
					.findPermission(modelclazz, sqlquery);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   public void setValue(AbstractModel value){
		Comboitem item = new Comboitem();
		item.setLabel(value.toString());
		item.setValue(value);
		this.appendChild(item);
		this.setSelectedItem(item);
   }
	public Integer getCountmax() {
		return countmax;
	}

	public void setCountmax(Integer countmax) {
		this.countmax = countmax;
	}

	public String getInputText() {
		return inputText;
	}

	public Class<?> getModelclazz() {
		return modelclazz;
	}

	public void setModelclazz(Class<?> modelclazz) {
		this.modelclazz = modelclazz;
	}

	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

	public Integer getLoadpage() {
		return loadpage;
	}

	public void setLoadpage(Integer loadpage) {
		this.loadpage = loadpage;
	}

	public String getSqlquery() {
		return sqlquery;
	}

	public void setSqlquery(String sqlquery) {
		this.sqlquery = sqlquery;
	}
}