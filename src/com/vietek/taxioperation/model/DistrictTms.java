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

/**
 * 
 * @author VuD
 * 
 */

@Entity
@Table(name = "lst_district", schema = "txm_tracking")
public class DistrictTms extends AbstractModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "DistrictID")
	private int id;
	@Searchable(placehoder = "Mã Quận huyện")
	@Validation(title = CommonDefine.DistrictTms.CODE_DISTRICTTMS, nullable = false, alowrepeat = false)
	@Column(name = "DistrictCode")
	private String value;
	@Searchable(placehoder = "Tên Quận huyện")
	@Validation(title = CommonDefine.DistrictTms.NAME_DISTRICTTMS,nullable = false)
	@Column(name = "DistrictName")
	private String name;
	@Column(name = "type")
	private String type;
	@Validation(title = CommonDefine.DistrictTms.CODE_PROVINCE, nullable = false)
	@Searchable(placehoder = "Tỉnh thành")
	@ManyToOne
	@JoinColumn(name = "ProvinceID", foreignKey = @ForeignKey(name = "FK_district_province_id"))
	private Province province;
	@Column(name = "location")
	private String location;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return name;
	}

}
