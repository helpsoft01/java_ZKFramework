package com.vietek.taxioperation.componentExtend;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.w3c.dom.ls.LSInput;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listfoot;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.South;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.event.ListDataListener;
import org.zkoss.zul.event.PagingListener;
import org.zkoss.zul.ext.Paginal;

import com.vietek.taxioperation.common.ColumnInfor;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.ui.util.HandleItemListRender;
import com.vietek.taxioperation.ui.util.ListItemRenderer;
import com.vietek.taxioperation.util.ControllerUtils;

public class ListViewExt extends Listbox implements HandleItemListRender {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6156488566260869291L;
	private static final String ATTB_INFORCOLUM = "name";
	private ModelInfor modelInfor;
	private HandlFilterListbox handlFilterListbox;
	private List<GridColumn> lstColus;
	private List<HtmlBasedComponent> lstCompnFilters;
	private final String onEventInit = "onEventInit";
	private final String onEventLoad = "onEventLoadSource";
	private ModelDBList<?> modeSource;
	private ListViewExt _this;
	private List<Object> lstCheckedSelect = null;

	// private Listbox listbox;

	public ListViewExt(ModelInfor modelInfor, HandlFilterListbox handlFilterListbox) {
		// TODO Auto-generated constructor stub
		super();
		_this = this;
		this.modelInfor = modelInfor;
		this.handlFilterListbox = handlFilterListbox;

		this.lstCompnFilters = new ArrayList<>();

		initEvents();

		// Events.postEvent(onEventInit, this, "");
		// Clients.showBusy(this, "Loading....");
	}

	private void initEvents() {
		/*
		 * event
		 */
		// this.paging.addEventListener("onPaging", EVENTSELECT_PAGING);
		this.addEventListener("onCheckSelectAll", EVENTSELECT_ALL);
		// this.addEventListener(onEventInit, EVENTINIT);
	}

	// List<GridColumn> lstCol, String nameGrid
	public void init(List<Object> lst) {

		lstCheckedSelect = lst;
		/*
		 * set source
		 */
		this.setModel(initModel(10));

		/*
		 * set properties
		 */
		this.setMold("paging");//
		// this.setsc
		// this.setAutopaging(true);

		this.setPagingPosition("bottom");
		this.setCheckmark(true);

		if (this.modelInfor.isMany2Many()) {
			this.setMultiple(true);
		}
		// this.setName("");

		this.setVflex("1");
		this.setHflex("1");

		this.setSizedByContent(true);
		this.setNonselectableTags("");
		this.setSclass("");

		ListItemRenderer<Class<?>> renderer = new ListItemRenderer<Class<?>>((ArrayList<GridColumn>) lstColus, this);
		this.setItemRenderer(renderer);
		/*
		 * create head
		 */
		initListboxHead(lstColus);
	}

	private void initListboxHeadFilter(Auxhead filtHead, GridColumn gridCol) {

		Auxheader filtHeader = new Auxheader();
		filtHeader.setParent(filtHead);

		HtmlBasedComponent compn = modelInfor.getComponentByGridColumn(gridCol);
		if (compn == null)
			return;
		else {

			compn.setParent(filtHeader);
			compn.setHflex("1");
			compn.setVflex("1");

			compn.setAttribute(ATTB_INFORCOLUM, gridCol);
			compn.addEventListener(Events.ON_OK, EVENTENTER_FILTER);

			/*
			 * 3.create filter corresponding
			 */
			this.lstCompnFilters.add(compn);
		}
	}

	private void initListboxHeadCol(GridColumn gridCol) {

		Listheader header = null;

		header = new Listheader(gridCol.getHeader());
		if (gridCol.getWidth() != 0) {
			header.setWidth(gridCol.getWidth() + "px");
			header.setStyle("max-width: " + gridCol.getWidth());
		} else {
			if (gridCol.getWidthHflex().equals(""))
				header.setWidth("0px");
			else
				header.setHflex(gridCol.getWidthHflex());
		}
		header.setParent(this.getListhead());
	}

	private void initListboxHead(List<GridColumn> lstCol) {

		if (this.getListhead() != null)
			return;
		Listhead lstHead = new Listhead();
		lstHead.setParent(this);
		lstHead.setSizable(true);

		Auxhead filtHead = new Auxhead();
		filtHead.setParent(this);

		for (int i = 0; i < lstCol.size(); i++) {

			GridColumn gridCol = lstCol.get(i);
			initListboxHeadCol(gridCol);
			initListboxHeadFilter(filtHead, gridCol);
		}
	}

	/**
	 * init columns
	 * 
	 * @return
	 */
	private List<GridColumn> initColumns() {

		ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();

		for (Field field : modelInfor.getFields()) {

			GridColumn cl = modelInfor.getGridColumn(field);
			if (cl != null)
				lstCols.add(cl);
		}
		return lstCols;
	}

