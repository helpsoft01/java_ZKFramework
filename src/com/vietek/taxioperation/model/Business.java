package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lst_business", schema = "txm_tracking")
public class Business extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "ID")
	@Id
	@GeneratedValue
	private int id;

	private String TaxCode;
	private String CompanyName;
	private String CompanyAddr;
	private String Tel;
	private String Fax;
	private String Website;
	private String Email;
	private Integer NeedRegister;
	private Integer RegisterOffice;
	@Column(name = "Active")
	private Boolean isActive = true;

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaxCode() {
		return TaxCode;
	}

	public void setTaxCode(String taxCode) {
		TaxCode = taxCode;
	}

	public String getCompanyName() {
		return CompanyName;
	}

	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}

	public String getCompanyAddr() {
		return CompanyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		CompanyAddr = companyAddr;
	}

	public String getTel() {
		return Tel;
	}

	public void setTel(String tel) {
		Tel = tel;
	}

	public String getFax() {
		return Fax;
	}

	public void setFax(String fax) {
		Fax = fax;
	}

	public String getWebsite() {
		return Website;
	}

	public void setWebsite(String website) {
		Website = website;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public Integer getNeedRegister() {
		return NeedRegister;
	}

	public void setNeedRegister(Integer needRegister) {
		NeedRegister = needRegister;
	}

	public Integer getRegisterOffice() {
		return RegisterOffice;
	}

	public void setRegisterOffice(Integer registerOffice) {
		RegisterOffice = registerOffice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
