package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.TaxiGroupController;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class TaxiGroupForm extends AbstractWindowPanel implements Serializable {

	private static final long serialVersionUID = 1L;
	private TaxiGroupFormDetail taxigroupformdetail = null;

	public TaxiGroupForm() {
		super(true);
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		// TODO Auto-generated method stub
		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Mã đội xe", 200, String.class, "getValue", "value", getModelClass()));
		lstCols.add(new GridColumn("Tên đội xe", 350, String.class, "getName", "name", getModelClass()));
		lstCols.add(new GridColumn("Đội trưởng", 200, String.class, "getLeader"));
		lstCols.add(new GridColumn("Chi nhánh", 250, Agent.class, "getAgent", "agent", getModelClass()));
		lstCols.add(new GridColumn("Kích hoạt", 50, Boolean.class, "getIsActive"));
		setGridColumns(lstCols);
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		TaxiGroupController taxigroupcontroller = (TaxiGroupController) ControllerUtils
				.getController(TaxiGroupController.class);
		List<TaxiGroup> lstValue = taxigroupcontroller.find("from TaxiGroup");
		setLstModel(lstValue);
		setModelClass(TaxiGroup.class);
	}

	@Override
	protected String prepareQuerySearch() {
		String Result = super.prepareQuerySearch();
		Result = Result.replace("agent.id", "AgentID");
		return Result;
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (taxigroupformdetail == null)
			taxigroupformdetail = new TaxiGroupFormDetail(getCurrentModel(), this);
		return taxigroupformdetail;
	}
}