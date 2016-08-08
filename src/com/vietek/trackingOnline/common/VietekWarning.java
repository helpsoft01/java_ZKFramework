package com.vietek.trackingOnline.common;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.model.Vehicle;
import com.vietek.trackingOnline.tracker.TrackingRDS2Json;

public class VietekWarning {
	private List<AbstractWarning> listWarning;

	public VietekWarning(Vehicle vehicle) {
		listWarning = new ArrayList<>();
		initWarning(vehicle);
	}

	private void initWarning(Vehicle vehicle) {
		CutSignalPulseVehicle cutsignal = new CutSignalPulseVehicle(vehicle);
		KickSignalPulseVehicle kicksignal = new KickSignalPulseVehicle(vehicle);
		listWarning.add(cutsignal);
		listWarning.add(kicksignal);
	}

	public void addWarning(AbstractWarning warning) {
		listWarning.add(warning);
	}

	public void updateConfigVietekWarning(Vehicle vehicle) {
		for (AbstractWarning abstractWarning : listWarning) {
			abstractWarning.updateConfig(vehicle);
		}
	}

	public void executeVietekWarning(TrackingRDS2Json msg) {
		for (AbstractWarning abstractWarning : listWarning) {
			abstractWarning.executeWarning(msg);
		}
	}

	public List<AbstractWarning> getListWarning() {
		return listWarning;
	}

	public void setListWarning(List<AbstractWarning> listWarning) {
		this.listWarning = listWarning;
	}
}
