package com.vietek.taxioperation.componentExtend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zul.AbstractListModel;

public abstract class ModelDBList<T> extends AbstractListModel<T> {

	protected Integer totalSize = null;
	private Map<Integer, List<T>> cache;
	private List<T> listItem;

	public ModelDBList() {
		super();
		cache = new HashMap<Integer, List<T>>();
		listItem = new ArrayList<>();

		setPageSize(10);
	}

	public ModelDBList(int chunkSize) {
		super();
		cache = new HashMap<Integer, List<T>>();
		listItem = new ArrayList<>();

		setPageSize(chunkSize);
	}

	@Override
	public T getElementAt(int index) {
		int _pageSize = getPageSize();
		if (_pageSize < 0)
			return null;
		if (_pageSize == -1) {
			_pageSize = 10;
			setPageSize(10);
		}
		int indexStart = index / _pageSize;
		List<T> elements = cache.get(indexStart);
		if (elements == null) {
			elements = loadFromDB(indexStart * _pageSize, _pageSize);
			cache.put(indexStart, elements);
		}

		return elements.get(index % _pageSize);

	}

	@Override
	public int getSize() {

		if (totalSize == null) {
			totalSize = loadTotalSize();
		}
		return totalSize;
	}

	public void clearCaches() {
		totalSize = null;
		cache.clear();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clearCaches();
	}

	protected abstract int loadTotalSize();

	protected abstract List<T> loadFromDB(int startIndex, int size);

	@Override
	public void setPageSize(int arg0) {
		super.setPageSize(arg0);
		clearCaches();
	}

	@Override
	protected void fireSelectionEvent(T e) {
		super.fireSelectionEvent(e);
	}

	@Override
	protected void fireEvent(int arg0, int arg1, int arg2) {
		super.fireEvent(arg0, arg1, arg2);
		if (arg1 == -1 && arg2 == -1)
			return;
		// loadFromDB(arg1, arg2);
		// totalSize = loadTotalSize();
	}

	public List<T> getListItem() {

		listItem = new ArrayList<T>((Collection<? extends T>) cache.values());
		return listItem;
	}

	public void setListItem(List<T> listItem) {
		this.listItem = listItem;
	}

	public Map<Integer, List<T>> getCache() {
		return cache;
	}

	public void setCache(Map<Integer, List<T>> cache) {
		this.cache = cache;
	}

	public void addRecord(Object record) {

		// for(Entry<Integer, List<T>> entry : cache.entrySet()) {
		// Integer key = entry.getKey();
		// List<T> value = entry.getValue();
		// }

		Iterator<Map.Entry<Integer, List<T>>> iterator = cache.entrySet().iterator();

		Map.Entry<Integer, List<T>> result = null;
		while (iterator.hasNext()) {
			result = iterator.next();
		}
		if (result != null) {
			int key = result.getKey();
			List<T> lst = result.getValue();
			if (lst.size() < getPageSize()) {
				lst.add((T) record);
			} else {
				List<T> newLst = new ArrayList<>();
				newLst.add((T) record);
				cache.put(key + 1, newLst);
			}
		} else {
			List<T> newLst = new ArrayList<>();
			newLst.add((T) record);
			cache.put(1, newLst);
		}
		++totalSize;
	}

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}
}
