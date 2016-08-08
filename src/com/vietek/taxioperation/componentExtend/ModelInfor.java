package com.vietek.taxioperation.componentExtend;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.common.ColumnInfor;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class ModelInfor {

	private Object modelObj;
	private String nameField;
	// private String model;
	private String nameModel;
	private String fieldSearch;
	private Class<?> clazz;
	private Field[] fields;
	List<?> cache;
	private boolean isMany2Many;

	public ModelInfor(Object model, String fieldName) {

		this(model, fieldName, false);
	}

	public ModelInfor(Object model, String nameField, boolean isMany2Many) {

		try {

			this.modelObj = model;
			this.nameField = nameField;
			this.isMany2Many = isMany2Many;
			this.clazz = AbstractModel.getGenericType(model, nameField);

			String result = this.clazz.toString();
			this.nameModel = result.substring(result.lastIndexOf(".") + 1).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getNameModel() {
		return nameModel;
	}

	public void setModel(String model) {

		try {

			StringBuilder result = new StringBuilder();
			result.append(String.valueOf(Character.toUpperCase(model.charAt(0))));
			result.append(model.substring(1));

			this.nameModel = result.toString();
			// this.model = "com.vietek.taxioperation.model." + result;

		} catch (Exception e) {
			e.printStackTrace();
		}
		cache = new ArrayList<>();
	}

	public Class<?> getClazz() {
		if (clazz == null) {
			this.clazz = modelObj.getClass();// Class.forName(model);

		}
		return clazz;
	}

	public String getFieldSearch() {
		return fieldSearch;
	}

	public void setFieldSearch(String fieldShow) {
		this.fieldSearch = fieldShow;
	}

	public boolean isMany2Many() {
		return isMany2Many;
	}

	public void setMany2Many(boolean isMany2Many) {
		this.isMany2Many = isMany2Many;
	}

	public void setCls(Class<?> cls) {
		this.clazz = cls;
	}

	public Field[] getFields() {
		if (fields == null) {
			this.fields = clazz.getDeclaredFields();
		}
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public void changeValueModel(Object obj) {
		AbstractModel.setValue(modelObj, nameField, obj);
	}

	public Annotation[] getAnnotations(Field field) {
		Annotation[] annotations = field.getDeclaredAnnotations();
		return annotations;
	}

	int totalSize = -1;
	int maxResults = 10;

	public List<Object> getSource(int size, String where) {

		boolean isNext = isNext(where);
		Session session = ControllerUtils.getCurrentSession();
		List<Object> result = new ArrayList<>();

		try {

			if (!session.isOpen() || !session.isConnected())
				return result;

			Query query;

			StringBuilder str = new StringBuilder("from %s where 1=1 %s");
			StringBuilder strWhere = new StringBuilder();
			if (where != "") {

				strWhere.append(" and ");
				strWhere.append(getStringWhere(where));
			}

			String sql = String.format(str.toString(), getNameModel(), strWhere.toString());
			query = session.createQuery(sql);
			query.setCacheable(true);
			query.setFirstResult(0);
			query.setMaxResults(size);

			result = query.list();
			cache = result;

			if (isNext) {
				result.add(newInstance());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	public Object newInstance() {
		try {
			return getClazz().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String getStringWhere(String value) {

		StringBuilder retVal = new StringBuilder("");

		if (getFieldSearch() != null && getFieldSearch() != "") {
			retVal.append(getFieldSearch());
			retVal.append(" like '%");
			retVal.append(value.trim());
			retVal.append("%'");
		} else {
			Field[] fields = clazz.getDeclaredFields();
			boolean isFirst = true;
			for (Field field : fields) {

				if (field.getType().equals(String.class)) {
					if (!isFirst)
						retVal.append(" or ");
					retVal.append(field.getName());
					retVal.append(" like '%");
					retVal.append(value.trim());
					retVal.append("%'");
					isFirst = false;
				}
			}
		}
		return retVal.toString();
	}

	public boolean isNext(String where) {
		if (totalSize > -1) {
			return (totalSize > maxResults ? true : false);
		}
		Session session = ControllerUtils.getCurrentSession();
		try {

			if (!session.isOpen() || !session.isConnected())
				return true;

			Query query;

			String sql = String.format("select count(*) from %s where 1=1", getNameModel());
			StringBuilder strWhere = new StringBuilder();
			if (where != "") {

				strWhere.append(" and ");
				strWhere.append(getStringWhere(where));
			}

			query = session.createQuery(sql);
			totalSize = ((Number) query.uniqueResult()).intValue();
		} catch (Exception ex) {

		} finally {
			session.close();
		}
		return (totalSize > maxResults ? true : false);
	}

	public HtmlBasedComponent getComponentByGridColumn(GridColumn gridCol) {

		/*
		 * 
		 */
		Field field = gridCol.getField();
		FixedCombobox fieldColumn = field.getAnnotation(FixedCombobox.class);
		if (fieldColumn != null)
			return null;

		/*
		 * 
		 */
		HtmlBasedComponent retVal = null;

		if (gridCol.getClazz().equals(Boolean.class) || gridCol.getClazz().equals(boolean.class)) {

			retVal = new Checkbox();

		} else if (gridCol.getClazz().equals(String.class)) {

			retVal = new Textbox();
			((Textbox) retVal).setPlaceholder("find...");

		} else if (gridCol.getClazz().equals(Integer.class) || gridCol.getClazz().equals(int.class)
				|| gridCol.getClazz().equals(Long.class) || gridCol.getClazz().equals(long.class)
				|| gridCol.getClazz().equals(Double.class) || gridCol.getClazz().equals(double.class)
				|| gridCol.getClazz().equals(BigDecimal.class) || gridCol.getClazz().equals(Float.class)
				|| gridCol.getClazz().equals(float.class)) {

			retVal = new Decimalbox();
			((Decimalbox) retVal).setFormat("#,##0");

		} else if (gridCol.getClazz().equals(Timestamp.class)) {

			retVal = new Datebox();
			((Datebox) retVal).setButtonVisible(true);
			((Datebox) retVal).setFormat("M-d-yy KK:mm:ss a");
			((Datebox) retVal).setDisplayedTimeZones("GMT+12,GMT+8");
			((Datebox) retVal).setTimeZone("GMT+8");
			((Datebox) retVal).setTimeZonesReadonly(true);
			((Datebox) retVal).setWeekOfYear(true);
			// ((Datebox) compn).setLocale(Locales.getCurrent());// "vn"

		}

		return retVal;
	}

	public GridColumn getGridColumn(Field field) {

		String header = "";
		String width = "0";
		Class<?> clazz = null;
		String getDataMethod = "";
		String fieldName = "";
		GridColumn retVal = null;
		try {
			fieldName = field.getName();
			if (!fieldName.startsWith("serialVersionUID") && !fieldName.startsWith("id")) {
				ColumnInfor fieldColumn = field.getAnnotation(ColumnInfor.class);
				if (fieldColumn != null) {
					header = (String) fieldColumn.label();
					width = (String) fieldColumn.width();
				} else {
					header = fieldName;
				}
				getDataMethod = getMethodByField(fieldName);
				clazz = field.getType();
				retVal = new GridColumn(header, width, clazz, getDataMethod, fieldName, getClazz());
				retVal.setField(field);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}

	private String getMethodByField(String name) {

		StringBuilder result = new StringBuilder("get");
		result.append(String.valueOf(Character.toUpperCase(name.charAt(0))));
		result.append(name.substring(1));

		return result.toString();
	}

	private void getFilds(String model) throws ClassNotFoundException {

		this.clazz = getClazz();// Class.forName(model);
		this.fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			// Messagebox.show(aFieldlist.getName());
			// Class type = field.getType();
			// String name = field.getName();
			Annotation[] annotations = field.getDeclaredAnnotations();
		}

	}

	private Div createItem(Class type, String name, Annotation[] annotations) {

		Div result = new Div();

		Label lb = new Label();
		lb.setParent(result);
		lb.setValue("-Type:" + type.getName());

		lb = new Label();
		lb.setParent(result);
		lb.setValue("-Name:" + type.getName());
		for (Annotation annotation : annotations) {
			String name1 = annotation.annotationType().getName();
			if (name1.endsWith("ManyToOne") || name1.endsWith("ManyToMany") || name1.endsWith("OneToMany")) {
				lb = new Label();
				lb.setParent(result);
				lb.setValue("+Annotation:" + name1);
			}
		}
		return result;
	}

	public List<?> getCache() {
		return cache;
	}

	public void setCache(List<?> cache) {
		this.cache = cache;
	}
}
