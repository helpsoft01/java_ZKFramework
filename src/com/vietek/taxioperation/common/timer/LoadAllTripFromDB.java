package com.vietek.taxioperation.common.timer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vietek.taxioperation.controller.TaxiOrderController;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.util.ControllerUtils;

public class LoadAllTripFromDB extends Thread{
	@Override
	public void run(){
		List<TaxiOrder> lst = this.getAll();
		for (TaxiOrder taxiOrder : lst) {
			Trip trip = new Trip();
			Rider rider = Rider.getRider(taxiOrder.getCustomer().getPhoneNumber());
			trip.setRider(rider);
			trip.setOrder(taxiOrder);
			if(taxiOrder.getBeginOrderTime().compareTo(new Date()) >= 0){// Trip in future
				TripManager.sharedInstance.addTrip(trip, rider);
			}else{
				TripManager.mapTrips.put(trip.getId(), trip);
			}
		}
	}
	
	private List<TaxiOrder> getAll(){
		List<TaxiOrder> lst = new ArrayList<TaxiOrder>();
		String sql = "from TaxiOrder where beginOrderTime >= ? and phoneNumber != null order by timeIsUpdated desc, beginOrderTime";
		lst = ((TaxiOrderController) ControllerUtils
				.getController(TaxiOrderController.class)).find(sql,
				new Object[] { (new Timestamp(System.currentTimeMillis() - (45l * 60l * 1000l)))});
		return lst;
	}
}
