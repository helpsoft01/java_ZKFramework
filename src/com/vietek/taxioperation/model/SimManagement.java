package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.vietek.taxioperation.common.ApplyEditor;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.ColumnInfor;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "lst_sim_management")
@Where(clause = "isDelete = 0")
public class SimManagement extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue()
	private int id;
	private String CodeDevice;
	@ColumnInfor(label = "Số điện thoại", width = "3")
	@Searchable(placehoder = "Số điện thoại")
	@Validation(title = CommonDefine.DeviceTxm.SIM_NUMBER, isHasSpecialChar = false, nullable = false, alowrepeat = false)
	private String SimNumber;
	@ColumnInfor(label = "Hình thức trả", width = "3")
	@FixedCombobox(label = { "TRẢ TRƯỚC", "TRẢ SAU" }, value = { 1, 2 })
	private Integer SimType;
	@ManyToOne
	@JoinColumn(name = "SimProviderID", referencedColumnName = "CommonID", nullable = true)
	@ColumnInfor(label = "Nhà cung cấp", width = "2")
	private SimProvider simProvider;
	@ColumnInfor(label = "Ngày kích hoạt", width = "3")
	@Column(name = "SimActiveDate")
	@ApplyEditor(classNameEditor = "DateTimeEditor")
	private Timestamp simActiveDate;
	@ColumnInfor(label = "Seri SIM", width = "3")
	@Searchable(placehoder = "Seri SIM")
	private String seriSim;
	@ColumnInfor(label = "Nội dung", width = "2")
	@Searchable(placehoder = "Nội dung")
	private String SimLogs;
	@ColumnInfor(label = "Kích hoạt", width = "1")
	@Column(name = "Active")
	private Boolean isActive = true;
	@ColumnInfor(width = "")
	@Column(name = "isDelete")
	private Boolean isDelete = false;

	public String getCodeDevice() {
		return CodeDevice;
	}

	public void setCodeDevice(String codeDevice) {
		CodeDevice = codeDevice;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getSeriSim() {
		return seriSim;
	}

	public void setSeriSim(String seriSim) {
		this.seriSim = seriSim;
	}

	public Integer getSimType() {
		return SimType;
	}

	public void setSimType(Integer simType) {
		SimType = simType;
	}

	public SimProvider getSimProvider() {
		return simProvider;
	}

	public void setSimProvider(SimProvider simProvider) {
		this.simProvider = simProvider;
	}

	public Timestamp getSimActiveDate() {
		return simActiveDate;
	}

	public String getSimActiveDateString() {
		return toOnlyDateComponent(simActiveDate);
	}

	public void setSimActiveDate(Timestamp simActiveDate) {
		this.simActiveDate = simActiveDate;
	}

	public String getSimLogs() {
		return SimLogs;
	}

	public void setSimLogs(String simLogs) {
		SimLogs = simLogs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSimNumber() {
		return SimNumber;
	}

	public void setSimNumber(String simNumber) {
		SimNumber = simNumber;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return SimNumber;
	}

	public String toOnlyDateComponent(Date date) {
		String output = "";
		try {
			SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
			if (date != null) {
				output = dateformat.format(date);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return output;
	}
}
