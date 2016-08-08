package com.vietek.taxioperation.dashboard;


import java.text.SimpleDateFormat;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Timer;

import com.vietek.taxioperation.common.EnumStatus;
import com.vietek.taxioperation.realtime.Trip;
import com.vietek.taxioperation.realtime.TripManager;
import com.vietek.taxioperation.ui.controller.TaxiOrdersForm;
import com.vietek.taxioperation.util.Env;

/**
 *
 * @author VuD
 */
public class TripIsProcessings extends Div {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid grid;

	public TripIsProcessings() {
		super();
		this.setHflex("1");
		this.setHeight("450px");
		this.init();
		Timer timer = new Timer();
		timer.setParent(this);
		timer.setRepeats(true);
		timer.setDelay(10000);
		timer.addEventListener(Events.ON_TIMER, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (grid != null) {
					grid.setModel(new ListModelList<>(TripManager.LST_TRIP_PROCESSING));
				}
			}
		});
		timer.start();
	}

	private void init() {
		grid = new Grid();
		grid.setParent(this);
		grid.setHflex("1");
		grid.setVflex("1");
		grid.setAutopaging(true);
//		grid.setStyle("overflow: scroll");
		// grid.setSizedByContent(true);
		Columns cols = new Columns();
		cols.setParent(grid);
		cols.setSizable(true);
		Column col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		// col.setWidth("100px");
		col.setLabel("Điện thoại");
		col = new Column();
		col.setParent(cols);
		col.setHflex("4");
		// col.setWidth("200px");
		col.setLabel("Địa chỉ yêu cầu");
		col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		// col.setWidth("100px");
		col.setLabel("Thời gian");
		col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		// col.setWidth("150px");
		col.setLabel("Tài xế");
		col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		// col.setWidth("50px");
		col.setLabel("Số tài");
		col = new Column();
		col.setParent(cols);
		col.setHflex("2");
		// col.setWidth("100px");
		col.setLabel("Trạng thái");
		col = new Column();
		col.setParent(cols);
		col.setHflex("1");
		// col.setWidth("30px");
		col.setLabel("Chi tiết");
		// col = new Column();
		// col.setParent(cols);
		// col.setHflex("2");
		// col.setLabel("Xử lý");
		grid.setRowRenderer(new GridRender());
		grid.setModel(new ListModelList<>(TripManager.LST_TRIP_PROCESSING));
	}

	private class GridRender implements RowRenderer<Trip> {

		@Override
		public void render(Row row, final Trip data, int index) throws Exception {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
			if (data != null && data.getOrder() != null) {
				row.appendChild(new Label(data.getOrder().getPhoneNumber()));
				row.appendChild(new Label(data.getOrder().getBeginOrderAddress()));
				row.appendChild(new Label(sdf.format(data.getOrder().getBeginOrderTime())));
				if (data.getTaxi() != null) {
					row.appendChild(new Label(data.getTaxi().getDriverName()));
					row.appendChild(new Label(data.getTaxi().getVehicle().getValue()));
					// row.appendChild(new
					// Label(data.getTaxi().getLicensePlate()));
				} else {
					row.appendChild(new Label());
					row.appendChild(new Label());
				}
				if (data.getStatus() == EnumStatus.MOI.getValue()) {
					row.appendChild(new Label(EnumStatus.MOI.getLabel()));
				} else if (data.getStatus() == EnumStatus.XE_DANG_KY_DON.getValue()) {
					row.appendChild(new Label(EnumStatus.XE_DANG_KY_DON.getLabel()));
				} else if (data.getStatus() == EnumStatus.XE_DA_DON.getValue()) {
					row.appendChild(new Label(EnumStatus.XE_DA_DON.getLabel()));
				} else if (data.getStatus() == EnumStatus.TRA_KHACH.getValue()) {
					row.appendChild(new Label(EnumStatus.TRA_KHACH.getLabel()));
				} else {
					row.appendChild(new Label(""));
				}
				final Button btnDetail = new Button();
				btnDetail.setTooltiptext("Chi tiết");
				row.appendChild(btnDetail);
				btnDetail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event envent) throws Exception {
						TaxiOrdersForm taxiOrder = Env.getTaxiOrdersWindow();
						if (taxiOrder == null) {
							Env.getHomePage().showFunction("TaxiOrdersForm");
							taxiOrder = Env.getTaxiOrdersWindow();
							if (taxiOrder != null) {
								try {
									if (data.getOrder().getPhoneNumber() != null && data.getOrder().getPhoneNumber().length() > 0) {
										taxiOrder.detailForm_showInforByPhone(data.getOrder().getPhoneNumber());
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}else{
							try {
								if (data.getOrder().getPhoneNumber() != null && data.getOrder().getPhoneNumber().length() > 0) {
									taxiOrder.detailForm_showInforByPhone(data.getOrder().getPhoneNumber());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});

				// Button btnAction = new Button();
				// btnDetail.setTooltiptext("Xử lý");
				// row.appendChild(btnAction);
			}
		}

	}
}
