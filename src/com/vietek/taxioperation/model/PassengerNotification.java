package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.vietek.taxioperation.common.Searchable;

@Entity
@Table(name = "mobile_notification")
public class PassengerNotification  extends AbstractModel implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@Searchable(placehoder="Nội dung thông báo")
	private String message;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "mobile_notification_customer", joinColumns = {
			@JoinColumn(name = "mobile_notification_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "customer_id", nullable = false, updatable = false) })
	private Set<Customer> customers = new HashSet<Customer>();
	private Boolean isActive;

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(HashSet<Customer> customers) {
		this.customers = customers;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
