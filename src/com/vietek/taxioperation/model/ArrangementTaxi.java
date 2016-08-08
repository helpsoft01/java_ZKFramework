package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "cat_arrangement_taxi")
@Where(clause = "isActive = 1")
public class ArrangementTaxi extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false)
	@Validation(title = "Mã điểm tiếp thị", maxLength = 255, nullable = false, isHasSpecialChar = true)
	private String value;
	@Column(nullable = false)
	@Validation(title = "Tên điểm tiếp thị", maxLength = 255, nullable = false, isHasSpecialChar = true)
	private String name;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AgentID", referencedColumnName = "AgentID", nullable = false, foreignKey = @ForeignKey(name = "FK_lst_pavilionarea_lst_agent_AgentID") )
//	private SysCompany sysCompany;
	private Agent agent;
	private String address;
	private Double longitude = 0d;
	private Double latitude = 0d;
	private Integer radius = 0;
	private Integer maxCar = 0;
	private Integer minCar = 0;
	private Boolean isActive = true;
	private Boolean isMarketing = true;

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

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Integer getMaxCar() {
		return maxCar;
	}

	public void setMaxCar(Integer maxCar) {
		this.maxCar = maxCar;
	}

	public Integer getMinCar() {
		return minCar;
	}

	public void setMinCar(Integer minCar) {
		this.minCar = minCar;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsMarketing() {
		return isMarketing;
	}

	public void setIsMarketing(Boolean isMarketing) {
		this.isMarketing = isMarketing;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ArrangementTaxi) {
			if (((ArrangementTaxi) obj).getId() == this.id) {
				result = true;
			}
		}
		return result;
	}

}
