package com.vietek.tracking.ui.model;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class GenaralHistoryWorker extends Thread {
	private TabboxInterface tabbox;
	private int vehicleid;
	private List<GpsTrackingMsg> lstHistory;
	private Desktop desktop;

	public GenaralHistoryWorker(Desktop des, List<GpsTrackingMsg> lstmp, int vehicleid, TabboxInterface tabbox) {
		this.tabbox = tabbox;
		this.lstHistory = lstmp;
		this.desktop = des;
		this.vehicleid = vehicleid;
	}

	@Override
	public void run() {
		Executions.schedule(desktop, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				GenaralValue genaral = Genaral(lstHistory);
				tabbox.updateData(vehicleid, genaral);
			}
		}, null);
	}

	private GenaralValue Genaral(List<GpsTrackingMsg> lstmsg) {
		GenaralValue result = new GenaralValue();
		result.setSodong(lstHistory.size());
		try {
			int Vmax = lstHistory.get(0).getGpsSepeed()[9];
			int Vmin = lstHistory.get(0).getGpsSepeed()[9];
			int vtrungbinh = 0;
			int kmvandoanh = 0;
			int solandungdo = 0;
			long thoigiandung = 0;
			long delaytimestop = 0;
			long tongthoigiandung = 0;
			GpsTrackingMsg PointStop = null;
			int solanvuottoc = 0;
			long thoigianvuottoc = 0;
			int solanqua4h = 0;
			long thoigianqua4h = 0;
			for (int i = 0; i < lstHistory.size(); i++) {
				GpsTrackingMsg gpstmp = lstHistory.get(i);
				thoigiandung = thoigiandung + gpstmp.getTimestop();
				if (gpstmp.getGpsSepeed()[9] > Vmax) {
					Vmax = gpstmp.getGpsSepeed()[9];
				}
				if (gpstmp.getGpsSepeed()[9] < Vmin) {
					Vmin = gpstmp.getGpsSepeed()[9];
				}
				vtrungbinh = vtrungbinh + gpstmp.getGpsSepeed()[9];
				/* Tính thông tin dừng đỗ */
				if (gpstmp.getGpsSepeed()[9] < 5) {
					if (i > 0) {
						long deltatime = gpstmp.getTimeLog().getTime() - lstHistory.get(i - 1).getTimeLog().getTime();
						if (thoigiandung == 0) {
							if (delaytimestop == 0 && i != 1) {
								PointStop = gpstmp;
							}
							delaytimestop = delaytimestop + deltatime;
							if (delaytimestop > TimeUnit.MINUTES.toMillis(5)) {
								thoigiandung = delaytimestop;
								delaytimestop = 0;
								solandungdo++;
								if (PointStop != null && !PointStop.isStopPoint()) {
									PointStop.setStopPoint(true);
								}
							}
						} else {
							thoigiandung += deltatime;
						}

					} else {
						PointStop = gpstmp;
					}
				}
				/* Tính thông tin hoạt động */
				if (gpstmp.getGpsSepeed()[9] > 5) {
					tongthoigiandung += thoigiandung;
					if (PointStop != null && PointStop.isStopPoint()) {
						PointStop.setTimestop(thoigiandung);
						PointStop.setCountStop(solandungdo);
						result.getPointsStop().add(PointStop);
					}
					thoigiandung = 0;
					delaytimestop = 0;
					PointStop = null;
				}
				/* Tính số lần vượt tốc */
				if (gpstmp.getGpsSepeed()[9] > 80 && i > 0) {
					thoigianvuottoc = thoigianvuottoc + gpstmp.getTimeLog().getTime()
							- lstHistory.get(i - 1).getTimeLog().getTime();
				} else {
					if (thoigianvuottoc > 10 * 1000) {
						solanvuottoc++;
					}
					thoigianvuottoc = 0;
				}
				/* Tính số lần vượt quá 4h */
				if (gpstmp.getGpsSepeed()[9] > 5 && i > 0) {
					thoigianqua4h = thoigianqua4h + gpstmp.getTimeLog().getTime()
							- lstHistory.get(i - 1).getTimeLog().getTime();

				} else {
					if (thoigianqua4h > 4 * 60 * 60 * 1000) {
						solanqua4h++;
					}
					thoigianqua4h = 0;
				}
			}
			result.setVmax(((Integer) Vmax).doubleValue());
			result.setVmin(((Integer) Vmin).doubleValue());
			result.setVtrungbinh(((Integer) vtrungbinh).doubleValue() / lstHistory.size());
			result.setKmvandanh(((Integer) kmvandoanh).doubleValue());
			result.setSolanvuottoc(solanvuottoc);
			result.setSolanqua4h(solanqua4h);
			result.setSolandungdo(solandungdo);
			long totalTime = lstHistory.get(lstHistory.size() - 1).getTimeLog().getTime()
					- lstHistory.get(0).getTimeLog().getTime();
			result.setSogiodungdo(tongthoigiandung);
			result.setSogiohoatdong(totalTime - tongthoigiandung);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
