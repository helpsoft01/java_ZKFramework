package com.vietek.taxioperation.ui.controller;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.common.CommonDefine;
import com.vietek.taxioperation.common.StringUtils;
import com.vietek.taxioperation.controller.TypeTablePriceController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.TypeTablePrice;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class TypeTablePricesDetail extends BasicDetailWindow {

	public TypeTablePricesDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		// TODO Auto-generated constructor stub
		this.setTitle("Loại bảng giá");
		this.setWidth("400px");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setWidth("25%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("5%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("70%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã"));
		Label lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		Editor editor = getMapEditor().get("codetype");
		row.appendChild(editor.getComponent());
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên Loại"));
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		editor = getMapEditor().get("tablepricename");
		row.appendChild(editor.getComponent());

//		row = new Row();
//		row.setParent(rows);
//		editor = getMapEditor().get("typevehicle");
//		row.appendChild(new Label("Loại xe"));
//		row.appendChild(new Label(""));
//		row.appendChild(editor.getComponent());
//		
//		row = new Row();
//		row.setParent(rows);
//		editor = getMapEditor().get("tablePrices");
//		row.appendChild(new Label("Bảng giá"));
//		row.appendChild(new Label(""));
//		row.appendChild(editor.getComponent());
		
		row = new Row();
		row.setParent(rows);
		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(3);
		cell.appendChild(new Label(CommonDefine.COMMON_VALIDATE_FORM_VALUES));
		cell.setStyle("color:red;text-align:center;");
	}

	@Override
	public void onEvent(Event event) throws Exception {
		AbstractModel model = super.getModel();
		TypeTablePrice modeltmp = (TypeTablePrice) model;
		String textvalue = modeltmp.getCodetype();
		if (StringUtils.isHasWhiteSpaceBeginEnd(textvalue)) {

			modeltmp.setCodetype(textvalue.trim());
			this.setModel(modeltmp);
		}
		if (event.getTarget().equals(this.getBtn_save())) {
			if (StringUtils.isEmpty(validateInput(modeltmp))) {
				super.handleSaveEvent();
			} else {
				Env.getHomePage().showValidateForm(validateInput(modeltmp),
						Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
		} else if (event.getTarget().equals(this.getBtn_cancel())
				|| (event.getName().equals(Events.ON_CANCEL) && event
						.getTarget().equals(this))) {
			this.setVisible(false);
			Env.getHomePage().showNotification("Bỏ qua thay đổi!",
					Clients.NOTIFICATION_TYPE_INFO);
			this.getListWindow().refresh();
		}
	}

	private String validateInput(TypeTablePrice model) {
		StringBuilder msg = new StringBuilder("");

		// validate code
		if (StringUtils.isEmpty(model.getCodetype())) {
			msg.append(CommonDefine.TypeTablePriceDefine.CODE_TYPE_TABLE_PRICE)
					.append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		} else {
			if (StringUtils.isHasSpecialChar(model.getCodetype())) {
				msg.append(
						CommonDefine.TypeTablePriceDefine.CODE_TYPE_TABLE_PRICE)
						.append(CommonDefine.ERRORS_STRING_IS_HAS_SPECIAL);
			}
			if (!StringUtils.checkMaxLength(model.getCodetype(),
					CommonDefine.CODE_MAX_LENGTH)) {
				msg.append(
						CommonDefine.TypeTablePriceDefine.CODE_TYPE_TABLE_PRICE)
						.append(CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
			}
		}
		// validate name
		if (StringUtils.isEmpty(model.getTablepricename())) {
			msg.append(CommonDefine.TypeTablePriceDefine.NAME_TYPE_TABLE_PRICE)
					.append(CommonDefine.ERRORS_STRING_IS_EMPTY);
		}
		

		// In case save

		if (model.getId() <= 0) {
			// check exist code
			if (getTypeTablePriceByValue(model.getCodetype()) != null) {
				msg.append(
						CommonDefine.TypeTablePriceDefine.CODE_TYPE_TABLE_PRICE)
						.append(CommonDefine.ERRORS_STRING_IS_EXIST);
			}
		} else {
			// In case edit
			TypeTablePrice tms = getTypeTablePriceByValue(model.getCodetype());
			if (tms != null && tms.getId() != model.getId())
				msg.append(
						CommonDefine.TypeTablePriceDefine.CODE_TYPE_TABLE_PRICE)
						.append(CommonDefine.ERRORS_STRING_IS_EXIST);
		}

		return msg.toString();
	}

	private TypeTablePrice getTypeTablePriceByValue(String value) {
		TypeTablePriceController controller = (TypeTablePriceController) ControllerUtils
				.getController(TypeTablePriceController.class);
		String sql = "from TypeTablePrice where codetype = ?";
		List<TypeTablePrice> lstvalue = controller.find(sql, value);
		if (lstvalue == null || lstvalue.size() > 0)
			return (TypeTablePrice) lstvalue.get(0);
		else
			return null;
	}

}
