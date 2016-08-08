package com.vietek.taxioperation.realtime;

/**
 *
 * @author VuD
 */
public class LonCell {
	private LatCell[] latCell = new LatCell[CellMapTaxi.LAT_INDEX_RANGER];

	public LatCell[] getLatCell() {
		return latCell;
	}

	public void setLatCell(LatCell[] latCell) {
		this.latCell = latCell;
	}

}
