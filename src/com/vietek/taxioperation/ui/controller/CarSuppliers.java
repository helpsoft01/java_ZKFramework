package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.CarSupplierController;
import com.vietek.taxioperation.model.CarSupplier;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class CarSuppliers extends AbstractWindowPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4562460558486125664L;

	private CarSuppliersDetail carSuppliersDetail = null;

	public CarSuppliers() {
		super(true);
		setTitle("Hãng xe");
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Số ID", 100, Integer.class, "getId"));
		lstCol.add(new GridColumn("Tên nhà sản xuất", 300, String.class,
				"getName"));
		setGridColumns((ArrayList<GridColumn>) lstCol);
	}

	@Override
	public void loadData() {
		CarSupplierController carSupplierController = (CarSupplierController) ControllerUtils
				.getController(CarSupplierController.class);
		List<CarSupplier> lstValue = carSupplierController
				.find("from CarSupplier");
		setLstModel(lstValue);
		setModelClass(CarSupplier.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (carSuppliersDetail == null)
			carSuppliersDetail = new CarSuppliersDetail(getCurrentModel(), this);
		return carSuppliersDetail;
	}

}
