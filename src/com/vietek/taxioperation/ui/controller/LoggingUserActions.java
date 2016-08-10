package com.vietek.taxioperation.ui.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.model.LoggingUserAction;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.DateUtils;

public class LoggingUserActions extends AbstractWindowPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LoggingUserActionDetail loggingUserActionDetail;
	private Datebox datefrom;
	private Datebox dateto;
	private Combobox cbxClassNames;
	private Combobox cbxZulfiles;
	
	
	public LoggingUserActions() {
		// TODO Auto-generated constructor stub
		super(true);
		this.setDisplayLeftPanel(true);
	}

	@Override
	public void initLeftPanel() {
		// TODO Auto-generated method stub
		Panel panel = new Panel();
		panel.setParent(getLeftPane());
		panel.setTitle("ĐIỀU KIỆN TÌM KIẾM");
		panel.setStyle("color : black; font-size : 14px; font-weight : bold; overflow : auto");	
		panel.setVisible(true);
		panel.setVflex("1");
		panel.setWidth("100%");
		
		Panelchildren panchild = new Panelchildren();
		panchild.setParent(panel);

		Div div = new Div();
		div.setParent(panchild);
		Label label = new Label("Từ này");
		label.setParent(div);
		label.setStyle("font-weight : bold; font-size : 14px; color : black");
		div = new Div();
		div.setParent(panchild);
		div.setStyle("margin-top : 10px; margin-left : 9px");
		datefrom = new Datebox();
		datefrom.setParent(div);
		datefrom.setWidth("170px");
		datefrom.setValue(DateUtils.addHour(new Date(), 00, 00));
		datefrom.setFormat("dd/MM/yyyy HH:mm");
		
		
	}

	@Override
	public void initColumns() {
		// TODO Auto-generated method stub
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Thời điểm", 200, Timestamp.class, "getTimelog", "timelog", getModelClass()));
		lstCols.add(new GridColumn("Form", 100, String.class, "getFormname", "formname", getModelClass()));
		lstCols.add(new GridColumn("Model", 100, String.class, "getModelname", "modelname", getModelClass()));
		lstCols.add(new GridColumn("Hành động", 80, Integer.class, "getAction", "action", getModelClass()));
		lstCols.add(new GridColumn("Fields", 300, String.class, "getFieldsdetail", "fields", getModelClass()));
		lstCols.add(new GridColumn("Content", 300, String.class, "getValuesdetail", "vals", getModelClass()));
		lstCols.add(new GridColumn("Host", 100, String.class, "getHostname", "hostname", getModelClass()));
		lstCols.add(new GridColumn("IP", 100, String.class, "getIpaddress", "ipaddress", getModelClass()));
		lstCols.add(new GridColumn("User", 80, String.class, "getUserid", "userid", getModelClass()));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		this.setModelClass(LoggingUserAction.class);
	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (loggingUserActionDetail == null) {
			loggingUserActionDetail = new LoggingUserActionDetail(getCurrentModel(), this);
		}
		return loggingUserActionDetail;
	}
	
}
