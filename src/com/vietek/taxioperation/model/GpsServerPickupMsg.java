package com.vietek.taxioperation.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author VuD
 */
public class GpsServerPickupMsg {
	private int taxiId;
	private int curShift;
	private int curTrip;
	private double longitude;
	private double latitude;
	private Date timeLog;

	public int getTaxiId() {
		return taxiId;
	}

	public void setTaxiId(int taxiId) {
		this.taxiId = taxiId;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Date getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(Date timeLog) {
		this.timeLog = timeLog;
	}

	public int getCurShift() {
		return curShift;
	}

	public void setCurShift(int curShift) {
		this.curShift = curShift;
	}

	public int getCurTrip() {
		return curTrip;
	}

	public void setCurTrip(int curTrip) {
		this.curTrip = curTrip;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		StringBuilder sb = new StringBuilder();
		sb.append("PickupRQ|TaxiId:").append(taxiId);
		sb.append("|CurShift:").append(curShift);
		sb.append("|CurTrip:").append(curTrip);
		sb.append("|Longitude:").append(longitude);
		sb.append("|Latitude:").append(latitude);
		if (timeLog != null) {
			sb.append("|TimeLog:").append(sdf.format(timeLog));
		}
		return sb.toString();
	}
}
