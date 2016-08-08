package com.vietek.taxioperation.taxiorder;

import java.util.List;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;

public class TaxiOrderSaveWorker implements Runnable {

	boolean newTrip = false;
	Customer cut = null;
	List<TaxiOrder> lstOrder = null;

	public TaxiOrderSaveWorker(List<TaxiOrder> lstOrder, Customer cut, boolean newTrip) {

		this.cut = cut;
		this.lstOrder = lstOrder;
		this.newTrip = newTrip;
	}

	@Override
	public void run() {
		TaxiOrder orderSave = new TaxiOrder();
		if (lstOrder.size() > 0)
			orderSave = lstOrder.get(0);
		try {

			/*
			 * custm
			 */
			cut.save();
			if (cut.getId() > 0) {
				for (TaxiOrder order : lstOrder) {
					TripManager.sharedInstance.saveTrip(order);
					if (newTrip) {
						Rider rider = Rider.getRider(order.getCustomer().getPhoneNumber());
						Trip trip = new Trip();
						trip.setRider(rider);
						trip.setOrder(order);
						TripManager.sharedInstance.addTrip(trip, rider);
					} 
					AppLogger.logTaxiorder.info("TaxiOrderSave|2. Order Save ok: Thread Start, phone:+ "
							+ orderSave.getPhoneNumber() + ", id:" + order.getId());
				}
			}

		} catch (Exception ex) {
			AppLogger.logTaxiorder.error("TaxiOrderSave|3. * Order Save ( Error ): Thread Start, phone:+ "
					+ cut.getPhoneNumber() + "- Customer:" + cut.getId(), ex);
			ex.printStackTrace();
		} finally {
		}
	}
}
