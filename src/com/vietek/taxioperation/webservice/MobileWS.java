package com.vietek.taxioperation.webservice;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.CustomerController;
import com.vietek.taxioperation.controller.DriverController;
import com.vietek.taxioperation.controller.DriverRegisterController;
import com.vietek.taxioperation.controller.TaxiOrderController;
import com.vietek.taxioperation.json.JsonCustomer;
import com.vietek.taxioperation.json.JsonDriver;
import com.vietek.taxioperation.json.JsonTaxi;
import com.vietek.taxioperation.json.JsonTaxiOrderResult;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.CustomerCallLog;
import com.vietek.taxioperation.model.CustomerFeedBack;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.DriverGCMInfo;
import com.vietek.taxioperation.model.DriverRegister;
import com.vietek.taxioperation.model.NotificationsFromDriver;
import com.vietek.taxioperation.model.SOSNotification;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.realtime.Rider;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.Content;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.GCMUtils;
import com.vietek.taxioperation.util.IntegerUtils;
import com.vietek.taxioperation.util.NotificationUtils;

@Path("/MobileWS")
public class MobileWS {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTaxi")
	public Taxi getTaxi() {
		Taxi taxi = new Taxi();
		taxi.taxiNumber = "1234";
		return taxi;
	}

	class Taxi {
		public String taxiNumber;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/postTaxiOrder")
	public PostTaxiOrderResult postTaxiOrder(
			@QueryParam("phonenumber") String phoneNumber,
			@QueryParam("bOAddress") String beginOrderAddress,
			@QueryParam("bOLatitude") String beginOrderLatitude,
			@QueryParam("bOLongitude") String beginOrderLongitude,
			@QueryParam("eOAddress") String endOrdeAddress,
			@QueryParam("eOLongitude") String endOrderLongitude,
			@QueryParam("eOLatitude") String endOrderLatitude,
			@QueryParam("beginOrderTime") String beginOrderTime,
			@QueryParam("fareEstimate") String fareEstimate,
			@QueryParam("twoWay") String twoWay,
			@QueryParam("fixedPrice") String fixedPrice,
			@QueryParam("note") String note) {
		TaxiOrder order = TaxiOrder.createNewTaxiOrder(phoneNumber,
				beginOrderAddress, beginOrderLatitude, beginOrderLongitude,
				endOrdeAddress, endOrderLongitude, endOrderLatitude,
				beginOrderTime, fareEstimate, twoWay, fixedPrice, note);
		return new PostTaxiOrderResult(order.getId());
	}

	class PostTaxiOrderResult {
		private int tripID;

		public PostTaxiOrderResult(int tripID) {
			this.tripID = tripID;
		}

		public int getTripID() {
			return tripID;
		}

		public void setTripID(int tripID) {
			this.tripID = tripID;
		}
	}

