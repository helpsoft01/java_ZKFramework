package com.vietek.taxioperation.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Repository;

import com.vietek.taxioperation.googlemapSearch.PointMap;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.StandandPhoneNumber;
import com.vietek.taxioperation.util.cache.AbstractCache;
import com.vietek.taxioperation.util.cache.Memcached;

@Repository
public class CustomerController extends BasicController<Customer> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3094065625310859128L;
	static private AbstractCache cacheMapByID = new Memcached("CUSTOMER_BY_ID", 0);
	static private AbstractCache cacheMapByPhone = new Memcached("CUSTOMER_BY_PHONE", 0);
	public static List<Customer> LIST_ALL_CUSTOMER = new ArrayList<Customer>();

	/**
	 * reload customer, by remove it from cache
	 * 
	 * @param customer
	 */
	static public void reload(Customer customer) {
		cacheMapByID.put(customer.getId() + "", customer);
		cacheMapByPhone.put(customer.getPhoneNumber(), customer);
	}

	static public Customer getCustomer(int id) {
		Customer customer = null;
		customer = (Customer) cacheMapByID.get(id + "");
		if (customer == null) {
			CustomerController controller = (CustomerController) ControllerUtils
					.getController(CustomerController.class);
			customer = controller.get(Customer.class, id);
			cacheMapByID.put(id + "", customer);
		}
		return customer;
	}

	static public Customer getCustomer(String phoneNumber) {
		Customer customer = null;
		customer = (Customer) cacheMapByPhone.get(phoneNumber);
		if (customer == null) {
			CustomerController customerCtl = (CustomerController) ControllerUtils
					.getController(CustomerController.class);
			List<Customer> lstCustomer = customerCtl.find("from Customer where phoneNumber = ?", phoneNumber.trim());
			if (lstCustomer.size() >= 1) {
				customer = lstCustomer.get(0);
				cacheMapByPhone.put(phoneNumber, customer);
			}
		}
		return customer;
	}

	public String getVerifyCode(Customer customer) {
		String retVal = customer.getVerifyCode();
		Timestamp tmp = customer.getVerifyTimeout();
		if (tmp == null) {
			tmp = new Timestamp(0);
		}
		if (customer.getVerifyCode() == null || tmp.before(new Timestamp(System.currentTimeMillis()))) {
			generateVerifyCode(customer);
			retVal = customer.getVerifyCode();
		}
		return retVal;
	}

	private void generateVerifyCode(Customer customer) {
		StringBuilder code = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			code.append(random.nextInt(9));
		}
		String verifyCode = code.toString();
		// Timeout in 10m
		Timestamp verifyTimeout = new Timestamp(System.currentTimeMillis() + 10 * 60 * 1000);
		customer.setVerifyCode(verifyCode);
		customer.setVerifyTimeout(verifyTimeout);
		this.saveOrUpdate(customer);
	}

	public String resetPassword(Customer customer) {
		StringBuilder code = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			code.append(random.nextInt(9));
		}
		customer.setPassword(code.toString());
		this.saveOrUpdate(customer);
		return code.toString();
	}

	public static Customer getOrCreateCustomer(String phone) {

		Customer customer = null;
		String phoneNumber = phone;
		if (phone.trim() != "") {

			phoneNumber = StandandPhoneNumber.standandPhone(phone);
			customer = CustomerController.getCustomer(phoneNumber);
			if (customer != null) {
				if (customer.getAddressLat() == null || customer.getAddressLng() == null
						|| customer.getAddress() == null) {

					customer.setAddress("");
					customer.setAddressLat(0.0);
					customer.setAddressLng(0.0);
				}
			} else {

				PointMap pMap = new PointMap();
				customer = new Customer();
				customer.setName("Khách mới(" + phoneNumber + ")");
				customer.setPhoneNumber(phoneNumber);
				customer.setPassword("");
				customer.setAddress(pMap.getAddress());
				customer.setAddressLat(pMap.getLat());
				customer.setAddressLng(pMap.getLng());
				customer.setIsActive(true);
				customer.setLastTimeCall(null);//new Timestamp(System.currentTimeMillis()));
			}
		} else {
			PointMap pMap = new PointMap();
			customer = new Customer();
			customer.setName("Khách mới(" + phoneNumber + ")");
			customer.setPhoneNumber(phoneNumber);
			customer.setPassword("");
			customer.setAddress(pMap.getAddress());
			customer.setAddressLat(pMap.getLat());
			customer.setAddressLng(pMap.getLng());
			customer.setIsActive(true);
			customer.setLastTimeCall(new Timestamp(System.currentTimeMillis()));
		}

		return customer;
	}
}
