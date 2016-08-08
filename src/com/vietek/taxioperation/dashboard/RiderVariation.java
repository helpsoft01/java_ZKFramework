package com.vietek.taxioperation.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ngi.zhighcharts.SimpleExtXYModel;
import org.ngi.zhighcharts.ZHighCharts;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Timer;

import com.vietek.taxioperation.model.RiderVariationModel;
import com.vietek.taxioperation.model.RiderVariationOrder;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.DateUtils;

/**
 * 
 * @author Dzung Nguyen created: 30/01/2016
 */
public class RiderVariation extends Div {

	private static final long serialVersionUID = 1L;
	private Grid grid;
	private ZHighCharts chart;
	private List<RiderVariationModel> riderVariation;
	private SimpleExtXYModel extXYModel;
	
	public RiderVariation() throws Exception {
		super();
		this.init();
	}

	private void init() throws Exception {		
		this.setHflex("1");
		this.setHeight("450px");
		Tabbox tabbox = new Tabbox();
		tabbox.setHflex("1");
		tabbox.setVflex("1");
		tabbox.setParent(this);

		Tabs tabs = new Tabs();
		tabs.setVflex("1");
		tabs.setParent(tabbox);

		Tabpanels tabpanels = new Tabpanels();
		tabpanels.setVflex("1");
		tabpanels.setParent(tabbox);

		this.initGrid(tabs, tabpanels);
		this.initChart(tabs, tabpanels);
		this.initTimer();
	}

	private void initGrid(Tabs tabs, Tabpanels tabpanels) {
		Tab tab = new Tab("Danh sách");
		tab.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setVflex("1");
		tabpanel.setParent(tabpanels);
		grid = new Grid();
		grid.setParent(tabpanel);
		grid.setHflex("1");
		grid.setVflex("1");
		grid.setAutopaging(true);
		grid.setSizedByContent(true);
		Columns cols = new Columns();
		cols.setParent(grid);
		Column col = new Column();
		col.setParent(cols);
		col.setHflex("3");
		col.setLabel("Thời điểm");
		col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		col.setLabel("Normal");
		col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		col.setLabel("Mobile");
		col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		col.setLabel("Website");
		col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		col.setLabel("Other");
		grid.setRowRenderer(new GridRender());
		grid.setMold("paging");
		grid.setAutopaging(true);
	}

	private void initChart(Tabs tabs, Tabpanels tabpanels) throws Exception {
		Tab tab = new Tab("Biểu đồ");
		tab.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setParent(tabpanels);

		chart = new ZHighCharts();
		chart.setHflex("1");
		chart.setVflex("1");
		chart.setParent(tabpanel);

		chart.setType("spline");
		chart.setTitle("Biểu đồ biến động lượng khách đặt xe theo thời gian");
		chart.setYAxisTitle("Số cuộc đặt xe");
		chart.setOptions("{" + "zoomType: 'xy'" + "}");
		chart.setyAxisOptions(setyAxOption());
		chart.setTooltipOptions("{" + "shared: true," + "crosshairs: true,"
				+ "borderColor : '#000000'" + "}");

		String formatt = "obj.x + '<br/>";
		for (int i = 0; i < 4; i++) {
			formatt += "<span style=\"" + "color:'+ obj.points[" + i
					+ "].series.color +';\">'" + "+ obj.points[" + i
					+ "].series.name  +'</span> : <b>'+ obj.points[" + i
					+ "].y " + "+ ' ' + obj.points[" + i
					+ "].series.options.units + '</b>";
			if (i < 3)
				formatt += "<br/>";
		}
		chart.setTooltipFormatter("function formatTooltip(obj){" + "return "
				+ formatt + "';" + "}");
		chart.setPlotOptions("{" + "spline: {" + "marker: {" + "radius: 4,"
				+ "lineColor: '#666666'," + "lineWidth: 1" + "}" + "}" + "}");

		/* set spline style */
		this.initSeries();
	}

