package com.vietek.taxioperation.googlemapSearch;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import com.vietek.taxioperation.webservice.TaxiResult;

public class DistanceBetweenTwoPoints_Run implements Runnable {

	Desktop desktop;
	Component ctrShow;
	List<TaxiResult> lstVehicle;
	String destination_LatLong;

	public DistanceBetweenTwoPoints_Run(List<TaxiResult> lstVehicle, String destination_LatLong, Component ctrShow,
			Desktop desktop) {
		// TODO Auto-generated constructor stub
		this.lstVehicle = lstVehicle;
		this.destination_LatLong = destination_LatLong;
		this.ctrShow = ctrShow;
		this.desktop = desktop;

	}

	@Override
	public void run() {
		try {
			Div div = ((Div) ctrShow);
			
			if (!desktop.isServerPushEnabled()) {
				desktop.enableServerPush(true);
			}
			Executions.schedule(desktop, new EventListener<Event>() {

				@Override
				public void onEvent(Event arg0) throws Exception {
					div.getChildren().clear();
					Label lbHeader = new Label(lstVehicle.size() + " XE - VỪA ĐĂNG KÝ ĐÓN");
					lbHeader.setParent(div);
					lbHeader.setSclass("taxiOrder2lbHeader");
				}
			}, null);


			DistanceBetweenTwoAddress distanceBetweenTwoAddress = new DistanceBetweenTwoAddress();
			int i = 0;
			for (TaxiResult taxiResult : lstVehicle) {
				Div divChild = new Div();
				divChild.setId("divChildInforDistanceBetween" + i++);
				divChild.setSclass("childDistanceRegister");

				String origins_Latlong = taxiResult.getLatitude().toString() + ","
						+ taxiResult.getLongitude().toString();
				
				distanceBetweenTwoAddress.getDistance(origins_Latlong, destination_LatLong);
				Executions.schedule(desktop, new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						Label lbVehicleStr = new Label();
						lbVehicleStr.setParent(divChild);
						lbVehicleStr.setValue(" - Xe: ");

						Label lbVehicleVal = new Label();
						lbVehicleVal.setParent(divChild);
						lbVehicleVal.setSclass("taxiOrder2lbVehicleVal");
						lbVehicleVal.setValue(taxiResult.getVehicleNumber());

						Label lbDistanceStr = new Label();
						lbDistanceStr.setParent(divChild);
						lbDistanceStr.setValue(", Cách: ");

						Label lbDistanceVal = new Label();
						lbDistanceVal.setParent(divChild);
						lbDistanceVal.setSclass("taxiOrder2lbDistanceVal");
						lbDistanceVal.setValue(distanceBetweenTwoAddress.strDistance);

						Label lbDurationStr = new Label();
						lbDurationStr.setParent(divChild);
						lbDurationStr.setValue(", Thời gian: ");

						Label lbDurationVal = new Label();
						lbDurationVal.setParent(divChild);
						lbDurationVal.setSclass("taxiOrder2lbDurationVal");
						lbDurationVal.setValue(distanceBetweenTwoAddress.strDuration);

						div.appendChild(divChild);
					}
				}, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Executions.schedule(desktop, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					Clients.clearBusy(ctrShow);
				}
			}, null);
		}
	}
}
