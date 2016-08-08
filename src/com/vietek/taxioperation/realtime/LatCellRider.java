package com.vietek.taxioperation.realtime;

import java.util.LinkedList;
import java.util.List;

import com.vietek.taxioperation.common.LinkedListSyn;

/**
 *
 * @author VuD
 */
public class LatCellRider {
	private final LinkedListSyn<Rider> lstSyn;

	public LatCellRider() {
		this.lstSyn = new LinkedListSyn<>();
	}

	public LinkedListSyn<Rider> getlstSyn() {
		return lstSyn;
	}

	public List<Rider> getListRider() {
		LinkedList<Rider> lstResult = lstSyn.clone();
		if (lstResult == null) {
			lstResult = new LinkedList<>();
		}
		return lstResult;
	}
}
