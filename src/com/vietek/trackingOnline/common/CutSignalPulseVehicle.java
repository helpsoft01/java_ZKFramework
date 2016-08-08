package com.vietek.trackingOnline.common;

import java.util.concurrent.TimeUnit;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.model.Device;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.trackingOnline.tracker.TrackingRDS2Json;

public class CutSignalPulseVehicle extends AbstractWarning {
	private int vgpsbd;
	private int vcokt;
	private int deltaVkt;
	private int timexn;
	private long deltabegintime;
	private long deltastoptime;

	public CutSignalPulseVehicle(Vehicle vehicle) {
		super(vehicle.getId(), 9);
		updateConfig(vehicle);
	}

	@Override
	public void updateConfig(Vehicle vehicle) {
		super.updateConfig(vehicle);
		Device device = vehicle.getDevice();
		vgpsbd = device.getSpeedGpsStartInCut();
		vcokt = device.getSpeedGpsStopInCut();
		deltaVkt = device.getDeltaSpeedInCut();
		timexn = device.getTimerInCut();

	}

	@Override
	public boolean checkConditionBeginWarning(TrackingRDS2Json msg) {
		boolean result = false;
		if (inTrip(msg)) {
			if (msg.getGpsSpeed() > vgpsbd) {
				if (msg.getMetterSpeed() < 2) {
					long time = msg.getTimeLog().getTime() - getTimeold();
					deltabegintime = deltabegintime + time;
					if (deltabegintime >= TimeUnit.SECONDS.toMillis(timexn)) {
						AppLogger.logVWarning.info("Thông tin kiểm tra:" + TimeUnit.MILLISECONDS.toMinutes(deltabegintime) + "|" + timexn);
						result = true;
						deltabegintime = 0;
					}
					if (getMsgStart() == null) {
						setMsgStart(msg);
					}
				} else {
					refreshValueStart();
				}
			} else {
				refreshValueStart();
			}
		} else {
			refreshValueStart();
		}

		return result;
	}

	private void refreshValueStart() {
		deltabegintime = 0;
		if (getMsgStart() != null) {
			setMsgStart(null);
		}
	}

	@Override
	public void refreshValue() {
		super.refreshValue();
		refreshValueStart();
	}

	@Override
	public boolean checkConditionStopWarning(TrackingRDS2Json msg) {
		boolean result = false;
		if (msg.getMetterSpeed() > vcokt && Math.abs(msg.getGpsSpeed() - msg.getMetterSpeed()) < deltaVkt) {
			long time = msg.getTimeLog().getTime() - getTimeold();
			deltastoptime = deltastoptime + time;
			if (deltastoptime >= TimeUnit.SECONDS.toMillis(timexn)) {
				AppLogger.logVWarning.info("Thông tin kiểm tra:" + deltastoptime + "|" + timexn);
				result = true;
				deltastoptime = 0;
			}
			if (getMsgStop() == null) {
				setMsgStop(msg);
			}
		} else {
			refreshValueStop();
		}
		return result;
	}

	private void refreshValueStop() {
		deltastoptime = 0;
		if (getMsgStop() != null) {
			setMsgStop(null);
		}
	}

	@Override
	public void beforeSaveBegin() {
	}

	public int getVgpsbd() {
		return vgpsbd;
	}

	public void setVgpsbd(int vgpsbd) {
		this.vgpsbd = vgpsbd;
	}

	public int getVcokt() {
		return vcokt;
	}

	public void setVcokt(int vcokt) {
		this.vcokt = vcokt;
	}

	public int getDeltaVkt() {
		return deltaVkt;
	}

	public void setDeltaVkt(int deltaVkt) {
		this.deltaVkt = deltaVkt;
	}

	public int getTimexn() {
		return timexn;
	}

	public void setTimexn(int timexn) {
		this.timexn = timexn;
	}

	public long getDeltastoptime() {
		return deltastoptime;
	}

	public void setDeltastoptime(long deltastoptime) {
		this.deltastoptime = deltastoptime;
	}

	public long getDeltabegintime() {
		return deltabegintime;
	}

	public void setDeltabegintime(long deltabegintime) {
		this.deltabegintime = deltabegintime;
	}

}
