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

/**
 * 
 * @author Viet Ha Ca
 *
 */
@Entity
@Table(name = "user_chat_log")
public class UserChatLog extends AbstractModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	@ManyToOne
	@JoinColumn(name = "sender")
	private SysUser sender;

	@ManyToOne
	@JoinColumn(name = "receiver")
	private SysUser receiver;

	@Column(name = "dateCreate")
	private Timestamp dateCreate;

	@Column(name = "message")
	private String message;

	@Column(name = "type")
	private int type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SysUser getSender() {
		return sender;
	}

	public void setSender(SysUser sender) {
		this.sender = sender;
	}

	public SysUser getReceiver() {
		return receiver;
	}

	public void setReceiver(SysUser receiver) {
		this.receiver = receiver;
	}

	public Timestamp getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Timestamp dateCreate) {
		this.dateCreate = dateCreate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
