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

/**
 * 
 * @author VuD
 * 
 */

@Entity
@Table(name = "ad_function")
public class SysFunction extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false)
	private String name;
	@Column
	private String clazz; // zul path, or java class
	@Column
	private String zulFile;
	private Boolean isActive = true;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ad_function_action", joinColumns = { @JoinColumn(name = "function_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "action_id", nullable = false, updatable = false) })
	private Set<SysAction> sysAction = new HashSet<SysAction>();
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "function")
	@Cascade(value = { CascadeType.DELETE })
	private Set<SysMenu> sysMenu = new HashSet<SysMenu>();
	private String modelClass;
	private Timestamp created;
	private Integer createBy;
	private Timestamp updated;
	private Integer updateBy;

	public String getZulFile() {
		return zulFile;
	}

	public void setZulFile(String zulFile) {
		this.zulFile = zulFile;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Set<SysAction> getSysAction() {
		return sysAction;
	}

	public void setSysAction(HashSet<SysAction> sysAction) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<SysMenu> getSysMenu() {
		return sysMenu;
	}

	public void setSysMenu(Set<SysMenu> sysMenu) {
		this.sysMenu = sysMenu;
	}

	public void setSysMenu(HashSet<SysMenu> sysMenu) {
		this.sysMenu = sysMenu;
	}

	public void setSysAction(Set<SysAction> sysAction) {
		this.sysAction = sysAction;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	@Override
	public String toString() {
		return name;
	}

}
