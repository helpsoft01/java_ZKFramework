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

public class DistrictTmsDetail extends BasicDetailWindow implements
		Serializable {

	/**
	 * @author MPV
	 */
	private static final long serialVersionUID = 1L;
	

	public DistrictTmsDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết Quận/ Huyện");
		this.setWidth("500px");
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
		row.appendChild(new Label("Mã quận/ huyện "));
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
		row.appendChild(new Label("Tên quận/ huyện"));
		editor = this.getMapEditor().get("name");

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
		row.appendChild(new Label("Loại"));
		editor = this.getMapEditor().get("type");

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
		row.appendChild(new Label("Tỉnh/thành phố"));
		editor = this.getMapEditor().get("province");

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
		cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES));
		cell.setStyle("color:red;text-align:center;");
	}
	
//	@Override
//	public void onEvent(Event event) throws Exception {
//		AbstractModel model = super.getModel();
//		DistrictTms modeltmp = (DistrictTms) model;
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
//	private DistrictTms getDistrictTmsByValue(String value) {
//		DistrictTmsController districtcontroller = (DistrictTmsController) ControllerUtils
//				.getController(DistrictTmsController.class);
//		String sql = "from DistrictTms where value = ?";
//		List<DistrictTms> lstValue = districtcontroller.find(sql, value);
//		if ((lstValue == null) || (lstValue.size() > 0))
//			return (DistrictTms) lstValue.get(0);
//		else
//			return null;
//	}
//
//	// Kiem tra su ton tai cua Province
//	private int checkProvinceByName(String name, int idprovince) {
//		int iddistrict_temp = -1;
//		DistrictTmsController controller = (DistrictTmsController) ControllerUtils
//				.getController(DistrictTmsController.class);
//		String sql = "from DistrictTms where name = ?";
//		List<DistrictTms> lstValue = controller.find(sql, name);
//		if (lstValue == null || lstValue.size() > 0) {
//			for (DistrictTms district : lstValue) {
//				Province province = district.getProvince();
//				if (province.getId() == idprovince) {
//					iddistrict_temp = district.getId();
//					return iddistrict_temp;
//				}
//			}
//		}
//		return iddistrict_temp;
//	}
//
//	private String validateInput(DistrictTms model) {
//
//		int iddistrictexist = -1;
//
//		StringBuilder msg = new StringBuilder("");
//		// validate code
//		if (StringUtils.isEmpty(model.getValue())) {
//			msg.append(CommonDefine.DistrictTmsDefine.CODE_DISTRICTTMS).append(
//					CommonDefine.ERRORS_STRING_IS_EMPTY);
//		} else {
//			if (StringUtils.isHasSpecialChar(model.getValue())) {
//				msg.append(CommonDefine.DistrictTmsDefine.CODE_DISTRICTTMS).append(
//						CommonDefine.ERRORS_STRING_IS_HAS_SPECIAL);
//			}
//			if (!StringUtils.checkMaxLength(model.getValue(),
//					CommonDefine.CODE_MAX_LENGTH)) {
//				msg.append(CommonDefine.DistrictTmsDefine.CODE_DISTRICTTMS).append(
//						CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
//			}
//		}
//		// validate name
//		if (StringUtils.isEmpty(model.getName())) {
//			msg.append(CommonDefine.DistrictTmsDefine.NAME_DISTRICTTMS).append(
//					CommonDefine.ERRORS_STRING_IS_EMPTY);
//		} else {
//			if (!StringUtils.checkMaxLength(model.getName(),
//					CommonDefine.NAME_MAX_LENGTH)) {
//				msg.append(CommonDefine.DistrictTms.NAME_DISTRICTTMS).append(
//						CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
//			}
//		}
//
//		// validate Province
//		if (model.getProvince() == null) {
//			msg.append(CommonDefine.DistrictTms.CODE_PROVINCE).append(
//					CommonDefine.ERRORS_STRING_IS_EMPTY);
//		} else {
//			iddistrictexist = checkProvinceByName(model.getName(), model
//					.getProvince().getId());
//		}
//
//		// in case save
//		if (model.getId() <= 0) {
//			// check exit code of District
//			if (getDistrictTmsByValue(model.getValue()) != null) {
//				msg.append(CommonDefine.DistrictTms.CODE_DISTRICTTMS).append(
//						CommonDefine.ERRORS_STRING_IS_EXIST);
//			}
//
//			if (iddistrictexist >= 0 && iddistrictexist != model.getId()) {
//				msg.append(CommonDefine.DistrictTms.NAME_DISTRICTTMS).append(
//						CommonDefine.ERRORS_STRING_IS_EXIST);
//			}
//		} else { // In case edit
//			DistrictTms district = getDistrictTmsByValue(model.getValue());
//			if (district != null && district.getId() != model.getId()) {
//				msg.append(CommonDefine.DistrictTms.CODE_DISTRICTTMS).append(
//						CommonDefine.ERRORS_STRING_IS_EXIST);
//			}
//			if (iddistrictexist >= 0 && iddistrictexist != model.getId()) {
//				msg.append(CommonDefine.DistrictTms.CODE_DISTRICTTMS).append(
//						CommonDefine.ERRORS_STRING_IS_EXIST);
//			}
//
//		}
//		return msg.toString();
//	}

	

}
