package com.vietek.taxioperation.model;

import java.io.Serializable;
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

import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

/**
 *
 * @author VuD
 */
@Entity
@Table(name = "cat_voip_center")
public class VoipCenter extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false, unique = true)
	@Validation(title = "Mã tổng đài voip", maxLength = 255, nullable = false, isHasSpecialChar = true)
	@Searchable(placehoder = "Tìm Mã")
	private String value;
	@Searchable(placehoder = "Tìm tên")
	private String name;
	private String url;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "sys_company_voipcenter", joinColumns = {
			@JoinColumn(name = "voip_center_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "sys_company_id", nullable = false, updatable = false) })
	private Set<SysCompany> sysCompany = new HashSet<>();
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<SysCompany> getSysCompany() {
		return sysCompany;
	}

	public void setSysCompany(Set<SysCompany> sysCompany) {
		this.sysCompany = sysCompany;
	}

	public void setSysCompany(HashSet<SysCompany> sysCompany) {
		this.sysCompany = sysCompany;
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

}