package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.TaxiTypeController;
import com.vietek.taxioperation.model.CarSupplier;
import com.vietek.taxioperation.model.Seat;
import com.vietek.taxioperation.model.TaxiType;
import com.vietek.taxioperation.model.TradeMark;
import com.vietek.taxioperation.model.TypeTablePrice;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class TaxiTypes extends AbstractWindowPanel {

	public TaxiTypes() {
		super(true);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TaxiTypesDetail taxiTypesDetail = null;

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Mã", 200, Integer.class, "getValue","value",getModelClass()));
		lstCol.add(new GridColumn("Tên", 200, String.class, "getName","name",getModelClass()));
		lstCol.add(new GridColumn("Hãng xe", 200, CarSupplier.class, "getCarSupplier","carSupplier",getModelClass()));
		lstCol.add(new GridColumn("Thương hiệu", 200, TradeMark.class, "getTradeMark","tradeMark",getModelClass()));
		lstCol.add(new GridColumn("Chỗ ngồi", 200, Seat.class, "getSeats"));
		lstCol.add(new GridColumn("Loại bảng cước đi giá", 200, TypeTablePrice.class, "getTypeTablePrice"));
		setGridColumns(lstCol);
	}

	@Override
	public void loadData() {
		TaxiTypeController taxiTypeController = (TaxiTypeController) ControllerUtils
				.getController(TaxiTypeController.class);
		List<TaxiType> lstTaxiTypes = taxiTypeController.find("from TaxiType");
		setLstModel(lstTaxiTypes);
		setModelClass(TaxiType.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (taxiTypesDetail == null)
			taxiTypesDetail = new TaxiTypesDetail(getCurrentModel(), this);
		return taxiTypesDetail;
	}

}
