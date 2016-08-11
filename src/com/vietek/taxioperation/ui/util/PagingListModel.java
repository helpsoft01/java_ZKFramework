package com.vietek.taxioperation.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.zul.AbstractListModel;

import com.vietek.taxioperation.controller.BasicController;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.cache.Memcached;

public class PagingListModel<T> extends AbstractListModel<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5116479980937459666L;
	private int startIndex = 0;
	private int pageSize = 10;
	List<T> cache = null;
	private String model;
	BasicController<T> controller;
	private String queryStr;
	private String orderBy = "";
	private List<Object> lstParam = new ArrayList<>();
	private String[] paramNames;

	public PagingListModel(String model) {
		super();
		this.model = model;
	}

	public PagingListModel(String model, String query, String orderBy) {
		super();
		this.model = model;
		this.queryStr = query;
		this.orderBy = orderBy;
		if (this.orderBy == null) {
			this.orderBy = "";
		}
	}

	@Override
	public T getElementAt(int index) {
		if (cache == null || index < startIndex || index >= startIndex + pageSize)
			loadData(index);
		int indexTmp = index - startIndex;
		if (indexTmp < 0) {
			indexTmp = 0;
		}
		return (T) cache.get(indexTmp);
	}

	@SuppressWarnings("unchecked")
	private void loadData(int index) {
		Session session = ControllerUtils.getCurrentSession();
		try {
			if (!session.isOpen() || !session.isConnected())
				return;
			
			Memcached cacheByModel = (Memcached) Memcached.getModelCaches().get(model);

			startIndex = (index / pageSize) * pageSize;
			Query query;
			String qString;
			if (queryStr == null || queryStr.isEmpty()) {
				qString = "from " + model;
				// query = session.createQuery("from " + model + orderBy);
			} else {
				qString = createQueryPermission(queryStr, model);
				query = session.createQuery(qString);
			}
			if (cacheByModel != null) {
				qString = "select id " + qString;
			}
			qString = createQueryPermission(qString, model);
			query = session.createQuery(qString);
			for (int i = 0; i < lstParam.size(); i++) {
				Object tmp = lstParam.get(i);
				String paramName = paramNames[i];
				if (tmp instanceof Collection) {
					query.setParameterList(paramName, (Collection<?>) tmp);
				} else if (tmp instanceof Object[]) {
					query.setParameterList(paramName, (Object[]) tmp);
				} else {
					query.setParameter(paramName, tmp);
				}
			}

			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			query.setCacheable(true);

			
			if (cacheByModel == null) {
				cache = query.list();
			}
			else {
				List<Object> retList = query.list();
				cache = new ArrayList<>();
				for (Object obj : retList) {
					int id = (int)obj;
					T item = (T) cacheByModel.get(id + "");
					if (item == null) {
						String hql = "from " + model + " where id = " + id;
						query = session.createQuery(hql);
						List<Object> tmp = query.list();
						if (tmp.size() > 0) {
							item = (T) tmp.get(0);
						}
						cacheByModel.put(id + "", item);
					}
					cache.add(item);
				}
			}

		} catch (Exception ex) {

		} finally {
			session.close();
		}
	}

	int totalSize = -1;

	/*
	 * 20163003: dungnd - bo sung dieu kien de chon cau query day du
	 * (select...from...where...)
	 */
	@Override
	public int getSize() {
		if (totalSize > -1)
			return totalSize;
		Session session = ControllerUtils.getCurrentSession();

		try {
			if (!session.isOpen() || !session.isConnected())
				return totalSize;

			String sql = "select count(*) ";
			if (queryStr == null)
				sql = sql + " from " + model;
			else {
				if (queryStr.toLowerCase().indexOf("select") > -1) {
					sql = sql + queryStr.substring(queryStr.toLowerCase().indexOf("from"));
				} else {
					sql = sql + queryStr;
				}
			}
			sql = createQueryPermission(sql, model);
			Query query = session.createQuery(sql);
			for (int i = 0; i < lstParam.size(); i++) {
				Object tmp = lstParam.get(i);
				String paramName = paramNames[i];
				if (tmp instanceof Collection) {
					query.setParameterList(paramName, (Collection<?>) tmp);
				} else if (tmp instanceof Object[]) {
					query.setParameterList(paramName, (Object[]) tmp);
				} else {
					query.setParameter(paramName, tmp);
				}
				// query.setParameter(i + 1, tmp);
			}
			totalSize = ((Long) query.uniqueResult()).intValue();

			return totalSize;
		} catch (Exception ex) {
			return 0;
		} finally {
			session.close();
		}
	}

	public List<T> getCache() {
		return cache;
	}

	public void setCache(List<T> cache) {
		this.cache = cache;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String createQueryPermission(String qString, String modelName) {
		lstParam.clear();
		String newStr;
		modelName = modelName.replace("com.vietek.taxioperation.model.", "");
		String rule = Env.getMapUserRule().get(modelName);
		if (rule != null) {
			if (!qString.toUpperCase().contains("WHERE")) {
				newStr = qString + " Where " + rule;
			} else {
				newStr = qString + " And " + rule;
			}
			while (newStr.indexOf("$") > 0) {
				String param = newStr.substring(newStr.indexOf("$"), newStr.indexOf("$", newStr.indexOf("$") + 1) + 1);
				newStr = newStr.replace(param, " :? ");
				lstParam.add(Env.getContext(param));
			}
			newStr += " ";
			StringBuilder sb = new StringBuilder();
			String[] strSplit = newStr.split("\\?");
			int end = 0;
			paramNames = new String[lstParam.size()];
			for (int i = 0; i < lstParam.size(); i++) {
				String param = "p" + i;
				paramNames[i] = param;
				sb.append(strSplit[i]).append(param);
				end = i;
			}
			sb.append(strSplit[end + 1]);
			newStr = sb.toString();
		} else {
			newStr = qString;
		}
		return newStr;
	}

	@Override
	protected void fireEvent(int arg0, int arg1, int arg2) {

		if (arg1 == -1 && arg2 == -1)
			return;
		super.fireEvent(arg0, arg1, arg2);

	}
}
