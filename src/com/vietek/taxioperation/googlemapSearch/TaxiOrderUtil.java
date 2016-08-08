package com.vietek.taxioperation.googlemapSearch;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.EnumCancelReason;
import com.vietek.taxioperation.common.EnumOrderType;
import com.vietek.taxioperation.common.EnumStatus;
import com.vietek.taxioperation.controller.CustomerController;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.util.Address;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.StandandPhoneNumber;

public class TaxiOrderUtil {

	public static TaxiOrder createTaxiOrder(TaxiOrder taxiOrder) {
		TaxiOrder reVal = new TaxiOrder();

		reVal.setPhoneNumber(taxiOrder.getPhoneNumber());
		reVal.setBeginOrderTime(taxiOrder.getBeginOrderTime());
		reVal.setOrderTime(taxiOrder.getOrderTime());
		reVal.setTimeGoiNhac(taxiOrder.getTimeGoiNhac());

		reVal.setUser(taxiOrder.getUser());
		reVal.setCustomer(taxiOrder.getCustomer());
		// reVal.setRegistedTaxis(taxiOrder.getRegistedTaxis());

		reVal.setCancelReason(taxiOrder.getCancelReason());
		reVal.setStatus(taxiOrder.getStatus());
		reVal.setOrderType(taxiOrder.getOrderType());
		reVal.setOrderCarType(taxiOrder.getOrderCarType());

		/*
		 * order car type
		 */
		reVal.setOrderCarType(taxiOrder.getOrderCarType());
		/*
		 * repeat time
		 */
		reVal.setRepeatTime(taxiOrder.getRepeatTime());

		/*
		 * field begin address order
		 */
		reVal.setBeginAddress(taxiOrder.getBeginAddress());
		reVal.setBeginLat(taxiOrder.getBeginLat());
		reVal.setBeginLon(taxiOrder.getBeginLon());

		reVal.setBeginOrderAddress(taxiOrder.getBeginOrderAddress());
		reVal.setBeginOrderLat(taxiOrder.getBeginOrderLat());
		reVal.setBeginOrderLon(taxiOrder.getBeginOrderLon());

		/*
		 * end address order
		 */
		reVal.setEndAddress(taxiOrder.getEndAddress());
		reVal.setEndLat(taxiOrder.getEndLat());
		reVal.setEndLon(taxiOrder.getEndLon());

		reVal.setEndOrderAddress(taxiOrder.getEndOrderAddress());
		reVal.setEndOrderLat(taxiOrder.getEndOrderLat());
		reVal.setEndOrderLon(taxiOrder.getEndOrderLon());

		/* tong tien */
		reVal.setMoneytotal(taxiOrder.getMoneytotal());
		/* thoi gian phu troi */
		reVal.setTimeAdd(taxiOrder.getTimeAdd());
		/* km phu troi */
		reVal.setPriceKmadd(taxiOrder.getPirceKmadd());
		/* thoi gian du tinh */
		reVal.setTimect(taxiOrder.getTimect());
		/*
		 * price Km
		 */
		reVal.setPriceOrderKm(taxiOrder.getPriceOrderKm());
		/*
		 * price
		 */
		reVal.setFareEstimate(taxiOrder.getFareEstimate());
		/*
		 * price add
		 */
		reVal.setFareAdd(taxiOrder.getFareAdd());
		/*
		 * go normal
		 */
		reVal.setGoNormal(taxiOrder.getGoNormal());
		/*
		 * go contract
		 */
		reVal.setConTract(taxiOrder.getConTract());
		/*
		 * go air
		 */
		reVal.setAirStation(taxiOrder.getAirStation());
		/*
		 * go people
		 */
		reVal.setSomePerson(taxiOrder.getSomePerson());
		/*
		 * way
		 */
		reVal.setTwoWay(taxiOrder.getTwoWay());
		/*
		 * start now
		 */
		reVal.setStartNow(taxiOrder.getStartNow());
		/*
		 * sometime
		 */
		reVal.setSomeTime(taxiOrder.getSomeTime());
		/*
		 * note
		 */
		reVal.setNote(taxiOrder.getNote());
		/*
		 * chanel
		 */
		reVal.setChannel(taxiOrder.getChannel());
		/*
		 * created
		 */
		reVal.setCreateBy(taxiOrder.getCreateBy());
		reVal.setUpdateBy(taxiOrder.getUpdateBy());
		/*
		 * user
		 */
		reVal.setUser(taxiOrder.getUser());
		/*
		 * Update to database
		 */
		reVal.setLoadData(taxiOrder.getLoadData());
		return reVal;
	}

