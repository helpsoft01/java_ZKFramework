package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.FixedCombobox;

@Entity
@Table(name = "lst_feedback")
public class CustomerFeedBack extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@FixedCombobox(label = { "App Khách hàng", "App Tài xế" }, value = { 1, 2 })
	private Integer feedbacktype;
	private Double point = 0.0;
	private String conten;
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	@ManyToOne
	@JoinColumn(name = "driver_id")
	private Driver driver;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFeedbacktype() {
		String returnvalue = "";
		if (feedbacktype == null) {
			return null;
		}
		if (feedbacktype == 1) {
			returnvalue = "App Khách hàng";
		}
		if (feedbacktype == 2) {
			returnvalue = "App Tài xế";
		}
		return returnvalue;
	}

	public void setFeedbacktype(Integer feedbacktype) {
		this.feedbacktype = feedbacktype;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public String getConten() {
		return conten;
	}

	public void setConten(String conten) {
		this.conten = conten;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Integer gettype() {
		return feedbacktype;
	}

	public Integer getFeedType() {
		return this.feedbacktype;
	}

}
