package com.vietek.taxioperation.model;

import java.sql.Timestamp;

import com.vietek.taxioperation.common.Searchable;

public class RptTotalBussinessAgent extends ReportModel {
	
	private int rptId;
	@Searchable
	private String agentName;
	@Searchable
	private String agentGroup;
	@Searchable
	private String phone;
	@Searchable
	private String fax;
	@Searchable
	private int totalVehicle;
	@Searchable
	private int totalActiveVehicle;
	@Searchable
	private int totalFinishVehicle;
	@Searchable
	private int totalDuplicateVehicle;
	@Searchable
	private int totalOnlineVehicle;
	@Searchable
	private int money;
	@Searchable
	private int avgMoney;
	@Searchable
	private double percent;
	@Searchable
	private int inWorkShop;
	@Searchable
	private int inAccident;
	@Searchable
	private Timestamp timeLog;
	
	public int getRptId() {
		return rptId;
	}
	public void setRptId(int rptId) {
		this.rptId = rptId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentGroup() {
		return agentGroup;
	}
	public void setAgentGroup(String agentGroup) {
		this.agentGroup = agentGroup;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public int getTotalVehicle() {
		return totalVehicle;
	}
	public void setTotalVehicle(int totalVehicle) {
		this.totalVehicle = totalVehicle;
	}
	public int getTotalActiveVehicle() {
		return totalActiveVehicle;
	}
	public void setTotalActiveVehicle(int totalActiveVehicle) {
		this.totalActiveVehicle = totalActiveVehicle;
	}
	public int getTotalFinishVehicle() {
		return totalFinishVehicle;
	}
	public void setTotalFinishVehicle(int totalFinishVehicle) {
		this.totalFinishVehicle = totalFinishVehicle;
	}
	public int getTotalDuplicateVehicle() {
		return totalDuplicateVehicle;
	}
	public void setTotalDuplicateVehicle(int totalDuplicateVehicle) {
		this.totalDuplicateVehicle = totalDuplicateVehicle;
	}
	public int getTotalOnlineVehicle() {
		return totalOnlineVehicle;
	}
	public void setTotalOnlineVehicle(int totalOnlineVehicle) {
		this.totalOnlineVehicle = totalOnlineVehicle;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getAvgMoney() {
		return avgMoney;
	}
	public void setAvgMoney(int avgMoney) {
		this.avgMoney = avgMoney;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public int getInWorkShop() {
		return inWorkShop;
	}
	public void setInWorkShop(int inWorkShop) {
		this.inWorkShop = inWorkShop;
	}
	public int getInAccident() {
		return inAccident;
	}
	public void setInAccident(int inAccident) {
		this.inAccident = inAccident;
	}
	public Timestamp getTimeLog() {
		return timeLog;
	}
	public void setTimeLog(Timestamp timeLog) {
		this.timeLog = timeLog;
	}
	
	

}
