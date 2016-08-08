package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.vietek.tracking.ui.model.TreePlace;

@Entity
@Table(name = "lst_common", schema = "txm_tracking")
@Where(clause = "CodeType = 'PLACETYPE'")
public class PointGroup extends AbstractModel implements TreePlace {
	@Id
	@GeneratedValue
	@Column(name = "CommonID")
	private int id;
	private String Code;
	private String Name;
	private String Value;
	private String type;
	private Timestamp LastTime;
	private Integer CanEdit;
	private Integer ParentID;
	private String CodeType;
	private String Decription;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public Timestamp getLastTime() {
		return LastTime;
	}
	public void setLastTime(Timestamp lastTime) {
		LastTime = lastTime;
	}
	public Integer getCanEdit() {
		return CanEdit;
	}
	public void setCanEdit(Integer canEdit) {
		CanEdit = canEdit;
	}
	public Integer getParentID() {
		return ParentID;
	}
	public void setParentID(Integer parentID) {
		ParentID = parentID;
	}
	public String getCodeType() {
		return CodeType;
	}
	public void setCodeType(String codeType) {
		CodeType = codeType;
	}
	public String getDecription() {
		return Decription;
	}
	public void setDecription(String decription) {
		Decription = decription;
	}
	@Override
	public int getPlaceId() {
		// TODO Auto-generated method stub
		return id;
	}
	@Override
	public String getPlaceName() {
		return Name;
	}
	@Override
	public String getSrc() {
		return Value.replace("../../Resource/Image","./themes/images");
	}
	
	
}
