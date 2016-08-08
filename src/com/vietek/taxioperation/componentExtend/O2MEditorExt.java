package com.vietek.taxioperation.componentExtend;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.vietek.taxioperation.ui.editor.Editor;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.ui.util.HandleItemListRender;
import com.vietek.taxioperation.ui.util.ListItemRenderer;
import com.vietek.taxioperation.util.ControllerUtils;

public class O2MEditorExt extends Editor implements InterfaceUI, HandleItemListRender, HandleWindowEditor {

	private Listbox listbox;
	private ModelInfor modelInfor;
	private Button btNewRecord;
	private List<GridColumn> lstColus;
	WindowRecordEditor wRecordEditor;
	protected O2MEditorExt _this;
	ModelDBList modelDBList;

	public O2MEditorExt(Object model, String field) {

		this.modelInfor = new ModelInfor(model, field);
		this._this = this;
		initValue();
		initUI();
		initEvents();
	}

	@Override
	public void initValue() {

	}

	@Override
	public void initUI() {

		listbox = new Listbox();
		component = listbox;

		// this.setsc
		listbox.setAutopaging(false);

		listbox.setPagingPosition("bottom");
		listbox.setCheckmark(true);

		listbox.setMultiple(false);
		// this.setName("");

		listbox.setVflex("1");
		listbox.setHflex("1");

		listbox.setSizedByContent(true);
		listbox.setNonselectableTags("");
		listbox.setSclass("");

		/*
		 * create head
		 */
		List<GridColumn> lstColus = getLstColus();
		initListboxHead(lstColus);
		/*
		 * item render
		 */
		ListItemRenderer<Class<?>> renderer = new ListItemRenderer<Class<?>>((ArrayList<GridColumn>) lstColus, this);
		listbox.setItemRenderer(renderer);

		modelDBList = initModel(200);
		listbox.setModel(modelDBList);

		/*
		 * set properties
		 */
		listbox.setMold("paging");//
	}

