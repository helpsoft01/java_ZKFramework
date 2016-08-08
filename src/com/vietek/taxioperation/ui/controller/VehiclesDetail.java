package com.vietek.taxioperation.ui.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.zkoss.zhtml.Div;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.controller.BasicController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.editor.DateTimeEditor;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.EditorFactory;
import com.vietek.taxioperation.ui.editor.IntCheckEditor;
import com.vietek.taxioperation.ui.editor.IntSpinnerEditor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class VehiclesDetail extends BasicDetailWindow {

	public VehiclesDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		setTitle("Chi tiết phương tiện");
		setWidth("800px");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("30%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("5%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("65%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Số tài"));
		Cell cell = new Cell();
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		cell.setParent(row);
		Editor editor = this.getMapEditor().get("value");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Biển kiểm soát"));
		cell = new Cell();
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		cell.setParent(row);
		editor = this.getMapEditor().get("taxiNumber");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Thiết bị"));
		cell = new Cell();
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		cell.setParent(row);
		editor = this.getMapEditor().get("device");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Số VIN"));
		row.appendChild(new Label(""));
		editor = this.getMapEditor().get("VinNumber");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Loại xe"));
		cell = new Cell();
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		cell.setParent(row);
		editor = this.getMapEditor().get("taxiType");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Đội xe"));
		cell = new Cell();
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		cell.setParent(row);
		editor = this.getMapEditor().get("taxiGroup");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Bãi giao ca:"));
		row.appendChild(new Label());
		editor = this.getMapEditor().get("Parking");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Loại hình kinh doanh:"));
		cell = new Cell();
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");
		cell.setParent(row);
		editor = this.getMapEditor().get("BusinessType");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Xung/km:"));
		row.appendChild(new Label());
		editor = this.getMapEditor().get("PulsePerKm");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setColspan(3);
		cell.setParent(row);
		Groupbox groupbox = new Groupbox();
		groupbox.setWidth("880px");
		groupbox.setParent(cell);
		groupbox.setMold("3d");
		groupbox.setSclass("z-groupbox z-groupbox-3d z-groupbox-FrmDevice");
		groupbox.setOpen(false);
		Caption caption = new Caption("Các thông số mở rộng");
		caption.setParent(groupbox);
		caption.setZclass("none");
		caption.setStyle("font-size: 15px;cursor: pointer; margin-top: 5px;");
		Hbox hbox = new Hbox();
		hbox.setSclass("z-hbox-device");
		hbox.setParent(groupbox);
		Div div = new Div();
		div.setSclass("z-list-template");
		div.setParent(hbox);
		Label lb = new Label("Đã đăng ký  ?");
		lb.setSclass("z-item-Label-list-template");
		div.appendChild(lb);
		editor = getMapEditor().get("NeedRegister");
		((IntCheckEditor) editor).setSclass("z-item-input-list-template");
		div.appendChild(editor.getComponent());
		div = new Div();
		div.setParent(hbox);
		div.setSclass("z-list-template");
		lb = new Label("Đ/k thông tin ?");
		lb.setSclass("z-item-Label-list-template");
		div.appendChild(lb);
		editor = getMapEditor().get("RegisterOffice");
		((IntCheckEditor) editor).setSclass("z-item-input-list-template");
		div.appendChild(editor.getComponent());

		hbox = new Hbox();
		hbox.setSclass("z-hbox-device");
		hbox.setParent(groupbox);
		div = new Div();
		div.setSclass("z-list-template");
		div.setParent(hbox);
		lb = new Label("Tự động chốt ca:");
		lb.setSclass("z-item-Label-list-template");
		div.appendChild(lb);
		editor = getMapEditor().get("AutoFinishShift");
		((IntCheckEditor) editor).setSclass("z-spinner-input-Editor z-item-input-list-template");
		div.appendChild(editor.getComponent());

		div = new Div();
		div.setSclass("z-list-template");
		div.setParent(hbox);
		lb = new Label("Thời gian chốt ca:");
		lb.setSclass("z-item-Label-list-template");
		div.appendChild(lb);
		editor = getMapEditor().get("FinishShiftTiming");
		((IntSpinnerEditor) editor).setSclass("z-spinner-input-Editor z-item-input-list-template");
		div.appendChild(editor.getComponent());

		hbox = new Hbox();
		hbox.setSclass("z-hbox-device");
		hbox.setParent(groupbox);
		div = new Div();
		div.setSclass("z-list-template");
		div.setParent(hbox);
		lb = new Label("km rỗng tối đa:");
		lb.setSclass("z-item-Label-list-template");
		div.appendChild(lb);
		editor = getMapEditor().get("EmptyDrivingLimit");
		((IntSpinnerEditor) editor).setSclass("z-spinner-input-Editor z-item-input-list-template");
		div.appendChild(editor.getComponent());

		div = new Div();
		div.setSclass("z-list-template");
		div.setParent(hbox);
		lb = new Label("Tốc độ tối đa:");
		lb.setSclass("z-item-Label-list-template");
		div.appendChild(lb);
		editor = getMapEditor().get("SpeedLimit");
		((IntSpinnerEditor) editor).setSclass("z-spinner-input-Editor z-item-input-list-template");
		div.appendChild(editor.getComponent());

		hbox = new Hbox();
		hbox.setSclass("z-hbox-device");
		hbox.setParent(groupbox);
		div = new Div();
		div.setSclass("z-list-template");
		div.setParent(hbox);
		lb = new Label("T/g dừng đỗ tối đa:");
		lb.setSclass("z-item-Label-list-template");
		div.appendChild(lb);
		editor = getMapEditor().get("StopTimeLimit");
		((IntSpinnerEditor) editor).setSclass("z-spinner-input-Editor z-item-input-list-template");
		div.appendChild(editor.getComponent());

		div = new Div();
		div.setSclass("z-list-template");
		div.setParent(hbox);
		lb = new Label("Ngày đăng ký:");
		lb.setSclass("z-item-Label-list-template");
		div.appendChild(lb);
		editor = getMapEditor().get("InsertDate");
		((DateTimeEditor) editor).setSclass("z-item-input-datetime-template");
		div.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Active:"));
		row.appendChild(new Label(""));
		editor = this.getMapEditor().get("isActive");
		row.appendChild(editor.getComponent());
	}

	@Override
	public void createMapEditor() {
		super.createMapEditor();
		Editor editor = EditorFactory.getMany2OneEditor(this.getModel(), "Parking");
		editor.setValueChangeListener(this);
		this.getMapEditor().put("Parking", editor);
		editor = EditorFactory.getMany2OneEditor(this.getModel(), "device");
		editor.setValueChangeListener(this);
		this.getMapEditor().put("device", editor);
		editor = EditorFactory.getMany2OneEditor(this.getModel(), "taxiGroup");
		editor.setValueChangeListener(this);
		this.getMapEditor().put("taxiGroup", editor);
		editor = EditorFactory.getMany2OneEditor(this.getModel(), "taxiType");
		editor.setValueChangeListener(this);
		this.getMapEditor().put("taxiType", editor);

	}

	@Override
	public boolean checkExitsValue(AbstractModel model, Object value, String field) {
		Boolean result = false;
		BasicController<?> controller = model.getControler();
		String query = "from " + model.getClass().getName().replace("com.vietek.taxioperation.model.", "") + " "
				+ "where " + field + "= ?";
		List<?> lstmp = null;
		if (field.equals("value")) {
			query = query + " and VehicleGroupID = ?";
			lstmp = controller.find(query, new Object[] { value, ((Vehicle) model).getVehicleGroupID() });
		} else {
			lstmp = controller.find(query, value);
		}
		if (lstmp != null && lstmp.size() > 0) {
			if (model.getId() == 0) {
				result = true;
			} else if (model.getId() > 0 && model.getId() != ((AbstractModel) lstmp.get(0)).getId()) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public void handleSaveEvent() {
		Session session = ControllerUtils.getCurrentSession();
		SessionImplementor sessionImplementor = (SessionImplementor) session;
		Connection conn = null;
		java.sql.CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = sessionImplementor.connection();
			if (conn == null) {
				Env.getHomePage().showNotification("Không thể kết nối lên Server!", Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
			String storeName = "";
			Vehicle modeltmp = (Vehicle) model;
			if (modeltmp.getId() > 0) {
				storeName = "call txm_tracking.lst_vehicle_Update(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				storeName = "call txm_tracking.lst_vehicle_Insert(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			}
			cs = conn.prepareCall(storeName);
			cs.setInt(1, modeltmp.getId());
			cs.setString(2, modeltmp.getValue());
			cs.setString(3, modeltmp.getTaxiNumber());
			cs.setString(4, modeltmp.getVinNumber());
			cs.setInt(5, modeltmp.getDeviceID());
			cs.setInt(6, modeltmp.getTaxiType().getId());
			cs.setInt(7, modeltmp.getTaxiGroup().getId());
			if (modeltmp.getParking() != null) {
				cs.setInt(8, modeltmp.getParking().getId());
			} else {
				cs.setObject(8, null);
			}
			cs.setObject(9, null);
			cs.setObject(10, modeltmp.getBusinessType());
			cs.setObject(11, modeltmp.getSpeedLimit());
			cs.setObject(12, modeltmp.getStopTimeLimit());
			cs.setObject(13, modeltmp.getEmptyDrivingLimit());
			// cs.setInt(14, modeltmp.getAutoFinishShift());
			cs.setObject(14, modeltmp.getAutoFinishShift());
			cs.setObject(15, modeltmp.getFinishShiftTiming());
			cs.setObject(16, modeltmp.getPulsePerKm());
			cs.setBoolean(17, modeltmp.getIsActive());
			cs.setTimestamp(18, modeltmp.getInsertDate());
			cs.setObject(19, modeltmp.getNeedRegister());
			cs.setObject(20, modeltmp.getRegisterOffice());
			cs.setObject(21, null);
			cs.setObject(22, null);
			cs.setString(23, modeltmp.getComment());

			int result = cs.executeUpdate();
			if (result > 0) {
				this.setVisible(false);
				this.getListWindow().refresh();
				Env.getHomePage().showNotification("Cập nhật thành công!", Clients.NOTIFICATION_TYPE_INFO);
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

	}
}