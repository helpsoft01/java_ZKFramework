package com.vietek.taxioperation.chat;

import java.sql.Timestamp;

import com.vietek.taxioperation.model.SysUser;


public class Message {

	private SysUser sender;

	private SysUser receiver;

	private String message;
	
	private Timestamp time;
	
	private int type;
	
	private Boolean isOnline;

	public Message(SysUser sender, SysUser receiver, String message, long date, int type) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = message.trim().replaceAll("\\s+", " ");
		this.time = new Timestamp(date);
		this.type = type;
	}
	
	public Message(SysUser sender, boolean isOnline){
		this.sender = sender;
		this.isOnline = isOnline;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public Boolean getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}


}
