package com.vietek.taxioperation.ui.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.ApplyEditor;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.controller.CommonValueController;
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.CommonValue;
import com.vietek.taxioperation.model.RptQcTruckStdOverSpeed;
import com.vietek.taxioperation.model.RptQcTrunkStdDriving;
import com.vietek.taxioperation.model.RptTripSearchingOnline;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.tracking.ui.utility.TrackingHistory;

public class ItemRenderReport<T> implements ListitemRenderer<T>, EventListener<Event> {
	ArrayList<GridColumn> gridColumns;

	public ItemRenderReport(ArrayList<GridColumn> gridColumns) {
		super();
		this.gridColumns = gridColumns;
	}

	@Override
	public void render(Listitem item, T data, int index) throws Exception {
		item.setValue(data);
		for (int i = 0; i < gridColumns.size(); i++) {
			if (i == 0) {
				item.appendChild(new Listcell("" + ++index));
			} else {
				GridColumn column = gridColumns.get(i);
				if (data != null && column.getGetDataMethod() != null) {
					Method method = data.getClass().getMethod(column.getGetDataMethod());
					Object val = method.invoke(data);

					if (column.getFieldName() != null && column.getFieldName().length() > 0) {
						Field field = column.getModelClazz().getDeclaredField(column.getFieldName());
						Annotation[] ann = field.getAnnotations();
						if (ann.length > 0) {
						if(ann[0].annotationType().equals(ApplyEditor.class)){
							Textbox txt1 = new Textbox();
//							Textbox txt2 = new Textbox();
							Label lbPriceTrip = new Label("");
							ApplyEditor annAppEditor = (ApplyEditor)ann[0];
							if(annAppEditor.classNameEditor().equals("Textbox1")){
							
							this.createCellFixedTextbox(item, data, txt1, lbPriceTrip);
							continue;
							} else if(annAppEditor.classNameEditor().equals("Textbox2")){
								// Tao 1 ham cho PriceTrip
								this.createCellFixedTextbox2(item, data, lbPriceTrip);
								continue;
							}
						} else if(ann[0].annotationType().equals(FixedCombobox.class)){
							this.createCellFixedCombobox(item, data);
							continue;
						}
						}
						
						// Create components in listcell
//						if(ann.length > 0){
//							if(ann[0].annotationType().equals(ReportEditor.class)){
//								ReportEditor annTemp = (ReportEditor)ann[0];
//								if(annTemp.nameEditor().equals("Textbox")){
//									this.createCellFixedTextbox(item, data);
//									continue;
//								} else if(annTemp.nameEditor().equals("Combobox")){
//									this.createCellFixedCombobox(item, data);
//									continue;
//								}
//							}
//						}
					}
					if (val instanceof Timestamp) {
						SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
						item.appendChild(new Listcell(dateformat.format(val)));
					} else if (column.getClazz().equals(Boolean.class) && column.getFieldName().equals("isHistory")) {
						Button btnHistory = new Button("Lịch sử");
						btnHistory.setStyle("color : black;font-size : 12px");
						btnHistory.setHeight("25px");
						btnHistory.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

							@Override
							public void onEvent(Event arg0) throws Exception {
								showHistory(data);
							}
						});
						Listcell lstCell = new Listcell();
						lstCell.setParent(item);
						lstCell.setStyle("text-align: center");
						lstCell.appendChild(btnHistory);
					} else {
						item.appendChild(new Listcell(val == null ? "" : val.toString()));
					}
				}
			}
		}
	}

	private void createCellFixedCombobox(Listitem item, T data) {
		if (data == null) {
			item.appendChild(new Listcell(data == null ? "" : data.toString()));
		} else {
			if (data.getClass().getTypeName() == RptTripSearchingOnline.class.getTypeName()) {
				RptTripSearchingOnline temp = RptTripSearchingOnline.class.cast(data);
				Combobox comboReason = createCboInputReason(temp);
				Listcell listCellReason = new Listcell();
				listCellReason.setParent(item);
				comboReason.setParent(listCellReason);
				comboReason.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						Comboitem item = comboReason.getSelectedItem();
						RptTripSearchingOnline dataTemp = (RptTripSearchingOnline) data;
						dataTemp.setReason(item.getValue());
						updateTripSearchingOnline(dataTemp.getShiftId(), dataTemp.getTripId(), dataTemp.getPriceTrip(),
								dataTemp.getReason(), "admin");
						AppLogger.logDebug.info("Đã update trong Combobox");
					}
				});
			} else {
				item.appendChild(new Listcell("Chọn lý do.."));
			}
		}
	}
	
	private void createCellFixedTextbox(Listitem item, T data, Textbox txt1, Label lb){
		if (data == null) {
			item.appendChild(new Listcell(data == null ? "" : data.toString()));
		} else {
			if (data.getClass().getTypeName() == RptTripSearchingOnline.class.getTypeName()) {
				RptTripSearchingOnline temp = RptTripSearchingOnline.class.cast(data);
				txt1.setValue("" + temp.getReduce());
//				Textbox textboxTemp = new Textbox(""+ temp.getReduce());
				txt1.setWidth("90%");
				Listcell listCellReduce = new Listcell();
				listCellReduce.setParent(item);
				txt1.setParent(listCellReduce);
				
//				Label lbPriceTrip = new Label("" + temp.getPriceTrip());
//				Listcell listCellPriceTrip = new Listcell();
//				lbPriceTrip.setParent(listCellReduce);
//				lbPriceTrip.setParent(listCellPriceTrip);
				
				txt1.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						RptTripSearchingOnline dataTemp = (RptTripSearchingOnline) data;
						dataTemp.setPriceTrip(dataTemp.getClock() - Integer.parseInt(txt1.getValue()));
						lb.setValue("" + dataTemp.getPriceTrip());
						AppLogger.logDebug.info("Nhan lb co gia tri : " + lb.getValue());
						updateTripSearchingOnline(dataTemp.getShiftId(), dataTemp.getTripId(), dataTemp.getPriceTrip(),
								dataTemp.getReason(), "admin");
						AppLogger.logDebug.info("Da update trong textbox");
					}
				});
				txt1.addForward(Events.ON_CHANGE, txt1, Events.ON_OK);
			} else {
				item.appendChild(new Listcell("Khong giam tru.."));
			}
		}
	}
	
	private void createCellFixedTextbox2(Listitem item, T data, Label lb){
		if (data == null) {
			item.appendChild(new Listcell(data == null ? "" : data.toString()));
		} else {
			if (data.getClass().getTypeName() == RptTripSearchingOnline.class.getTypeName()) {
				RptTripSearchingOnline temp = RptTripSearchingOnline.class.cast(data);
				lb.setValue("" + temp.getPriceTrip());
//				Textbox textboxTemp2 = new Textbox(""+ temp.getReduce());
//				txt2.setWidth("90%");
				Listcell listCellPriceTrip = new Listcell();
				listCellPriceTrip.setParent(item);
				lb.setParent(listCellPriceTrip);
//				txt2.addEventListener(Events., listener)
			} else {
				item.appendChild(new Listcell("Khong co gia cuoc.."));
			}
		}
	}

	private void showHistory(T data) {
		TrackingHistory history = null;
		if (data.getClass().getTypeName() == RptQcTrunkStdDriving.class.getTypeName()) {
			RptQcTrunkStdDriving r1 = RptQcTrunkStdDriving.class.cast(data);
			history = new TrackingHistory(new java.sql.Date(r1.getBeginTime().getTime()),
					new java.sql.Date(r1.getEndTime().getTime()), r1.getVehicleId());
		} else if (data.getClass().getTypeName() == RptQcTruckStdOverSpeed.class.getTypeName()) {
			RptQcTruckStdOverSpeed r1 = RptQcTruckStdOverSpeed.class.cast(data);
			history = new TrackingHistory(new java.sql.Date(r1.getTimeStart().getTime()),
					new java.sql.Date(r1.getTimeStop().getTime()), r1.getVehicleId());
		}
		if (history != null) {
			Window window = new Window();
			window.setWidth("100%");
			window.setHeight("100%");
			window.setClosable(true);
			window.setTitle("Lịch sử cuốc chi tiết");
			window.setParent(Env.getHomePage().getDivTab());
			window.appendChild(history);
			window.doModal();
		}
	}

	// Viet ham tao combobox tai day
	private Combobox createCboInputReason(RptTripSearchingOnline data) {

		List<CommonValue> lstcommonvalue = getCommonValue("REDUCEMONEY");
		HashMap<String, String> mapValue = new HashMap<String, String>();
		for (CommonValue common : lstcommonvalue) {
			mapValue.put(common.getCode(), common.getName());
		}
		Combobox cboinput = new Combobox();
		ComboboxRender renderCbo = new ComboboxRender();
		cboinput = renderCbo.ComboboxRendering(mapValue, "", "", 120, 8, data.getReason(), false);
		cboinput.setInplace(true);
		cboinput.setSclass("comboboxtripsearching");
		return cboinput;

	}

	private List<CommonValue> getCommonValue(String codetype) {
		CommonValueController controller = (CommonValueController) ControllerUtils
				.getController(CommonValueController.class);
		String sql = "from CommonValue where codetype = ?";
		List<CommonValue> lstvalue = controller.find(sql, codetype);
		if (lstvalue == null || lstvalue.size() > 0)
			return lstvalue;
		else
			return null;
	}
	// Refresh textbox
	private void refreshTextBox(String value, Textbox txt2){
		txt2.setValue(value);
	}
	
	// Ham update TripSearchingOnlines
	private void updateTripSearchingOnline(int shiftId, int tripId, int cash, int reason, String userId) {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		lstObj.updateTripSearchingOnline(shiftId, tripId, cash, reason, userId);
		return;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
