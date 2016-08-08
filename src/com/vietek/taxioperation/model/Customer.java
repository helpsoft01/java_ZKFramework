package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.Validation;
import com.vietek.taxioperation.controller.CustomerController;

@Entity
@Table(name = "customer")
public class Customer extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9016313557920472164L;

	public Customer() {
		super();
	}

	@Id
	@GeneratedValue
	private int id;
	@Searchable(placehoder = "Nhập tên khách hàng")
	@Validation(title = CommonDefine.Customer.NAME_CUSTOMER, maxLength = 255, nullable = false, isHasSpecialChar = true)
	private String name;
	@Column(nullable = false)
	@Searchable(placehoder = "Nhập số điện thoại")
	@Validation(title = CommonDefine.Customer.PHONE_CUSTOMER, nullable = false, maxLength = 11, regex = CommonDefine.NUM_PATTERN)
	private String phoneNumber;
	@Validation(title = CommonDefine.Customer.EMAIL_CUSTOMER, isEmail = true)
	private String email;
	private String address;
	private String address2;
	private String address3;
	private Double addressLat = 0.0;
	private Double addressLng = 0.0;
	private Double address2Lat = 0.0;
	private Double address2Lng = 0.0;
	private Double address3Lat = 0.0;
	private Double address3Lng = 0.0;
	private long totalCall;
	
	@Validation(title = CommonDefine.Customer.NOTE_CUSTOMER, isHasSpecialChar = false)
	private String note;
	private Boolean isVIP = false;
	private Boolean isFrequently = false;
	private Boolean isActive = true;
	private String password;
	private String verifyCode;
	private Timestamp verifyTimeout;
	private Integer status = 0; // 1 : inActive ; 0: Active
	private String regId;
	private int resetPasswordCount = 0;
	
	// Add value calculate total call and total order success
	private int totalCallOrder;
	private int totalSuccessOrder;
	private Timestamp lastTimeCall;
	@Column
	@Lob
	private byte[] avatar;

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Timestamp getVerifyTimeout() {
		return verifyTimeout;
	}

	public void setVerifyTimeout(Timestamp verifyTimeout) {
		this.verifyTimeout = verifyTimeout;
	}

	@FixedCombobox(label = { "Nam", "Nữ", "Khác" }, value = { 0, 1, 2 })
	private Integer sex;
	private Integer age;

	@FixedCombobox(label = { "Vùng Trung du và miền núi phía Bắc", "Vùng đồng bằng sông Hồng",
			"Vùng Bắc Trung Bộ và Duyên hải miền Trung", "Vùng Tây Nguyên", "Vùng Đông Nam Bộ",
			"Vùng đồng bằng sông Cửu Long" }, value = { 0, 1, 2, 3, 4, 5 })
	private Integer region;

	private String favour;

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

	public long getTotalCall() {
		return totalCall;
	}

	public void setTotalCall(long totalCall) {
		this.totalCall = totalCall;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public Double getAddressLat() {
		return addressLat;
	}

	public void setAddressLat(Double addressLat) {
		this.addressLat = addressLat;
	}

	public Double getAddressLng() {
		return addressLng;
	}

	public void setAddressLng(Double addressLng) {
		this.addressLng = addressLng;
	}

	public Double getAddress2Lat() {
		return address2Lat;
	}

	public void setAddress2Lat(Double address2Lat) {
		this.address2Lat = address2Lat;
	}

	public Double getAddress2Lng() {
		return address2Lng;
	}

	public void setAddress2Lng(Double address2Lng) {
		this.address2Lng = address2Lng;
	}

	public Double getAddress3Lat() {
		return address3Lat;
	}

	public void setAddress3Lat(Double address3Lat) {
		this.address3Lat = address3Lat;
	}

	public Double getAddress3Lng() {
		return address3Lng;
	}

	public void setAddress3Lng(Double address3Lng) {
		this.address3Lng = address3Lng;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getIsVIP() {
		return isVIP;
	}

	public void setIsVIP(Boolean isVIP) {
		this.isVIP = isVIP;
	}

	public Boolean getIsFrequently() {
		return isFrequently;
	}

	public void setIsFrequently(Boolean isFrequently) {
		this.isFrequently = isFrequently;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return name + " (" + phoneNumber + ")";
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public String getFavour() {
		return favour;
	}

	public void setFavour(String favour) {
		this.favour = favour;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getResetPasswordCount() {
		return resetPasswordCount;
	}

	public void setResetPasswordCount(int resetPasswordCount) {
		this.resetPasswordCount = resetPasswordCount;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}
	

	public int getTotalCallOrder() {
		return totalCallOrder;
	}

	public void setTotalCallOrder(int totalCallOrder) {
		this.totalCallOrder = totalCallOrder;
	}

	public int getTotalSuccessOrder() {
		return totalSuccessOrder;
	}

	public void setTotalSuccessOrder(int totalSuccessOrder) {
		this.totalSuccessOrder = totalSuccessOrder;
	}
	
	public Timestamp getLastTimeCall() {
		return lastTimeCall;
	}

	public void setLastTimeCall(Timestamp lastTimeCall) {
		this.lastTimeCall = lastTimeCall;
	}

	@Override
	public void save() {
		super.save();
		CustomerController.reload(this);
	}

}
