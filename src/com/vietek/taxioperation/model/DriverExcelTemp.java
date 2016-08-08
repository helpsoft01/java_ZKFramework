package com.vietek.taxioperation.model;

import java.util.Date;

public class DriverExcelTemp {
	private int agentid;
	private int groupid;
	private String drivercode;
	private String drivername;
	private int staffcard;
	private String driverlicense;
	private String licensetype;
	private int licensetypeid;
	private Date registerdate;
	private Date expiatedate;
	private int timelimit;
	private String phonenumber;
	private String phoneoffice;
	private String identitycard;
	private Date birthday;
	private String sexname;
	private int sexid;
	private String bloodtype;
	private int bloodtypeid;

	public int getBloodtypeid() {
		return bloodtypeid;
	}

	public void setBloodtypeid(int bloodtypeid) {
		this.bloodtypeid = bloodtypeid;
	}

	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}

	public String getBloodtype() {
		return bloodtype;
	}

	public int getAgentid() {
		return agentid;
	}

	public void setAgentid(int agentid) {
		this.agentid = agentid;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getDrivercode() {
		return drivercode;
	}

	public void setDrivercode(String drivercode) {
		this.drivercode = drivercode;
	}

	public int getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(int timelimit) {
		this.timelimit = timelimit;
	}

	public String getDrivername() {
		return drivername;
	}

	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}

	public int getLicensetypeid() {
		return licensetypeid;
	}

	public void setLicensetypeid(int licensetypeid) {
		this.licensetypeid = licensetypeid;
	}

	public int getStaffcard() {
		return staffcard;
	}

	public void setStaffcard(int staffcard) {
		this.staffcard = staffcard;
	}

	public String getDriverlicense() {
		return driverlicense;
	}

	public void setDriverlicense(String driverlicense) {
		this.driverlicense = driverlicense;
	}

	public String getLicensetype() {
		return licensetype;
	}

	public void setLicensetype(String licensetype) {
		this.licensetype = licensetype;
	}

	public Date getRegisterdate() {
		return registerdate;
	}

	public void setRegisterdate(Date registerdate) {
		this.registerdate = registerdate;
	}

	public Date getExpiatedate() {
		return expiatedate;
	}

	public void setExpiatedate(Date expiatedate) {
		this.expiatedate = expiatedate;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getPhoneoffice() {
		return phoneoffice;
	}

	public void setPhoneoffice(String phoneoffice) {
		this.phoneoffice = phoneoffice;
	}

	public String getIdentitycard() {
		return identitycard;
	}

	public void setIdentitycard(String identitycard) {
		this.identitycard = identitycard;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSexname() {
		return sexname;
	}

	public void setSexname(String sexname) {
		this.sexname = sexname;
	}

	public int getSexid() {
		return sexid;
	}

	public void setSexid(int sexid) {
		this.sexid = sexid;
	}
}