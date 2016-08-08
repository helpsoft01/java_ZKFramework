package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Validation;

public class RiderVariationOrderLog extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RiderVariationOrderLog() {
		super();
	}

	@Id
	@GeneratedValue
	private int id;
	@Validation(maxLength = 10, regex = CommonDefine.NUM_PATTERN)
	private String timeCode;
	@Column
	private Timestamp timelog;
	private int normalOrder;
	private int mobileOrder;
	private int webOrder;
	private int otherOrder;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTimeCode() {
		return timeCode;
	}

	public void setTimeCode(String timeCode) {
		this.timeCode = timeCode;
	}

	public Timestamp getTimelog() {
		return timelog;
	}

	public void setTimelog(Timestamp timelog) {
		this.timelog = timelog;
	}

	public int getNormalOrder() {
		return normalOrder;
	}

	public void setNormalOrder(int normalOrder) {
		this.normalOrder = normalOrder;
	}

	public int getMobileOrder() {
		return mobileOrder;
	}

	public void setMobileOrder(int mobileOrder) {
		this.mobileOrder = mobileOrder;
	}

	public int getWebOrder() {
		return webOrder;
	}

	public void setWebOrder(int webOrder) {
		this.webOrder = webOrder;
	}

	public int getOtherOrder() {
		return otherOrder;
	}

	public void setOtherOrder(int otherOrder) {
		this.otherOrder = otherOrder;
	}

}