	@Override
	public void initEvents() {

		btNewRecord.addEventListener(Events.ON_CLICK, EVENT_ONCLICK_NEWRECORD);
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public void setValue(Object value) {

	}

	@Override
	public void setRows(Object value) {
	}

	@Override
	public void onRender(Listitem item, Object data, int index) {
		Listcell cell = new Listcell();
		cell.setParent(item);
		Button bt_edit = new Button("Sửa");
		bt_edit.setSclass("btn-warning");
		bt_edit.setParent(cell);
		bt_edit.setAutodisable("self");
		//
		cell = new Listcell();
		cell.setParent(item);
		Button bt_delete = new Button("Xoá");
		bt_delete.setSclass("btn-danger");
		bt_delete.setParent(cell);
		bt_delete.setAutodisable("self");
		/*
		 * events
		 */
		bt_edit.addEventListener(Events.ON_CLICK, EVENT_ONCLICK_EDITORRECORD);
		bt_delete.addEventListener(Events.ON_CLICK, EVENT_ONCLICK_DELETERECORD);
	}

	private void initListboxHead(List<GridColumn> lstCol) {

		if (listbox.getListhead() != null)
			return;
		Listhead lstHead = new Listhead();
		lstHead.setParent(listbox);
		lstHead.setSizable(true);

		initHeadGroup(lstCol);

		initHeadCols(lstCol);
	}

	private void initHeadGroup(List<GridColumn> lstCol) {

		Auxhead head = new Auxhead();
		head.setParent(listbox);

		Auxheader header = new Auxheader();
		header.setParent(head);
		header.setColspan(lstCol.size());

		Button bt = new Button("New Record");
		bt.setParent(header);

		btNewRecord = bt;
	}

	private void initHeadCols(List<GridColumn> lstCol) {

		for (int i = 0; i < lstCol.size(); i++) {

			GridColumn gridCol = lstCol.get(i);
			Listheader header = new Listheader(gridCol.getHeader());

			if (gridCol.getWidth() != 0) {
				header.setWidth(gridCol.getWidth() + "px");
				header.setStyle("max-width: " + gridCol.getWidth());
			} else {
				if (gridCol.getWidthHflex().equals(""))
					header.setWidth("0px");
				else
					header.setHflex(gridCol.getWidthHflex());
			}
			header.setParent(listbox.getListhead());
		}
		Listheader header = new Listheader("Edit");
		header.setParent(listbox.getListhead());
		header = new Listheader("Delete");
		header.setParent(listbox.getListhead());
	}

	public List<GridColumn> getLstColus() {
		if (lstColus == null)
			lstColus = initColumns();
		return lstColus;
	}

	public void setLstColus(List<GridColumn> lstColus) {
		this.lstColus = lstColus;
	}

	private List<GridColumn> initColumns() {

		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();

		for (Field field : modelInfor.getFields()) {

			GridColumn cl = modelInfor.getGridColumn(field);
			if (cl != null)
				lstCols.add(cl);
		}

		return lstCols;
	}

	public ModelDBList<?> initModel(int size) {

		ModelDBList<?> retVal = new ModelDBList<Object>(size) {
			/**
			 * 
			 */

			@Override
			protected int loadTotalSize() {
				int totalSize = 0;
				Session session = ControllerUtils.getCurrentSession();
				try {
					if (!session.isOpen() || !session.isConnected())
						return totalSize;

					Query query;
					String sql = String.format("select count(*) from %s where 1=1 ", modelInfor.getNameModel());

					query = session.createQuery(sql);
					totalSize = ((Number) query.uniqueResult()).intValue();
				} catch (Exception ex) {

				} finally {
					session.close();
				}
				return totalSize;
			}

			@Override
			protected List<Object> loadFromDB(int startIndex, int size) {

				List<Object> result = new ArrayList<Object>();
				Session session = ControllerUtils.getCurrentSession();
				try {
					if (startIndex == -1 && size == -1)
						return result;

					if (!session.isOpen() || !session.isConnected())
						return result;

					Query query;
					String sql = String.format("from %s where 1=1 ", modelInfor.getNameModel());
					query = session.createQuery(sql);
					query.setFirstResult(startIndex);
					query.setMaxResults(size);
					query.setCacheable(true);

					result = query.list();

				} catch (Exception ex) {

				} finally {
					session.close();
				}
				return result;
			}

			@Override
			public void addRecord(Object record) {
				// TODO Auto-generated method stub
				super.addRecord(record);
			}
		};

		listbox.setAttribute("org.zkoss.zul.listbox.rod", String.valueOf(true));
		listbox.setAttribute("org.zkoss.zul.listbox.initRodSize", String.valueOf(retVal.getPageSize()));

		return retVal;
	}

	public Object getCheckItem() {

		Object retVal = null;
		List<Object> objs = getCheckItems();
		if (objs.size() > 0) {
			retVal = objs.get(0);
		}
		return retVal;
	}

	public List<Object> getCheckItems() {

		List<Object> result = new ArrayList<>();
		for (Listitem item : listbox.getSelectedItems()) {
			result.add(item.getValue());
		}
		return result;
	}

	/*
	 * =============================================== ====================
	 * EVENTS====================
	 * ===============================================
	 */

	private EventListener<Event> EVENT_ONCLICK_EDITORRECORD = new EventListener<Event>() {
		@Override
		public void onEvent(Event arg0) throws Exception {

			Object obj = getCheckItem();
			if (obj != null) {
				if (wRecordEditor == null) {
					wRecordEditor = new WindowRecordEditor(obj, arg0.getTarget(), _this);
					wRecordEditor.setColus(getLstColus());
				}
				// wRecordEditor.setAction(EnumAction.EDIT_ACTION);
				wRecordEditor.setModelObj(obj, EnumAction.EDIT_ACTION);
				wRecordEditor.show();
			}
		}
	};
	private EventListener<Event> EVENT_ONCLICK_DELETERECORD = new EventListener<Event>() {
		@Override
		public void onEvent(Event arg0) throws Exception {
		}
	};
	private EventListener<Event> EVENT_ONCLICK_NEWRECORD = new EventListener<Event>() {
		@Override
		public void onEvent(Event arg0) throws Exception {
			if (wRecordEditor == null) {
				wRecordEditor = new WindowRecordEditor(null, arg0.getTarget(), _this);
				wRecordEditor.setColus(getLstColus());
			}
			Object obj = modelInfor.newInstance();
			// wRecordEditor.setAction(EnumAction.NEW_ACTION);
			wRecordEditor.setModelObj(obj, EnumAction.NEW_ACTION);
			wRecordEditor.show();
		}
	};

	@Override
	public void onOk(Object obj) {

	}

	@Override
	public void onCancel() {
	}

	@Override
	public void onOk(Object obj, EnumAction action) {
		switch (action) {
		case EDIT_ACTION:
			listbox.setModel(listbox.getModel());
			break;
		case NEW_ACTION:
			modelDBList.addRecord(obj);
			listbox.setModel(listbox.getModel());

			int index = listbox.getItems().size() - 1;
			// listbox.setSelectedIndex(index);// modelDBList.getTotalSize()
			// -
			// 1);
			Listitem item = listbox.getItems().get(index);
			item.setSelectable(true);
			listbox.setSelectedItem(item);
			listbox.getSelectedItem().focus();
			break;
		case DELETE_ACTION:
			break;
		default:
			break;
		}
	}

	@Override
	public void setEmpty() {
		// TODO Auto-generated method stub

	}
}
