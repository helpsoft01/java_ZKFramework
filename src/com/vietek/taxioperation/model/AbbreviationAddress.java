package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;
import com.vietek.taxioperation.util.SearchAbbreviationUtils;

@Entity
@Table(name = "abbreviationaddress")
public class AbbreviationAddress extends AbstractModel implements Serializable {

	/**
	 * Danh mục bảng địa chỉ viết tắt
	 * 
	 * @author PVM
	 * @updated by dungnd
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable = false)
	@Validation(title = CommonDefine.AbbreviationAddress.CODE_ABB_ADDRESS, nullable = false)
	@Searchable(placehoder = "Nhập địa chỉ viết tắt")
	private String value;

	@Validation(title = CommonDefine.AbbreviationAddress.FULL_ABB_ADDRESS, nullable = false)
	@Searchable(placehoder = "Nhập địa chỉ đầy đủ")
	private String description;

	@Searchable(placehoder = "Thuộc chi nhánh")
	@ManyToOne
	@JoinColumn(name = "agentid")
	private Agent agent;
	@Searchable(placehoder = "Công ty")
	@ManyToOne
	@JoinColumn(name = "companyid")
	private SysCompany company;
	private String note;
	private Double lati;
	private Double longi;
	private Boolean isActive = true;
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

	public String getValue() {
		return value;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public SysCompany getCompany() {
		return company;
	}

	public void setCompany(SysCompany company) {
		this.company = company;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Double getLati() {
		return lati;
	}

	public void setLati(Double lati) {
		this.lati = lati;
	}

	public Double getLongi() {
		return longi;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setLongi(Double longi) {
		this.longi = longi;
	}

	@Override
	public String toString() {
		return "" + value;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public void save() {
		super.save();
		SearchAbbreviationUtils.reloadBVTAddress(this);
	}

}