	/**
	 * Login customer
	 * 
	 * @param phoneNumber
	 * @param password
	 * @param email
	 * @return 1 if successful 0 if failed
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public LoginRegisterResult login(
			@QueryParam("phonenumber") String phoneNumber,
			@QueryParam("pass") String password) {
		List<Customer> lstValue = ((CustomerController) ControllerUtils
				.getController(CustomerController.class))
				.find("from Customer where phoneNumber = '" + phoneNumber + "'");
		if (lstValue.size() > 0) { // User already register
			Customer customer = lstValue.get(0);
			if (customer.getPassword() != null
					&& customer.getPassword().equals(password)) {
				return new LoginRegisterResult(1,
						customer.getName() == null ? "" : customer.getName(),
						customer.getEmail() == null ? "" : customer.getEmail(),
						"", customer.getAvatar() == null ? "" : new String(
								customer.getAvatar()),
								customer.getRegId() == null ? "" : customer.getRegId());
			} else {
				return new LoginRegisterResult(0, "", "",
						"Kiểm tra lại mật khẩu", "","");
			}
		}
		return new LoginRegisterResult(0, "", "",
				"Không có thông tin user, bạn đã đăng ký chưa?", "","");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/changePassword")
	public LoginRegisterResult changePassword(
			@QueryParam("phonenumber") String phoneNumber,
			@QueryParam("oldPass") String oldPass,
			@QueryParam("pass") String password) {
		List<Customer> lstValue = ((CustomerController) ControllerUtils
				.getController(CustomerController.class))
				.find("from Customer where phoneNumber = '" + phoneNumber + "'");
		if (lstValue.size() > 0) { // User already register
			Customer customer = lstValue.get(0);
			if (customer.getPassword() != null
					&& customer.getPassword().equals(oldPass)) {
				customer.setPassword(password);
				customer.save();
				return new LoginRegisterResult(1, "", "", "", "","");
			} else {
				return new LoginRegisterResult(0, "", "",
						"Kiểm tra lại mật khẩu", "","");
			}
		}
		return new LoginRegisterResult(0, "", "",
				"Không có thông tin user, bạn đã đăng ký chưa?", "","");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/verify")
	public VerifyPhoneResult verify(
			@QueryParam("phonenumber") String phoneNumber) {
		CustomerController customerController = ((CustomerController) ControllerUtils
				.getController(CustomerController.class));
		List<Customer> lstValue = customerController
				.find("from Customer where phoneNumber = '" + phoneNumber + "'");
		if (lstValue.size() != 1) {
			return new VerifyPhoneResult(0, "", "Check phone number");
		} else {
			String verifyCode = customerController.getVerifyCode(lstValue.get(0));
			NotificationUtils.sendNotification(lstValue.get(0).getPhoneNumber(),lstValue.get(0).getEmail(), "MaiLinhTaxi - Verify Code", "MaiLinhTaxi - Verify code: " + verifyCode);
			return new VerifyPhoneResult(1, verifyCode, "");
		}
	}

	class VerifyPhoneResult {
		private int status;
		private String verifyCode;
		private String error;

		public VerifyPhoneResult(int status, String verifyCode, String error) {
			super();
			this.status = status;
			this.verifyCode = verifyCode;
			this.error = error;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getVerifyCode() {
			return verifyCode;
		}

		public void setVerifyCode(String verifyCode) {
			this.verifyCode = verifyCode;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

	}

	/**
	 * Login or register customer
	 * 
	 * @param phoneNumber
	 * @param password
	 * @param email
	 * @return 1 if successful 0 if failed
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/register")
	public LoginRegisterResult register(
			@QueryParam("phonenumber") String phoneNumber,
			@QueryParam("pass") String password,
			@QueryParam("email") String email, @QueryParam("name") String name) {
		List<Customer> lstValue = ((CustomerController) ControllerUtils
				.getController(CustomerController.class))
				.find("from Customer where phoneNumber = '" + phoneNumber + "'");
		Customer customer = null;
		if (lstValue.size() > 0)
		{
			customer = lstValue.get(0);
		}
		if (lstValue.size() > 0) {
			if (customer.getPassword() != null && customer.getPassword().trim().length() > 0) {
				return new LoginRegisterResult(
					0,
					"",
					"",
					"Số điện thoại đã được đang ký, hãy đăng nhập hoặc đăng ký số điện thoại khác",
					"","");
			}
		}
		
		try {
			if (customer == null) {
				customer = Customer.class.newInstance();
				customer.setPhoneNumber(phoneNumber);
			}
			customer.setEmail(email);
			customer.setPassword(password);
			customer.setName(name);
			customer.save();
			return new LoginRegisterResult(1, name, email, "", "","");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new LoginRegisterResult(0, "", "",
				"Có lỗi trong quá trình đăng ký", "","");
	}

	class LoginRegisterResult {
		private int result; // 0: Error: 1: Oke
		private String userEmail;
		private String userName;
		private String errorString;
		private String avatar;
		private String regId;
		public LoginRegisterResult(int result, String userName,
				String userEmail, String errorString, String avatar, String regId) {
			this.result = result;
			this.userEmail = userEmail;
			this.errorString = errorString;
			this.setAvatar(avatar);
			this.setUserName(userName);
			this.setRegId(regId);
			
		}

		public String getUserEmail() {
			return userEmail;
		}

		public void setUserEmail(String userEmail) {
			this.userEmail = userEmail;
		}

		public String getErrorString() {
			return errorString;
		}

		public void setErrorString(String errorString) {
			this.errorString = errorString;
		}

		public int getResult() {
			return result;
		}

		public void setResult(int result) {
			this.result = result;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getRegId() {
			return regId;
		}

		public void setRegId(String regId) {
			this.regId = regId;
		}
		
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getPositionTaxi")
	public TaxiResultGroup getPositionTaxi(
			@QueryParam("longitude") double longitude,
			@QueryParam("latitude") double latitude,
			@QueryParam("distance") int distance,
			@QueryParam("amount") int amount, @QueryParam("status") int status,
			@QueryParam("userid") String userid) {
		TaxiResultGroup taxiResultGroup = new TaxiResultGroup();
		taxiResultGroup.setStatus("FALSE");
		taxiResultGroup.setLstTaxiResults(new ArrayList<TaxiResult>());
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sessionImplementor.connection();
			if (conn == null) {
				return taxiResultGroup;
			}
			cs = conn
					.prepareCall("call txm_tracking.cmdGetCarMobile(?, ?, ?, ?, ?, ?)");
			cs.setDouble(1, longitude);
			cs.setDouble(2, latitude);
			cs.setInt(3, distance);
			cs.setInt(4, amount);
			cs.setInt(5, status);
			cs.setString(6, userid);
			rs = cs.executeQuery();
			while (rs.next()) {
				TaxiResult tx = new TaxiResult();
				tx.setId(rs.getInt("id"));
				tx.setTimeLog(rs.getTimestamp("TimeLog"));
				tx.setLongitude(rs.getDouble("Longi"));
				tx.setLatitude(rs.getDouble("Lati"));
				// tx.setLonDiff(rs.getBlob("LongiDiff"));
				// tx.setLatDiff(rs.getBlob("LatiDiff"));
				tx.setLastGpsSpeed(rs.getInt("LastGPSSpeed"));
				// tx.setMeterSpeed(rs.getInt("MeterSpeed"));
				tx.setLicensePlace(rs.getString("LicensePlate"));
				tx.setVehicleNumber(rs.getString("VehicleNumber"));
				// tx.setVinNumber(rs.getString("VinNumber"));
				tx.setGroupName(rs.getString("GroupName"));
				tx.setTypeName(rs.getString("TypeName"));
				tx.setDriverName(rs.getString("DriverName"));
				tx.setRegisterNumber(rs.getString("RegisterNumber"));
				tx.setMobileNumber(rs.getString("MobileNumber"));
				tx.setAngle(rs.getDouble("Angle"));
				taxiResultGroup.getLstTaxiResults().add(tx);

			}
			if (taxiResultGroup.getLstTaxiResults().size() > 0) {
				taxiResultGroup.setStatus("True");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}
		return taxiResultGroup;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/phoneLog")
	public CallLogResult sendCallLog(
			@QueryParam("customerPhone") String customerPhone,
			@QueryParam("driverPhone") String driverPhone,
			@QueryParam("vehicleNumber") String vehicleNumber) {

		if (customerPhone == null || customerPhone.length() <= 0) {
			return new CallLogResult("Khong co so dien thoai khach hang");
		}

		if (driverPhone == null || driverPhone.length() <= 0) {
			return new CallLogResult("Khong co so dien thoai cua lai xe");
		}
		if (vehicleNumber == null || vehicleNumber.length() <= 0) {
			return new CallLogResult("Khong co so tai");
		}
		CustomerCallLog customerCallLog = new CustomerCallLog();
		customerCallLog.setCustomerPhone(customerPhone);
		customerCallLog.setDriverPhone(driverPhone);
		customerCallLog.setVehicleNumber(vehicleNumber);
		customerCallLog.setCreated(new Timestamp(System.currentTimeMillis()));
		customerCallLog.save();
		return new CallLogResult("OK");
	}

	class CallLogResult {
		private String result;

		public CallLogResult(String result) {
			this.setResult(result);
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTaxiOrder")
	public JsonTaxiOrderResult getTaxiOrder(@QueryParam("id") String id) {
		if (IntegerUtils.isString2Integer(id)) {
			List<TaxiOrder> lstValue = ((TaxiOrderController) ControllerUtils
					.getController(TaxiOrderController.class)).find(
					"from TaxiOrder where id = ?", new Object[] { new Integer(
							id) });
			if (lstValue != null && lstValue.size() > 0) {
				TaxiOrder taxiOrder = lstValue.get(0);
				JsonTaxiOrderResult jto = new JsonTaxiOrderResult();
				jto.setId(taxiOrder.getId());
				jto.setPhoneNumber(taxiOrder.getPhoneNumber());
				jto.setRepeatTime(taxiOrder.getRepeatTime());
				if (taxiOrder.getRegistedTaxis() != null) {
					List<JsonTaxi> lstTaxi = new ArrayList<JsonTaxi>();
					Iterator<com.vietek.taxioperation.model.Vehicle> ite = taxiOrder
							.getRegistedTaxis().iterator();
					while (ite.hasNext()) {
						com.vietek.taxioperation.model.Vehicle taxi = ite
								.next();
						JsonTaxi jsonTaxi = new JsonTaxi();
						jsonTaxi.setId(taxi.getId());
						jsonTaxi.setLicensePlate(taxi.getTaxiNumber());
						jsonTaxi.setVehicleNumber(taxi.getValue());
						jsonTaxi.setTaxiType(taxi.getTaxiType().getName());
						lstTaxi.add(jsonTaxi);
					}
				} else {
					jto.setRegistedTaxis(null);
				}

				com.vietek.taxioperation.model.Vehicle taxi = taxiOrder
						.getPickedTaxi();
				if (taxi != null) {
					JsonTaxi jsonTaxi = new JsonTaxi();
					jsonTaxi.setId(taxi.getId());
					jsonTaxi.setLicensePlate(taxi.getTaxiNumber());
					jsonTaxi.setVehicleNumber(taxi.getValue());
					jsonTaxi.setTaxiType(taxi.getTaxiType().getName());
					jto.setPickedTaxi(jsonTaxi);
				} else {
					jto.setPickedTaxi(null);
				}
				Customer customer = taxiOrder.getCustomer();
				if (customer != null) {
					JsonCustomer jsonCustomer = new JsonCustomer();
					jsonCustomer.setId(customer.getId());
					jsonCustomer.setName(customer.getName());
					jsonCustomer.setEmail(customer.getEmail());
					jsonCustomer.setAddress(customer.getAddress());
					jsonCustomer.setIsVip(customer.getIsVIP());
					jto.setCustomer(jsonCustomer);
				} else {
					jto.setCustomer(null);
				}
				jto.setOrderType(taxiOrder.getOrderType());
				jto.setNote(taxiOrder.getNote());
				jto.setBeginOrderAddress(taxiOrder.getBeginOrderAddress());
				jto.setBeginOrderLat(taxiOrder.getBeginOrderLat());
				jto.setBeginOrderLon(taxiOrder.getBeginOrderLon());
				jto.setBeginAddress(taxiOrder.getBeginAddress());
				jto.setBeginLat(taxiOrder.getBeginLat());
				jto.setBeginLon(taxiOrder.getBeginLon());
				jto.setEndOrderAddress(taxiOrder.getEndOrderAddress());
				jto.setEndOrderLat(taxiOrder.getEndOrderLat());
				jto.setEndOrderLon(taxiOrder.getEndOrderLon());
				jto.setEndAddress(taxiOrder.getEndAddress());
				jto.setEndLat(taxiOrder.getEndLat());
				jto.setEndLon(taxiOrder.getEndLon());
				jto.setOrderTime(taxiOrder.getOrderTime());
				jto.setBeginOrderTime(taxiOrder.getBeginOrderTime());
				jto.setBeginTime(taxiOrder.getBeginTime());
				jto.setEndTime(taxiOrder.getEndTime());
				jto.setFareEstimate(taxiOrder.getFareEstimate());
				jto.setFareActual(taxiOrder.getFareActual());
				jto.setFareGps(taxiOrder.getFareGps());
				jto.setTwoWay(taxiOrder.getTwoWay());
				Driver driver = taxiOrder.getDriver();
				if (driver != null) {
					JsonDriver jsonDriver = new JsonDriver();
					jsonDriver.setId(driver.getId());
					jsonDriver.setName(driver.getName());
					jsonDriver.setPhoneNumber(driver.getPhoneOffice());
					jsonDriver.setStaffCard(driver.getStaffCard());
					jsonDriver.setValue(driver.getValue());
					jto.setDriver(jsonDriver);
				} else {
					jto.setDriver(null);
				}
				jto.setFixedPrice(taxiOrder.getFixedPrice());
				jto.setStatus(taxiOrder.getStatus());
				jto.setRate(taxiOrder.getRate());
				// jto.setTypeTaxiOrder(taxiOrder.getTypeTaxiOrder());
				return jto;
			} else {
				return null;
			}
		}
		return null;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/resetPasswordDriver")
	public Response resetPasswordDriver(
			@QueryParam("phonenumber") String phoneNumber) {
		
		DriverController driverController = ((DriverController) ControllerUtils
				.getController(DriverController.class));
		
		List<Driver> lstValue = driverController
				.find("from Driver where phoneOffice = '" + phoneNumber + "'");
		if (lstValue.size() != 1) {
		} else {
			Driver driver = lstValue.get(0);
			String newPass = driverController.resetPassword(lstValue.get(0));
			
			NotificationUtils.sendNotification(driver.getPhoneOffice(), "", "MaiLinhDriver - Lay lai mat khau", "MaiLinhDriver - Mat khau moi: " + newPass);
		}
		return Response.status(200).build();
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/resetPassword")
	public ResetPasswordResult resetPassword(
			@QueryParam("phonenumber") String phoneNumber) {
		
		CustomerController customerController = ((CustomerController) ControllerUtils
				.getController(CustomerController.class));
		
		List<Customer> lstValue = customerController
				.find("from Customer where phoneNumber = '" + phoneNumber + "'");
		if (lstValue.size() != 1) {
			return new ResetPasswordResult(0);
		} else {
			Customer customer = lstValue.get(0);
			if (customer.getResetPasswordCount() > 5) {
				return new ResetPasswordResult(0);
			}
			String newPass = customerController.resetPassword(lstValue.get(0));
			
			NotificationUtils.sendNotification(customer.getPhoneNumber(), customer.getEmail(), "MaiLinhTaxi - Reset password", "MaiLinhTaxi - New password: " + newPass);
			// send sms
			customer.setResetPasswordCount(customer.getResetPasswordCount() + 1);
			return new ResetPasswordResult(1);
		}
	}

	class ResetPasswordResult {
		private int status;

		public ResetPasswordResult(int status) {
			super();
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateAccountInfo")
	public Response updateAccountInfo(InputStream incomingData) {
		JsonObject jsonObj;
		JsonReader reader = Json.createReader(incomingData);
		jsonObj = reader.readObject();
		String userPhone = jsonObj.getString("userPhone");
		String userName = jsonObj.getString("userName");
		String userEmail = jsonObj.getString("userEmail");
		String userAvatar = jsonObj.getString("userAvatar");
		List<Customer> lstValue = ((CustomerController) ControllerUtils
				.getController(CustomerController.class))
				.find("from Customer where phoneNumber = '" + userPhone + "'");
		if (lstValue.size() == 1) {
			Customer customer = lstValue.get(0);
			customer.setEmail(userEmail);
			customer.setName(userName);
			customer.setAvatar(userAvatar.getBytes());
			customer.save();
			CustomerController.reload(customer);
		}
		return Response.status(200).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getHistory")
	public ArrayList<TripSummary> getHistory(
			@QueryParam("phonenumber") String phoneNumber) {
		// List<TaxiOrder> lstValue = ((TaxiOrderController) ControllerUtils
		// .getController(TaxiOrderController.class))
		// .find("from TaxiOrder where phoneNumber = '" + phoneNumber
		// + "' Order by beginOrderTime DESC 10");
		Session session = ControllerUtils.getCurrentSession();
		Query query = session
				.createQuery("from TaxiOrder where phoneNumber = '"
						+ phoneNumber + "' Order by beginOrderTime DESC");
		query.setMaxResults(10);
		@SuppressWarnings("unchecked")
		List<TaxiOrder> lstValue = query.list();
		session.close();
		ArrayList<TripSummary> retVal = new ArrayList<>();
		for (TaxiOrder order : lstValue) {
			TripSummary trip = new TripSummary();
			trip.startAddress = order.getBeginAddress();
			trip.endAddress = order.getEndAddress();
			trip.beginTime = order.getBeginOrderTime().toString();
			trip.setStatus(order.getStatus());
			trip.setTrip_id(order.getId() + "");
			retVal.add(trip);
		}
		return retVal;
	}

	class TripSummary {
		private String startAddress;
		private String endAddress;
		private String beginTime;
		private int status;
		private String trip_id;

		public String getStartAddress() {
			return startAddress;
		}

		public void setStartAddress(String startAddress) {
			this.startAddress = startAddress;
		}

		public String getEndAddress() {
			return endAddress;
		}

		public void setEndAddress(String endAddress) {
			this.endAddress = endAddress;
		}

		public String getBeginTime() {
			return beginTime;
		}

		public void setBeginTime(String beginTime) {
			this.beginTime = beginTime;
		}

		public String getTrip_id() {
			return trip_id;
		}

		public void setTrip_id(String trip_id) {
			this.trip_id = trip_id;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

	}

	private static DriverRegisterController dRegisterController = (DriverRegisterController) ControllerUtils
			.getController(DriverRegisterController.class);
	private static DriverController driverController = (DriverController) ControllerUtils
			.getController(DriverController.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/driver_register")
	public DriverResult driverRegister(
			@QueryParam("staff_number") String staffNumber,
			@QueryParam("pass") String password, @QueryParam("uuid") String uuid) {
		List<Driver> tmpLst = driverController.find(
				"from Driver where staffCard = ?",
				Integer.parseInt(staffNumber));
		if (tmpLst.size() == 1) {
			Driver driver = tmpLst.get(0);
			if (driver.getIsAppRegister() == null || !driver.getIsAppRegister()) {
				// if (driver.getIsAppRegister() == null
				// || !driver.getIsAppRegister()) {
				List<DriverRegister> lstDRegister = dRegisterController
						.find("from DriverRegister where staffNumber = ? and (isProcessed = 0 or isProcessed is null)",
								staffNumber + "");
				if (lstDRegister.size() <= 0) {
					DriverRegister dRegister = new DriverRegister();
					dRegister.setDriver(driver);
					dRegister.setIsProcessed(false);
					dRegister.setIsApproved(false);
					dRegister.setIsActive(false);
					dRegister.setPassword(password);
					dRegister.setStaffNumber(staffNumber);
					dRegister.setUuid(uuid);
					dRegister.save();
					return new DriverResult(0, "Đăng ký thành công",
							driver.getId());
				} else {
					return new DriverResult(-1,
							"Bạn đã đăng ký và đang chờ xử lý", -1);
				}
			} else {
				return new DriverResult(-1,
						"Thiết bị đã được đăng ký cho tài xế khác", -1);
			}
		}
		return new DriverResult(-1, "Mã nhân viên không chính xác!", -1);
	}

	class DriverResult {
		private int status;
		private String message;
		private int driver_id;

		public DriverResult(int status, String message, int driver_id) {
			super();
			this.status = status;
			this.message = message;
			this.driver_id = driver_id;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getDriver_id() {
			return driver_id;
		}

		public void setDriver_id(int driver_id) {
			this.driver_id = driver_id;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/driver_login")
	public DriverResult driverLogin(
			@QueryParam("staff_number") String staffNumber,
			@QueryParam("pass") String password, @QueryParam("uuid") String uuid) {
		List<Driver> tmpLst = driverController.find(
				"from Driver where staffCard = ?",
				Integer.parseInt(staffNumber));
		if (tmpLst.size() == 1) {
			Driver driver = tmpLst.get(0);
			String mobileUUID = driver.getMobileUUID();
			if (mobileUUID.equalsIgnoreCase(uuid)) {
				if (driver.getPassword().equals(password)) {
					DriverResult result = new DriverResult(0,
							"Dang nhap thanh cong", driver.getId());
					return result;
				} else {
					return new DriverResult(-1, "Sai mat khau", -1);
				}
			} else {
				return new DriverResult(-1,
						"Dien thoai chua duoc dang ky voi he thong", -1);
			}
		}
		return new DriverResult(-1, "Kiem tra thong tin dang nhap", -1);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get_driver_info")
	public DriverInfo getDriverInfo(@QueryParam("driver_id") String driver_id) {
		DriverInfo info = new DriverInfo();
		List<Driver> tmpLst = driverController.find("from Driver where id = ?",
				Integer.parseInt(driver_id));
		if (tmpLst.size() == 1) {
			Driver driver = tmpLst.get(0);
			info.setName(driver.getName());
			info.setRate(driver.getRate());
			info.setPhone(driver.getPhoneOffice());
			info.setStaffCard(driver.getStaffCard() + "");
			info.setAvatar(driver.getAvatar() == null ? "" : new String(driver
					.getAvatar()));
		} else {
			info.setAvatar("");
		}
		return info;
	}

	class DriverInfo {
		private String name;
		private double rate;
		private String avatar;
		private String staffCard;
		private String phone;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getRate() {
			return rate;
		}

		public void setRate(double rate) {
			this.rate = rate;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getStaffCard() {
			return staffCard;
		}

		public void setStaffCard(String staffCard) {
			this.staffCard = staffCard;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateDrivertInfo")
	public Response updateDriverInfo(InputStream incomingData) {
//		JsonObject jsonObj;
//		JsonReader reader = Json.createReader(incomingData);
//		jsonObj = reader.readObject();
//		String driverId = jsonObj.getString("driver_id");
//		String driverPhone = jsonObj.getString("driverPhone");
//		String driverName = jsonObj.getString("driverName");
//		String driverAvatar = jsonObj.getString("driverAvatar");
//		List<Driver> tmpLst = driverController.find("from Driver where id = ?",
//				Integer.parseInt(driverId));
//		if (tmpLst.size() == 1) {
//			Driver driver = tmpLst.get(0);
//			driver.setName(driverName);
//			driver.setPhoneNumber(driverPhone);
//			driver.setAvatar(driverAvatar.getBytes());
//			driver.save();
//		}
		return Response.status(200).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get_vehicle")
	public VehicleInfo getVehicle(@QueryParam("driver_id") String driver_id) {
		VehicleInfo vehicle = new VehicleInfo();
		vehicle.vehicleId = 0;
		Session session = ControllerUtils.getCurrentSession();
		List<?> lstVehicle = session.createSQLQuery(
				"call vt_tms.getVerhicleInfo(" + driver_id + ")").list();
		session.close();
		try {
			Driver driver = DriverController.getDriver(Integer.parseInt(driver_id));
			if (lstVehicle.size() == 1 && driver.getIsAppRegister()) {
				Object[] info = (Object[]) lstVehicle.get(0);
				vehicle.setVehicleId(Integer.parseInt(info[0].toString()));
				vehicle.setVehicleNumber(info[1].toString());
				vehicle.setLicencePlate(info[2].toString());
				vehicle.setSeat(Integer.parseInt(info[4].toString()));
				vehicle.setDeviceId(Integer.parseInt(info[5].toString()));
			}
		}
		catch (Exception e) {
			vehicle.vehicleId = 0;
		}
		return vehicle;
	}

	class VehicleInfo {
		private int vehicleId;
		private String VehicleNumber;
		private String LicencePlate;
		private int seat;
		private int deviceId;

		public int getVehicleId() {
			return vehicleId;
		}

		public void setVehicleId(int vehicleId) {
			this.vehicleId = vehicleId;
		}

		public String getLicencePlate() {
			return LicencePlate;
		}

		public void setLicencePlate(String licencePlate) {
			LicencePlate = licencePlate;
		}

		public int getSeat() {
			return seat;
		}

		public void setSeat(int seat) {
			this.seat = seat;
		}

		public String getVehicleNumber() {
			return VehicleNumber;
		}

		public void setVehicleNumber(String vehicleNumber) {
			VehicleNumber = vehicleNumber;
		}

		public int getDeviceId() {
			return deviceId;
		}

		public void setDeviceId(int deviceId) {
			this.deviceId = deviceId;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get_callcenter")
	public CallCenter[] getCallCenter() {
		Session session = ControllerUtils.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<com.vietek.taxioperation.model.CallCenter> lstTmp = session
				.createQuery("from CallCenter").list();
		session.close();
		CallCenter[] lstCallCenter = new CallCenter[lstTmp.size()];
		for (int i = 0; i < lstTmp.size(); i++) {
			CallCenter callCenter = new CallCenter();
			callCenter.setName(lstTmp.get(i).getCallname());
			callCenter.setNumber(lstTmp.get(i).getPhonenmber());
			lstCallCenter[i] = callCenter;
		}
		return lstCallCenter;
	}

	class CallCenter {
		private String name;
		private String number;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/push_feedbackCustomer")
	public Integer getFeedBackCustomer(@QueryParam("type_id") Integer typeid,
			@QueryParam("point") Double point,
			@QueryParam("phonenumber") String phone,
			@QueryParam("content") String conten) {
		Session session = ControllerUtils.getCurrentSession();
		CustomerFeedBack feedbacktem = new CustomerFeedBack();
		feedbacktem.setFeedbacktype(typeid);
		feedbacktem.setPoint(point);
		feedbacktem.setConten(conten);
		if (typeid == 1) {
			@SuppressWarnings("unchecked")
			List<Customer> customerTmp = session.createQuery(
					"from Customer where phoneNumber = '" + phone + "'").list();
			if (customerTmp.size() == 1) {
				feedbacktem.setCustomer(customerTmp.get(0));
			}
			session.close();	
		} else if (typeid == 2) {
			@SuppressWarnings("unchecked")
			List<Driver> driverTmp = session.createQuery(
					"from Driver where phoneNumber ='" + phone + "'").list();
			if (driverTmp.size() == 1) {
				feedbacktem.setDriver(driverTmp.get(0));
			}
			session.close();
		} else {
			return 0;
		}
		
		try {
			feedbacktem.save();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/push_SOSNotification")
	public Integer pushSOSNotification(@QueryParam("driver_id") String driver_id,
			@QueryParam("longtitude") double longtitude, 
			@QueryParam("latitude") double latitude){
			Session session = ControllerUtils.getCurrentSession();
			@SuppressWarnings("unchecked")
			List<Driver> lstDriver = session.createQuery("from Driver where id = '" + driver_id + "'").list();
			Driver driver = new Driver();
			if(lstDriver != null && lstDriver.size() > 0){
				driver = lstDriver.get(0);
			}else{
				return 0;
			}
			Timestamp toTimeStamp = new Timestamp(System.currentTimeMillis());
			Timestamp fromTimeStamp = new Timestamp(System.currentTimeMillis() - CommonDefine.TIME_VALUE_SOS_NOTIFICATION*60*1000);
			String mysql = "from SOSNotification WHERE driver_id = '" + driver_id + "' AND startTime BETWEEN '" + fromTimeStamp +"' AND '"  + toTimeStamp +"' ORDER BY id DESC";
			Query query = session.createQuery(mysql);
			@SuppressWarnings("unchecked")
			List<SOSNotification> lst = query.list();
			session.close();
			if(lst == null || lst.size() == 0){
				SOSNotification sosNotification = new SOSNotification();
				sosNotification.setDriver(driver);
				sosNotification.setLongtitude(longtitude);
				sosNotification.setLatitude(latitude);
				sosNotification.setStartTime(new Timestamp(new Date().getTime()));
				try {
					sosNotification.save();
					return 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				SOSNotification sosNotification = lst.get(0);
				sosNotification.setEndTime(new Timestamp(new Date().getTime()));
				try {
					sosNotification.save();
					return 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		return 0;
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/push_NotificationsFromDriver")
	public Integer pushDriverNotification(@QueryParam("driver_id") String driver_id, 
			@QueryParam("typeNotification") Integer type,
			@QueryParam("longtitude") double longtitude,
			@QueryParam("latitude") double latitude){
		Session session = ControllerUtils.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Driver> lstDriver = session.createQuery("from Driver where id = '" + driver_id +"'").list();
		session.close();
		Driver driver = new Driver();
		if(lstDriver != null && lstDriver.size() > 0){
			driver = lstDriver.get(0);
		}else{
			return 0;
		}
		NotificationsFromDriver model = new NotificationsFromDriver();
		model.setDriver(driver);
		model.setLatitude(latitude);
		model.setLongtitude(longtitude);
		model.setTypeNotification(type);
		model.setTimeSend(new Timestamp(new Date().getTime()));
		try{
			model.save();
			return 1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateRegId")
	/*-- Update RegId apps : type: 0: Customer; 1: Driver --*/
	public Integer updateRegId(@QueryParam("type") String type, @QueryParam("Id") String id, @QueryParam("regId") String regId){
		Session session = ControllerUtils.getCurrentSession();
		try {
			/*--- type = 0 : In case Customer --*/
			if(StringUtils.equals(type, CommonDefine.TYPE_APP_CUSTOMER)){
				@SuppressWarnings("unchecked")
				List<Customer> lst = session.createQuery("from Customer where id = '" + id + "' or phoneNumber = '" + id + "'").list();
				session.close();
				if(lst != null && lst.size() > 0){
					Customer customer = lst.get(0);
						customer.setRegId(regId);
						customer.save();
						CustomerController.reload(customer);
						return 1;
				}else{
					return 0;
				}
			}else{ /* --- In case Driver --- */
				@SuppressWarnings("unchecked")
				List<DriverGCMInfo> lst = session.createQuery("from DriverGCMInfo where id = '" + id + "'").list();
				if(lst != null && lst.size() > 0){
					session.close();
					DriverGCMInfo driver = lst.get(0);
					if(StringUtils.isEmpty(driver.getRegId())){
						driver.setRegId(regId);
						driver.save();
						return 1;
					}else{
						return 0;
					}
				}else if(lst == null || lst.size() == 0){
					@SuppressWarnings("unchecked")
					List<Driver> lstDriver = session.createQuery("from Driver where id = '" + id + "'").list();
						session.close();
					if(lstDriver != null && lstDriver.size() > 0){
						DriverGCMInfo driverInfo = new DriverGCMInfo();
						driverInfo.setDriver(lstDriver.get(0));
						driverInfo.setRegId(regId);
						driverInfo.save();
						return 1;
					}else{
						return 0;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/push_notifyToCustomer")
	/**
	 * type apps
	 * 1: Android ; 2: IOS
	 * 
	 * status of trip
	 * -1 : trip status waiting ; 0 : trip status cancel ; 1 : trip status new ; 2 : trip status driver registered; 3: trip status driver come ; 4 : trip status done
	 * */
	public Integer push_notifyToCustomer(@QueryParam("type") Integer type,
			@QueryParam("status") Integer status,
			@QueryParam("phoneNumber") String phoneNumber,
			@QueryParam("staffNumber") String staffNumber,
			@QueryParam("driverName") String driverName){
		Session session = ControllerUtils.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Customer> lst = session.createQuery("from Customer where phoneNumber = '" + phoneNumber + "'").list();
		session.close();
		if(lst != null && lst.size() > 0){
			Customer customer = lst.get(0);
			List<String> lstRegId = new LinkedList<String>();
			lstRegId.add(customer.getRegId());
			StringBuffer msg = new StringBuffer();
			if(status == CommonDefine.TRIP_STATUS_DRIVER_REGISTERED){
				msg.append(CommonDefine.DRIVER_NAME).append(driverName).append(", ").append(CommonDefine.DRIVER_NUMBER).append(staffNumber).append(CommonDefine.TRIP_STATUS_DRIVER_REGISTERED_CONTENT);
			}else if(status == CommonDefine.TRIP_STATUS_DRIVER_COME){
				msg.append(CommonDefine.DRIVER_NAME).append(driverName).append(", ").append(CommonDefine.DRIVER_NUMBER).append(staffNumber).append(CommonDefine.TRIP_STATUS_DRIVER_COME_CONTENT);
			}else if(status == CommonDefine.TRIP_STATUS_CANCELED){
				msg.append(CommonDefine.DRIVER_NAME).append(driverName).append(", ").append(CommonDefine.DRIVER_NUMBER).append(staffNumber).append(CommonDefine.TRIP_CANCELED_REASON);
			}
			Content content  = Content.createContent(msg.toString(),lstRegId);
			String apiKey = "";
			if(type == CommonDefine.TYPE_APP_ANDROID){
				apiKey = CommonDefine.API_KEY_ANDROID;
			}else{
				apiKey = CommonDefine.API_KEY_IOS;
			}
			try {
				GCMUtils.sendToDevices(apiKey, content);
				return 1;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			
		}else {
			return 0;
		}
	}
	
	class NotificationFromDriver{
		private double latitude;
		private double longtitude;
		private int type;
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public double getLongtitude() {
			return longtitude;
		}
		public void setLongtitude(double longtitude) {
			this.longtitude = longtitude;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		
	}
	
	/*-- Service list all notify and info about rider, taxi online --*/
	@SuppressWarnings("resource")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get_groupInfoDriver")
	public GroupInfoDriver getGroupInfoDriver(@QueryParam("latitude") double latitude,
			@QueryParam("longtitude") double longtitude){
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		GroupInfoDriver groupInfoDriver = new GroupInfoDriver();
		groupInfoDriver.setStatus("true");
		String tempDistance = ConfigUtil.getConfig("NOTIFICATION_RADIUS");
		int distance = CommonDefine.DEFAULT_DISTANCE;
		if(StringUtils.isNotEmpty(tempDistance)){
			distance = Integer.parseInt(tempDistance);
		}
		String tempTime = ConfigUtil.getConfig("NOTIFICATION_TIME_RANK");
		int timeRank = CommonDefine.DEFAULT_TIME;
		if(StringUtils.isNotEmpty(tempTime)){
			timeRank = Integer.parseInt(tempTime);
		}
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		List<NotificationFromDriver> lstTemp = new ArrayList<NotificationFromDriver>();
		List<TaxiOnline> listTaxi = new ArrayList<TaxiOnline>();
		List<CustomerOrder> lstCustomer = new ArrayList<CustomerOrder>();
		try {
			conn = sessionImplementor.connection();
			if(conn == null){
				return null;
			}
			/*-- Get all Notification from Driver --*/
			cs = conn.prepareCall("call vt_tms.cmdGetNotificationFromDriver(?,?,?,?)");
			cs.setDouble(1, longtitude);
			cs.setDouble(2, latitude);
			cs.setInt(3, distance);
			cs.setInt(4, timeRank);
			rs = cs.executeQuery();
			while(rs.next()){
				NotificationFromDriver bean = new NotificationFromDriver();
				bean.setLatitude(rs.getDouble("latitude"));
				bean.setLongtitude(rs.getDouble("longtitude"));
				bean.setType(rs.getInt("typeNotification"));
				lstTemp.add(bean);
			}
			if(lstTemp.size() > 0){
				groupInfoDriver.setLstNotification(lstTemp);
				groupInfoDriver.setStatus("true");
			}else{
				groupInfoDriver.setLstNotification(new ArrayList<NotificationFromDriver>());
			}
			
			/*-- Get all Taxi Online --*/
			listTaxi = GetTaxiOnline.getTaxi(longtitude, latitude, distance, CommonDefine.MAX_TAXI_ONLINE, 0);
			if(listTaxi.size() > 0){
				groupInfoDriver.setLstTaxi(listTaxi);
				groupInfoDriver.setStatus("true");
			}else{
				groupInfoDriver.setLstTaxi(new ArrayList<TaxiOnline>());
			}
			
			/*-- Get all customer order --*/
			List<Rider> lstRider = Rider.getAvaiableRider(longtitude, latitude, distance);
			if(lstRider != null && lstRider.size() > 0){
				for (Rider rider : lstRider) {
					if(rider.getCustomer() != null){
						CustomerOrder customer = new CustomerOrder();
						customer.setCustomerId(rider.getCustomer().getId());
						customer.setName(rider.getCustomer().getName());
						customer.setPhoneNumber(rider.getCustomer().getPhoneNumber());
						customer.setBeginOrderLat(rider.getLattitute());
						customer.setBeginOrderLong(rider.getLongtitute());
						lstCustomer.add(customer);
					}
				}
			}
			groupInfoDriver.setLstCustomer(lstCustomer);
			return groupInfoDriver;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.close();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
	
}
