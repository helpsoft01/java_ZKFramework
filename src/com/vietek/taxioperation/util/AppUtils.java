package com.vietek.taxioperation.util;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.List;

import com.vietek.taxioperation.controller.ConfigController;
import com.vietek.taxioperation.controller.TablePriceController;
import com.vietek.taxioperation.controller.VehicleController;
import com.vietek.taxioperation.model.Config;
import com.vietek.taxioperation.model.TablePrice;
import com.vietek.taxioperation.model.TaxiType;
import com.vietek.taxioperation.model.TypeTablePrice;
import com.vietek.taxioperation.model.Vehicle;

/**
 * 
 * @author VuD
 * 
 */

public class AppUtils {

	/**
	 * Ban kinh trai dat; R = 6372800 m;
	 */
	public static final double R = 6372.8 * 1000;

	/**
	 * Tinh khoang cach hai diem tren he toa do cau
	 *
	 * @author VuD
	 * @param lon1
	 * @param lat1
	 * @param lon2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double lon1, double lat1, double lon2,
			double lat2) {
		double cosd = sin(lat1 * PI / (double) 180)
				* sin(lat2 * PI / (double) 180) + cos(lat1 * PI / (double) 180)
				* cos(lat2 * PI / (double) 180)
				* cos((lon1 - lon2) * PI / (double) 180);
		if (cosd >= 1) {
			return 0.0;
		}
		double d = acos(cosd);
		double distance = d * R;
		return distance;
	}
	
	/**
	 * Tinh gia tien cho khach dat xe di gia
	 * @author Habv
	 * @param vehicleID - Ma xe di gia
	 * @param distance - Doan duong di
	 * @param time - Thoi gian di
	 * @param km - Km dinh muc
	 * @param type - 1 or 2 (Khach di 1 chieu hoac 2 chieu)
	 * @return
	 */
	public static double DiGia(int vehicleID, int distance, int time, int km, int type){
		double result = 0;
		TablePrice tablePrice = getTablePrice(vehicleID, km);
		TypeTablePrice typeTablePrice = tablePrice.getTypeTablePrice();
		ConfigController configController = (ConfigController) ControllerUtils.getController(ConfigController.class);
		Config config = configController.find(1, "from Config WHERE note like ?", "EXTRA_KM").get(0);
		int extKm = Integer.parseInt(config.getValue());
		config = configController.find(1, "from Config WHERE note like ?", "EXTRA_TIME").get(0);
		int extTime = Integer.parseInt(config.getValue());
		double pricePerTime = typeTablePrice.getPricePerExtTime();
		double pricePerKm = typeTablePrice.getPricePerKm();
		
		// Lay thoi gian/quang duong qua han cho phep
		if(tablePrice != null){
			if(type == 1) {	// Di 1 chieu
				int overKm = distance - tablePrice.getKm();
				result = tablePrice.getPrice1c();
				int overTime = time - tablePrice.getTime1c();
				if(overKm - extKm > 0){		// Qua km
					if(overTime - extTime <= 0){	// Khong qua thoi gian
						result += (overKm * pricePerKm);
					} else {
						result += (overKm * pricePerKm);
						result += (overTime * pricePerTime);
					}
				} else { // Khong qua km
					if(overTime - extTime > 0){	// Qua thoi gian
						result += (overTime * pricePerTime);
					} 
				}
			} 
			else {	// Di 2 chieu
				int overKm = distance - (tablePrice.getKm() * 2);
				result = tablePrice.getPrice2c();
				int overTime = time - tablePrice.getTime2c();
				if(overKm - extKm > 0){		// Qua km
					if(overTime - extTime <= 0){	// Khong qua thoi gian
						result += (overKm * pricePerKm);
					} else {
						result += (overKm * pricePerKm);
						result += (overTime * pricePerTime);
					}
				} else { // Khong qua km
					if(overTime - extTime > 0){	// Qua thoi gian
						result += (overTime * pricePerTime);
					} 
				}
			}
		}
		
		return result;
	}
	
	private static TablePrice getTablePrice(int vehicleID, int km) {
		TablePrice tablePrice = null;
		VehicleController vehicleController = (VehicleController) ControllerUtils.getController(VehicleController.class);
		Vehicle vehicle = vehicleController.get(Vehicle.class, vehicleID);
		TaxiType taxiType = vehicle.getTaxiType();
		TypeTablePrice typeTablePrice = taxiType.getTypeTablePrice();
		TablePriceController tablePriceController = (TablePriceController) ControllerUtils.getController(TablePriceController.class);
		
		//Lay danh sach bang gia thuoc loai bang gia va so km gan bang doan duong
		List<TablePrice> tablePrices = tablePriceController.find("from TablePrice WHERE typeTablePrice NOT NULL AND typeTablePrice=? AND km = ?", typeTablePrice, km);
		for (TablePrice _tablePrice : tablePrices) {
			if(System.currentTimeMillis() - _tablePrice.getFromDate().getTime() >=0 && _tablePrice.getToDate().getTime() - System.currentTimeMillis()>=0){
				tablePrice = _tablePrice;
				break;
			}
		}
		return tablePrice;
	}

}
