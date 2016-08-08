package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ReportDetailOverTenHour;
import com.vietek.taxioperation.model.RptQcTruckStdOverSpeed;
import com.vietek.taxioperation.model.RptQcTrunkStdDriving;
import com.vietek.taxioperation.model.RptTotalViolateQc31;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.tracking.ui.utility.TrackingHistory;

public class ReportTotalViolateQC31 extends AbstractReportWindow implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean flag = false;
	private int isCheck = 0;
	private Grid gridDetail;
	List<RptTotalViolateQc31> lstData;

	public ReportTotalViolateQC31() {
		super();
		this.removeComponent(this.chosenVehicle, true);
		this.removeComponent(this.chosenDriver, true);
		this.removeComponent(this.txtVehicleNumber, true);
		this.removeComponent(this.txtLicensePlate, true);
	}

	@Override
	public void loadModel() {
		setModelClass(RptTotalViolateQc31.class);

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("", 30, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Họ và tên", 170, String.class, "getDriverName", "driverName", getModelClass()));
		lstCols.add(new GridColumn("MSNV", 70, String.class, "getStaffCard", "staffCard", getModelClass()));
		lstCols.add(new GridColumn("GPLX", 120, String.class, "getDriverLicense", "driverLicense", getModelClass()));
		lstCols.add(new GridColumn("Đội xe", 200, String.class, "getGroupName", "groupName", getModelClass()));
		lstCols.add(new GridColumn("Số lần", 70, Integer.class, "getOverSpeed", "overSpeed", getModelClass()));
		lstCols.add(new GridColumn("Chọn xem", 70, Integer.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Số lần", 70, Integer.class, "getOverFourHour", "overFourHour", getModelClass()));
		lstCols.add(new GridColumn("Chọn xem", 80, Integer.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("Số lần", 70, Integer.class, "getOverTenHour", "overTenHour", getModelClass()));
		lstCols.add(new GridColumn("Chọn xem", 80, Integer.class, "", "", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("BÁO CÁO TỔNG HỢP VI PHẠM QCVN31");

	}

	@Override
	public void loadData() {
		lstData = displayRptTotalViolateQc31();
		setLstModel(lstData);

	}

	private List<RptTotalViolateQc31> displayRptTotalViolateQc31() {

		ListObjectDatabase lstObj = new ListObjectDatabase();
		if (getDateTo().getValue().getTime() < getDateFrom().getValue().getTime()) {
			Env.getHomePage().showNotification("Hãy chọn lại thời gian cho báo cáo!", Clients.NOTIFICATION_TYPE_ERROR);
		}
		Timestamp timedatefrom = new Timestamp(getDateFrom().getValue().getTime());
		Timestamp timedateto = new Timestamp(getDateTo().getValue().getTime());

		String stragentid = "" + (getStrAgentId() == null ? "0" : "" + getStrAgentId());
		String strgroupid = ""
				+ (getStrGroupId() == null ? "0" : (getStrGroupId().length() == 0) ? "0" : "" + getStrGroupId());

		List<RptTotalViolateQc31> lstvalue = lstObj.getReportTotalViolateQc31(timedatefrom, timedateto, stragentid,
				strgroupid);
		return lstvalue;

	}

	@Override
	public void createHeaderExtraGrid(Grid grid) {
		Auxhead head = new Auxhead();
		head.setParent(grid);

		Auxheader header = new Auxheader();
		header.setParent(head);
		header.setColspan(6);

		header = new Auxheader("Vi phạm tốc độ");
		header.setParent(head);
		header.setColspan(2);
		header.setAlign("center");

		header = new Auxheader("Lái xe liên tục quá 4h");
		header.setParent(head);
		header.setColspan(2);
		header.setAlign("center");

		header = new Auxheader("Làm việc quá 10h");
		header.setParent(head);
		header.setColspan(2);
		header.setAlign("center");
	}

	@Override
	public void setMapParams() {
		Timestamp fromdate = new Timestamp(dateFrom.getValue().getTime());
		Timestamp todate = new Timestamp(dateTo.getValue().getTime());
		mapParamsReport.put("_fromdate", fromdate);
		mapParamsReport.put("_todate", todate);
		mapParamsReport.put("lst_agentId", strAgentId);
		mapParamsReport.put("lst_groupId", strGroupId);

		reportFile = "ReportTotalViolateQCVN31";
		reportTitle = "TỔNG ĐÀI ĐIỀU HÀNH - BÁO CÁO TỔNG HỢP VI PHẠM QCVN31";
		exportFileName = "bao_cao_tong_hop_vi_pham_qcvn31";
		reportName = "BÁO CÁO TỔNG HỢP VI PHẠM QCVN31";

	}

	// Render with Grid
	@Override
	public void renderExtraReport() {
//		gridData.setWidth("1080px");
		gridData.setRowRenderer(new RowRenderer<RptTotalViolateQc31>() {

			@Override
			public void render(Row row, RptTotalViolateQc31 data, int index) throws Exception {

				Detail detail = new Detail();
				row.appendChild(detail);
				row.setStyle("padding : 0px 0px");
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + data.getDriverName()));
				row.appendChild(new Label("" + data.getStaffCard()));
				row.appendChild(new Label("" + data.getDriverLicense()));
				row.appendChild(new Label("" + data.getGroupName()));

				row.appendChild(new Label("" + data.getOverSpeed()));
				Checkbox chbOverSpeed = new Checkbox();
				row.appendChild(chbOverSpeed);

				row.appendChild(new Label("" + data.getOverFourHour()));
				Checkbox chbOverFourHour = new Checkbox();
				row.appendChild(chbOverFourHour);

				row.appendChild(new Label("" + data.getOverTenHour()));
				Checkbox chbOverTenHour = new Checkbox();
				row.appendChild(chbOverTenHour);

				chbOverSpeed.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (chbOverSpeed.isChecked()) {
							isCheck = 1;
							gridDetail = null;
							chbOverFourHour.setChecked(false);
							chbOverTenHour.setChecked(false);

							List<Component> components = detail.getChildren();
							for (Component component : components) {
								if (component instanceof Grid) {
									gridDetail = (Grid) component;
									detail.removeChild(gridDetail);
									break;
								}
							}
							gridDetail = null;
						}

					}
				});
				chbOverFourHour.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (chbOverFourHour.isChecked()) {
							isCheck = 2;
							gridDetail = null;
							chbOverSpeed.setChecked(false);
							chbOverTenHour.setChecked(false);

							List<Component> components = detail.getChildren();
							for (Component component : components) {
								if (component instanceof Grid) {
									gridDetail = (Grid) component;
									detail.removeChild(gridDetail);
									break;
								}
							}
							gridDetail = null;
						}

					}
				});
				chbOverTenHour.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (chbOverTenHour.isChecked()) {
							isCheck = 3;
							gridDetail = null;
							chbOverSpeed.setChecked(false);
							chbOverFourHour.setChecked(false);

							List<Component> components = detail.getChildren();
							for (Component component : components) {
								if (component instanceof Grid) {
									gridDetail = (Grid) component;
									detail.removeChild(gridDetail);
									break;
								}
							}
							gridDetail = null;
						}

					}
				});

				detail.addEventListener(Events.ON_OPEN, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						if (flag) {
							flag = false;
							detail.setOpen(false);
							return;
						} else {
							if (!(chbOverSpeed.isChecked() || chbOverFourHour.isChecked()
									|| chbOverTenHour.isChecked())) {
								Env.getHomePage().showNotification("Hãy lựa chọn 1 trong 3 báo cáo để xem!",
										Clients.NOTIFICATION_TYPE_ERROR);
								flag = true;
							} else {
								Timestamp timefrom = new Timestamp(getDateFrom().getValue().getTime());
								Timestamp timeto = new Timestamp(getDateTo().getValue().getTime());
								String strAgentId = "" + (getStrAgentId() == null ? "0"
										: (getStrAgentId().length() == 0) ? "0" : getStrAgentId());
								String strgroupId = "" + (getStrGroupId() == null ? "0"
										: (getStrGroupId().length() == 0) ? "0" : getStrGroupId());
								String strvehicleId = "" + (getStrVehicleId() == null ? "0"
										: (getStrVehicleId().length() == 0) ? "0" : getStrVehicleId());
								String strdriverId = "" + data.getDriverId();

								switch (isCheck) {
								case 1:
									List<Component> components = detail.getChildren();
									for (Component component : components) {
										if (component instanceof Grid) {
											gridDetail = (Grid) component;
											break;
										}
									}

									if (gridDetail == null) {
										createGridReportOverSpeed(detail, gridDetail, timefrom, timeto, strgroupId,
												strvehicleId, strdriverId);
									} else {
										return;
									}
									break;
								case 2:
									List<Component> components2 = detail.getChildren();
									for (Component component : components2) {
										if (component instanceof Grid) {
											gridDetail = (Grid) component;
											break;
										}
									}

									if (gridDetail == null) {
										createGridReportDrivingViolate(detail, gridDetail, timefrom, timeto, strgroupId,
												strdriverId);
									} else {
										return;
									}
									break;

								case 3:
									List<Component> components3 = detail.getChildren();
									for (Component component : components3) {
										if (component instanceof Grid) {
											gridDetail = (Grid) component;
											break;
										}
									}

									if (gridDetail == null) {
										createGridReportDrivingOverTenHour(detail, gridDetail, timefrom, timeto,
												strAgentId, strgroupId, strdriverId);
									} else {
										return;
									}
									break;

								default:
									Env.getHomePage().showNotification("Không kết xuất được báo cáo chi tiết!",
											Clients.NOTIFICATION_TYPE_ERROR);
									break;
								}
								flag = true;
							}
						}
					}
				});
			}
		});
	}
	
	@Override
	public void setStyleForGridData() {
		getGridData().setVflex("true");
		getGridData().setMold("paging");
		gridData.setAutopaging(true);
		// getGridData().setPageSize(20);
		getGridData().setSclass("grid_report_total");
	}

	@Override
	public void renderReportWithListBox() {
		setRenderReportWithListBox(false);

	}

	private void createGridReportOverSpeed(Detail detail, Grid griddetail, Timestamp timefrom, Timestamp timeto,
			String strgroupId, String strvehicleId, String strdriverId) {
		if (griddetail == null) {
			griddetail = new Grid();
			griddetail.setWidth("2030px");
			griddetail.setHeight("100%");
			griddetail.setParent(detail);
			griddetail.setMold("paging");
			griddetail.setPageSize(2);
			griddetail.setSpan(true);
			griddetail.setSizedByContent(true);

			Auxhead auxhead = new Auxhead();
			auxhead.setParent(griddetail);
			Auxheader auxheader = new Auxheader();
			auxheader.setParent(auxhead);
			auxheader.setColspan(14);
			auxheader.setLabel("CHI TIẾT QUÁ TỐC ĐỘ GIỚI HẠN");
			auxheader.setStyle("color : black; font-size : 12px; font-weight : bold; margin-left : 50px");

			Frozen frozen = new Frozen();
			frozen.setParent(griddetail);
			frozen.setColumns(2);

			Columns cols = new Columns();
			cols.setParent(griddetail);

			Column col = new Column();
			col.setParent(cols);
			col.setLabel("TT");
			col.setWidth("40px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("center");

			col = new Column();
			col.setParent(cols);
			col.setLabel("BKS");
			col.setWidth("100px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Loại kinh doanh");
			col.setWidth("100px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Tốc độ");
			col.setWidth("130px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Tốc độ cho phép");
			col.setWidth("130px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Thời điểm bắt đầu");
			col.setWidth("130px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Thời điểm kết thúc");
			col.setWidth("130px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Thời gian");
			col.setWidth("100px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Vị trí bắt đầu");
			col.setWidth("450px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Vị trí kết thúc");
			col.setWidth("450px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Quãng đường (km)");
			col.setWidth("150px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Hành trình");
			col.setWidth("120px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			griddetail.setRowRenderer(new RowRenderer<RptQcTruckStdOverSpeed>() {

				@Override
				public void render(Row row, RptQcTruckStdOverSpeed data, int index) throws Exception {
					SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					row.appendChild(new Label("" + ++index));
					row.appendChild(new Label("" + data.getLicensePlate()));
					row.appendChild(new Label("" + data.getTypeName()));
					row.appendChild(new Label("" + data.getSpeed()));
					row.appendChild(new Label("" + data.getSpeedLimit()));
					row.appendChild(new Label("" + dateformat.format(data.getTimeStart())));
					row.appendChild(new Label("" + dateformat.format(data.getTimeStop())));
					row.appendChild(new Label("" + data.getTime()));
					row.appendChild(new Label("" + data.getAddrBegin()));
					row.appendChild(new Label("" + data.getAddrFinish()));
					row.appendChild(new Label("" + data.getKm()));
					Button btnHistory = new Button("Hành trình");
					btnHistory.setStyle("color : black;font-size : 12px");
					btnHistory.setHeight("25px");
					row.appendChild(btnHistory);
					btnHistory.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

						@Override
						public void onEvent(Event arg0) throws Exception {
							showHistoryStdOverSpeed(data);
						}
					});
				}
			});

		}
		ListObjectDatabase lstObj = new ListObjectDatabase();
		List<RptQcTruckStdOverSpeed> lstValue = lstObj.getDetailQcTruckStdSpeed(user, timefrom, timeto, strgroupId,strdriverId);
		if (lstValue == null || lstValue.isEmpty()) {
			griddetail.setEmptyMessage("Không có dữ liệu");
		} else {
			griddetail.setModel(new ListModelList<RptQcTruckStdOverSpeed>(lstValue));
		}
	}

	private void createGridReportDrivingViolate(Detail detail, Grid griddetail, Timestamp timefrom, Timestamp timeto,
			String strgroupId, String strdriverID) {
		// if (griddetail == null) {
		griddetail = new Grid();
		griddetail.setWidth("2030px");
		griddetail.setHeight("100%");
		griddetail.setParent(detail);
		griddetail.setMold("paging");
		griddetail.setPageSize(2);
		griddetail.setSpan(true);
		griddetail.setSizedByContent(true);

		Auxhead auxhead = new Auxhead();
		auxhead.setParent(griddetail);
		Auxheader auxheader = new Auxheader();
		auxheader.setParent(auxhead);
		auxheader.setColspan(14);
		auxheader.setLabel("CHI TIẾT LÁI XE LIÊN TỤC QUÁ 4 GIỜ");
		auxheader.setStyle("color : black; font-size : 12px; font-weight : bold; margin-left : 50px");

		Frozen frozen = new Frozen();
		frozen.setParent(griddetail);
		frozen.setColumns(2);

		Columns cols = new Columns();
		cols.setParent(griddetail);

		Column col = new Column();
		col.setParent(cols);
		col.setLabel("TT");
		col.setWidth("40px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("center");

		col = new Column();
		col.setParent(cols);
		col.setLabel("BKS");
		col.setWidth("100px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Thời điểm bắt đầu");
		col.setWidth("130px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Thời điểm kết thúc");
		col.setWidth("130px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Thời gian");
		col.setWidth("100px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Loại kinh doanh");
		col.setWidth("100px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Vị trí bắt đầu");
		col.setWidth("450px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Vị trí kết thúc");
		col.setWidth("450px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Quãng đường (km)");
		col.setWidth("150px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		col = new Column();
		col.setParent(cols);
		col.setLabel("Hành trình");
		col.setWidth("120px");
		col.setStyle("color : black ; font-weight : bold");
		col.setAlign("left");

		griddetail.setRowRenderer(new RowRenderer<RptQcTrunkStdDriving>() {

			@Override
			public void render(Row row, RptQcTrunkStdDriving data, int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + data.getLicensePlate()));
				row.appendChild(new Label("" + dateformat.format(data.getBeginTime())));
				row.appendChild(new Label("" + dateformat.format(data.getEndTime())));
				row.appendChild(new Label("" + data.getTimeOverStr()));
				row.appendChild(new Label("" + data.getTypeName()));
				row.appendChild(new Label("" + data.getBeginAddr()));
				row.appendChild(new Label("" + data.getEndAddr()));
				row.appendChild(new Label("" + data.getKmGPS()));
				Button btnHistory = new Button("Hành trình");
				btnHistory.setStyle("color : black;font-size : 12px");
				btnHistory.setHeight("25px");
				row.appendChild(btnHistory);
				btnHistory.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						showHistoryStdDrivingOver4Hour(data);
					}
				});
			}
		});

		// }

		// Set data for Grid
		ListObjectDatabase lstObj = new ListObjectDatabase();
		List<RptQcTrunkStdDriving> lstValue = lstObj.getDetailTrunkStdDrivingOverTime(user, timefrom, timeto,
				strgroupId, strdriverID);
		if (lstValue == null || lstValue.isEmpty()) {
			griddetail.setEmptyMessage("Không có dữ liệu");
		} else {
			griddetail.setModel(new ListModelList<RptQcTrunkStdDriving>(lstValue));
		}
	}

	private void createGridReportDrivingOverTenHour(Detail detail, Grid griddetail, Timestamp timefrom,
			Timestamp timeto, String strAgentId, String strGroupId, String strDriverId) {
		if (griddetail == null) {
			griddetail = new Grid();
			griddetail.setWidth("850px");
			griddetail.setHeight("100%");
			griddetail.setParent(detail);
			griddetail.setMold("paging");
			griddetail.setPageSize(2);
			griddetail.setSpan(true);
			griddetail.setSizedByContent(true);

			Auxhead auxhead = new Auxhead();
			auxhead.setParent(griddetail);
			Auxheader auxheader = new Auxheader();
			auxheader.setParent(auxhead);
			auxheader.setColspan(14);
			auxheader.setLabel("CHI TIẾT LÁI XE QUÁ 10 GIỜ TRONG NGÀY");
			auxheader.setStyle("color : black; font-size : 12px; font-weight : bold; margin-left : 50px");

			Frozen frozen = new Frozen();
			frozen.setParent(griddetail);
			frozen.setColumns(2);

			Columns cols = new Columns();
			cols.setParent(griddetail);

			Column col = new Column();
			col.setParent(cols);
			col.setLabel("TT");
			col.setWidth("40px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("center");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Họ và tên");
			col.setWidth("170px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("MSNV");
			col.setWidth("100px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("GPLX");
			col.setWidth("120px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Ngày vi phạm");
			col.setWidth("120px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Đội xe");
			col.setWidth("200px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			col = new Column();
			col.setParent(cols);
			col.setLabel("Thời gian(phút)");
			col.setWidth("100px");
			col.setStyle("color : black ; font-weight : bold");
			col.setAlign("left");

			griddetail.setRowRenderer(new RowRenderer<ReportDetailOverTenHour>() {

				@Override
				public void render(Row row, ReportDetailOverTenHour data, int index) throws Exception {
					SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
					row.appendChild(new Label("" + ++index));
					row.appendChild(new Label("" + data.getDriverName()));
					row.appendChild(new Label("" + data.getStaffCard()));
					row.appendChild(new Label("" + data.getDriverLicense()));
					row.appendChild(new Label("" + dateformat.format(data.getTimeLog())));
					row.appendChild(new Label("" + data.getGroupName()));
					row.appendChild(new Label("" + data.getTimeOver()));
					Button btnHistory = new Button("Lịch sử");
					btnHistory.setStyle("color : black;font-size : 12px");
					btnHistory.setHeight("25px");
					row.appendChild(btnHistory);
				}
			});

		}

		ListObjectDatabase lstObj = new ListObjectDatabase();
		List<ReportDetailOverTenHour> lstValue = lstObj.getDetailOverTenHour(timefrom, timeto, strAgentId, strGroupId,
				strDriverId);
		if (lstValue == null || lstValue.isEmpty()) {
			griddetail.setEmptyMessage("Không có dữ liệu");
		} else {
			griddetail.setModel(new ListModelList<ReportDetailOverTenHour>(lstValue));
		}

	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(btnExcel)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				Grid gridExcel = (Grid) getGridData();
				Iterator<Component> component = gridExcel.getChildren().iterator();
				while (component.hasNext()) {
					Component value = component.next();
					if (((value instanceof Frozen)) || ((value instanceof Auxhead)) || ((value instanceof Detail))
							|| ((value instanceof Checkbox))) {
						gridExcel.removeChild((Component) value);
						component = gridExcel.getChildren().iterator();
					}
				}

				CommonUtils.exportListboxToExcel(gridExcel, "bao_cao_tong_hop_vi_pham_qcvn31.xlsx");
				AppLogger.logDebug.info("Xuat file excel bao cao vi pham qc31");
			}
		} else {
			super.onEvent(event);
		}
	}
	
	private void showHistoryStdOverSpeed(RptQcTruckStdOverSpeed data){
		TrackingHistory history = new TrackingHistory(new java.sql.Date(data.getTimeStart()
				.getTime()), new java.sql.Date(data.getTimeStop().getTime()),
				data.getVehicleId());
		Window window = new Window();
		window.setWidth("100%");
		window.setHeight("100%");
		window.setClosable(true);
		window.setTitle("Lịch sử hành trình");
		window.setParent(this);
		window.appendChild(history);
		window.doModal();
	}
	
	private void showHistoryStdDrivingOver4Hour(RptQcTrunkStdDriving data){
		TrackingHistory history = new TrackingHistory(new java.sql.Date(data.getBeginTime()
				.getTime()), new java.sql.Date(data.getEndTime().getTime()),
				data.getVehicleId());
		Window window = new Window();
		window.setWidth("100%");
		window.setHeight("100%");
		window.setClosable(true);
		window.setTitle("Lịch sử hành trình");
		window.setParent(this);
		window.appendChild(history);
		window.doModal();
	}
}
