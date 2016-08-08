package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;
import com.vietek.taxioperation.controller.AgentController;
import com.vietek.taxioperation.util.ControllerUtils;

@Entity
@Table(name = "lst_vehiclegroup", schema = "txm_tracking")
@Where(clause = "IsDelete = 0 And GroupType = 0")
public class TaxiGroup extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "GroupID")
	private int id;
	@Searchable(placehoder = "Mã Nhóm xe")
	@Validation(title = CommonDefine.VehicleGroup.CODE_VEHICLE_GROUP, nullable = false, alowrepeat = false)
	@Column(name = "GroupCode")
	private String value;
	@Searchable(placehoder = "Tìm tên nhóm")
	@Validation(title = CommonDefine.VehicleGroup.NAME_VEHICLE_GROUP, nullable = false)
	@Column(name = "GroupName")
	private String name;
	@Column(name = "GroupDesription")
	private String desription;
	@Column(name = "GroupLeader")
	private String leader;
	@Column(name = "GroupAddress")
	private String address;
	@Searchable(placehoder = "Tìm chi nhánh")
	@Validation(title = CommonDefine.VehicleGroup.AGENT_VEHICLE_GROUP, nullable = false)
	// @ManyToOne
	// @JoinColumn(name = "AgentID", foreignKey = @ForeignKey(name =
	// "FK_lst_vehiclegroup_lst_agent_AgentID"), nullable = true)
	@Transient
	private Agent agent;
	private Integer AgentID;

	@FixedCombobox(label = { "Xe công ty", "Xe hợp tác kinh doanh" }, value = { 0, 1 })
	@Validation(title = CommonDefine.VehicleGroup.VEHICLE_GROUP_TYPE, nullable = false)
	@Column(name = "GroupType")
	private Integer type;
	@Column(name = "Active")
	private Boolean isActive = true;
	private Integer IsDelete = 0;

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

	public String getDesription() {
		return desription;
	}

	public void setDesription(String desription) {
		this.desription = desription;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsDelete() {
		return IsDelete;
	}

	public void setIsDelete(Integer isDelete) {
		IsDelete = isDelete;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public Agent getAgent() {
		if (agent == null) {
			AgentController controler = (AgentController) ControllerUtils.getController(AgentController.class);
			String query = "From Agent where id = ?";
			List<Agent> lstResult = controler.find(query, AgentID);
			if (lstResult != null && lstResult.size() > 0) {
				agent = lstResult.get(0);
			}
		}
		return agent;
	}

	public void setAgent(Agent agent) {
		this.AgentID = agent.getId();
		this.agent = agent;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof TaxiGroup) {
			if (((TaxiGroup) obj).getId() == this.id) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	public Integer getAgentID() {
		return AgentID;
	}

	public void setAgentID(Integer agentID) {
		AgentID = agentID;
	}

}