package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "lst_common", schema = "txm_tracking")
@Where(clause = "CodeType = 'REQUESTTYPE'")
public class RequestType extends AbstractModel {
	@Id
	@GeneratedValue
	@Column(name = "CommonID")
	private int id;
	@Column(name = "Code")
	private String code;
	@Column(name = "Name")
	private String name;
	@Column(name = "Value")
	private String value;
	@Column(name = "Type")
	private String type;
	@Column(name = "LastTime")
	private Timestamp lasttime;
	@Column(name = "CanEdit")
	private Integer canedit;
	@Column(name = "ParentID")
	private Integer parentid;
	@Column(name = "CodeType")
	private String codetype;
	@Column(name = "Decription")
	private String decription;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getLasttime() {
		return lasttime;
	}

	public void setLasttime(Timestamp lasttime) {
		this.lasttime = lasttime;
	}

	public int getCanedit() {
		return canedit;
	}

	public void setCanedit(int canedit) {
		this.canedit = canedit;
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public String getCodetype() {
		return codetype;
	}

	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	@Override
	public String toString() {
		return name;
	}

}
