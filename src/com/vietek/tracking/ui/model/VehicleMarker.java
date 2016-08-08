package com.vietek.tracking.ui.model;

import java.sql.Timestamp;

import com.google.maps.model.LatLng;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;

public class VehicleMarker extends VMarker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String PATH_VEHICLE_ICON = "./themes/images/Vehicles/icon_4seats_cokhach.png";
	public static final String PATH_VEHICLE_BEGIN_TRIP = "./themes/images/path_start.png";
	public static final String PATH_VEHICLE_STOP_TRIP = "./themes/images/path_stop.png";
	public static final String PATH_PARKING_MARKER = "./themes/images/ML20.png";
	public static final String PATH_POINT_STOP = "./themes/images/Place/stop-flag.png";
	public static final String PATH_POINT_START = "./themes/images/Place/start-flag.png";
	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_BEGIN_TRIP = 2;
	public static final int TYPE_STOP_TRIP = 3;
	public static final int TYPE_PARKING_MARKER = 4;
	public static final int TYPE_POINT_STOP = 5;
	public static final int TYPE_POINT_START = 6;
	private boolean isBeginRun = true;
	private Integer rowid;
	private int typemarker;

	public VehicleMarker(GpsTrackingMsg data, int type) {
		super();
		typemarker = type;
		switch (typemarker) {
		case TYPE_NORMAL:
			this.setIconImage(PATH_VEHICLE_ICON);
			break;
		case TYPE_BEGIN_TRIP:
			this.setIconImage(PATH_VEHICLE_BEGIN_TRIP);
			break;
		case TYPE_STOP_TRIP:
			this.setIconImage(PATH_VEHICLE_STOP_TRIP);
			break;
		case TYPE_PARKING_MARKER:
			this.setIconImage(PATH_PARKING_MARKER);
			break;
		case TYPE_POINT_STOP:
			this.setIconImage(PATH_POINT_STOP);
			break;
		case TYPE_POINT_START:
			this.setIconImage(PATH_POINT_START);
			break;
		default:
			this.setIconImage(PATH_VEHICLE_ICON);
			break;
		}
		if (data != null) {
			setData(data);
			setDivConten(data);
		}

	}

	public VehicleMarker() {
		this.setIconImage(PATH_VEHICLE_ICON);
		this.setSclass("gmaker_vehicle");
	}

	public void setLatLng(LatLng latlng) {
		super.setPosition(latlng);
	}

	public String creatNormalContain(GpsTrackingMsg data) {
		String divconten = "<table>"
//	               + "<tr><td class=vgmaker_left>" + "Row:" + "</td>" + "<td class=vgmaker_right>"
//				+ data.getRowid() + "</td></tr>" 
	            + "<tr><td class=vgmaker_left>" + "Vị trí:" + "</td>"
				+ "<td class=vgmaker_right>" + "Chua cap nhat" + "</td></tr>" + "<tr><td class=vgmaker_left>"
				+ "Thời điểm:" + "</td>" + "<td class=vgmaker_right>"
				+ StringUtils.valueOfTimestamp(new Timestamp(data.getTimeLog().getTime())) + "</td></tr>"
				+ "<tr><td class=vgmaker_left>" + "Vận tốc:" + "</td>" + "<td class=vgmaker_right>"
				+ String.valueOf(data.getGpsSepeed()[9]) + " km/h</td></tr>" + "<tr><td class=vgmaker_left>"
				+ "Trạng thái khách:" + "</td>" + "<td class=vgmaker_right>" + data.getInTripStatus() + "</td></tr>"
//				+ "<tr><td class=vgmaker_left>" + "Số giờ hoạt động:" + "</td>" + "<td class=vgmaker_right>"
//				+ StringUtils.MilisToHours(data.getTimeactivity()) + "</td></tr>"

//				+ "<tr><td class=vgmaker_left>" + "Số lần dừng đỗ:" + "</td>" + "<td class=vgmaker_right>"
//				+ String.valueOf(data.getCountStop()) + " Lần</td></tr>"

//				+ "<tr><td class=vgmaker_left>" + "Số giờ dừng đỗ:" + "</td>" + "<td class=vgmaker_right>"
//				+ StringUtils.MilisToHours(data.getTimestop()) + "</td></tr>" 
				+ "<tr><td class=vgmaker_left>"
				+ "Km Trong cuốc:" + "</td>" + "<td class=vgmaker_right>" + StringUtils.doubleFormat(data.getPathTrip())
				+ " km</td></tr>" + "<tr><td class=vgmaker_left>" + "Tiền trong cuốc:" + "</td>"
				+ "<td class=vgmaker_right>" + StringUtils.priceWithoutDecimal(data.getMoneyTrip()) + " VND</td></tr>"

				+ "<tr><td class=vgmaker_left>" + "Tiền ca:" + "</td>" + "<td class=vgmaker_right>"
				+ StringUtils.priceWithoutDecimal(data.getTotalMoneyShift()) + " VND</td></tr>" + "<tr><td class=vgmaker_left>"
				+ "Cuốc trong ca:" + "</td>" + "<td class=vgmaker_right>" + String.valueOf(data.getTotalTrip())
				+ " Cuốc</td></tr>" + "<tr><td class=vgmaker_left>" + "Tổng km khách:" + "</td>"
				+ "<td class=vgmaker_right>" + StringUtils.priceWithoutDecimal(data.getTripPath()) + " Km</td></tr>"
				+ "<tr><td class=vgmaker_left>" + "Tổng km rỗng:" + "</td>" + "<td class=vgmaker_right>"
				+ String.valueOf(data.getEmptyPath()) + " Km</td></tr>" + "<tr><td class=vgmaker_left>"
				+ "Tổng tiền đồng hô:" + "</td>" + "<td class=vgmaker_right>" + StringUtils.priceWithoutDecimal(data.getTotalMoney())
				+ " VND</td></tr>" + "<tr><td colspan=2 align=center></td></tr></table>";
		return divconten;
	}

	private void setDivConten(GpsTrackingMsg data) {
		if (typemarker == TYPE_POINT_STOP) {
			this.setContent(creatGenaralConten(data));
		} else {
			this.setContent(creatNormalContain(data));
		}
	}

	private String creatGenaralConten(GpsTrackingMsg data) {
		String divconten = "<table>" + "<tr><td class=vgmaker_left>" + "Tọa độ:" + "</td>" + "<td class=vgmaker_right>"
				+ data.getLatitude() + "-" + data.getLongitude() + "</td></tr>" + "<tr><td class=vgmaker_left>"
				+ "Vị trí:" + "</td>" + "<td class=vgmaker_right>" + data.getAddress() + "</td></tr>"
				+ "<tr><td class=vgmaker_left>" + "Thời điểm:" + "</td>" + "<td class=vgmaker_right>"
				+ StringUtils.valueOfTimestamp(new Timestamp(data.getTimeLog().getTime())) + "</td></tr>"
				+ "<tr><td class=vgmaker_left>" + "Lần dừng đỗ:" + "</td>" + "<td class=vgmaker_right>"
				+ data.getCountStop() + "Lần" + "</td></tr>" + "<tr><td class=vgmaker_left>" + "Tổng T/g dừng đỗ:"
				+ "</td>" + "<td class=vgmaker_right>" + StringUtils.MilisToHours(data.getTimestop()) + "</td></tr>"
				+ "<tr><td colspan=2 align=center></td></tr></table>";
		return divconten;
	}

	public void setData(GpsTrackingMsg data) {
		this.setLatLng(data.getLatlng());
		setDivConten(data);
		this.setAngle(data.getAngle());
	}

	public Integer getRowid() {
		return rowid;
	}

	public void setRowid(Integer rowid) {
		this.rowid = rowid;
	}

	public boolean isBeginRun() {
		return isBeginRun;
	}

	public void setBeginRun(boolean isBeginRun) {
		this.isBeginRun = isBeginRun;
	}

}