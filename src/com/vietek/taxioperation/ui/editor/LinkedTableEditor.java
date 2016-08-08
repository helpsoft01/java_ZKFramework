package com.vietek.taxioperation.ui.editor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;

import com.vietek.taxioperation.util.ControllerUtils;

/**
 * 
 * @author VuDo
 * 
 */

public class LinkedTableEditor extends Editor {
	private List<LinkedTableModel> lstItem = new ArrayList<LinkedTableEditor.LinkedTableModel>();
	private String modelClazz;
	private String whereClause;
	private String displayField;
	private Object value;

	public LinkedTableEditor() {
		super();
		component = new Combobox();
		((Combobox) component).setHflex("1");
		((Combobox) component).setAutodrop(true);
		((Combobox) component).addEventListener(Events.ON_OPEN, this);
		((Combobox) component).addEventListener(Events.ON_SELECT, this);
		((Combobox) component).setItemRenderer(new ComboitemRenderer<LinkedTableModel>() {
			@Override
			public void render(Comboitem item, LinkedTableModel data, int arg2) throws Exception {
				item.setLabel(data.getLabel());
				item.setValue(data);
			}
		});
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
		((Combobox) component).setValue(null);
		if (value == null) {
			((Combobox) component).setSelectedItem(null);
		} else {
			if (value.equals(0)) {
				((Combobox) component).setSelectedItem(null);
			}
			for (int i = 0; i < lstItem.size(); i++) {
				if (lstItem.get(i).getId().equals(value)) {
					((Combobox) component).setSelectedIndex(i);
					break;
				}
			}
		}
	}

	public void setData() {
		((Combobox) component).setModel(new ListModelList<LinkedTableModel>(lstItem));
	}

	public String getModelClazz() {
		return modelClazz;
	}

	public void setModelClazz(String modelClazz) {
		this.modelClazz = modelClazz;
	}

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public String getDisplayField() {
		return displayField;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_OPEN)) {
			// this.getListModel();
			// this.setData();
		}
		if (event.getName().equals(Events.ON_SELECT)) {
			int index = ((Combobox) component).getSelectedIndex();
			Comboitem item = ((Combobox) component).getSelectedItem();
			if (item != null) {
				value = ((LinkedTableModel) item.getValue()).getId();
			} else {
				value = 0;
			}
			postEventChangeValue();
		}
	}

	public void getListModel() {
		this.lstItem.clear();
		String modelName = modelClazz.substring(modelClazz.indexOf(".") + 1);
		String controllerName = "com.vietek.taxioperation.controller." + modelName + "Controller";
		String whereString = "";
		if (whereClause.length() > 0) {
			whereString = " where " + whereClause;
		}
		try {
			List<?> lstModel = ControllerUtils.getController(Class.forName(controllerName))
					.find("from " + modelName + whereString);
			String clazzName = "com.vietek.taxioperation.model." + modelName;
			Class<? extends Object> clazz = Class.forName(clazzName);

			for (Object object : lstModel) {
				LinkedTableModel item = new LinkedTableModel();
				Field field = clazz.getDeclaredField(displayField);
				field.setAccessible(true);
				String label = field.get(object).toString();
				item.setLabel(label);
				field = clazz.getDeclaredField("id");
				field.setAccessible(true);
				int value = field.getInt(object);
				item.setId(value);
				lstItem.add(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class LinkedTableModel {
		private Integer id;
		private String label;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
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