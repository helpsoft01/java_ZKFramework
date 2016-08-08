package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;

import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;

import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

public class Customers extends AbstractWindowPanel {
	private CustomersDetail customersDetail = null;
	private Editor editor;
	private Combobox cbb;

	public Customers() {
		super(true);
		setTitle("Danh sách khách hàng");
		setDetailTitle("Chi tiết khách hàng");

	}

	private static final long serialVersionUID = -2423992205373693064L;

	@Override
	public void initLeftPanel() {
		initColumns();
	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> columns = new ArrayList<GridColumn>();
		columns.add(new GridColumn("Số điện thoại", 150, String.class, "getPhoneNumber","phoneNumber",getModelClass()));
		columns.add(new GridColumn("Khách hàng", 150, String.class, "getName","name",getModelClass()));
		columns.add(new GridColumn("Địa chỉ 1", 300, String.class, "getAddress"));
		columns.add(new GridColumn("Địa chỉ 2", 300, String.class, "getAddress2"));
		columns.add(new GridColumn("Địa chỉ 3", 300, String.class, "getAddress3"));
		columns.add(new GridColumn("Lần gọi cuối", 100, String.class, "getLastTimeCall"));
		columns.add(new GridColumn("Lần thành công", 100, String.class, "getTotalSuccessOrder"));
		columns.add(new GridColumn("Tổng lần gọi", 100, Integer.class, "getTotalCallOrder"));
		columns.add(new GridColumn("Ghi chú", 200, String.class, "getNote"));
		setGridColumns(columns);
	}

	@Override
	public void loadData() {
		this.setStrQuery("from Customer");
		this.setStrOrderBy(" order by phoneNumber");
		setModelClass(Customer.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (customersDetail == null)
			customersDetail = new CustomersDetail(getCurrentModel(), this);
		return customersDetail;
	}

	@Listen("onClick=#btnAdd")
	public void searchAdvance(MouseEvent event) {
		if (cbb != null && editor != null) {
			String key = cbb.getSelectedItem().getLabel();
			String value = editor.getValue().toString().trim();
			Messagebox.show(key + " : " + value);
		}
	}

}
