package com.vietek.taxioperation.realtime.command.driver;

import com.vietek.taxioperation.common.EnumOrderType;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.realtime.command.AbstractCommand;

public class DriverPostGuestCommand extends AbstractCommand {
	public static final String COMMAND = "$post_guest=";
	private double longtitute;
	private double lattitute;
	private String address;

	public DriverPostGuestCommand() {
		super(COMMAND);
	}

	/**
	 * data should be: longtitute&lattitute
	 */
	@Override
	public void parseData() {
		super.parseData();
		longtitute = jsonObj.getJsonNumber("beginOrderLong").doubleValue();
		lattitute = jsonObj.getJsonNumber("beginOrderLat").doubleValue();
		address = jsonObj.getString("beginOrderAddr");
	}

	@Override
	public void processData() {
		Taxi taxi = (Taxi) this.getDevice();
		Rider rider = Rider.getRiderVL(taxi.getDriver());
		rider.setLattitute(lattitute);
		rider.setLongtitute(longtitute);
		String note = "";
		if (taxi.getVehicle() != null) {
			note += taxi.getVehicle().getFullValue() + " - " + taxi.getDriver().getPhoneNumber();
		}
		TaxiOrder order = TaxiOrder.createNewTaxiOrder(null, // rider.getCustomer(),
				address, lattitute, longtitute, "", 0, 0, "", note, EnumOrderType.APP_SMARTPHONE.getValue(),false);
		if (order != null) {
			Trip trip = new Trip();
			trip.setOrder(order);
			trip.setPostGuestTaxi(taxi);
			trip.saveToDataBase();
			rider.setTrip(trip);
			TripManager.sharedInstance.addTrip(trip, rider);
		}
	}
}
