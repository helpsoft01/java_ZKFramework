package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.ColumnInfor;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "cat_TelephoneTable")
public class TelephoneTableTms extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -358237710302674991L;

	public TelephoneTableTms() {
		super();
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	@ColumnInfor(name = "id", label = "id")
	private int id;

	@ColumnInfor(name = "value", label = "Mã")
	@Validation(title = CommonDefine.TelephoneTableTms.CODE_TELTABLE, nullable = false)
	@Searchable(placehoder = "Tìm mã")
	private String value;

	@ColumnInfor(name = "name", label = "Tên")
	@Validation(title = CommonDefine.TelephoneTableTms.NAME_TELTABLE, nullable = false)
	@Searchable(placehoder = "Tìm tên")
	private String name;

	@ManyToOne(optional = false)
	@JoinColumn(name = "company_ID", nullable = false, updatable = true)
	@ColumnInfor(name = "sysCompany", label = "Công ty")
	@Validation(title = CommonDefine.SysCompany.COMPANY, nullable = false)
	@Searchable(placehoder = "Tìm công ty")
	private SysCompany sysCompany;

	public SysCompany getSysCompany() {
		return sysCompany;
	}

	public void setSysCompany(SysCompany sysCompany) {
		this.sysCompany = sysCompany;
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

	@Override
	public String toString() {
		return value + "_" + name;
	}

}