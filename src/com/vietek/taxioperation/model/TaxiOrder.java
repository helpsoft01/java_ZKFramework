package com.vietek.taxioperation.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.vietek.taxioperation.common.EnumOrderType;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.controller.ChannelTmsController;
import com.vietek.taxioperation.controller.CustomerController;
import com.vietek.taxioperation.controller.TaxiOrderController;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.util.BigdecimalUtils;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.DateUtils;
import com.vietek.taxioperation.util.DoubleUtils;
import com.vietek.taxioperation.util.MapUtils;

@Entity
@Table(name = "taxi_order")
// @Cacheable
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TaxiOrder extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7639907362280243606L;

	public TaxiOrder() {
		super();
	}

	@Id
	@GeneratedValue
	private int id;

	/* So dien thoai */
	// @Searchable(placehoder = "Số điện thoại")
	
	private String phoneNumber;
	/** So lan nhac */
	private Integer repeatTime = 0;
	/**
	 * Thoi gian goi nhac lan cuoi
	 */
	private Timestamp timeGoiNhac;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "taxiorder_vehicle", joinColumns = {
			@JoinColumn(name = "taxi_order_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "taxi_id", nullable = false, updatable = false) })
	// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	/* Xe dang ky don */
	private Set<Vehicle> registedTaxis = new HashSet<Vehicle>();
	@ManyToOne
	@JoinColumn(name = "taxi_id")
	/* Xe don */
	private Vehicle pickedTaxi;

	@FixedCombobox(label = { "Khách hàng huỷ", "Không có tài xế", "Không đón được khách", "Đón khách khác",
			"Lý do khác" }, value = { 1, 2, 3, 4, 5 })
	private Integer cancelReason = 0;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	@Cascade(value = { CascadeType.DELETE })
	private Customer customer;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private SysUser user;
	/**
	 * 1: Goi tong dai. 2: App smartphone. 3:Nha tu web. 4:Nhap tu sms. 5:Nhap
	 * tu chat
	 *
	 */
	@FixedCombobox(label = { "Tổng đài", "App", "Web", "Sms", "Chat" }, value = { 1, 2, 3, 4, 5 })
	private Integer orderType;
	@ManyToOne
	@JoinColumn(name = "channel_id")
	
	private ChannelTms channel;
	/* Ghi chu */
	private String note;
	/* Dia chi yeu cau */
	// @Searchable(placehoder = "Địa chỉ yêu cầu")
	private String beginOrderAddress;
	/* Dia chi don thuc te */
	private String beginAddress;
	/* Dia chi tra khach yeu cau */
	// @Searchable(placehoder = "Địa chỉ đến")
	private String endOrderAddress;
	/* Dia chi tra thuc te */
	private String loaddata;
	private String endAddress;
	/*So Km du tinh*/
	private String priceorderKm;
	/*Thoi Gian du tinh*/
	private String timect;
	/*So Km Phu Troi*/
	private BigDecimal priceKmadd = BigDecimal.ZERO;
	/*Thoi Gian Phu Troi*/
	private BigDecimal timeadd = BigDecimal.ZERO;
	/*Tong tien*/
	private String moneytotal;
	
	private Double beginOrderLon = 0.0;

	private Double beginOrderLat = 0.0;
	private Double beginLon = 0.0;
	private Double beginLat = 0.0;
	private Double endOrderLon = 0.0;
	private Double endOrderLat = 0.0;
	private Double endLon = 0.0;
	private Double endLat = 0.0;
	/** Thoi gian goi xe */
	private Timestamp orderTime;
	/** Thoi gian dat xe */
	private Timestamp beginOrderTime;
	/** Thoi gian bat khach */
	private Timestamp beginTime;
	/** Thoi gian tra khach */
	private Timestamp endTime;
	/* Gia du tinh */
	private BigDecimal fareEstimate = BigDecimal.ZERO;
	/*Gia Phu Troi*/
	private BigDecimal fareAdd = BigDecimal.ZERO;
	/* Gia dong ho */
	private BigDecimal fareGps = BigDecimal.ZERO;
	/* Gia thuc tra */
	private BigDecimal fareActual = BigDecimal.ZERO;
	/* Hai chieu */
	private Boolean twoWay = false;
	@ManyToOne
	@JoinColumn(name = "driver_id")
	private Driver driver;
	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;
	/* Gia co dinh */
	private Boolean fixedPrice = false;
	/* Di ngay */
	private Boolean startNow = true;
	/*Di Thoi Gian*/
	private Boolean someTime = true;
	/*Di Thuong*/
	private Boolean goNormal = true;
	/*Di Hop Dong*/
	private Boolean conTract = true;
	/* Di san bay */
	private Boolean airStation = true;
	/* Di chung */
	private Boolean somePerson = true;

	private Boolean isAutoOperation = true;

	private int blackBoxTripId = -1;
	private int blackBoxShipId = -1;

	private int GPSTripId = -1;

	@ManyToOne
	@JoinColumn(name = "introduced_driver_id")
	private Driver driverIntroduced;

	/**
	 * 0:Huy 1:Moi 2:Xe dk don 3:Xe don 4:Done
	 */
	@FixedCombobox(label = { "Hủy", "Mới", "Đăng ký đón", "Đã đón khách", "Đã trả khách", "Đọc đàm" }, value = { 0, 1,
			2, 3, 4, 5 })
	private Integer status = 1;
	private Integer rate = -1;
	private Boolean isUp = false;
	@FixedCombobox(label = { "", "4C", "7C" }, value = { 0, 1, 2 })
	private Integer orderCarType = 0;
	/**
	 * Thoi gian tao ban tin
	 */
	private Timestamp created = new Timestamp(System.currentTimeMillis());
	/**
	 * Thoi gian cap nhat ban tin
	 */
	private Timestamp updated = new Timestamp(System.currentTimeMillis());
	private Integer createBy;
	private Integer updateBy;
	/**
	 * Thoi gian bat dau co xe dang ky
	 */
	private Timestamp startRegisterTime = null;
	/**
	 * Thoi gian huy cuoc
	 */
	private Timestamp cancelTime;
	/**
	 * Thoi gian chuyen ban tin len doc dam
	 */
	private Timestamp timeIsUpdated = new Timestamp(System.currentTimeMillis());
	private Integer isDelCustomer = 0;

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

	public Set<Vehicle> getRegistedTaxis() {
		return registedTaxis;
	}

	public void setRegistedTaxis(HashSet<Vehicle> registedTaxis) {
		this.registedTaxis = registedTaxis;
	}

	public void setRegistedTaxis(Set<Vehicle> registedTaxis) {
		this.registedTaxis = registedTaxis;
	}

	public Vehicle getPickedTaxi() {
		return pickedTaxi;
	}

	public void setPickedTaxi(Vehicle pickedTaxi) {
		this.pickedTaxi = pickedTaxi;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
	
	public String getPriceOrderKm() {
		return priceorderKm;
	}
	public void setPriceOrderKm(String priceorderKm) {
		this.priceorderKm = priceorderKm;
	}
	
	public String getTimect() {
		return timect;
	}
	public void setTimect(String timect) {
		this.timect = timect;
	}
	
	public BigDecimal getPirceKmadd() {
		return priceKmadd;
	}
	public void setPriceKmadd(BigDecimal priceKmadd) {
		this.priceKmadd = priceKmadd;
	}
	
	public BigDecimal getTimeAdd() {
		return timeadd;
	}
	public void setTimeAdd(BigDecimal timeadd) {
		this.timeadd = timeadd;
	}
	
	public String getLoadData() {
		return loaddata;
	}
	public void setLoadData(String loaddata) {
		this.loaddata = loaddata;
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

	public String getTimeBeginOrder() {
		String strTime = null;
		if (beginOrderTime != null) {
			DateFormat df = new SimpleDateFormat("HH:mm");
			strTime = df.format(beginOrderTime);
		}
		return strTime;
	}

	public String getTimeOrder() {
		String strTime = null;
		if (orderTime != null) {
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			strTime = df.format(orderTime);
		}
		return strTime;
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
	
	public String getMoneytotal() {
		return moneytotal;
	}

	public void setMoneytotal(String moneytotal) {
		this.moneytotal = moneytotal;
	}

	public BigDecimal getFareAdd() {
		return fareAdd;
	}
	public void setFareAdd(BigDecimal fareAdd) {
		this.fareAdd = fareAdd;
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

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
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

	// public String getTypeTaxiOrder() {
	// StringBuffer strBuffer = new StringBuffer();
	// if (typeTaxiOrder != null && !typeTaxiOrder.isEmpty()) {
	// String[] as = typeTaxiOrder.split(";");
	// if(as.length == 3) {
	// try {
	// for (int i = 0; i < as.length; i++) {
	// Double tmp = Double.parseDouble(as[i]);
	// int sl = Integer.parseInt(as[i]);
	// if (i==0 && sl != 0) {
	// strBuffer = strBuffer.append("Mặc định : "+ sl + " xe ");
	// }else if (i==1 && sl != 0) {
	// if (strBuffer.toString().length() > 0) {
	// strBuffer = strBuffer.append(" - ");
	// }
	// strBuffer = strBuffer.append("4 chỗ : " + sl + " xe");
	// }else if (i==2 && sl != 0) {
	// if (strBuffer.toString().length() > 0) {
	// strBuffer = strBuffer.append(" - ");
	// }
	// strBuffer = strBuffer.append("7 chỗ : " + sl + " xe");
	// }
	// }
	// return strBuffer.toString();
	// }catch(Exception e){
	// return "Dữ liệu chưa xác thực";
	// }
	// }else {
	// return "Dữ liệu chưa xác thực";
	// }
	// }else {
	// return "Chưa có dữ liệu";
	// }
	// }
	//
	// public void setTypeTaxiOrder(String typeTaxiOrder) {
	// this.typeTaxiOrder = typeTaxiOrder;
	// }

	// public Integer getTypeTaxiOrder() {
	// return typeTaxiOrder;
	// }
	//
	// public void setTypeTaxiOrder(Integer typeTaxiOrder) {
	// this.typeTaxiOrder = typeTaxiOrder;
	// }

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(Integer cancelReason) {
		this.cancelReason = cancelReason;
	}

	public ChannelTms getChannel() {
		return channel;
	}

	public void setChannel(ChannelTms channel) {
		this.channel = channel;
	}
	
	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public Integer getOrderCarType() {
		return orderCarType;
	}

	public void setOrderCarType(Integer orderCarType) {
		this.orderCarType = orderCarType;
	}

	public static TaxiOrder createNewTaxiOrder(String phoneNumber, String beginOrderAddress, String beginOrderLatitude,
			String beginOrderLongitude, String endOrdeAddress, String endOrderLongitude, String endOrderLatitude,
			String beginOrderTime, String fareEstimate, String twoWay, String fixedPrice, String note) {
		TaxiOrder model = null;
		try {
			model = TaxiOrder.class.newInstance();
			model.setPhoneNumber(phoneNumber);
			model.setBeginOrderAddress(beginOrderAddress);
			if (BigdecimalUtils.isString2Bigdecimal(beginOrderLatitude)) {
				model.setBeginOrderLat(new Double(beginOrderLatitude));
			} else {
				model.setBeginOrderLat(0.0);
			}
			if (BigdecimalUtils.isString2Bigdecimal(beginOrderLongitude)) {
				model.setBeginOrderLon(new Double(beginOrderLongitude));
			} else {
				model.setBeginOrderLon(0.0);
			}
			model.setEndOrderAddress(endOrdeAddress);
			if (BigdecimalUtils.isString2Bigdecimal(endOrderLatitude)) {
				model.setEndOrderLat(new Double(endOrderLatitude));
			} else {
				model.setEndOrderLat(0.0);
			}
			if (BigdecimalUtils.isString2Bigdecimal(endOrderLongitude)) {
				model.setEndOrderLon(new Double(endOrderLongitude));
			} else {
				model.setEndOrderLon(0.0);
			}
			if (BigdecimalUtils.isString2Bigdecimal(fareEstimate)) {
				model.setFareEstimate(new BigDecimal(fareEstimate));
			} else {
				model.setFareEstimate(BigDecimal.ZERO);
			}
			model.setTwoWay(false);
			if (twoWay != null && twoWay.length() > 0) {
				if (twoWay.equalsIgnoreCase("1")) {
					model.setTwoWay(true);
				}
			}
			model.setFixedPrice(false);
			if (fixedPrice != null && fixedPrice.length() > 0) {
				if (fixedPrice.equalsIgnoreCase("1")) {
					model.setTwoWay(true);
				}
			}
			model.setNote(note);
			model.setOrderType(EnumOrderType.APP_SMARTPHONE.getValue());
			model.setStatus(1);
			Date date = DateUtils.parseString2Date(beginOrderTime);
			if (date != null) {
				model.setBeginOrderTime(new Timestamp(date.getTime()));
			} else {
				model.setBeginOrderTime(new Timestamp(System.currentTimeMillis()));
			}
			model.setOrderTime(new Timestamp(System.currentTimeMillis()));
			List<Customer> lstUser = ((CustomerController) ControllerUtils.getController(CustomerController.class))
					.find("from Customer where phonenumber = ?", new Object[] { phoneNumber });
			if (lstUser != null && lstUser.size() > 0) {
				model.setCustomer(lstUser.get(0));
			}
			ChannelTms channel = getAutoChannel(DoubleUtils.string2Double(beginOrderLongitude),
					DoubleUtils.string2Double(beginOrderLatitude));
			model.setChannel(channel);
			model.save();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return model;
	}

	public static TaxiOrder createNewTaxiOrder(Customer customer, String beginOrderAddress, double beginOrderLatitude,
			double beginOrderLongitude, String endOrdeAddress, double endOrderLongitude, double endOrderLatitude,
			String beginOrderTime, String note, int type, boolean isAirport) {
		TaxiOrder model = null;
		try {
			model = TaxiOrder.class.newInstance();
			model.setPhoneNumber(customer == null ? null : customer.getPhoneNumber());
			model.setCustomer(customer);
			model.setBeginOrderAddress(beginOrderAddress);
			model.setBeginAddress(beginOrderAddress);
			model.setBeginOrderLat(new Double(beginOrderLatitude));
			model.setBeginOrderLon(new Double(beginOrderLongitude));
			model.setBeginLat(model.getBeginOrderLat());
			model.setBeginLon(model.getBeginOrderLon());
			model.setEndOrderAddress(endOrdeAddress);
			model.setEndAddress(endOrdeAddress);
			model.setEndOrderLat(new Double(endOrderLatitude));
			model.setEndOrderLon(new Double(endOrderLongitude));
			model.setEndLat(model.getEndOrderLat());
			model.setEndLon(model.getEndOrderLon());
			model.setNote(note);
			// model.setOrderType(EnumOrderType.APP_SMARTPHONE.getValue());
			model.setStatus(1);
			model.setAirStation(isAirport);
			Date date = DateUtils.parseString2Date(beginOrderTime);
			if (date != null) {
				model.setBeginOrderTime(new Timestamp(date.getTime()));
			} else {
				model.setBeginOrderTime(new Timestamp(System.currentTimeMillis()));
			}
			model.setOrderTime(new Timestamp(System.currentTimeMillis()));
			ChannelTms channel = getAutoChannel(beginOrderLongitude, beginOrderLatitude);
			model.setChannel(channel);
			model.setOrderType(type);
			model.save();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			model = null;
		}
		return model;
	}

	public static TaxiOrder createNewTaxiOrder(String orderid, String phoneNumber, String beginOrderAddress,
			String beginOrderLatitude, String beginOrderLongitude, String endOrdeAddress, String endOrderLongitude,
			String endOrderLatitude, String beginOrderTime, String twoWay, String fixedPrice, String somePersion,
			String airstation, String vehicleType1, String vehicleType2, String note) {
		TaxiOrder order = null;
		try {
			if (orderid.equals("") || orderid == null) {
				order = TaxiOrder.class.newInstance();
			} else {
				TaxiOrderController ordercontroller = (TaxiOrderController) ControllerUtils
						.getController(TaxiOrderController.class);
				List<TaxiOrder> lstorder = ordercontroller.find("from TaxiOrder where id = ?",
						Integer.valueOf(orderid));
				if (lstorder != null && lstorder.size() > 0) {
					order = lstorder.get(0);
				}
			}

			order.setPhoneNumber(phoneNumber);
			order.setBeginOrderAddress(beginOrderAddress);
			order.setBeginAddress(beginOrderAddress);
			if (BigdecimalUtils.isString2Bigdecimal(beginOrderLatitude)) {
				order.setBeginOrderLat(new Double(beginOrderLatitude));
				order.setBeginLat(new Double(beginOrderLatitude));
			} else {
				order.setBeginOrderLat(0.0);
				order.setBeginLat(0.0);
			}
			if (BigdecimalUtils.isString2Bigdecimal(beginOrderLongitude)) {
				order.setBeginOrderLon(new Double(beginOrderLongitude));
				order.setBeginLon(new Double(beginOrderLongitude));
			} else {
				order.setBeginOrderLon(0.0);
				order.setBeginLon(0.0);
			}
			order.setEndOrderAddress(endOrdeAddress);
			order.setEndAddress(endOrdeAddress);
			if (BigdecimalUtils.isString2Bigdecimal(endOrderLatitude)) {
				order.setEndOrderLat(new Double(endOrderLatitude));
				order.setEndLat(new Double(endOrderLatitude));
			} else {
				order.setEndOrderLat(0.0);
				order.setEndLat(0.0);
			}
			if (BigdecimalUtils.isString2Bigdecimal(endOrderLongitude)) {
				order.setEndOrderLon(new Double(endOrderLongitude));
				order.setEndLon(new Double(endOrderLongitude));
			} else {
				order.setEndOrderLon(0.0);
				order.setEndLon(0.0);
			}
			order.setSomePerson(false);
			if (somePersion != null && somePersion.length() > 0) {
				if (somePersion.equalsIgnoreCase("1")) {
					order.setSomePerson(true);
				}
			}
			order.setAirStation(false);
			if (airstation != null && airstation.length() > 0) {
				if (airstation.equalsIgnoreCase("1")) {
					order.setAirStation(true);
				}

			}
			order.setTwoWay(false);
			if (twoWay != null && twoWay.length() > 0) {
				if (twoWay.equalsIgnoreCase("1")) {
					order.setTwoWay(true);
				}
			}
			order.setFixedPrice(false);
			if (fixedPrice != null && fixedPrice.length() > 0) {
				if (fixedPrice.equalsIgnoreCase("1")) {
					order.setTwoWay(true);
				}
			}
			order.setNote(note);
			order.setOrderType(EnumOrderType.NHAP_TU_WEB.getValue());
			order.setStatus(1);
			Date date = DateUtils.parseString2Date(beginOrderTime);
			if (date != null) {
				order.setBeginOrderTime(new Timestamp(date.getTime()));
			} else {
				order.setBeginOrderTime(new Timestamp(System.currentTimeMillis()));
			}
			order.setOrderTime(new Timestamp(System.currentTimeMillis()));
			List<Customer> lstUser = ((CustomerController) ControllerUtils.getController(CustomerController.class))
					.find("from Customer where phonenumber = ?", new Object[] { phoneNumber });
			if (lstUser != null && lstUser.size() > 0) {
				order.setCustomer(lstUser.get(0));
			}
			ChannelTms channel = getAutoChannel(Double.valueOf(beginOrderLongitude),
					Double.valueOf(beginOrderLatitude));
			order.setChannel(channel);
			order.save();
			// luu loai xe tuong ung voi so luong moi loai

			// TypeVehicleForOrder typevehicleForder = null;
			// TypeVehicleForOrderController typevhforordercontroller =
			// (TypeVehicleForOrderController) ControllerUtils
			// .getController(TypeVehicleForOrderController.class);
			// TypeVehicleOrderController typevhController =
			// (TypeVehicleOrderController) ControllerUtils
			// .getController(TypeVehicleOrderController.class);
			// List<TypeVehicleForOrder> lsttypevhordertmp = null;
			// List<TypeVehicleOrder> lsttypevhtmp = null;
			// // ------Save Vehicle Type 1
			// if (!vehicleType1.equals("")) {
			// Map<String, Integer> mapvehicletype1 =
			// StringUtils.mapFromString(vehicleType1);
			//
			// if (mapvehicletype1.get("TypeOrderid") == 0) {
			// typevehicleForder = TypeVehicleForOrder.class.newInstance();
			// } else {
			// lsttypevhordertmp = typevhforordercontroller.find("from
			// TypeVehicleForOrder where id = ? ",
			// mapvehicletype1.get("TypeOrderid"));
			// if (lsttypevhordertmp != null && lsttypevhordertmp.size() > 0) {
			// typevehicleForder = lsttypevhordertmp.get(0);
			// }
			// }
			// typevehicleForder.setTaxiOrder(order);
			// typevehicleForder.setNumberSeat(mapvehicletype1.get("NumberSeat"));
			// if (mapvehicletype1.get("Typeid").equals(0)) {
			// lsttypevhtmp = typevhController.find("From TypeVehicleOrder where
			// id = ? ", 4);
			// } else {
			// lsttypevhtmp = typevhController.find("From TypeVehicleOrder where
			// id = ? ",
			// mapvehicletype1.get("Typeid"));
			// }
			//
			// if (lsttypevhtmp != null && lsttypevhtmp.size() > 0) {
			// typevehicleForder.setTypeVehicleOrder(lsttypevhtmp.get(0));
			// }
			// typevehicleForder.save();
			// }
			// // --------Save Vehicle Type 2
			//
			// if (!vehicleType2.equals("")) {
			// Map<String, Integer> mapvehicletype2 =
			// StringUtils.mapFromString(vehicleType2);
			//
			// if (mapvehicletype2.get("TypeOrderid") == 0) {
			// typevehicleForder = TypeVehicleForOrder.class.newInstance();
			// } else {
			// lsttypevhordertmp = typevhforordercontroller.find("from
			// TypeVehicleForOrder where id = ? ",
			// Math.abs(mapvehicletype2.get("TypeOrderid")));
			// if (lsttypevhordertmp != null && lsttypevhordertmp.size() > 0) {
			// typevehicleForder = lsttypevhordertmp.get(0);
			// }
			// }
			// if (mapvehicletype2.get("Delete") == null) {
			// typevehicleForder.setTaxiOrder(order);
			// typevehicleForder.setNumberSeat(mapvehicletype2.get("NumberSeat"));
			// lsttypevhtmp = typevhController.find("From TypeVehicleOrder where
			// id = ? ",
			// mapvehicletype2.get("Typeid"));
			// if (lsttypevhtmp != null && lsttypevhtmp.size() > 0) {
			// typevehicleForder.setTypeVehicleOrder(lsttypevhtmp.get(0));
			// }
			// typevehicleForder.save();
			// } else if (mapvehicletype2.get("Delete") == 1) {
			// typevehicleForder.delete();
			// }

			// -----------------------
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return order;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Boolean getStartNow() {
		return startNow;
	}

	public void setStartNow(Boolean startNow) {
		this.startNow = startNow;
	}
	public Boolean getSomeTime() {
		return someTime;
	}
	public void setSomeTime(Boolean someTime) {
		this.someTime = someTime;
	}
	public Boolean getGoNormal() {
		return goNormal;
	}
	public void setGoNormal(Boolean goNormal) {
		this.goNormal = goNormal;
	}
	public Boolean getConTract() {
		return conTract;
	}
	public void setConTract(Boolean conTract) {
		this.conTract = conTract;
	}

	public Boolean getAirStation() {
		return airStation;
	}

	public void setAirStation(Boolean airStation) {
		this.airStation = airStation;
	}

	public Boolean getSomePerson() {
		return somePerson;
	}

	public void setSomePerson(Boolean somePerson) {
		this.somePerson = somePerson;
	}

	public Boolean getIsUp() {
		return isUp;
	}

	public void setIsUp(Boolean isUp) {
		this.isUp = isUp;
	}

	public Timestamp getStartRegisterTime() {
		return startRegisterTime;
	}

	public void setStartRegisterTime(Timestamp startRegisterTime) {
		this.startRegisterTime = startRegisterTime;
	}

	public Timestamp getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Timestamp cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Timestamp getTimeIsUpdated() {
		return timeIsUpdated;
	}

	public void setTimeIsUpdated(Timestamp timeIsUpdated) {
		this.timeIsUpdated = timeIsUpdated;
	}

	public Integer getIsDelCustomer() {
		return isDelCustomer;
	}

	public void setIsDelCustomer(Integer isDelCustomer) {
		this.isDelCustomer = isDelCustomer;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Integer getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}

	public Timestamp getTimeGoiNhac() {
		return timeGoiNhac;
	}

	public void setTimeGoiNhac(Timestamp timeGoiNhac) {
		this.timeGoiNhac = timeGoiNhac;
	}

	private static ChannelTms getAutoChannel(Double longitude, Double latitude) {
		if (longitude == null || latitude == null) {
			return null;
		}
		List<ChannelTms> lstChannel = getListChannel();
		ChannelTms tmp = null;
		double distanceTmp = Double.MAX_VALUE;
		if (lstChannel != null) {
			for (ChannelTms channelTms : lstChannel) {
				if (channelTms.getLongitude() != null && channelTms.getLatitude() != null) {
					double distance = MapUtils.distance(longitude, latitude, channelTms.getLongitude(),
							channelTms.getLatitude());
					if (distance < distanceTmp) {
						tmp = channelTms;
						distanceTmp = distance;
					}
				}
			}
		}
		return tmp;
	}

	private static List<ChannelTms> getListChannel() {
		ChannelTmsController controller = (ChannelTmsController) ControllerUtils
				.getController(ChannelTmsController.class);
		List<ChannelTms> lst = controller.find("from ChannelTms where isActive = true");
		return lst;
	}

	public Driver getDriverIntroduced() {
		return driverIntroduced;
	}

	public void setDriverIntroduced(Driver driverIntroduced) {
		this.driverIntroduced = driverIntroduced;
	}

	public Boolean getIsAutoOperation() {
		return isAutoOperation;
	}

	public void setIsAutoOperation(Boolean isAutoOperation) {
		this.isAutoOperation = isAutoOperation;
	}

	public int getBlackBoxTripId() {
		return blackBoxTripId;
	}

	public void setBlackBoxTripId(int blackBoxTripId) {
		this.blackBoxTripId = blackBoxTripId;
	}

	public int getBlackBoxShipId() {
		return blackBoxShipId;
	}

	public void setBlackBoxShipId(int blackBoxShipId) {
		this.blackBoxShipId = blackBoxShipId;
	}

	public int getGPSTripId() {
		return GPSTripId;
	}

	public void setGPSTripId(int gPSTripId) {
		GPSTripId = gPSTripId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TaxiOrder) {
			if (((TaxiOrder) obj).getId() == id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return phoneNumber;
	}

	@Override
	public void save() {
		TripManager.sharedInstance.updateTaxiOrder(this);
		super.save();
	}

	@Override
	public Integer saveOrUpdate(AbstractModel model) {
		TripManager.sharedInstance.updateTaxiOrder(this);
		return super.saveOrUpdate(model);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

}
