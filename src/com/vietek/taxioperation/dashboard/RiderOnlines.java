package com.vietek.taxioperation.dashboard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.ngi.zhighcharts.SimpleExtXYModel;
import org.ngi.zhighcharts.ZHighCharts;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ChartModel;
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

import com.vietek.taxioperation.realtime.Rider;

/**
 *
 * @author VuD
 */
public class RiderOnlines extends Div implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid grid;
	private ZHighCharts chart;
	private Gmaps gmap;
	private Gmarker gmarker;
	Tab tabMap;

	public RiderOnlines() {
		super();
		this.init();
	}

	private void init() {
		this.setHflex("1");
		this.setHeight("400px");
		Tabbox tabox = new Tabbox();
		tabox.setParent(this);
		tabox.setHflex("1");
		tabox.setVflex("1");
		Tabs tabs = new Tabs();
		tabs.setParent(tabox);
		Tabpanels tabpanels = new Tabpanels();
		tabpanels.setVflex("1");
		tabpanels.setParent(tabox);
		this.initChart(tabs, tabpanels);
		this.initGrid(tabs, tabpanels);
		this.initMapRider(tabs, tabpanels);
		Timer timer = new Timer();
		timer.setParent(this);
		timer.setDelay(10000);
		timer.setRepeats(true);
		timer.addEventListener(Events.ON_TIMER, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
//				addDataTest();
				grid.setModel(new ListModelList<>(Rider.lstRiderOnline));
				chart.setxAxisOptions(getxAxOption());
				chart.setModel(getModelChart());
				renderMarker();
			}
		});
		timer.start();
	}

	private void initGrid(Tabs tabs, Tabpanels tabpanels) {
		Tab tab = new Tab("Danh sách khách hàng");
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
		col.setHflex("2");
		col.setLabel("Điện thoại");
		col = new Column();
		col.setParent(cols);
		col.setHflex("4");
		col.setLabel("Tên");
		col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		col.setLabel("Kinh độ");
		col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		col.setLabel("Vĩ độ");
		grid.setRowRenderer(new GridRender());
		grid.setModel(new ListModelList<>(Rider.lstRiderOnline));
		grid.setMold("paging");
		grid.setAutopaging(true);
		grid.setPagingPosition("both");
	}

	private void initChart(Tabs tabs, Tabpanels tabpanels) {
		Tab tab = new Tab("Biểu đồ");
		tab.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setParent(tabpanels);
		chart = new ZHighCharts();
		chart.setHflex("1");
		chart.setVflex("1");
		chart.setParent(tabpanel);
		chart.setType("column");
		chart.setTitle("Biểu đồ khách hàng online trong vòng 24 giờ");
		chart.setxAxisOptions(this.getxAxOption());
		chart.setYAxisTitle("Số lượng khách hàng");
		chart.setModel(this.getModelChart());

	}

	private String getxAxOption() {
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

	private ChartModel getModelChart() {
		SimpleExtXYModel dataModel = new SimpleExtXYModel();
		for (int i = 0; i < Rider.lstCustomerByHour.size(); i++) {
			List<String> lstTmp = Rider.lstCustomerByHour.get(i);
			dataModel.addValue("", i, lstTmp.size());
		}
		return dataModel;
	}

	private void initMapRider(Tabs tabs, Tabpanels tabpanels) {
		tabMap = new Tab("Bản đồ");
		tabMap.setParent(tabs);
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setId("mapRider");
		tabpanel.setParent(tabpanels);
		initMap();
		renderMarker();
		tabpanel.appendChild(gmap);
	}

	private void initMap() {
		gmap = new Gmaps();
		gmap.setHflex("1");
		gmap.setVflex("1");
		gmap.setVersion("3.9");
		gmap.setShowSmallCtrl(true);
		gmap.addEventListener("onMapClick", new EventListener<Event>() {
		@Override
		public void onEvent(Event events) throws Exception {
			if(events.getName().equals("onMapClick")){
				MapMouseEvent event = (MapMouseEvent) events;
			    AbstractComponent comp = (AbstractComponent) event.getReference();
			    if (comp instanceof Gmarker) {
				   ((Gmarker) comp).setOpen(true);
			    }
			}
		}
		});
		gmap.setZoom(8);
	}

	private void renderMarker() {
		clearVehicleMarker();
		if (Rider.lstRiderOnline != null && Rider.lstRiderOnline.size() > 0) {
			for (int i = 0; i < Rider.lstRiderOnline.size(); i++) {
				Rider rider = Rider.lstRiderOnline.get(i);
				if (rider.getLattitute() > 0d && rider.getLongtitute() > 0d) {
					gmarker = new Gmarker();
					gmarker.setLat(rider.getLattitute());
					gmarker.setLng(rider.getLongtitute());
					gmarker.setIconImage("/themes/images/Hail_A_Taxi-64_Green.png");
					gmarker.setContent(rider.getCustomer().getPhoneNumber() + " " + "\n" + rider.getCustomer().getName());
					gmarker.setParent(gmap);
					gmap.appendChild(gmarker);
				}
			}
		}
		gmap.panTo(10.778164, 106.688539);
		gmap.setCenter(10.778164, 106.688539);
	}

	private class GridRender implements RowRenderer<Rider> {

		@Override
		public void render(Row row, final Rider data, int index) throws Exception {
			
			if (data != null) {
				if (data.getCustomer() != null) {
					if (data.getLongtitute() > 0d && data.getLattitute() > 0d) {
						row.appendChild(new Label(data.getCustomer().getPhoneNumber()));
						row.appendChild(new Label(data.getCustomer().getName()));
						row.appendChild(new Label(data.getLongtitute() + ""));
						row.appendChild(new Label(data.getLattitute() + ""));
						row.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								tabMap.setSelected(true);
								List<AbstractComponent> listcomponent = gmap.getChildren();
								if (listcomponent != null && listcomponent.size() > 0) {
									for (Iterator<AbstractComponent> it = listcomponent.iterator(); it.hasNext();) {
										AbstractComponent abscomponent = it.next();
										if (abscomponent instanceof Gmarker) {
											if(((Gmarker) abscomponent).getContent().contains(data.getCustomer().getPhoneNumber())){
												((Gmarker) abscomponent).setOpen(true);
											}
										}
									}
								}
							}
						});
					}
				}
			}
		}

	}

	private void clearVehicleMarker() {
		List<AbstractComponent> listcomponent = gmap.getChildren();
		if (listcomponent != null && listcomponent.size() > 0) {
			for (Iterator<AbstractComponent> it = listcomponent.iterator(); it.hasNext();) {
				AbstractComponent abscomponent = it.next();
				if (abscomponent instanceof Gmarker) {
					it.remove();
				}
			}
		}
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
