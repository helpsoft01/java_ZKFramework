package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.vietek.taxioperation.common.ApplyEditor;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;
import com.vietek.taxioperation.controller.AgentController;
import com.vietek.taxioperation.controller.SimManagementController;
import com.vietek.taxioperation.util.ControllerUtils;

@Entity
@Table(name = "lst_device", schema = "txm_tracking")
@Where(clause = "IsDelete = 0")
public class Device extends AbstractModel implements Serializable {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue()
	@Column(name = "DeviceID")
	private int id;
	@Searchable(placehoder = "Tìm biển số")
	@Column(name = "DeviceCode")
	private String DeviceCode;
	@Searchable(placehoder = "Tìm số Imei")
	@Validation(title = CommonDefine.DeviceTxm.IMEI_DEVICE, nullable = false, isHasSpecialChar = false, alowrepeat = false)
	private String Imei;
	@Searchable(placehoder = "Số điện thoại")
	// @Validation(title = CommonDefine.DeviceTxm.SIM_NUMBER, isHasSpecialChar =
	// false, alowrepeat = false)
	private String SimNumber;
	@ManyToOne
	@JoinColumn(name = "simid", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(name = "FK_lst_device_lst_sim_management_id"))
	@Transient
	private SimManagement Sim;
	@Validation(title = CommonDefine.DeviceTxm.SIM_NUMBER, alowrepeat = false)
	private Integer simid;
	@Column(name = "ReceivedConfig", nullable = false)
	private Integer ReceivedConfig = 0;
	private Integer SendingCommand;
	private Date LastetTimeReceivedConfig;
	private Date LastTimeSendingCommand;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	@Column(name = "CreatedFileConfig", nullable = false)
	private Integer CreatedFileConfig = 0;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer Lamp = 1;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer ACLock = 0;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer Reprint = 0;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer TaxiType;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	private Integer Pulse;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	@Column(name = "PulseUp", nullable = false)
	private Integer PulseUp = 1;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	@Column(name = "PulseDown", nullable = false)
	private Integer PulseDown = 1;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer DisplayError = 1;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer SendBackupToFTP = 0;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer AllowPrintTrip = 0;
	@Searchable(placehoder = "Tìm chi nhánh")
	@ManyToOne
	@JoinColumn(name = "AgentID", nullable = false)
	@Transient
	private Agent agent;
	@Validation(title = CommonDefine.DeviceTxm.AGENT_DEVICE, nullable = false)
	private Integer AgentID;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "SpeedGpsStartInCut", nullable = false)
	private Integer SpeedGpsStartInCut = 20;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "TimerInCut", nullable = false)
	private Integer TimerInCut = 300;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "SpeedGpsStopInCut", nullable = false)
	private Integer SpeedGpsStopInCut = 20;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "DeltaSpeedInCut", nullable = false)
	private Integer DeltaSpeedInCut = 12;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "SpeedGpsStartInKick", nullable = false)
	private Integer SpeedGpsStartInKick = 40;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "TimeStartInKick", nullable = false)
	private Integer TimeStartInKick = 240;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "TimeStopInKick", nullable = false)
	private Integer TimeStopInKick = 60;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "SpeedMeterStopInKick", nullable = false)
	private Integer SpeedMeterStopInKick = 20;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "DeltaSpeedStartInKick", nullable = false)
	private Integer DeltaSpeedStartInKick = 20;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "DeltaSpeedStopInKick", nullable = false)
	private Integer DeltaSpeedStopInKick = 10;
	@ApplyEditor(classNameEditor = "IntSpinnerEditor")
	@Column(name = "RepeatInKick", nullable = false)
	private Integer RepeatInKick = 3;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer AllowSendSms = 0;
	private Integer PaymentLimit = 20000;
	private Integer Option1 = 0;
	private Integer Option2 = 0;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer AlowTinyReceipt = 0;

	private Integer DeletedTemp;
	private String FirmwareVersion;
	private Integer FirmwareUpdated = 1;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	private Integer FirmwareRequest = 0;
	private Integer FirmwareCountdown = 5;
	private Integer VersionNumber;
	@Column(name = "MccFileReceived", nullable = false)
	private Integer MccFileReceived = 0;
	private Date MccFileReceivedTime;
	private Integer MccEventIndex = 0;

	private Date InsertDate;
	private Date UsingDate;
	private Date ExpireDate;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	@Column(name = "Login", nullable = false)
	private Integer Login = 0;
	@ApplyEditor(classNameEditor = "IntCheckEditor")
	@Column(name = "Active", nullable = false)
	private Integer isActive = 1;
	private Integer IsDelete = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getSimid() {
		return simid;
	}

	public void setSimid(Integer simid) {
		this.simid = simid;
	}

	public SimManagement getSim() {
		if (Sim == null) {
			SimManagementController controler = (SimManagementController) ControllerUtils
					.getController(SimManagementController.class);
			String query = "From SimManagement where id = ?";
			List<SimManagement> lstResult = controler.find(query, simid);
			if (lstResult != null && lstResult.size() > 0) {
				Sim = lstResult.get(0);
			}
		}
		return Sim;
	}

	public void setSim(SimManagement sim) {
		if (sim != null) {
			this.SimNumber = sim.getSimNumber();
			this.simid = sim.getId();
		} else {
			this.simid = null;
			this.SimNumber = null;
		}
		Sim = sim;
	}

	public String getDeviceCode() {
		return DeviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		DeviceCode = deviceCode;
	}

	public String getImei() {
		return Imei;
	}

	public void setImei(String imei) {
		Imei = imei;
	}

	public String getSimNumber() {
		return SimNumber;
	}

	public void setSimNumber(String simNumber) {
		SimNumber = simNumber;
	}

	public Integer getLogin() {
		return Login;
	}

	public Boolean getLoginStatus() {
		if (Login == 1) {
			return true;
		} else {
			return false;
		}
	}

	public void setLogin(Integer login) {
		Login = login;
	}

	public Integer getReceivedConfig() {
		return ReceivedConfig;
	}

	public void setReceivedConfig(Integer receivedConfig) {
		ReceivedConfig = receivedConfig;
	}

	public Integer getSendingCommand() {
		return SendingCommand;
	}

	public void setSendingCommand(Integer sendingCommand) {
		SendingCommand = sendingCommand;
	}

	public Date getLastetTimeReceivedConfig() {
		return LastetTimeReceivedConfig;
	}

	public void setLastetTimeReceivedConfig(Date lastetTimeReceivedConfig) {
		LastetTimeReceivedConfig = lastetTimeReceivedConfig;
	}

	public Date getLastTimeSendingCommand() {
		return LastTimeSendingCommand;
	}

	public void setLastTimeSendingCommand(Date lastTimeSendingCommand) {
		LastTimeSendingCommand = lastTimeSendingCommand;
	}

	public Integer getCreatedFileConfig() {
		return CreatedFileConfig;
	}

	public void setCreatedFileConfig(Integer createdFileConfig) {
		CreatedFileConfig = createdFileConfig;
	}

	public Boolean getActiveStatus() {
		if (isActive == 1) {
			return true;
		} else {
			return false;
		}
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getLamp() {
		return Lamp;
	}

	public Boolean getLampStatus() {
		if (Lamp != null && Lamp == 1) {
			return true;
		}
		return false;
	}

	public void setLamp(Integer lamp) {
		Lamp = lamp;
	}

	public Integer getACLock() {
		return ACLock;
	}

	public void setACLock(Integer aCLock) {
		ACLock = aCLock;
	}

	public Boolean getACLockStatus() {
		if (ACLock != null && ACLock == 1) {
			return true;
		} else {
			return false;
		}
	}

	public Integer getReprint() {
		return Reprint;
	}

	public void setReprint(Integer reprint) {
		Reprint = reprint;
	}

	public Integer getTaxiType() {
		return TaxiType;
	}

	public void setTaxiType(Integer taxiType) {
		TaxiType = taxiType;
	}

	public Integer getPulse() {
		return Pulse;
	}

	public void setPulse(Integer pulse) {
		Pulse = pulse;
	}

	public Integer getPulseUp() {
		return PulseUp;
	}

	public void setPulseUp(Integer pulseUp) {
		PulseUp = pulseUp;
	}

	public Integer getPulseDown() {
		return PulseDown;
	}

	public void setPulseDown(Integer pulseDown) {
		PulseDown = pulseDown;
	}

	public Integer getDisplayError() {
		return DisplayError;
	}

	public Boolean getDisplayErrorStatus() {

		if (DisplayError != null && DisplayError == 1) {
			return true;
		}
		return false;
	}

	public void setDisplayError(Integer displayError) {
		DisplayError = displayError;
	}

	public Integer getSendBackupToFTP() {
		return SendBackupToFTP;
	}

	public void setSendBackupToFTP(Integer sendBackupToFTP) {
		SendBackupToFTP = sendBackupToFTP;
	}

	public Integer getAllowPrintTrip() {
		return AllowPrintTrip;
	}

	public void setAllowPrintTrip(Integer allowPrintTrip) {
		AllowPrintTrip = allowPrintTrip;
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
		if (agent != null) {
			this.AgentID = agent.getId();
			this.agent = agent;
		}else {
			this.agent = null;
			this.AgentID = null;
		}
		
	}

	public Integer getAgentID() {
		return AgentID;
	}

	public void setAgentID(Integer agentID) {
		AgentID = agentID;
	}

	public Integer getSpeedGpsStartInCut() {
		return SpeedGpsStartInCut;
	}

	public void setSpeedGpsStartInCut(Integer speedGpsStartInCut) {
		SpeedGpsStartInCut = speedGpsStartInCut;
	}

	public Integer getTimerInCut() {
		return TimerInCut;
	}

	public void setTimerInCut(Integer timerInCut) {
		TimerInCut = timerInCut;
	}

	public Integer getSpeedGpsStopInCut() {
		return SpeedGpsStopInCut;
	}

	public void setSpeedGpsStopInCut(Integer speedGpsStopInCut) {
		SpeedGpsStopInCut = speedGpsStopInCut;
	}

	public Integer getDeltaSpeedInCut() {
		return DeltaSpeedInCut;
	}

	public void setDeltaSpeedInCut(Integer deltaSpeedInCut) {
		DeltaSpeedInCut = deltaSpeedInCut;
	}

	public Integer getSpeedGpsStartInKick() {
		return SpeedGpsStartInKick;
	}

	public void setSpeedGpsStartInKick(Integer speedGpsStartInKick) {
		SpeedGpsStartInKick = speedGpsStartInKick;
	}

	public Integer getTimeStartInKick() {
		return TimeStartInKick;
	}

	public void setTimeStartInKick(Integer timeStartInKick) {
		TimeStartInKick = timeStartInKick;
	}

	public Integer getTimeStopInKick() {
		return TimeStopInKick;
	}

	public void setTimeStopInKick(Integer timeStopInKick) {
		TimeStopInKick = timeStopInKick;
	}

	public Integer getSpeedMeterStopInKick() {
		return SpeedMeterStopInKick;
	}

	public void setSpeedMeterStopInKick(Integer speedMeterStopInKick) {
		SpeedMeterStopInKick = speedMeterStopInKick;
	}

	public Integer getDeltaSpeedStartInKick() {
		return DeltaSpeedStartInKick;
	}

	public void setDeltaSpeedStartInKick(Integer deltaSpeedStartInKick) {
		DeltaSpeedStartInKick = deltaSpeedStartInKick;
	}

	public Integer getDeltaSpeedStopInKick() {
		return DeltaSpeedStopInKick;
	}

	public void setDeltaSpeedStopInKick(Integer deltaSpeedStopInKick) {
		DeltaSpeedStopInKick = deltaSpeedStopInKick;
	}

	public Integer getRepeatInKick() {
		return RepeatInKick;
	}

	public void setRepeatInKick(Integer repeatInKick) {
		RepeatInKick = repeatInKick;
	}

	public Integer getAllowSendSms() {
		return AllowSendSms;
	}

	public void setAllowSendSms(Integer allowSendSms) {
		AllowSendSms = allowSendSms;
	}

	public Integer getPaymentLimit() {
		return PaymentLimit;
	}

	public void setPaymentLimit(Integer paymentLimit) {
		PaymentLimit = paymentLimit;
	}

	public Integer getOption1() {
		return Option1;
	}

	public void setOption1(Integer option1) {
		Option1 = option1;
	}

	public Integer getOption2() {
		return Option2;
	}

	public void setOption2(Integer option2) {
		Option2 = option2;
	}

	public Integer getAlowTinyReceipt() {
		return AlowTinyReceipt;
	}

	public void setAlowTinyReceipt(Integer alowTinyReceipt) {
		AlowTinyReceipt = alowTinyReceipt;
	}

	public String getInsertDate() {
		return toOnlyDateComponent(InsertDate);
	}

	public void setInsertDate(Date insertDate) {
		InsertDate = insertDate;
	}

	public Integer getDeletedTemp() {
		return DeletedTemp;
	}

	public void setDeletedTemp(Integer deletedTemp) {
		DeletedTemp = deletedTemp;
	}

	public String getFirmwareVersion() {
		return FirmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		FirmwareVersion = firmwareVersion;
	}

	public Integer getFirmwareUpdated() {
		return FirmwareUpdated;
	}

	public void setFirmwareUpdated(Integer firmwareUpdated) {
		FirmwareUpdated = firmwareUpdated;
	}

	public Integer getFirmwareRequest() {
		return FirmwareRequest;
	}

	public void setFirmwareRequest(Integer firmwareRequest) {
		FirmwareRequest = firmwareRequest;
	}

	public Integer getFirmwareCountdown() {
		return FirmwareCountdown;
	}

	public void setFirmwareCountdown(Integer firmwareCountdown) {
		FirmwareCountdown = firmwareCountdown;
	}

	public Integer getVersionNumber() {
		return VersionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		VersionNumber = versionNumber;
	}

	public Integer getMccFileReceived() {
		return MccFileReceived;
	}

	public void setMccFileReceived(Integer mccFileReceived) {
		MccFileReceived = mccFileReceived;
	}

	public Date getMccFileReceivedTime() {
		return MccFileReceivedTime;
	}

	public void setMccFileReceivedTime(Date mccFileReceivedTime) {
		MccFileReceivedTime = mccFileReceivedTime;
	}

	public Integer getMccEventIndex() {
		return MccEventIndex;
	}

	public void setMccEventIndex(Integer mccEventIndex) {
		MccEventIndex = mccEventIndex;
	}

	public Integer getIsDelete() {
		return IsDelete;
	}

	public void setIsDelete(Integer isDelete) {
		IsDelete = isDelete;
	}

	public Date getUsingDate() {
		return UsingDate;
	}

	public void setUsingDate(Date usingDate) {
		UsingDate = usingDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getExpireDate() {
		return ExpireDate;
	}

	public void setExpireDate(Date expireDate) {
		ExpireDate = expireDate;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Imei;
	}

	public String toOnlyDateComponent(Date date) {
		String output = "";
		try {
			if (date != null) {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
				output = dateformat.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
}