package com.vietek.taxioperation.common.timer;

import java.util.List;

import com.vietek.taxioperation.controller.CustomerController;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.util.ControllerUtils;

public class LoadAllCustomerWorker extends Thread{
	@Override
	public void run(){
		List<Customer> lst = this.getAllCustomer();
		for (Customer customer : lst) {
			CustomerController.reload(customer);
		}
	}
	
	private List<Customer> getAllCustomer(){
		CustomerController controller= (CustomerController) ControllerUtils.getController(CustomerController.class);
		CustomerController.LIST_ALL_CUSTOMER = controller.find("from Customer");
		return CustomerController.LIST_ALL_CUSTOMER;
	}
}