	private void initTimer() {
		final Timer timer = new Timer();
		timer.setParent(this);
		timer.setDelay(10 * 60 * 1000);
		timer.setRepeats(true);
		timer.addEventListener(Events.ON_TIMER, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				timer.setRepeats(false);
				onTimerElaped();
				timer.setRepeats(true);
			}
		});
		timer.start();
	}

	private void onTimerElaped() throws Exception {
		this.getChartDataModel();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initSeries() {
		HashMap marker = new HashMap();
		// Series 1 - normal booking
		HashMap style = new HashMap();
		style.put("color", "#89A54E");
		style.put("type", "spline");
		style.put("yAxis", 0);
		style.put("units", "rider");
		chart.setSeriesOptions("Normal", style);
		// Series 2 - mobile booking
		style = new HashMap();
		style.put("color", "#4572A7");
		style.put("type", "spline");
		style.put("yAxis", 1);
		style.put("units", "rider");
		chart.setSeriesOptions("Mobile", style);
		// Series 3 - web booking
		style = new HashMap();
		style.put("color", "#AA4643");
		style.put("type", "spline");
		style.put("dashStyle", "shortdot");
		style.put("yAxis", 2);
		style.put("units", "rider");
		marker = new HashMap();
		marker.put("enabled", false);
		style.put("marker", marker);
		chart.setSeriesOptions("Website", style);
		// Series 4 - web booking
		style = new HashMap();
		style.put("color", "#808080");
		style.put("type", "spline");
		style.put("dashStyle", "shortdot");
		style.put("yAxis", 3);
		style.put("units", "rider");
		marker = new HashMap();
		marker.put("enabled", false);
		style.put("marker", marker);
		chart.setSeriesOptions("Other", style);
		chart.setYAxisTitle("");

		extXYModel = new SimpleExtXYModel();
		chart.setModel(extXYModel);
	}

	private void updateSeries() throws Exception {
		if (riderVariation == null)
			return;
		int len = riderVariation.size();
		chart.setxAxisOptions(setxAxOption());		
		extXYModel = new SimpleExtXYModel();		
		chart.setModel(extXYModel);	
		if (len > 0) {	
			/*if (!finishInit) {
				
				finishInit = !finishInit;
			}else{
				int normal = riderVariation.get(len - 1).getNormalOrder();
				int mobile = riderVariation.get(len - 1).getMobileOrder();
				int website = riderVariation.get(len - 1).getWebOrder();
				int other = riderVariation.get(len - 1).getOtherOrder();
				String sb = "{" +
						"events: {" +
							"load: function() {" +
								"var series0 = this.series[0];" +
								"var series1 = this.series[1];" +
								"var series2 = this.series[2];" +
								"var series3 = this.series[3];" +
								"series0.addPoint([" + (len - 1) + "," + normal + "], true, true);" +
								"series1.addPoint([" + (len - 1) + "," + mobile + "], true, true);" +
								"series2.addPoint([" + (len - 1) + "," + website + "], true, true);" +
								"series3.addPoint([" + (len - 1) + "," + other + "], true, true);" +
							"}" +
						"}" +
					"}";
				chart.setOptions(sb);
			}	*/				
			for (int i = 0; i < len; i++) {
				extXYModel.addValue("Normal", i, riderVariation.get(i).getNormalOrder());
				extXYModel.addValue("Mobile", i, riderVariation.get(i).getMobileOrder());
				extXYModel.addValue("Website", i, riderVariation.get(i).getWebOrder());
				extXYModel.addValue("Other", i, riderVariation.get(i).getOtherOrder());
			}			
		}				
	}

	private void updateGrid() throws Exception {
		if (riderVariation != null) {
			Collections.reverse(riderVariation);
			grid.setModel(new ListModelList<>(riderVariation));
		}
	}
	
	private void getChartDataModel() throws Exception{		
		Session session = ControllerUtils.getCurrentSession();
		Transaction tx = null;
		try {
			tx = (Transaction) session.beginTransaction();
			Criteria criteria = session.createCriteria(RiderVariationOrder.class);						
			criteria.add(Restrictions.ge("timelog", DateUtils.getDateNow()));
			criteria.addOrder(Order.desc("timeCode"));
			criteria.setMaxResults(12);
			
			@SuppressWarnings("unchecked")
			List<RiderVariationOrder> riderVariationOrders = criteria.list();
			tx.commit();
			Collections.reverse(riderVariationOrders);
			riderVariation = new ArrayList<RiderVariationModel>();
			for (@SuppressWarnings("rawtypes")
			Iterator iterator = riderVariationOrders.iterator(); iterator.hasNext();) {
				RiderVariationOrder riderVariationOrder = (RiderVariationOrder) iterator.next();
				RiderVariationModel rvm = new RiderVariationModel();
				rvm.setTimeCode(riderVariationOrder.getTimeCode());
				rvm.setTimelog(riderVariationOrder.getTimelog());
				rvm.setNormalOrder(riderVariationOrder.getNormalOrder());
				rvm.setMobileOrder(riderVariationOrder.getMobileOrder());
				rvm.setWebOrder(riderVariationOrder.getWebOrder());
				rvm.setOtherOrder(riderVariationOrder.getOtherOrder());
				riderVariation.add(rvm);								
				
			}						

			// Update series data
			this.updateSeries();
			// Update grid data
			this.updateGrid();
			
		} catch (Exception e) {
			if (tx!=null) 
				tx.rollback();
			e.printStackTrace();
		}finally {
	         session.close(); 
		}
	}	

	private String setxAxOption() {
		StringBuilder sb = new StringBuilder();
		sb.append("{" + "categories: [");
		int len = riderVariation.size();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				String value = riderVariation.get(i).getTimeCode()
						.substring(0, 2)
						+ ":"
						+ riderVariation.get(i).getTimeCode().substring(2);
				sb.append("'" + value + "'");
				if (i < len - 1) {
					sb.append(",");
				}
			}
		}
		sb.append("],");
		sb.append("labels:{rotation: -45}");
		sb.append("}");
		return sb.toString();
	}

	private String setyAxOption() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("{"
				+ // yAxis 1
				"labels: {" + "formatter: function() {"
				+ "return this.value;"
				+ "},"
				+ "style: {"
				+ "color: '#89A54E'"
				+ "} "
				+ "},"
				+ "min: 0,"
				+ "allowDecimals: false,"
				+ "rotation: -45,"
				+ "title: {"
				+ "text: 'Normal',"
				+ "style: { "
				+ "color: '#89A54E'"
				+ "}"
				+ "},"
				+ "},"
				+ "{"
				+ // yAxis 2
				"title: {" + "text: 'Mobile'," + "style: {"
				+ "color: '#4572A7'" + "}" + "},"
				+ "min: 0,"
				+ "allowDecimals: false,"
				+ "labels: {"
				+ "formatter: function() { "
				+ "return this.value;"
				+ "},"
				+ "style: {"
				+ "color: '#4572A7'"
				+ "}"
				+ "},"
				+ "},"
				+ "{"
				+ // yAxis 3
				"title: { " + "text: 'Website'," + "style: {"
				+ "color: '#AA4643' " + "}" + "}," + "min: 0,"
				+ "allowDecimals: false," + "labels: {"
				+ "formatter: function() {" + "return this.value;" + "}, "
				+ "style: {" + "color: '#AA4643'"
				+ "}"
				+ "},"
				+ "opposite: true"
				+ "},"
				+ "{"
				+ // yAxis 4
				"title: { " + "text: 'Other'," + "style: {"
				+ "color: '#AA5050' " + "}" + "}," + "min: 0,"
				+ "allowDecimals: false," + "labels: {"
				+ "formatter: function() {" + "return this.value;" + "}, "
				+ "style: {" + "color: '#AA5050'" + "}" + "},"
				+ "opposite: true" + "}");
		sb.append("]");
		return sb.toString();
	}

	private class GridRender implements RowRenderer<RiderVariationModel> {

		@Override
		public void render(Row row, RiderVariationModel data, int index)
				throws Exception {
			if (data != null) {
				SimpleDateFormat dateformat = new SimpleDateFormat(
						"dd-MM-yyyy HH:mm");
				if (data.getTimeCode() != null) {
					row.appendChild(new Label(""
							+ (data.getTimelog() == null ? "" : dateformat
									.format(data.getTimelog()))));
					row.appendChild(new Label(data.getNormalOrder() + ""));
					row.appendChild(new Label(data.getMobileOrder() + ""));
					row.appendChild(new Label(data.getWebOrder() + ""));
					row.appendChild(new Label(data.getOtherOrder() +  ""));
				}
			}
		}
	}
}
