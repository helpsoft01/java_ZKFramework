package com.vietek.taxioperation.model;

import java.util.Date;

public class StatisticTmsActivityModel {
	public int OrderId;
	public Date OrderTime;
	public String OrderPhone;
	public String OrderName;
	public String Telephonlist;
	public String Chanel;
	public String OrderAddress;
	public String BeginTripAddress;
	public String EndTripAddress;
	public String RegisterVehicles;
	public String AcceptVehicle;
	public int VehicleId;
	public Double KmInTrip;
	public int MoneyInTrip;
	public String TripType;	

	public StatisticTmsActivityModel(){
		OrderId = 0;
		OrderTime = new Date();
		OrderPhone = "";
		OrderName = "";
		Telephonlist = "";
		Chanel = "";
		OrderAddress = "";
		BeginTripAddress = "";
		EndTripAddress = "";
		RegisterVehicles = "";
		AcceptVehicle = "";
		VehicleId = 0;
		KmInTrip = (double) 0;
		MoneyInTrip = 0;
		TripType = "";
	}
	
	public int getOrderId() {
		return OrderId;
	}
	public void setOrderId(int orderId) {
		OrderId = orderId;
	}
	public Date getOrderTime() {
		return OrderTime;
	}
	public void setOrderTime(Date orderTime) {
		OrderTime = orderTime;
	}
	public String getOrderPhone() {
		return OrderPhone;
	}
	public void setOrderPhone(String orderPhone) {
		OrderPhone = orderPhone;
	}
	public String getOrderName() {
		return OrderName;
	}
	public void setOrderName(String orderName) {
		OrderName = orderName;
	}
	public String getTelephonlist() {
		return Telephonlist;
	}
	public void setTelephonlist(String telephonlist) {
		Telephonlist = telephonlist;
	}
	public String getChanel() {
		return Chanel;
	}
	public void setChanel(String chanel) {
		Chanel = chanel;
	}
	public String getOrderAddress() {
		return OrderAddress;
	}
	public void setOrderAddress(String orderAddress) {
		OrderAddress = orderAddress;
	}
	public String getBeginTripAddress() {
		return BeginTripAddress;
	}
	public void setBeginTripAddress(String beginTripAddress) {
		BeginTripAddress = beginTripAddress;
	}
	public String getEndTripAddress() {
		return EndTripAddress;
	}
	public void setEndTripAddress(String endTripAddress) {
		EndTripAddress = endTripAddress;
	}
	public String getRegisterVehicles() {
		return RegisterVehicles;
	}
	public void setRegisterVehicles(String registerVehicles) {
		RegisterVehicles = registerVehicles;
	}
	public String getAcceptVehicle() {
		return AcceptVehicle;
	}
	public void setAcceptVehicle(String acceptVehicle) {
		AcceptVehicle = acceptVehicle;
	}
	public int getVehicleId() {
		return VehicleId;
	}
	public void setVehicleId(int vehicleId) {
		VehicleId = vehicleId;
	}
	public Double getKmInTrip() {
		return KmInTrip;
	}
	public void setKmInTrip(Double kmInTrip) {
		KmInTrip = kmInTrip;
	}
	public int getMoneyInTrip() {
		return MoneyInTrip;
	}
	public void setMoneyInTrip(int moneyInTrip) {
		MoneyInTrip = moneyInTrip;
	}
	public String getTripType() {
		return TripType;
	}
	public void setTripType(String tripType) {
		TripType = tripType;
	}	
}
