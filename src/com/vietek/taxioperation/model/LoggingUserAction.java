package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vietek.taxioperation.common.ApplyEditor;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.controller.SysUserController;
import com.vietek.taxioperation.util.ControllerUtils;

@Entity
@Table(name = "log_user_action", 
		indexes = { @Index(columnList = "timelog", name = "timelog"), 
					@Index(columnList = "action, timelog", name = "action_time")})
public class LoggingUserAction  extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int id;
	@Column(columnDefinition = "model dang tuong tac")
	
	@Searchable(placehoder = "")
	private String modelname;
	
	@Searchable(placehoder = "")
	private String formname;
	
	@Searchable(placehoder = "")
	@FixedCombobox(label = { "Login", "Logout", "Sửa", "Thêm", "Xóa", "Truy cập" }, 
					value = { 0, 1, 2, 3, 4, 5 })
	@Column(columnDefinition = "loai hoat dong: 0 = login, 1 = logout, 2 = update, 3 = insert")
	private Integer action;		
	
	@Searchable(placehoder = "")
	@Column(columnDefinition = "danh sach cac truong du lieu")
	private String fields;
	
	@Searchable(placehoder = "")
	@Column(columnDefinition = "du lieu sau thay doi, tuong ung voi tung truong, phan biet bang dau ';'")
	private String vals;
	
	@Searchable(placehoder = "")
	@Column(columnDefinition = "ten may client")
	private String hostname;
	
	@Searchable(placehoder = "")
	@Column(columnDefinition = "ip cua may client")	
	private String ipaddress;
	
	@ApplyEditor(classNameEditor = "DateTimeEditor")
	@Column(columnDefinition = "thoi diem ghi nhan")
	private Timestamp timelog;
	@Transient
	private SysUser user;
	private Integer userid;	
	
	
	public Integer getUserid() {
		return userid;
	}


	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	
	public String getModelname() {
		return modelname;
	}


	public void setModelname(String modelname) {
		this.modelname = modelname;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Integer getAction() {
		return action;
	}


	public void setAction(int action) {
		this.action = action;
	}


	public String getFormname() {
		return formname;
	}


	public void setFormname(String formname) {
		this.formname = formname;
	}


	public String getFieldsdetail() {
		return fields;
	}


	public void setFieldsdetail(String fields) {
		this.fields = fields;
	}


	public String getValuesdetail() {
		return vals;
	}


	public void setValuesdetail(String values) {
		this.vals = values;
	}


	public Timestamp getTimelog() {
		return timelog;
	}


	public void setTimelog(Timestamp timelog) {
		this.timelog = timelog;
	}


	public LoggingUserAction() {
		// TODO Auto-generated constructor stub
	}


	public String getHostname() {
		return hostname;
	}


	public void setHostname(String hostname) {
		this.hostname = hostname;
	}


	public String getIpaddress() {
		return ipaddress;
	}


	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}


	public SysUser getUser() {
		if (user == null) {
			SysUserController controler = (SysUserController) ControllerUtils
					.getController(SysUserController.class);
			String query = "From SysUser where id = ?";
			List<SysUser> lstResult = controler.find(query, userid);
			if (lstResult != null && lstResult.size() > 0) {
				user = lstResult.get(0);
			}
		}
		return user;
	}


	public void setUser(SysUser user) {
		if (user != null) {
			this.userid = user.getId();
			this.user = null;
		} else {
			this.userid = null;
			this.user = null;
		}
	}
}
