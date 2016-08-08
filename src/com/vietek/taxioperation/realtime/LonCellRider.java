package com.vietek.taxioperation.realtime;

/**
 *
 * @author VuD
 */
public class LonCellRider {

	private LatCellRider[] latCell = new LatCellRider[CellMapTaxi.LAT_INDEX_RANGER];

	public LatCellRider[] getLatCell() {
		return latCell;
	}

	public void setLatCell(LatCellRider[] latCell) {
		this.latCell = latCell;
	}

}
