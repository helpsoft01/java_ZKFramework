package com.vietek.taxioperation.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.util.DropboxAdvanceSearchItem;

public class ChosenBoxWorker {
	TreeMap<String, DropboxAdvanceSearchItem> result;
	List<?> dataList;
	List<?> dataListAll;
	public static int MAX_LOAD_ITEM = 100000;
	public ChosenBoxWorker(String dataField, String value) {

	}
	
	public ChosenBoxWorker(Object model, String dataField, String strFind, int loadPages){
		Class<?> clazz = AbstractModel.getGenericType(model, dataField);
		String filter = "";
		if (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					if (field.getType() == String.class) {
						if (filter.length() > 0)
							filter = filter + " OR ";
						filter = filter + field.getName() + " LIKE '%" + strFind + "%' ";
					}
				}
			}
		}
		String className = clazz.getName().substring(
				clazz.getName().lastIndexOf(".") + 1);
		String controllerClassName = clazz.getName().replace(
				"com.vietek.taxioperation.model",
				"com.vietek.taxioperation.controller")
				+ "Controller";
		try {
			dataList = ControllerUtils.getController(
					Class.forName(controllerClassName)).find(MAX_LOAD_ITEM * loadPages,
					"FROM " + className + " WHERE " + filter);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//Add "Load more" Item
		long totalRecord = 0;
		Session session = ControllerUtils.getCurrentSession();
		String sql = "select count(*) ";
		sql = sql + " from " + clazz.getName();
		Query query = session.createQuery(sql);
		totalRecord = ((Long) query.uniqueResult()).intValue();
		session.close();
		
		if (totalRecord > dataList.size()) {
			//Add "Load more"
			dataList.add(null);
		}
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