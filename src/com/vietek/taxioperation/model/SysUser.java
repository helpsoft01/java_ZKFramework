package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

/**
 * 
 * @author VuD
 * 
 */

@Entity
@Table(name = "ad_user")
public class SysUser extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false, unique = true)
	@Validation(nullable = false)
	@Searchable(placehoder = "Mã người dùng")
	private String value;
	@Column(nullable = false)
	@Validation(nullable = false)
	@Searchable(placehoder = "Tên người dùng")
	private String name;
	@Column(nullable = false, unique = true)
	@Validation(nullable = false)
	@Searchable(placehoder = "Tên đăng nhập")
	private String user;
	private String password;
	@ManyToOne
	@JoinColumn(name = "company_id")
	private SysCompany sysCompany;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ad_group_user", joinColumns = {
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "group_id", nullable = false, updatable = false) })
	private Set<SysGroup> sysGroup = new HashSet<SysGroup>();
	@ManyToOne
	@JoinColumn(name = "switchboard_id")
	private SwitchboardTMS switchboard;
	@ManyToOne
	@JoinColumn(name = "channel_id")
	private ChannelTms channel;
	private String extNumber;
	private Boolean isActive = true;
	private Timestamp created;
	private Integer createBy;
	private Timestamp updated;
	private Integer updateBy;
	private Boolean isRadio;
	private Boolean isTelephonist;
	private Boolean isMobiler;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ad_rule_user", joinColumns = {
			@JoinColumn(name = "ad_user_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "ad_rule_id", nullable = false, updatable = false) })

	private Set<SysRule> setSysRule = new HashSet<>();
	private Double mapLat = CommonDefine.GoogleMap.MAP_LAT;
	private Double mapLng = CommonDefine.GoogleMap.MAP_LNG;
	private Integer mapZoom = CommonDefine.GoogleMap.MAP_ZOOM;
	private Integer mapType = CommonDefine.GoogleMap.MAP_TYPE;

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

	public String getUserName() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SysCompany getSysCompany() {
		return sysCompany;
	}

	public void setSysCompany(SysCompany sysCompany) {
		this.sysCompany = sysCompany;
	}

	public Set<SysGroup> getSysGroup() {
		return sysGroup;
	}

	public void setSysGroup(HashSet<SysGroup> sysGroup) {
		this.sysGroup = sysGroup;
	}

	public Boolean getIsRadio() {
		return isRadio;
	}

	public void setIsRadio(Boolean isRadio) {
		this.isRadio = isRadio;
	}

	public Boolean getIsTelephonist() {
		return isTelephonist;
	}

	public void setIsTelephonist(Boolean isTelephonist) {
		this.isTelephonist = isTelephonist;
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

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public Integer getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}

	public SwitchboardTMS getSwitchboard() {
		return switchboard;
	}

	public void setSwitchboard(SwitchboardTMS switchboard) {
		this.switchboard = switchboard;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setSysGroup(Set<SysGroup> sysGroup) {
		this.sysGroup = sysGroup;
	}

	public ChannelTms getChannel() {
		return channel;
	}

	public void setChannel(ChannelTms channel) {
		this.channel = channel;
	}

	public String getExtNumber() {
		return extNumber;
	}

	public void setExtNumber(String extNumber) {
		this.extNumber = extNumber;
	}

	public Boolean getIsMobiler() {
		return isMobiler;
	}

	public void setIsMobiler(Boolean isMobiler) {
		this.isMobiler = isMobiler;
	}

	public Set<SysRule> getSetSysRule() {
		return setSysRule;
	}

	public void setSetSysRule(Set<SysRule> setSysRule) {
		this.setSysRule = setSysRule;
	}

	public void setSetSysRule(HashSet<SysRule> setSysRule) {
		this.setSysRule = setSysRule;
	}

	public Double getMapLat() {
		return mapLat;
	}

	public void setMapLat(Double mapLat) {
		this.mapLat = mapLat;
	}

	public Double getMapLng() {
		return mapLng;
	}

	public void setMapLng(Double mapLng) {
		this.mapLng = mapLng;
	}

	public Integer getMapZoom() {
		return mapZoom;
	}

	public void setMapZoom(Integer mapZoom) {
		this.mapZoom = mapZoom;
	}

	public Integer getMapType() {
		return mapType;
	}

	public void setMapType(Integer mapType) {
		this.mapType = mapType;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof SysUser) {
			return ((SysUser) obj).getId() == this.getId() ? true : false;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		if (id != 0) {
			return id;
		} else {
			return super.hashCode();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return user + "_" + name;
	}

}