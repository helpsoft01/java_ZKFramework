package com.vietek.tracking.ui.model;

import java.util.ArrayList;
import java.util.List;

public class GenaralValue {

	private Integer sodong = 0;
	private Double Vtrungbinh = 0.0;
	private Double Vmin = 0.0;
	private Double Vmax = 0.0;
	private Double Kmvandanh = 0.0;
	private long sogiohoatdong = 0;
	private long sogiodungdo = 0;
	private int solandungdo = 0;
	private int solanchaylientuc = 0;
	private int solanvuottoc = 0;
	private int solanqua4h = 0;
	private List<GpsTrackingMsg> pointsStop;
	public GenaralValue() {
		pointsStop = new ArrayList<GpsTrackingMsg>();
	}
	public Integer getSodong() {
		return sodong;
	}
	public void setSodong(Integer sodong) {
		this.sodong = sodong;
	}
	public Double getVtrungbinh() {
		return Vtrungbinh;
	}
	public void setVtrungbinh(Double vtrungbinh) {
		Vtrungbinh = vtrungbinh;
	}
	public Double getVmin() {
		return Vmin;
	}
	public void setVmin(Double vmin) {
		Vmin = vmin;
	}
	public Double getVmax() {
		return Vmax;
	}
	public void setVmax(Double vmax) {
		Vmax = vmax;
	}
	public Double getKmvandanh() {
		return Kmvandanh;
	}
	public void setKmvandanh(Double kmvandanh) {
		Kmvandanh = kmvandanh;
	}
	public long getSogiohoatdong() {
		return sogiohoatdong;
	}
	public void setSogiohoatdong(long sogiohoatdong) {
		this.sogiohoatdong = sogiohoatdong;
	}
	public long getSogiodungdo() {
		return sogiodungdo;
	}
	public void setSogiodungdo(long sogiodungdo) {
		this.sogiodungdo = sogiodungdo;
	}
	public int getSolandungdo() {
		return solandungdo;
	}
	public void setSolandungdo(int solandungdo) {
		this.solandungdo = solandungdo;
	}
	public int getSolanchaylientuc() {
		return solanchaylientuc;
	}
	public void setSolanchaylientuc(int solanchaylientuc) {
		this.solanchaylientuc = solanchaylientuc;
	}
	public int getSolanvuottoc() {
		return solanvuottoc;
	}
	public void setSolanvuottoc(int solanvuottoc) {
		this.solanvuottoc = solanvuottoc;
	}
	public int getSolanqua4h() {
		return solanqua4h;
	}
	public void setSolanqua4h(int solanqua4h) {
		this.solanqua4h = solanqua4h;
	}
	public List<GpsTrackingMsg> getPointsStop() {
		return pointsStop;
	}
	public void setPointsStop(List<GpsTrackingMsg> pointsStop) {
		this.pointsStop = pointsStop;
	}
	
	
}
