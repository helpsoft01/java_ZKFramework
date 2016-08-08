package com.vietek.taxioperation.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.DeviceConfig;

@Repository
public class DeviceConfigController extends BasicController<DeviceConfig> implements
Serializable {
	
	private static final long serialVersionUID = 1L;

	public List<DeviceConfig> getDeviceConfig(int deviceid){
		ListObjectDatabase listObjectDatabase = new ListObjectDatabase();
		List<DeviceConfig> result = listObjectDatabase.getDeviceConfigById(deviceid);
		return result;
	}
}
