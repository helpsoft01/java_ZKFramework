package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "lst_zone" , schema = "txm_tracking")
public class Zone extends AbstractModel implements Serializable {

	/**
	 * @author MPV
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ZoneID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Searchable()
	@Validation(title = "Tên khu vực", alowrepeat = false , nullable = false)
	private String ZoneName;
	@Searchable
	@Validation(title = "Mã khu vực", alowrepeat = false, nullable = false)
	@Column(name="ZoneCode", unique = true, nullable = false)
	private String value;
	@Validation(title = CommonDefine.Zone.CODE_REGION , nullable = false)
	@Searchable(placehoder = "Tìm vùng miền")
	@ManyToOne
	@JoinColumn(name = "RegionID", nullable = false,
	foreignKey = @ForeignKey(name = "lst_zone_ibfk_1"))
	private Region region;
    @Column(name = "Active")
    private Boolean isActive = true;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getZoneName() {
		return ZoneName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public void setZoneName(String zoneName) {
		ZoneName = zoneName;
	}

	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "" + ZoneName;
	}

	public String getString() {
		return toString();
	}

}
