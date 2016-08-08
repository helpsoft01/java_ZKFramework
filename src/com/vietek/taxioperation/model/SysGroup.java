package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.vietek.taxioperation.common.AnnonationLinkedTable;
import com.vietek.taxioperation.common.Searchable;

/**
 * 
 * @author VuD
 * 
 */

@Entity
@Table(name = "ad_group")
public class SysGroup extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	@Column(unique = true, nullable = false)
	@Searchable(placehoder="Tìm mã nhóm")
	private String value;
	@Column(unique = true, nullable = false)
	@Searchable(placehoder="Tìm tên nhóm")
	private String name;
	private String note;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ad_group_user", joinColumns = {
			@JoinColumn(name = "group_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "user_id", nullable = false, updatable = false) })
	private Set<SysUser> sysUser = new HashSet<SysUser>();
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sysGroup")
	@Cascade(value = { CascadeType.DELETE })
	private Set<SysGroupLine> sysGroupLines = new HashSet<SysGroupLine>();
	@AnnonationLinkedTable(modelClazz = "SysGroup", displayFieldName = "name")
	private Integer parentId;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ad_rule_group", joinColumns = {
			@JoinColumn(name = "ad_group_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "ad_rule_id", nullable = false, updatable = false) })
	private Set<SysRule> setSysRule = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sysGroup")
	private Set<MapSysGroupCompany> setCompany = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sysGroup")
	private Set<MapSysGroupVehicle> setVehicle = new HashSet<>();

	private Boolean isActive = true;
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

	public Set<SysUser> getSysUser() {
		return sysUser;
	}

	public void setSysUser(HashSet<SysUser> sysUser) {
		this.sysUser = sysUser;
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

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public Integer getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<SysGroupLine> getSysGroupLines() {
		return sysGroupLines;
	}

	public void setSysGroupLines(Set<SysGroupLine> sysGroupLines) {
		this.sysGroupLines = sysGroupLines;
	}

	public void setSysGroupLines(HashSet<SysGroupLine> sysGroupLines) {
		this.sysGroupLines = sysGroupLines;
	}

	public void setSysUser(Set<SysUser> sysUser) {
		this.sysUser = sysUser;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Set<SysRule> getSetSysRule() {
		return setSysRule;
	}

	public void setSetSysRule(Set<SysRule> setSysRule) {
		this.setSysRule = setSysRule;
	}

	public void setSetSysRule(HashSet<SysRule> setSysRule) {
		this.setSysRule = setSysRule;
	}

	public Set<MapSysGroupCompany> getSetCompany() {
		return setCompany;
	}

	public void setSetCompany(Set<MapSysGroupCompany> setCompany) {
		this.setCompany = setCompany;
	}

	public Set<MapSysGroupVehicle> getSetVehicle() {
		return setVehicle;
	}

	public void setSetVehicle(Set<MapSysGroupVehicle> setVehicle) {
		this.setVehicle = setVehicle;
	}

	@Override
	public String toString() {
		return value + "_" + name;
	}

}