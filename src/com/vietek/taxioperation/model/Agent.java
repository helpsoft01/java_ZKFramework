package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;
import com.vietek.taxioperation.controller.SysCompanyController;
import com.vietek.taxioperation.util.ControllerUtils;

/**
 * 
 * @author VuD
 * 
 */

@Entity
@Table(name = "lst_agent", schema = "txm_tracking")
@Where(clause = "AgentType = 1")
public class Agent extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "AgentID")
	private int id;
	@Searchable(placehoder = "Mã chi nhánh")
	@Validation(title = CommonDefine.Agent.AGENT_CODE, nullable = false, alowrepeat = false)
	@Column(name = "AgentCode")
	private String value;
	@Searchable(placehoder = "Tên chi nhánh")
	@Validation(title = CommonDefine.Agent.AGENT_NAME, nullable = false)
	private String AgentName;
	private String AgentDescription;
	private String Phone;
	private String Fax;
	private String AgentAddress;
	@Searchable(placehoder = "Công ty chủ quản")
	@Transient
	private SysCompany company;
	@Validation(title = CommonDefine.Agent.AGENT_COMPANY, nullable = false)
	private Integer CompanyID;
	@ManyToOne
	@JoinColumn(name = "ZoneID", foreignKey = @ForeignKey(name = "FK_lst_agent_lst_zone_ZoneID"))
	private Zone zone;
	@ManyToOne
	@JoinColumn(name = "ProvinceID", foreignKey = @ForeignKey(name = "FK_lst_agent_lst_province_ProvinceID"))
	private Province province;

	private String AgentLogoPath;
	private String AgentBorders;
	private String AgentCodePrint;
	private Integer DeltaWaitTimeForFree;
	private String Hotline;
	private String TaxCode;
	private String CallCenterNumber;
	@Column(name = "Active")
	private Boolean isActive = true;
	private Integer AllowInVehicleNumber = 0;
	private String AgentNumber = "0";
	private Integer AgentType = 1;

	@ManyToOne
	@JoinColumn(name = "BusinessID", foreignKey = @ForeignKey(name = "FK_lst_agent_lst_business_ID"))
	private Business business;

	public String getAgentName() {
		return AgentName;
	}

	public void setAgentName(String agentName) {
		AgentName = agentName;
	}

	public String getAgentDescription() {
		return AgentDescription;
	}

	public void setAgentDescription(String agentDescription) {
		AgentDescription = agentDescription;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getFax() {
		return Fax;
	}

	public void setFax(String fax) {
		Fax = fax;
	}

	public String getAgentAddress() {
		return AgentAddress;
	}

	public void setAgentAddress(String agentAddress) {
		AgentAddress = agentAddress;
	}

	public String getHotline() {
		return Hotline;
	}

	public void setHotline(String hotline) {
		Hotline = hotline;
	}

	public String getTaxCode() {
		return TaxCode;
	}

	public void setTaxCode(String taxCode) {
		TaxCode = taxCode;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getAgentLogoPath() {
		return AgentLogoPath;
	}

	public void setAgentLogoPath(String agentLogoPath) {
		AgentLogoPath = agentLogoPath;
	}

	public String getAgentBorders() {
		return AgentBorders;
	}

	public void setAgentBorders(String agentBorders) {
		AgentBorders = agentBorders;
	}

	public String getAgentCodePrint() {
		return AgentCodePrint;
	}

	public void setAgentCodePrint(String agentCodePrint) {
		AgentCodePrint = agentCodePrint;
	}

	public Integer getDeltaWaitTimeForFree() {
		return DeltaWaitTimeForFree;
	}

	public void setDeltaWaitTimeForFree(Integer deltaWaitTimeForFree) {
		DeltaWaitTimeForFree = deltaWaitTimeForFree;
	}

	public String getCallCenterNumber() {
		return CallCenterNumber;
	}

	public void setCallCenterNumber(String callCenterNumber) {
		CallCenterNumber = callCenterNumber;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getAllowInVehicleNumber() {
		return AllowInVehicleNumber;
	}

	public void setAllowInVehicleNumber(Integer allowInVehicleNumber) {
		AllowInVehicleNumber = allowInVehicleNumber;
	}

	public String getAgentNumber() {
		return AgentNumber;
	}

	public void setAgentNumber(String agentNumber) {
		AgentNumber = agentNumber;
	}

	public Integer getAgentType() {
		return AgentType;
	}

	public void setAgentType(Integer agentType) {
		AgentType = agentType;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public SysCompany getCompany() {
		if (company == null) {
			SysCompanyController controler = (SysCompanyController) ControllerUtils
					.getController(SysCompanyController.class);
			String query = "From SysCompany where id = ?";
			List<SysCompany> lstResult = controler.find(query, CompanyID);
			if (lstResult != null && lstResult.size() > 0) {
				company = lstResult.get(0);
			}
		}
		return company;
	}

	public void setCompany(SysCompany company) {
		if (company != null) {
			this.setCompanyID(company.getId());
			this.company = company;
		} else {
			this.CompanyID = null;
			this.company = null;
		}

	}

	public int getCompanyID() {
		return CompanyID;
	}

	public void setCompanyID(int companyID) {
		CompanyID = companyID;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof Agent) {
			if (((Agent) obj).getId() == this.id) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "" + value + "-" + AgentName;
	}
}