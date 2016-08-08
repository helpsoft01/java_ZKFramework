package com.vietek.taxioperation.controller;

import java.io.Serializable;
import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.vietek.taxioperation.model.CallCenter;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.PaymentCard;
import com.vietek.taxioperation.util.ControllerUtils;

@Repository
public class PaymentCardController extends BasicController<CallCenter> implements
Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8542303532706157755L;
	public static boolean createToken(String phoneNumber, String token, String cardScheme, String note) {
		Customer customer = CustomerController.getCustomer(phoneNumber);
		if (customer == null)
			return false;
		PaymentCard card = new PaymentCard();
		card.setCustomerId(customer.getId());
		card.setToken(token);
		card.setNote(note);
		card.setCardScheme(cardScheme);
		card.save();
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<PaymentCard> getToken(String phoneNumber) {
		ArrayList<PaymentCard> retVal = null;
		
		Customer customer = CustomerController.getCustomer(phoneNumber);
		if (customer == null)
			return retVal;
		Session session = ControllerUtils.getCurrentSession();
		Query query = session.createQuery("from PaymentCard where customerId = " + customer.getId());
		retVal = (ArrayList<PaymentCard>) query.list();
		session.close();
		
		return retVal;
	}
}
