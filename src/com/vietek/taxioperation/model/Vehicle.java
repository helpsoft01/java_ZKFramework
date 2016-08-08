package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.vietek.taxioperation.common.ApplyEditor;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;
import com.vietek.taxioperation.controller.DeviceController;
import com.vietek.taxioperation.controller.ParkingAreaController;
import com.vietek.taxioperation.controller.TaxiGroupController;
import com.vietek.taxioperation.controller.TaxiTypeController;
import com.vietek.taxioperation.util.ControllerUtils;

@Entity
@Table(name = "lst_vehicle", schema = "txm_tracking")
@Where(clause = "IsDelete = 0")
public class Vehicle extends AbstractModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1922606530937309079L;
	@Id
	@GeneratedValue
	@Column(name = "VehicleID")
	private int id;
	@Validation(title = CommonDefine.VehicleTxm.LICENSE_PLACE, nullable = false, alowrepeat = false)
	@Column(name = "LicensePlate")
	@Searchable(placehoder = "Biển số")
	private String taxiNumber;
	@Validation(title = CommonDefine.VehicleTxm.VEHICLE_NUMBER, nullable = false, alowrepeat = false)
	@Column(name = "VehicleNumber")
	@Searchable(placehoder = "Số tài")
	private String value;

	private String VinNumber;

	// @ManyToOne
	// @JoinColumn(name = "VehicleTypeID", referencedColumnName = "TypeID",
	// nullable = false, foreignKey = @ForeignKey(name =
	// "FK_lst_vehicle_lst_vehicletype_TypeID"))
	@Searchable(placehoder = "Tìm loại xe")
	@Transient
	private TaxiType taxiType;
	@Validation(title = CommonDefine.VehicleTxm.VEHICLE_TYPE, nullable = false)
	private Integer VehicleTypeID;

	// @ManyToOne
	// @JoinColumn(name = "VehicleGroupID", referencedColumnName = "GroupID",
	// nullable = false, foreignKey = @ForeignKey(name =
	// "FK_lst_vehicle_lst_vehiclegroup_GroupID"))
	@Searchable(placehoder = "Tìm nhóm xe")
	@Transient
	private TaxiGroup taxiGroup;

	@Validation(title = CommonDefine.VehicleTxm.VEHICLE_GROUP, nullable = false)
	private Integer VehicleGroupID;

	// @ManyToOne(optional = true)
	// @JoinColumn(name = "DeviceID", referencedColumnName = "DeviceID",
	// foreignKey = @ForeignKey(name = "FK_lst_vehicle_lst_device_DeviceID"))
	@Searchable(placehoder = "Tìm Thiết bị")
	@Transient
	private Device device;
	@Validation(title = CommonDefine.VehicleTxm.DEVICE, alowrepeat = false)
	private Integer DeviceID;
	// @Searchable(placehoder = "Tìm bãi xe")
	// @ManyToOne
	// @JoinColumn(name = "ParkingID", nullable = false, foreignKey =
	// @ForeignKey(name = "FK_lst_vehicle_lst_parkingarea_ParkingID"))
	// private ParkingArea Parking;
	private Integer ParkingID;
	@Transient
	@Searchable(placehoder = "Tìm bãi xe")
	private ParkingArea Parking;

	private Integer MainDriverID;
	private Integer CompanyID;
	@FixedCombobox(label = { "Xe Công ty", "Xe Hợp tác Kinh doanh" }, value = { 1, 2 })
	@Validation(title = CommonDefine.VehicleTxm.TYPE_MAR_TAXI, nullable = false)
	private Integer BusinessType;
	private Integer ManagementType;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(nullable = false)
	private Integer SpeedLimit = 80;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(nullable = false)
	private Integer StopTimeLimit = 120;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(nullable = false)
	private Integer EmptyDrivingLimit = 50;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer AutoFinishShift = 0;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	private Integer FinishShiftTiming = 0;
	private String DigitalValue;
	private Integer Digital;
	private Integer PulsePerKm = 3600;
	private Timestamp LastMaintainTime;
	// @ApplyEditor(classNameEditor = "IntCheckEditor")
	@Column(name = "Active")
	private Boolean isActive = true;
	@ApplyEditor(classNameEditor = "DateTimeEditor")
	private Timestamp InsertDate;
	private Integer IsDelete;
	private Integer MeterVersion;
	private Integer MemoryState;
	private Timestamp MeterDate;
	private Timestamp MeterPingTime;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer NeedRegister = 1;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer RegisterOffice = 0;
	private String Comment;

	public Vehicle() {

	}

	public Vehicle(Vehicle msg) {
		this.id = msg.getId();
		this.taxiNumber = msg.getTaxiNumber();
		this.value = msg.getValue();
		this.taxiType = msg.getTaxiType();
		this.taxiGroup = msg.getTaxiGroup();
		this.device = msg.getDevice();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaxiNumber() {
		return taxiNumber;
	}

	public void setTaxiNumber(String taxiNumber) {
		this.taxiNumber = taxiNumber;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getVinNumber() {
		return VinNumber;
	}

	public void setVinNumber(String vinNumber) {
		VinNumber = vinNumber;
	}

	public Integer getBusinessType() {
		return BusinessType;
	}

	public void setBusinessType(Integer businessType) {
		BusinessType = businessType;
	}

	public Integer getManagementType() {
		return ManagementType;
	}

	public void setManagementType(Integer managementType) {
		ManagementType = managementType;
	}

	public Integer getSpeedLimit() {
		return SpeedLimit;
	}

	public void setSpeedLimit(Integer speedLimit) {
		SpeedLimit = speedLimit;
	}

	public Integer getStopTimeLimit() {
		return StopTimeLimit;
	}

	public void setStopTimeLimit(Integer stopTimeLimit) {
		StopTimeLimit = stopTimeLimit;
	}

	public Integer getEmptyDrivingLimit() {
		return EmptyDrivingLimit;
	}

	public void setEmptyDrivingLimit(Integer emptyDrivingLimit) {
		EmptyDrivingLimit = emptyDrivingLimit;
	}

	public Integer getAutoFinishShift() {
		return AutoFinishShift;
	}

	public void setAutoFinishShift(Integer autoFinishShift) {
		AutoFinishShift = autoFinishShift;
	}

	public Integer getFinishShiftTiming() {
		return FinishShiftTiming;
	}

	public void setFinishShiftTiming(Integer finishShiftTiming) {
		FinishShiftTiming = finishShiftTiming;
	}

	public String getDigitalValue() {
		return DigitalValue;
	}

	public void setDigitalValue(String digitalValue) {
		DigitalValue = digitalValue;
	}

	public Integer getDigital() {
		return Digital;
	}

	public void setDigital(Integer digital) {
		Digital = digital;
	}

	public Integer getPulsePerKm() {
		return PulsePerKm;
	}

	public void setPulsePerKm(Integer pulsePerKm) {
		PulsePerKm = pulsePerKm;
	}

	public Timestamp getLastMaintainTime() {
		return LastMaintainTime;
	}

	public void setLastMaintainTime(Timestamp lastMaintainTime) {
		LastMaintainTime = lastMaintainTime;
	}

	public Boolean getActiveStatus() {
		if (isActive) {
			return true;
		}
		return false;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Timestamp getInsertDate() {
		return InsertDate;
	}

	public void setInsertDate(Timestamp insertDate) {
		InsertDate = insertDate;
	}

	public Integer getIsDelete() {
		return IsDelete;
	}

	public void setIsDelete(Integer isDelete) {
		IsDelete = isDelete;
	}

	public Integer getMeterVersion() {
		return MeterVersion;
	}

	public Timestamp getMeterDate() {
		return MeterDate;
	}

	public void setMeterDate(Timestamp meterDate) {
		MeterDate = meterDate;
	}

	public Timestamp getMeterPingTime() {
		return MeterPingTime;
	}

	public void setMeterPingTime(Timestamp meterPingTime) {
		MeterPingTime = meterPingTime;
	}

	public Integer getNeedRegister() {
		return NeedRegister;
	}

	public void setNeedRegister(Integer needRegister) {
		NeedRegister = needRegister;
	}

	public Integer getRegisterOffice() {
		return RegisterOffice;
	}

	public void setRegisterOffice(Integer registerOffice) {
		RegisterOffice = registerOffice;
	}

	public String getComment() {
		return Comment;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public void setMeterVersion(Integer meterVersion) {
		MeterVersion = meterVersion;
	}

	public Integer getMemoryState() {
		return MemoryState;
	}

	public void setMemoryState(Integer memoryState) {
		MemoryState = memoryState;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Device getDevice() {
		if (device == null) {
			DeviceController controler = (DeviceController) ControllerUtils.getController(DeviceController.class);
			String query = "From Device where id = ?";
			List<Device> lstResult = controler.find(query, DeviceID);
			if (lstResult != null && lstResult.size() > 0) {
				device = lstResult.get(0);
			}
		}
		return device;
	}

	public void setDevice(Device device) {
		if (device != null) {
			this.DeviceID = device.getId();
			this.device = device;
		} else {
			this.device = null;
			this.DeviceID = null;
		}
	}

	public Integer getDeviceID() {
		return DeviceID;
	}

	public void setDeviceID(Integer deviceID) {
		DeviceID = deviceID;
	}

	public void setVehicleTypeID(Integer vehicleTypeID) {
		VehicleTypeID = vehicleTypeID;
	}

	public void setVehicleGroupID(Integer vehicleGroupID) {
		VehicleGroupID = vehicleGroupID;
	}

	public ParkingArea getParking() {
		if (Parking == null) {
			ParkingAreaController controler = (ParkingAreaController) ControllerUtils
					.getController(ParkingAreaController.class);
			String query = "From ParkingArea where id = ?";
			List<ParkingArea> lstResult = controler.find(query, ParkingID);
			if (lstResult != null && lstResult.size() > 0) {
				Parking = lstResult.get(0);
			}
		}
		return Parking;
	}

	public void setParking(ParkingArea parking) {
		if (parking != null) {
			this.ParkingID = parking.getId();
			this.Parking = parking;
		} else {
			this.ParkingID = null;
			this.Parking = null;
		}
	}

	public Integer getParkingID() {
		return ParkingID;
	}

	public void setParkingID(Integer parkingID) {
		ParkingID = parkingID;
	}

	public Integer getMainDriverID() {
		return MainDriverID;
	}

	public void setMainDriverID(Integer mainDriverID) {
		MainDriverID = mainDriverID;
	}

	public Integer getCompanyID() {
		return CompanyID;
	}

	public void setCompanyID(Integer companyID) {
		CompanyID = companyID;
	}

	public int getCarType() {
		return this.getTaxiType().getSeats().getId() == 23 ? 1 : 2;
	}

	public TaxiType getTaxiType() {
		if (taxiType == null) {
			TaxiTypeController controler = (TaxiTypeController) ControllerUtils.getController(TaxiTypeController.class);
			String query = "From TaxiType where id = ?";
			List<TaxiType> lstResult = controler.find(query, VehicleTypeID);
			if (lstResult != null && lstResult.size() > 0) {
				taxiType = lstResult.get(0);
			}
		}
		return taxiType;
	}

	public void setTaxiType(TaxiType taxiType) {
		if (taxiType != null) {
			this.VehicleTypeID = taxiType.getId();
			this.taxiType = taxiType;
		} else {
			VehicleTypeID = null;
			this.taxiType = null;
		}

	}

	public TaxiGroup getTaxiGroup() {
		if (taxiGroup == null) {
			TaxiGroupController controler = (TaxiGroupController) ControllerUtils
					.getController(TaxiGroupController.class);
			String query = "From TaxiGroup where id = ?";
			List<TaxiGroup> lstResult = controler.find(query, VehicleGroupID);
			if (lstResult != null && lstResult.size() > 0) {
				taxiGroup = lstResult.get(0);
			}
		}
		return taxiGroup;
	}

	public void setTaxiGroup(TaxiGroup taxiGroup) {
		if (taxiGroup != null) {
			this.VehicleGroupID = taxiGroup.getId();
			this.taxiGroup = null;
		} else {
			this.VehicleGroupID = null;
			this.taxiGroup = null;
		}
	}


	public Integer getVehicleGroupID() {
		return VehicleGroupID;
	}

	@Override
	public String toString() {
		return getTaxiGroup().getAgent().getValue() + "_" + value;
	}

	public String fieldSearh() {
		return "value";
	}

	public String getFullValue() {
		return getTaxiGroup().getAgent().getValue() + value;
	}

	public Integer getVehicleTypeID() {
		return VehicleTypeID;
	}

	@Override
	public int hashCode() {
		return id;
	}

}
