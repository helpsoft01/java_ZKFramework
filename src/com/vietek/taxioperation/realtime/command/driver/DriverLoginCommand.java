package com.vietek.taxioperation.realtime.command.driver;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.vietek.taxioperation.controller.DriverController;
import com.vietek.taxioperation.controller.VehicleController;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.realtime.TripDispatcher;
import com.vietek.taxioperation.realtime.command.AbstractCommand;
import com.vietek.taxioperation.realtime.command.MobileCommand;
import com.vietek.taxioperation.util.ConfigUtil;

public class DriverLoginCommand extends AbstractCommand {
	public static final String COMMAND = "$driverlogin=";
	private int driverId;
	private int vehicleId;
	public DriverLoginCommand() {
		super(COMMAND);
	}

	@Override
	public void parseData() {
		super.parseData();
		driverId = jsonObj.getInt("driver_id");
		vehicleId = jsonObj.getInt("vehicle_id");
	}

	@Override
	public void processData() {
		super.processData();
		Driver driver = DriverController.getDriver(driverId);
		Vehicle vehicle = VehicleController.getVehicle(vehicleId);

		if (driver!= null && vehicle != null) {
			Taxi taxi = Taxi.getTaxi(driver, vehicle);
			taxi.setHandler(getHandler());
			getHandler().setDevice(taxi);
			JsonObjectBuilder tripJsonObj = Json.createObjectBuilder();
			JsonObjectBuilder riderJsonObj = Json.createObjectBuilder();
			
			taxi.restoreTripFromDB();
			
			if (taxi.getTrip() != null) {
				if (taxi.getTrip().isInProgress()) {
					tripJsonObj = taxi.getTrip().getTripInfo();
					riderJsonObj = taxi.getTrip().getRiderInfo();
				}
				else {
					taxi.setTrip(null);
				}
			}
			
			String dispatchType = ConfigUtil.getValueConfig("FIND_TAXI_TYPE", TripDispatcher.DISPATCH_TYPE_UNICAST);
			if (!dispatchType.equalsIgnoreCase(TripDispatcher.DISPATCH_TYPE_UNICAST) 
					&& !dispatchType.equalsIgnoreCase(TripDispatcher.DISPATCH_TYPE_MULTICAST)) {
				dispatchType = TripDispatcher.DISPATCH_TYPE_UNICAST;
			}
			
			JsonObject json = Json.createObjectBuilder()
					.add("status", 0)
					.add("trip", tripJsonObj)
					.add("rider", riderJsonObj)
					.add("dispatch_type", dispatchType)
					.build();
			getHandler().getCtx().writeAndFlush("$login_success=" + json + MobileCommand.END_CMD);
			taxi.driverGetMarketingPlaceInfo();
			if (taxi.getTrip() != null && taxi.getTrip().getStatus() == 0) {
				taxi.setTrip(null);
			}
		}
	}
}
