package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vietek.taxioperation.controller.AgentController;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.Province;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.Zone;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;

public class Agents extends AbstractWindowPanel implements Serializable {

	/**
	 * @author MPV
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AgentDetail agentdetail = null;

	public Agents() {
		super(true);
		this.setTitle("Danh mục chi nhánh");

	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initColumns() {
		ArrayList<GridColumn> lstCol = new ArrayList<GridColumn>();
		lstCol.add(new GridColumn("Mã chi nhánh", 120, String.class, "getValue", "value", getModelClass()));
		lstCol.add(new GridColumn("Tên chi nhánh", 350, String.class, "getAgentName", "AgentName", getModelClass()));
		lstCol.add(new GridColumn("Số điện thoại", 150, String.class, "getPhone"));
		lstCol.add(new GridColumn("Số Fax", 100, String.class, "getFax"));
		lstCol.add(new GridColumn("Địa chỉ", 300, String.class, "getAgentAddress"));
		lstCol.add(new GridColumn("Khu vực", 100, Zone.class, "getZone"));
		lstCol.add(new GridColumn("Tỉnh Thành", 100, Province.class, "getProvince"));
		lstCol.add(new GridColumn("Mã số thuế", 100, String.class, "getTaxCode"));
		lstCol.add(new GridColumn("Số tổng đài", 100, String.class, "getCallCenterNumber"));
		lstCol.add(new GridColumn("Công ty trực thuộc", 300, SysCompany.class, "getCompany"));
		lstCol.add(new GridColumn("Kích hoạt", 300, Boolean.class, "getIsActive"));
		setGridColumns(lstCol);

	}

	@Override
	public void loadData() {
		AgentController agentcontroller = (AgentController) ControllerUtils.getController(AgentController.class);
		List<Agent> lstvalue = agentcontroller.find("from Agent");
		setLstModel(lstvalue);
		setModelClass(Agent.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (agentdetail == null) {
			agentdetail = new AgentDetail(getCurrentModel(), this);
		}
		return agentdetail;
	}
}