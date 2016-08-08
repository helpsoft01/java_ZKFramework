package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vietek.taxioperation.common.AnnonationLinkedTable;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;

/**
 * 
 * @author VuD
 * 
 */

@Entity
@Table(name = "ad_menu")
public class SysMenu extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static public int TYPE_SETTING = 1;
	static public int TYPE_NAVBAR = 2;
	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false, unique = true)
	@Searchable(placehoder = "Tên nhóm")
	@Validation(nullable = false)
	private String name; // store the key, will be translated in localization
							// file

	@ManyToOne
	@JoinColumn(name = "function_id")
	@Searchable(placehoder = "Chức năng")
	private SysFunction function;
	private Boolean isActive = true;
	private int type;// Loai Menu: 1: Setting - 2: Nav bar
	@AnnonationLinkedTable(modelClazz = "SysMenu", displayFieldName = "name")
	private Integer parentId = 0;
	private Integer sequence = 0;
	private Timestamp created;
	private Integer createBy;
	private Timestamp updated;
	private Integer updateBy;

	public static int getTYPE_SETTING() {
		return TYPE_SETTING;
	}

	public static void setTYPE_SETTING(int tYPE_SETTING) {
		TYPE_SETTING = tYPE_SETTING;
	}

	public static int getTYPE_NAVBAR() {
		return TYPE_NAVBAR;
	}

	public static void setTYPE_NAVBAR(int tYPE_NAVBAR) {
		TYPE_NAVBAR = tYPE_NAVBAR;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SysFunction getFunction() {
		return function;
	}

	public void setFunction(SysFunction function) {
		this.function = function;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SysMenu) {
			if (((SysMenu) obj).getId() == id) {
				return true;
			}
		}
		return false;
	}

	public synchronized void updateValue(SysMenu updatedMsg) {
		this.name = updatedMsg.getName();
		this.function = updatedMsg.getFunction();
		this.isActive = updatedMsg.getIsActive();
		this.parentId = updatedMsg.getParentId();
		this.sequence = updatedMsg.getSequence();
		this.type = updatedMsg.getType();
	}

}
