package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.util.MapUtils;

@Entity
@Table(name = "request_baggagefinding", schema = "txm_tracking")
public class RequestBaggageFinding extends AbstractModel {

	@Id
	@GeneratedValue
	@Column(name = "RequestID")
	private int id;
	@ManyToOne
	@JoinColumn(name = "AgentID", referencedColumnName = "AgentID", foreignKey = @ForeignKey(name = "FK_request_baggagefinding_lst_agent_AgentID"))
	private Agent agent;
	@Searchable(placehoder = "Tên khách hàng")
	private String CustomerName;
	@Searchable(placehoder = "Số điện thoại")
	private String PhoneNumber;
	private String CustomerAddr;
	private String LicensePlate;
	@FixedCombobox(label = { "4 Chỗ", "7 Chỗ" }, value = { 23, 66 })
	private Integer SeatType;
	private String VehicleType;
	private String VehicleNumber;
	private String DriverName;
	private Integer PaymentType;
	private Integer TripType;
	private Integer Money;
	private String CardNumber;
	private Timestamp BeginTime;
	private Timestamp FinishTime;
	private String BeginAddr;
	private String FinishAddr;
	private String Address1;
	private String Address2;
	private String BeginLoc;
	private String FinishLoc;
	private String Addr1Loc;
	private String Addr2Loc;
	@Searchable(placehoder = "Trạng thái")
	@ManyToOne
	@JoinColumn(name = "RequestProcess", referencedColumnName = "CommonID", foreignKey = @ForeignKey(name = "FK_request_baggagefinding_lst_common_CommonID"), nullable = true)
	private RequestType Requesttype;
	private String RequestContent;
	private Integer CusPosition;
	private Integer CusNumber;
	private String OtherDecription;
	@Searchable(placehoder = "Ngày nhận yêu cầu")
	private Timestamp ReceiveDate;
	private Timestamp ProcessDate;
	private Timestamp Timelog;
	@FixedCombobox(label = {}, value = {})
	private Integer UpdateAgainState;
	private String FeedbackResult;
	@Transient
	private LatLng latlngbegin;
	@Transient
	private LatLng latlngstop;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public RequestType getRequesttype() {
		return Requesttype;
	}

	public void setRequesttype(RequestType requesttype) {
		Requesttype = requesttype;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public String getCustomerAddr() {
		return CustomerAddr;
	}

	public void setCustomerAddr(String customerAddr) {
		CustomerAddr = customerAddr;
	}

	public String getLicensePlate() {
		return LicensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		LicensePlate = licensePlate;
	}

	public Integer getSeatType() {
		return SeatType;
	}

	public String getFeedbackResult() {
		if (FeedbackResult == null) {
			FeedbackResult = "";
		}
		return FeedbackResult;
	}

	public void setFeedbackResult(String feedbackResult) {
		FeedbackResult = feedbackResult;
	}

	public void setSeatType(Integer seatType) {
		SeatType = seatType;
	}

	public String getVehicleType() {
		return VehicleType;
	}

	public LatLng getLatlngbegin() {
		if (latlngbegin == null) {
			latlngbegin = MapUtils.convertAddresstoLatLng(BeginAddr);
		}
		return latlngbegin;
	}

	public void setLatlngbegin(LatLng latlngbegin) {
		this.latlngbegin = latlngbegin;
	}

	public LatLng getLatlngstop() {
		if (latlngstop == null) {
			latlngstop = MapUtils.convertAddresstoLatLng(FinishAddr);
		}
		return latlngstop;
	}

	public void setLatlngstop(LatLng latlngstop) {
		this.latlngstop = latlngstop;
	}

	public void setVehicleType(String vehicleType) {
		VehicleType = vehicleType;
	}

	public String getVehicleNumber() {
		return VehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		VehicleNumber = vehicleNumber;
	}

	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	public Integer getPaymentType() {
		return PaymentType;
	}

	public void setPaymentType(Integer paymentType) {
		PaymentType = paymentType;
	}

	public Integer getTripType() {
		return TripType;
	}

	public void setTripType(Integer tripType) {
		TripType = tripType;
	}

	public Integer getMoney() {
		if (Money == null) {
			Money = 0;
		}
		return Money;
	}

	public void setMoney(Integer money) {
		Money = money;
	}

	public String getCardNumber() {
		return CardNumber;
	}

	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}

	public Timestamp getBeginTime() {
		return BeginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		BeginTime = beginTime;
	}

	public Timestamp getFinishTime() {
		return FinishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		FinishTime = finishTime;
	}

	public String getBeginAddr() {
		return BeginAddr;
	}

	public void setBeginAddr(String beginAddr) {
		BeginAddr = beginAddr;
	}

	public String getFinishAddr() {
		return FinishAddr;
	}

	public void setFinishAddr(String finishAddr) {
		FinishAddr = finishAddr;
	}

	public String getAddress1() {
		if (Address1 == null) {
			Address1 = "";
		}
		return Address1;
	}

	public void setAddress1(String address1) {
		Address1 = address1;
	}

	public String getAddress2() {
		if (Address2 == null) {
			Address2 = "";
		}
		return Address2;
	}

	public void setAddress2(String address2) {
		Address2 = address2;
	}

	public String getBeginLoc() {
		return BeginLoc;
	}

	public void setBeginLoc(String beginLoc) {
		BeginLoc = beginLoc;
	}

	public String getFinishLoc() {
		return FinishLoc;
	}

	public void setFinishLoc(String finishLoc) {
		FinishLoc = finishLoc;
	}

	public String getAddr1Loc() {
		return Addr1Loc;
	}

	public void setAddr1Loc(String addr1Loc) {
		Addr1Loc = addr1Loc;
	}

	public String getAddr2Loc() {
		return Addr2Loc;
	}

	public void setAddr2Loc(String addr2Loc) {
		Addr2Loc = addr2Loc;
	}

	public String getRequestContent() {
		return RequestContent;
	}

	public void setRequestContent(String requestContent) {
		RequestContent = requestContent;
	}

	public Integer getCusPosition() {
		return CusPosition;
	}

	public void setCusPosition(Integer cusPosition) {
		CusPosition = cusPosition;
	}

	public Integer getCusNumber() {
		return CusNumber;
	}

	public void setCusNumber(Integer cusNumber) {
		CusNumber = cusNumber;
	}

	public String getOtherDecription() {
		return OtherDecription;
	}

	public void setOtherDecription(String otherDecription) {
		OtherDecription = otherDecription;
	}

	public Timestamp getReceiveDate() {
		return ReceiveDate;
	}

	public void setReceiveDate(Timestamp receiveDate) {
		ReceiveDate = receiveDate;
	}

	public Timestamp getProcessDate() {
		return ProcessDate;
	}

	public void setProcessDate(Timestamp processDate) {
		ProcessDate = processDate;
	}

	public Timestamp getTimelog() {
		return Timelog;
	}

	public void setTimelog(Timestamp timelog) {
		Timelog = timelog;
	}

	public Integer getUpdateAgainState() {
		return UpdateAgainState;
	}

	public String getUpdateAgainStatus() {
		return "Trạng thái";
	}

	public void setUpdateAgainState(Integer updateAgainState) {
		UpdateAgainState = updateAgainState;
	}

}
