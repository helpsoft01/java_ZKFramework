package com.vietek.taxioperation.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.util.Env;

@Repository
@Transactional
public class BasicController<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2283668337228299687L;
	@Autowired
	HibernateTemplate hibernateTemplate;

	public BasicController() {

	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	// public void initialize(final Object proxy) throws DataAccessException {
	// hibernateTemplate.initialize(proxy);
	// }

	// public T merge(T entity) throws DataAccessException {
	// return (T) hibernateTemplate.merge(entity);
	// }

	// public void persist(T entity) throws DataAccessException {
	// hibernateTemplate.persist(entity);
	// }

	public void refresh(T entity) throws DataAccessException {
		hibernateTemplate.refresh(entity);
	}

	public void refresh(AbstractModel entity) throws DataAccessException {
		try {
			hibernateTemplate.refresh(entity);
		} catch (Exception e) {
			// ignore
		}
	}

	public void save(T entity) throws DataAccessException {
		hibernateTemplate.save(entity);
	}

	public void saveOrUpdate(T entity) throws DataAccessException {
		hibernateTemplate.saveOrUpdate(entity);
	}

	public void update(T entity) throws DataAccessException {
		hibernateTemplate.update(entity);
	}

	public void merge(T entity) throws DataAccessException {
		hibernateTemplate.merge(entity);
	}

	public void delete(T entity) throws DataAccessException {
		hibernateTemplate.delete(entity);
	}

	protected void deleteAll(Collection<T> entities) throws DataAccessException {
		hibernateTemplate.deleteAll(entities);
	}

	public T get(Class<T> entityClass, Serializable id) throws DataAccessException {
		return (T) hibernateTemplate.get(entityClass, id);
	}

	public List<T> loadAll(Class<T> entityClass) throws DataAccessException {
		return hibernateTemplate.loadAll(entityClass);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(String queryString, Object... values) {
		hibernateTemplate.setMaxResults(-1);
		return (List<T>) hibernateTemplate.find(queryString, values);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(int maxResult, String queryString, Object... values) {
		hibernateTemplate.setMaxResults(maxResult);
		return (List<T>) hibernateTemplate.find(queryString, values);
	}

	// DUNGNM_START_END_11082015
	@SuppressWarnings("unchecked")
	public List<T> findLimit(final DetachedCriteria criteria, final int firstResult, final int maxResults) {
		return (List<T>) hibernateTemplate.findByCriteria(criteria, firstResult, maxResults);
	}

	@SuppressWarnings("unchecked")
	public T saveOrUpdateEntity(AbstractModel entity) {
		return (T) hibernateTemplate.save(entity);
	}

	public void saveOrUpdate(AbstractModel entity) {		
		hibernateTemplate.saveOrUpdate(entity);
	}

	public void merge(AbstractModel entity) {
		hibernateTemplate.merge(entity);
	}
	/*
	 *  dungnd: bulk insert
	 */
	@SuppressWarnings("unchecked")
	public void bulkInsert(List<?> lst){
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			for (Iterator<T> iterator = (Iterator<T>) lst.iterator(); iterator.hasNext();) {
				T entity = iterator.next();
				session.save(entity);
			}
			session.flush();
	        session.clear();
			tx.commit();			
		} catch (Exception e) {
			// TODO: handle exception
			AppLogger.logDebug.error(e.getMessage());
		}finally {	
			session.close();
		}
	}

	/*
	 * dungnd: bulk update
	 */
	@SuppressWarnings("unchecked")
	public void bulkUpdate(List<?> lst) {
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		try {			
			for (Iterator<T> iterator = (Iterator<T>) lst.iterator(); iterator.hasNext();) {
				T entity = iterator.next();
				session.update(entity);
			}			
			session.flush();
		    session.clear();
			tx.commit();
			session.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			session.close();
		}
	}
	
	/*
	 * public void updateStatus(AbstractModel entity) { try {
	 * AbstractModel.setValue(entity, "isActive", false); } catch (Exception e)
	 * { e.printStackTrace(); }
	 * 
	 * hibernateTemplate.saveOrUpdate(entity); }
	 */
	public void delete(AbstractModel entity) {
		hibernateTemplate.delete(entity);
	}
	
	/**
	 * @author Batt
	 * @param Customer
	 *            entity Update status Customer = 1 ( Use for delete function)
	 *            status : 1 : inActive status : 0 : Active
	 */
	public void updateStatusCustomer(Customer entity) {
		entity.setStatus(1);
		hibernateTemplate.saveOrUpdate(entity);
	}

	public List<T> findPermissionLimit(int maxResults, Class<?> modelClass, String queryString, Object... values) {
		String modelName = modelClass.getSimpleName();
		String rule = Env.getMapUserRule().get(modelName);
		if (rule != null) {
			List<Object> lstParam = new ArrayList<>();
			String newStr;
			if (!queryString.toUpperCase().contains("WHERE")) {
				newStr = queryString + " Where " + rule;
				while (newStr.indexOf("$") > 0) {
					String param = newStr.substring(newStr.indexOf("$"),
							newStr.indexOf("$", newStr.indexOf("$") + 1) + 1);
					newStr = newStr.replace(param, " :? ");
					lstParam.add(Env.getContext(param));
				}
			} else {
				newStr = queryString + " And " + rule;
				while (newStr.indexOf("$") > 0) {
					String param = newStr.substring(newStr.indexOf("$"),
							newStr.indexOf("$", newStr.indexOf("$") + 1) + 1);
					newStr = newStr.replace(param, " :? ");
					lstParam.add(Env.getContext(param));
				}
			}
			newStr += " ";
			StringBuilder sb = new StringBuilder();
			String[] strSplit = newStr.split("\\?");
			Object[] newParam = new Object[values.length + lstParam.size()];
			System.arraycopy(values, 0, newParam, 0, values.length);
			System.arraycopy(lstParam.toArray(), 0, newParam, values.length, lstParam.size());
			String[] paramNames = new String[newParam.length];
			int end = 0;
			for (int i = 0; i < newParam.length; i++) {
				String param = "p" + i;
				paramNames[i] = param;
				sb.append(strSplit[i]).append(param);
				end = i;
			}
			// if (strSplit.length == end + 1) {
			sb.append(strSplit[end + 1]);
			// }
			// return find(newStr, newParam);
			return findAsd(maxResults, sb.toString(), paramNames, newParam);
		} else {
			return find(maxResults, queryString, values);
		}
	}

	/**
	 * @author VuD
	 * @param modelClass
	 * @param queryString
	 * @param values
	 * @return
	 */
	public List<T> findPermission(Class<?> modelClass, String queryString, Object... values) {
		String modelName = modelClass.getSimpleName();
		String rule = Env.getMapUserRule().get(modelName);
		if (rule != null) {
			List<Object> lstParam = new ArrayList<>();
			String newStr;
			if (!queryString.toUpperCase().contains("WHERE")) {
				newStr = queryString + " Where " + rule;
				while (newStr.indexOf("$") > 0) {
					String param = newStr.substring(newStr.indexOf("$"),
							newStr.indexOf("$", newStr.indexOf("$") + 1) + 1);
					newStr = newStr.replace(param, " :? ");
					lstParam.add(Env.getContext(param));
				}
			} else {
				newStr = queryString + " And " + rule;
				while (newStr.indexOf("$") > 0) {
					String param = newStr.substring(newStr.indexOf("$"),
							newStr.indexOf("$", newStr.indexOf("$") + 1) + 1);
					newStr = newStr.replace(param, " :? ");
					lstParam.add(Env.getContext(param));
				}
			}
			newStr += " ";
			StringBuilder sb = new StringBuilder();
			String[] strSplit = newStr.split("\\?");
			Object[] newParam = new Object[values.length + lstParam.size()];
			System.arraycopy(values, 0, newParam, 0, values.length);
			System.arraycopy(lstParam.toArray(), 0, newParam, values.length, lstParam.size());
			String[] paramNames = new String[newParam.length];
			int end = 0;
			for (int i = 0; i < newParam.length; i++) {
				String param = "p" + i;
				paramNames[i] = param;
				sb.append(strSplit[i]).append(param);
				end = i;
			}
			// if (strSplit.length == end + 1) {
			sb.append(strSplit[end + 1]);
			// }
			// return find(newStr, newParam);
			return findAsd(sb.toString(), paramNames, newParam);
		} else {
			return find(queryString, values);
		}
	}

	public List<T> findPermission(int maxResult, Class<?> modelClass, String queryString, Object... values) {
		String modelName = modelClass.getSimpleName();
		String rule = Env.getMapUserRule().get(modelName);
		if (rule != null) {
			List<Object> lstParam = new ArrayList<>();
			String newStr;
			if (!queryString.toUpperCase().contains("WHERE")) {
				newStr = queryString + " Where " + rule;
				while (newStr.indexOf("$") > 0) {
					String param = newStr.substring(newStr.indexOf("$"),
							newStr.indexOf("$", newStr.indexOf("$") + 1) + 1);
					newStr = newStr.replace(param, " :? ");
					lstParam.add(Env.getContext(param));
				}
			} else {
				newStr = queryString + " And " + rule;
				while (newStr.indexOf("$") > 0) {
					String param = newStr.substring(newStr.indexOf("$"),
							newStr.indexOf("$", newStr.indexOf("$") + 1) + 1);
					newStr = newStr.replace(param, " :? ");
					lstParam.add(Env.getContext(param));
				}
			}
			newStr += " ";
			StringBuilder sb = new StringBuilder();
			String[] strSplit = newStr.split("\\?");
			Object[] newParam = new Object[values.length + lstParam.size()];
			System.arraycopy(values, 0, newParam, 0, values.length);
			System.arraycopy(lstParam.toArray(), 0, newParam, values.length, lstParam.size());
			String[] paramNames = new String[newParam.length];
			int end = 0;
			for (int i = 0; i < newParam.length; i++) {
				String param = "p" + i;
				paramNames[i] = param;
				sb.append(strSplit[i]).append(param);
				end = i;
			}
			
			sb.append(strSplit[end + 1]);
			return findAsd(sb.toString(), paramNames, newParam);
		} else {
			return find(queryString, values);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findAsd(String queryString, String[] paramNames, Object[] values) {
		return (List<T>) hibernateTemplate.findByNamedParam(queryString, paramNames, values);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAsd(int maxResults, String queryString, String[] paramNames, Object[] values) {
		hibernateTemplate.setMaxResults(maxResults);
		return (List<T>) hibernateTemplate.findByNamedParam(queryString, paramNames, values);
	}
}
