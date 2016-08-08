package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

@Entity
@Table(name = "cat_channel")
public class ChannelTms extends AbstractModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true, nullable = false)
	@Validation(title = CommonDefine.Channel.CODE_CHANNEL, nullable = false, maxLength = 10)
	@Searchable(placehoder = "Nhập mã kênh")
	private String value;
	@Column(columnDefinition = "NVARCHAR(Max)")
	@Searchable(placehoder = "Nhập tên kênh")
	@Validation(title = CommonDefine.Channel.NAME_CHANNEL, nullable = false, maxLength = 15)
	private String name;
	@ManyToOne
	@Searchable(placehoder = "Tìm tổng đài")
	@JoinColumn(name = "switchboard_id", nullable = false)
	@Validation(nullable = false)
	private SwitchboardTMS switchboardtms;
	private Double longitude;
	private Double latitude;
	@Searchable(placehoder = "kích hoạt")
	private Boolean isActive = true;
	private Timestamp created = new Timestamp(System.currentTimeMillis());
	private Integer createBy;
	private Timestamp updated = new Timestamp(System.currentTimeMillis());
	private Integer updateBy;

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

	public SwitchboardTMS getSwitchboardtms() {
		return switchboardtms;
	}

	public void setSwitchboardtms(SwitchboardTMS switchboardtms) {
		this.switchboardtms = switchboardtms;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public void setCreateBy(int createBy) {
		this.createBy = createBy;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public void setUpdateBy(int updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Integer getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "" + value + " - " + name;
	}

}