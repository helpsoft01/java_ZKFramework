package com.vietek.taxioperation.report;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Listbox;

import com.vietek.taxioperation.ui.util.GridColumn;

public class ReportTotalActivityByVehicle extends AbstractReportWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ReportTotalActivityByVehicle() {
		super();
		this.removeComponent(this.chosenAgent, true);
		this.removeComponent(this.chosenDriver, true);
		this.removeComponent(this.txtVehicleNumber, true);
		this.removeComponent(this.txtLicensePlate, true);
	}

	@Override
	public void loadModel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("TT", 40, String.class, "", "", getModelClass()));
		lstCols.add(new GridColumn("BKS", 100, String.class, "getLicensePlate", "licensePlate", getModelClass()));
		lstCols.add(new GridColumn("Loại xe", 100, String.class, "getTypeName", "typeName", getModelClass()));
		lstCols.add(new GridColumn("Ngày", 120, Integer.class, "getKmGps", "kmGps", getModelClass()));
		lstCols.add(new GridColumn("Bắt đầu", 100, Float.class, "getFivePerPath", "fivePerPath", getModelClass()));
		lstCols.add(new GridColumn("Kết thúc", 100, Float.class, "getTenPerPath", "tenPerPath", getModelClass()));
		lstCols.add(new GridColumn("Xe chạy", 100, Float.class, "getTwentyPerPath", "twentyPerPath", getModelClass()));
		lstCols.add(new GridColumn("Dừng đỗ", 100, Float.class, "getThirthPerPath", "thirthPerPath", getModelClass()));
		lstCols.add(new GridColumn("Dừng xe nổ máy", 100, Integer.class, "getFiveTime", "fiveTime", getModelClass()));
		lstCols.add(new GridColumn("Dừng xe tắt máy", 100, Integer.class, "getTenTime", "tenTime", getModelClass()));
		lstCols.add(new GridColumn("Bắt đầu", 250, Integer.class, "getTwentyTime", "twentyTime", getModelClass()));
		lstCols.add(new GridColumn("Kết thúc", 250, Integer.class, "getTwentyTime", "twentyTime", getModelClass()));
		lstCols.add(new GridColumn("Km GPS", 100, Integer.class, "getTwentyTime", "twentyTime", getModelClass()));
		lstCols.add(new GridColumn("Km Cơ", 100, Integer.class, "getTwentyTime", "twentyTime", getModelClass()));
		lstCols.add(new GridColumn("Tiêu hao nhiên liệu (lít)", 150, Integer.class, "getTwentyTime", "twentyTime", getModelClass()));
		lstCols.add(new GridColumn("Lần dừng đỗ", 120, Integer.class, "getTwentyTime", "twentyTime", getModelClass()));
		lstCols.add(new GridColumn("Thời gian bật điều hòa", 150, Integer.class, "getTwentyTime", "twentyTime", getModelClass()));
		setGridColumns((ArrayList<GridColumn>) lstCols);

	}

	@Override
	public void setTitleReport() {
		this.setLbTitleInput("Điều Kiện Tìm Kiếm");
		this.setLbTitleReport("Báo Cáo Hoạt Động Theo Xe");

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void createHeaderExtra(Listbox listbox) {
		Auxhead head = new Auxhead();
		head.setParent(listbox);

		Auxheader header = new Auxheader();
		header.setParent(head);
		header.setColspan(1);

		header = new Auxheader("Phương tiện");
		header.setParent(head);
		header.setColspan(2);
		header.setAlign("center");

		header = new Auxheader("Thời gian hoạt động");
		header.setParent(head);
		header.setColspan(3);
		header.setAlign("center");
		
		header = new Auxheader("Tình trạng hoạt động");
		header.setParent(head);
		header.setColspan(4);
		header.setAlign("center");
		
		header = new Auxheader("Vị trí");
		header.setParent(head);
		header.setColspan(2);
		header.setAlign("center");
		
		header = new Auxheader("");
		header.setParent(head);
		header.setColspan(5);
		header.setAlign("center");
	}

	@Override
	public void setMapParams() {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderReportWithListBox() {
		// TODO Auto-generated method stub
		
	}

}
