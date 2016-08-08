package com.vietek.taxioperation.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.model.Vehicle;

/**
 *
 * Jul 5, 2016
 *
 * @author VuD
 *
 */

public class VehicleApi {
	public static synchronized List<Vehicle> getVehicle(SysCompany... companies) {
		List<Vehicle> result = new ArrayList<>();
		if (companies != null) {
			for (SysCompany company : companies) {
				List<TaxiGroup> lstTaxiGroup = getListTaxiGroup(company);
				Iterator<Integer> ite = MapCommon.MAP_VEHICLE_ID.keySet().iterator();
				while (ite.hasNext()) {
					Integer key = (Integer) ite.next();
					Vehicle vehicle = MapCommon.MAP_VEHICLE_ID.get(key);
					if (vehicle != null) {
						if (isIntoGroup(lstTaxiGroup, vehicle)) {
							result.add(vehicle);
						}
					}
				}
			}
		}
		return result;
	}

	public static synchronized List<Vehicle> getVehicle(Agent... agents) {
		List<Vehicle> result = new ArrayList<>();
		if (agents != null) {
			for (Agent agent : agents) {
				List<TaxiGroup> lstTaxiGroup = getListTaxiGroup(agent);
				Iterator<Integer> ite = MapCommon.MAP_VEHICLE_ID.keySet().iterator();
				while (ite.hasNext()) {
					Integer key = (Integer) ite.next();
					Vehicle vehicle = MapCommon.MAP_VEHICLE_ID.get(key);
					if (vehicle != null) {
						if (isIntoGroup(lstTaxiGroup, vehicle)) {
							result.add(vehicle);
						}
					}
				}
			}
		}
		return result;
	}

	public static synchronized List<Vehicle> getVehicle(TaxiGroup... groups) {
		List<Vehicle> result = new ArrayList<>();
		if (groups != null) {
			List<TaxiGroup> lstTaxiGroup = Arrays.asList(groups);
			Iterator<Integer> ite = MapCommon.MAP_VEHICLE_ID.keySet().iterator();
			while (ite.hasNext()) {
				Integer key = (Integer) ite.next();
				Vehicle vehicle = (Vehicle) MapCommon.MAP_VEHICLE_ID.get(key);
				if (vehicle != null) {
					if (isIntoGroup(lstTaxiGroup, vehicle)) {
						result.add(vehicle);
					}
				}
			}
		}

		return result;
	}

	public static synchronized List<Driver> getDriver(TaxiGroup... groups) {
		List<Driver> result = new ArrayList<>();
		if (groups != null) {
			List<TaxiGroup> lstTaxiGroup = Arrays.asList(groups);
			Iterator<Integer> ite = MapCommon.MAP_DRIVER.keySet().iterator();
			while (ite.hasNext()) {
				Integer key = (Integer) ite.next();
				Driver driver = MapCommon.MAP_DRIVER.get(key);
				if (driver != null) {
					if (isIntoGroup(lstTaxiGroup, driver)) {
						result.add(driver);
					}
				}
			}
		}
		return result;
	}

	public static synchronized List<Agent> getListAgent(SysCompany company) {
		List<Agent> result = new ArrayList<>();
		Iterator<Integer> ite = MapCommon.MAP_AGENT.keySet().iterator();
		while (ite.hasNext()) {
			Integer key = (Integer) ite.next();
			Agent agent = MapCommon.MAP_AGENT.get(key);
			if (agent != null) {
				if (agent.getCompanyID() == company.getId()) {
					result.add(agent);
				}
			}
		}
		return result;
	}

	private static synchronized List<TaxiGroup> getListTaxiGroup(SysCompany company) {
		List<TaxiGroup> result = new ArrayList<>();
		List<Agent> lstAgent = getListAgent(company);
		for (Agent agent : lstAgent) {
			List<TaxiGroup> lstTaxiGroup = getListTaxiGroup(agent);
			if (lstTaxiGroup != null) {
				result.addAll(lstTaxiGroup);
			}
		}
		return result;
	}
	
	public static synchronized List<TaxiGroup> getListTaxiGroup(Agent... agents) {
		List<TaxiGroup> result = new ArrayList<>();
		for (Agent agent : agents) {
			List<TaxiGroup> lstTmp = getListTaxiGroup(agent);
			if (lstTmp != null) {
				result.addAll(lstTmp);
			}
		}
		return result;
	}

	public static synchronized List<TaxiGroup> getListTaxiGroup(Agent agent) {
		List<TaxiGroup> result = new ArrayList<>();
		Iterator<Integer> ite = MapCommon.MAP_TAXI_GROUP.keySet().iterator();
		while (ite.hasNext()) {
			Integer key = (Integer) ite.next();
			TaxiGroup taxiGroup = MapCommon.MAP_TAXI_GROUP.get(key);
			if (taxiGroup != null) {
				if (taxiGroup.getAgentID() == agent.getId()) {
					result.add(taxiGroup);
				}
			}
		}
		return result;
	}
	
	

	private static synchronized boolean isIntoGroup(List<TaxiGroup> lstGroup, Vehicle vehicle) {
		boolean result = false;
		for (TaxiGroup taxiGroup : lstGroup) {
			if (vehicle.getVehicleGroupID() == taxiGroup.getId()) {
				result = true;
				break;
			}
		}
		return result;
	}

	private static synchronized boolean isIntoGroup(List<TaxiGroup> lstGroup, Driver driver) {
		boolean result = false;
		for (TaxiGroup taxiGroup : lstGroup) {
			if (driver.getVehicleGroupID() == taxiGroup.getId()) {
				result = true;
				break;
			}
		}
		return result;
	}
}
