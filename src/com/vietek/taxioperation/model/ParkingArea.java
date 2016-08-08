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

import com.vietek.taxioperation.common.ApplyEditor;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "lst_parkingarea", schema = "txm_tracking")
@Where(clause = "Active = 1")
public class ParkingArea extends AbstractModel implements Serializable {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue()
	@Column(name = "ParkingID")
	private int id;
	@Validation(title = CommonDefine.ParkingAreaDefine.CODE_PARKINGAREA, alowrepeat = false, nullable = false)
	@Column(name = "ParkingCode")
	private String value;
	@Validation(title = CommonDefine.ParkingAreaDefine.NAME_PARKINGAREA, nullable = false)
	private String ParkingName;
	@Validation(title = CommonDefine.ParkingAreaDefine.ADDRESS, nullable = false)
	private String Address;
	private Integer Quota = 0;
	private Double Lati = 0.0;
	private Double Longi = 0.0;
	private Integer Radius = 0;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	@Column(name = "Active", nullable = false)
	private Integer isActive = 1;
	private String Decription;
	@ManyToOne
	@JoinColumn(name = "AgentID", nullable = false, foreignKey = @ForeignKey(name = "FK_lst_parkingarea_lst_agent_AgentID") )
	private Agent agent;
	@Validation(title = CommonDefine.ParkingAreaDefine.GROUP_PARKING, nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "VehicleGroupID", referencedColumnName = "GroupID", foreignKey = @ForeignKey(name = "FK_lst_parkingarea_lst_vehiclegroup_GroupID") , nullable = true)
	private TaxiGroup taxigroup;

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

	public String getParkingName() {
		return ParkingName;
	}

	public void setParkingName(String parkingName) {
		ParkingName = parkingName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public Integer getQuota() {
		return Quota;
	}

	public void setQuota(Integer quota) {
		Quota = quota;
	}

	public Double getLati() {
		return Lati;
	}

	public void setLati(Double lati) {
		Lati = lati;
	}

	public Double getLongi() {
		return Longi;
	}

	public void setLongi(Double longi) {
		Longi = longi;
	}

	public Integer getRadius() {
		return Radius;
	}

	public void setRadius(Integer radius) {
		Radius = radius;
	}

	public Boolean getActiveStatus() {
		if (isActive == 1)
			return true;
		return false;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getDecription() {
		return Decription;
	}

	public void setDecription(String decription) {
		Decription = decription;
	}

	public TaxiGroup getTaxigroup() {
		return taxigroup;
	}

	public void setTaxigroup(TaxiGroup taxigroup) {
		this.taxigroup = taxigroup;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	@Override
	public String toString() {
		return "" + ParkingName;
	}

	public String getString() {
		return toString();
	}
}