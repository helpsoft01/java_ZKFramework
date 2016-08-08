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
import com.vietek.taxioperation.controller.ShiftworkTmsController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.ShiftworkTms;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class ShiftworkTmssDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShiftworkTmssDetail(AbstractModel model,
			AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết ca làm việc");
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
		col.setHflex("25%");
		
		col = new Column();
		col.setParent(cols);
		col.setHflex("5%");
		
		col = new Column();
		col.setParent(cols);
		col.setHflex("70%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Mã ca"));
		Label lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		Editor editor = this.getMapEditor().get("value");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên ca"));
		lb = new Label("(*)");
		lb.setStyle("color:Red");
		row.appendChild(lb);
		editor = this.getMapEditor().get("name");
		row.appendChild(editor.getComponent());
		

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Vào ca"));
		lb = new Label();
		row.appendChild(lb);
		editor = this.getMapEditor().get("startshift");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Rời ca"));
		lb = new Label();
		row.appendChild(lb);
		editor = this.getMapEditor().get("stopshift");
		row.appendChild(editor.getComponent());
		
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
		// TODO Auto-generated method stub
		AbstractModel model = super.getModel();
		ShiftworkTms modeltmp = (ShiftworkTms)model;
		String textvalue = modeltmp.getValue();
		if (StringUtils.isHasWhiteSpaceBeginEnd(textvalue)) {
			
		     modeltmp.setValue(textvalue.trim());
		     this.setModel(modeltmp);
		}
		if (event.getTarget().equals(this.getBtn_save())) {
			if(StringUtils.isEmpty(validateInput(modeltmp))){
				super.handleSaveEvent();
			}else{
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
	
	private String validateInput(ShiftworkTms model) {
		StringBuilder msg = new StringBuilder("");

		// validate code
		if (StringUtils.isEmpty(model.getValue())) {
			msg.append(CommonDefine.ShiftworkTmsDefine.CODE_SHIFTWORK).append(
					CommonDefine.ERRORS_STRING_IS_EMPTY);
		} else {
			if (StringUtils.isHasSpecialChar(model.getValue())) {
				msg.append(CommonDefine.ShiftworkTmsDefine.CODE_SHIFTWORK).append(
						CommonDefine.ERRORS_STRING_IS_HAS_SPECIAL);
			}
			if (!StringUtils.checkMaxLength(model.getValue(),
					CommonDefine.CODE_MAX_LENGTH)) {
				msg.append(CommonDefine.ShiftworkTmsDefine.CODE_SHIFTWORK).append(
						CommonDefine.ERRORS_STRING_IS_LIMIT_MAXLENGTH);
			}
		}
		//validate time 
		if (!StringUtils.checktimestam(model.getStopshift(),model.getStartshift())) {
			
			msg.append(CommonDefine.ShiftworkTmsDefine.TIME_INPUT).append(
					CommonDefine.ERRORS_STRING_INVALID);
		}
		// In case save

		if (model.getId() <= 0) {
			if (getShiftworkByValue(model.getValue()) != null) { // check exist code
				msg.append(CommonDefine.ShiftworkTmsDefine.CODE_SHIFTWORK)
						.append(CommonDefine.ERRORS_STRING_IS_EXIST);
			}
			
			
		} else { // In case edit
			ShiftworkTms tms = getShiftworkByValue(model.getValue());
			if (tms != null && tms.getId() != model.getId())
				msg.append(CommonDefine.ShiftworkTmsDefine.CODE_SHIFTWORK)
						.append(CommonDefine.ERRORS_STRING_IS_EXIST);
		}

		return msg.toString();
	}

	private ShiftworkTms getShiftworkByValue(String value) {
		ShiftworkTmsController controller = (ShiftworkTmsController) ControllerUtils
				.getController(ShiftworkTmsController.class);
		String sql = "from ShiftworkTms where value = ?";
		List<ShiftworkTms> lstvalue = controller.find(sql, value);
		if (lstvalue == null || lstvalue.size() > 0)
			return (ShiftworkTms) lstvalue.get(0);
		else
			return null;
	}
	
}
//
//	@Override
//	public void onEvent(Event event) throws Exception {
//
//		if (event.getName().equals(Events.ON_CHANGE)
//				|| event.getName().equals(Events.ON_SELECT)
//				|| event.getName().equals(Events.ON_CHECK)) {
//			Editor editor = (Editor) event.getTarget().getAttribute(
//					Editor.EDITOR);
//			if (editor != null) {
//				Object value = editor.getValue();
//				AbstractModel.setValue(this.getModel(), editor.getDataField(),
//						value);
//			}
//
//		} else if (event.getTarget().equals(this.getBtn_save())) {
//
//			AbstractModel model = super.getModel();
//			ShiftworkTms modeltmp1 = (ShiftworkTms) model;
//			try {
//				modeltmp = (ShiftworkTms) model;
//				modeltmp.setValue((modeltmp1.getValue().trim()));
//				modeltmp.setName((modeltmp1.getName().trim()));
//			} catch (Exception ex) {
//				Env.getHomePage().showNotification(
//						"Không được để trống trường mã ca !",
//						Clients.NOTIFICATION_TYPE_ERROR);
//			}
//
//			if (modeltmp.getValue() == null
//					|| modeltmp.getValue().length() <= 0) {
//				Env.getHomePage().showNotification(
//						"Không được để trống trường mã ca",
//						Clients.NOTIFICATION_TYPE_ERROR);
//			} else if (modeltmp.getName() == null
//					|| modeltmp.getName().length() <= 0) {
//				Env.getHomePage().showNotification(
//						"Không được để trống trường tên ca",
//						Clients.NOTIFICATION_TYPE_ERROR);
//			} else if (this.checkexistid(modeltmp.getId())) {
//
//				boolean val4 = this.checkexisteditvalue(modeltmp.getId(),
//						modeltmp.getValue().trim());
//				boolean val5 = this.checkexisteditname(modeltmp.getId(),
//						modeltmp.getName().trim());
//				// Kiểm tra việc sửa mã ca có bị trùng không
//				if (val4) {
//					Env.getHomePage().showNotification("Sửa mã ca bị trùng!",
//							Clients.NOTIFICATION_TYPE_ERROR);
//				} else if (val5) {
//					// Kiểm tra việc sửa tên tổng đài có bị trùng không
//					Env.getHomePage().showNotification("Sửa tên ca bị trùng!",
//							Clients.NOTIFICATION_TYPE_ERROR);
//				} else {
//
//					try {
//						// Lưu dữ liệu sửa, nếu nhập quá dài sẽ báo lỗi
//						modeltmp.save();
//						getListWindow().refresh();
//						this.setVisible(false);
//					} catch (Exception ex) {
//
//						Env.getHomePage()
//								.showNotification(
//										"Nhập ký tự quá dài (>25 ký tự)! Hãy kiểm tra lại !",
//										Clients.NOTIFICATION_TYPE_ERROR);
//
//					}
//				}
//
//			} else {
//				// Kiểm tra việc nhập mã ca có bị trùng với các mã đã có
//				if (this.checkexistvalue(modeltmp.getValue().trim())) {
//					Env.getHomePage().showNotification(
//							"Đã nhập trùng trường mã ca!",
//							Clients.NOTIFICATION_TYPE_ERROR);
//				} else if (this.checkexistname(modeltmp.getName().trim())) {
//					// Kiểm tra việc nhập tên ca có bị trùng với các tên
//					// đã có
//					Env.getHomePage().showNotification(
//							"Đã nhập trùng trường tên ca!",
//							Clients.NOTIFICATION_TYPE_ERROR);
//				} else {
//
//					try {
//						modeltmp.save();
//						getListWindow().refresh();
//						this.setVisible(false);
//					} catch (Exception ex) {
//						Env.getHomePage()
//								.showNotification(
//										"Nhập ký tự quá dài (>25 ký tự)! Hãy kiểm tra lại!",
//										Clients.NOTIFICATION_TYPE_ERROR);
//					}
//				}
//			}
//
//		} else if (event.getTarget().equals(this.getBtn_cancel())
//				|| (event.getName().equals(Events.ON_CANCEL) && event
//						.getTarget().equals(this))) {
//			this.setVisible(false);
//			Env.getHomePage().showNotification("Bỏ qua thay đổi!",
//					Clients.NOTIFICATION_TYPE_INFO);
//		} else {
//			super.onEvent(event);
//		}
//	}
//
//	// Hàm kiểm tra việc nhập mã tổng đài có bị trùng không
//	private boolean checkexistvalue(String value) {
//		ShiftworkTmsController controller = (ShiftworkTmsController) ControllerUtils
//				.getController(ShiftworkTmsController.class);
//		String sql = "from ShiftworkTms where value = ?";
//		List<ShiftworkTms> lstvalue = controller.find(sql, value);
//		if (lstvalue == null || lstvalue.isEmpty()) {
//			return false;
//		}
//		return true;
//	}
//
//	// Hàm kiểm tra việc nhập tên tổng đài có bị trùng không
//	private boolean checkexistname(String name) {
//		ShiftworkTmsController controller = (ShiftworkTmsController) ControllerUtils
//				.getController(ShiftworkTmsController.class);
//		String sql = "from ShiftworkTms where name = ?";
//		List<ShiftworkTms> lstvalue = controller.find(sql, name);
//		if (lstvalue == null || lstvalue.isEmpty()) {
//			return false;
//		}
//		return true;
//	}
//
//	// Hàm kiểm tra sự tồn tại của tổng đài qua id
//	private boolean checkexistid(int id) {
//		ShiftworkTmsController controller = (ShiftworkTmsController) ControllerUtils
//				.getController(ShiftworkTmsController.class);
//		String sql = "from ShiftworkTms where id = ?";
//		List<ShiftworkTms> lstvalue = controller.find(sql, id);
//		if (lstvalue == null || lstvalue.isEmpty()) {
//			return false;
//		}
//		return true;
//	}
//
//	// Hàm kiểm tra việc sửa mã tổng đài có bị trùng
//	private boolean checkexisteditvalue(int id, String newVal) {
//		boolean exist = false;
//		ShiftworkTmsController controller = (ShiftworkTmsController) ControllerUtils
//				.getController(ShiftworkTmsController.class);
//		String sql = "from ShiftworkTms where id <> ?";
//		List<ShiftworkTms> lstvalue = controller.find(sql, id);
//		if (lstvalue == null || lstvalue.isEmpty()) {
//			exist = false;
//			return exist;
//		}
//		for (ShiftworkTms shiftworktms : lstvalue) {
//			if (shiftworktms.getValue().equals(newVal)) {
//				exist = true;
//				break;
//			}
//		}
//		return exist;
//	}
//
//	// Hàm kiểm tra việc sửa tên tổng đài có bị trùng
//	private boolean checkexisteditname(int id, String newname) {
//		boolean exist = false;
//		ShiftworkTmsController controller = (ShiftworkTmsController) ControllerUtils
//				.getController(ShiftworkTmsController.class);
//		String sql = "from ShiftworkTms where id <> ?";
//		List<ShiftworkTms> lstvalue = controller.find(sql, id);
//		if (lstvalue == null || lstvalue.isEmpty()) {
//			exist = false;
//			return exist;
//		}
//		for (ShiftworkTms shiftworktms : lstvalue) {
//			if (shiftworktms.getName().equals(newname)) {
//				exist = true;
//				break;
//			}
//		}
//		return exist;
//	}
//}
