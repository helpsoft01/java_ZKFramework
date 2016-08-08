package com.vietek.taxioperation.ui.editor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.common.AnnonationLinkedTable;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.componentExtend.M2MEditorExt;
import com.vietek.taxioperation.componentExtend.M2OEditorExt;

public class EditorFactory {
	static List<?> dataListAll = null;

	public static Editor getEditor(Object model, Class<?> type, String dataField) {
		if (type.equals(String.class)) {
			Editor editor = new TextEditor();
			editor.setDataField(dataField);
			return editor;
		} else if (type.equals(Timestamp.class)) {
			Editor editor = new DateTimeEditor();
			editor.setDataField(dataField);
			return editor;
		} else if (type.equals(Integer.class) || type.equals(int.class)) {
			Editor editor = new IntEditor();
			editor.setDataField(dataField);
			return editor;
		} else if (type.equals(BigDecimal.class)) {
			Editor editor = new DecimalEditor();
			editor.setDataField(dataField);
			return editor;
		} else if (type.equals(Double.class)) {
			Editor editor = new DoubleEditor();
			editor.setDataField(dataField);
			return editor;
		} else if (type.equals(Time.class)) {
			Editor editor = new TimeEditor();
			editor.setDataField(dataField);
			return editor;
		} else if (type.equals(Boolean.class)) {
			Editor editor = new YesNoEditor();
			editor.setDataField(dataField);
			return editor;
		}
		return null;
	}

	public static Editor getEditor(Object model, Class<?> type, String dataField, String placehoder) {
		if (type.equals(String.class)) {
			placehoder = "Nhập " + placehoder;
			Editor editor = new TextEditor(placehoder);
			editor.setDataField(dataField);
			return editor;
		} else if (type.equals(Timestamp.class)) {
			placehoder = "Nhập " + placehoder;
			Editor editor = new DateTimeEditor(placehoder);
			editor.setDataField(dataField);
			return editor;
		} else if (type.equals(Integer.class) || type.equals(int.class)) {
			placehoder = "Nhập " + placehoder;
			Editor editor = new IntEditor(placehoder);
			editor.setDataField(dataField);
			return editor;
		} else if (type.equals(BigDecimal.class)) {
			placehoder = "Nhập " + placehoder;
			Editor editor = new DecimalEditor(placehoder);
			editor.setDataField(dataField);
			return editor;
		} else if (type.equals(Boolean.class)) {
			placehoder = "Có phải " + placehoder + " không ?";
			Editor editor = new YesNoEditor(placehoder);
			editor.setDataField(dataField);
			return editor;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Editor getMany2OneEditor(Object model, String dataField) {
		Editor editor = new M2OEditorExt(model, dataField);// M2OEditor();
		editor.setDataField(dataField);
		// editor.setDataField(dataField);
		// Class<?> clazz = AbstractModel.getDataType(model, dataField);

		// String className =
		// clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
		// String controllerClassName =
		// clazz.getName().replace("com.vietek.taxioperation.model",
		// "com.vietek.taxioperation.controller") + "Controller";
		// List<?> dataList = null;
		// try {
		// // dataList = ControllerUtils.getController(
		// // Class.forName(controllerClassName)).find(
		// // ConstantValueSearch.NUMBER_RECORD_OF_CBB,
		// // "from " + className);
		// dataList =
		// ControllerUtils.getController(Class.forName(controllerClassName))
		// .findPermissionLimit(ConstantValueSearch.NUMBER_RECORD_OF_CBB, clazz,
		// "from " + className);
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		// Start Vud
		// ComboboxThread t = new ComboboxThread(model, dataField, "", 1);
		// dataList = (List<Object>) t.getDataList();
		// if (dataList == null) {
		// dataList = new ArrayList<>();
		// }
		// End Vud

		// ((M2OEditorExt) editor).setModel(model);
		// ((M2OEditorExt) editor).setDataField(dataField);
		// ((M2OEditorExt) editor).setList((List<Object>) dataList);
		// ((M2OEditorExt) editor).setData();
		return editor;
	}

	@SuppressWarnings("unchecked")
	public static Editor getMany2ManyEditor(Object model, String dataField) {
		Editor editor = new M2MEditorExt(model, dataField);// M2MEditor();
		editor.setDataField(dataField);
		// List<?> dataList = null;
		// ChosenBoxWorker t = new ChosenBoxWorker(model, dataField, "", 1);
		// dataList = (List<Object>) t.getDataList();
		// ((M2MEditor) editor).setModel(model);
		// ((M2MEditor) editor).setDataField(dataField);
		// ((M2MEditor) editor).setList(dataList);
		// ((M2MEditor) editor).setData();
		return editor;
	}

	public static Editor getFixedCombobox(Object model, Field field) {
		Editor editor = new FixedComboboxEditor();
		editor.setDataField(field.getName());
		List<String> lstLabel = null;
		List<Integer> lstValue = null;
		if (field.isAnnotationPresent(FixedCombobox.class)) {
			Annotation annotation = field.getAnnotation(FixedCombobox.class);
			FixedCombobox fixedCombobox = (FixedCombobox) annotation;
			lstLabel = new ArrayList<String>();
			lstValue = new ArrayList<Integer>();
			for (int i = 0; i < fixedCombobox.label().length; i++) {
				lstLabel.add(fixedCombobox.label()[i]);
				lstValue.add(fixedCombobox.value()[i]);
			}
		}
		((FixedComboboxEditor) editor).setData(lstLabel, lstValue);
		return editor;
	}

	public static Editor getLinkedTable(Object model, Field field) {
		Editor editor = new LinkedTableEditor();
		editor.setDataField(field.getName());
		if (field.isAnnotationPresent(AnnonationLinkedTable.class)) {
			Annotation annotation = field.getAnnotation(AnnonationLinkedTable.class);
			AnnonationLinkedTable linkedTable = (AnnonationLinkedTable) annotation;
			String modelClazz = linkedTable.modelClazz();
			String whereClause = linkedTable.whereClause();
			String displayField = linkedTable.displayFieldName();
			((LinkedTableEditor) editor).setModelClazz(modelClazz);
			((LinkedTableEditor) editor).setWhereClause(whereClause);
			((LinkedTableEditor) editor).setDisplayField(displayField);
		}
		((LinkedTableEditor) editor).getListModel();
		((LinkedTableEditor) editor).setData();
		return editor;
	}
}
