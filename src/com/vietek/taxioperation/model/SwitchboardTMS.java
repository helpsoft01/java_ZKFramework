package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "cat_switchboard")
public class SwitchboardTMS extends AbstractModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue()
	private int id;
	@Column(unique = true, nullable = false)
	@Validation(title = CommonDefine.SwitchboardDefine.CODE_SWITCHBOARD, nullable = false)
	@Searchable(placehoder = "Tìm mã")
	private String value;
	@Column(unique = true, nullable = false)
	@Validation(title = CommonDefine.SwitchboardDefine.NAME_SWITCHBOARD, nullable = false)
	@Searchable(placehoder = "Tìm tên")
	private String name;
	private String note;
	private Boolean isActive = true;
	private Timestamp created = new Timestamp(System.currentTimeMillis());
	private Integer createBy;
	private Timestamp updated = new Timestamp(System.currentTimeMillis());
	private Integer ubdateBy;

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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

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

	public void setCreateBy(int createBy) {
		this.createBy = createBy;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public void setUbdateBy(int ubdateBy) {
		this.ubdateBy = ubdateBy;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Integer getUbdateBy() {
		return ubdateBy;
	}

	public void setUbdateBy(Integer ubdateBy) {
		this.ubdateBy = ubdateBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return value + "-" + name;
	}

	public String getString() {
		return toString();
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (obj instanceof SwitchboardTMS) {
			return ((SwitchboardTMS) obj).getId() == this.getId() ? true : false;
		} else
			return false;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
