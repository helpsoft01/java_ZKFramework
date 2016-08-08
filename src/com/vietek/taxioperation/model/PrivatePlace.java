package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.vietek.tracking.ui.model.TreePlace;

@Entity
@Table(name = "lst_point", schema = "txm_tracking")
@Where(clause = "")
public class PrivatePlace extends AbstractModel implements Serializable,
		TreePlace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "PointID")
	private int id;
	private String PointName;
	private Double Longi = 0.0;
	private Double Lati = 0.0;
	private Integer Radius;
	private String Description;
	private String Address;
	@ManyToOne
	@JoinColumn(name = "PointGroupID", referencedColumnName = "CommonID", foreignKey = @ForeignKey(name = "FK_lst_point_lst_common_CommonID"))
	private PointGroup group;
	@ManyToOne
	@JoinColumn(name = "AgentID", referencedColumnName = "AgentID", foreignKey = @ForeignKey(name = "FK_lst_point_lst_agent_AgentID"))
	private Agent agent;
	private Timestamp TimeLog;
	private Integer IsParent = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPointName() {
		return PointName;
	}

	public void setPointName(String pointName) {
		PointName = pointName;
	}

	public Double getLongi() {
		return Longi;
	}

	public void setLongi(Double longi) {
		Longi = longi;
	}

	public Double getLati() {
		return Lati;
	}

	public void setLati(Double lati) {
		Lati = lati;
	}

	public Integer getRadius() {
		return Radius;
	}

	public void setRadius(Integer radius) {
		Radius = radius;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public PointGroup getGroup() {
		return group;
	}

	public void setGroup(PointGroup group) {
		this.group = group;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Timestamp getTimeLog() {
		return TimeLog;
	}

	public void setTimeLog(Timestamp timeLog) {
		TimeLog = timeLog;
	}

	public Integer getIsParent() {
		return IsParent;
	}

	public void setIsParent(Integer isParent) {
		IsParent = isParent;
	}

	@Override
	public int getPlaceId() {
		return id;
	}

	@Override
	public String getPlaceName() {
		// TODO Auto-generated method stub
		return PointName;
	}

	@Override
	public String getSrc() {
		return "./themes/images/Place/sta.png";
	}

}
