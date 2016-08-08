package com.vietek.taxioperation.report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.ngi.zhighcharts.SimpleExtXYModel;
import org.ngi.zhighcharts.ZHighCharts;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.ChartModel;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.util.ControllerUtils;

public class StatisticTripWeek extends Div implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ZHighCharts chart;
	private Datebox dbFromDate;
	private Datebox dbToDate;
	private Button btnSearch;
	private final String KEY_ORDER_ALL = "order_all";
	private final String KEY_ORDER_SUCCESS = "order_success";
	private final String KEY_ORDER_AUTOMATIC = "order_auto";

	public StatisticTripWeek() {
		Vlayout vlayout = new Vlayout();
		vlayout.setStyle("width: 100%; height: 100%;");
		vlayout.setParent(this);
		Div div = new Div();
		div.setSclass("div_trip_week_title");
		div.setParent(vlayout);
		Label label = new Label("Thống kê số lượng đặt xe");
		label.setSclass("trip_week_title");
		label.setParent(div);

		div = new Div();
		div.setSclass("div_trip_week_filter");
		div.setParent(vlayout);
		dbFromDate = new Datebox();
		dbFromDate.setSclass("datebox_trip_week");
		dbFromDate.setFormat("dd/MM/yyyy");
		dbFromDate.setParent(div);
		dbFromDate.addEventListener(Events.ON_CHANGE, this);
		dbToDate = new Datebox();
		dbToDate.setStyle("margin-left: 10px;");
		dbToDate.setSclass("datebox_trip_week");
		dbToDate.setFormat("dd/MM/yyyy");
		dbToDate.setParent(div);
		dbToDate.addEventListener(Events.ON_CHANGE, this);
		dbToDate.setValue(new Date(System.currentTimeMillis()));
		dbFromDate.setValue(new Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)));
		btnSearch = new Button("Xem");
		btnSearch.setSclass("btn_trip_week");
		btnSearch.setParent(div);
		btnSearch.addEventListener(Events.ON_CLICK, this);

		chart = new ZHighCharts();
		chart.setHflex("1");
		chart.setVflex("1");
		chart.setParent(vlayout);
		chart.setType("column");
		chart.setPlotOptions("{ series: { dataLabels: { enabled: true, style: { fontWeight: 'bold' } } } }");
		chart.setTitle(" ");
		chart.setxAxisOptions(this.getxAxOption(System.currentTimeMillis()));
		chart.setYAxisTitle("Số lượng đặt xe");
		chart.setModel(this.getModelChart(System.currentTimeMillis()));
	}

	private String getxAxOption(long toTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		long timeOfDay = 24l * 60l * 60l * 1000l;
		StringBuilder sb = new StringBuilder();
		sb.append("{categories: [");
		for (int i = 6; i >= 0; i--) {
			String value = sdf.format(new Date(toTime - ((long) i * timeOfDay)));
			sb.append("'").append(value).append("'");
			sb.append(",");
		}
		String value = sdf.format(new Date(toTime));
		sb.append("'").append(value).append("'");
		sb.append("],");
		sb.append("labels: {");
		sb.append("rotation: 0");
		sb.append(",").append("align: 'right'");
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	private ChartModel getModelChart(long toTime) {
		SimpleExtXYModel dataModel = new SimpleExtXYModel();
		long timeOfDay = 24l * 60l * 60l * 1000l;
		long cur = toTime;
		HashMap<String, Integer> values;
		for (int i = 6; i >= 0; i--) {
			int j = Math.abs(i - 6);
			Timestamp time = new Timestamp(cur - ((long) i * timeOfDay));
			values = getValue(time);
			if (values.get(KEY_ORDER_ALL) != null)
				dataModel.addValue("Tổng số cuốc", j, values.get(KEY_ORDER_ALL));
			else
				dataModel.addValue("Tổng số cuốc", j, 0);
			if (values.get(KEY_ORDER_SUCCESS) != null)
				dataModel.addValue("Cuốc thực hiện thành công", j, values.get(KEY_ORDER_SUCCESS));
			else
				dataModel.addValue("Cuốc thực hiện thành công", j, 0);
			if (values.get(KEY_ORDER_AUTOMATIC) != null)
				dataModel.addValue("Cuốc xử lý tự động thành công", j, values.get(KEY_ORDER_AUTOMATIC));
			else
				dataModel.addValue("Cuốc xử lý tự động thành công", j, 0);
		}
		return dataModel;
	}

	@SuppressWarnings("deprecation")
	private HashMap<String, Integer> getValue(Timestamp day) {
		Map<String, Integer> mapValue = new HashMap<String, Integer>();
		Session session = ControllerUtils.getCurrentSession();
		try {
			Timestamp startOfDay = new Timestamp(day.getTime());
			startOfDay.setHours(0);
			startOfDay.setMinutes(0);
			startOfDay.setSeconds(0);
			startOfDay.setNanos(0);
			Timestamp endOfDay = new Timestamp(day.getTime());
			endOfDay.setHours(23);
			endOfDay.setMinutes(59);
			endOfDay.setSeconds(59);
			endOfDay.setNanos(59);
			Query query = session.createQuery("select count(*) from TaxiOrder where beginOrderTime >= '" + startOfDay
					+ "' AND beginOrderTime<='" + endOfDay + "'");
			Object obj = query.list().get(0);
			if (obj != null) {
				mapValue.put(KEY_ORDER_ALL, Integer.parseInt(obj.toString()));
			} else {
				mapValue.put(KEY_ORDER_ALL, 0);
			}
			query = session.createQuery("select count(*) from TaxiOrder where beginOrderTime >= '" + startOfDay
					+ "' AND beginOrderTime<='" + endOfDay + "' AND status>=3");
			obj = query.list().get(0);
			if (obj != null) {
				mapValue.put(KEY_ORDER_SUCCESS, Integer.parseInt(obj.toString()));
			} else {
				mapValue.put(KEY_ORDER_SUCCESS, 0);
			}
			query = session.createQuery("select count(*) from TaxiOrder where beginOrderTime >= '" + startOfDay
					+ "' AND beginOrderTime<='" + endOfDay + "' AND status>=3 AND isAutoOperation=true");
			obj = query.list().get(0);
			if (obj != null) {
				mapValue.put(KEY_ORDER_AUTOMATIC, Integer.parseInt(obj.toString()));
			} else {
				mapValue.put(KEY_ORDER_AUTOMATIC, 0);
			}
			session.close();
		} catch (Exception e) {
			AppLogger.logDebug.error("Loi thong ke dat xe theo ngay (habv)", e);
			session.close();
		}
		return (HashMap<String, Integer>) mapValue;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget().equals(btnSearch)) {
				chart.setxAxisOptions(this.getxAxOption(dbToDate.getValue().getTime()));
				chart.setModel(this.getModelChart(dbToDate.getValue().getTime()));
			}
		} else if (event.getName().equals(Events.ON_CHANGE)) {
			if (event.getTarget().equals(dbFromDate)) {
				Date date = dbFromDate.getValue();
				if (date == null) {
					dbFromDate.setValue(new Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)));
				} else {
					long dTime1 = dbToDate.getValue().getTime() - (7 * 24 * 60 * 60 * 1000);
					long dTime2 = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
					if (date.getTime() > dTime1) {
						if (date.getTime() > dTime2) {
							dbToDate.setValue(new Date(System.currentTimeMillis()));
							dbFromDate.setValue(new Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)));
						} else {
							dbToDate.setValue(new Date(date.getTime() + ((long) 7 * 24 * 60 * 60 * 1000)));
						}
					}
				}
			} else if (event.getTarget().equals(dbToDate)) {
				Date date = dbToDate.getValue();
				if (date == null) {
					dbToDate.setValue(new Date(System.currentTimeMillis()));
				} else {
					if (date.getTime() > System.currentTimeMillis()) {
						dbToDate.setValue(new Date(System.currentTimeMillis()));
						dbFromDate.setValue(new Date(System.currentTimeMillis() - ((long) 7 * 24 * 60 * 60 * 1000)));
					} else {
						dbFromDate.setValue(new Date(date.getTime() - ((long) 7 * 24 * 60 * 60 * 1000)));
					}
				}
			}
		}
	}

}