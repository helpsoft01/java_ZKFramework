package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Validation;

/**
 * @author Dzung
 *
 */
@Entity
@Table(name = "taxi_order_variation")
public class RiderVariationOrder extends AbstractModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@Validation (maxLength = 10, regex = CommonDefine.NUM_PATTERN)
	private String timeCode;
	@Column (name = "timelog")
	private Timestamp timelog;
	@Column (name = "normalOrder")
	private int normalOrder;
	@Column (name = "mobileOrder")
	private int mobileOrder;
	@Column (name = "webOrder")
	private int webOrder;
	@Column (name = "otherOrder")
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

	public RiderVariationOrder(){
		super();
	}
	
}
