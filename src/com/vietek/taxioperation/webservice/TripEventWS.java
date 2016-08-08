package com.vietek.taxioperation.webservice;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.vietek.taxioperation.common.QueCommon;
import com.vietek.taxioperation.model.GpsServerPickupMsg;
import com.vietek.taxioperation.model.GpsServerTripDoneMsg;
import com.vietek.taxioperation.util.DoubleUtils;
import com.vietek.taxioperation.util.IntegerUtils;

/**
 *
 * @author VuD
 */

@Path("/tripevent")
public class TripEventWS {
	public static final String formatDate = new String("HH:mm:ss MM/dd/yy");

	@GET
	@Path("/pickup")
	public String receivePickupMsg(@QueryParam("taxiid") String taxiIdParam,
			@QueryParam("curshift") String curShiftParam, @QueryParam("curtrip") String curTripParam,
			@QueryParam("longitude") String longitudeParam, @QueryParam("latitude") String latitudeParam,
			String dateParam) {
		String result = "400";
		try {
			Integer taxiId = IntegerUtils.string2Integer(taxiIdParam);
			if (taxiId != null) {
				Integer curShift = IntegerUtils.string2Integer(curShiftParam);
				if (curShift != null) {
					Integer curTrip = IntegerUtils.string2Integer(curTripParam);
					if (curTrip != null) {
						Double longitude = DoubleUtils.string2Double(longitudeParam);
						if (longitude != null) {
							Double latitude = DoubleUtils.string2Double(latitudeParam);
							if (latitude != null) {
								SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
								Date datelog = sdf.parse(dateParam);
								if (datelog != null) {
									GpsServerPickupMsg msg = new GpsServerPickupMsg();
									msg.setTaxiId(taxiId);
									msg.setCurShift(curShift);
									msg.setCurTrip(curTrip);
									msg.setLongitude(longitude);
									msg.setLatitude(latitude);
									msg.setTimeLog(datelog);
									QueCommon.quePickupMsg.offer(msg);
									result = "200";
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@GET
	@Path("/tripdone")
	public String receiveTripDoneMsg(@QueryParam("taxiid") String taxiIdParam,
			@QueryParam("curshift") String curShiftParam, @QueryParam("curtrip") String curTripParam,
			@QueryParam("longitude") String longitudeParam, @QueryParam("latitude") String latitudeParam,
			String dateParam) {
		String result = "400";
		try {
			Integer taxiId = IntegerUtils.string2Integer(taxiIdParam);
			if (taxiId != null) {
				Integer curShift = IntegerUtils.string2Integer(curShiftParam);
				if (curShift != null) {
					Integer curTrip = IntegerUtils.string2Integer(curTripParam);
					if (curTrip != null) {
						Double longitude = DoubleUtils.string2Double(longitudeParam);
						if (longitude != null) {
							Double latitude = DoubleUtils.string2Double(latitudeParam);
							if (latitude != null) {
								SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
								Date datelog = sdf.parse(dateParam);
								if (datelog != null) {
									GpsServerTripDoneMsg msg = new GpsServerTripDoneMsg();
									msg.setTaxiId(taxiId);
									msg.setCurShift(curShift);
									msg.setCurTrip(curTrip);
									msg.setLongitude(longitude);
									msg.setLatitude(latitude);
									msg.setTimeLog(datelog);
									QueCommon.queTripDoneMsg.offer(msg);
									result = "200";
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
