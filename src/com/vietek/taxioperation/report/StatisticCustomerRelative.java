package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.ReportCustomerRelative;
import com.vietek.taxioperation.model.ReportQcTripDetail;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.DateUtils;
import com.vietek.taxioperation.util.Env;

public class StatisticCustomerRelative extends Window implements EventListener<Event> {

	/**
	 * @author Dzung
	 * @throws Exception
	 */

	private static final long serialVersionUID = 1L;
	private Datebox datefrom;
	private Datebox dateto;

	private Textbox txtPhone;
	private Intbox callAmount;
	private Grid gridData;

	private Button btnsearch;
	private Button btnExcel;
	private Label labelSum;

	public StatisticCustomerRelative() throws Exception {
		this.init();
	}

	public void init() {
		this.setShadow(true);
		this.setVflex("1");
		this.setHflex("1");
		this.setClosable(true);

		Hlayout hlayout = new Hlayout();
		hlayout.setParent(this);
		hlayout.setVflex("1");
		hlayout.setHflex("1");

		this.initTopPanel(hlayout);
		this.initBottomPanel(hlayout);
	}

	public void initTopPanel(Hlayout hlayout) {
		Panel toppanel = new Panel();
		toppanel.setParent(hlayout);
		toppanel.setHeight("100%");
		toppanel.setHflex("2.5");
		toppanel.setStyle("color : black; font-size : 14px; font-weight : bold;");
		toppanel.setTitle("ĐIỀU KIỆN TÌM KIẾM");

		Panelchildren child = new Panelchildren();
		child.setParent(toppanel);

		Window window = new Window();
		window.setParent(child);
		window.setHflex("1");
		window.setVflex("1");

		Grid filter = new Grid();
		filter.setParent(window);
		filter.setStyle("border: none");
		window.setHflex("1");
		filter.setVflex("1");

		// columns
		Columns cols = new Columns();
		cols.setParent(filter);
		// title
		Column col = new Column();
		col.setParent(cols);
		col.setWidth("30%");
		// value
		col = new Column();
		col.setParent(cols);
		col.setWidth("70%");

		Rows rows = new Rows();
		rows.setParent(filter);

		Row row = new Row();
		row.setParent(rows);
		Label label = new Label("Từ ngày");
		label.setStyle("font-weight : bold; font-size : 14px;");
		label.setParent(row);
		datefrom = new Datebox();
		datefrom.setStyle("font-size : 14px;");
		datefrom.setFormat("dd/MM/yyyy");
		datefrom.setValue(DateUtils.addHour(new Date(), 0, 0));
		datefrom.setConstraint("no empty: Không được để trống");
		datefrom.setParent(row);

		row = new Row();
		row.setParent(rows);
		label = new Label("Đến ngày");
		label.setStyle("font-weight : bold; font-size : 14px;");
		label.setParent(row);
		dateto = new Datebox();
		dateto.setStyle("font-size : 14px;");
		dateto.setFormat("dd/MM/yyyy");
		dateto.setValue(DateUtils.addHour(new Date(), 0, 0));
		dateto.setConstraint("no empty: Không được để trống");
		dateto.setParent(row);

		row = new Row();
		row.setParent(rows);
		label = new Label("Số ĐT");
		label.setStyle("font-weight: bold; font-size: 14px;");
		label.setParent(row);
		txtPhone = new Textbox();
		txtPhone.setStyle("font-weight: bold; font-size: 16px;");
		txtPhone.setWidth("80%");
		txtPhone.setParent(row);
		txtPhone.addEventListener(Events.ON_OK, this);

		row = new Row();
		row.setParent(rows);
		label = new Label("Lần gọi >=");
		label.setStyle("font-weight: bold; font-size: 14px;");
		label.setParent(row);
		callAmount = new Intbox();
		callAmount.setParent(row);
		callAmount.setConstraint("no empty,no negative");
		callAmount.setStyle("font-weight: bold; font-size: 16px;");
		callAmount.setWidth("80%");
		callAmount.addEventListener(Events.ON_OK, this);

		row = new Row();
		row.setParent(rows);
		label = new Label("");
		label.setParent(row);
		labelSum = new Label("Tổng số bản ghi: " + 0);
		labelSum.setStyle("color: black; font-weight: bold");
		labelSum.setParent(row);

		row = new Row();
		row.setParent(rows);
		label = new Label("");
		label.setParent(row);
		Div ctrl = new Div();
		ctrl.setParent(row);
		btnsearch = new Button("Thống kê");
		btnsearch.setStyle("color: black; font-weight: bold; float: left; margin-right: 5px;");
		btnsearch.setParent(ctrl);
		btnExcel = new Button("Excel");
		btnExcel.setStyle("color: black; font-weight: bold; float: left; margin-right: 5px;");
		btnExcel.setParent(ctrl);
		btnsearch.addEventListener(Events.ON_CLICK, this);
		btnExcel.addEventListener(Events.ON_CLICK, this);
	}

