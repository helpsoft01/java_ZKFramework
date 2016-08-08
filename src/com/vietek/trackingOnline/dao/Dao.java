package com.vietek.trackingOnline.dao;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.vietek.trackingOnline.model.Agent;
import com.vietek.trackingOnline.model.Group;
import com.vietek.trackingOnline.model.Vehicle;
import com.vietek.trackingOnline.model.VehicleByGroup;

public interface Dao {
	public List<Group> cmdGetListGroupCar();

	public List<Agent> cmdGetAgentByUserOnline();

	public List<VehicleByGroup> cmdGetListVehicleInGrp(String selectedGroup);

	public int cmdGetNumberOfOnline(String selectedGroup, int selectedState);

	public List<Vehicle> cmdGetOnline_new(String selectedGroup, int selectedState, int start, int limit);

	public List<JSONObject> cmdGetDetailVehicleOnline(int vehicleID);
	
	public double cmdTotalRowsHistory(int vehicleID, String timeRange);
	
}
