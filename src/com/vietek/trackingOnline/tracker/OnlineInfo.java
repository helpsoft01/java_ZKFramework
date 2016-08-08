package com.vietek.trackingOnline.tracker;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.zul.Image;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.MapCommon;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.GPSOnlineController;
import com.vietek.taxioperation.controller.GPSOnlineParamsController;
import com.vietek.taxioperation.model.GPSOnline;
import com.vietek.taxioperation.model.GPSOnlineParams;
import com.vietek.taxioperation.ui.controller.vmap.VMarker;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.MapUtils;

public class OnlineInfo {
	private GPSOnline gpsOnline;
	private VMarker marker;
	private TrackingRDS2Json rds2Json;
	private GPSOnlineParams gpsOnlineParams;
	private String content;

	public OnlineInfo(int deviceId, VMarker marker) {
		this.gpsOnline = getGPSOnline(deviceId);
		this.marker = marker;
		this.rds2Json = MapCommon.TRACKING_RDS.get(deviceId);
		GPSOnlineParamsController gpsOnlineParamsController = (GPSOnlineParamsController) ControllerUtils
				.getController(GPSOnlineParamsController.class);
		gpsOnlineParams = gpsOnlineParamsController.get(GPSOnlineParams.class, rds2Json.getDeviceId());
		setContent();
	}

	private GPSOnline getGPSOnline(int deviceId) {
		GPSOnline online = null;
		try {
			GPSOnlineController controller = (GPSOnlineController) ControllerUtils
					.getController(GPSOnlineController.class);
			online = controller.find("from GPSOnline where deviceID=?", deviceId).get(0);

		} catch (Exception e) {
			e.printStackTrace();
			AppLogger.logTracking.error("Loi tracking", e);
		}
		return online;
	}

	public void setContent() {
		StringBuffer strContent = new StringBuffer();

		strContent.append("<div class=tab_panel><ul class=tab_link>");
		strContent.append("<li id=" + this.marker.getId() + "_tab1><a onclick=clickTab(&#39;" + this.marker.getId()
				+ "&#39;,1)>Hiện trạng</a></li>");
		strContent.append("<li id=" + this.marker.getId() + "_tab2><a onclick=clickTab(&#39;" + this.marker.getId()
				+ "&#39;,2)>BGT</a></li>");
		strContent.append("<li id=" + this.marker.getId() + "_tab3><a onclick=clickTab(&#39;" + this.marker.getId()
				+ "&#39;,3)>Thiết bị</a></li>");
		strContent.append("<li id=" + this.marker.getId() + "_tab4><a onclick=clickTab(&#39;" + this.marker.getId()
				+ "&#39;,4)>Doanh thu</a></li></ul>");
		strContent.append("<div class=content_area><div id=" + this.marker.getId() + "_cont1 class=active>");
		strContent.append(trackingInfo()).append("</div>");
		strContent.append("<div id=" + this.marker.getId() + "_cont2 class=inactive>");
		strContent.append(driverInfo()).append("</div>");
		strContent.append("<div id=" + this.marker.getId() + "_cont3 class=inactive>");
		strContent.append(deviceInfo()).append("</div>");
		strContent.append("<div id=" + this.marker.getId() + "_cont4 class=inactive>");
		strContent.append(doanhThu()).append("</div></div></div>");
		content = strContent.toString();
	}

	public String getContent() {
		return content;
	}
	
	String label;
	public String getMarkerLabel(){
		return label;
	}

	private String trackingInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<table><tr><td class=markerOnline_left>BKS</td><td class=markerOnline_right>");
		if (rds2Json.getVehicleNumber() != null) {
			String taxiNumber = marker.getLabel();
			label = taxiNumber;
			info.append(taxiNumber);
		}

		info.append("</td></tr><tr><td class=markerOnline_left>Vị trí</td><td class=markerOnline_right>");
		if (rds2Json.getLatitude() != 0.0 || rds2Json.getLongitude() != 0.0) {
			String address = MapUtils.convertLatLongToAddrest(rds2Json.getLatitude(), rds2Json.getLongitude());
			info.append(address);
		} else {
			if (gpsOnline.getLat() != 0 || gpsOnline.getLng() != 0) {
				String address = MapUtils.convertLatLongToAddrest(gpsOnline.getLat(), gpsOnline.getLng());
				info.append(address);
			}
		}

