package com.vietek.taxioperation.ui.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Center;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.South;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.database.DatabaseUtils;
import com.vietek.taxioperation.model.RequestBaggageFinding;
import com.vietek.taxioperation.model.RequestType;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.ComboboxSearch;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.tracking.ui.model.FindingResult;
import com.vietek.tracking.ui.utility.TrackingHistory;

public class BaggageFinding extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RequestBaggageFinding model;
	private Label khachhang;
	private Label sodienthoai;
	private Label diachi;
	private Label noidung;
	private Label giodon;
	private Label giotra;
	private Label diemdon;
	private Label diemtra;
	private Label diemqua1;
	private Label diemqua2;
	private Label tiencuoc;
	private Button btnTim;
	private Button btnHuyTimKiem;
	private Button btnXuly;
	private Listbox listresult;
	private Listbox listProcess;
	private Textbox txtConten;
	private ComboboxSearch cmbStatusProcess;
	private AbstractWindowPanel listWindow;

	public BaggageFinding(RequestBaggageFinding model, AbstractWindowPanel listWindow) {
		this.model = model;
		this.listWindow = listWindow;
		this.setTitle("XỬ LÝ THÔNG TIN HÀNH LÝ THẤT LẠC");
		this.setStyle("Width:100%;Height:100%");
		this.setClosable(true);
		initUI();

	}

	private void initUI() {
		Borderlayout borderlayout = new Borderlayout();
		borderlayout.setParent(this);
		creatWestLayout(borderlayout);
		creatCenterLayout(borderlayout);
		initValue();
	}

	private void creatSouthLayout(Borderlayout borderlayout) {
		South south = new South();
		south.setCollapsible(true);
		south.setParent(borderlayout);
		south.setHeight("180px");
		south.setTitle("Xử lý yêu cầu");
		Hlayout hl = new Hlayout();
		hl.setVflex("1");
		hl.setParent(south);
		listProcess = new Listbox();
		listProcess.setWidth("400px");
		listProcess.setVflex("1");
		listProcess.setParent(hl);
		listProcess.setItemRenderer(new ListitemRenderer<FindingResult>() {

			@Override
			public void render(Listitem item, FindingResult data, int index) throws Exception {
				item.setValue(data.getTripID());
				new Listcell(index + 1 + "").setParent(item);
				new Listcell(data.getVehicleNumber()).setParent(item);
				new Listcell(data.getLicensePlate()).setParent(item);
			}

		});
		listProcess.setModel(new ListModelList<>());
		Listhead listhead = new Listhead();
		listhead.setParent(listProcess);
		Listheader header = new Listheader("STT");
		header.setWidth("50px");
		header.setParent(listhead);
		header = new Listheader("Số tài");
		header.setWidth("100px");
		header.setParent(listhead);
		header = new Listheader("Biển kiểm soát");
		header.setWidth("250px");
		header.setParent(listhead);
		Vlayout vl = new Vlayout();
		vl.setParent(hl);
		vl.setVflex("1");
		vl.setWidth("300px");
		vl.setStyle("margin-left: 10px; margin-top:5px");
		Hlayout hl1 = new Hlayout();
		hl1.setParent(vl);
		hl1.setVflex("1");
		hl1.setStyle("padding-top: 15px;");
		hl1.appendChild(new Label("Trạng thái:"));
		cmbStatusProcess = new ComboboxSearch(RequestType.class, "From RequestType");
		cmbStatusProcess.setButtonVisible(true);
		cmbStatusProcess.setHflex("1");
		cmbStatusProcess.setPlaceholder("Trạng thái xử lý");
		hl1.appendChild(cmbStatusProcess);
		hl1 = new Hlayout();
		hl1.setParent(vl);
		hl1.setVflex("1");
		hl1.appendChild(new Label("Nội dung:"));
		txtConten = new Textbox();
		txtConten.setPlaceholder("Nội dung xử lý");
		txtConten.setMultiline(true);
		txtConten.setVflex("1");
		txtConten.setHflex("1");
		hl1.appendChild(txtConten);

		vl = new Vlayout();
		vl.setStyle("float:right;margin-left:120px");
		Vlayout vl1 = new Vlayout();
		vl1.setHeight("75px");
		vl.setParent(hl);
		vl.appendChild(vl1);
		btnXuly = new Button("Xử lý");
		btnXuly.setStyle("float: left; Margin-right:100px; width: 120px");
		btnXuly.setSclass("btn-default btn z-btn-history");
		btnXuly.addEventListener(Events.ON_CLICK, LISTENER_XU_LY);
		vl.appendChild(btnXuly);

	}

	private void initValue() {
		khachhang.setValue(model.getCustomerName());
		sodienthoai.setValue(model.getPhoneNumber());
		diachi.setValue(model.getCustomerAddr());
		noidung.setValue(model.getRequestContent());
		giodon.setValue(StringUtils.valueOfTimestamp(model.getBeginTime()));
		giotra.setValue(StringUtils.valueOfTimestamp(model.getFinishTime()));
		diemdon.setValue(model.getBeginAddr());
		diemtra.setValue(model.getFinishAddr());
		diemqua1.setValue(model.getAddress1());
		diemqua2.setValue(model.getAddress2());
		tiencuoc.setValue(StringUtils.priceWithoutDecimal(model.getMoney() * 1000) + " VND");
		listresult.setModel(new ListModelList<>());
		txtConten.setValue(model.getRequestContent());
		cmbStatusProcess.setValue(model.getRequesttype());
		List<FindingResult> findings = getFeedBack(model.getFeedbackResult());
		if (findings.size() > 0) {
			btnTim.setDisabled(true);
		}else {
			btnTim.setDisabled(false);
		}
		listProcess.setModel(new ListModelList<>(findings));

	}

	private List<FindingResult> getFeedBack(String arrid) {
		List<FindingResult> lstfeed = new ArrayList<>();
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		java.sql.CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sessionImplementor.connection();
			if (conn == null) {
				return null;
			}
			cs = conn.prepareCall("Call txm_tracking.cmdGetTripInfo(?,?,?,?)");
			Timestamp begin = new Timestamp(model.getBeginTime().getTime() - TimeUnit.MINUTES.toMillis(10));
			cs.setObject(1, begin);
			cs.setObject(2, model.getFinishTime());
			cs.setObject(3, model.getAgent().getId());
			cs.setObject(4, arrid);
			rs = cs.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					FindingResult findrs = new FindingResult();
					findrs.setTripID(rs.getInt(1));
					findrs.setVehicleID(rs.getInt(2));
					findrs.setVehicleNumber(rs.getString(3));
					findrs.setLicensePlate(rs.getString(4));
					findrs.setBeginTime(rs.getTimestamp(5));
					findrs.setStopTime(rs.getTimestamp(6));
					findrs.setBeginPosition(rs.getString(7));
					findrs.setStopPosition(rs.getString(8));
					findrs.setFullPath(rs.getString(9));
					lstfeed.add(findrs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Env.getHomePage().showNotification("Không thể cập nhật!", Clients.NOTIFICATION_TYPE_ERROR);
		} finally {
			session.close();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}
		return lstfeed;
	}

	private void creatCenterLayout(Borderlayout borderlayout) {
		Center center = new Center();
		center.setParent(borderlayout);
		Borderlayout borderCenter = new Borderlayout();
		borderCenter.setParent(center);
		Center result = new Center();
		result.setParent(borderCenter);
		Div div = new Div();
		div.setVflex("1");
		div.setParent(result);
		listresult = new Listbox();
		listresult.setSclass("z-grid-result");
		listresult.setVflex("true");
		listresult.setParent(div);
		listresult.setCheckmark(true);
		listresult.setEmptyMessage("Không có dữ liệu!");
		listresult.setSizedByContent(true);
		listresult.setSpan(true);
		listresult.addEventListener(Events.ON_SELECT, LISTENER_SELECT_LISTBOX);
		Frozen frozen = new Frozen();
		frozen.setColumns(3);
		frozen.setStart(1);
		frozen.setParent(listresult);
		Listhead listhead = new Listhead();
		listhead.setParent(listresult);
		listhead.setSizable(true);
		Listheader header = new Listheader("");
		header.setWidth("60px");
		header.setParent(listhead);
		header = new Listheader("Số Tài");
		header.setWidth("100px");
		header.setParent(listhead);
		header = new Listheader("Biển kiểm soát");
		header.setWidth("150px");
		header.setParent(listhead);
		header = new Listheader("Giờ đón");
		header.setWidth("200px");
		header.setParent(listhead);
		header = new Listheader("Giờ trả");
		header.setWidth("200px");
		header.setParent(listhead);
		header = new Listheader("Điểm đón");
		header.setWidth("200px");
		header.setParent(listhead);
		header = new Listheader("Điểm trả");
		header.setWidth("200px");
		header.setParent(listhead);
		header = new Listheader("Tiền");
		header.setWidth("200px");
		header.setParent(listhead);
		header = new Listheader("Đi qua");
		header.setWidth("300px");
		header.setParent(listhead);
		header = new Listheader("Hành trình");
		header.setWidth("87px");
		header.setParent(listhead);
		listresult.setItemRenderer(new ListitemRenderer<FindingResult>() {
			@Override
			public void render(Listitem item, FindingResult data, int index) throws Exception {
				item.setValue(data);
				new Listcell().setParent(item);
				new Listcell(data.getVehicleNumber()).setParent(item);
				new Listcell(data.getLicensePlate()).setParent(item);
				new Listcell(StringUtils.valueOfTimestamp(data.getBeginTime(), "dd/MM/yyyy HH:mm:ss")).setParent(item);
				new Listcell(StringUtils.valueOfTimestamp(data.getStopTime(), "dd/MM/yyyy HH:mm:ss")).setParent(item);
				new Listcell(data.getBeginPosition()).setParent(item);
				new Listcell(data.getStopPosition()).setParent(item);
				new Listcell(StringUtils.priceWithoutDecimal(data.getMoney())).setParent(item);
				new Listcell(data.getFullPath()).setParent(item);
				Toolbarbutton history = new Toolbarbutton("Lịch sử");
				Listcell listcell = new Listcell();
				listcell.setParent(item);
				listcell.appendChild(history);
				history.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						openHistory(data);
					}
				});
			}
		});
		creatSouthLayout(borderCenter);
	}

	private void openHistory(FindingResult data) {
		Window window = new Window();
		window.setStyle("height:100%;width:100%");
		window.setTitle("Lịch sử hành trình");
		window.setClosable(true);
		window.setParent(this);
		Date datefrom = new Date(data.getBeginTime().getTime());
		Date dateto = new Date(data.getStopTime().getTime());
		TrackingHistory his = new TrackingHistory(datefrom, dateto, data.getVehicleID());
		his.setParent(window);
		window.doModal();
	}

	private void creatWestLayout(Borderlayout borderlayout) {
		West west = new West();
		west.setTitle("Thông tin tìm kiếm");
		west.setSize("25%");
		west.setVflex("True");
		west.setCollapsible(true);
		west.setSplittable(true);
		west.setParent(borderlayout);
		Vlayout vl = new Vlayout();
		vl.setHflex("true");
		vl.setParent(west);
		Grid grid = new Grid();
		grid.setParent(vl);
		grid.setHeight("536px");
		Columns cols = new Columns();
		cols.setParent(grid);
		Column col = new Column();
		col.setWidth("25%");
		col.setParent(cols);
		col = new Column();
		col.setWidth("75%");
		col.setParent(cols);
		Rows rows = new Rows();
		rows.setParent(grid);
		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Khách hàng"));
		khachhang = new Label();
		row.appendChild(khachhang);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Số điện thoại"));
		sodienthoai = new Label();
		row.appendChild(sodienthoai);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Địa chỉ"));
		diachi = new Label();
		row.appendChild(diachi);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Nội dung"));
		noidung = new Label();
		row.appendChild(noidung);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Giờ đón"));
		giodon = new Label();
		row.appendChild(giodon);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Giờ trả"));
		giotra = new Label();
		row.appendChild(giotra);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Điểm đón"));
		diemdon = new Label();
		row.appendChild(diemdon);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Điểm trả"));
		diemtra = new Label();
		row.appendChild(diemtra);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Điểm qua 1"));
		diemqua1 = new Label();
		row.appendChild(diemqua1);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Điểm qua 2"));
		diemqua2 = new Label();
		row.appendChild(diemqua2);
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tiền cuốc"));
		tiencuoc = new Label();
		row.appendChild(tiencuoc);
		Toolbar toolbar = new Toolbar();
		toolbar.setAlign("center");
		toolbar.setParent(vl);
		btnTim = new Button();
		btnTim.setLabel("Tìm đồ");
		btnTim.setStyle("Width:100px;Height:34px;margin-right:20px;font-weight: bold;");
		// btnTim.setSclass("btn-default btn z-btn-history");
		btnTim.setParent(toolbar);
		btnTim.addEventListener(Events.ON_CLICK, LISTENER_TIM);
		btnTim.setImage("./themes/images/Search Computer_16.png");
		btnTim.setFocus(false);
		btnHuyTimKiem = new Button();
		btnHuyTimKiem.setLabel("Bỏ qua");
		btnHuyTimKiem.setStyle("Width:100px;Height:34px;margin-right:20px;font-weight: bold;");
		btnHuyTimKiem.setImage("/themes/images/close_16.png");
		btnHuyTimKiem.setParent(toolbar);
		btnHuyTimKiem.addEventListener(Events.ON_CLICK, LISTENER_CANCEL);

	}

	private EventListener<Event> LISTENER_TIM = new EventListener<Event>() {
		@Override
		public void onEvent(Event arg0) throws Exception {
			listresult.setModel(new ListModelList<>(finding()));
			listresult.setMultiple(true);
		}
	};

	private List<FindingResult> finding() {
		List<FindingResult> lstresult = new ArrayList<>();
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		java.sql.CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sessionImplementor.connection();
			if (conn == null) {
				Env.getHomePage().showNotification("Không thể kết nối lên Server!", Clients.NOTIFICATION_TYPE_ERROR);
				return null;
			}
			cs = conn.prepareCall("call txm_tracking.cmdGetFindingResult(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			cs.setObject(1, model.getAgent().getId());
			cs.setObject(2, model.getBeginTime());
			cs.setObject(3, model.getFinishTime());
			cs.setObject(4, model.getLicensePlate());
			cs.setObject(5, model.getVehicleNumber());
			cs.setObject(6, model.getSeatType());
			cs.setObject(7, model.getVehicleType());
			cs.setObject(8, model.getDriverName());
			cs.setObject(9, model.getPaymentType());
			cs.setObject(10, model.getTripType());
			cs.setObject(11, model.getCardNumber());
			cs.setObject(12, model.getMoney());
			cs.setObject(13, model.getLatlngbegin().lat);
			cs.setObject(14, model.getLatlngbegin().lng);
			cs.setObject(15, model.getLatlngstop().lat);
			cs.setObject(16, model.getLatlngstop().lng);
			cs.setObject(17, model.getBeginAddr());
			cs.setObject(18, model.getFinishAddr());
			cs.setObject(19, "");
			cs.setObject(20, "");
			rs = cs.executeQuery();
			while (rs.next()) {
				FindingResult result = new FindingResult();
				result.setTripID(rs.getInt(1));
				result.setVehicleID(rs.getInt(2));
				result.setVehicleNumber(rs.getString(5));
				result.setLicensePlate(rs.getString(6));
				result.setBeginTime(rs.getTimestamp(8));
				result.setStopTime(rs.getTimestamp(9));
				result.setMoney(rs.getFloat(10));
				result.setBeginPosition(rs.getString(11));
				result.setStopPosition(rs.getString(12));
				result.setFullPath(rs.getString(13));
				lstresult.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}

		return lstresult;
	}

	private EventListener<Event> LISTENER_CANCEL = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			closeForm();
		}

	};
	@SuppressWarnings("unchecked")
	private EventListener<Event> LISTENER_SELECT_LISTBOX = new EventListener<Event>() {
		@Override
		public void onEvent(Event event) throws Exception {
			SelectEvent<Listitem, Set<Listitem>> selectEvent = (SelectEvent<Listitem, Set<Listitem>>) event;
			listProcess.setModel(new ListModelList<>(selectEvent.getSelectedObjects()));
		}

	};
	private EventListener<Event> LISTENER_XU_LY = new EventListener<Event>() {
		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(Event event) throws Exception {
			String feedback = "";
			List<FindingResult> lstRS = (List<FindingResult>) listProcess.getModel();
			for (FindingResult item : lstRS) {
				feedback = feedback + "," + item.getTripID();
			}
			if (feedback != "") {

				handerUpdateFeedBackResult(model.getId(), StringUtils.Trim(feedback, ","));
			}

		}
	};

	private void handerUpdateFeedBackResult(int reqestid, String feedback) {
		List<Object> parameters = new ArrayList<>();
		String msg = validateFeedback();
		if (msg.equals("")) {
			RequestType type = cmbStatusProcess.getSelectedItem().getValue();
			parameters.add(type.getId());
			parameters.add(txtConten.getValue());
			parameters.add(model.getId());
			parameters.add(Env.getUserID());
			parameters.add(feedback);
			String cmdcommand = "Call txm_tracking.cmdUpdateFeedbackResult(?,?,?,?,?)";
			try {
				int resultupdate = DatabaseUtils.executeUpdate(cmdcommand, parameters);
				if (resultupdate > 0) {
					Env.getHomePage().showNotification("Xử lý thành công !", Clients.NOTIFICATION_TYPE_INFO);
					listWindow.refresh();
					this.detach();
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

	private String validateFeedback() {
		StringBuilder msg = new StringBuilder("");
		if (cmbStatusProcess.getSelectedItem() == null) {
			msg.append("Bạn chưa chọn trạng thái xử lý!");
		}
		return msg.toString();
	}

	private void closeForm() {
		this.detach();
	}

	public Button getBtnTim() {
		return btnTim;
	}

	public void setBtnTim(Button btnTim) {
		this.btnTim = btnTim;
	}

	public Button getBtnHuyTimKiem() {
		return btnHuyTimKiem;
	}

	public void setBtnHuyTimKiem(Button btnHuyTimKiem) {
		this.btnHuyTimKiem = btnHuyTimKiem;
	}

	public RequestBaggageFinding getModel() {
		return model;
	}

	public void setModel(RequestBaggageFinding model) {
		this.model = model;
		initValue();
	}

	public Label getKhachhang() {
		return khachhang;
	}

	public void setKhachhang(Label khachhang) {
		this.khachhang = khachhang;
	}

	public Label getSodienthoai() {
		return sodienthoai;
	}

	public void setSodienthoai(Label sodienthoai) {
		this.sodienthoai = sodienthoai;
	}

	public Label getDiachi() {
		return diachi;
	}

	public void setDiachi(Label diachi) {
		this.diachi = diachi;
	}

	public Label getNoidung() {
		return noidung;
	}

	public void setNoidung(Label noidung) {
		this.noidung = noidung;
	}

	public Label getGiodon() {
		return giodon;
	}

	public void setGiodon(Label giodon) {
		this.giodon = giodon;
	}

	public Label getGiotra() {
		return giotra;
	}

	public void setGiotra(Label giotra) {
		this.giotra = giotra;
	}

	public Label getDiemdon() {
		return diemdon;
	}

	public void setDiemdon(Label diemdon) {
		this.diemdon = diemdon;
	}

	public Label getDiemtra() {
		return diemtra;
	}

	public void setDiemtra(Label diemtra) {
		this.diemtra = diemtra;
	}

	public Label getDiemqua1() {
		return diemqua1;
	}

	public void setDiemqua1(Label diemqua1) {
		this.diemqua1 = diemqua1;
	}

	public Label getDiemqua2() {
		return diemqua2;
	}

	public void setDiemqua2(Label diemqua2) {
		this.diemqua2 = diemqua2;
	}

	public Label getTiencuoc() {
		return tiencuoc;
	}

	public void setTiencuoc(Label tiencuoc) {
		this.tiencuoc = tiencuoc;
	}

}