	public void initBottomPanel(Hlayout hlayout) {
		gridData = new Grid();
		gridData.setParent(hlayout);
		gridData.setHflex("7.5");
		gridData.setVflex("1");
		gridData.setMold("paging");
		gridData.setSclass("gridcus");

		// columns
		Columns cols = new Columns();
		cols.setParent(gridData);
		// STT
		Column col = new Column();
		col.setParent(cols);
		col.setWidth("5%");
		// STT
		col = new Column();
		col.setParent(cols);
		col.setWidth("10%");
		// STT
		col = new Column();
		col.setParent(cols);
		col.setWidth("5%");
		col.setLabel("STT");
		// phone
		col = new Column();
		col.setParent(cols);
		col.setWidth("12%");
		col.setLabel("Số điện thoại");
		// So lan goi
		col = new Column();
		col.setParent(cols);
		col.setWidth("5%");
		col.setLabel("Lần gọi");
		// Chuyen thanh cong
		col = new Column();
		col.setParent(cols);
		col.setWidth("8%");
		col.setLabel("Thành công");
		// Lan goi cuoi
		col = new Column();
		col.setParent(cols);
		col.setWidth("15%");
		col.setLabel("Lần gọi cuối");
		// Vi tri dat xe
		col = new Column();
		col.setParent(cols);
		col.setWidth("40%");
		col.setLabel("Vị trí đặt xe");

		gridData.setRowRenderer(new RowRenderer<ReportCustomerRelative>() {
			@Override
			public void render(Row row, ReportCustomerRelative data, int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				Detail detail = new Detail();
				row.appendChild(detail);
				Button btnPrintDetail = new Button("In chi tiết");
				row.appendChild(btnPrintDetail);
				row.appendChild(new Label("" + ++index));
				row.appendChild(new Label("" + data.getPhone()));
				row.appendChild(new Label("" + data.getCallAmount()));
				row.appendChild(new Label("" + data.getCallSuccess()));
				row.appendChild(new Label(
						"" + (data.getLastOrderTime() == null ? "" : dateformat.format(data.getLastOrderTime()))));
				row.appendChild(new Label("" + data.getLastOrderAddress()));

				detail.addEventListener(Events.ON_OPEN, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// TODO Auto-generated method stub
						List<Component> components = detail.getChildren();
						Grid grid = null;
						for (Component component : components) {
							if (component instanceof Grid) {
								grid = (Grid) component;
							}
						}
						renderingdetail(creatingDetail(detail, grid), data.getPhone());
					}
				});

				btnPrintDetail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						// TODO Auto-generated method stub
						Row row = (Row) btnPrintDetail.getParent();
						List<Component> components = row.getChildren();
						for (Component component : components) {
							if (component instanceof Detail) {
								Detail detail = (Detail) component;
								List<Component> components2 = detail.getChildren();
								for (Component component2 : components2) {
									if (component2 instanceof Grid) {
										Grid grid = (Grid) component2;
										if (grid.getModel() != null) {
											CommonUtils.exportListboxToExcel(grid, "chi_tiet_cuoc_goi.xlsx");
										}
										break;
									}
								}
								break;
							}
						}
					}
				});
			}
		});
	}

	public Grid creatingDetail(Detail parent, Grid detail) {
		if (detail == null) {
			detail = new Grid();
			detail.setParent(parent);
			detail.setHflex("1");
			detail.setVflex("1");
			detail.setMold("paging");

			// columns
			Columns cols = new Columns();
			cols.setParent(detail);
			// STT
			Column col = new Column();
			col.setParent(cols);
			col.setWidth("5%");
			col.setLabel("TT");
			//
			col = new Column();
			col.setParent(cols);
			col.setWidth("10%");
			col.setLabel("Số điện thoại");
			//
			col = new Column();
			col.setParent(cols);
			col.setWidth("15%");
			col.setLabel("Thời điểm");
			//
			col = new Column();
			col.setParent(cols);
			col.setWidth("10%");
			col.setLabel("Hình thức");
			//
			col = new Column();
			col.setParent(cols);
			col.setWidth("35%");
			col.setLabel("Điểm đặt xe");
			//
			col = new Column();
			col.setParent(cols);
			col.setWidth("15%");
			col.setLabel("Xe Đ.ký");
			//
			col = new Column();
			col.setParent(cols);
			col.setWidth("10%");
			col.setLabel("Ghi chú");

			detail.setRowRenderer(new RowRenderer<ReportQcTripDetail>() {

				@Override
				public void render(Row row, ReportQcTripDetail data, int index) throws Exception {
					SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
					row.appendChild(new Label("" + ++index));
					row.appendChild(new Label("" + data.getPhoneNumber()));
					row.appendChild(new Label(""
							+ (data.getBeginOrderTime() == null ? "" : dateformat.format(data.getBeginOrderTime()))));
					row.appendChild(new Label("" + data.getOrderCarType()));
					row.appendChild(new Label("" + data.getBeginOrderAddress()));
					row.appendChild(new Label("" + data.getListRegisterVeh()));
					row.appendChild(new Label("" + data.getCancelReason()));
				}
			});
		}
		return detail;
	}

	public void renderingdetail(Grid detail, String phone) {
		Timestamp from = new Timestamp(datefrom.getValue().getTime());
		Timestamp to = new Timestamp(dateto.getValue().getTime());
		ListObjectDatabase lst = new ListObjectDatabase();
		List<ReportQcTripDetail> results = lst.getrptCallingByOperationDetail(from, to, phone);
		if (results == null || results.isEmpty()) {
			detail.setEmptyMessage("Không có dữ liệu");
		} else {
			detail.setModel(new ListModelList<ReportQcTripDetail>(results));
		}
	}

	public boolean checkingInputCondition() {
		try {
			long timedateto = dateto.getValue().getTime();
			long timedatefrom = datefrom.getValue().getTime();
			if (timedatefrom > timedateto) {
				Env.getHomePage().showNotification("Hãy chọn lại thời gian cho báo cáo!",
						Clients.NOTIFICATION_TYPE_ERROR);
				labelSum.setValue("Tổng số bản ghi: " + 0);
				gridData.setEmptyMessage("Không có dữ liệu !");
				return false;
			}
			if (callAmount.getValue() == null || callAmount.getValue() < 1) {
				Env.getHomePage().showNotification("Vui lòng nhập Số lần gọi (>= 1)", Clients.NOTIFICATION_TYPE_ERROR);
				labelSum.setValue("Tổng số bản ghi: " + 0);
				gridData.setEmptyMessage("Không có dữ liệu !");
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void getCustomerRelativeRessult() {
		if (checkingInputCondition()) {
			try {
				Timestamp fromdate = new Timestamp(datefrom.getValue().getTime());
				Timestamp todate = new Timestamp(dateto.getValue().getTime());
				String phone = txtPhone.getValue();
				int amount = callAmount.getValue();
				ListObjectDatabase lst = new ListObjectDatabase();
				List<ReportCustomerRelative> lstValue = lst.getrptCustomerRelative(fromdate, todate, phone, amount);
				if (lstValue == null || lstValue.isEmpty()) {
					gridData.setEmptyMessage("Không có dữ liệu");
					labelSum.setValue("Tổng số bản ghi: " + 0);
				} else {
					gridData.setModel(new ListModelList<ReportCustomerRelative>(lstValue));
					labelSum.setValue("Tổng số bản ghi: " + lstValue.size());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget().equals(btnsearch)) {
				this.getCustomerRelativeRessult();
			} else if (event.getTarget().equals(btnExcel)) {
				CommonUtils.exportListboxToExcel(gridData, "thong_ke_khach_hang.xlsx");
			}
		} else if (event.getName().equals(Events.ON_OK)) {
			if (event.getTarget().equals(txtPhone) || event.getTarget().equals(callAmount)) {
				this.getCustomerRelativeRessult();
			}
		}
	}
}