	public static TaxiOrder createTaxiOrder(String numberPhone, Address addressCurrent) {

		TaxiOrder taxiOrder = createTaxiOrder(numberPhone);
		taxiOrder.setBeginAddress(addressCurrent.getName());
		taxiOrder.setBeginLat(addressCurrent.getLatitude());
		taxiOrder.setBeginLon(addressCurrent.getLongitude());
		return taxiOrder;
	}

	public static TaxiOrder createTaxiOrder(String numberPhone) {

		String phoneNumber = StandandPhoneNumber.standandPhone(numberPhone);

		TaxiOrder taxiOrder = new TaxiOrder();

		taxiOrder = new TaxiOrder();
		taxiOrder.setPhoneNumber(phoneNumber);
		Timestamp timeNow = new Timestamp(System.currentTimeMillis());
		taxiOrder.setBeginOrderTime(timeNow);
		taxiOrder.setOrderTime(timeNow);
		taxiOrder.setTimeGoiNhac(timeNow);

		taxiOrder.setUser(Env.getUser());

		Customer cust = CustomerController.getOrCreateCustomer(phoneNumber);

		taxiOrder.setCustomer(cust);
		taxiOrder.setRegistedTaxis(new HashSet<Vehicle>());

		taxiOrder.setCancelReason(EnumCancelReason.CREATING.getValue());
		taxiOrder.setStatus(EnumStatus.CREATING.getValue());
		taxiOrder.setOrderType(EnumOrderType.CREATING.getValue());
		return taxiOrder;
	}

	public static TaxiOrder getTaxiOrder(String numberPhone) {

		TaxiOrder taxiOrder = null;
		if (numberPhone != "")
			taxiOrder = TripManager.sharedInstance.getProcessingTaxiOrder(numberPhone);

		if (taxiOrder == null)
			taxiOrder = createTaxiOrder(numberPhone);

		taxiOrder.setUser(Env.getUser());
		return taxiOrder;
	}

	@SuppressWarnings("unchecked")
	public static TaxiOrder getLastTaxiOrderDB(String numberPhone) {
		TaxiOrder taxiOrder = null;

		String sql = "from TaxiOrder where phoneNumber = :phone ORDER BY created DESC";
		List<TaxiOrder> lstOrder = new ArrayList<>();

		Session session = ControllerUtils.getCurrentSession();
		try {
			if (!session.isOpen() || !session.isConnected())
				return createTaxiOrder(numberPhone);

			Query query;
			query = session.createQuery(sql).setParameter("phone", numberPhone);

			query.setFirstResult(0);
			query.setMaxResults(1);

			query.setCacheable(true);
			lstOrder = query.list();

			if (lstOrder.size() > 0) {
				taxiOrder = lstOrder.get(0);
			} else
				taxiOrder = createTaxiOrder(numberPhone);
		} catch (Exception ex) {
			AppLogger.logDebug.error("", ex);
		} finally {
			if (session.isOpen())
				session.close();
		}
		if (taxiOrder == null)
			taxiOrder = createTaxiOrder(numberPhone);
		return taxiOrder;

	}

	static void resetRepeatTimeCall(String numberPhone) {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		sessionImplementor.connection();
		Connection conn = null;
		CallableStatement callableStatement;
		ResultSet rs = null;
		String sql = "";

		conn = sessionImplementor.connection();
		try {
			sql = "{Call resetRepeatTimeCall(?)}";
			if (conn != null && !conn.isClosed()) {

				callableStatement = conn.prepareCall(sql);
				callableStatement.setString(1, numberPhone);
				rs = callableStatement.executeQuery();

				if (rs.next()) {

					if (rs.getString("result").equals("OK")) {
					}
					;
				}
				callableStatement.cancel();
				callableStatement.close();
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public static Customer getCustomer(String phone) {

		Customer customer = CustomerController.getOrCreateCustomer(phone);
		return customer;
	}
}
