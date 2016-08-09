package com.vietek.taxioperation.ui.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.database.DatabaseUtils;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.util.ComponentsReport;
import com.vietek.taxioperation.util.Env;
import com.vietek.tracking.ui.utility.TrackingHistory;
import com.vietek.trackingOnline.common.RowWarningReport;

public class ProcessWarningWindow extends Window {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox txtnguoixuly;
	private Textbox txtnoidung;
	private Combobox cbtrangthai;
	private Datebox dtthoidiemghinhan;
	private Button btnXemhanhtrinh;
	private Button btnXacnhan;
	private Button btnKetthuc;
	private RowWarningReport rptModel;
	private int typeWarning;

	public ProcessWarningWindow(Object model, int type) {
		this.setWidth("650px");
		this.setHeight("400px");
		if (model instanceof RowWarningReport) {
			rptModel = (RowWarningReport) model;
		}
		initUI();
		initValue();
		this.typeWarning = type;
	}

	private void initValue() {

	}

	private void initUI() {
		Grid grid = new Grid();
		grid.setParent(this);
		grid.setStyle("padding-bottom: 15px;");
		Columns cols = new Columns();
		cols.setParent(grid);
		Column col = new Column();
		col.setWidth("20%");
		col.setParent(cols);
		col = new Column();
		col.setWidth("30%");
		col.setParent(cols);
		col = new Column();
		col.setWidth("20%");
		col.setParent(cols);
		col = new Column();
		col.setWidth("30%");
		col.setParent(cols);
		Rows rows = new Rows();
		rows.setParent(grid);
		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Người xử lý:"));
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);
		txtnguoixuly = new Textbox();
		txtnguoixuly.setPlaceholder("Người xử lý");
		cell.appendChild(txtnguoixuly);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Nội dung:"));
		cell = new Cell();
		cell.setColspan(3);
		cell.setParent(row);
		txtnoidung = new Textbox();
		txtnoidung.setPlaceholder("Nội dung");
		txtnoidung.setMultiline(true);
		txtnoidung.setHeight("200px");
		txtnoidung.setHflex("1");
		cell.appendChild(txtnoidung);

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Trạng thái:"));
		cbtrangthai = ComponentsReport.ComboboxRendering(new String[] { "Đã hoàn thành", "Chưa hoàn thành" },
				new int[] { 0, 1 }, "", "", 150, 30, false);
		cbtrangthai.setSclass("z-combobox-history");
		cbtrangthai.setSelectedIndex(-1);
		row.appendChild(cbtrangthai);
		row.appendChild(new Label("Thời điểm ghi nhận:"));
		dtthoidiemghinhan = new Datebox();
		dtthoidiemghinhan.setStyle("width:130px");
		dtthoidiemghinhan.setFormat("dd/MM/yyyy HH:mm");
		dtthoidiemghinhan.setValue(new Date(System.currentTimeMillis()));
		row.appendChild(dtthoidiemghinhan);
		Hlayout bottonLayout = new Hlayout();
		bottonLayout.setStyle("padding:10px; text-align: center;");
		bottonLayout.setParent(this);
		bottonLayout.setValign("center");
		btnXemhanhtrinh = new Button("Xem H.Trinh");
		btnXemhanhtrinh.setStyle("font-weight: bold;width:110px");
		btnXemhanhtrinh.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				openHistory(rptModel);
			}
		});
		bottonLayout.appendChild(btnXemhanhtrinh);
		btnXacnhan = new Button("Xác Nhận");
		btnXacnhan.setStyle("font-weight: bold;width:110px");
		btnXacnhan.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				handleProcessWarning();
			}
		});
		bottonLayout.appendChild(btnXacnhan);
		btnKetthuc = new Button("Kết Thúc");
		btnKetthuc.setStyle("font-weight: bold;width:110px");
		btnKetthuc.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				closeWarningWindow();
			}
		});
		bottonLayout.appendChild(btnKetthuc);
	}

	protected void handleProcessWarning() {
		List<Object> params = new ArrayList<>();
		String msg = validateValue();
		if (msg.equals("")) {
			Vehicle vehicle = MapCommon.MAP_VEHICLE_ID.get(rptModel.getVehicleID());
			params.add(vehicle.getDeviceID());
			params.add(Env.getUser().getId());
			params.add(txtnguoixuly.getValue());
			params.add(txtnoidung.getValue());
			params.add(typeWarning);
			params.add(cbtrangthai.getSelectedItem().getValue());
			String cmdcommand = "Call txm_tracking.cmdUpdateErrorProcessing(?,?,?,?,?,?)";
			try {
				int resultupdate = DatabaseUtils.executeUpdate(cmdcommand, params);
				if (resultupdate > 0) {
					Env.getHomePage().showNotification("Xử lý thành công !", Clients.NOTIFICATION_TYPE_INFO);
					closeWarningWindow();
				} else {
					Env.getHomePage().showNotification("Không thể cập nhật!", Clients.NOTIFICATION_TYPE_ERROR);
				}

			} catch (Exception e) {
				Env.getHomePage().showNotification("Không thể cập nhật", Clients.NOTIFICATION_TYPE_ERROR);
			}
		} else {
			Env.getHomePage().showNotification(msg, Clients.NOTIFICATION_TYPE_ERROR);
		}
	}

	private String validateValue() {
		String msg = "";
		return msg;
	}

	private void closeWarningWindow() {
		this.detach();
	}

	private void openHistory(RowWarningReport data) {
		Window window = new Window();
		window.setStyle("height:100%;width:100%");
		window.setTitle("Lịch sử hành trình");
		window.setClosable(true);
		window.setParent(this);
		Date datefrom = new Date(data.getBegintime().getTime());
		Date dateto = new Date(data.getEndtime().getTime());
		TrackingHistory his = new TrackingHistory(datefrom, dateto, data.getVehicleID());
		his.setParent(window);
		window.doModal();
	}

	public Textbox getTxtnguoixuly() {
		return txtnguoixuly;
	}

	public void setTxtnguoixuly(Textbox txtnguoixuly) {
		this.txtnguoixuly = txtnguoixuly;
	}

	public Textbox getTxtnoidung() {
		return txtnoidung;
	}

	public void setTxtnoidung(Textbox txtnoidung) {
		this.txtnoidung = txtnoidung;
	}

	public Combobox getCbtrangthai() {
		return cbtrangthai;
	}

	public void setCbtrangthai(Combobox cbtrangthai) {
		this.cbtrangthai = cbtrangthai;
	}

	public Datebox getDtthoidiemghinhan() {
		return dtthoidiemghinhan;
	}

	public void setDtthoidiemghinhan(Datebox dtthoidiemghinhan) {
		this.dtthoidiemghinhan = dtthoidiemghinhan;
	}

	public RowWarningReport getRptModel() {
		return rptModel;
	}

	public void setRptModel(RowWarningReport rptModel) {
		this.rptModel = rptModel;
	}

}