		info.append("</td></tr><tr><td class=markerOnline_left>Thời điểm</td><td class=markerOnline_right>");
		if (rds2Json.getTimeLog() != null) {
			info.append(StringUtils.valueOfTimestamp(rds2Json.getTimeLog()));
		} else if (gpsOnline.getTimeLog() != null) {
			info.append(StringUtils.valueOfTimestamp(gpsOnline.getTimeLog()));
		}

		info.append("</td></tr><tr><td class=markerOnline_left>V.GPS-V.Cơ</td><td class=markerOnline_right>");
		info.append(rds2Json.getGpsSpeed()).append(" - ").append(rds2Json.getMetterSpeed());
		info.append("</td></tr><tr><td class=markerOnline_left>TT máy bật/ tắt</td><td class=markerOnline_right>");

		if (rds2Json.getGpsSpeed() < 5)
			info.append("Xe dừng/đỗ");
		else {
			info.append("Xe chạy");
		}
		info.append("</td></tr><tr><td class=markerOnline_left>Động cơ</td><td class=markerOnline_right>");
		if(gpsOnline.getEngine()!=null){
			if (gpsOnline.getEngine() == 1)
				info.append("Bật");
			else
				info.append("Tắt");
		}

		info.append("</td></tr><tr><td class=markerOnline_left>Điều hòa</td><td class=markerOnline_right>");
		if (gpsOnline.getAirConditioner() == 0)
			info.append("Bật");
		else
			info.append("Tắt");
		info.append("</td></tr></table>");
		return info.toString();
	}

	private String driverInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<table><tr><td class=markerOnline_left>Họ tên lái xe</td><td class=markerOnline_right>")
				.append(gpsOnlineParams.getDriverName());
		info.append("</td></tr><tr><td class=markerOnline_left>Điện thoại</td><td class=markerOnline_right>")
				.append(gpsOnlineParams.getMobileNumber());
		info.append("</td></tr><tr><td class=markerOnline_left>Số GPLX</td><td class=markerOnline_right>")
				.append(gpsOnlineParams.getRegisterNumber());
		info.append("</td></tr><tr><td class=markerOnline_left>Số lần vượt tốc</td><td class=markerOnline_right>")
				.append(gpsOnline.getSpeedTimeCounting());
		info.append("</td></tr><tr><td class=markerOnline_left>L.Xe trong ngày</td><td class=markerOnline_right>")
				.append(gpsOnline.getTimeDrivingPerday());
		info.append("</td></tr><tr><td class=markerOnline_left>L.Xe liên tục</td><td class=markerOnline_right>")
				.append(gpsOnline.getTimeDrivingContinuous());
		info.append("</td></tr><tr><td class=markerOnline_left>Q.đường trong ngày</td><td class=markerOnline_right>")
				.append(gpsOnline.getPathPerday());
		info.append("</td></tr></table>");
		return info.toString();//
	}

	private String deviceInfo() {
		StringBuffer info = new StringBuffer();
		info.append("<table><tr><td class=markerOnline_left>Device ID</td><td class=markerOnline_right>")
				.append(gpsOnline.getDeviceID());
		info.append("</td></tr><tr><td class=markerOnline_left>AcQuy</td><td class=markerOnline_right>")
				.append(gpsOnline.getAcquyPower());
		info.append("</td></tr><tr><td class=markerOnline_left>Pin</td><td class=markerOnline_right>")
				.append(gpsOnline.getPinPower());
		info.append("</td></tr><tr><td class=markerOnline_left>SDCard</td><td class=markerOnline_right>")
				.append(gpsOnline.getSDCard());
		info.append("</td></tr><tr><td class=markerOnline_left>Sim</td><td class=markerOnline_right>")
				.append(getSimNumber(gpsOnline.getDeviceID()));
		info.append("</td></tr></table>");
		return info.toString();
	}
	
	private String doanhThu(){
		StringBuffer info = new StringBuffer();
		info.append("<table><tr><td class=markerOnline_left>Doanh thu ca</td><td class=markerOnline_right>");
		info.append(StringUtils.priceWithoutDecimal(gpsOnline.getTotalMoneyShift()*1000)).append(" VNĐ");
		info.append("</td></tr><tr><td class=markerOnline_left>Tổng doanh thu</td><td class=markerOnline_right>");
		info.append(StringUtils.priceWithoutDecimal(gpsOnline.getTotalMoney()*1000)).append(" VNĐ</td></tr></table>");
		
		return info.toString();
	}

	private String getSimNumber(int deviceID) {
		String simNumber = "";
		Session session = ControllerUtils.getCurrentSession();
		try {
			Query query = session.createQuery("select SimNumber from Device where id=" + deviceID + "");
			simNumber = query.list().get(0).toString();
		} catch (Exception ex) {
			simNumber = "Khong xac dinh";
		} finally {
			session.close();
		}

		return simNumber;
	}

	public static TrackingVehicleInfo getTrackingInfo(TrackingRDS2Json json) {
		TrackingVehicleInfo info = new TrackingVehicleInfo();
		long diffTime = (System.currentTimeMillis() - json.getTimeLog().getTime()) / 1000 / 60; // tinh
																								// phut
		if (diffTime >= 4320) { // Mat tin hieu 3 ngay tro len
			info.setImageSrc(CommonDefine.TaxiIcon.ICON_MAINTAIN_16PX);
			info.setImage(new Image(CommonDefine.TaxiIcon.ICON_MAINTAIN_16PX));
			info.setLostDigital(true);
		} else {
			// Mat gsm
			if ((diffTime >= 15 && json.getGpsSpeed() > 10) || (diffTime >= 30 && json.getGpsSpeed() <= 10)) {
				info.setImageSrc(CommonDefine.TaxiIcon.ICON_LOST_GSM);
				info.setImage(new Image(CommonDefine.TaxiIcon.ICON_LOST_GSM));
				info.setLostDigital(true);
			} else {
				// Mat gps
				if (json.getLatitude() <= 0 && json.getLongitude() <= 0) {
					info.setImageSrc(CommonDefine.TaxiIcon.ICON_LOST_GPS);
					info.setImage(new Image(CommonDefine.TaxiIcon.ICON_LOST_GPS));
					info.setLostDigital(true);
				} else { // Tin hieu bt
					info.setLostDigital(false);
					if (json.getCarType() == 1) {
						if (json.getGpsSpeed() > 5) {
							if (json.getInTrip() == 0) {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_4SEATS_NON_PROCESSING);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_4SEATS_NON_PROCESSING));
							} else {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_4SEATS_PROCESSING);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_4SEATS_PROCESSING));
							}
						} else {
							if (json.getEngine() == 0) {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_4SEATS_STOP);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_4SEATS_STOP));
							} else {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_4SEATS_PAUSE);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_4SEATS_PAUSE));
							}
						}
					} else {
						if (json.getGpsSpeed() > 5) {
							if (json.getInTrip() == 0) {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_7SEATS_NON_PROCESSING);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_7SEATS_NON_PROCESSING));
							} else {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_7SEATS_PROCESSING);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_7SEATS_PROCESSING));
							}
						} else {
							if (json.getEngine() == 0) {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_7SEATS_STOP);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_7SEATS_STOP));
							} else {
								info.setImageSrc(CommonDefine.TaxiIcon.ICON_7SEATS_PAUSE);
								info.setImage(new Image(CommonDefine.TaxiIcon.ICON_7SEATS_PAUSE));
							}
						}
					}
				}
			}
		}
		return info;
	}

	public static boolean isLostDigital(TrackingRDS2Json json) {
		boolean isLostDiagital = false;

		return isLostDiagital;
	}

	public static String getImageSrc(TrackingRDS2Json json) {
		String strImg = "";
		long diffTime = (System.currentTimeMillis() - json.getTimeLog().getTime()) / 1000 / 60; // tinh
																								// phut
		if (diffTime >= 4320) { // Mat tin hieu 3 ngay tro len
			strImg = CommonDefine.TaxiIcon.ICON_MAINTAIN;
		} else {
			// Mat gsm
			if ((diffTime >= 15 && json.getGpsSpeed() > 10) || (diffTime >= 30 && json.getGpsSpeed() <= 10)) {
				strImg = CommonDefine.TaxiIcon.ICON_LOST_GSM;
			} else {
				// Mat gps
				if (json.getLatitude() <= 0 && json.getLongitude() <= 0) {
					strImg = CommonDefine.TaxiIcon.ICON_LOST_GPS;
				} else { // Tin hieu bt
					if (json.getCarType() == 1) {
						if (json.getInTrip() == 0) {
							strImg = CommonDefine.TaxiIcon.ICON_4SEATS_NON_PROCESSING;
						} else
							strImg = CommonDefine.TaxiIcon.ICON_4SEATS_PROCESSING;
					} else {
						if (json.getInTrip() == 0) {
							strImg = CommonDefine.TaxiIcon.ICON_7SEATS_NON_PROCESSING;
						} else {
							strImg = CommonDefine.TaxiIcon.ICON_7SEATS_PROCESSING;
						}
					}
				}
			}
		}
		return strImg;
	}
}
