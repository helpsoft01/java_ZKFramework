package com.vietek.taxioperation.model;

import java.io.Serializable;
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
import javax.persistence.Table;

/**
 *
 * @author VuD
 */
@Entity
@Table(name = "ad_rule")
public class SysRule extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@Column(unique = true, nullable = false)
	private String value;
	@Column(nullable = false)
	private String name;
	private String description;
	@Column(length = 8000)
	private String hql;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ad_rule_group", joinColumns = {
			@JoinColumn(name = "ad_rule_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "ad_group_id", nullable = false, updatable = false) })
	private Set<SysGroup> setSysGroup = new HashSet<>();
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ad_rule_user", joinColumns = {
			@JoinColumn(name = "ad_rule_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "ad_user_id", nullable = false, updatable = false) })
	private Set<SysUser> setSysUser = new HashSet<>();
	private Integer priority = 0;
	private String modelName;
	private Boolean isActive = true;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public Set<SysGroup> getSetSysGroup() {
		return setSysGroup;
	}

	public void setSetSysGroup(Set<SysGroup> setSysGroup) {
		this.setSysGroup = setSysGroup;
	}

	public void setSetSysGroup(HashSet<SysGroup> setSysGroup) {
		this.setSysGroup = setSysGroup;
	}

	public Set<SysUser> getSetSysUser() {
		return setSysUser;
	}

	public void setSetSysUser(Set<SysUser> setSysUser) {
		this.setSysUser = setSysUser;
	}

	public void setSetSysUser(HashSet<SysUser> setSysUser) {
		this.setSysUser = setSysUser;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return value + "_" + name;
	}

	@Override
	public int hashCode() {
		if (id != 0) {
			return id;
		} else {
			return super.hashCode();
		}
	}
}
