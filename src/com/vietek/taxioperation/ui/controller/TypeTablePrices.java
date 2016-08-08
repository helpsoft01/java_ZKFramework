package com.vietek.taxioperation.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.TypeTablePriceController;
import com.vietek.taxioperation.model.TablePrice;
import com.vietek.taxioperation.model.TaxiType;
import com.vietek.taxioperation.model.TypeTablePrice;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class TypeTablePrices extends AbstractWindowPanel {

	private TypeTablePricesDetail detailWindow = null;

	public TypeTablePrices() {
		super(true);
		this.setTitle("Danh mục loại bảng giá");
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstcols = new ArrayList<GridColumn>();
		lstcols.add(new GridColumn("Mã", 200, String.class, "getCodetype", "codetype", getModelClass()));
		lstcols.add(new GridColumn("Loại bảng giá", 200, String.class, "getTablepricename", "tablepricename",
				getModelClass()));
		lstcols.add(new GridColumn("Loại xe", 400, TaxiType.class, "getTypevehicle", "typevehicle", getModelClass()));
		lstcols.add(
				new GridColumn("Bảng giá", 200, TablePrice.class, "getTablePrices", "tablePrices", getModelClass()));
		this.setGridColumns(lstcols);
	}

	@Override
	public void loadData() {
		TypeTablePriceController controller = (TypeTablePriceController) ControllerUtils
				.getController(TypeTablePriceController.class);
		List<TypeTablePrice> lstModel = controller.find("from TypeTablePrice");
		this.setModelClass(TypeTablePrice.class);
		this.setLstModel(lstModel);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new TypeTablePricesDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

}