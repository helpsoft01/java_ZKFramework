package com.vietek.taxioperation.realtime;

import java.util.LinkedList;
import java.util.List;

import com.vietek.taxioperation.common.LinkedListSyn;

/**
 *
 * @author VuD
 */
public class LatCell {
	private final LinkedListSyn<Taxi> lstSyn;

	public LatCell() {
		super();
		this.lstSyn = new LinkedListSyn<>();
	}

	public LinkedListSyn<Taxi> getlstSyn() {
		return lstSyn;
	}

	public List<Taxi> getListTaxi() {
		LinkedList<Taxi> lstResult = lstSyn.clone();
		if (lstResult == null) {
			lstResult = new LinkedList<>();
		}
		return lstResult;
	}

}
