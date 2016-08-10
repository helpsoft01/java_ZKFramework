package com.vietek.trackingOnline.common;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.tracking.ui.model.GpsTrackingMsg;
import com.vietek.trackingOnline.tracker.TrackingRDS2Json;

public abstract class AbstractWarning {
	final int IS_BEGIN = 0;
	final int IS_END = 1;
	private Integer vehicleid;
	private TrackingRDS2Json msgStart;
	private TrackingRDS2Json msgStop;
	private long timeold = 0;
	private Integer typeWarning;
	private boolean inwarning = false;
	private Timestamp pingtimemeter = null;
	private RowWarningReport data = null;
	private Integer tripStatusOld = null;
	private int countStoptrip = 0;
	private boolean refreshwhenstoptrip = true;

	public AbstractWarning(Integer vehicleid, Integer typewarning) {
		this.vehicleid = vehicleid;
		this.typeWarning = typewarning;
	}

	public abstract boolean checkConditionBeginWarning(TrackingRDS2Json msg);

	public abstract boolean checkConditionStopWarning(TrackingRDS2Json msg);

	public void updateConfig(Vehicle vehicle) {
		pingtimemeter = vehicle.getMeterPingTime();
	}

	public void executeWarning(TrackingRDS2Json msg) {
		beforeExecute(msg);
		if (!msg.isLostGPS()) {
			if (timeold == 0) {
				timeold = msg.getTimeLog().getTime();
			}
			if (isMsgNew(msg)) {
				if (!inwarning && checkConditionBeginWarning(msg)) {
					saveBeginWarning();
					inwarning = true;
				} else if (inwarning && checkConditionStopWarning(msg)) {
					saveStopWarning();
					inwarning = false;
				}
			} else {
				refreshValue();
			}
		}
		if (isStoptrip(msg) && refreshwhenstoptrip) {
			refreshValue();
		}
		timeold = msg.getTimeLog().getTime();

	}

	public void beforeExecute(TrackingRDS2Json msg) {

	}

	public boolean isStoptrip(TrackingRDS2Json msg) {
		boolean result = false;
		if (tripStatusOld == null) {
			tripStatusOld = msg.getInTrip();
		} else {
			if (countStoptrip == 0 && tripStatusOld == GpsTrackingMsg.CO_KHACH && tripStatusOld != msg.getInTrip()) {
				countStoptrip++;
			}
			if (countStoptrip > 0 && msg.getInTrip() == GpsTrackingMsg.KHONG_KHACH) {
				countStoptrip++;
				if (countStoptrip >= 5) {
					result = true;
					countStoptrip = 0;
				}
			} else {
				countStoptrip = 0;
			}
			tripStatusOld = msg.getInTrip();
		}
		return result;
	}

	public void refreshValue() {

	}

	public boolean isMsgNew(TrackingRDS2Json msg) {
		if (msg.getTimeLog().getTime() > timeold
				&& msg.getTimeLog().getTime() - timeold < TimeUnit.MINUTES.toMillis(1)) {
			return true;
		} else {
			return false;
		}
	}

	public void saveBeginWarning() {
		beforeSaveBegin();
		updatedata(msgStart, IS_BEGIN);
		AppLogger.logVWarning.info("CutSignal--" + "Bat Dau" + " | " + typeWarning + " | " + msgStart.toString());
	}

	public void saveStopWarning() {
		beforeSaveStop();
		updatedata(msgStop, IS_END);
		AppLogger.logVWarning.info("CutSignal--" + "Kết thúc" + " | " + typeWarning + " | " + msgStop.toString());
	}

	public void beforeSaveBegin() {

	}

	private void updatedata(TrackingRDS2Json msg, int type) {
		if (type == IS_BEGIN) {
			data = new RowWarningReport();
			data.setBegintime(msg.getTimeLog());
			data.setBeginlati(msg.getLatitude());
			data.setBeginLongi(msg.getLongitude());
		} else if (type == IS_END) {
			if (data != null) {
				data.setEndtime(msg.getTimeLog());
				data.setEndLati(msg.getLatitude());
				data.setEndLongi(msg.getLongitude());
			}
		}
	}

	public void beforeSaveStop() {
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		AbstractWarning warning = (AbstractWarning) obj;
		if (vehicleid == warning.getVehicleid()) {
			result = true;
		}
		return result;
	}

	public TrackingRDS2Json getMsgStart() {
		return msgStart;
	}

	public void setMsgStart(TrackingRDS2Json msgStart) {
		this.msgStart = msgStart;

	}

	public boolean inTrip(TrackingRDS2Json msg) {
		boolean result = false;
		if (pingtimemeter != null) {
			if (msg.getInTrip() == GpsTrackingMsg.CO_KHACH) {
				if (Math.abs(System.currentTimeMillis() - pingtimemeter.getTime()) < TimeUnit.MINUTES.toMillis(30)) {
					result = true;
				} else {
					// AppLogger.logVWarning.info("TimepingMeter: " + vehicleid
					// + "|"
					// + new Timestamp(System.currentTimeMillis()) + "|" + new
					// Timestamp(pingtimemeter.getTime()));
				}

			}
		} else {
		}

		// if (msg.getInTrip() == GpsTrackingMsg.CO_KHACH) {
		// result = true;
		// }

		return result;
	}

	public long getTimeold() {
		return timeold;
	}

	public void setTimeold(long timeold) {
		this.timeold = timeold;
	}

	public Integer getTypeWarning() {
		return typeWarning;
	}

	public TrackingRDS2Json getMsgStop() {
		return msgStop;
	}

	public void setMsgStop(TrackingRDS2Json msgStop) {
		this.msgStop = msgStop;
	}

	public boolean isInwarning() {
		return inwarning;
	}

	public void setInwarning(boolean inwarning) {
		this.inwarning = inwarning;
	}

	public String getTypeWarningName() {
		String name = "Không xác định";
		return name;
	}

	public void setTypeWarning(Integer typeWarning) {
		this.typeWarning = typeWarning;
	}

	public Integer getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(Integer vehicleid) {
		this.vehicleid = vehicleid;
	}

	public Timestamp getPingtimemeter() {
		return pingtimemeter;
	}

	public void setPingtimemeter(Timestamp pingtimemeter) {
		this.pingtimemeter = pingtimemeter;
	}

	public RowWarningReport getData() {
		return data;
	}

	public void setData(RowWarningReport data) {
		this.data = data;
	}

}
