package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.model.PassengerNotification;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;

/**
 * author Viet Ha Ca
 */
public class PassengerNotifications extends AbstractWindowPanel {

	public PassengerNotifications() {
		super(true);
		setTitle("Thông báo cho khách hàng");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox textSearch;

	private PassengerNotificationsDetail detailWindow = null;

	@Override
	public void initLeftPanel() {
		initColumns();
	}

	public void basicSearch(){
		String text = textSearch.getValue();
		if(text=="")
			return;
		else{
			String query="WHERE message like '%"+text+"%'";
			searchData(query);
		}
	}
	
	@Override
	public void initColumns() {
		// TODO Auto-generated method stub
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã", 100, String.class, "getId"));
		lstCols.add(new GridColumn("Nội dung", 600, String.class, "getMessage"));
		lstCols.add(new GridColumn("Khách hàng", 300, String.class,
				"getCustomers"));
		lstCols.add(new GridColumn("Kích hoạt", 100, String.class,
				"getIsActive"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		setModelClass(PassengerNotification.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new PassengerNotificationsDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}
}
