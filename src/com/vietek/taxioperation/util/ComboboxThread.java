package com.vietek.taxioperation.util;

import java.util.List;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.zul.Messagebox;

import com.vietek.taxioperation.common.ConstantValueSearch;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.util.DropboxAdvanceSearchItem;

public class ComboboxThread extends Thread {
	TreeMap<String, DropboxAdvanceSearchItem> result;
	List<?> dataList;
	List<?> dataListAll;

	public ComboboxThread(String dataField, String value) {

	}

	public ComboboxThread(Class<?> clazz) {
		String className = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
		String controllerClassName = clazz.getName().replace("com.vietek.taxioperation.model",
				"com.vietek.taxioperation.controller") + "Controller";
		try {
			dataList = ControllerUtils.getController(Class.forName(controllerClassName)).find("FROM " + className + " WHERE id = -1");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	public ComboboxThread(Class<?> clazz, String whereClause, int loadPages) {
		String className = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
		String controllerClassName = clazz.getName().replace("com.vietek.taxioperation.model",
				"com.vietek.taxioperation.controller") + "Controller";
		try {
			dataList = ControllerUtils.getController(Class.forName(controllerClassName)).findPermission(
					ConstantValueSearch.NUMBER_RECORD_OF_CBB * loadPages, clazz,
					"FROM " + className + " WHERE (isActive = true OR isActive = 1) ");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// Add "Load more" Item
		long totalRecord = 0;
		Session session = ControllerUtils.getCurrentSession();
		String sql = "select count(*) ";
		sql = sql + " from " + clazz.getName();
		Query query = session.createQuery(sql);
		totalRecord = ((Long) query.uniqueResult()).intValue();
		session.close();

		if (totalRecord > dataList.size()) {
			// Add "Load more"
			dataList.add(null);
		}
	}

	public ComboboxThread(Object model, String dataField, String strFind, int loadPages) {
		Class<?> clazz = AbstractModel.getDataType(model, dataField);
		// String filter = "";
		// if (clazz != null) {
		// Field[] fields = clazz.getDeclaredFields();
		// if (fields != null) {
		// for (int i = 0; i < fields.length; i++) {
		// Field field = fields[i];
		// if (field.getType() == String.class) {
		// if (filter.length() > 0)
		// filter = filter + " OR ";
		// filter = filter + field.getName() + " LIKE '%" + strFind + "%' ";
		// }
		// }
		// }
		// }
		String className = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
		String controllerClassName = clazz.getName().replace("com.vietek.taxioperation.model",
				"com.vietek.taxioperation.controller") + "Controller";
		try {
			// dataList = ControllerUtils.getController(
			// Class.forName(controllerClassName)).find(ConstantValueSearch.NUMBER_RECORD_OF_CBB
			// * loadPages,
			// "FROM " + className + " WHERE " + filter);
			dataList = ControllerUtils.getController(Class.forName(controllerClassName)).findPermission(
					ConstantValueSearch.NUMBER_RECORD_OF_CBB * loadPages, clazz,
					"FROM " + className + " where isActive = 1 ");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// Add "Load more" Item
		long totalRecord = 0;
		Session session = ControllerUtils.getCurrentSession();
		String sql = "select count(*) ";
		sql = sql + " from " + clazz.getName();
		Query query = session.createQuery(sql);
		totalRecord = ((Long) query.uniqueResult()).intValue();
		session.close();

		if (totalRecord > dataList.size()) {
			// Add "Load more"
			dataList.add(null);
		}
	}

	public ComboboxThread(String strFind, TreeMap<String, DropboxAdvanceSearchItem> data) {
		if (data != null && strFind != null) {
			result = new TreeMap<String, DropboxAdvanceSearchItem>();
			if (!strFind.isEmpty()) {
				for (String key : data.keySet()) {
					if (key.toLowerCase().contains(strFind.toLowerCase())) {
						result.put(key, data.get(key));
					}
				}
			} else {
				result = data;
			}
		}
	}

	@Override
	public void run() {
		Messagebox.show("Thread find data for combobox running..");
	}

	public TreeMap<String, DropboxAdvanceSearchItem> getResult() {
		return result;
	}

	public void setResult(TreeMap<String, DropboxAdvanceSearchItem> result) {
		this.result = result;
	}

	public List<?> getDataList() {
		return dataList;
	}

	public void setDataList(List<?> dataList) {
		this.dataList = dataList;
	}

	public List<?> getDataListAll() {
		return dataListAll;
	}

	public void setDataListAll(List<?> dataListAll) {
		this.dataListAll = dataListAll;
	}

}