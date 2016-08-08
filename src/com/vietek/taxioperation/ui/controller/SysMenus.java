package com.vietek.taxioperation.ui.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import com.vietek.taxioperation.common.AnnonationLinkedTable;
import com.vietek.taxioperation.common.FixedCombobox;
import com.vietek.taxioperation.common.ListCommon;
import com.vietek.taxioperation.controller.SysMenuController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.SysMenu;
import com.vietek.taxioperation.ui.util.AbstractWindowPanel;
import com.vietek.taxioperation.ui.util.BasicDetailWindow;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

/**
 * 
 * @author VuD
 * 
 */

public class SysMenus extends AbstractWindowPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SysMenusDetail detailWindow = null;
	private List<GridColumn> lstCols = new ArrayList<>();
	private Tree tree;

	public SysMenus() {
		super(true);
	}

	@Override
	public void beforInit() {
		super.beforInit();
		this.setDisplayLeftPanel(false);
	}

	@Override
	public void initLeftPanel() {

	}

	@Override
	public void initDetailRightPanel() {
		this.initColumnsTree();
		tree = new Tree();
		tree.setParent(this.getVlayout());
		tree.setVflex("1");
		tree.setHflex("1");
		Treecols treecols = new Treecols();
		treecols.setParent(tree);
		for (GridColumn gridColumn : lstCols) {
			Treecol treecol = new Treecol();
			treecol.setParent(treecols);
			treecol.setLabel(gridColumn.getHeader());
			treecol.setWidth(gridColumn.getWidth() + "px");
			treecol.setStyle("text-align:center;");
		}
		SysMenuTreeNode root = this.getMenuNode();
		tree.setModel(new SysMenuTreeModel(root));
		tree.setItemRenderer(new SysMenuTreeRender(lstCols));
	}

	@Override
	public void initColumns() {
		List<GridColumn> lstCols = new ArrayList<GridColumn>();
		lstCols.add(new GridColumn("Tên nhóm", 200, String.class, "getName"));
		lstCols.add(new GridColumn("Function", 200, String.class, "getFunction"));
		// lstCols.add(new GridColumn("Loại", 100, Integer.class, "getType"));
		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class, "getIsActive"));
		lstCols.add(new GridColumn("Menu cha", 200, String.class, "getParentId", "parentId", SysMenu.class));
		lstCols.add(new GridColumn("Thứ tự", 100, Integer.class, "getSequence"));
		this.setGridColumns((ArrayList<GridColumn>) lstCols);
		this.lstCols.addAll(lstCols);
	}

	private void initColumnsTree() {
		lstCols = new ArrayList<>();
		lstCols.add(new GridColumn("Tên nhóm", 400, String.class, "getName"));
		lstCols.add(new GridColumn("Function", 200, String.class, "getFunction"));
		// lstCols.add(new GridColumn("Loại", 100, Integer.class, "getType"));
		lstCols.add(new GridColumn("Kích hoạt", 100, Boolean.class, "getIsActive"));
		lstCols.add(new GridColumn("Menu cha", 200, String.class, "getParentId", "parentId", SysMenu.class));
		lstCols.add(new GridColumn("Thứ tự", 100, Integer.class, "getSequence"));
	}

	@Override
	public void loadData() {
		// SysMenuController controller = (SysMenuController)
		// ControllerUtils.getController(SysMenuController.class);
		// List<SysMenu> lstModel = controller.find("from SysMenu");
		this.setModelClass(SysMenu.class);
		this.setStrQuery("from SysMenu");
		// this.setLstModel(lstModel);

	}

	@Override
	public BasicDetailWindow modifyDetailWindow() {
		if (detailWindow == null) {
			detailWindow = new SysMenusDetail(getCurrentModel(), this);
		}
		return detailWindow;
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(this.getBt_edit())) {
			if (event.getName().equals(Events.ON_CLICK)) {
				if (this.getCurrentModel() != null) {
					setCurrentModel(this.getCurrentModel());
					showDetail(this.getCurrentModel());
					return;
				}
			}
		}
		super.onEvent(event);
	}

	private SysMenuTreeNode getMenuNode() {
		SysMenuTreeNode root = null;
		SysMenuController controller = (SysMenuController) ControllerUtils.getController(SysMenuController.class);
		List<SysMenu> lstValue = controller.find("from SysMenu order by sequence");
		if (lstValue != null) {
			root = this.getChildNode(null, lstValue);
		}
		return root;
	}

	private SysMenuTreeNode getChildNode(SysMenu parent, List<SysMenu> lstValue) {
		if (parent != null) {
			List<SysMenu> lstChildSysMenu = this.getListChild(parent, lstValue);
			if (lstChildSysMenu.isEmpty()) {
				SysMenuTreeNode leaf = new SysMenuTreeNode(parent);
				leaf.setLeaf(true);
				return leaf;
			} else {
				List<SysMenuTreeNode> lstChild = new ArrayList<>();
				for (SysMenu sysMenu : lstChildSysMenu) {
					SysMenuTreeNode child = this.getChildNode(sysMenu, lstValue);
					lstChild.add(child);
				}
				SysMenuTreeNode superChild = new SysMenuTreeNode(parent, lstChild);
				return superChild;
			}
		} else {
			List<SysMenu> lstOneChild = new ArrayList<>();
			for (SysMenu sysMenu : lstValue) {
				if (sysMenu.getParentId() == 0) {
					lstOneChild.add(sysMenu);
				}
			}
			List<SysMenuTreeNode> lstChild = new ArrayList<>();
			for (SysMenu sysMenu : lstOneChild) {
				SysMenuTreeNode child = this.getChildNode(sysMenu, lstValue);
				lstChild.add(child);
			}
			SysMenuTreeNode root = new SysMenuTreeNode(parent, lstChild);
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

	@Override
	public void refresh() {
		SysMenuTreeNode root = this.getMenuNode();
		tree.setModel(new SysMenuTreeModel(root));
	}

	@Override
	public boolean handleEventDelete() {
		if (this.beforDelete()) {
			int oldid = ((SysMenu) this.getCurrentModel()).getId();
			if (super.handleEventDelete()) {
				synchronized (ListCommon.LIST_SYS_MENU) {
					SysMenu oldMenu = null;
					for (SysMenu sysMenu : ListCommon.LIST_SYS_MENU) {
						if (sysMenu.getId() == oldid) {
							oldMenu = sysMenu;
							break;
						}
					}
					if (oldMenu != null) {
						ListCommon.LIST_SYS_MENU.remove(oldMenu);
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
		getBt_delete().setDisabled(true);
		getBt_edit().setDisabled(true);
		return true;
	}

	private boolean beforDelete() {
		SysMenuController controller = (SysMenuController) ControllerUtils.getController(SysMenuController.class);
		List<SysMenu> lstMenu = controller.find("from SysMenu where parentId = ?",
				AbstractModel.getValue(this.getCurrentModel(), "id"));
		if (lstMenu != null && lstMenu.size() > 0) {
			Env.getHomePage().showNotification("Phải xóa menu con trước !", Clients.NOTIFICATION_TYPE_ERROR);
			return false;
		}
		return true;
	}

	public void refreshTree() {
		SysMenuTreeNode root = this.getMenuNode();
		tree.setModel(new SysMenuTreeModel(root));
		this.getBt_delete().setDisabled(true);
		this.getBt_edit().setDisabled(true);
	}

	private class SysMenuTreeNode extends DefaultTreeNode<SysMenu> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean isOpen = false;
		private boolean isLeaf = false;

		public SysMenuTreeNode(SysMenu data, boolean nullAsMax) {
			super(data, nullAsMax);
		}

		public SysMenuTreeNode(SysMenu data, Collection<? extends TreeNode<SysMenu>> lstChildren, boolean arg2) {
			super(data, lstChildren, arg2);
		}

		public SysMenuTreeNode(SysMenu data, Collection<? extends TreeNode<SysMenu>> children) {
			super(data, children);
		}

		public SysMenuTreeNode(SysMenu data, TreeNode<SysMenu>[] children) {
			super(data, children);
		}

		public SysMenuTreeNode(SysMenu data) {
			super(data);
		}

		@SuppressWarnings("unused")
		public boolean isOpen() {
			return isOpen;
		}

		@SuppressWarnings("unused")
		public void setOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}

		public boolean isLeaf() {
			return isLeaf;
		}

		public void setLeaf(boolean isLeaf) {
			this.isLeaf = isLeaf;
		}

	}

	private class SysMenuTreeModel extends DefaultTreeModel<SysMenu> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SysMenuTreeModel(TreeNode<SysMenu> root) {
			super(root);
		}

	}

	private class SysMenuTreeRender implements TreeitemRenderer<SysMenuTreeNode> {
		private List<GridColumn> lstcol;
		private Map<String, List<?>> mapLinkTable = new HashMap<String, List<?>>();

		public SysMenuTreeRender(List<GridColumn> lstcol) {
			super();
			this.lstcol = lstcol;
		}

		@Override
		public void render(Treeitem treeItem, SysMenuTreeNode treeNode, int index) throws Exception {
			SysMenu sysmenu = treeNode.getData();
			treeItem.setValue(treeNode);
			final Treerow treeRow = new Treerow();
			treeRow.setParent(treeItem);

			for (int i = 0; i < lstcol.size(); i++) {
				Treecell cell = new Treecell();
				cell.setParent(treeRow);
				if (i == 0) {
					if (!treeNode.isLeaf) {
						cell.setStyle("font-weight: bold");
					}
				}
				GridColumn column = lstcol.get(i);
				Method method = sysmenu.getClass().getMethod(column.getGetDataMethod());
				Object val = method.invoke(sysmenu);
				if (column.getFieldName() != null && column.getFieldName().length() > 0) {
					Field field = column.getModelClazz().getDeclaredField(column.getFieldName());
					if (field.isAnnotationPresent(FixedCombobox.class)) {
						this.createCellFixedCombobox(cell, field, val);
						continue;
					} else if (field.isAnnotationPresent(AnnonationLinkedTable.class)) {
						this.createCellLinkedMap(cell, field, val);
						continue;
					}
				}
				if (column.getClazz().equals(Boolean.class)) {
					Checkbox checkbox = new Checkbox();
					checkbox.setLabel("");
					if (val == null || !(boolean) val) {
						checkbox.setChecked(false);
					} else {
						checkbox.setChecked(true);
					}
					cell.appendChild(checkbox);
				} else {
					cell.setLabel(val == null ? "" : val.toString());
				}
			}

			treeRow.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					getBt_delete().setDisabled(false);
					getBt_edit().setDisabled(false);
				}
			});

			treeRow.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					Treeitem item = (Treeitem) treeRow.getParent();
					SysMenuTreeNode node = item.getValue();
					SysMenu model = node.getData();
					setCurrentModel(model);
					getBt_delete().setDisabled(false);
					getBt_edit().setDisabled(false);
				}
			});

			treeRow.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					Treeitem item = (Treeitem) treeRow.getParent();
					SysMenuTreeNode node = item.getValue();
					SysMenu model = node.getData();
					setCurrentModel(model);
					showDetail(model);
				}
			});
		}

		private void createCellFixedCombobox(Treecell cell, Field field, Object val) {
			if (val == null) {
				cell.setLabel(val == null ? "" : val.toString());
			} else {
				Annotation ann = field.getAnnotation(FixedCombobox.class);
				FixedCombobox fixedCombobox = (FixedCombobox) ann;
				for (int j = 0; j < fixedCombobox.value().length; j++) {
					if (fixedCombobox.value()[j] == (int) val) {
						cell.setLabel(fixedCombobox.label()[j]);
						break;
					}
				}
			}
		}

		private void createCellLinkedMap(Treecell cell, Field field, Object val)
				throws ClassNotFoundException, NoSuchFieldException, SecurityException {
			if (val == null || (int) val == 0) {
				cell.setLabel("");
			} else {
				Annotation ann = field.getAnnotation(AnnonationLinkedTable.class);
				AnnonationLinkedTable annLinkedTable = (AnnonationLinkedTable) ann;
				String clazzName = "com.vietek.taxioperation.model." + annLinkedTable.modelClazz();
				Class<? extends Object> clazz = Class.forName(clazzName);
				String modelClazz = annLinkedTable.modelClazz();
				List<?> lstValue = mapLinkTable.get(modelClazz);
				if (lstValue == null) {
					lstValue = this.getListValue(modelClazz);
					mapLinkTable.put(modelClazz, lstValue);

				}
				if (lstValue != null) {
					for (Object object : lstValue) {
						if (object.getClass() == clazz) {
							Field fieldTmp = clazz.getDeclaredField("id");
							try {
								fieldTmp.setAccessible(true);
								int id = fieldTmp.getInt(object);
								if (id == (int) val) {
									fieldTmp = clazz.getDeclaredField(annLinkedTable.displayFieldName());
									fieldTmp.setAccessible(true);
									String labelValue = (String) fieldTmp.get(object);
									cell.setLabel(labelValue);
									break;
								}
							} catch (Exception e) {
								cell.setLabel("");
								e.printStackTrace();
							}

						}
					}

				}
			}
		}

		private List<?> getListValue(String modelClazz) {
			String controllerName = "com.vietek.taxioperation.controller." + modelClazz + "Controller";
			String whereString = "";
			List<?> lstModel = null;
			try {
				lstModel = ControllerUtils.getController(Class.forName(controllerName))
						.find("from " + modelClazz + whereString);
			} catch (Exception e) {
				lstModel = null;
				e.printStackTrace();
			}
			return lstModel;
		}

	}

}
