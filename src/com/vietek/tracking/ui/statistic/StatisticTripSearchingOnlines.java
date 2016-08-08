package com.vietek.tracking.ui.statistic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.IntFormat;
import com.vietek.taxioperation.common.MathForRound;
import com.vietek.taxioperation.controller.CommonValueController;
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.CommonValue;
import com.vietek.taxioperation.model.RptTripSearchingOnline;
import com.vietek.taxioperation.report.AbstractReportWindow;
import com.vietek.taxioperation.ui.util.ComboboxRender;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.tracking.ui.utility.TrackingHistory;

public class StatisticTripSearchingOnlines extends AbstractReportWindow implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<RptTripSearchingOnline> lstData;

	public StatisticTripSearchingOnlines() {
		super();
		this.removeComponent(getChosenDriver(), true);
		this.removeComponent(getChosenVehicle(), true);
		getBtnReport().setVisible(false);
		getBtnExcel().setVisible(false);
	}

	@Override
	public void loadModel() {
		setModelClass(RptTripSearchingOnline.class);

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Đội xe", 220, String.class, "getGroupName", "groupName", getModelClass()));
		lstCols.add(new GridColumn("Số tài", 100, String.class, "getVehicleNumber", "vehicleNumber", getModelClass()));
		lstCols.add(new GridColumn("BKS", 90, String.class, "getLicensePlate", "licensePlate", getModelClass()));
		lstCols.add(new GridColumn("Loại xe", 220, String.class, "getVehicleType", "vehicleType", getModelClass()));
		lstCols.add(new GridColumn("Giờ đón", 130, Timestamp.class, "getTimeStart", "timeStart", getModelClass()));
		lstCols.add(new GridColumn("Giờ trả", 130, Timestamp.class, "getTimeFinish", "timeFinish", getModelClass()));
		lstCols.add(new GridColumn("Điểm đón", 350, String.class, "getPlaceStart", "placeStart", getModelClass()));
		lstCols.add(new GridColumn("Điểm trả", 350, String.class, "getPlaceFinish", "placeFinish", getModelClass()));
		lstCols.add(new GridColumn("Đồng hồ", 130, Integer.class, "getClock", "clock", getModelClass()));
		lstCols.add(new GridColumn("Giảm trừ", 130, Integer.class, "getReduce", "reduce", getModelClass()));
		lstCols.add(new GridColumn("Tiền cuốc", 130, Integer.class, "getPriceTrip", "priceTrip", getModelClass()));
		lstCols.add(new GridColumn("Lý do", 150, Integer.class, "getReason", "reason", getModelClass()));
		lstCols.add(new GridColumn("Km", 150, Float.class, "getKm", "km", getModelClass()));
		lstCols.add(new GridColumn("Tài xế", 160, String.class, "getDriver", "driver", getModelClass()));
		lstCols.add(new GridColumn("Hành trình", 1000, String.class, "getTripDetail", "tripDetail", getModelClass()));
		lstCols.add(new GridColumn("Lịch sử", 100, Boolean.class, "getIsHistory", "isHistory", getModelClass()));

		setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Danh Sách Cuốc Khách Tìm Kiếm");

	}

	@Override
	public void loadData() {
		lstData = displayStatisticTripSeachingOnlines();
		setLstModel(lstData);

	}

	@Override
	public void setMapParams() {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderExtraReport() {
		getListbox().setItemRenderer(new ListitemRenderer<RptTripSearchingOnline>() {

			@Override
			public void render(Listitem item, RptTripSearchingOnline data, int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				item.appendChild(new Listcell("" + ++index));
				// item.appendChild(new Label("" + ++index));
				item.appendChild(new Listcell("" + data.getGroupName()));
				item.appendChild(new Listcell("" + data.getVehicleNumber()));
				item.appendChild(new Listcell("" + data.getLicensePlate()));
				item.appendChild(new Listcell("" + data.getVehicleType()));
				item.appendChild(new Listcell("" + dateformat.format(data.getTimeStart())));
				item.appendChild(new Listcell("" + dateformat.format(data.getTimeFinish())));
				item.appendChild(new Listcell("" + data.getPlaceStart()));
				item.appendChild(new Listcell("" + data.getPlaceFinish()));
				item.appendChild(new Listcell("" + IntFormat.formatTypeInt("###,###", data.getClock())));
				Textbox txtInputReduce = new Textbox();
				txtInputReduce.setInplace(true);
				txtInputReduce.setWidth("90%");
				txtInputReduce.setValue("" + data.getReduce());

				Listcell listcellInputReduce = new Listcell();
				txtInputReduce.setParent(listcellInputReduce);

				item.appendChild(listcellInputReduce);

				Label lbPrice = new Label("" + IntFormat.formatTypeInt("###,###", data.getPriceTrip()));
				Listcell lb = new Listcell();
				lbPrice.setParent(lb);
				item.appendChild(lb);
				Combobox cboInputReason = createCboInputReason(item, data);
				cboInputReason.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {

						Comboitem item = cboInputReason.getSelectedItem();
						data.setReason(item.getValue());
						updateTripSearchingOnline(data.getShiftId(), data.getTripId(), data.getPriceTrip(),
								data.getReason(), "admin");
					}
				});
				item.appendChild(new Listcell("" + MathForRound.round(data.getKm(), 1)));
				item.appendChild(new Listcell(
						"" + (data.getDriver().trim().toLowerCase() == null ? "" : "" + data.getDriver())));
				item.appendChild(new Listcell("" + data.getTripDetail()));
				txtInputReduce.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						data.setPriceTrip(data.getClock() - Integer.parseInt(txtInputReduce.getValue()));
						lbPrice.setValue("" + data.getPriceTrip());
						updateTripSearchingOnline(data.getShiftId(), data.getTripId(), data.getPriceTrip(),
								data.getReason(), "admin");
					}
				});
				txtInputReduce.addForward(Events.ON_CHANGE, txtInputReduce, Events.ON_OK);

				Button btnHistory = new Button("Lịch sử");
				btnHistory.setStyle("color : black;font-size : 12px");
				btnHistory.setHeight("25px");
				Listcell listcell = new Listcell();
				btnHistory.setParent(listcell);
				item.appendChild(listcell);
				 btnHistory.addEventListener(Events.ON_CLICK, new
				 EventListener<Event>() {
				
				 @Override
				 public void onEvent(Event arg0) throws Exception {
				 showHistory(data);
				 }
				 });
			}
		});
	}

	private List<RptTripSearchingOnline> displayStatisticTripSeachingOnlines() {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		if (getDateTo().getValue().getTime() < getDateFrom().getValue().getTime()) {
			Env.getHomePage().showNotification("Hãy chọn lại thời gian cho báo cáo!", Clients.NOTIFICATION_TYPE_ERROR);
		}
		Timestamp timedatefrom = new Timestamp(getDateFrom().getValue().getTime());
		Timestamp timedateto = new Timestamp(getDateTo().getValue().getTime());

		String stragentid = "" + (getStrAgentId() == null ? "0" : "" + getStrAgentId());
		String strgroupid = ""
				+ (getStrGroupId() == null ? "0" : (getStrGroupId().length() == 0) ? "0" : "" + getStrGroupId());
		String strparkareaid = "" + (getStrPakingAreaId() == null ? "0"
				: (getStrPakingAreaId().length() == 0) ? "0" : "" + getStrPakingAreaId());

		List<RptTripSearchingOnline> lstvalue = lstObj.getTripSearchingOnline(timedatefrom, timedateto, "" + getUser(),
				stragentid, strgroupid, strparkareaid, "" + getTxtLicensePlate().getValue(),
				"" + getTxtVehicleNumber().getValue());
		return lstvalue;
	}

	// Tao CboReason mới
	private Combobox createCboInputReason(Listitem item, RptTripSearchingOnline data) {

		List<CommonValue> lstcommonvalue = getCommonValue("REDUCEMONEY");
		HashMap<String, String> mapValue = new HashMap<String, String>();
		for (CommonValue common : lstcommonvalue) {
			mapValue.put(common.getCode(), common.getName());
		}
		Combobox cboinput = new Combobox();
		ComboboxRender renderCbo = new ComboboxRender();
		cboinput = renderCbo.ComboboxRendering(mapValue, "", "", 120, 8, data.getReason(), false);

		Listcell listcell = new Listcell();
		cboinput.setParent(listcell);

		item.appendChild(listcell);
		cboinput.setInplace(true);
		cboinput.setSclass("comboboxtripsearching");
		return cboinput;

	}

	private List<CommonValue> getCommonValue(String codetype) {
		CommonValueController controller = (CommonValueController) ControllerUtils
				.getController(CommonValueController.class);
		String sql = "from CommonValue where codetype = ?";
		List<CommonValue> lstvalue = controller.find(sql, codetype);
		if (lstvalue == null || lstvalue.size() > 0)
			return lstvalue;
		else
			return null;
	}

	public void updateTripSearchingOnline(int shiftId, int tripId, int cash, int reason, String userId) {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		lstObj.updateTripSearchingOnline(shiftId, tripId, cash, reason, userId);
		return;

	}
	
	private void showHistory(RptTripSearchingOnline data) {
		TrackingHistory history = new TrackingHistory(new java.sql.Date(data.getTimeStart().getTime()),
				new java.sql.Date(data.getTimeFinish().getTime()), data.getVehicleId());
		Window window = new Window();
		window.setWidth("100%");
		window.setHeight("100%");
		window.setClosable(true);
		window.setTitle("Lịch sử cuốc chi tiết");
		window.setParent(this);
		window.appendChild(history);
		window.doModal();

	}

	@Override
	public void renderReportWithListBox() {
		setRenderReportWithListBox(false);

	}

}
