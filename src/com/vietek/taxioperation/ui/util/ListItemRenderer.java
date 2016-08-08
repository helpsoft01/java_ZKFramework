package com.vietek.taxioperation.ui.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.vietek.taxioperation.common.AnnonationLinkedTable;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.util.ControllerUtils;

public class ListItemRenderer<T> implements ListitemRenderer<T> {

	ArrayList<GridColumn> gridColumns;
	HandleItemListRender handleItemListRender;
	// private Map<String, List<?>> mapLinkTable = new HashMap<String,
	// List<?>>();

	public ListItemRenderer(ArrayList<GridColumn> gridColumns, HandleItemListRender handleItemListRender) {
		super();
		this.gridColumns = gridColumns;
		this.handleItemListRender = handleItemListRender;
	}

	public ListItemRenderer(ArrayList<GridColumn> gridColumns) {
		this(gridColumns, null);
		// super();
		// this.gridColumns = gridColumns;
		// this.handleItemListRender = null;
	}

	@Override
	public void render(Listitem item, T data, int index) throws Exception {
		item.setValue(data);
		// Add Edit, Delete button
		// Listcell cell = new Listcell();
		// cell.setParent(item);
		// Button bt_edit = new Button("Chi tiết");
		// bt_edit.setSclass("btn-warning");
		// bt_edit.setParent(cell);
		for (int i = 0; i < gridColumns.size(); i++) {
			GridColumn column = gridColumns.get(i);
			if (data != null && column.getGetDataMethod() != null) {
				Method method = data.getClass().getMethod(column.getGetDataMethod());
				Object val = method.invoke(data);
				if (column.getFieldName() != null && column.getFieldName().length() > 0) {
					Field field = column.getModelClazz().getDeclaredField(column.getFieldName());
					if (field.isAnnotationPresent(FixedCombobox.class)) {
						this.createCellFixedCombobox(item, field, val);
						continue;
					} else if (field.isAnnotationPresent(AnnonationLinkedTable.class)) {
						this.createCellLinkedMap(item, field, val);
						continue;
					} else if (field.isAnnotationPresent(FixedTimestamp.class)) {
						this.createCellTimestamp(item, field, val);
						continue;
					}
				}
				if (column.getClazz().equals(Boolean.class)) {
					Checkbox checkbox = new Checkbox();
					checkbox.setLabel("");
					checkbox.setDisabled(true);
					if (val == null || !(boolean) val) {
						checkbox.setChecked(false);
					} else {
						checkbox.setChecked(true);
					}
					Listcell lstCell = new Listcell();
					lstCell.setParent(item);
					lstCell.setStyle("text-align: center");
					lstCell.appendChild(checkbox);
				} else {
					if (item != null)
						item.appendChild(new Listcell(val == null ? "" : val.toString()));
				}
			}
		}

		// //Add Edit, Delete button
		// Listcell cell = new Listcell();
		// cell.setParent(item);
		// Button bt_edit = new Button("Sửa");
		// bt_edit.setSclass("btn-warning");
		// bt_edit.setParent(cell);
		// //
		// cell = new Listcell();
		// cell.setParent(item);
		// Button bt_delete = new Button("Xoá");
		// bt_delete.setSclass("btn-danger");
		// bt_delete.setParent(cell);
		if (handleItemListRender != null)
			handleItemListRender.onRender(item, data, index);
	}

	private void createCellFixedCombobox(Listitem item, Field field, Object val) {
		if (val == null) {
			item.appendChild(new Listcell(val == null ? "" : val.toString()));
		} else {
			Annotation ann = field.getAnnotation(FixedCombobox.class);
			FixedCombobox fixedCombobox = (FixedCombobox) ann;
			boolean isNotExist = true;
			for (int j = 0; j < fixedCombobox.value().length; j++) {
				if (fixedCombobox.value()[j] == (int) val) {
					item.appendChild(new Listcell(fixedCombobox.label()[j]));
					isNotExist = false;
					break;
				}
			}
			if (isNotExist) {
				item.appendChild(new Label(""));
			}
		}
	}

	private void createCellTimestamp(Listitem item, Field field, Object val) {
		if (val == null) {
			item.appendChild(new Listcell(val == null ? "" : val.toString()));
		} else {
			Annotation annotation = field.getAnnotation(FixedTimestamp.class);
			FixedTimestamp fixedTimestamp = (FixedTimestamp) annotation;
			DateFormat dateFormat = new SimpleDateFormat(fixedTimestamp.format());
			String strTime = dateFormat.format(new Date(((Timestamp) val).getTime()));
			item.appendChild(new Listcell(strTime));
		}
	}

	private void createCellLinkedMap(Listitem item, Field field, Object val)
			throws ClassNotFoundException, NoSuchFieldException, SecurityException {
		if (val == null || (int) val == 0) {
			item.appendChild(new Listcell(""));
		} else {
			Annotation ann = field.getAnnotation(AnnonationLinkedTable.class);
			AnnonationLinkedTable annLinkedTable = (AnnonationLinkedTable) ann;
			String clazzName = "com.vietek.taxioperation.model." + annLinkedTable.modelClazz();
			Class<? extends Object> clazz = Class.forName(clazzName);
			String modelClazz = annLinkedTable.modelClazz();
			List<?> lstValue = this.getListValue(modelClazz);
			if (lstValue != null) {
				for (Object object : lstValue) {
					if (object.getClass() == clazz) {
						Field fieldTmp = clazz.getDeclaredField("id");
						try {
							fieldTmp.setAccessible(true);
							int id = fieldTmp.getInt(object);
							if (id == (int) val) {
								fieldTmp = clazz.getDeclaredField(annLinkedTable.displayFieldName());
								fieldTmp.setAccessible(true);
								String labelValue = (String) fieldTmp.get(object);
								item.appendChild(new Listcell(labelValue));
								break;
							}
						} catch (Exception e) {
							item.appendChild(new Listcell(""));
							e.printStackTrace();
						}

					}
				}

			}
		}
	}

	private List<?> getListValue(String modelClazz) {
		String controllerName = "com.vietek.taxioperation.controller." + modelClazz + "Controller";
		String whereString = "";
		List<?> lstModel = null;
		try {
			lstModel = ControllerUtils.getController(Class.forName(controllerName))
					.find("from " + modelClazz + whereString);
		} catch (Exception e) {
			lstModel = null;
			e.printStackTrace();
		}
		return lstModel;
	}
}
