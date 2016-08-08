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

@Entity
@Table(name = "lst_ward",schema = "txm_tracking")
public class Ward extends AbstractModel implements Serializable {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "WardID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "WardCode", unique = true, nullable = false)
	private String value;
	@Column(name = "WardName")
	private String name;
	private String type;
	private String location;
	@ManyToOne
	@JoinColumn(name = "DistrictID",nullable = false,
	foreignKey = @ForeignKey(name = "FK_lst_ward_lst_district_DistrictID"))
	private DistrictTms district;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public DistrictTms getDistrict() {
		return district;
	}
	public void setDistrict(DistrictTms district) {
		this.district = district;
	}
	

}
