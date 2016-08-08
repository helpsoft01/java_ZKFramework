package com.vietek.taxioperation.realtime;

import java.util.LinkedList;
import java.util.List;

import com.vietek.taxioperation.common.AppLogger;

/**
 *
 * @author VuD
 */
public class CellMapTaxi {
	/**
	 * Vi do cuc bac cua viet nam
	 */
	public static final double N_LATITUDE = 23.372514d;

	/**
	 * Vi do cuc nam cuar viet nam
	 */
	public static final double S_LATITUDE = 8.526701d;

	/**
	 * Kinh do cuc tay cua viet nam
	 */
	public static final double W_LONGITUDE = 102.117919d;

	/**
	 * Kinh do cuc dong cua viet nam
	 */
	public static final double E_LONGITUDE = 109.46228d;

	/**
	 * Do dai cua moi cell
	 */
	public static final double RANGER_CELL = 500d;

	/**
	 * Do lech do cua moi cell. delta_degree = 360 / ((2 * R * PI) /
	 * Ranger_cell)
	 */
	public static final double DELTA_DEGREE = 0.0045;

	/**
	 * Lon_Index_Ranger = (e_longtitude - w_longitude) / delta_degree = 1650
	 */
	public static final int LON_INDEX_RANGER = 1650;

	/**
	 * Lat_Index_Ranger = (n_latitude - s_latitude) / delta_degree = 3300
	 */
	public static final int LAT_INDEX_RANGER = 3300;

	private static LonCell[] mapCell;

	static {
		mapCell = new LonCell[CellMapTaxi.LON_INDEX_RANGER];
		for (int i = 0; i < CellMapTaxi.LON_INDEX_RANGER; i++) {
			LonCell lonCell = new LonCell();
			LatCell[] latCellArr = new LatCell[CellMapTaxi.LAT_INDEX_RANGER];
			for (int j = 0; j < CellMapTaxi.LAT_INDEX_RANGER; j++) {
				LatCell latCell = new LatCell();
				latCellArr[j] = latCell;
			}
			lonCell.setLatCell(latCellArr);
			mapCell[i] = lonCell;
		}
	}

	public static List<Taxi> getListVehicleNearest(int lonIndex, int latIndex) {
		if (lonIndex >= LON_INDEX_RANGER || lonIndex < 0 || latIndex >= LAT_INDEX_RANGER || latIndex < 0) {
			return new LinkedList<>();
		}
		return CellMapTaxi.mapCell[lonIndex].getLatCell()[latIndex].getListTaxi();
	}

	public static List<Taxi> getListVehicleNearest(double longitude, double latitude) {
		int lonIndex = CellMapTaxi.getLonIndex(longitude);
		int latIndex = CellMapTaxi.getLatIndex(latitude);
		return CellMapTaxi.mapCell[lonIndex].getLatCell()[latIndex].getListTaxi();
	}

	public static boolean removeMapCell(Taxi msg) {
		if (msg == null) {
			return false;
		}
		if (msg.getLongitude() == 0d || msg.getLattitute() == 0d) {
			return false;
		}
		boolean result = false;
		try {
			int lonIndex = CellMapTaxi.getLonIndex(msg.getLongitude());
			int latIndex = CellMapTaxi.getLatIndex(msg.getLattitute());
			if (lonIndex >= LON_INDEX_RANGER || lonIndex < 0 || latIndex >= LAT_INDEX_RANGER || latIndex < 0) {
				result = false;
			} else {
				LonCell lonCell = CellMapTaxi.mapCell[lonIndex];
				LatCell[] latCellArr = lonCell.getLatCell();
				LatCell latCell = latCellArr[latIndex];
				result = latCell.getlstSyn().remove(msg);
			}
		} catch (Exception e) {
			AppLogger.logUserAction.error("RemoveMapCell|" + msg.getId() + "|" + msg.getLongitude() + "|" + msg.getLattitute(),
					e);
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public static boolean addMapCell(Taxi msg) {
		if (msg == null) {
			return false;
		}
		if (msg.getLongitude() == 0f || msg.getLattitute() == 0f) {
			return false;
		}
		try {
			int lonIndex = getLonIndex(msg.getLongitude());
			int latIndex = getLatIndex(msg.getLattitute());
			if (lonIndex >= LON_INDEX_RANGER || lonIndex < 0 || latIndex >= LAT_INDEX_RANGER || latIndex < 0) {
				return false;
			} else {
				LonCell lonCell = CellMapTaxi.mapCell[lonIndex];
				LatCell[] latCellArr = lonCell.getLatCell();
				LatCell latCell = latCellArr[latIndex];
				boolean isAdd = latCell.getlstSyn().add(msg);
				return isAdd;
			}
		} catch (Exception e) {
			AppLogger.logUserAction.error("AddMapCell|" + msg.getId() + "|" + msg.getLongitude() + "|" + msg.getLattitute(),
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
