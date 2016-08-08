package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lst_config", schema = "txm_tracking")

public class DeviceConfig extends AbstractModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "ConfigID")
	private int id;
	@Column(name = "ConfigName")	
	private String configname;
	@Column(name = "ConfigCode")	
	private String configcode;
	@Column(name = "DefaultValue")
	private String configvalue;
	@Column(name = "ParentID")
	private int parentid;
	@Column(name = "Type")
	private String configtype;
	@Column(name = "CanEdit")
	private int canedit;
	
	public int getConfigid() {
		return id;
	}
	public void setConfigid(int configid) {
		this.id = configid;
	}
	public String getConfigname() {
		return configname;
	}
	public void setConfigname(String configname) {
		this.configname = configname;
	}
	public String getConfigcode() {
		return configcode;
	}
	public void setConfigcode(String configcode) {
		this.configcode = configcode;
	}
	public String getConfigvalue() {
		return configvalue;
	}
	public void setConfigvalue(String configvalue) {
		this.configvalue = configvalue;
	}	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentid() {
		return parentid;
	}
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	public String getConfigtype() {
		return configtype;
	}
	public void setConfigtype(String configtype) {
		this.configtype = configtype;
	}
	public int getCanedit() {
		return canedit;
	}
	public void setCanedit(int canedit) {
		this.canedit = canedit;
	}
	
	public DeviceConfig(){
		
	}
	public DeviceConfig(String configname, String configcode){
		this.configcode = configcode;
		this.configname = configname;
	}
	
	public DeviceConfig(String configname, String configcode, String configvalue, String configtype){
		this.configcode = configcode;
		this.configname = configname;
		this.configvalue = configvalue;
		this.configtype = configtype;
	}
	
	public int getConfigtypeval(String type){
		int val = 0;
		switch (type) {
		case "int":
			val = 0;
			break;
		case "string":
			val = 1;
			break;
		case "boolean":
			val = 2;
			break;
		case "real":
			val = 3;
			break;			
		case "array":
			val = 4;
			break;
		case "object":
			val = 5;
			break;
		default:
			val = 0;
			break;
		}
		return val;
	}
}