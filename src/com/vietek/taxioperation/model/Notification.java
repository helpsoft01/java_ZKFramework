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
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.vietek.taxioperation.common.FixedCombobox;

@Entity
@Table(name = "notification")
public class Notification extends AbstractModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	private String subject;
	@Column(nullable = false)
	private String content;
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "phoneNumber")
	private Set<Customer> listCustomer = new HashSet<Customer>();
	@FixedCombobox(label = { "All app", "Android apps", "IOS apps" }, value = { 0, 1, 2 })
	private Integer type; // 0 : All ; 1: Android; 2: IOs
	private Timestamp timeSend;
	private String note;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Timestamp getTimeSend() {
		return timeSend;
	}

	public void setTimeSend(Timestamp timeSend) {
		this.timeSend = timeSend;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Set<Customer> getListCustomer() {
		return listCustomer;
	}

	public void setListCustomer(Set<Customer> listCustomer) {
		this.listCustomer = listCustomer;
	}

}
