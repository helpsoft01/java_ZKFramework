package com.vietek.taxioperation.dashboard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.ngi.zhighcharts.SimpleExtXYModel;
import org.ngi.zhighcharts.ZHighCharts;
import org.zkoss.zul.ChartModel;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import com.vietek.taxioperation.realtime.Taxi;

public class RequestOnlines extends Div {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	private Grid grid;
	private ZHighCharts chart;
	
	public RequestOnlines() {
		super();
		this.init();
	}

	private void init() {
		this.setHflex("1");
		this.setHeight("300px");
		Tabbox tabbox = new Tabbox();
		tabbox.setParent(this);
		tabbox.setHflex("1");
		tabbox.setVflex("1");
		
		Tabs tabs = new Tabs();
		tabs.setParent(tabbox);
		
		Tabpanels tabpanels = new Tabpanels();
		tabpanels.setParent(tabbox);
		tabpanels.setVflex("1");
		
		this.initChart(tabs, tabpanels);
		this.initGrid(tabs, tabpanels);
		
	}

	private void initChart(Tabs tabs, Tabpanels tabpanels) {
		Tab tab = new Tab("Biểu đồ");
		tab.setParent(tabs);
		
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setParent(tabpanels);
		
		chart = new ZHighCharts();
		chart.setVflex("1");
		chart.setHflex("1");
		chart.setParent(tabpanel);
		chart.setType("column");
		chart.setTitle("Biểu đồ lượng yêu cầu của khách trong ngày");
		chart.setYAxisTitle("Số lượng yêu cầu");
		chart.setxAxisOptions(this.getxAxisOp());
		chart.setModel(this.getChartModel());
		
		
		
	}

	private ChartModel getChartModel() {
		SimpleExtXYModel dataModel = new SimpleExtXYModel();
		for (int i = 0; i < Taxi.LST_DRIVER_BY_HOUR.size(); i++) {
			List<String> lstTmp = Taxi.LST_DRIVER_BY_HOUR.get(i);
			dataModel.addValue("", i, lstTmp.size());
		}
		return dataModel;
	}

	private String getxAxisOp() {

		SimpleDateFormat sdf = new SimpleDateFormat("HH dd/MM");
		long timeOfHour = 60l * 60l * 1000l;
		StringBuilder sb = new StringBuilder();
		sb.append("{categories: [");
		long cur = System.currentTimeMillis();
		for (int i = 23; i > 0; i--) {
			String value = sdf.format(new Date(cur - ((long) i * timeOfHour)));
			sb.append("'").append(value).append("'");
			sb.append(",");
		}
		String value = sdf.format(new Date(cur));
		sb.append("'").append(value).append("'");
		sb.append("],");
		sb.append("labels: {");
		sb.append("rotation: -45");
		sb.append(",").append("align: 'right'");
		sb.append("}");
		sb.append("}");
		return sb.toString();
		
	}

	private void initGrid(Tabs tabs, Tabpanels tabpanels) {
		Tab tab = new Tab("Danh sách yêu cầu");
		tab.setParent(tabs);
		
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setParent(tabpanels);
		tabpanel.setVflex("1");
		
		grid = new Grid();
		grid.setParent(tabpanel);
		grid.setVflex("1");
		grid.setHflex("1");
		grid.setAutopaging(true);
		
		Columns cols = new Columns();
		cols.setParent(grid);
		
		Column col = new Column();
		col.setParent(cols);
		col.setWidth("100px");
		col.setLabel("Số điện thoại khách");
		
		col = new Column();
		col.setParent(cols);
		col.setWidth("100px");
		col.setLabel("Tên khách");
		
		col = new Column();
		col.setParent(cols);
		col.setWidth("100px");
		col.setLabel("Địa chỉ yêu cầu");
		
	}

}
