package com.vietek.taxioperation.ui.controller;

import java.util.Date;

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
import org.zkoss.zul.Vbox;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.Device;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.editor.IntCheckEditor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class DeviceDetail extends BasicDetailWindow {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;

	public DeviceDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết thiết bị");
		this.setWidth("900px");
	}

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
		row.appendChild(new Label("Imei"));
		Editor editor = this.getMapEditor().get("Imei");

		Cell cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Số sim"));
		editor = this.getMapEditor().get("Sim");

		cell = new Cell();
		cell.setParent(row);

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Chi nhánh"));
		editor = this.getMapEditor().get("agent");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
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
		hbox.setParent(groupbox);
		// ---------------------------------------------------
		Vbox vbox = new Vbox();
		vbox.setSclass("z-vbox-device");
		vbox.setParent(hbox);

		Hbox hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		Label lb = new Label("Lamp:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("Lamp");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Aclock:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("ACLock");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Hiện lỗi:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("DisplayError");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("PulseUp:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("PulseUp");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		// ---------------------------------------------------------------
		vbox = new Vbox();
		vbox.setSclass("z-vbox-device");
		vbox.setParent(hbox);
		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Cho phép In  cuốc:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("AllowPrintTrip");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Cho phép In lại:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("Reprint");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Loại Taxi:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("TaxiType");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("PulseDown:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("PulseDown");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		// -----------------------------------------------------------
		vbox = new Vbox();
		hb.setSclass("z-hbox-device");
		vbox.setSclass("z-vbox-device");
		vbox.setParent(hbox);
		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Cho phép gửi SMS:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("AllowSendSms");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Gửi cuốc lên FTP:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("SendBackupToFTP");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Gửi lệnh:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("FirmwareRequest");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		// -----------------------------------------------------------------
		vbox = new Vbox();
		vbox.setSclass("z-vbox-device");
		vbox.setParent(hbox);

		vbox = new Vbox();
		hb.setSclass("z-hbox-device");
		vbox.setSclass("z-vbox-device");
		vbox.setParent(hbox);
		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("In biên lai thu gọn:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("AlowTinyReceipt");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Login:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("Login");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Đã tạo Config? :");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("CreatedFileConfig");
		((IntCheckEditor) editor).setSclass("z-checkbox-device");
		hb.appendChild(editor.getComponent());

		// -----------------------------------------------------
		hbox = new Hbox();
		hbox.setParent(groupbox);
		vbox = new Vbox();
		vbox.setParent(hbox);
		vbox.setStyle("Width:270px");
		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("Pulse:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("Pulse");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("NX:Vgps Bắt đầu:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("SpeedGpsStartInCut");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("NX:Vgps Kết thúc:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("SpeedGpsStopInCut");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("NX: Thời gian Xác nhận");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("TimerInCut");
		hb.appendChild(editor.getComponent());

		vbox = new Vbox();
		vbox.setParent(hbox);
		vbox.setStyle("Width:270px");

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("NX:Sai số (km/h):");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("DeltaSpeedInCut");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("KX:Sai số Bắt đầu(km/h):");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("DeltaSpeedStartInKick");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("KX:Sai số Kết thúc(km/h)");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("DeltaSpeedStopInKick");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("KX:T/g Xác nhận Bắt đầu:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("TimeStartInKick");
		hb.appendChild(editor.getComponent());

		vbox = new Vbox();
		vbox.setParent(hbox);
		vbox.setStyle("Width:270px");

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("KX:T/g Xác nhận Kết thúc");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("TimeStopInKick");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("KX:Vcơ Kết thúc):");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("SpeedMeterStopInKick");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("KX:Vgps Bắt đầu:");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("SpeedGpsStartInKick");
		hb.appendChild(editor.getComponent());

		hb = new Hbox();
		hb.setSclass("z-hbox-device");
		hb.setParent(vbox);
		lb = new Label("KX:Số lần Xác nhận):");
		lb.setSclass("z-lable-Device");
		hb.appendChild(lb);
		editor = getMapEditor().get("RepeatInKick");
		hb.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kich hoat"));
		editor = this.getMapEditor().get("isActive");
		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES));
		cell.setStyle("color:red;text-align:center;");
	}
//	@Override
//	public void createMapEditor() {
//		super.createMapEditor();
//		Editor editor = EditorFactory.getMany2OneEditor(this.getModel(), "Sim");
//		editor.setValueChangeListener(this);
//		this.getMapEditor().put("Sim", editor);
//		editor = EditorFactory.getMany2OneEditor(this.getModel(), "agent");
//		editor.setValueChangeListener(this);
//		this.getMapEditor().put("agent", editor);
//
//	}


	@Override
	public void handleSaveEvent() {
		if (((Device) getModel()).getId() == 0) {
			((Device) getModel()).setInsertDate(new Date(System.currentTimeMillis()));
		}
		super.handleSaveEvent();
	}

}