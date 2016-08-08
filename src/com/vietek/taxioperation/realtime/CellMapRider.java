package com.vietek.taxioperation.realtime;

import java.util.LinkedList;
import java.util.List;

import com.vietek.taxioperation.common.AppLogger;

/**
 *
 * @author VuD
 */
public class CellMapRider {
	private static LonCellRider[] mapCellRider;

	static {
		mapCellRider = new LonCellRider[CellMapTaxi.LON_INDEX_RANGER];
		for (int i = 0; i < CellMapTaxi.LON_INDEX_RANGER; i++) {
			LonCellRider lonCell = new LonCellRider();
			LatCellRider[] latCellArr = new LatCellRider[CellMapTaxi.LAT_INDEX_RANGER];
			for (int j = 0; j < CellMapTaxi.LAT_INDEX_RANGER; j++) {
				LatCellRider latCell = new LatCellRider();
				latCellArr[j] = latCell;
			}
			lonCell.setLatCell(latCellArr);
			mapCellRider[i] = lonCell;
		}
	}
	
	public static List<Rider> getListVehicleNearest(int lonIndex, int latIndex) {
		if (lonIndex >= CellMapTaxi.LON_INDEX_RANGER || lonIndex < 0 || latIndex >= CellMapTaxi.LAT_INDEX_RANGER || latIndex < 0) {
			return new LinkedList<>();
		}
		return mapCellRider[lonIndex].getLatCell()[latIndex].getListRider();
	}

	public static List<Rider> getListVehicleNearest(double longitude, double latitude) {
		int lonIndex = getLonIndex(longitude);
		int latIndex = getLatIndex(latitude);
		return mapCellRider[lonIndex].getLatCell()[latIndex].getListRider();
	}

	public static boolean removeMapCell(Rider msg) {
		if (msg == null) {
			return false;
		}
		if (msg.getLongtitute() == 0d || msg.getLattitute() == 0d) {
			return false;
		}
		boolean result = false;
		try {
			int lonIndex = getLonIndex(msg.getLongtitute());
			int latIndex = getLatIndex(msg.getLattitute());
			if (lonIndex >= CellMapTaxi.LON_INDEX_RANGER || lonIndex < 0 || latIndex >= CellMapTaxi.LAT_INDEX_RANGER || latIndex < 0) {
				result = false;
			} else {
				LonCellRider lonCell = mapCellRider[lonIndex];
				LatCellRider[] latCellArr = lonCell.getLatCell();
				LatCellRider latCell = latCellArr[latIndex];
				result = latCell.getlstSyn().remove(msg);
			}
		} catch (Exception e) {
			AppLogger.logUserAction.error("RemoveMapCell|" + msg.getRiderId() + "|" + msg.getLongtitute() + "|" + msg.getLattitute(),
					e);
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public static boolean addMapCell(Rider msg) {
		if (msg == null) {
			return false;
		}
		if (msg.getLongtitute() == 0f || msg.getLattitute() == 0f) {
			return false;
		}
		try {
			int lonIndex = getLonIndex(msg.getLongtitute());
			int latIndex = getLatIndex(msg.getLattitute());
			if (lonIndex >= CellMapTaxi.LON_INDEX_RANGER || lonIndex < 0 || latIndex >= CellMapTaxi.LAT_INDEX_RANGER || latIndex < 0) {
				return false;
			} else {
				LonCellRider lonCell = mapCellRider[lonIndex];
				LatCellRider[] latCellArr = lonCell.getLatCell();
				LatCellRider latCell = latCellArr[latIndex];
				boolean isAdd = latCell.getlstSyn().add(msg);
				return isAdd;
			}
		} catch (Exception e) {
			AppLogger.logUserAction.error("AddMapCell|" + msg.getRiderId() + "|" + msg.getLongtitute() + "|" + msg.getLattitute(),
					e);
			e.printStackTrace();
			return false;
		}
	}
	

	public static int getLonIndex(double longitude) {
		int index = 0;
		index = (int) ((longitude - CellMapTaxi.W_LONGITUDE) / CellMapTaxi.DELTA_DEGREE);
		return index;
	}

	public static int getLatIndex(double latitude) {
		int index = 0;
		index = (int) ((latitude - CellMapTaxi.S_LATITUDE) / CellMapTaxi.DELTA_DEGREE);
		return index;
	}
}
