package com.vietek.taxioperation.processor;

import java.util.List;
import java.util.Set;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.controller.DriverController;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.SMSUtils;
import com.vietek.taxioperation.util.TaxiUtils;

/**
 *
 * @author VuD
 */
public class SendCancelSmsProcessor extends Thread {
	private final Set<Vehicle> setVehicle;
	private final Customer customer;

	public SendCancelSmsProcessor(Customer customer, Set<Vehicle> setVehicle) {
		super();
		this.customer = customer;
		this.setName("SendCancelSmsProcessor");
		this.setVehicle = setVehicle;
	}

	@Override
	public void run() {
		try {
			if (customer == null || setVehicle == null) {
				return;
			}
			String message = "MAILINHTAXI: Khach hang da huy cuoc ban vua dang ky don";
			for (Vehicle vehicle : setVehicle) {
				if (vehicle.getDeviceID() != null) {
					Driver driver = this.getDriver(vehicle.getDeviceID());
					if (driver != null) {
						if (driver.getPhoneOffice() != null && driver.getPhoneOffice().length() > 0) {
							String phone = CommonUtils.getPhoneForSms(driver.getPhoneOffice());
							if (phone != null && phone.length() > 0) {
								SMSUtils.sendSMS(phone, message);
							}

						}
					}
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	private Driver getDriver(int deviceId) {
		Driver result = null;
		int driverId = TaxiUtils.getDriverIdWs(deviceId + "");
		if (driverId != -1) {
			DriverController controller = (DriverController) ControllerUtils.getController(DriverController.class);
			List<Driver> lstDriver = controller.find("from Driver where id = ?", driverId);
			if (lstDriver != null && !lstDriver.isEmpty()) {
				result = lstDriver.get(0);
			}
		}
		return result;
	}

}