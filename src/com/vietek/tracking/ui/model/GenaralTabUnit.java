package com.vietek.tracking.ui.model;

import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.West;

import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.model.Vehicle;

public class GenaralTabUnit extends Borderlayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label lblsodong;
	private Label lblvtrungbinh;
	private Label lblvmin;
	private Label lblvmax;
	private Label lblkmvandanh;
	private Label lblsogiohoatdong;
	private Label lblsogiodungdo;
	private Label lblsolandungdo;
	private Label lblsolanchaylientuc;
	private Label lblsolanvuottoc;
	private Label lblsolanqua4h;
	private Vehicle vehicle;

	public GenaralTabUnit() {
		creatUnit();
	}

	public GenaralTabUnit(Vehicle vehicle) {
		this.vehicle = vehicle;
		creatUnit();
	}

	private void creatUnit() {
		this.setStyle("z-borderlayout-detail-history");
		West west = new West();
		west.setSclass("z-west-detail-history");
		west.setTitle("Các thông tin tổng hợp khác");
		west.setMaxsize(250);
		west.setParent(this);
		west.setCollapsible(true);
		west.setOpen(false);
		Vlayout vls = new Vlayout();
		vls.setParent(west);

		Hlayout hl = new Hlayout();
		hl.setParent(vls);
		hl.setSclass("z-hlayout-west-history");
		Vlayout vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		vl.appendChild(new Label("Số giờ hoạt động:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		lblsogiohoatdong = new Label();
		vl.appendChild(lblsogiohoatdong);

		hl = new Hlayout();
		hl.setSclass("z-hlayout-west-history");
		hl.setParent(vls);
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		vl.appendChild(new Label("Số giờ dừng đỗ:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		lblsogiodungdo = new Label();
		vl.appendChild(lblsogiodungdo);

		hl = new Hlayout();
		hl.setSclass("z-hlayout-west-history");
		hl.setParent(vls);
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		vl.appendChild(new Label("Số lần dừng đỗ:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		lblsolandungdo = new Label();
		vl.appendChild(lblsolandungdo);

		hl = new Hlayout();
		hl.setSclass("z-hlayout-west-history");
		hl.setParent(vls);
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		vl.appendChild(new Label("Số lần chạy liên tục:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		lblsolanchaylientuc = new Label();
		vl.appendChild(lblsolanchaylientuc);

		hl = new Hlayout();
		hl.setSclass("z-hlayout-west-history");
		hl.setParent(vls);
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		vl.appendChild(new Label("Số lần vượt tốc:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		lblsolanvuottoc = new Label();
		vl.appendChild(lblsolanvuottoc);

		hl = new Hlayout();
		hl.setSclass("z-hlayout-west-history");
		hl.setParent(vls);
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		vl.appendChild(new Label("Số lần quá 4h:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-west-history");
		vl.setParent(hl);
		lblsolanqua4h = new Label();
		vl.appendChild(lblsolanqua4h);

		Center center = new Center();
		center.setParent(this);
		vls = new Vlayout();
		vls.setParent(center);
		hl = new Hlayout();
		hl.setParent(vls);
		hl.setSclass("z-hlayout-center-history");
		vl = new Vlayout();
		vl.setSclass("z-vlayout-center-history");
		vl.setParent(hl);
		vl.appendChild(new Label("Số dòng:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-center-history");
		vl.setParent(hl);
		lblsodong = new Label("");
		vl.appendChild(lblsodong);

		hl = new Hlayout();
		hl.setParent(vls);
		hl.setSclass("z-hlayout-center-history");
		vl = new Vlayout();
		vl.setSclass("z-vlayout-center-history");
		vl.setParent(hl);
		vl.appendChild(new Label("V Trung bình:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-center-history");
		vl.setParent(hl);
		lblvtrungbinh = new Label();
		vl.appendChild(lblvtrungbinh);

		hl = new Hlayout();
		hl.setParent(vls);
		hl.setSclass("z-hlayout-center-history");
		vl = new Vlayout();
		vl.setSclass("z-vlayout-center-history");
		vl.setParent(hl);
		vl.appendChild(new Label("V Min:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-center-history");
		vl.setParent(hl);
		lblvmin = new Label();
		vl.appendChild(lblvmin);

		hl = new Hlayout();
		hl.setParent(vls);
		hl.setSclass("z-hlayout-center-history");
		vl = new Vlayout();
		vl.setSclass("z-vlayout-center-history");
		vl.setParent(hl);
		vl.appendChild(new Label("V Max:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-center-history");
		vl.setParent(hl);
		lblvmax = new Label();
		lblvmax.setSclass("z-label-detail-his");
		;
		vl.appendChild(lblvmax);

		hl = new Hlayout();
		hl.setParent(vls);
		hl.setSclass("z-hlayout-center-history");
		vl = new Vlayout();
		vl.setSclass("z-vlayout-center-history");
		vl.setParent(hl);
		vl.appendChild(new Label("Km Vận danh:"));
		vl = new Vlayout();
		vl.setSclass("z-vlayout-center-history");
		vl.setParent(hl);
		lblkmvandanh = new Label();
		vl.appendChild(lblkmvandanh);

	}

	public void updateGenaral(GenaralValue genaralvalue) {
		lblsodong.setValue(String.valueOf(genaralvalue.getSodong()) + " Dòng");
		lblvtrungbinh.setValue(StringUtils.doubleFormat(genaralvalue.getVtrungbinh()) + " Km/h");
		lblvmin.setValue(String.valueOf(genaralvalue.getVmin()) + " Km/h");
		lblvmax.setValue(String.valueOf(String.valueOf(genaralvalue.getVmax())) + " Km/h");
		lblkmvandanh.setValue(String.valueOf(genaralvalue.getKmvandanh()) + " Km");
		lblsogiohoatdong.setValue(StringUtils.MilisToHours(genaralvalue.getSogiohoatdong()).replace("&nbsp", " "));
		lblsogiodungdo.setValue(StringUtils.MilisToHours(genaralvalue.getSogiodungdo()).replace("&nbsp", " "));
		lblsolandungdo.setValue(String.valueOf(genaralvalue.getSolandungdo()) + " Lần");
		lblsolanchaylientuc.setValue(String.valueOf(genaralvalue.getSolanchaylientuc()) + " Lần");
		lblsolanvuottoc.setValue(String.valueOf(genaralvalue.getSolanvuottoc()) + " Lần");
		lblsolanqua4h.setValue(String.valueOf(genaralvalue.getSolanqua4h()) + " Lần ");
	}
    public void refresh(){
    	lblsodong.setValue("");
		lblvmax.setValue("");
		lblvmin.setValue("");
		lblkmvandanh.setValue("");
		lblsogiohoatdong.setValue("");
		lblsogiodungdo.setValue("");
		lblsolandungdo.setValue("");
		lblsolanchaylientuc.setValue("");
		lblsolanvuottoc.setValue("");
		lblsolanqua4h.setValue("");
    }
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Label getLblsodong() {
		return lblsodong;
	}

	public void setLblsodong(Label lblsodong) {
		this.lblsodong = lblsodong;
	}

	public Label getLblvtrungbinh() {
		return lblvtrungbinh;
	}

	public void setLblvtrungbinh(Label lblvtrungbinh) {
		this.lblvtrungbinh = lblvtrungbinh;
	}

	public Label getLblvmin() {
		return lblvmin;
	}

	public void setLblvmin(Label lblvmin) {
		this.lblvmin = lblvmin;
	}

	public Label getLblvmax() {
		return lblvmax;
	}

	public void setLblvmax(Label lblvmax) {
		this.lblvmax = lblvmax;
	}

	public Label getLblkmvandanh() {
		return lblkmvandanh;
	}

	public void setLblkmvandanh(Label lblkmvandanh) {
		this.lblkmvandanh = lblkmvandanh;
	}

	public Label getLblsogiohoatdong() {
		return lblsogiohoatdong;
	}

	public void setLblsogiohoatdong(Label lblsogiohoatdong) {
		this.lblsogiohoatdong = lblsogiohoatdong;
	}

	public Label getLblsogiodungdo() {
		return lblsogiodungdo;
	}

	public void setLblsogiodungdo(Label lblsogiodungdo) {
		this.lblsogiodungdo = lblsogiodungdo;
	}

	public Label getLblsolandungdo() {
		return lblsolandungdo;
	}

	public void setLblsolandungdo(Label lblsolandungdo) {
		this.lblsolandungdo = lblsolandungdo;
	}

	public Label getLblsolanchaylientuc() {
		return lblsolanchaylientuc;
	}

	public void setLblsolanchaylientuc(Label lblsolanchaylientuc) {
		this.lblsolanchaylientuc = lblsolanchaylientuc;
	}

	public Label getLblsolanvuottoc() {
		return lblsolanvuottoc;
	}

	public void setLblsolanvuottoc(Label lblsolanvuottoc) {
		this.lblsolanvuottoc = lblsolanvuottoc;
	}

	public Label getLblsolanqua4h() {
		return lblsolanqua4h;
	}

	public void setLblsolanqua4h(Label lblsolanqua4h) {
		this.lblsolanqua4h = lblsolanqua4h;
	}

}
