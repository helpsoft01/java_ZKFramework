package com.vietek.taxioperation.model;

import java.io.Serializable;

import com.vietek.taxioperation.common.AppConstant;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.MapCommon;

/**
 *
 * @author VuD
 */
public class VehicleStatusDD implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1787440610665778673L;
	private int vehicleId;
	private long lastRegisted = 0;
	private boolean isFree = false;

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public long getLastRegisted() {
		return lastRegisted;
	}

	public void setLastRegisted(long lastRegisted) {
		this.lastRegisted = lastRegisted;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public static synchronized void updateStatusRegisted(int vehicleId) {
		try {
			VehicleStatusDD vDd = (VehicleStatusDD) MapCommon.MAP_VEHICLE_STATUS_ID.get(vehicleId + "");
			if (vDd == null) {
				vDd = new VehicleStatusDD();
				vDd.setVehicleId(vehicleId);
				vDd.setLastRegisted(System.currentTimeMillis());
				vDd.setFree(false);
			} else {
				vDd.setFree(false);
				vDd.setLastRegisted(System.currentTimeMillis());
			}
			MapCommon.MAP_VEHICLE_STATUS_ID.put(vehicleId + "", vDd);
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrderDD|UpdateRegistedStatus", e);
		}
	}

	public static synchronized void updateStatusPickup(int vehicleId) {
		try {
			VehicleStatusDD vDd = (VehicleStatusDD) MapCommon.MAP_VEHICLE_STATUS_ID.get(vehicleId + "");
			if (vDd == null) {
				vDd = new VehicleStatusDD();
				vDd.setVehicleId(vehicleId);
				vDd.setFree(true);
			} else {
				vDd.setFree(true);
			}
			MapCommon.MAP_VEHICLE_STATUS_ID.put(vehicleId + "", vDd);
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrderDD|UpdatePickupTaxiStatus", e);
		}
	}

	public static synchronized void updateStatusPickup(TaxiOrder taxiOrder) {
		if (taxiOrder == null) {
			return;
		}
		try {
			for (Vehicle vehicle : taxiOrder.getRegistedTaxis()) {
				VehicleStatusDD vDd = (VehicleStatusDD) MapCommon.MAP_VEHICLE_STATUS_ID.get(vehicle.getId() + "");
				if (vDd == null) {
					vDd = new VehicleStatusDD();
					vDd.setVehicleId(vehicle.getId());
					vDd.setFree(true);
//					MapCommon.MAP_VEHICLE_STATUS_ID.put(vehicle.getId() + "", vDd);
				} else {
					vDd.setFree(true);
				}
				MapCommon.MAP_VEHICLE_STATUS_ID.put(vehicle.getId() + "", vDd);
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("TaxiOrderDD|UpdatePickupTaxiStatus", e);
		}
	}

	/**
	 * Kiem tra xe da dang ky don o xe khac chua
	 * 
	 * @param vehicle
	 * @return true neu khong du dieu kien, false neu du dieu kien
	 */
	public static synchronized boolean checkRegistedOther(int vehicleId) {
		boolean result = false;
		VehicleStatusDD vDd = (VehicleStatusDD) MapCommon.MAP_VEHICLE_STATUS_ID.get(vehicleId + "");
		if (vDd != null) {
			if (!vDd.isFree()) {
				if (vDd.getLastRegisted() > 0) {
					if ((System.currentTimeMillis() - vDd.getLastRegisted()) < AppConstant.TIME_OUT_REGISTED) {
						result = true;
					}
				}
			}
		}
		return result;
	}

}
