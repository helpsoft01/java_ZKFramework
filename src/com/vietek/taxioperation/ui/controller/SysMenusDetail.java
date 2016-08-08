package com.vietek.taxioperation.ui.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.vietek.taxioperation.common.ListCommon;
import com.vietek.taxioperation.controller.SysMenuController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.SysMenu;
import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

/**
 * 
 * @author VuD
 * 
 */

public class SysMenusDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SysMenusDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết menu");
		this.setWidth("650px");
	}

	@Override
	public void setDefaultValue() {
		super.setDefaultValue();
		int id = (int) AbstractModel.getValue(this.getModel(), "id");
		if (id == new Integer(0)) {
			SysMenuController controller = (SysMenuController) ControllerUtils.getController(SysMenuController.class);
			List<SysMenu> lstModel = controller.find("from SysMenu order by sequence desc");
			if (lstModel != null && lstModel.size() > 0) {
				Editor editor = this.getMapEditor().get("sequence");
				int sequenTmp = lstModel.get(0).getSequence() + 10;
				editor.setValue(sequenTmp);
				AbstractModel.setValue(this.getModel(), "sequence", sequenTmp);
			}
		}
	}

	@Override
	public void createForm() {
		Grid grid = new Grid();
		grid.setParent(this);

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setParent(cols);
		col.setHflex("15%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("35%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("15%");

		col = new Column();
		col.setParent(cols);
		col.setHflex("35%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Tên nhóm"));
		Editor editor = this.getMapEditor().get("name");
		row.appendChild(editor.getComponent());

		row.appendChild(new Label("Chức năng"));
		editor = this.getMapEditor().get("function");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Thứ tự"));
		editor = this.getMapEditor().get("sequence");
		row.appendChild(editor.getComponent());

		row.appendChild(new Label("Menu cha"));
		editor = this.getMapEditor().get("parentId");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kich hoạt"));
		editor = this.getMapEditor().get("isActive");
		row.appendChild(editor.getComponent());

	}

	private boolean beforeSave() {
		boolean result = true;
		String name = (String) AbstractModel.getValue(this.getModel(), "name");
		int currentId = (int) AbstractModel.getValue(this.getModel(), "id");
		if (name != null) {
			if (name.trim().length() <= 0) {
				Env.getHomePage().showValidateForm("Trường tên nhóm không được để toàn khoảng trắng",
						Clients.NOTIFICATION_TYPE_ERROR);
				Editor editor = this.getMapEditor().get("name");
				editor.setValue(null);
				return false;
			}
			String nameTmp = name.trim();
			AbstractModel.setValue(this.getModel(), "name", nameTmp);
			Editor editor = this.getMapEditor().get("name");
			editor.setValue(nameTmp);
			if (nameTmp.length() > 50) {
				Env.getHomePage().showValidateForm("Trường tên nhóm không được để quá 50 ký tự",
						Clients.NOTIFICATION_TYPE_ERROR);
				return false;
			}
			// SysMenuController controller = (SysMenuController)
			// ControllerUtils.getController(SysMenuController.class);
			// if (currentId == 0) {
			// List<SysMenu> lstValue = controller.find("from SysMenu where name
			// = ?", nameTmp);
			// if (lstValue != null && !lstValue.isEmpty()) {
			// Env.getHomePage().showValidateForm("Trường tên nhóm bị trùng với
			// nhóm khác",
			// Clients.NOTIFICATION_TYPE_ERROR);
			// return false;
			// }
			// } else {
			// List<SysMenu> lstValue = controller.find("from SysMenu where name
			// = ? and id != ?",
			// new Object[] { nameTmp, currentId });
			// if (lstValue != null && !lstValue.isEmpty()) {
			// Env.getHomePage().showValidateForm("Trường tên nhóm bị trùng với
			// nhóm khác",
			// Clients.NOTIFICATION_TYPE_ERROR);
			// return false;
			// }
			// }

		} else {
			Env.getHomePage().showValidateForm("Trường tên nhóm không được để trống", Clients.NOTIFICATION_TYPE_ERROR);
			return false;
		}
		if (currentId > 0) {
			int parentId = (int) AbstractModel.getValue(this.getModel(), "parentId");
			if (parentId <= 0) {
				return true;
			}
			if (parentId == currentId) {
				Env.getHomePage().showValidateForm("Bạn không thể chọn thư mục cha là chính nó",
						Clients.NOTIFICATION_TYPE_ERROR);
				return false;
			}
			if (!this.checkParrent()) {
				result = false;
			}
		}
		return result;
	}

	private boolean checkParrent() {
		boolean result = true;
		SysMenuController controller = (SysMenuController) ControllerUtils.getController(SysMenuController.class);
		// SysMenu parrent = controller.get(SysMenu.class,
		// (int) AbstractModel.getValue(this.getModel(), "parrentId"));
		int curParentId = (int) AbstractModel.getValue(this.getModel(), "parentId");
		int currentId = (int) AbstractModel.getValue(this.getModel(), "id");
		List<SysMenu> lstChildModel = controller.find("from SysMenu where parentId = ?", currentId);
		if (lstChildModel != null && lstChildModel.size() > 0) {
			for (SysMenu sysMenu : lstChildModel) {
				if (sysMenu.getId() == curParentId) {
					result = false;
					Env.getHomePage().showValidateForm("Giá trị cha của bạn đã ở mức cao hơn bạn",
							Clients.NOTIFICATION_TYPE_ERROR);
					return false;
				}
			}
			List<SysMenu> lstOldModel = controller.find("from SysMenu ");
			if (lstOldModel != null && lstOldModel.size() > 0) {
				for (SysMenu sysMenu : lstChildModel) {
					if (!checkChild(curParentId, sysMenu, lstOldModel)) {
						Env.getHomePage().showValidateForm("Giá trị cha của bạn đã ở mức cao hơn bạn",
								Clients.NOTIFICATION_TYPE_ERROR);
						result = false;
					}
				}
			}
		}
		return result;
	}

	private boolean checkChild(int curParentId, SysMenu parrent, List<SysMenu> lstOldModel) {
		boolean result = true;
		for (SysMenu sysMenu : lstOldModel) {
			if (sysMenu.getParentId() == parrent.getId()) {
				for (SysMenu sysMenuSub : lstOldModel) {
					if (sysMenuSub.getId() == curParentId) {
						return false;
					} else {

					}
				}
			}
		}
		return result;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(this.getBtn_save())) {
			this.handleSaveEvent();
			((SysMenus) this.getListWindow()).refreshTree();
		} else if (event.getTarget().equals(this.getBtn_cancel())
				|| (event.getName().equals(Events.ON_CANCEL) && event.getTarget().equals(this))) {
			this.setVisible(false);
			Env.getHomePage().showNotification("Bỏ qua thay đổi!", Clients.NOTIFICATION_TYPE_INFO);
			((SysMenus) this.getListWindow()).refreshTree();
		} else if (event.getTarget().equals(this)) {
			if (event.getName().equals(Events.ON_OK)) {
				this.handleSaveEvent();
				((SysMenus) this.getListWindow()).refreshTree();
			}
		}
	}

	@Override
	public void handleSaveEvent() {
		if (beforeSave()) {
			this.getModel().save();
			this.afterSave();
			this.setVisible(false);
			Env.getHomePage().showNotification("Đã cập nhật thông tin!", Clients.NOTIFICATION_TYPE_INFO);
		}
		// else {
		// Env.getHomePage().showValidateForm("Giá trị cha của bạn đã ở mức cao
		// hơn bạn",
		// Clients.NOTIFICATION_TYPE_ERROR);
		// }
	}

	private void afterSave() {
		SysMenu obj = (SysMenu) this.getModel();
		synchronized (ListCommon.LIST_SYS_MENU) {
			if (ListCommon.LIST_SYS_MENU.contains(obj)) {
				int index = ListCommon.LIST_SYS_MENU.indexOf(obj);
				SysMenu oldMenu = ListCommon.LIST_SYS_MENU.get(index);
				oldMenu.updateValue(obj);
				Collections.sort(ListCommon.LIST_SYS_MENU, new Comparator<SysMenu>() {
					@Override
					public int compare(SysMenu o1, SysMenu o2) {
						if (o1.getSequence() < o2.getSequence()) {
							return -1;
						} else if (o1.getSequence() > o2.getSequence()) {
							return 1;
						}
						return 0;
					}
				});
			} else {
				ListCommon.LIST_SYS_MENU.add(obj);
				Collections.sort(ListCommon.LIST_SYS_MENU, new Comparator<SysMenu>() {
					@Override
					public int compare(SysMenu o1, SysMenu o2) {
						if (o1.getSequence() < o2.getSequence()) {
							return -1;
						} else if (o1.getSequence() > o2.getSequence()) {
							return 1;
						}
						return 0;
					}
				});
			}
		}
	}

}
