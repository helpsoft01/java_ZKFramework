package com.vietek.taxioperation.ui.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;

import com.vietek.taxioperation.controller.TablePriceController;
import com.vietek.taxioperation.model.TablePrice;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.ui.util.ListItemRenderer;
import com.vietek.taxioperation.util.ControllerUtils;

public class TablePrices extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TablePriceDetail detailWindow = null;

	public TablePrices() {
		super(true);
		this.setTitle("Danh mục bảng giá");

	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {

		ArrayList<GridColumn> lstcolus = new ArrayList<GridColumn>();
		lstcolus.add(new GridColumn("Km", 150, double.class, "getKm", "km", getModelClass()));
		lstcolus.add(new GridColumn("1 chiều", 80, double.class, "getTime1c"));
		lstcolus.add(new GridColumn("2 chiều", 80, double.class, "getTime2c"));
		lstcolus.add(new GridColumn("1 chiều", 120, double.class, "getPrice1c"));
		lstcolus.add(new GridColumn("2 chiều", 120, double.class, "getPrice2c"));
		lstcolus.add(new GridColumn("Tên bảng giá", 250, TypeTbPrices.class, "getTypeTablePrice"));
		lstcolus.add(new GridColumn("Từ ngày", 120, Timestamp.class, "getFromDate"));
		lstcolus.add(new GridColumn("Đến ngày", 120, Timestamp.class, "getToDate"));
		this.setGridColumns(lstcolus);

	}

	@Override
	public void loadData() {
		TablePriceController tablepricecontroller = (TablePriceController) ControllerUtils
				.getController(TablePriceController.class);
		List<TablePrice> lstValue = tablepricecontroller.find("from TablePrice");
		setLstModel(lstValue);
		setModelClass(TablePrice.class);

	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new TablePriceDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

	// @Override
	// public void initDetailRightPanel() {
	// int i = 0;
	// initTopRight();
	// initColumns();
	// if (i == 0) {
	//// setupcols();
	// renderGrid();
	// } else {
	// }
	//
	// }

	@Override
	public void setupColumns() {

		listbox = new Listbox();
		listbox.addEventListener(Events.ON_SELECT, this);
		listbox.addEventListener(Events.ON_OK, this);
		listbox.addEventListener(Events.ON_DOUBLE_CLICK, this);
		listbox.setParent(this.getVlayout());
		listbox.setCheckmark(true);
		listbox.setNonselectableTags("");
		listbox.setMold("paging");
		listbox.setAutopaging(true);
		// listbox.setPageSize(10);
		listbox.setVflex(true);
		listbox.setPagingPosition("both");
		Events.echoEvent("focus", listbox, null);
		Auxhead lstauxhead = new Auxhead();
		lstauxhead.setParent(listbox);
		Auxheader aux = new Auxheader("QUÃNG ĐƯỜNG (Km)");
		aux.setStyle("text-align:center;");
		aux.setColspan(1);
		aux.setParent(lstauxhead);
		aux = new Auxheader("THỜI GIAN (Phút)");
		aux.setStyle("text-align:center;");
		aux.setColspan(2);
		aux.setParent(lstauxhead);
		aux = new Auxheader("GIÁ (VNĐ)");
		aux.setStyle("text-align:center;");
		aux.setColspan(2);
		aux.setParent(lstauxhead);
		aux = new Auxheader("TÊN BẢNG GIÁ");
		aux.setStyle("text-align:center;");
		aux.setColspan(1);
		aux.setParent(lstauxhead);
		aux = new Auxheader("THỜI GIAN ÁP DỤNG");
		aux.setStyle("text-align:center;");
		aux.setColspan(2);
		aux.setParent(lstauxhead);

		Listhead head = new Listhead();
		head.setParent(listbox);
		head.setMenupopup("auto");
		// setup columns
		Listheader header = null;
		// header = new Listheader();
		// header.setHflex("min");
		// header.setParent(head);
		for (int i = 0; i < this.getGridColumns().size(); i++) {
			GridColumn gridCol = this.getGridColumns().get(i);
			header = new Listheader(gridCol.getHeader());

			header.setStyle("text-align:center;");
			if (i == this.getGridColumns().size() - 1) {
				header.setHflex("1");
			} else
				header.setWidth(gridCol.getWidth() + "px");
			header.setParent(head);
		}

		ListItemRenderer<Class<?>> renderer = new ListItemRenderer<Class<?>>(this.getGridColumns());
		listbox.setItemRenderer(renderer);
	}
}