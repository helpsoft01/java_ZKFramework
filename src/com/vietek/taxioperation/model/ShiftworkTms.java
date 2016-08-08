package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.StringUtils;

@Entity
@Table(name = "cat_shiftwork")
public class ShiftworkTms extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@Column(unique = true, nullable = false)
	@Searchable(placehoder = "Mã ca")
	private String value;
	@Column(unique = true, nullable = false)
	@Searchable(placehoder = "Tên ca")
	private String name;
	private Time startshift = StringUtils.getTimeBegin("00:00:00");
	private Time stopshift = new Time(System.currentTimeMillis());
	private Boolean isActive = true;
	private Timestamp created = new Timestamp(System.currentTimeMillis());
	private Integer createBy;
	private Timestamp updated = new Timestamp(System.currentTimeMillis());
	private Integer ubdateBy;

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public Integer getUbdateBy() {
		return ubdateBy;
	}

	public void setUbdateBy(Integer ubdateBy) {
		this.ubdateBy = ubdateBy;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Time getStartshift() {
		return startshift;
	}

	public void setStartshift(Time startshift) {
		this.startshift = startshift;
	}

	public Time getStopshift() {
		return stopshift;
	}

	public void setStopshift(Time stopshift) {
		this.stopshift = stopshift;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "" + name;
	}

}