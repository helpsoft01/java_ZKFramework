package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Validation;

/**
 * 
 * @author VuD
 * 
 */

@Entity
@Table(name = "ad_company")
public class SysCompany extends AbstractModel implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public int id;
	@Column(nullable = false, unique = true)
	@Validation(title = CommonDefine.SysCompany.CODE_SYSCOMPANY, maxLength = 255, nullable = false, isHasSpecialChar = true)
	private String value;
	@Validation(title = CommonDefine.SysCompany.NAME_SYSCOMPANY, maxLength = 255, nullable = false, isHasSpecialChar = true)
	private String name;
	private String note;
	@Column(name = "parent_id")
	private Integer parentId;
	private Boolean isActive = true;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "sys_company_voipcenter", joinColumns = {
			@JoinColumn(name = "sys_company_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "voip_center_id", nullable = false, updatable = false) })
	private Set<VoipCenter> voipCenter = new HashSet<>();
	private Timestamp created;
	private Integer createBy;
	private Timestamp updated;
	private Integer updateBy;

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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<VoipCenter> getVoipCenter() {
		return voipCenter;
	}

	public void setVoipCenter(Set<VoipCenter> voipCenter) {
		this.voipCenter = voipCenter;
	}

	public void setVoipCenter(HashSet<VoipCenter> voipCenter) {
		this.voipCenter = voipCenter;
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

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Integer getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return value + "_" + name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SysCompany) {
			if (((SysCompany) obj).getId() == id) {
				return true;
			}
		}
		return false;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}