package com.vietek.taxioperation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lst_card_payment")
public class PaymentCard  extends AbstractModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String note;
	private String token;
	private int customerId;
	private int driverId;
	private String cardScheme;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getDriverId() {
		return driverId;
	}
	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}
	/**
	 * @return the cardScheme
	 */
	public String getCardScheme() {
		return cardScheme;
	}
	/**
	 * @param cardScheme the cardScheme to set
	 */
	public void setCardScheme(String cardScheme) {
		this.cardScheme = cardScheme;
	}
}
