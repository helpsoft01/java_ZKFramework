package com.vietek.taxioperation.ui.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.Env;

public class ListOrderContracts extends AbstractWindowPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListOrderContracts() {
		super(true);
	}

	@Override
	public void initLeftPanel() {
	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Số điện thoại", 170, String.class, "getPhoneNumber"));
		lstCols.add(new GridColumn("Loại xe", 80, String.class, "getOrderCarType", "orderCarType", TaxiOrder.class));
		lstCols.add(new GridColumn("Địa chỉ yêu cầu", 300, String.class, "getBeginOrderAddress"));
		lstCols.add(new GridColumn("Thời gian yêu cầu", 150, Timestamp.class, "getBeginOrderTime"));
		lstCols.add(new GridColumn("Xe đăng ký", 200, String.class, "getRegistedTaxis"));
		lstCols.add(new GridColumn("Xe đón", 80, Vehicle.class, "getPickedTaxi"));
		lstCols.add(new GridColumn("Loại yêu cầu", 150, String.class, "getOrderType", "orderType", TaxiOrder.class));
		lstCols.add(new GridColumn("Hình thức đi", 150, Boolean.class, "getTwoWay", "twoWay", TaxiOrder.class));
		lstCols.add(new GridColumn("Đi sân bay", 150, Boolean.class, "getAirStation", "airStation", TaxiOrder.class));
		lstCols.add(new GridColumn("Trạng thái", 150, String.class, "getStatus", "status", TaxiOrder.class));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "from TaxiOrder where beginOrderTime > '"
				+ dateFormat.format(new Date(System.currentTimeMillis() - (45l * 60l * 1000l)))
//				+ "' and phoneNumber != null and airStation = true" 
				+ "' and phoneNumber != null " 
				+ " order by created desc";
		this.setStrQuery(sql);
		this.setModelClass(TaxiOrder.class);
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		if (((event.getName().equals(Events.ON_OK) || event.getName().equals(Events.ON_DOUBLE_CLICK))
				&& event.getTarget().equals(listbox) || event.getTarget().equals(this.getBt_edit()))) {
			if (this.getBt_edit().isVisible()) {
				if (listbox.getSelectedIndex() >= 0) {
					currentModel = (AbstractModel) listModel.getElementAt(listbox.getSelectedIndex());
					currentIndex = listbox.getSelectedIndex();
					if (currentModel != null) {
						showDetailTmp((TaxiOrder) currentModel);
					}
				}
			}
		} else if (event.getTarget().equals(this.getBt_refresh())) {
			if (event.getName().equals(Events.ON_CLICK)) {
				refresh();
			}
		}
	}

	private void showDetailTmp(TaxiOrder taxiOrder) {
		TaxiOrdersForm taxiOrderForm = Env.getTaxiOrdersWindow();
		if (taxiOrderForm == null) {
			Env.getHomePage().showFunction("TaxiOrdersForm");
			taxiOrderForm = Env.getTaxiOrdersWindow();
			if (taxiOrderForm != null) {
				try {
					if (taxiOrder.getPhoneNumber() != null && taxiOrder.getPhoneNumber().length() > 0) {
						taxiOrderForm.detailForm_showInforByPhone(taxiOrder.getPhoneNumber());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				if (taxiOrder.getPhoneNumber() != null && taxiOrder.getPhoneNumber().length() > 0) {
					taxiOrderForm.detailForm_showInforByPhone(taxiOrder.getPhoneNumber());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
