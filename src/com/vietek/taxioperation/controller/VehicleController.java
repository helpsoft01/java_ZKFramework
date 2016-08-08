package com.vietek.taxioperation.controller;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.cache.AbstractCache;
import com.vietek.taxioperation.util.cache.Memcached;

@Repository
public class VehicleController extends BasicController<Vehicle> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static AbstractCache vehicleMap = new Memcached("VEHICLE_MAP", 0);
	public static VehicleController controller = (VehicleController) ControllerUtils.getController(VehicleController.class);
	
	public static Vehicle getVehicle(Integer vehicle_id) {
		Vehicle vehicle = (Vehicle) vehicleMap.get(vehicle_id + "");
		if (vehicle == null) {
			vehicle = controller.get(Vehicle.class, vehicle_id);
			vehicleMap.put(vehicle_id + "", vehicle);
		}
		return vehicle;
	}
	
	public static void reload(Integer vehicle_id) {
		vehicleMap.remove(vehicle_id + "");
	}
}
