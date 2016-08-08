package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author sonvh
 *
 */

@Entity
@Table(name = "ad_userGroupCompany")
public class MapSysGroupCompany extends AbstractModel implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4110421047237214029L;

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	private SysGroup sysGroup;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private SysCompany sysCompany;

	public SysCompany getSysCompany() {
		return sysCompany;
	}

	public void setSysCompany(SysCompany sysCompany) {
		this.sysCompany = sysCompany;
	}

	public SysGroup getSysGroup() {
		return sysGroup;
	}

	public void setSysGroup(SysGroup sysGroup) {
		this.sysGroup = sysGroup;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MapSysGroupCompany) {
			if (((MapSysGroupCompany) obj).getId() == id) {
				return true;
			}
		}
		return false;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