	private String getQueryWhere() {
		StringBuilder result = new StringBuilder("");

		for (HtmlBasedComponent compnFilter : lstCompnFilters) {

			result.append(getQueryWhereByComponent(compnFilter));
		}

		return result.toString();
	}

	private String getQueryWhereByComponent(HtmlBasedComponent compn) {

		StringBuilder result = new StringBuilder("");
		/*
		 * get value of current component
		 */
		if (compn != null) {

			GridColumn col = null;

			col = (GridColumn) compn.getAttribute(ATTB_INFORCOLUM);

			if (compn.getClass().equals(Textbox.class)) {

				Textbox tb = (Textbox) compn;

				if (tb.getValue() != null && !tb.getValue().trim().isEmpty()) {
					result.append(" and ");
					result.append(col.getFieldName());
					result.append(" like '%");
					result.append(tb.getValue());
					result.append("%'");
				}

			} else if (compn.getClass().equals(Decimalbox.class)) {

				Decimalbox tb = (Decimalbox) compn;
				if (tb.getValue() != null && !tb.getValue().equals(0)) {
					result.append(" and ");
					result.append(col.getFieldName());
					result.append(" = ");
					result.append(tb.getValue());
					result.append(" ");
				}

			} else if (compn.getClass().equals(Datebox.class)) {

				Datebox tb = (Datebox) compn;
				if (tb.getValue() != null && !tb.getValue().equals(0)) {
					result.append(" and ");
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(tb.getValue().getTime());
					result.append(col.getFieldName());
					result.append(" = ");
					result.append(tb.getValue());
					result.append(" ");
				}

			} else if (compn.getClass().equals(Checkbox.class)) {

				Checkbox tb = (Checkbox) compn;
				if (tb.isChecked()) {
					result.append(" and ");
					result.append(col.getFieldName());
					result.append(" is true ");
				}
			}
		}
		return result.toString();

	}

	public ModelDBList<?> initModel(int size) {

		ModelDBList<?> retVal = new ModelDBList<Object>(size) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected int loadTotalSize() {
				int totalSize = 0;
				Session session = ControllerUtils.getCurrentSession();
				try {
					if (!session.isOpen() || !session.isConnected())
						return totalSize;

					Query query;
					queryWhere = getQueryWhere();
					String sql = String.format("select count(*) from %s where 1=1 %s", modelInfor.getNameModel(),
							queryWhere);

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
					queryWhere = getQueryWhere();
					String sql = String.format("from %s where 1=1 %s", modelInfor.getNameModel(), queryWhere);
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
		};

		this.setAttribute("org.zkoss.zul.listbox.rod", String.valueOf(true));
		this.setAttribute("org.zkoss.zul.listbox.initRodSize", String.valueOf(retVal.getPageSize()));

		return retVal;
	}

	public List<?> getCheckItems() {

		List<?> result = new ArrayList<>();
		for (Listitem item : this.getSelectedItems()) {
			result.add(item.getValue());
		}
		return result;
	}

	private String queryWhere = "";

	private EventListener<Event> EVENTSELECT_ALL = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
		}
	};

	@Override
	public void onRender(Listitem item, Object data, int index) {

		if (lstCheckedSelect != null) {
			for (Object object : lstCheckedSelect) {
				if (object != null)
					if (object.equals(data)) {
						ModelDBList<Object> model = (ModelDBList<Object>) getModel();
						if (index < model.getCache().values().size() * getPageSize()) {
							item.setSelectable(true);
							item.setSelected(true);
						}
						break;
					}
			}
		}
	}

	@Override
	protected void renderProperties(ContentRenderer arg0) throws IOException {
		// TODO Auto-generated method stub
		super.renderProperties(arg0);
		if (this.modelInfor.isMany2Many()) {
			this.setMultiple(true);
		}
	}

	private EventListener<Event> EVENTENTER_FILTER = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {

			setModel(initModel(10));
			if (modelInfor.isMany2Many()) {
				setMultiple(true);
			}
			setAttribute("org.zkoss.zul.listbox.rod", String.valueOf(true));
			setAttribute("org.zkoss.zul.listbox.initRodSize", String.valueOf(getPageSize()));
		}

	};
	private EventListener<Event> EVENT_SELECT_LB = new EventListener<Event>() {

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub

			handlFilterListbox.onSelectItems(null);
		}
	};

	public ModelInfor getModelInfor() {
		return modelInfor;
	}

	public void setModelInfor(ModelInfor modelInfor) {
		this.modelInfor = modelInfor;
		this.lstColus = initColumns();
	}

}
