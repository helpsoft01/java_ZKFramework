/**
*
* VIETEK JSC - VOpen - Vietnam open framework
* Create date: Aug 5, 2016
* Author: tuanpa
*
*/
package com.vietek.taxioperation.util.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.vietek.taxioperation.util.ConfigUtil;

import net.spy.memcached.ConnectionObserver;
import net.spy.memcached.MemcachedClient;

public class Memcached extends AbstractCache implements ConnectionObserver {
	public MemcachedClient mcc;
	public MemcachedClient getMcc() {
		if (mcc == null) {
			try {
				mcc = new MemcachedClient(new InetSocketAddress(ConfigUtil.getConfig("MEMCACHED_IP"), ConfigUtil.getConfig("MEMCACHED_PORT", 11211)));
				mcc.addObserver(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mcc;
	}
	public Memcached(String cacheName, int timeout) {
		this.setCacheName(cacheName);
		this.setTimeout(timeout);
	}

	@Override
	protected void putToCache(String key, Object object) {
		key = key.replaceAll("\\s","");
		key = getCacheName() + "|" + key;
		getMcc().add(key, getTimeout(), object);
	}

	@Override
	protected Object getInCache(String key) {
		key = key.replaceAll("\\s","");
		key = getCacheName() + "|" + key;
		return getMcc().get(key);
	}

	@Override
	public void removeInCache(String key) {
		key = key.replaceAll("\\s","");
		key = getCacheName() + "|" + key;
		getMcc().delete(key);
	}
	@Override
	public void connectionEstablished(SocketAddress sa, int reconnectCount) {
		
	}
	@Override
	public void connectionLost(SocketAddress sa) {
		mcc = null;
	}

}
