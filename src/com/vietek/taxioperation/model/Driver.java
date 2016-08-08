package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;
import com.vietek.taxioperation.controller.DriverController;
import com.vietek.taxioperation.controller.TaxiGroupController;
import com.vietek.taxioperation.util.ControllerUtils;

@Entity
@Table(name = "lst_driver", schema = "txm_tracking")
@Where(clause = "Active=1")
public class Driver extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "DriverID")
	private int id;
	@Column(name = "DriverCode")
	private String value;
	@Validation(title = CommonDefine.DriverTxm.DRIVER_NAME, nullable = false)
	@Searchable(placehoder = "Tìm Họ tên lái xe")
	@Column(name = "DriverName")
	private String name;
	@Searchable(placehoder = "Tìm Số cá nhân")
	@Column(name = "PhoneNumber")
	private String phoneNumber;
	@Validation(title = CommonDefine.DriverTxm.DRIVER_PHONE, nullable = false)
	@Searchable(placehoder = "Tìm Văn hóa số")
	@Column(name = "PhoneOffice")
	private String phoneOffice;
	@Validation(title = CommonDefine.DriverTxm.DRIVER_CODE, nullable = false, alowrepeat = false)
	@Searchable(placehoder = "Tìm MSNV")
	@Column(name = "StaffCard")
	private Integer staffCard;
	@Searchable(placehoder = "Tìm Đơn vị quản lý")
	@Validation(title = CommonDefine.DriverTxm.DRIVER_AGENT, nullable = false)
	@ManyToOne
	@JoinColumn(name = "AgentID")
	private Agent agent;
	@Searchable(placehoder = "Tìm UUID")
	@Column(name = "mobile_uuid")
	private String mobileUUID;

	private String password;
	@Column(name = "Active")
	private Boolean isActive = true;
	private Boolean isAppRegister = false;
	@Transient
	@Column(name = "VehicleGroupID")
	private TaxiGroup taxiGroup = null;
	private Integer VehicleGroupID;

	public String getMobileUUID() {
		return mobileUUID == null ? "" : mobileUUID;
	}

	public void setMobileUUID(String mobileUUID) {
		this.mobileUUID = mobileUUID;
	}

	private Double rate = 5d;

	@Column
	@Lob
	private byte[] avatar;

	public byte[] getAvatar() {
		if (avatar == null) {
			return "abc".getBytes();
		}
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneOffice() {
		return phoneOffice;
	}

	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice;
	}

	public Integer getStaffCard() {
		return staffCard;
	}

	public void setStaffCard(Integer staffCard) {
		this.staffCard = staffCard;
	}

	@Override
	public String toString() {
		return staffCard + "_" + name;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsAppRegister() {
		return isAppRegister;
	}

	public void setIsAppRegister(Boolean isAppRegister) {
		this.isAppRegister = isAppRegister;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public TaxiGroup getTaxiGroup() {
		if (taxiGroup == null) {
			TaxiGroup taxiGroup = MapCommon.MAP_TAXI_GROUP.get(VehicleGroupID);
			if (taxiGroup == null) {
				TaxiGroupController controller = (TaxiGroupController) ControllerUtils
						.getController(TaxiGroupController.class);
				List<TaxiGroup> lstTmp = controller.find("from TaxiGroup where id = ?", this.VehicleGroupID);
				if (lstTmp != null && lstTmp.size() > 0) {
					taxiGroup = lstTmp.get(0);
				}
			}
			this.taxiGroup = taxiGroup;
		}
		return taxiGroup;
	}

	public void setTaxiGroup(TaxiGroup taxiGroup) {
		this.taxiGroup = taxiGroup;
	}

	public Integer getVehicleGroupID() {
		return VehicleGroupID;
	}

	public void setVehicleGroupID(Integer vehicleGroupID) {
		VehicleGroupID = vehicleGroupID;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof Driver) {
			if (((Driver) obj).getId() == this.id) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public void save() {
		super.save();
		DriverController.reload(this.getId());
	}
}
