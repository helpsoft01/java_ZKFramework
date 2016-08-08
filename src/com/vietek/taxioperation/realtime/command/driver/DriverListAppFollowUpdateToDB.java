package com.vietek.taxioperation.realtime.command.driver;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.model.DriverAppsFollowTripStatus;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.TaxiUtils;

public class DriverListAppFollowUpdateToDB implements Runnable {
	private ArrayList<Taxi> lstTaxi;
	private TaxiOrder order;
	private int status;
	private double lat;
	private double lng;

	public DriverListAppFollowUpdateToDB(ArrayList<Taxi> lstTaxi, TaxiOrder order, int status, double lat, double lng) {
		this.lstTaxi = lstTaxi;
		this.order = order;
		this.status = status;
		this.lat = lat;
		this.lng = lng;
	}

	@Override
	public void run() {
		try {
			StringBuffer lstDriverId = new StringBuffer();
			int MAX_DISTANCE = ConfigUtil.getValueConfig("MAX_DISTANCE", CommonDefine.MAX_DISTANCE); // 1km
			int MAX_DRIVER_TO_ASK = ConfigUtil.getValueConfig("MAX_DRIVER_TO_ASK", CommonDefine.MAX_DRIVER_TO_ASK);
			if (lstTaxi == null || lstTaxi.size() == 0 || status == 2) {
				lstTaxi = (ArrayList<Taxi>) TaxiUtils.getAvaiableTaxiFilter(lat, lng, order.getOrderCarType(),
						MAX_DISTANCE, MAX_DRIVER_TO_ASK, new ArrayList<Taxi>());
			}
			if (lstTaxi != null && lstTaxi.size() > 0) {
				for (Taxi taxi : lstTaxi) {
					lstDriverId.append(taxi.getDriver().getId()).append(",");
				}
			}
			if (lstDriverId.length() > 0)
				lstDriverId.setLength(lstDriverId.length() - 1); // bo dau ';'
																	// cuoi cung

			DriverAppsFollowTripStatus bean = new DriverAppsFollowTripStatus(order, status, lstDriverId.toString());
			bean.setTime(new Timestamp(System.currentTimeMillis()));
			bean.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
