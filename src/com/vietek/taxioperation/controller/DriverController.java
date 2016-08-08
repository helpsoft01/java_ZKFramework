package com.vietek.taxioperation.controller;

import java.io.Serializable;
import java.util.Random;

import org.springframework.stereotype.Repository;

import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.cache.AbstractCache;
import com.vietek.taxioperation.util.cache.Memcached;

@Repository
public class DriverController extends BasicController<Driver> implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static AbstractCache driverMap = new Memcached("DRIVER_MAP", 0);
	public static DriverController controller = (DriverController) ControllerUtils.getController(DriverController.class);
	
	public static Driver getDriver(Integer driver_id) {
		Driver driver = (Driver) driverMap.get(driver_id + "");
		if (driver == null) {
			driver = controller.get(Driver.class, driver_id);
			driverMap.put(driver_id + "", driver);
		}
		return driver;
	}
	
	public static void reload(Integer driver_id) {
		driverMap.remove(driver_id + "");
	}
	
	public String resetPassword(Driver driver) {
		StringBuilder code = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			code.append(random.nextInt(9));
		}
		driver.setPassword(code.toString());
		this.saveOrUpdate(driver);
		return code.toString();
	}
}
