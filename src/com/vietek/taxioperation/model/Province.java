package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "lst_province", schema = "txm_tracking")
public class Province extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ProvinceID")
	@GeneratedValue
	private int id;
	@Searchable(placehoder = "Mã tỉnh thành")
	@Validation(title = CommonDefine.Province.CODE_PROVINCE, nullable = false, alowrepeat = false)
	@Column(name = "ProvinceCode")
	private String value;
	@Searchable(placehoder = "Tên tỉnh thành")
	@Validation(title = CommonDefine.Province.NAME_PROVINCE, nullable = false)
	private String ProvinceName;
	@Searchable(placehoder = "Quốc gia")
	@Validation(title = CommonDefine.Province.CODE_NATIONAL, nullable = false)
	@ManyToOne
	@JoinColumn(name = "NationalID", nullable = false, foreignKey = @ForeignKey(name = "FK_lst_province_lst_national_NationalID") )
	private National national;
	@Searchable(placehoder = "Loại")
	private String type;
	@Column(name = "Active")
	private Boolean isActive = true;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// public String getProvinceCode() {
	// return ProvinceCode;
	// }
	//
	// public void setProvinceCode(String provinceCode) {
	// ProvinceCode = provinceCode;
	// }
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getProvinceName() {
		return ProvinceName;
	}

	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}

	public National getNational() {
		return national;
	}

	public void setNational(National national) {
		this.national = national;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return this.ProvinceName;
	}
}
