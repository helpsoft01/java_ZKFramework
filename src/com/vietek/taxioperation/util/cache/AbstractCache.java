/**
*
* VIETEK JSC - VOpen - Vietnam open framework
* Create date: Aug 5, 2016
* Author: tuanpa
*
*/
package com.vietek.taxioperation.util.cache;

import java.io.Serializable;
import java.sql.Timestamp;

public abstract class AbstractCache {
	/**
	 * Time out: minute
	 */
	private int timeout;
	/**
	 * Cache name
	 */
	private String cacheName;
	abstract protected void putToCache(String key, Object object);
	abstract protected Object getInCache(String key);
	abstract public void removeInCache(String key);
	
	public void put(String key, Object object) {
		CacheItem item = new CacheItem();
		item.time = new Timestamp(System.currentTimeMillis());
		item.value = object;
		putToCache(key, item);
	}
	
	public Object get(String key) {
		CacheItem item = (CacheItem) getInCache(key);
		if (item == null)
			 return null;
		Object retval = item.value;
		long time = System.currentTimeMillis() - item.time.getTime();
		if (timeout > 0 && ((time / (60 * 1000)) > timeout)) {
			retval = null;
			removeInCache(key);
		}
		return retval;
	}
	
	public void remove(String key) {
		removeInCache(key);
	}

	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public String getCacheName() {
		return cacheName;
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
}

class CacheItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7705689756875428367L;
	Timestamp time;
	Object value;
}
