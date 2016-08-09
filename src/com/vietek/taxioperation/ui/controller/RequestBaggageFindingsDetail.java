package com.vietek.taxioperation.ui.controller;

import java.sql.Timestamp;

import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.RequestBaggageFinding;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class RequestBaggageFindingsDetail extends BasicDetailWindow {

	public RequestBaggageFindingsDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setStyle("width:100%;height:100%");
		this.setTitle("Thông tin tìm đồ");
	}

	private Radio rdbthanhtoanthe;
	private Radio rdbthanhtoantructiep;
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
		col.setWidth("10%");
		col.setParent(cols);
		col = new Column();
		col.setWidth("18%");
		col.setParent(cols);
		col = new Column();
		col.setWidth("18%");
		col.setParent(cols);
		col = new Column();
		col.setWidth("18%");
		col.setParent(cols);
		col = new Column();
		col.setWidth("18%");
		col.setParent(cols);
		col = new Column();
		col.setWidth("18%");
		col.setParent(cols);
		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setSclass("z-row-finding");
		row.setParent(rows);
		Label lb = new Label("Mã Y/c");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		Editor editor = this.getMapEditor().get("Requesttype");
		row.appendChild(editor.getComponent());
		lb = new Label("Khu vực tìm kiếm");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		editor = this.getMapEditor().get("agent");
		row.appendChild(editor.getComponent());
		lb = new Label("Tên khách hàng");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		editor = this.getMapEditor().get("CustomerName");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		lb = new Label("Địa chỉ");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		Cell cell = new Cell();
		cell.setColspan(3);
		cell.setParent(row);
		editor = this.getMapEditor().get("CustomerAddr");
		cell.appendChild(editor.getComponent());
		lb = new Label("Điện thoại");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		editor = this.getMapEditor().get("PhoneNumber");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		lb = new Label("Nội dung");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(5);
		editor = this.getMapEditor().get("RequestContent");
		((Textbox) editor.getComponent()).setMultiline(true);
		((Textbox) editor.getComponent()).setHeight("80px");
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		lb = new Label("Hình thức");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		Radiogroup rdgroup = new Radiogroup();
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);
		cell.setStyle("Margin-right:20px");
		lb = new Label("Thanh toán thẻ");
		lb.setStyle(" margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		cell.appendChild(lb);
		rdbthanhtoanthe = new Radio();
		rdbthanhtoanthe.setStyle("margin-right:5px");
		rdbthanhtoanthe.setRadiogroup(rdgroup);
		cell.appendChild(rdbthanhtoanthe);
		lb = new Label("Số thẻ");
		lb.setStyle(" margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		cell.appendChild(lb);
		editor = this.getMapEditor().get("CardNumber");
		// ((Textbox)editor.getComponent()).setWidth("130px");
		cell.appendChild(editor.getComponent());
		rdgroup.setParent(row);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		lb = new Label("TT trực tiếp");
		lb.setStyle(" margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		cell.appendChild(lb);
		rdbthanhtoantructiep = new Radio();
		rdbthanhtoantructiep.setStyle("margin-right:5px");
		rdbthanhtoantructiep.setRadiogroup(rdgroup);
		cell.appendChild(rdbthanhtoantructiep);
		editor = this.getMapEditor().get("");
		lb = new Label("Số tiền");
		lb.setStyle("margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		cell.appendChild(lb);
		editor = this.getMapEditor().get("Money");
//		 ((Textbox)editor.getComponent()).setPlaceholder("");
		cell.appendChild(editor.getComponent());
		cell.appendChild(new Label(".000 VND"));

		row = new Row();
		row.setParent(rows);
		lb = new Label("Giờ Đón-Trả");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		cell = new Cell();
		cell.setColspan(2);
		cell.setParent(row);
		lb = new Label("Giờ đón:");
		lb.setStyle(" margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		cell.appendChild(lb);
		editor = this.getMapEditor().get("BeginTime");
		cell.appendChild(editor.getComponent());
		row.appendChild(new Cell());
		cell = new Cell();
		cell.setColspan(2);
		cell.setParent(row);
		lb = new Label("Giờ trả:");
		lb.setStyle(" margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		cell.appendChild(lb);
		editor = this.getMapEditor().get("FinishTime");
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		lb = new Label("Điểm đón:");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		cell = new Cell();
		cell.setColspan(5);
		cell.setParent(row);
		editor = this.getMapEditor().get("BeginAddr");
		((Textbox)editor.getComponent()).setPlaceholder("Chỉ nhập tên đường (nếu khách hàng cung cấp )");
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		lb = new Label("Điểm trả:");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		cell = new Cell();
		cell.setColspan(5);
		cell.setParent(row);
		editor = this.getMapEditor().get("FinishAddr");
		((Textbox)editor.getComponent()).setPlaceholder("Chỉ nhập tên đường (nếu khách hàng cung cấp )");
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		lb = new Label("Điểm qua 1:");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		cell = new Cell();
		cell.setColspan(5);
		cell.setParent(row);
		editor = this.getMapEditor().get("Address1");
		((Textbox)editor.getComponent()).setPlaceholder("Chỉ nhập tên đường (nếu khách hàng cung cấp )");
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		lb = new Label("Điểm qua 2:");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		cell = new Cell();
		cell.setColspan(5);
		cell.setParent(row);
		editor = this.getMapEditor().get("Address2");
		((Textbox)editor.getComponent()).setPlaceholder("Chỉ nhập tên đường (nếu khách hàng cung cấp )");
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		lb = new Label("Loại xe:");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		cell = new Cell();
		cell.setColspan(5);
		editor = this.getMapEditor().get("SeatType");
		cell.appendChild(editor.getComponent());
		cell.setParent(row);
		
		row = new Row();
		row.setParent(rows);
		lb = new Label("Tên tài xế:");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		editor = this.getMapEditor().get("DriverName");
		row.appendChild(editor.getComponent());
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);
		lb = new Label("Số tài:");
		lb.setStyle("margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		cell.appendChild(lb);
		editor = this.getMapEditor().get("VehicleNumber");
		cell.appendChild(editor.getComponent());
		lb = new Label("BKS:");
		lb.setStyle("margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		cell.appendChild(lb);
		editor = this.getMapEditor().get("LicensePlate");
		cell.appendChild(editor.getComponent());
		
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);
		lb = new Label("Vị trí ngồi:");
		lb.setStyle("margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		cell.appendChild(lb);
		editor = this.getMapEditor().get("CusPosition");
		cell.appendChild(editor.getComponent());
		lb = new Label("Số người:");
		lb.setStyle("margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		cell.appendChild(lb);
		editor = this.getMapEditor().get("CusNumber");
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		lb = new Label("Mô tả khác:");
		lb.setStyle("float:right; margin-right:10px;font-size: 14px;color: green;font-weight: bold;");
		row.appendChild(lb);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(5);
		editor = this.getMapEditor().get("OtherDecription");
		((Textbox)editor.getComponent()).setMultiline(true);
		((Textbox)editor.getComponent()).setHeight("100px");
		cell.appendChild(editor.getComponent());
	}
	@Override
	public void handleSaveEvent() {
	   ((RequestBaggageFinding)this.getModel()).setReceiveDate(new Timestamp(System.currentTimeMillis()));
		super.handleSaveEvent();
	}
}
