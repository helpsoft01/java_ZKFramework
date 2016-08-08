package com.vietek.taxioperation.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;
@Entity
@Table(name = "lst_callCenter")
public class CallCenter extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue
	private int id;
	@Searchable(placehoder = "Tên tổng đài")
	private String callname;
	@Validation(title = CommonDefine.CallCenterDefine.PHONE_CALLCENTER, alowrepeat = false)
	@Searchable(placehoder = "Số điện thoại")
	private String phonenmber;
	private String node;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCallname() {
		return callname;
	}
	public void setCallname(String callname) {
		this.callname = callname;
	}
	
	public String getPhonenmber() {
		return phonenmber;
	}
	public void setPhonenmber(String phonenmber) {
		this.phonenmber = phonenmber;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
