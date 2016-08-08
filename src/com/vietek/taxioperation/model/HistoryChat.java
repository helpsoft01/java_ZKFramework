package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "history_chat")
public class HistoryChat extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8798826367586932547L; 
	@Id
	@GeneratedValue
	private int id;
	private String sendUser;
	private String reUser;
	private String content;
	private Timestamp time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSendUser() {
		return sendUser;
	}
	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
	public String getReUser() {
		return reUser;
	}
	public void setReUser(String reUser) {
		this.reUser = reUser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
