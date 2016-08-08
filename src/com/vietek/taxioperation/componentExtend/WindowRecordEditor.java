package com.vietek.taxioperation.componentExtend;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.common.AnnonationLinkedTable;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.EditorFactory;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * sonvh
 */
public class WindowRecordEditor extends WindowEditor {

	private Object modelObj;
	private List<GridColumn> colus;
	private HandleWindowEditor windowRecordHandler;
	private Grid table;
	private EnumAction action = EnumAction.NEW_ACTION;
	private Map<String, Editor> mapEditor;

	private static final long serialVersionUID = 7652597876200170966L;

	public WindowRecordEditor(Object modelObj, Component parent, HandleWindowEditor windowRecordHandler) {

		super(parent);
		this.modelObj = modelObj;
		this.colus = null;
		this.windowRecordHandler = windowRecordHandler;
		this.mapEditor = new HashMap<String, Editor>();
		initEvents();
	}

	@Override
	public void initEvents() {
	}

	public Object getUpdateModelObj() {
		for (Entry<String, Editor> entry : mapEditor.entrySet()) {
			String key = entry.getKey();
			Editor editor = entry.getValue();
			AbstractModel.setValue(modelObj, editor.getDataField(), editor.getValue());
		}
		return modelObj;
	}

	public Object getModelObj() {
		return modelObj;
	}

	public void setModelObj(Object modelObj, EnumAction action) {

		this.action = action;
		this.modelObj = modelObj;

		for (Entry<String, Editor> entry : mapEditor.entrySet()) {
			String key = entry.getKey();
			Editor editor = entry.getValue();
			if (action.equals(EnumAction.EDIT_ACTION))
				editor.setValue(AbstractModel.getValue(modelObj, editor.getDataField()));
			else {
				editor.setEmpty();
			}
		}

	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void initUIAfter() {
	}

	@Override
	public void initDetailUI(Component parent) {

		table = new Grid();
		table.setParent(parent);
		table.setVflex(true);
		table.setHflex("1");

	}

	private void createDeatil(List<GridColumn> colus) {
		Rows rows = new Rows();
		rows.setParent(table);

		for (GridColumn gridColumn : colus) {

			Row row = new Row();
			row.setParent(rows);
			createEditor(gridColumn, row);
		}
	}

	private void createEditor(GridColumn gridColumn, Row row) {

		/*
		 * label
		 */
		Label lb = new Label();
		lb.setParent(row);
		lb.setValue(gridColumn.getHeader());
		/*
		 * editor
		 */
		Field field = gridColumn.getField();
		Editor editor = null;

		if (field.getType().equals(String.class) || field.getType().equals(Timestamp.class)
				|| field.getType().equals(Integer.class) || field.getType().equals(int.class)
				|| field.getType().equals(BigDecimal.class) || field.getType().equals(Double.class)
				|| field.getType().equals(Time.class) || field.getType().equals(Boolean.class)) {

			editor = EditorFactory.getEditor(modelObj, field.getType(), field.getName());

		} else if (field.isAnnotationPresent(ManyToOne.class)) {

			editor = EditorFactory.getMany2OneEditor(modelObj, field.getName());

		} else if (field.isAnnotationPresent(ManyToMany.class)) {

			editor = EditorFactory.getMany2ManyEditor(modelObj, field.getName());

		} else if (field.isAnnotationPresent(OneToMany.class)) {
			// editor = EditorFactory.getMany2OneEditor(model, field.getName());
		} else if (field.isAnnotationPresent(FixedCombobox.class)) {

			editor = EditorFactory.getFixedCombobox(modelObj, field);

		} else if (field.isAnnotationPresent(AnnonationLinkedTable.class)) {

			editor = EditorFactory.getLinkedTable(modelObj, field);

		}

		if (editor != null) {
			editor.getComponent().setParent(row);
			// if (action.equals(EnumAction.EDIT_ACTION))
			// editor.setValue(AbstractModel.getValue(modelObj,
			// field.getName()));
			editor.setValueChangeListener(this);
			mapEditor.put(field.getName(), editor);
		}
		/*
		 * validate
		 */
	}

	public String getAction() {
		return action.toString();
	}

	public void setAction(EnumAction action) {
		this.action = action;
	}

	public List<GridColumn> getColus() {
		return colus;
	}

	public void setColus(List<GridColumn> colus) {
		this.colus = colus;
		createDeatil(this.colus);
	}

	@Override
	public void eventOnCLick_OK() {
		super.eventOnCLick_OK();
		if (action.equals(EnumAction.NEW_ACTION))
			getUpdateModelObj();

		windowRecordHandler.onOk(modelObj, action);
	}

	public void onEditorChangeValue(Event e) {
		Editor editor = (Editor) e.getData();
		if (editor != null) {
			Object value = editor.getValue();
			AbstractModel.setValue(modelObj, editor.getDataField(), value);
		}
	}

}
