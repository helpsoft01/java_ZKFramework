package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "lst_region", schema = "txm_tracking")
public class Region extends AbstractModel implements Serializable {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "RegionID")
	@GeneratedValue()
	private int id;
	@Searchable(placehoder = "Tìm tên")
	@Validation(title = CommonDefine.Region.NAME_REGION, nullable = false)
	private String RegionName;
	@Column(name = "RegionCode", unique = true, nullable = false)
	@Validation(title = CommonDefine.Region.CODE_REGION, nullable = false, alowrepeat = false)
	@Searchable(placehoder = "Tìm mã")
	private String value;
	@Column(unique = true, nullable = false)
	private int ParentRegionID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegionName() {
		return RegionName;
	}

	public void setRegionName(String regionName) {
		RegionName = regionName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getParentRegionID() {
		return ParentRegionID;
	}

	public void setParentRegionID(int parentRegionID) {
		ParentRegionID = parentRegionID;
	}

	@Override
	public String toString() {
		return "" + RegionName;
	}

	public String getString() {
		return toString();
	}

}
