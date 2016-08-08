package com.vietek.taxioperation.ui.util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.AnnonationLinkedTable;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.ApplyEditor;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.EnumUserAction;
import com.vietek.taxioperation.common.ColumnInfor;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.common.Validation;
import com.vietek.taxioperation.controller.BasicController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.EditorFactory;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.SaveLogToQueue;

public class BasicDetailWindow extends Window implements Serializable, EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1704036309175052925L;

	private AbstractWindowPanel listWindow;
	protected AbstractModel model;
	protected HashMap<String, Editor> mapEditor = new HashMap<String, Editor>();
	private HashMap<String, String> mapValidation = new HashMap<String, String>();
	private Hlayout bottonLayout;
	private Button btn_save;
	private Button btn_cancel;

	public BasicDetailWindow(AbstractWindowPanel listWindow) {
		this.listWindow = listWindow;
		this.addEventListener(Events.ON_OK, this);
		this.addEventListener(Events.ON_CANCEL, this);
	}

	public BasicDetailWindow(AbstractModel model, AbstractWindowPanel listWindow) {
		this.model = model;
		this.listWindow = listWindow;
		this.addEventListener(Events.ON_OK, this);
		this.addEventListener(Events.ON_CANCEL, this);
		init();
		// this.addEve
	}

	private void init() {
		this.setClosable(true);
		this.setMaximizable(true);
		// this.setWidth("600px");
		// this.setHeight("600px");
		this.addEventListener(Events.ON_CANCEL, this);
		this.beforInit();
		createMapEditor();
		createForm();
		initUI();
		// Events.echoEvent("setDefaultValue", this, null);
	}

	public void beforInit() {

	}

	public void initUI() {
		bottonLayout = new Hlayout();
		bottonLayout.setStyle("padding:10px; text-align: center;");
		bottonLayout.setParent(this);
		bottonLayout.setValign("center");
		btn_save = new Button(CommonDefine.Tittle.TITLE_BTN_SAVE);
		btn_save.setImage("/themes/images/save_16.png");
		// btn_save.setSclass("btn-success");
		btn_save.setParent(bottonLayout);
		btn_save.addEventListener(Events.ON_CLICK, this);
		btn_cancel = new Button(CommonDefine.Tittle.TITLE_BTN_CLOSE);
		btn_cancel.setParent(bottonLayout);
		btn_cancel.setImage("/themes/images/close_16.png");
		btn_cancel.addEventListener(Events.ON_CLICK, this);
	}

	public void createForm() {
		Iterator<String> keys = mapEditor.keySet().iterator();
		// setup grid
		Grid grid = new Grid();
		grid.setParent(this);
		grid.setSclass("vt-form");

		// setup columns
		Columns columns = new Columns();
		columns.setParent(grid);
		Column column = new Column();
		column.setWidth("30%");
		column.setAlign("right");
		column.setParent(columns);
		column = new Column();
		column.setWidth("70%");
		column.setParent(columns);

		// setup rows
		Rows rows = new Rows();
		rows.setParent(grid);

		while (keys.hasNext()) {

			Editor editor = mapEditor.get(keys.next());
			if (editor.getDataField() != "id") {
				Row row = new Row();
				row.setParent(rows);

				row.appendChild(new Label(editor.getLable())); // editor.getDataField()
				row.appendChild(editor.getComponent());
			}
		}
	}

	public void createMapEditor() {
		try {
			Field[] fields = model.getClass().getDeclaredFields();
			getValidationValueAnnotation(fields);
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Editor editor = null;
				Annotation[] ann = field.getAnnotations();
				if (ann.length > 0 && ann[0].annotationType().equals(FixedCombobox.class)) {
					editor = EditorFactory.getFixedCombobox(model, field);
				} else if (ann.length > 0 && ann[0].annotationType().equals(AnnonationLinkedTable.class)) {
					editor = EditorFactory.getLinkedTable(model, field);
				} else if (ann.length > 0 && ann[0].annotationType().equals(ApplyEditor.class)) {
					editor = getApplyEditor(ann[0], field);
				} else if (field.getType().equals(String.class) || field.getType().equals(Timestamp.class)
						|| field.getType().equals(Integer.class) || field.getType().equals(int.class)
						|| field.getType().equals(BigDecimal.class) || field.getType().equals(Double.class)
						|| field.getType().equals(Time.class) || field.getType().equals(Boolean.class)) {
					editor = EditorFactory.getEditor(model, field.getType(), field.getName());
				}
				// For Relation field: Many2Many, Many2One, One2Many
				else {
					Annotation[] ans = field.getAnnotations();
					for (int j = 0; j < ans.length; j++) {
						Annotation an = ans[j];
						if (an.annotationType().equals(javax.persistence.ManyToOne.class)) {
							editor = EditorFactory.getMany2OneEditor(model, field.getName());

							break;
						} else if (an.annotationType().equals(javax.persistence.ManyToMany.class)) {
							editor = EditorFactory.getMany2ManyEditor(model, field.getName());
							break;
						} else if (an.annotationType().equals(javax.persistence.OneToMany.class)) {
						}

					}
				}
				if (editor != null) {
					editor.setValueChangeListener(this);
					// editor.getComponent().setAttribute(Editor.EDITOR,
					// editor);
					// editor.getComponent().addEventListener(Events.ON_CHANGE,
					// this);
					// if (editor.getComponent() instanceof Checkbox) {
					// editor.getComponent().addEventListener(Events.ON_CHECK,
					// this);
					// }
					// if (editor.getComponent() instanceof Chosenbox) {
					// editor.getComponent().addEventListener(Events.ON_SELECT,
					// this);
					// }
					mapEditor.put(field.getName(), editor);
				}
			}

			/*
			 * set label
			 */
			Field[] fieldsClass = model.getClass().getDeclaredFields();
			for (Map.Entry<String, Editor> entry : mapEditor.entrySet()) {
				String key = entry.getKey();
				Editor edit = entry.getValue();
				//
				for (Field field : fieldsClass) {
					if (field.isAnnotationPresent(ColumnInfor.class)) {
						ColumnInfor fAnno = field.getAnnotation(ColumnInfor.class);
						AppLogger.logDebug.info("name:" + fAnno.name() + "label: " + fAnno.label());

						if (key.equals(fAnno.name()))
							edit.setLable(fAnno.label());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setDefaultValue() {
		Iterator<String> keys = mapEditor.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			Editor editor = mapEditor.get(key);
			Object defaultvalue = AbstractModel.getValue(model, editor.getDataField());
			editor.setValue(defaultvalue);
		}
	}

	@Override
	public void onEvent(Event event) throws Exception {
		// if (event.getName().equals(Events.ON_CHANGE)
		// || event.getName().equals(Events.ON_SELECT)
		// || event.getName().equals(Events.ON_CHECK)) {
		// Editor editor = (Editor) event.getTarget().getAttribute(
		// Editor.EDITOR);
		// if (editor != null) {
		// Object value = editor.getValue();
		// AbstractModel.setValue(model, editor.getDataField(), value);
		// }
		// } else
		if (event.getTarget().equals(btn_save)) {
			String err = Validate(mapValidation);
			if (StringUtils.isEmpty(err))
				this.handleSaveEvent();
			else {
				Env.getHomePage().showValidateForm(err, Clients.NOTIFICATION_TYPE_WARNING);
				return;
			}
		} else if (event.getTarget().equals(btn_cancel)
				|| (event.getName().equals(Events.ON_CANCEL) && event.getTarget().equals(this))) {
			this.setVisible(false);
			/*
			 * Sonvh Không lấy lại dữ liệu khi Cancel Vì không có dữ liệu mới
			 * cần cập nhật
			 */
			// this.getModel().refresh();
			/*
			 * end
			 */
			Env.getHomePage().showNotification("Bỏ qua thay đổi!", Clients.NOTIFICATION_TYPE_INFO);
			Events.postEvent(Events.ON_CLOSE, this, null);
			if (listWindow != null) {
				listWindow.refresh();
			}
		} else if (event.getTarget().equals(this)) {
			if (event.getName().equals(Events.ON_OK)) {
				this.handleSaveEvent();
			}
		}
	}

	/**
	 * @author batt
	 * @param list
	 *            all field of form
	 * 
	 */

	protected void getValidationValueAnnotation(Field[] fields) {
		fields = model.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().equals(Validation.class)) {
					mapValidation.put(field.getName(), annotation.toString());
				}
			}
		}
	}

	/**
	 * @author batt
	 * @param annotation
	 *            of content
	 * @return map : is content of annotation in a field
	 */

	private HashMap<String, String> stringToMap(String str) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (str == null || str.length() == 0) {
			return mapValidation;
		}
		try {
			String temp = (String) str.subSequence(str.indexOf("(") + 1, str.length() - 1);
			String[] strArr = temp.split(",");
			if (strArr.length > 0) {
				for (String string : strArr) {
					String[] arrTemp = string.split("=");
					if (arrTemp.length == 2 && arrTemp[1] != null) {
						map.put(arrTemp[0].trim(), arrTemp[1].trim());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @author batt
	 * @param map
	 *            annotation a field
	 * @return errors message
	 */

	private String Validate(HashMap<String, String> map) {
		StringBuilder errStr = new StringBuilder("");
		if (map == null || map.size() == 0) {
			return errStr.toString();
		}
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			String msg = "";
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
			HashMap<String, String> mapTemp = stringToMap(entry.getValue());
			String field = entry.getKey();
			msg = validateDetailField(mapTemp, field);
			if (StringUtils.isNotEmpty(msg)) {
				errStr.append(" - ").append(msg);
			}
		}
		return errStr.toString();
	}

	public boolean checkExitsValue(AbstractModel model, Object value, String field) {
		Boolean result = false;
		BasicController<?> controller = model.getControler();
		String query = "from " + model.getClass().getName().replace("com.vietek.taxioperation.model.", "") + " "
				+ "where " + field + "= ?";
		List<?> lstmp = controller.find(query, value);
		if (lstmp != null && lstmp.size() > 0) {
			if (model.getId() == 0) {
				result = true;
			} else if (model.getId() > 0 && model.getId() != ((AbstractModel) lstmp.get(0)).getId()) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * @author batt
	 * @param map
	 *            annotation a field
	 * @return map : is content of annotation in a field
	 */

	private String validateDetailField(HashMap<String, String> map, String field) {
		String msg = "";
		String title = map.get("title");
		if (StringUtils.isEmpty(title)) {
			return null;
		}
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		Object realvalue = AbstractModel.getValue(model, field);
		if (realvalue == null || realvalue instanceof AbstractModel) {
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				if (StringUtils.equals(entry.getKey().toString(), CommonDefine.AnnotationTitle.TITLE_NULLABLE)
						&& StringUtils.equals(entry.getValue().toString(), "false")) {
					if (realvalue == null)
						msg += title + " không được để trống! <br>";
				} else if (StringUtils.equals(entry.getKey().toString(), CommonDefine.AnnotationTitle.TITLE_ALOW_EXIST)
						&& StringUtils.equals(entry.getValue().toString(), "false")) {
					if (checkExitsValue(model, realvalue, field))
						msg += title + " đã tồn tại! <br>";
				}
			}
		} else {
			String value = String.valueOf(realvalue);
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				if (StringUtils.equals(entry.getKey().toString(), CommonDefine.AnnotationTitle.TITLE_NULLABLE)
						&& StringUtils.equals(entry.getValue().toString(), "false")) {
					if (StringUtils.isEmpty(value))
						msg += title + " không được để trống! <br>";
				} else if (StringUtils.equals(entry.getKey().toString(),
						CommonDefine.AnnotationTitle.TITTLE_MIN_LENGHT)) {
					if (StringUtils.checkMinLength(value, Integer.parseInt(entry.getValue())))
						msg += title + "phải nhiều hơn " + entry.getValue() + " ký tự! <br>";
				} else if (StringUtils.equals(entry.getKey().toString(),
						CommonDefine.AnnotationTitle.TITTLE_MAX_LENGHT)) {
					if (!StringUtils.checkMaxLength(value, Integer.parseInt(entry.getValue())))
						msg += title + " không được vượt quá " + entry.getValue() + " ký tự! <br>";
				} else if (StringUtils.equals(entry.getKey().toString(), CommonDefine.AnnotationTitle.TITTLE_REGEX)) {
					if (StringUtils.isNotEmpty(value) && !StringUtils.checkRegexStr(value, entry.getValue()))
						msg += title + " không hợp lệ! <br>";
				} else if (StringUtils.equals(entry.getKey().toString(), CommonDefine.AnnotationTitle.TITLE_IS_EMAIL)
						&& StringUtils.equals(entry.getValue().toString(), "true")) {
					if (StringUtils.isNotEmpty(value) && !StringUtils.isValidEmail(value))
						msg += title + " không hợp lệ! <br>";
				} else if (StringUtils.equals(entry.getKey().toString(),
						CommonDefine.AnnotationTitle.TITLE_IS_HAS_SPECIAL_CHAR)
						&& StringUtils.equals(entry.getValue().toString(), "false")) {
					if (StringUtils.isNotEmpty(value) && StringUtils.isHasSpecialChar(value))
						msg += title + " không hợp lệ! <br>";
				} else if (StringUtils.equals(entry.getKey().toString(), CommonDefine.AnnotationTitle.TITLE_ALOW_EXIST)
						&& StringUtils.equals(entry.getValue().toString(), "false")) {
					if (checkExitsValue(model, realvalue, field))
						msg += title + " đã tồn tại! <br>";
				}
			}
		}
		return msg;
	}

	/**
	 * 
	 *
	 * @author VuD
	 */
	public void handleSaveEvent() {
		int lastid = model.getId();
		EnumUserAction action = EnumUserAction.UPDATE;
		if (lastid == 0) {
			action = EnumUserAction.INSERT;
		}

		model.save();

		this.setVisible(false);
		if (listWindow != null) {
			listWindow.refresh();
		}

		Env.getHomePage().showNotification("Đã cập nhật thông tin!", Clients.NOTIFICATION_TYPE_INFO);

		SaveLogToQueue savelog = new SaveLogToQueue(this.getModel(), action, Env.getHomePage().getCurrentFunction(),
				Env.getUserID());
		savelog.start();
	}

	public Editor getApplyEditor(Annotation anno, Field field) {
		Editor editor = null;
		try {
			ApplyEditor applyeditor = (ApplyEditor) anno;
			String editorClassName = "com.vietek.taxioperation.ui.editor." + applyeditor.classNameEditor();
			editor = (Editor) (Class.forName(editorClassName)).newInstance();
			editor.setDataField(field.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return editor;
	}

	public AbstractWindowPanel getListWindow() {
		return listWindow;
	}

	public void setListWindow(AbstractWindowPanel listWindow) {
		this.listWindow = listWindow;
	}

	public AbstractModel getModel() {
		return model;
	}

	public void setModel(AbstractModel model) {
		this.model = model;
		Events.echoEvent("setDefaultValue", this, null);
	}

	public HashMap<String, Editor> getMapEditor() {
		return mapEditor;
	}

	public void setMapEditor(HashMap<String, Editor> mapEditor) {
		this.mapEditor = mapEditor;
	}

	public Button getBtn_save() {
		return btn_save;
	}

	public void setBtn_save(Button btn_save) {
		this.btn_save = btn_save;
	}

	public Button getBtn_cancel() {
		return btn_cancel;
	}

	public void setBtn_cancel(Button btn_cancel) {
		this.btn_cancel = btn_cancel;
	}

	public Hlayout getBottonLayout() {
		return bottonLayout;
	}

	public void setBottonLayout(Hlayout bottonLayout) {
		this.bottonLayout = bottonLayout;
	}

	/**
	 * Tuanpa
	 * 
	 * @param e
	 */
	public void onEditorChangeValue(Event e) {
		Editor editor = (Editor) e.getData();
		if (editor != null) {
			Object value = editor.getValue();
			AbstractModel.setValue(model, editor.getDataField(), value);
		}
	}
}
