package com.vietek.taxioperation.realtime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.Session;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.EnumStatus;
import com.vietek.taxioperation.model.ChannelTms;
import com.vietek.taxioperation.model.SysUser;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.StandandPhoneNumber;

public class TripManager {
	public static TripManager sharedInstance = new TripManager();
	public static final List<Trip> LST_TRIP_PROCESSING = new ArrayList<>();
//	private ConcurrentHashMap<String, Trip> mapTrips = new ConcurrentHashMap<>();
	
	public static ConcurrentHashMap<String, Trip> mapTrips = new ConcurrentHashMap<>();

	public synchronized void addTrip(Trip trip, Rider rider) {
		mapTrips.put(trip.getId(), trip);
		trip.setRider(rider);
		trip.setTripDispatcher(new TripDispatcher(trip, rider));
	}

	public synchronized void addTrip(TaxiOrder order) {

		Rider rider = Rider.getRider(order.getCustomer().getPhoneNumber());
		Trip trip = new Trip();
		trip.setRider(rider);
		trip.setOrder(order);
		order.setIsAutoOperation(true);
		TripManager.sharedInstance.addTrip(trip, rider);
		trip.saveToDataBase();
	}
	
	public synchronized void saveTrip(TaxiOrder order){
		Rider rider = Rider.getRider(order.getCustomer().getPhoneNumber());
		Trip trip = new Trip();
		trip.setRider(rider);
		trip.setOrder(order);
		trip.saveToDataBase();
	}

	public Trip getTrip(String trip_id) {
		Trip trip = mapTrips.get(trip_id);
		if (trip == null) {
			// Restore from database
			trip = new Trip();
			Session session = ControllerUtils.getCurrentSession();
			TaxiOrder order = (TaxiOrder) session.createQuery("from TaxiOrder WHERE id = '" + trip_id + "'").list()
					.get(0);
			trip.setOrder(order);
			if (order.getCustomer() != null) {
				trip.setRider(Rider.getRider(order.getCustomer().getPhoneNumber()));
			} else {
				trip.setRider(Rider.getRiderVL(order.getDriverIntroduced()));
			}
			session.close();
			mapTrips.put(trip.getId(), trip);
		}
		return trip;
	}

	public Trip getTrip(TaxiOrder order) {
		Trip trip = mapTrips.get(order.getId() + "");
		if (trip == null) {
			trip = new Trip();
			trip.setOrder(order);
			mapTrips.put(trip.getId(), trip);
		}
		return trip;
	}

	public ConcurrentHashMap<String, Trip> getMapTrips() {
		return mapTrips;
	}
	
	public List<TaxiOrder> getListOrderByChannel(int minute, ChannelTms channel){
		List<TaxiOrder> lstOrder = new ArrayList<TaxiOrder>();
		if(mapTrips.isEmpty()){
			return lstOrder;
		}
		for (String key : mapTrips.keySet()) {
			Trip trip = mapTrips.get(key);
			if (trip.getOrder().getBeginOrderTime().getTime() > (System.currentTimeMillis() - (minute * 60l * 1000l)) 
					&& trip.getOrder().getChannel().getId() == channel.getId()
						&& trip.getOrder().getPhoneNumber() != null
							&& !trip.getOrder().getIsAutoOperation()) {
				lstOrder.add(trip.getOrder());
			}
		}
		return lstOrder;
	}
	
	public List<TaxiOrder> getListOrderForDTV(int minute, SysUser user) {
		List<TaxiOrder> lstOrder = new ArrayList<TaxiOrder>();
		if(mapTrips.isEmpty()){
			return lstOrder;
		}
		for (String key : mapTrips.keySet()) {
			Trip trip = mapTrips.get(key);
			if (trip.getOrder().getBeginOrderTime().getTime() > (System.currentTimeMillis() - (minute * 60l * 1000l)) 
					&& trip.getOrder().getUser() != null && trip.getOrder().getUser().equals(user)) {
				lstOrder.add(trip.getOrder());
			}
		}
		return lstOrder;
	}
	
	/**
	 * Tuanpa: lấy cuốc chưa xử lý gần nhất => gọi nhắc
	 * @param phoneNumber
	 * @return
	 */
	public TaxiOrder getProcessingTaxiOrder(String phoneNumber) {
		TaxiOrder taxiOrder = null;
		for (Trip trip : mapTrips.values()) {
			if ((trip.getOrder().getStatus() == EnumStatus.MOI.getValue()
					|| trip.getOrder().getStatus() == EnumStatus.DA_DOC_DAM.getValue()
					|| trip.getOrder().getStatus() == EnumStatus.XE_DANG_KY_DON.getValue())
				&& StandandPhoneNumber.standandPhone(phoneNumber).equals(trip.getOrder().getPhoneNumber())) {
				TaxiOrder orderTmp = trip.getOrder();
				if (orderTmp.getCreated().after(new Timestamp(System.currentTimeMillis() - CommonDefine.DURATION_VALID_TAXIORDER * 60 * 60 * 1000))) {
					taxiOrder = trip.getOrder();
					break;
				}
			}
		}
		return taxiOrder;
	}
	
	public void updateTaxiOrder(TaxiOrder taxiOrder) {
		for (Trip trip : mapTrips.values()) {
			if (trip.getOrder().equals(taxiOrder)) {
				trip.setOrder(taxiOrder);
				return;
			}
		}
	}
}
