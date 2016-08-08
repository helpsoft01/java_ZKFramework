package com.vietek.trackingOnline.common;

import java.util.concurrent.TimeUnit;

import com.vietek.taxioperation.model.Device;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.trackingOnline.tracker.TrackingRDS2Json;

public class KickSignalPulseVehicle extends AbstractWarning {
	private int Vgpsbd;
	private int deltaVbd;
	private long deltaTbd;
	private int Vcokt;
	private int deltaVkt;
	private long deltaTkt;
	private int RepeatInKick;
	private long timebeginlong = 0;
	private long timeendlong = 0;
	private int repeatcount = 0;

	public KickSignalPulseVehicle(Vehicle vehicle) {
		super(vehicle.getId(), 10);
		updateConfig(vehicle);

	}

	@Override
	public boolean checkConditionBeginWarning(TrackingRDS2Json msg) {
		boolean result = false;
		if (inTrip(msg)) {
			if (msg.getGpsSpeed() > Vgpsbd && msg.getMetterSpeed() - msg.getGpsSpeed() >= deltaVbd) {
				long time = msg.getTimeLog().getTime() - getTimeold();
				timebeginlong = timebeginlong + time;
				if (timebeginlong >= TimeUnit.SECONDS.toMillis(deltaTbd) && repeatcount == 0) {
					result = true;
					timebeginlong = 0;
					repeatcount++;
				}
				if (getMsgStart() == null) {
					setMsgStart(msg);
				}
			} else {
				refreshValueBegin();
			}
		} else {
			refreshValueBegin();
		}
		return result;
	}

	private void refreshValueBegin() {
		timebeginlong = 0;
		if (getMsgStart() != null) {
			setMsgStart(null);
		}
	}

	@Override
	public boolean checkConditionStopWarning(TrackingRDS2Json msg) {
		boolean result = false;
		if (msg.getMetterSpeed() > Vcokt && Math.abs(msg.getMetterSpeed() - msg.getGpsSpeed()) < deltaVkt) {
			if (getMsgStop() == null) {
				setMsgStop(msg);
			}
			long time = msg.getTimeLog().getTime() - getTimeold();
			timeendlong = timeendlong + time;
			if (timeendlong > TimeUnit.SECONDS.toMillis(deltaTkt)) {
				timeendlong = 0;
				result = true;
			}
		} else {
			refreshValueStop();
		}
		return result;
	}

	private void refreshValueStop() {
		timeendlong = 0;
		if (getMsgStop() != null) {
			setMsgStop(null);
		}
	}

	@Override
	public void refreshValue() {
		super.refreshValue();
		refreshValueBegin();
		repeatcount = 0;
	}

	@Override
	public void updateConfig(Vehicle vehicle) {
		super.updateConfig(vehicle);
		Device device = vehicle.getDevice();
		this.Vgpsbd = device.getSpeedGpsStartInKick();
		this.Vcokt = device.getSpeedMeterStopInKick();
		this.deltaTbd = device.getTimeStartInKick();
		this.deltaTkt = device.getTimeStopInKick();
		this.deltaVbd = device.getDeltaSpeedStartInKick();
		this.deltaVkt = device.getDeltaSpeedStopInKick();
		this.RepeatInKick = device.getRepeatInKick();

	}

	public int getVgpsbd() {
		return Vgpsbd;
	}

	public void setVgpsbd(int vgpsbd) {
		Vgpsbd = vgpsbd;
	}

	public int getDeltaVbd() {
		return deltaVbd;
	}

	public void setDeltaVbd(int deltaVbd) {
		this.deltaVbd = deltaVbd;
	}

	public long getDeltaTbd() {
		return deltaTbd;
	}

	public void setDeltaTbd(long deltaTbd) {
		this.deltaTbd = deltaTbd;
	}

	public int getVcokt() {
		return Vcokt;
	}

	public void setVcokt(int vcokt) {
		Vcokt = vcokt;
	}

	public int getDeltaVkt() {
		return deltaVkt;
	}

	public void setDeltaVkt(int deltaVkt) {
		this.deltaVkt = deltaVkt;
	}

	public long getDeltaTkt() {
		return deltaTkt;
	}

	public void setDeltaTkt(long deltaTkt) {
		this.deltaTkt = deltaTkt;
	}

	public int getRepeatInKick() {
		return RepeatInKick;
	}

	public void setRepeatInKick(int repeatInKick) {
		RepeatInKick = repeatInKick;
	}

	public long getTimebeginlong() {
		return timebeginlong;
	}

	public void setTimebeginlong(long timebeginlong) {
		this.timebeginlong = timebeginlong;
	}

	public long getTimeendlong() {
		return timeendlong;
	}

	public void setTimeendlong(long timeendlong) {
		this.timeendlong = timeendlong;
	}

	public int getRepeatcount() {
		return repeatcount;
	}

	public void setRepeatcount(int repeatcount) {
		this.repeatcount = repeatcount;
	}
}
