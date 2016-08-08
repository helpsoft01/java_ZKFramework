package com.vietek.taxioperation.ui.controller;

import java.io.Serializable;

import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;

public class ProvinceDetail extends BasicDetailWindow implements Serializable {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;

	public ProvinceDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết tỉnh thành");
		this.setWidth("400px");
	}
	
	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("30%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("5%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("65%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã tỉnh thành "));
		Editor editor = this.getMapEditor().get("value");

		Cell cell = new Cell();
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên tỉnh thành"));
		editor = this.getMapEditor().get("ProvinceName");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(new Label("(*)"));
		cell.setStyle("color:red");

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Quốc gia"));
		editor = this.getMapEditor().get("national");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Loại"));
		editor = this.getMapEditor().get("type");

		cell = new Cell();
		cell = new Cell();
		cell.setParent(row);

		cell = new Cell();
		cell.setParent(row);
		cell.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES));
		cell.setStyle("color:red;text-align:center;");
	}
	
//	@Override
//	public void onEvent(Event event) throws Exception {
//		AbstractModel model = super.getModel();
//		Province modeltmp = (Province) model;
//		if (StringUtils.isHasWhiteSpace(modeltmp.getValue())) {
//			String valuetmp = modeltmp.getValue().trim();
//			modeltmp.setValue(valuetmp);
//			super.setModel(modeltmp);
//		}
//		if (event.getTarget().equals(this.getBtn_save())) {
//			if (StringUtils.isEmpty(validateInput(modeltmp))) {
//				super.handleSaveEvent();
//			} else {
//				Env.getHomePage().showNotification(validateInput(modeltmp),
//						Clients.NOTIFICATION_TYPE_ERROR);
//				return;
//			}
//		} else if (event.getTarget().equals(this.getBtn_cancel())
//				|| (event.getName().equals(Events.ON_CANCEL) && event
//						.getTarget().equals(this))) {
//			this.setVisible(false);
//			Env.getHomePage().showNotification("Bỏ qua thay đổi!",
//					Clients.NOTIFICATION_TYPE_INFO);
//			this.getListWindow().refresh();
//		}
//	}
//
//	// Lay model theo value
//
//	private Province getProvinceByValue(String value) {
//		ProvinceController provincecontroller = (ProvinceController) ControllerUtils
//				.getController(ProvinceController.class);
//		String sql = "from Province where value = ?";
//		List<Province> lstValue = provincecontroller.find(sql, value);
//		if ((lstValue == null) || (lstValue.size() > 0))
//			return (Province) lstValue.get(0);
//		else
//			return null;
//	}
//
//	// Kiem tra su ton tai cua National
//	private int checkNationalByName(String name, int idnational) {
//		int idprovince_temp = -1;
//		ProvinceController controller = (ProvinceController) ControllerUtils
//				.getController(ProvinceController.class);
//		String sql = "from Province where ProvinceName = ?";
//		List<Province> lstValue = controller.find(sql, name);
//		if (lstValue == null || lstValue.size() > 0) {
//			for (Province province : lstValue) {
//				National national = province.getNational();
//				if (national.getId() == idnational) {
//					idprovince_temp = province.getId();
//					return idprovince_temp;
//				}
//			}
//		}
//		return idprovince_temp;
//	}
//
//	private String validateInput(Province model) {
//
//		int idprovinceexist = -1;
//
//		StringBuilder msg = new StringBuilder("");
//		// validate code
//		if (StringUtils.isEmpty(model.getValue())) {
//			msg.append(CommonDefine.ProvinceDefine.CODE_PROVINCE).append(
//					CommonDefine.ERRORS_STRING_IS_EMPTY);
//		} else {
//			if (StringUtils.isHasSpecialChar(model.getValue())) {
//				msg.append(CommonDefine.ProvinceDefine.CODE_PROVINCE).append(
//						CommonDefine.ERRORS_STRING_IS_HAS_SPECIAL);
//			}
//			if (!StringUtils.checkMaxLength(model.getValue(),
//					CommonDefine.CODE_MAX_LENGTH)) {
//				msg.append(CommonDefine.ProvinceDefine.CODE_PROVINCE).append(
//						CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
//			}
//		}
//		// validate name
//		if (StringUtils.isEmpty(model.getProvinceName())) {
//			msg.append(CommonDefine.ProvinceDefine.NAME_PROVINCE).append(
//					CommonDefine.ERRORS_STRING_IS_EMPTY);
//		} else {
//			if (!StringUtils.checkMaxLength(model.getProvinceName(),
//					CommonDefine.NAME_MAX_LENGTH)) {
//				msg.append(CommonDefine.Province.NAME_PROVINCE).append(
//						CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
//			}
//		}
//
//		// validate National
//		if (model.getNational() == null) {
//			msg.append(CommonDefine.Province.CODE_NATIONAL).append(
//					CommonDefine.ERRORS_STRING_IS_EMPTY);
//		} else {
//			idprovinceexist = checkNationalByName(model.getProvinceName(), model
//					.getNational().getId());
//		}
//
//		// in case save
//		if (model.getId() <= 0) {
//			// check exit code of Zone
//			if (getProvinceByValue(model.getValue()) != null) {
//				msg.append(CommonDefine.Province.CODE_PROVINCE).append(
//						CommonDefine.ERRORS_STRING_IS_EXIST);
//			}
//
//			if (idprovinceexist >= 0 && idprovinceexist != model.getId()) {
//				msg.append(CommonDefine.Province.NAME_PROVINCE).append(
//						CommonDefine.ERRORS_STRING_IS_EXIST);
//			}
//		} else { // In case edit
//			Province province = getProvinceByValue(model.getValue());
//			if (province != null && province.getId() != model.getId()) {
//				msg.append(CommonDefine.Province.CODE_PROVINCE).append(
//						CommonDefine.ERRORS_STRING_IS_EXIST);
//			}
//			if (idprovinceexist >= 0 && idprovinceexist != model.getId()) {
//				msg.append(CommonDefine.Province.CODE_PROVINCE).append(
//						CommonDefine.ERRORS_STRING_IS_EXIST);
//			}
//
//		}
//		return msg.toString();
//	}

}
