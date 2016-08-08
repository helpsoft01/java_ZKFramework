
package com.vietek.taxioperation.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.util.cache.AbstractCache;
import com.vietek.taxioperation.util.cache.Memcached;
import com.vietek.trackingOnline.common.VietekWarning;
import com.vietek.trackingOnline.tracker.TrackingRDS2Json;

/**
 *
 * @author VuD
 */
public class MapCommon {

	/**
	 * Map luu thong tin tinh cua xe.
	 * <p>
	 * <b>Key</b>:VehicleValue. <b>Value</b>:VehicleDD
	 */
	public static final AbstractCache MAP_VEHICLE_INFO = new Memcached("MAP_VEHICLE_INFO", 0);

	/**
	 * Map luu thong tin ve cac xe co chung so tai
	 */
	public static final AbstractCache MAP_LIST_VEHICLE_INFO = new Memcached("MAP_LIST_VEHICLE_INFO", 0);

	/**
	 * Map luu thong tin xe qua so tai day du.
	 * <p>
	 * <b>Key</b>:CompanyValue_VehicleNumber. <b>Value</b>:VehicleDD
	 */
	public static final AbstractCache MAP_VEHICLE_INFO_FULL = new Memcached("MAP_VEHICLE_INFO_FULL", 0);

	/**
	 * Map vehicledd theo id.
	 * <p>
	 * <b>Key</b>:VehicleId. <b>Value</b>:VehicleDD
	 */
	public static final AbstractCache MAP_VEHICLEDD_ID = new Memcached("MAP_VEHICLEDD_ID", 0);

	/**
	 * Map vehicle theo vehicleId
	 * <p>
	 * <b>Key</b>:VehicleId. <b>Value</b>:Vehicle
	 */
	public static final Map<Integer, Vehicle> MAP_VEHICLE_ID = new ConcurrentHashMap<>();

	/**
	 * Map luu trang thai xe dang ky cuoi cung.
	 * <p>
	 * <b>Key</b>:VehcleId. <b>Value</b>:VehicleStatusDD
	 */
	public static final AbstractCache MAP_VEHICLE_STATUS_ID = new Memcached("MAP_VEHICLE_STATUS_ID", 0);

	/**
	 * Map luu cac xe dang xap tai theo diem tiep thi
	 * <p>
	 * <b>Key</b>:ArrangementId. <b>Value</b>:Danh sach cac xe trong diem sap
	 * tai
	 */
	public static final Map<Integer, LinkedList<Taxi>> MAP_TAXI_LIST = new ConcurrentHashMap<Integer, LinkedList<Taxi>>();

	/**
	 * Map luu cac diem tiep thi
	 * <p>
	 * <b>Key</b>:ArrangementId. <b>Value</b>:Arrangement
	 */
	public static final AbstractCache MAP_ARRANGEMENT = new Memcached("MAP_ARRANGEMENT", 0);

	/**
	 * <p>
	 * <b>Key</b>:AgentId. <b>Value</b>:Agent
	 */
	public static final Map<Integer, Agent> MAP_AGENT = new ConcurrentHashMap<>();

	/**
	 * Map luu cac doi xe
	 * <p>
	 * <b>Key</b>:TaxiGroupId. <b>Value</b>:TaxiGroup
	 */
	public static final Map<Integer, TaxiGroup> MAP_TAXI_GROUP = new ConcurrentHashMap<>();

	/**
	 * Map luu thong tin cac tai xe
	 * <p>
	 * <b>Key</b>:DriverId. <b>Value</b>:Driver
	 */
	public static final Map<Integer, Driver> MAP_DRIVER = new ConcurrentHashMap<>();

	/**
	 * Ban tin hanh trinh cuoi cung cuaa xe. Key: deviceId
	 */
	public static final Map<Integer, TrackingRDS2Json> TRACKING_RDS = new HashMap<>();

	public static final ConcurrentHashMap<Integer, VietekWarning> MAP_VIETEK_WARNING = new ConcurrentHashMap<Integer, VietekWarning>();
}
