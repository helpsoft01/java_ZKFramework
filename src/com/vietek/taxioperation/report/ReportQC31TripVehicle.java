package com.vietek.taxioperation.report;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Listbox;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.database.MongoAction;
import com.vietek.taxioperation.model.ReportInputVehicle;
import com.vietek.taxioperation.model.ReportQcTripVehicle;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.ui.util.VTReportViewer;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.taxioperation.util.MapUtils;
import com.vietek.tracking.ui.model.GpsTrackingMsg;

public class ReportQC31TripVehicle extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ReportQcTripVehicle> lstData;

	public ReportQC31TripVehicle() {
		super();
		this.removeComponent(this.chosenAgent, true);
		this.removeComponent(this.chosenDriver, true);
		this.removeComponent(this.txtVehicleNumber, true);
		this.removeComponent(this.txtLicensePlate, true);
	}

	@Override
	public void loadModel() {
		setModelClass(ReportQcTripVehicle.class);
	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Thời điểm", 130, Date.class, "getTimeLog", "timeLog", getModelClass()));
		lstCols.add(new GridColumn("Kinh độ - Vĩ độ", 200, String.class, "getLocation", "location", getModelClass()));
		lstCols.add(new GridColumn("Vị trí", 450, String.class, "getAddr", "addr", getModelClass()));
		lstCols.add(new GridColumn("Ghi chú", 150, String.class, "getNote", "note", getModelClass()));

		setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Hành Trình Xe Chạy");
	}

	@Override
	public void loadData() {
		lstData = this.getLstReportQcTripVehicle();
		setLstModel(lstData);
	}

	// Find list GpsTrackingMsg
	private List<GpsTrackingMsg> getLstGpsTrackingMsg() {
		MongoAction mongo = new MongoAction();
		long fromtime = TimeUnit.MILLISECONDS.toSeconds(dateFrom.getValue().getTime() + TimeUnit.HOURS.toMillis(7));
		long totime = TimeUnit.MILLISECONDS.toSeconds(dateTo.getValue().getTime() + TimeUnit.HOURS.toMillis(7));

		List<GpsTrackingMsg> lstValue = mongo.getHistoryVehicle(fromtime, totime, deviceId);
		return lstValue;
	}

	// Create list ReportQcTripVehicle
	private List<ReportQcTripVehicle> getLstReportQcTripVehicle() {
		List<ReportQcTripVehicle> lstValue = new ArrayList<ReportQcTripVehicle>();

		List<GpsTrackingMsg> lstTemp = this.getLstGpsTrackingMsg();
		Date oldTimeLog = new Date(0);
		String oldAddr = "";

		for (int i = 0; i < lstTemp.size(); i += 3) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			ReportQcTripVehicle rptQcTrip = new ReportQcTripVehicle();
			GpsTrackingMsg messageTracking = lstTemp.get(i);

			int[] lstSpeedMessage = messageTracking.getGpsSepeed();
			int maxIndex = lstSpeedMessage.length - 1;

			if (oldTimeLog == messageTracking.getTimeLog()) {
				continue;
			} else {
				if (lstSpeedMessage[maxIndex] <= 5) {
					if (oldAddr == "") {
						oldAddr = MapUtils.convertLatLongToAddrest((double) messageTracking.getLatitude(),
								(double) messageTracking.getLongitude());
					}
					rptQcTrip.setAddr("" + oldAddr);
				} else {
					oldAddr = MapUtils.convertLatLongToAddrest((double) messageTracking.getLatitude(),
							(double) messageTracking.getLongitude());
					rptQcTrip.setAddr("" + oldAddr);
				}
				rptQcTrip.setTimeLog("" + sdf.format(messageTracking.getTimeLog()));
				rptQcTrip.setLocation("" + messageTracking.getLongitude() + " - " + messageTracking.getLatitude());
				rptQcTrip.setNote("");
				lstValue.add(rptQcTrip);

				oldTimeLog = messageTracking.getTimeLog();
			}
		}

		return lstValue;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(btnSearch)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				Set<ReportInputVehicle> lstObjchosen = chosenVehicle.getSelectedObjects();
				if (dateTo.getValue().getTime() < dateFrom.getValue().getTime()) {
					Env.getHomePage().showNotification("Hãy chọn lại thời gian cho báo cáo!",
							Clients.NOTIFICATION_TYPE_ERROR);
				} else if (strGroupId == null) {
					Env.getHomePage().showNotification("Phải chọn ít nhất một đội xe cho báo cáo!",
							Clients.NOTIFICATION_TYPE_ERROR);
				} else if ((lstObjchosen == null) || (lstObjchosen.size() <= 0)) {
					Env.getHomePage().showNotification("Hãy chọn 1 xe cho báo cáo!", Clients.NOTIFICATION_TYPE_ERROR);
				} else if (lstObjchosen.size() >= 2) {
					Env.getHomePage().showNotification("Chỉ được chọn 1 xe cho báo cáo !",
							Clients.NOTIFICATION_TYPE_ERROR);
				} else {
					this.renderGrid();
				}
			}
		}

		if (event.getTarget().equals(btnExcel)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				Listbox listboxExcel = (Listbox) getListbox();
				Iterator<Component> component = listboxExcel.getChildren().iterator();
				while (component.hasNext()) {
					Component value = component.next();
					if (((value instanceof Frozen)) || ((value instanceof Auxhead))) {
						listboxExcel.removeChild((Component) value);
						component = listboxExcel.getChildren().iterator();
					}
				}

				CommonUtils.exportListboxToExcel(listboxExcel, "bao_cao_hanh_trinh_xe_chay.xlsx");
			}
		}
		if (event.getTarget().equals(btnReport)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				exportToReportViewer(event);
			}
		}
	}

	@Override
	public void setMapParams() {
		// TODO Auto-generated method stub
		reportFile = "ReportQC31TripDetailVehicle";
		reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO HÀNH TRÌNH XE CHẠY";
		exportFileName = "bao_cao_hanh_trinh_xe";
		reportName = "BÁO CÁO HÀNH TRÌNH XE CHẠY";
	}

	public void exportToReportViewer(Event event) {
		try {
			if (event.getTarget().equals(btnReport)) {
				this.setMapParams();
				VTReportViewer reportWindow = new VTReportViewer(reportFile, reportTitle, reportName, exportFileName,
						this, getLstModel().toArray());
				reportWindow.onShowing();
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	@Override
	public void renderReportWithListBox() {
		// TODO Auto-generated method stub
		
	}
}
