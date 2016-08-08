package com.vietek.taxioperation.ui.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.ListCommon;
import com.vietek.taxioperation.controller.SysActionController;
import com.vietek.taxioperation.controller.SysGroupController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.SysAction;
import com.vietek.taxioperation.model.SysCompany;
import com.vietek.taxioperation.model.SysFunction;
import com.vietek.taxioperation.model.SysGroup;
import com.vietek.taxioperation.model.SysGroupLine;
import com.vietek.taxioperation.model.SysMenu;
import com.vietek.taxioperation.model.SysRule;
import com.vietek.taxioperation.model.SysUser;
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

public class SysGroupsDetail extends BasicDetailWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String ATT_SYSACTION = new String("sysAction");
	private Tree tree;
	private Vlayout mainLayout;
	private Listbox lstboxUser;
	private Listbox lstboxRule;
	private Listbox lstboxFunction;
	private List<SysUser> lstUserModel;
	private List<SysRule> lstRuleModel;
	private Button btnAddFunction;
	private List<SysAction> lstActionModel;
	private Window winAction = null;

	public SysGroupsDetail(AbstractModel model, AbstractWindowPanel listWindow) {
		super(model, listWindow);
		this.setTitle("Chi tiết nhóm người dùng");
		this.setSizable(true);
		this.setHeight("600px");
		this.setWidth("1000px");
	}

	public SysGroupsDetail getCurWindows() {
		return this;
	}

	@Override
	public void createForm() {
		// TODO: createForm
		try {

			long start = System.currentTimeMillis();
			Hlayout totalLayout = new Hlayout();
			totalLayout.setParent(this);
			totalLayout.setHeight("90%");
			Vlayout leftLayout = new Vlayout();
			leftLayout.setParent(totalLayout);
			leftLayout.setHflex("3");
			leftLayout.setVflex("1");
			this.createLeft(leftLayout);
			mainLayout = new Vlayout();
			mainLayout.setParent(totalLayout);
			mainLayout.setHflex("7");
			mainLayout.setVflex("1");
			this.createGrid(mainLayout);
			long startTime = System.currentTimeMillis();
			this.lstUserModel = this.getListUser();
			this.lstRuleModel = this.getListRule();
			this.lstActionModel = this.getListActionModel();
			System.out.println("TimeCallProcedure:" + (System.currentTimeMillis() - startTime));
			System.out.println("TimeCreateForm:" + (System.currentTimeMillis() - start));

		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	private void createLeft(Vlayout leftLayout) {
		tree = new Tree();
		tree.setParent(leftLayout);
		tree.setMultiple(true);
		tree.setCheckmark(true);
		tree.setStyle("overflow-y: auto");
		tree.setVflex("9");
		// tree.addEventListener(Events.ON_CHECK, this);
		this.initTree(tree);
		btnAddFunction = new Button();
		btnAddFunction.setParent(leftLayout);
		btnAddFunction.setVflex("1");
		btnAddFunction.setLabel("Cập nhật chức năng");
		btnAddFunction.setSclass("btn_sys_group");
		btnAddFunction.addEventListener(Events.ON_CLICK, this);
	}

	@Override
	public void initUI() {
		try {
			super.initUI();
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	// TODO: setDefaultValue
	@Override
	public void setDefaultValue() {
		try {
			long start = System.currentTimeMillis();
			super.setDefaultValue();
			this.renderTree(tree);

			this.refreshListUser();
			this.refreshListRule();
			this.refreshListFunction();
			System.out.println("TimeCreateForm:" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	private void initTree(Tree tree) {
		Treecols cols = new Treecols();
		cols.setParent(tree);
		cols.setSizable(true);
		Treecol col = new Treecol();
		col.setParent(cols);
		col.setLabel("Cây Menu");
		col.setHflex("7");
		col = new Treecol();
		col.setParent(cols);
		col.setHflex("3");
		col.setLabel("Chức năng");
		tree.setItemRenderer(new MenuTreeRender());
	}

	private void createGrid(Component parent) {
		Vlayout vlayout = new Vlayout();
		vlayout.setParent(parent);
		Grid grid = new Grid();
		grid.setParent(vlayout);

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
		row.appendChild(new Label("Mã nhóm"));
		Editor editor = this.getMapEditor().get("value");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("Tên nhóm"));
		editor = this.getMapEditor().get("name");
		row.appendChild(editor.getComponent());
		row = new Row();
		row.setParent(rows);
		row.appendChild(new Label("Kich hoạt"));
		editor = this.getMapEditor().get("isActive");
		row.appendChild(editor.getComponent());
		row.appendChild(new Label("Ghi chú"));
		editor = this.getMapEditor().get("note");
		row.appendChild(editor.getComponent());

		row = new Row();
		row.setParent(rows);
		editor = mapEditor.get("sysUser");
		row.appendChild(editor.getComponent());
		editor = mapEditor.get("setSysRule");
		row.appendChild(editor.getComponent());
		row.setVisible(false);

		Tabbox tabbox = new Tabbox();
		tabbox.setParent(vlayout);
		tabbox.setHeight("100%");
		Tabs tabs = new Tabs();
		tabs.setParent(tabbox);
		Tabpanels panels = new Tabpanels();
		panels.setParent(tabbox);
		Tab tab = new Tab();
		tab.setParent(tabs);
		tab.setLabel("Người dùng");
		Tabpanel panel = new Tabpanel();
		panel.setParent(panels);
		this.createUserLine(panel);

		tab = new Tab();
		tab.setParent(tabs);
		tab.setLabel("Rule");
		panel = new Tabpanel();
		panel.setParent(panels);
		this.createRuleLine(panel);

		tab = new Tab();
		tab.setParent(tabs);
		tab.setLabel("Chức năng");
		panel = new Tabpanel();
		panel.setParent(panels);
		this.createFunctionLine(panel);
	}

	private void createUserLine(Tabpanel tabpanel) {
		lstboxUser = new Listbox();
		lstboxUser.setSclass("lstbox_sys_group_user");
		lstboxUser.setParent(tabpanel);
		lstboxUser.setCheckmark(true);
		lstboxUser.setMultiple(true);
		lstboxUser.setHeight("350px");
		lstboxUser.setSizedByContent(false);
		Listhead listhead = new Listhead();
		listhead.setParent(lstboxUser);
		listhead.setSizable(true);
		Listheader hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Mã");
		hearder.setWidth("150px");

		hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Tên");
		hearder.setHflex("true");
		hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Công ty");
		hearder.setWidth("100px");
		hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Kích hoạt");
		hearder.setWidth("100px");
		lstboxUser.setItemRenderer(new ListitemRenderer<SysUser>() {
			@Override
			public void render(Listitem item, SysUser data, int index) throws Exception {
				try {
					if (data != null) {
						item.setValue(data);
						if (isExistUser(data)) {
							item.setSelected(true);
						}
						item.appendChild(new Listcell(data.getValue()));
						item.appendChild(new Listcell(data.getName()));
						SysCompany company = data.getSysCompany();
						if (company != null) {
							item.appendChild(new Listcell(company.toString()));
						} else {
							item.appendChild(new Listcell(""));
						}
						Checkbox checkbox = new Checkbox();
						if (data.getIsActive()) {
							checkbox.setChecked(true);
						} else {
							checkbox.setChecked(false);
						}
						Listcell cell = new Listcell();
						cell.appendChild(checkbox);
						item.appendChild(cell);
					}
				} catch (Exception e) {
					AppLogger.logDebug.error("", e);
				}

			}
		});
	}

	private void createRuleLine(Tabpanel tabpanel) {
		lstboxRule = new Listbox();
		lstboxRule.setSclass("lstbox_sys_group_rule");
		lstboxRule.setParent(tabpanel);
		lstboxRule.setCheckmark(true);
		lstboxRule.setMultiple(true);
		lstboxRule.setHeight("350px");
		lstboxRule.setSizedByContent(false);
		Listhead listhead = new Listhead();
		listhead.setParent(lstboxRule);
		listhead.setSizable(true);
		Listheader hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Mã");
		hearder.setWidth("150px");
		hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Tên");
		hearder.setWidth("300px");
		hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Model");
		hearder.setWidth("100px");
		hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Độ ưu tiên");
		hearder.setWidth("70px");
		hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Kích hoạt");
		hearder.setWidth("70px");
		lstboxRule.setItemRenderer(new ListitemRenderer<SysRule>() {
			@Override
			public void render(Listitem item, SysRule data, int index) throws Exception {
				try {

					if (data != null) {
						item.setValue(data);
						if (isExistRule(data)) {
							item.setSelected(true);
						}
						item.appendChild(new Listcell(data.getValue()));
						item.appendChild(new Listcell(data.getName()));
						item.appendChild(new Listcell(data.getModelName()));
						item.appendChild(new Listcell(data.getPriority() + ""));
						Checkbox checkbox = new Checkbox();
						if (data.getIsActive()) {
							checkbox.setChecked(true);
						} else {
							checkbox.setChecked(false);
						}
						Listcell cell = new Listcell();
						cell.appendChild(checkbox);
						item.appendChild(cell);
					}
				} catch (Exception e) {
					AppLogger.logDebug.error("", e);
				}
			}
		});
	}

	private void createFunctionLine(Tabpanel tabpanel) {
		lstboxFunction = new Listbox();
		lstboxFunction.setSclass("lstbox_sys_group_func");
		lstboxFunction.setParent(tabpanel);
		lstboxFunction.setCheckmark(true);
		lstboxFunction.setHeight("400px");
		lstboxFunction.addEventListener(Events.ON_DOUBLE_CLICK, this);
		Listhead listhead = new Listhead();
		listhead.setParent(lstboxFunction);
		Listheader hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Chức năng");
		hearder.setWidth("200px");
		hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Hành động");
		hearder.setWidth("350px");
		hearder = new Listheader();
		hearder.setParent(listhead);
		hearder.setLabel("Kích hoạt");
		hearder.setWidth("100px");
		lstboxFunction.setItemRenderer(new ListitemRenderer<SysGroupLine>() {
			@Override
			public void render(Listitem item, SysGroupLine data, int index) throws Exception {
				try {
					if (data != null) {
						item.setValue(data);
						item.appendChild(new Listcell(data.getSysFunction().getName()));
						boolean isFirst = true;
						StringBuilder sb = new StringBuilder();
						for (SysAction action : data.getSysAction()) {
							if (!isFirst) {
								sb.append(", ");
							}
							isFirst = false;
							sb.append(action.toString());
						}
						item.appendChild(new Listcell(sb.toString()));
						Checkbox checkbox = new Checkbox();
						if (data.getSysFunction().getIsActive()) {
							checkbox.setChecked(true);
						} else {
							checkbox.setChecked(false);
						}
						Listcell cell = new Listcell();
						cell.appendChild(checkbox);
						item.appendChild(cell);
					}
				} catch (Exception e) {
					AppLogger.logDebug.error("", e);
				}
			}
		});

	}

	private boolean beforAction() {
		boolean result = true;
		try {
			this.getModel().save();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	private void renderTree(Tree tree) {
		try {
			MenuTreeNode root = this.getMenuNode();
			tree.setModel(new MenuTreeModel(root));
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	private SysGroupLine createGroupLineTree(SysFunction function) {
		SysGroupLine sysLine = new SysGroupLine();
		sysLine.setSysFunction(function);
		sysLine.setSysGroup((SysGroup) this.getModel());
		HashSet<SysAction> setAction = new HashSet<>(lstActionModel);
		sysLine.setSysAction(setAction);
		return sysLine;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(btnAddFunction)) {
			if (beforAction()) {
				List<SysFunction> lstFunction = new ArrayList<>();
				Set<Treeitem> items = tree.getSelectedItems();
				for (Treeitem treeitem : items) {
					TreeNode<SysMenu> node = treeitem.getValue();
					if (node.isLeaf()) {
						SysMenu sysMenu = node.getData();
						SysFunction function = sysMenu.getFunction();
						lstFunction.add(function);
						// if (!isExistGroup(function)) {
						// SysGroupLine tmp = createGroupLineTree(function);
						// if (tmp != null) {
						// lstTmp.add(tmp);
						// }
						// }
					}
				}
				List<SysGroupLine> lstDel = new ArrayList<>();
				SysGroup model = (SysGroup) this.getModel();
				for (SysGroupLine sysGroupLine : model.getSysGroupLines()) {
					SysFunction func = sysGroupLine.getSysFunction();
					if (!lstFunction.contains(func)) {
						lstDel.add(sysGroupLine);
					}
				}
				for (SysGroupLine sysGroupLine : lstDel) {
					model.getSysGroupLines().remove(sysGroupLine);
					sysGroupLine.delete();
				}
				List<SysGroupLine> lstTmp = new ArrayList<>();
				for (SysFunction sysFunction : lstFunction) {
					if (!isExistGroup(sysFunction)) {
						SysGroupLine tmp = createGroupLineTree(sysFunction);
						if (tmp != null) {
							lstTmp.add(tmp);
						}
					}
				}
				for (SysGroupLine sysGroupLine : lstTmp) {
					model.getSysGroupLines().add(sysGroupLine);
				}
			}
			this.refreshListFunction();
		}
		if (event.getTarget().equals(lstboxFunction)) {
			if (event.getName().equals(Events.ON_DOUBLE_CLICK)) {
				try {
					Listitem item = lstboxFunction.getSelectedItem();
					SysGroupLine groupLine = item.getValue();
					this.createChangeActionForm(groupLine);
				} catch (Exception e) {
					AppLogger.logDebug.error("", e);
				}
			}
		}
		super.onEvent(event);
	}

	private void createChangeActionForm(SysGroupLine groupLine) {
		if (winAction == null) {
			winAction = new Window();
			winAction.setParent(this);
			winAction.setSizable(true);
			winAction.setHeight("min");
			winAction.setWidth("min");
			Vlayout vlayout = new Vlayout();
			vlayout.setParent(winAction);
			for (SysAction sysAction : lstActionModel) {
				Checkbox checkbox = new Checkbox();
				checkbox.setLabel(sysAction.getName());
				checkbox.setParent(vlayout);
				checkbox.setAttribute(ATT_SYSACTION, sysAction);
				checkbox.setChecked(false);
			}
			Hlayout hlayout = new Hlayout();
			hlayout.setParent(winAction);
			hlayout.setStyle("align:center");
			Button btnSave = new Button();
			btnSave.setLabel("Xác nhận");
			btnSave.setParent(hlayout);
			Button btnCancel = new Button();
			btnCancel.setLabel("Hủy bỏ");
			btnCancel.setParent(hlayout);
			btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					Set<SysAction> actionSelected = new HashSet<>();
					Component child = winAction.getFirstChild();
					List<Component> lstCheckbox = child.getChildren();
					for (Component component : lstCheckbox) {
						Checkbox checkbox = (Checkbox) component;
						if (checkbox.isChecked()) {
							SysAction tmpAction = (SysAction) checkbox.getAttribute(ATT_SYSACTION);
							actionSelected.add(tmpAction);
						}
					}
					SysGroupLine line = (SysGroupLine) winAction.getAttribute("GROUP_LINE");
					line.setSysAction(actionSelected);
					winAction.setVisible(false);
					refreshListFunction();
				}
			});
			btnCancel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					winAction.setVisible(false);
				}
			});
		}
		Events.echoEvent("setDataFormAction", this, groupLine);
		winAction.setAttribute("GROUP_LINE", groupLine);
		winAction.doModal();
	}

	public void setDataFormAction(Event event) {
		SysGroupLine groupLine = (SysGroupLine) event.getData();
		SysFunction function = groupLine.getSysFunction();
		winAction.setTitle(function.toString());
		Set<SysAction> setAction = groupLine.getSysAction();
		Component child = winAction.getFirstChild();
		List<Component> lstCheckbox = child.getChildren();
		for (Component component : lstCheckbox) {
			Checkbox checkbox = (Checkbox) component;
			SysAction tmpAction = (SysAction) checkbox.getAttribute(ATT_SYSACTION);
			if (setAction.contains(tmpAction)) {
				checkbox.setChecked(true);
			}
		}
	}

	@Override
	public void handleSaveEvent() {
		if (this.checkValidate()) {
			SysGroup model = (SysGroup) this.getModel();
			Set<Listitem> setUserSelected = lstboxUser.getSelectedItems();
			Set<SysUser> users = new HashSet<>();
			for (Listitem listitem : setUserSelected) {
				SysUser user = listitem.getValue();
				if (user != null) {
					users.add(user);
				}
			}
			model.setSysUser(users);

			Set<Listitem> setRuleSelected = lstboxRule.getSelectedItems();
			Set<SysRule> rules = new HashSet<>();
			for (Listitem listitem : setRuleSelected) {
				SysRule user = listitem.getValue();
				if (user != null) {
					rules.add(user);
				}
			}
			model.setSetSysRule(rules);
			for (SysGroupLine line : model.getSysGroupLines()) {
				line.save();
			}
			super.handleSaveEvent();
		} else {

		}
	}

	private boolean isExistUser(SysUser sysUser) {
		boolean result = false;
		SysGroup model = (SysGroup) this.getModel();
		Set<SysUser> users = model.getSysUser();
		if (users.contains(sysUser)) {
			result = true;
		}
		return result;
	}

	private boolean isExistRule(SysRule sysRule) {
		boolean result = false;
		SysGroup model = (SysGroup) this.getModel();
		Set<SysRule> users = model.getSetSysRule();
		if (users.contains(sysRule)) {
			result = true;
		}
		return result;
	}

	private boolean isExistGroup(SysFunction function) {
		boolean result = false;
		if (this.getModel() != null) {
			SysGroup group = (SysGroup) this.getModel();
			Set<SysGroupLine> setLine = group.getSysGroupLines();
			for (SysGroupLine sysGroupLine : setLine) {
				SysFunction functionTmp = sysGroupLine.getSysFunction();
				if (functionTmp != null) {
					if (function.getId() == functionTmp.getId()) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	private boolean checkValidate() {
		if (this.checkOtherField()) {
			if (this.checkValue()) {
				if (this.chechName()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	private boolean chechName() {
		boolean result = true;
		SysGroup sysGroup = (SysGroup) this.getModel();
		String name = sysGroup.getName();
		if (name == null) {
			Env.getHomePage().showNotification("Tên không được để trống", Clients.NOTIFICATION_TYPE_ERROR);
			result = false;
		} else if (name.trim().length() <= 0) {
			sysGroup.setName("");
			Editor editor = this.getMapEditor().get("name");
			editor.setValue("");
			Env.getHomePage().showNotification("Tên không được để toàn khoảng trắng", Clients.NOTIFICATION_TYPE_ERROR);
			result = false;
		} else if (name.trim().length() >= 255) {
			String nameTmp = name.trim().substring(0, 255);
			sysGroup.setName(nameTmp);
			Editor editor = this.getMapEditor().get("name");
			editor.setValue(nameTmp);
			Env.getHomePage().showNotification("Tên không được để quá 255 ký tự", Clients.NOTIFICATION_TYPE_ERROR);
			result = false;
		} else {
			if (sysGroup.getId() <= 0) {
				SysGroupController controller = (SysGroupController) ControllerUtils
						.getController(SysGroupController.class);
				List<SysGroup> lstValue = controller.find("from SysGroup where name = ?", name.trim());
				if (lstValue != null && lstValue.size() > 0) {
					Env.getHomePage().showNotification("Tên này đã tồn tại", Clients.NOTIFICATION_TYPE_ERROR);
					result = false;
				}
			} else {
				SysGroupController controller = (SysGroupController) ControllerUtils
						.getController(SysGroupController.class);
				List<SysGroup> lstValue = controller.find("from SysGroup where name = ? and id != ?",
						new Object[] { name.trim(), sysGroup.getId() });
				if (lstValue != null && lstValue.size() > 0) {
					Env.getHomePage().showNotification("Tên này đã tồn tại", Clients.NOTIFICATION_TYPE_ERROR);
					result = false;
				}
				sysGroup.setName(name.trim());
			}
		}
		return result;
	}

	private boolean checkValue() {
		SysGroup sysGroup = (SysGroup) this.getModel();
		String value = sysGroup.getValue();
		if (value == null || value.trim().length() <= 0) {
			Env.getHomePage().showNotification("Mã không được để trống hoặc toàn khoảng trắng",
					Clients.NOTIFICATION_TYPE_ERROR);
			return false;
		} else {
			if (value.trim().length() < 3 || value.trim().length() > 15) {
				Env.getHomePage().showNotification("Độ dài của mã phải từ 3 đến 15 ký tự có nghĩa",
						Clients.NOTIFICATION_TYPE_ERROR);
				return false;
			}
			if (sysGroup.getId() <= 0) {
				SysGroupController controller = (SysGroupController) ControllerUtils
						.getController(SysGroupController.class);
				List<SysGroup> lstValue = controller.find("from SysGroup where value = ?", value.trim());
				if (lstValue != null && lstValue.size() > 0) {
					Env.getHomePage().showNotification("Mã này đã tồn tại", Clients.NOTIFICATION_TYPE_ERROR);
					return false;
				}
				sysGroup.setValue(value.trim());
			} else {
				SysGroupController controller = (SysGroupController) ControllerUtils
						.getController(SysGroupController.class);
				List<SysGroup> lstValue = controller.find("from SysGroup where value = ? and id != ?",
						new Object[] { value.trim(), sysGroup.getId() });
				if (lstValue != null && lstValue.size() > 0) {
					Env.getHomePage().showNotification("Mã này đã tồn tại", Clients.NOTIFICATION_TYPE_ERROR);
					return false;
				}
				sysGroup.setValue(value.trim());
			}
		}
		return true;
	}

	private boolean checkOtherField() {
		boolean result = true;
		String note = (String) AbstractModel.getValue(this.getModel(), "note");
		if (note != null) {
			if (note.length() > 255) {
				AbstractModel.setValue(this.getModel(), "note", note.substring(0, 255));
				Editor editor = this.getMapEditor().get("note");
				editor.setValue(note.substring(0, 255));
				Env.getHomePage().showNotification("Ghi chú không được quá 255 ký tự", Clients.NOTIFICATION_TYPE_ERROR);
				result = false;
			}
		}
		return result;
	}

	private MenuTreeNode getMenuNode() {
		MenuTreeNode root = null;
		List<SysMenu> lstValue = new ArrayList<>();
		lstValue.addAll(ListCommon.LIST_SYS_MENU);
		if (lstValue != null) {
			long start = System.currentTimeMillis();
			root = this.getChildNode(null, lstValue);
			long finishTime = System.currentTimeMillis() - start;
			System.out.println("GetTreeNode:" + finishTime);
		}
		return root;
	}

	private MenuTreeNode getChildNode(SysMenu parent, List<SysMenu> lstValue) {
		if (parent != null) {
			List<SysMenu> lstChildSysMenu = this.getListChild(parent, lstValue);
			if (lstChildSysMenu.isEmpty()) {
				SysFunction function = parent.getFunction();
				boolean isChecked = false;
				if (function != null) {
					if (this.isExistCurModel(function)) {
						isChecked = true;
					}
				}
				MenuTreeNode leaf = new MenuTreeNode(parent);
				leaf.setLeaf(true);
				leaf.setChecked(isChecked);
				return leaf;
			} else {
				List<MenuTreeNode> lstChild = new ArrayList<>();
				for (SysMenu sysMenu : lstChildSysMenu) {
					MenuTreeNode child = this.getChildNode(sysMenu, lstValue);
					lstChild.add(child);
				}
				MenuTreeNode superChild = new MenuTreeNode(parent, lstChild);
				superChild.setChecked(this.isChildSelected(lstChild));
				superChild.setLeaf(false);
				return superChild;
			}
		} else {
			List<SysMenu> lstOneChild = new ArrayList<>();
			for (SysMenu sysMenu : lstValue) {
				if (sysMenu.getParentId() == 0) {
					lstOneChild.add(sysMenu);
				}
			}
			List<MenuTreeNode> lstChild = new ArrayList<>();
			for (SysMenu sysMenu : lstOneChild) {
				MenuTreeNode child = this.getChildNode(sysMenu, lstValue);
				lstChild.add(child);
			}
			MenuTreeNode root = new MenuTreeNode(parent, lstChild);
			root.setLeaf(false);
			return root;
		}
	}

	private List<SysMenu> getListChild(SysMenu parent, List<SysMenu> lstValue) {
		List<SysMenu> lstChild = new ArrayList<>();
		for (SysMenu sysMenu : lstValue) {
			if (sysMenu.getParentId() == parent.getId()) {
				lstChild.add(sysMenu);
			}
		}
		return lstChild;
	}

	private boolean isExistCurModel(SysFunction function) {
		boolean result = false;
		SysGroup sysGroup = (SysGroup) this.getModel();
		if (sysGroup.getId() != 0) {
			Set<SysGroupLine> sysLine = sysGroup.getSysGroupLines();
			for (SysGroupLine sysGroupLine : sysLine) {
				SysFunction tmp = sysGroupLine.getSysFunction();
				if (tmp != null) {
					if (tmp.getId() == function.getId()) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	private boolean isChildSelected(List<MenuTreeNode> lstChild) {
		boolean result = false;
		for (MenuTreeNode menuTreeNode : lstChild) {
			if (menuTreeNode.isChecked) {
				result = true;
				break;
			}
		}
		return result;
	}

	private class MenuTreeNode extends DefaultTreeNode<SysMenu> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean isLeaf = true;
		private boolean isChecked = false;

		public MenuTreeNode(SysMenu data, boolean nullAsMax) {
			super(data, nullAsMax);
		}

		public MenuTreeNode(SysMenu arg0, Collection<? extends TreeNode<SysMenu>> arg1, boolean arg2) {
			super(arg0, arg1, arg2);
		}

		public MenuTreeNode(SysMenu data, Collection<? extends TreeNode<SysMenu>> children) {
			super(data, children);
		}

		public MenuTreeNode(SysMenu data, TreeNode<SysMenu>[] children) {
			super(data, children);
		}

		public MenuTreeNode(SysMenu data) {
			super(data);
		}

		public boolean isLeaf() {
			return isLeaf;
		}

		public void setLeaf(boolean isLeaf) {
			this.isLeaf = isLeaf;
		}

		public boolean isChecked() {
			return isChecked;
		}

		public void setChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}

	}

	private class MenuTreeModel extends DefaultTreeModel<SysMenu> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MenuTreeModel(TreeNode<SysMenu> root) {
			super(root);
			this.setMultiple(true);
		}

		public MenuTreeModel(TreeNode<SysMenu> root, boolean emptyChildAsLeaf) {
			super(root, emptyChildAsLeaf);
			this.setMultiple(true);
		}

	}

	private class MenuTreeRender implements TreeitemRenderer<MenuTreeNode> {

		@Override
		public void render(Treeitem treeItem, MenuTreeNode data, int arg2) throws Exception {
			try {
				SysMenu sysMenu = data.getData();
				treeItem.setValue(data);
				final Treerow row = new Treerow();
				row.setParent(treeItem);
				row.setVflex("1");
				Treecell cell = new Treecell();
				cell.setSclass("sys_group_cell");
				cell.setParent(row);
				cell.setLabel(sysMenu.getName());
				if (!data.isLeaf()) {
					cell.setStyle("font-weight: bold");
				}
				if (data.isChecked()) {
					treeItem.setOpen(true);
					if (data.isLeaf()) {
						treeItem.setSelected(true);
					}
				}

				row.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						Treeitem item = (Treeitem) row.getParent();
						MenuTreeNode node = item.getValue();
						if (node.isLeaf) {
//							System.out.println("Chon la");
						} else {
							if (item.isSelected()) {
								doCheckChild(item, true);
							} else {
								doCheckChild(item, false);
							}
						}
					}

				});
			} catch (Exception e) {
				AppLogger.logDebug.error("", e);
			}
		}

		private void doCheckChild(Treeitem itemParent, boolean isCheck) {
			itemParent.setOpen(true);
			TreeNode<SysMenu> nodeParent = itemParent.getValue();
			if (nodeParent.isLeaf()) {
				itemParent.setSelected(isCheck);
			} else {
				itemParent.setSelected(isCheck);
				Treechildren treechildren = itemParent.getTreechildren();
				List<Treeitem> lstItem = treechildren.getChildren();
				for (Treeitem treeitem : lstItem) {
					doCheckChild(treeitem, isCheck);
				}
			}
		}
	}

	private List<SysUser> getListUser() {
		List<SysUser> result = new ArrayList<>();
		Session session = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			session = ControllerUtils.getCurrentSession();
			SessionImplementor sip = (SessionImplementor) session;
			conn = sip.connection();
			if (conn != null) {
				String sql = "{call getAllUserTms()}";
				cs = conn.prepareCall(sql);
				rs = cs.executeQuery();
				while (rs.next()) {
					SysUser sysUser = new SysUser();
					sysUser.setId(rs.getInt("id"));
					sysUser.setName(rs.getString("name"));
					sysUser.setUser(rs.getString("user"));
					sysUser.setValue(rs.getString("value"));
					sysUser.setIsActive(rs.getBoolean("isActive"));
					result.add(sysUser);
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					AppLogger.logDebug.error("", e);
				}
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
					AppLogger.logDebug.error("", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					AppLogger.logDebug.error("", e);
				}
			}
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	private List<SysRule> getListRule() {
		List<SysRule> result = new ArrayList<>();
		Session session = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			session = ControllerUtils.getCurrentSession();
			SessionImplementor sip = (SessionImplementor) session;
			conn = sip.connection();
			if (conn != null) {
				String sql = "{call getAllRuleTms()}";
				cs = conn.prepareCall(sql);
				rs = cs.executeQuery();
				while (rs.next()) {
					SysRule sysRule = new SysRule();
					sysRule.setId(rs.getInt("id"));
					sysRule.setName(rs.getString("name"));
					sysRule.setValue(rs.getString("value"));
					sysRule.setIsActive(rs.getBoolean("isActive"));
					sysRule.setModelName(rs.getString("modelName"));
					sysRule.setPriority(rs.getInt("priority"));
					result.add(sysRule);
				}
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					AppLogger.logDebug.error("", e);
				}
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
					AppLogger.logDebug.error("", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					AppLogger.logDebug.error("", e);
				}
			}
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	private List<SysAction> getListActionModel() {
		List<SysAction> result = null;
		try {
			SysActionController controller = (SysActionController) ControllerUtils
					.getController(SysActionController.class);
			result = controller.find("from SysAction");
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
			result = null;
		}
		return result;
	}

	private void refreshListUser() {
		ListModelList<SysUser> lmlSysUser = new ListModelList<SysUser>(lstUserModel);
		lmlSysUser.setMultiple(true);
		lstboxUser.setModel(lmlSysUser);

	}

	private void refreshListRule() {
		ListModelList<SysRule> lmlSysRule = new ListModelList<SysRule>(lstRuleModel);
		lmlSysRule.setMultiple(true);
		lstboxRule.setModel(lmlSysRule);
	}

	private void refreshListFunction() {
		SysGroup model = (SysGroup) this.getModel();
		lstboxFunction.setModel(new ListModelList<SysGroupLine>(model.getSysGroupLines()));
	}
}
