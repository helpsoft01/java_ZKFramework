package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//@Entity
//@Table(name = "user")
public class User extends AbstractModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5840095994494133447L;

	@Id
	@GeneratedValue
	private int id;
	private String fullName;
	private String accountName;
	private String password;
	private String description;
	private Boolean isActive = true;
	private String extNumber;
	private String email;
	private String phoneNumber;
	private String address;
	private Timestamp birthDay;

	

	// @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	// @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name =
	// "User_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
	// @JoinColumn(name = "Role_ID", nullable = false, updatable = false) })
	// private Set<Role> roles = new HashSet<Role>();

	public String getExtNumber() {
		return extNumber;
	}

	public void setExtNumber(String extNumber) {
		this.extNumber = extNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	// public Set<Role> getRoles() {
	// return roles;
	// }
	//
	// public void setRoles(Set<Role> roles) {
	// this.roles = roles;
	// }

	public String getEmail() {
		return email;
	}

	

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Timestamp getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Timestamp birthDay) {
		this.birthDay = birthDay;
	}

	@Override
	public String toString() {
		// return fullName + " (" + accountName + ")";
		return fullName + "(" + extNumber + ")";
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
