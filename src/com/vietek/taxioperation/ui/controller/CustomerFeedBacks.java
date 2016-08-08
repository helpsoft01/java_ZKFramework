package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.CustomerFeedBackController;
import com.vietek.taxioperation.model.Customer;
import com.vietek.taxioperation.model.CustomerFeedBack;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class CustomerFeedBacks extends AbstractWindowPanel {

	public CustomerFeedBacks() {
		super(true);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomerFeedBackDetail customerfeedbackdetail = null;

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Loai đánh giá", 150, String.class,
				"getFeedbacktype"));
		lstCols.add(new GridColumn("Điểm đánh giá", 150, Double.class,
				"getPoint"));
		lstCols.add(new GridColumn("Khách hàng", 200, Customer.class,
				"getCustomer"));
		lstCols.add(new GridColumn("Tài xế", 200, Driver.class, "getDriver"));
		lstCols.add(new GridColumn("Nội Dung", 200, String.class, "getConten"));
		this.setGridColumns(lstCols);
	}

	@Override
	public void loadData() {

		CustomerFeedBackController controler = (CustomerFeedBackController) ControllerUtils
				.getController(CustomerFeedBackController.class);
		List<CustomerFeedBack> lstfeedback = controler
				.find("from CustomerFeedBack");
		this.setLstModel(lstfeedback);
		this.setModelClass(CustomerFeedBack.class);

	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (customerfeedbackdetail == null) {
			customerfeedbackdetail = new CustomerFeedBackDetail(currentModel,
					this);
		}
		return customerfeedbackdetail;
	}

}
