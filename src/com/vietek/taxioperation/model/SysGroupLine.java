package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author VuD
 * 
 */

@Entity
@Table(name = "ad_groupline")
public class SysGroupLine extends AbstractModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@ManyToOne
	@JoinColumn(name = "ad_group_id", nullable = false)
	private SysGroup sysGroup;
	@ManyToOne
	@JoinColumn(name = "ad_function_id", nullable = false)
	private SysFunction sysFunction;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ad_groupline_action", joinColumns = {
			@JoinColumn(name = "groupline_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "action_id", nullable = false, updatable = false) })
	private Set<SysAction> sysAction;
	private Timestamp created = new Timestamp(System.currentTimeMillis());
	private Integer createBy;
	private Timestamp updated = new Timestamp(System.currentTimeMillis());
	private Integer updateBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SysGroup getSysGroup() {
		return sysGroup;
	}

	public void setSysGroup(SysGroup sysGroup) {
		this.sysGroup = sysGroup;
	}

	public SysFunction getSysFunction() {
		return sysFunction;
	}

	public void setSysFunction(SysFunction sysFunction) {
		this.sysFunction = sysFunction;
	}

	public Set<SysAction> getSysAction() {
		return sysAction;
	}

	public void setSysAction(HashSet<SysAction> sysAction) {
		this.sysAction = sysAction;
	}

	public void setSysAction(Set<SysAction> sysAction) {
		this.sysAction = sysAction;
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
	

	@Override
	public String toString() {
		return "SysGroupLine:" + id + "|" + sysFunction.toString();
	}

	@Override
	public int hashCode() {
		if (id > 0) {
			return id;
		} else {
			return super.hashCode();
		}
	}

}
