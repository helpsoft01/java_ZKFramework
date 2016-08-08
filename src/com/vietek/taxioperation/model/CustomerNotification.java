package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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

@Entity
@Table(name = "customer_notification")
public class CustomerNotification extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private int id;
	// ten thong bao (header)
	@Searchable(placehoder = "Tên thông báo")
	@Validation(title = CommonDefine.CustomerNotification.NOTICE_NAME, nullable = false)
	private String name;
	// tieu de thong bao (subject)
	@Searchable(placehoder = "Tiêu đề")
	@Validation(title = CommonDefine.CustomerNotification.NOTICE_OBJECT, nullable = false)
	private String subject;
	// noi dung thong bao 
	@Searchable(placehoder = "Nội dung")
	private String content;
	// thoi gian bat dau
	@Validation(title = CommonDefine.CustomerNotification.NOTICE_F_DATE, nullable = false)
	private Timestamp begindate;
	// thoi gian ket thuc
	@Validation(title = CommonDefine.CustomerNotification.NOTICE_T_DATE, nullable = false)
	private Timestamp finishdate;
	// loai thong bao
	@Searchable(placehoder = "Loại thông báo")
	@Validation(title = CommonDefine.CustomerNotification.NOTICE_TYPE, nullable = false)
	@ManyToOne
	@JoinColumn(name = "typeid", referencedColumnName = "CommonID", nullable = true)
	private NotificationType type;	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "customer_notification_mapping", joinColumns = {
			@JoinColumn(name = "notice_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "user_id", nullable = false, updatable = false) })
	// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	/* Xe dang ky don */
	private Set<SysUser> customerSets = new HashSet<SysUser>();
	// kich hoat
	private Boolean active;
	
	
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
	public Timestamp getBegindate() {
		return begindate;
	}
	public void setBegindate(Timestamp begindate) {
		this.begindate = begindate;
	}
	public Timestamp getFinishdate() {
		return finishdate;
	}
	public void setFinishdate(Timestamp finishdate) {
		this.finishdate = finishdate;
	}
	public NotificationType getType() {
		return type;
	}
	public void setType(NotificationType type) {
		this.type = type;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Set<SysUser> getCustomerSets() {
		return customerSets;
	}
	public void setCustomerSets(Set<SysUser> customerSets) {
		this.customerSets = customerSets;
	}
	
}
