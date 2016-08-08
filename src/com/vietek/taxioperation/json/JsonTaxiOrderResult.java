package com.vietek.taxioperation.json;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.User;

public class JsonTaxiOrderResult {
	private int id;
	/* So dien thoai */
	private String phoneNumber;
	/* So lan nhac */
	private Integer repeatTime;
	/* Xe dang ky don */
	private List<JsonTaxi> registedTaxis;
	/* Xe don */
	private JsonTaxi pickedTaxi;
	/* Khach hang */
	private JsonCustomer customer;
	private User user;
	private Integer orderType;
	private ChannelTms channel;
	/* Ghi chu */
	private String note;
	/* Dia chi gan day 1 */
	private String address1;
	/* Dia chi gan day 2 */
	private String address2;
	/* Dia chi gan day 3 */
	private String address3;
	/* Dia chi yeu cau */
	private String beginOrderAddress;
	/* Dia chi don thu te */
	private String beginAddress;
	/* Dia chi tra khach yeu cau */
	private String endOrderAddress;
	/* Dia chi tra thuc te */
	private String endAddress;
	private Double beginOrderLon = Double.MIN_VALUE;

	private Double beginOrderLat = Double.MIN_VALUE;
	private Double beginLon = Double.MIN_VALUE;
	private Double beginLat = Double.MIN_VALUE;
	private Double endOrderLon = Double.MIN_VALUE;
	private Double endOrderLat = Double.MIN_VALUE;
	private Double endLon = Double.MIN_VALUE;
	private Double endLat = Double.MIN_VALUE;
	/* Thoi gian goi xe */
	private Timestamp orderTime;
	/* Thoi gian dat xe */
	private Timestamp beginOrderTime;
	/* Thoi gian bat khach */
	private Timestamp beginTime;
	/* Thoi gian tra khach */
	private Timestamp endTime;
	/* Gia du tinh */
	private BigDecimal fareEstimate = BigDecimal.ZERO;
	/* Gia dong ho */
	private BigDecimal fareGps = BigDecimal.ZERO;
	/* Gia thuc tra */
	private BigDecimal fareActual = BigDecimal.ZERO;
	/* Hai chieu */
	private Boolean twoWay = false;
	private JsonDriver driver;
	/* Duong dai */
	private Boolean fixedPrice = false;
	private Integer status = 1;
	private Integer rate = -1;
	private Integer typeTaxiOrder;

	public Double getBeginOrderLon() {
		return beginOrderLon;
	}

	public void setBeginOrderLon(Double beginOrderLon) {
		this.beginOrderLon = beginOrderLon;
	}

	public Double getBeginOrderLat() {
		return beginOrderLat;
	}

	public void setBeginOrderLat(Double beginOrderLat) {
		this.beginOrderLat = beginOrderLat;
	}

	public Double getBeginLon() {
		return beginLon;
	}

	public void setBeginLon(Double beginLon) {
		this.beginLon = beginLon;
	}

	public Double getBeginLat() {
		return beginLat;
	}

	public void setBeginLat(Double beginLat) {
		this.beginLat = beginLat;
	}

	public Double getEndOrderLon() {
		return endOrderLon;
	}

	public void setEndOrderLon(Double endOrderLon) {
		this.endOrderLon = endOrderLon;
	}

	public Double getEndOrderLat() {
		return endOrderLat;
	}

	public void setEndOrderLat(Double endOrderLat) {
		this.endOrderLat = endOrderLat;
	}

	public Double getEndLon() {
		return endLon;
	}

	public void setEndLon(Double endLon) {
		this.endLon = endLon;
	}

	public Double getEndLat() {
		return endLat;
	}

	public void setEndLat(Double endLat) {
		this.endLat = endLat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getRepeatTime() {
		return repeatTime;
	}

	public void setRepeatTime(Integer repeatTime) {
		this.repeatTime = repeatTime;
	}

	public JsonTaxi getPickedTaxi() {
		return pickedTaxi;
	}

	public void setPickedTaxi(JsonTaxi pickedTaxi) {
		this.pickedTaxi = pickedTaxi;
	}

	public JsonCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(JsonCustomer customer) {
		this.customer = customer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public ChannelTms getChannel() {
		return channel;
	}

	public void setChannel(ChannelTms channel) {
		this.channel = channel;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
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

	public String getBeginOrderAddress() {
		return beginOrderAddress;
	}

	public void setBeginOrderAddress(String beginOrderAddress) {
		this.beginOrderAddress = beginOrderAddress;
	}

	public String getBeginAddress() {
		return beginAddress;
	}

	public void setBeginAddress(String beginAddress) {
		this.beginAddress = beginAddress;
	}

	public String getEndOrderAddress() {
		return endOrderAddress;
	}

	public void setEndOrderAddress(String endOrderAddress) {
		this.endOrderAddress = endOrderAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	public Timestamp getBeginOrderTime() {
		return beginOrderTime;
	}

	public void setBeginOrderTime(Timestamp beginOrderTime) {
		this.beginOrderTime = beginOrderTime;
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getFareEstimate() {
		return fareEstimate;
	}

	public void setFareEstimate(BigDecimal fareEstimate) {
		this.fareEstimate = fareEstimate;
	}

	public BigDecimal getFareGps() {
		return fareGps;
	}

	public void setFareGps(BigDecimal fareGps) {
		this.fareGps = fareGps;
	}

	public BigDecimal getFareActual() {
		return fareActual;
	}

	public void setFareActual(BigDecimal fareActual) {
		this.fareActual = fareActual;
	}

	public Boolean getTwoWay() {
		return twoWay;
	}

	public void setTwoWay(Boolean twoWay) {
		this.twoWay = twoWay;
	}

	public JsonDriver getDriver() {
		return driver;
	}

	public void setDriver(JsonDriver driver) {
		this.driver = driver;
	}

	public Boolean getFixedPrice() {
		return fixedPrice;
	}

	public void setFixedPrice(Boolean fixedPrice) {
		this.fixedPrice = fixedPrice;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public Integer getTypeTaxiOrder() {
		return typeTaxiOrder;
	}

	public void setTypeTaxiOrder(Integer typeTaxiOrder) {
		this.typeTaxiOrder = typeTaxiOrder;
	}

	public List<JsonTaxi> getRegistedTaxis() {
		return registedTaxis;
	}

	public void setRegistedTaxis(List<JsonTaxi> registedTaxis) {
		this.registedTaxis = registedTaxis;
	}

}
