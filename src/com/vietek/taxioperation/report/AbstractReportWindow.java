package com.vietek.taxioperation.report;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.Searchable;
import com.vietek.taxioperation.common.VehicleApi;
import com.vietek.taxioperation.model.Agent;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.ReportModel;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.ui.util.ComponentsReport;
import com.vietek.taxioperation.ui.util.GridColumn;
import com.vietek.taxioperation.ui.util.GridRow;
import com.vietek.taxioperation.ui.util.ItemRenderReport;
import com.vietek.taxioperation.ui.util.VChosenbox;
import com.vietek.taxioperation.ui.util.VTReportViewer;
import com.vietek.taxioperation.util.CommonUtils;
import com.vietek.taxioperation.util.Env;
import com.vietek.tracking.ui.model.PredicateSearch;
import com.vietek.tracking.ui.model.PredicateUtil;

public abstract class AbstractReportWindow extends Window implements EventListener<Event>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Grid gridFilter;
	protected Grid gridData;
	protected Listbox listbox;

	Div leftPanel = new Div();
	Div rightPanel = new Div();
	Vlayout vlayout;
	Hlayout hlayout;

	Button btnSearch = new Button("Tìm kiếm");
	Button btnReport = new Button("Bản in");
	Button btnExcel = new Button("Excel");
	String lbTitleReport = new String("");
	String lbTitleInput = new String("");
	int gridWidthCols;
	int countCols;
	boolean renderReportWithListBox = true;
	String reportFile = new String("");
	String reportTitle = new String("");
	String exportFileName = new String("");
	String reportName = new String("");
	String user = Env.getHomePage().getSysUser().getUser();

	List<?> lstModel;
	Class<?> modelClass;
	protected AbstractListModel<?> listModel;
	HashMap<String, Field> mapSearch = new HashMap<String, Field>();
	Map<String, Object> mapParamsReport = new HashMap<String, Object>();

	private ArrayList<GridColumn> lstCols = new ArrayList<GridColumn>();
	private ArrayList<GridRow> lstRows = new ArrayList<GridRow>();

	Datebox dateFrom;
	Datebox dateTo;

	Chosenbox chosenAgent;
	Chosenbox chosenGroup;
	Chosenbox chosenVehicle;
	Chosenbox chosenDriver;
	Textbox txtVehicleNumber;
	Textbox txtLicensePlate;

	StringBuilder strAgentId;
	StringBuilder strGroupId;
	StringBuilder strPakingAreaId;
	StringBuilder strVehicleId;
	StringBuilder strDriverId;
	int deviceId;
	List<PredicateSearch> lstPredicate;

	public AbstractReportWindow() {
		beforeInit();
		setTitleReport();
		init();
		loadModel();
		renderReportWithListBox();
		initDetailPanel();
	}

	public abstract void loadModel();

	public abstract void initColumns();

	public abstract void setTitleReport();

	public abstract void renderReportWithListBox();

	public void beforeInit() {
	}

	public abstract void loadData();

	public void init() {
		this.setVflex("1");
		this.setHflex("1");
		Hlayout homeLayout = new Hlayout();
		homeLayout.setVflex("1");
		homeLayout.setHflex("1");
		homeLayout.setParent(this);
		rightPanel.setParent(homeLayout);
		rightPanel.setVflex("1");
		rightPanel.setHflex("1");
	}

	public void initDetailPanel() {

		hlayout = new Hlayout();
		hlayout.setParent(rightPanel);
		hlayout.setHflex("1");
		hlayout.setVflex("1");

		this.initDetailLeftPanel();
		this.initDetailRightPanel();
	}

	public void initDetailLeftPanel() {
		gridFilter = new Grid();
		gridFilter.setParent(hlayout);
		gridFilter.setVflex("1");
		gridFilter.setHflex("2");
		Auxhead aux = new Auxhead();
		aux.setParent(gridFilter);
		Auxheader auxh = new Auxheader();
		auxh.setParent(aux);
		auxh.setColspan(2);
		auxh.setLabel(lbTitleInput);
		auxh.setStyle("color : black; font-size : 14px; font-weight : bold; margin-left : 50px");
		Columns cols = new Columns();
		cols.setParent(gridFilter);
		Column col = new Column();
		col.setParent(cols);
		col.setWidth("35%");

		col = new Column();
		col.setParent(cols);
		col.setWidth("65%");

		Rows rows = new Rows();
		rows.setParent(gridFilter);

		initRows(rows);

		initExtraRows(rows);

		if (lstRows != null && lstRows.size() > 0) {

			for (int i = 0; i < lstRows.size(); i++) {
				GridRow gridRow = lstRows.get(i);
				Label label = new Label();
				Row row = new Row();
				row.setParent(rows);

				label.setValue(gridRow.getLabel());
				label.setParent(row);
				label.setStyle("color : black; font-weight : bold; ");

				Component comp = gridRow.getComponent();
				comp.setParent(row);

			}
		}

		Row row = new Row();
		row.setParent(rows);
		Label label = new Label("");
		label.setParent(row);
		label = new Label("");
		label.setParent(row);

		row = new Row();
		row.setParent(rows);

		Cell cell = new Cell();
		cell.setParent(row);
		cell.setColspan(2);

		Hlayout hl = new Hlayout();
		hl.setParent(cell);

		Hbox hbox = new Hbox();
		hbox.setParent(hl);
		btnSearch.setParent(hbox);
		btnSearch.setStyle("color : black; font-weight : bold; font-size : 13px");
		btnSearch.addEventListener(Events.ON_CLICK, this);

		hbox = new Hbox();
		hbox.setParent(hl);
		btnReport.setParent(hbox);
		btnReport.setStyle("color : black; font-weight : bold; font-size : 13px");
		btnReport.addEventListener(Events.ON_CLICK, this);

		hbox = new Hbox();
		hbox.setParent(hl);
		btnExcel.setParent(hbox);
		btnExcel.setStyle("color : black; font-weight : bold; font-size : 13px");
		btnExcel.addEventListener(Events.ON_CLICK, this);

	}

	public void initDetailRightPanel() {

		// Setup Columns for GridData
		initColumns();

		// Load annotation for class model
		if (modelClass != null) {
			Field[] fields = modelClass.getDeclaredFields();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					ArrayList<Annotation> anns = new ArrayList<Annotation>(Arrays.asList(field.getAnnotations()));
					if (anns != null) {
						for (int j = 0; j < anns.size(); j++) {
							if (anns.get(j).annotationType().equals(Searchable.class)) {
								if (mapSearch == null) {
									mapSearch = new HashMap<String, Field>();
								}
								mapSearch.put(field.getName(), field);
							}
						}
					}
				}
			}
		}

		setupColumns();
	}

	public void setupColumns() {

		int temp = 0;
		int count = 0;
		for (GridColumn gridColumn : lstCols) {
			temp += gridColumn.getWidth();
			count++;
		}
		setGridWidthCols(temp);
		setCountCols(count);

		if (renderReportWithListBox == true) {

			// Hien thi voi Listbox
			listbox = new Listbox();
			listbox.setParent(hlayout);
			listbox.setHflex("8");
			listbox.setMold("paging");
			listbox.setAutopaging(true);
			listbox.setVflex("1");
			Frozen frozen = new Frozen();
			frozen.setParent(listbox);
			frozen.setColumns(2);

			Auxhead aux = new Auxhead();
			aux.setParent(listbox);
			Auxheader auxh = new Auxheader();
			auxh.setParent(aux);
			auxh.setColspan(getCountCols());
			auxh.setLabel(lbTitleReport);
			auxh.setStyle("color : black; font-size : 14px; font-weight : bold; margin-left : 50px");

			this.createHeaderExtra(listbox);

			Listhead head = new Listhead();
			head.setParent(listbox);
			head.setMenupopup("auto");
			head.setSizable(true);
			// Setup Columns
			Auxhead auxs = new Auxhead();
			auxs.setParent(listbox);
			Listheader header = null;
			Auxheader auxheader = null;
			for (int i = 0; i < lstCols.size(); i++) {
				GridColumn gridCol = lstCols.get(i);
				header = new Listheader(gridCol.getHeader());
				if (i == lstCols.size() - 1 && i < 5) {
					header.setHflex("1");
				} else {
					header.setWidth(gridCol.getWidth() + "px");
				}
				header.setStyle("text-align : left");
				header.setParent(head);
				auxheader = new Auxheader();
				auxheader.setParent(auxs);
				auxheader.setStyle("");
				if (mapSearch.get(gridCol.getFieldName()) != null) {
					Field field = mapSearch.get(gridCol.getFieldName());
					Searchable search = field.getAnnotation(Searchable.class);
					auxheader.appendChild(new Image("./themes/images/filter.png"));
					createSearchComponent(auxheader, field.getType(), field.getName(), search);
				}
			}
		} else {
			// Display data with Grid
			gridData = new Grid();
			gridData.setParent(hlayout);
			gridData.setHflex("8");
			this.setStyleForGridData();
			// gridData.setMold("paging");
			// gridData.setAutopaging(true);
			// gridData.setPageSize(20);
			// gridData.setVflex("true");
			// gridData.setSclass("grid_report_total");
			Frozen frozen = new Frozen();
			frozen.setParent(gridData);
			frozen.setColumns(2);

			Auxhead aux = new Auxhead();
			aux.setParent(gridData);
			Auxheader auxh = new Auxheader();
			auxh.setParent(aux);
			auxh.setColspan(getCountCols());
			auxh.setLabel(lbTitleReport);
			auxh.setStyle("color : black; font-size : 14px; font-weight : bold; margin-left : 50px");

			this.createHeaderExtraGrid(gridData);

			Columns cols = new Columns();
			cols.setParent(gridData);
			Column col = null;

			Auxhead auxFilter = new Auxhead();
			auxFilter.setParent(gridData);
			Auxheader filter = null;

			for (int i = 0; i < lstCols.size(); i++) {
				GridColumn gridCol = lstCols.get(i);
				col = new Column(gridCol.getHeader());
				if (i == lstCols.size() - 1 && i < 5) {
					col.setHflex("1");
				} else {
					col.setWidth("" + gridCol.getWidth() + "px");
				}
				col.setStyle("text-align : left");
				col.setParent(cols);

				filter = new Auxheader();
				filter.setParent(auxFilter);

				if (mapSearch.get(gridCol.getFieldName()) != null) {
					Field field = mapSearch.get(gridCol.getFieldName());
					Searchable search = field.getAnnotation(Searchable.class);
					filter.appendChild(new Image("./themes/images/filter.png"));
					createSearchComponent(filter, field.getType(), field.getName(), search);
				}
			}

		}

		if (renderReportWithListBox == true) {
			ItemRenderReport<Class<?>> renderers = new ItemRenderReport<Class<?>>(lstCols);
			listbox.setItemRenderer(renderers);
		} else {
			this.renderExtraReport();
			// ItemRenderReport<Class<?>> renderers = new
			// ItemRenderReport<Class<?>>(lstCols);
			// listbox.setItemRenderer(renderers);
		}

	}

	public void renderGrid() {
		this.loadData();
		if (renderReportWithListBox == true) {
			listbox.setModel(new ListModelList<>(lstModel));
		} else {
			gridData.setModel(new ListModelList<>(lstModel));
		}
	}

	public void initRows(Rows rows) {
		ComponentsReport comReport = new ComponentsReport();

		Row row = new Row();
		row.setParent(rows);

		row = new Row();
		row.setParent(rows);
		Label label = new Label("Từ ngày");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);
		dateFrom = comReport.dateboxDisplay(true, row, "dd/MM/yyyy HH:mm:ss", 160, 00, 00, 00);
		dateFrom.setParent(row);

		row = new Row();
		row.setParent(rows);
		label = new Label("Đến ngày");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);
		dateTo = comReport.dateboxDisplay(true, row, "dd/MM/yyyy HH:mm:ss", 160, 23, 59, 59);
		dateTo.setParent(row);

		row = new Row();
		row.setParent(rows);
		label = new Label("Chi nhánh");
		label.setParent(row);
		label.setStyle("color : black; font-weight : bold; ");
		chosenAgent = ComponentsReport.ChosenboxReportInput(Agent.class);
		chosenAgent.setParent(row);
		chosenAgent.addEventListener(Events.ON_SELECT, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				Set<Agent> lstObjChosen = chosenAgent.getSelectedObjects();
				strAgentId = new StringBuilder();
				int temp = 0;
				for (Agent reportQcAgent : lstObjChosen) {
					if (temp < lstObjChosen.size() - 1) {
						strAgentId.append(reportQcAgent.getId()).append(",");
					} else {
						strAgentId.append(reportQcAgent.getId());
					}
					temp++;
				}
				Agent[] arrAgent = lstObjChosen.toArray(new Agent[lstObjChosen.size()]);
				if (arrAgent != null && arrAgent.length > 0) {
					List<TaxiGroup> lstGroup = VehicleApi.getListTaxiGroup(arrAgent);
					if (lstGroup != null && lstGroup.size() > 0) {
						chosenGroup.setModel(new ListModelList<>(lstGroup));
					} else {
						Env.getHomePage().showNotification("Chọn chi nhánh khác cho báo cáo!",
								Clients.NOTIFICATION_TYPE_ERROR);
					}
				}
			}
		});

		row = new Row();
		row.setParent(rows);
		label = new Label("Đội xe");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);

		chosenGroup = ComponentsReport.ChosenboxReportInput(TaxiGroup.class);
		chosenGroup.setParent(row);
		chosenGroup.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				Set<TaxiGroup> lstObjchosen = chosenGroup.getSelectedObjects();
				strGroupId = new StringBuilder();
				int temp = 0;
				for (TaxiGroup taxiGroup : lstObjchosen) {
					if (temp < lstObjchosen.size() - 1) {
						strGroupId.append(taxiGroup.getId()).append(",");
					} else {
						strGroupId.append(taxiGroup.getId());
					}
					temp++;
				}
				TaxiGroup[] arrTaxiGroup = lstObjchosen.toArray(new TaxiGroup[lstObjchosen.size()]);

				if (chosenVehicle.isVisible()) {
					List<Vehicle> lstVehicle = VehicleApi.getVehicle(arrTaxiGroup);
//					chosenVehicle.setModel(new ListModelList<>(lstVehicle));
					((VChosenbox)chosenVehicle).setLstAllModel(lstVehicle);
				}
				if (chosenDriver.isVisible()) {
					List<Driver> lstDriver = VehicleApi.getDriver(arrTaxiGroup);
					chosenDriver.setModel(new ListModelList<>(lstDriver));
				}
			}
		});

		row = new Row();
		row.setParent(rows);
		label = new Label("Xe");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);
		chosenVehicle = ComponentsReport.ChosenboxReportInputDefault(Vehicle.class);
		chosenVehicle.setParent(row);
		chosenVehicle.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				Set<Vehicle> lstObjchosen = chosenVehicle.getSelectedObjects();
				strVehicleId = new StringBuilder();
				int temp = 0;
				for (Vehicle reportVehicle : lstObjchosen) {
					if (temp < lstObjchosen.size() - 1) {
						strVehicleId.append(reportVehicle.getId()).append(",");
					} else {
						strVehicleId.append(reportVehicle.getId());
					}
					temp++;
				}
				for (Vehicle reportVehicle : lstObjchosen) {
					deviceId = reportVehicle.getDeviceID();
					break;
				}				
			}
		});

		row = new Row();
		row.setParent(rows);
		label = new Label("Lái xe");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);

		chosenDriver = ComponentsReport.ChosenboxReportInputDefault(Driver.class);
		chosenDriver.setParent(row);
		chosenDriver.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				Set<Driver> lstObjchosen = chosenDriver.getSelectedObjects();
				strDriverId = new StringBuilder();
				int temp = 0;
				for (Driver driver : lstObjchosen) {
					if (temp < lstObjchosen.size() - 1) {
						strDriverId.append(driver.getId()).append(",");
					} else {
						strDriverId.append(driver.getId());
					}
					temp++;
				}
			}
		});

		row = new Row();
		row.setParent(rows);
		label = new Label("Số tài");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);
		txtVehicleNumber = comReport.textboxInput();
		txtVehicleNumber.setPlaceholder("Số tài");
		txtVehicleNumber.setParent(row);

		row = new Row();
		row.setParent(rows);
		label = new Label("BKS");
		label.setStyle("color : black; font-weight : bold; ");
		label.setParent(row);
		txtLicensePlate = comReport.textboxInput();
		txtLicensePlate.setPlaceholder("Biển kiểm soát");
		txtLicensePlate.setParent(row);
	}

	public void initExtraRows(Rows rows) {

	}

	public void createHeaderExtra(Listbox listbox) {

	}

	public void createHeaderExtraGrid(Grid gridData) {

	}

	public void renderExtraReport() {

	}

	public void setStyleForGridData() {

	}

	public void removeComponent(Component comp, boolean isRemove) {
		if (isRemove == true) {
			Row row = (Row) comp.getParent();

			row.setVisible(false);
			comp.setVisible(false);
			return;
		}
	}

	// Tao cac truong loc trong bao cao
	public void createSearchComponent(Auxheader panelChildren, Class<?> type, String name, Searchable search) {
		if (lstPredicate == null) {
			lstPredicate = new ArrayList<PredicateSearch>();
		}
		if (type.equals(String.class)) {
			Textbox txtb = new Textbox();
			txtb.setErrorboxSclass("z-error-search");
			txtb.setSclass("report-textbox-search");
			txtb.setPlaceholder(search.placehoder());
			PredicateSearch pre = new PredicateSearch(name, "");
			lstPredicate.add(pre);
			txtb.setAttribute("PreSearchindex", lstPredicate.indexOf(pre));
			txtb.addForward(Events.ON_OK, txtb, Events.ON_CHANGE);
			txtb.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					String dataFind = ((Textbox) event.getTarget()).getValue();
					Integer index = (Integer) ((Textbox) event.getTarget()).getAttribute("PreSearchindex");
					((PredicateSearch) lstPredicate.get(index)).setDataSearch(dataFind);
					executeSearch();

				}

			});
			txtb.setWidth("80%");
			txtb.setParent(panelChildren);
		} else if (type.equals(Timestamp.class)) {
			Datebox db = new Datebox();
			db.setFormat("yyyy-MM-dd hh:mm:ss");
			db.setStyle("width:100%; font-style:normal; ");
			db.setPlaceholder(search.placehoder());
			db.setWidth("90%");
			db.setFormat("dd/MM/yyyy");
			PredicateSearch pre = new PredicateSearch(name, new Timestamp(0));
			lstPredicate.add(pre);
			db.setAttribute("PreSearchindex", lstPredicate.indexOf(pre));
			db.addForward(Events.ON_OK, db, Events.ON_CHANGE);
			db.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					Date dataFind = ((Datebox) event.getTarget()).getValue();
					Integer index = (Integer) ((Datebox) event.getTarget()).getAttribute("PreSearchindex");
					if (dataFind != null) {
						((PredicateSearch) lstPredicate.get(index)).setDataSearch(new Timestamp(dataFind.getTime()));
					} else {
						((PredicateSearch) lstPredicate.get(index)).setDataSearch(new Timestamp(0));
					}

					executeSearch();

				};
			});
			db.setParent(panelChildren);
		} else if ((type.equals(Integer.class) || type.equals(int.class))) {

			final Intbox in = new Intbox();
			in.setSclass("report-textbox-search");
			in.setPlaceholder(search.placehoder());
			PredicateSearch pre = new PredicateSearch(name, -1);
			lstPredicate.add(pre);
			in.setAttribute("PreSearchindex", lstPredicate.indexOf(pre));
			in.addForward(Events.ON_OK, in, Events.ON_CHANGE);
			in.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer dataFind = ((Intbox) event.getTarget()).getValue();
					Integer index = (Integer) ((Intbox) event.getTarget()).getAttribute("PreSearchindex");
					if (dataFind != null) {
						((PredicateSearch) lstPredicate.get(index)).setDataSearch(dataFind);
					} else {
						((PredicateSearch) lstPredicate.get(index)).setDataSearch(-1);
					}
					executeSearch();
				};
			});
			in.setWidth("80%");
			in.setParent(panelChildren);
		} else if (type.equals(Double.class) || type.equals(double.class) || type.equals(Float.class)
				|| type.equals(float.class)) {

			final Doublespinner dbl = new Doublespinner();
			dbl.setSclass("report-textbox-search");
			dbl.setStyle("height : 24px");
			dbl.setPlaceholder(search.placehoder());
			PredicateSearch pre = new PredicateSearch(name, 0.0);
			lstPredicate.add(pre);
			dbl.setAttribute("PreSearchindex", lstPredicate.indexOf(pre));
			dbl.addForward(Events.ON_OK, dbl, Events.ON_CHANGE);
			dbl.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					Double dataFind = ((Doublespinner) event.getTarget()).getValue();
					Integer index = (Integer) ((Doublespinner) event.getTarget()).getAttribute("PreSearchindex");
					if (dataFind != null) {
						((PredicateSearch) lstPredicate.get(index)).setDataSearch(dataFind);
					} else {
						((PredicateSearch) lstPredicate.get(index)).setDataSearch(0.0);
					}
					executeSearch();
				};
			});
			dbl.setParent(panelChildren);
		}
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget().equals(btnSearch)) {
			if (event.getName().equals(Events.ON_CLICK)) {

				if (dateTo.getValue().getTime() < dateFrom.getValue().getTime()) {
					Env.getHomePage().showNotification("Hãy chọn lại thời gian cho báo cáo!",
							Clients.NOTIFICATION_TYPE_ERROR);
				} else if (chosenGroup.isVisible() && strGroupId == null) {
					Env.getHomePage().showNotification("Phải chọn ít nhất một đội xe cho báo cáo!",
							Clients.NOTIFICATION_TYPE_ERROR);
				} else {
					this.renderGrid();
				}
			}
		}
		if (event.getTarget().equals(btnExcel)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				Listbox listboxExcel = (Listbox) getListbox();
				Iterator<Component> component = listboxExcel.getChildren().iterator();
				while (component.hasNext()) {
					Component value = component.next();
					if (((value instanceof Frozen)) || ((value instanceof Auxhead))) {
						listboxExcel.removeChild((Component) value);
						component = listboxExcel.getChildren().iterator();
					}
				}

				CommonUtils.exportListboxToExcel(listboxExcel, "bao_cao_qc31.xlsx");
			}
		}

		if (event.getTarget().equals(btnReport)) {
			if (event.getName().equals(Events.ON_CLICK)) {
				this.exportToReportViewer(event);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void executeSearch() {
		if (lstPredicate != null && lstPredicate.size() > 0) {
			PredicateUtil predicate = new PredicateUtil(lstPredicate, PredicateUtil.PRE_AND);
			Collection<ReportModel> coll = CollectionUtils.select(lstModel, predicate);
			List<ReportModel> lsttmp = new ArrayList<ReportModel>(coll);
			if (renderReportWithListBox == true) {
				listbox.setModel(new ListModelList<>(lsttmp));
			} else {
				gridData.setModel(new ListModelList<>(lsttmp));
			}
		}
	}

	public void exportToReportViewer(Event event) {
		try {
			if (event.getTarget().equals(btnReport)) {
				Timestamp fromdate = new Timestamp(dateFrom.getValue().getTime());
				Timestamp todate = new Timestamp(dateTo.getValue().getTime());
				if (fromdate.after(todate)) {
					Messagebox.show("Hãy chọn lại thời gian!");
					return;
				}
				setMapParams();
				VTReportViewer reportWindow = new VTReportViewer(reportFile, reportTitle, reportName, exportFileName,
						this, mapParamsReport);
				reportWindow.onShowing();
			}
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	public abstract void setMapParams();

	public Grid getGridFilter() {
		return gridFilter;
	}

	public void setGridFilter(Grid gridFilter) {
		this.gridFilter = gridFilter;
	}

	public Grid getGridData() {
		return gridData;
	}

	public void setGridData(Grid gridData) {
		this.gridData = gridData;
	}

	public Div getLeftPanel() {
		return leftPanel;
	}

	public void setLeftPanel(Div leftPanel) {
		this.leftPanel = leftPanel;
	}

	public Div getRightPanel() {
		return rightPanel;
	}

	public void setRightPanel(Div rightPanel) {
		this.rightPanel = rightPanel;
	}

	public Vlayout getVlayout() {
		return vlayout;
	}

	public void setVlayout(Vlayout vlayout) {
		this.vlayout = vlayout;
	}

	public Hlayout getHlayout() {
		return hlayout;
	}

	public void setHlayout(Hlayout hlayout) {
		this.hlayout = hlayout;
	}

	public Button getBtnSearch() {
		return btnSearch;
	}

	public void setBtnSearch(Button btnSearch) {
		this.btnSearch = btnSearch;
	}

	public Button getBtnReport() {
		return btnReport;
	}

	public void setBtnReport(Button btnReport) {
		this.btnReport = btnReport;
	}

	public Button getBtnExcel() {
		return btnExcel;
	}

	public void setBtnExcel(Button btnExcel) {
		this.btnExcel = btnExcel;
	}

	public List<?> getLstModel() {
		return lstModel;
	}

	public void setLstModel(List<?> lstModel) {
		this.lstModel = lstModel;
	}

	public Class<?> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<?> modelClass) {
		this.modelClass = modelClass;
	}

	public ArrayList<GridColumn> getGridColumns() {
		return lstCols;
	}

	public void setGridColumns(ArrayList<GridColumn> gridColumns) {
		this.lstCols = gridColumns;
	}

	public ArrayList<GridRow> getGridRows() {
		return lstRows;
	}

	public void setGridRows(ArrayList<GridRow> gridRows) {
		this.lstRows = gridRows;
	}

	public AbstractListModel<?> getListModel() {
		return listModel;
	}

	public void setListModel(AbstractListModel<?> listModel) {
		this.listModel = listModel;
	}

	public Datebox getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Datebox dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Datebox getDateTo() {
		return dateTo;
	}

	public void setDateTo(Datebox dateTo) {
		this.dateTo = dateTo;
	}

	public Chosenbox getChosenAgent() {
		return chosenAgent;
	}

	public void setChosenAgent(Chosenbox chosenAgent) {
		this.chosenAgent = chosenAgent;
	}

	public Chosenbox getChosenGroup() {
		return chosenGroup;
	}

	public void setChosenGroup(Chosenbox chosenGroup) {
		this.chosenGroup = chosenGroup;
	}

	public Textbox getTxtVehicleNumber() {
		return txtVehicleNumber;
	}

	public void setTxtVehicleNumber(Textbox txtVehicleNumber) {
		this.txtVehicleNumber = txtVehicleNumber;
	}

	public Textbox getTxtLicensePlate() {
		return txtLicensePlate;
	}

	public void setTxtLicensePlate(Textbox txtLicensePlate) {
		this.txtLicensePlate = txtLicensePlate;
	}

	public String getLbTitleReport() {
		return lbTitleReport;
	}

	public void setLbTitleReport(String lbTitleReport) {
		this.lbTitleReport = lbTitleReport;
	}

	public String getLbTitleInput() {
		return lbTitleInput;
	}

	public void setLbTitleInput(String lbTitleInput) {
		this.lbTitleInput = lbTitleInput;
	}

	public int getGridWidthCols() {
		return gridWidthCols;
	}

	public void setGridWidthCols(int gridWidthCols) {
		this.gridWidthCols = gridWidthCols;
	}

	public int getCountCols() {
		return countCols;
	}

	public void setCountCols(int countCols) {
		this.countCols = countCols;
	}

	public Listbox getListbox() {
		return listbox;
	}

	public void setListbox(Listbox listbox) {
		this.listbox = listbox;
	}

	public Chosenbox getChosenDriver() {
		return chosenDriver;
	}

	public void setChosenDriver(Chosenbox chosenDriver) {
		this.chosenDriver = chosenDriver;
	}

	public Chosenbox getChosenVehicle() {
		return chosenVehicle;
	}

	public void setChosenVehicle(Chosenbox chosenVehicle) {
		this.chosenVehicle = chosenVehicle;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public StringBuilder getStrAgentId() {
		return strAgentId;
	}

	public void setStrAgentId(StringBuilder strAgentId) {
		this.strAgentId = strAgentId;
	}

	public StringBuilder getStrGroupId() {
		return strGroupId;
	}

	public void setStrGroupId(StringBuilder strGroupId) {
		this.strGroupId = strGroupId;
	}

	public StringBuilder getStrPakingAreaId() {
		return strPakingAreaId;
	}

	public void setStrPakingAreaId(StringBuilder strPakingAreaId) {
		this.strPakingAreaId = strPakingAreaId;
	}

	public StringBuilder getStrVehicleId() {
		return strVehicleId;
	}

	public void setStrVehicleId(StringBuilder strVehicleId) {
		this.strVehicleId = strVehicleId;
	}

	public StringBuilder getStrDriverId() {
		return strDriverId;
	}

	public void setStrDriverId(StringBuilder strDriverId) {
		this.strDriverId = strDriverId;
	}

	public boolean isRenderReportWithListBox() {
		return renderReportWithListBox;
	}

	public void setRenderReportWithListBox(boolean renderReport) {
		this.renderReportWithListBox = renderReport;
	}

}
