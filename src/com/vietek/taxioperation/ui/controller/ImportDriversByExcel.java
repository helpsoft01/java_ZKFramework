package com.vietek.taxioperation.ui.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.controller.CommonValueController;
import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.CommonValue;
import com.vietek.taxioperation.model.DriverExcelTemp;
import com.vietek.taxioperation.ui.util.ComboboxRender;
import com.vietek.taxioperation.util.ControllerUtils;
import com.vietek.taxioperation.util.Env;

public class ImportDriversByExcel extends Window implements EventListener<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Window importviewer;
	private Component parent;
	private Listbox listbox;
	private Combobox cbxagent;
	private Combobox cbxgroup;
	private Combobox cbxsheet;
	private Textbox txtfilename;
	private Button btnread;
	private Button btnupdate;
	private Button btnupload;
	private InputStream datastream;
	private Workbook workbook;
	private ComboboxRender renderCbo;

	public ImportDriversByExcel(Drivers parent) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		this.init();
	}

	public void init() {
		importviewer = new Window();
		importviewer.setTitle("Cập nhật danh sách lái xe");
		importviewer.setParent(parent);
		importviewer.setHflex("1");
		importviewer.setVflex("1");
		importviewer.setClosable(true);
		importviewer.setMaximizable(true);

		Vlayout vlayout = new Vlayout();
		vlayout.setParent(importviewer);
		vlayout.setHflex("1");
		vlayout.setVflex("1");

		datastream = null;
		workbook = null;

		this.initControl(vlayout);
		this.initContent(vlayout);
	}

	public void initControl(Vlayout vlayout) {
		Panel panel = new Panel();
		panel.setParent(vlayout);
		panel.setHflex("1");
		panel.setVflex("2.5");
		panel.setTitle("Chọn file thông tin (.xls, .xlsx)");
		panel.setStyle("margin-bottom: 5px;");

		Panelchildren child = new Panelchildren();
		child.setParent(panel);

		Grid grid = new Grid();
		grid.setParent(child);
		grid.setStyle("border: none");
		grid.setVflex("1");
		grid.setHflex("1");

		Columns cols = new Columns();
		cols.setParent(grid);
		// col1
		Column col = new Column();
		col.setParent(cols);
		col.setWidth("5%");
		// col2
		col = new Column();
		col.setParent(cols);
		col.setWidth("20%");
		// col3
		col = new Column();
		col.setParent(cols);
		col.setWidth("5%");
		// col4
		col = new Column();
		col.setParent(cols);
		col.setWidth("20%");
		// col5
		col = new Column();
		col.setParent(cols);
		col.setWidth("10%");
		// col6
		col = new Column();
		col.setParent(cols);
		col.setWidth("15%");
		// col7
		col = new Column();
		col.setParent(cols);
		col.setWidth("10%");
		// col8
		col = new Column();
		col.setParent(cols);
		col.setWidth("7.5%");
		// co9
		col = new Column();
		col.setParent(cols);
		col.setWidth("7.5%");

		Rows rows = new Rows();
		rows.setParent(grid);

		Row row = new Row();
		row.setParent(rows);
		String style = "font-weight: bold;";
		//
		Label label = new Label();
		label.setValue("Đơn vị");
		label.setStyle(style);
		label.setParent(row);
		//
		String styles = "line-height: 20px !important; font-size: 12px !important; padding: 3px;";
		String sClass = "comboboxtripsearching";
		renderCbo = new ComboboxRender();
		cbxagent = renderCbo.agentComboboxReder(styles, sClass, 200, 20, 0, (String)Env.getContext(Env.USER_NAME));
		cbxagent.setWidth("100%");
		cbxagent.setParent(row);
		//
		label = new Label();
		label.setValue("Đội xe");
		label.setStyle(style);
		label.setParent(row);
		//
		renderCbo = new ComboboxRender();
		cbxgroup = renderCbo.vehgroupComboboxReder(styles, sClass, 200, 20, -1, 0, (String)Env.getContext(Env.USER_NAME));
		cbxgroup.setWidth("100%");
		cbxgroup.setParent(row);
		cbxgroup.addEventListener(Events.ON_SELECT, this);
		//
		btnupload = new Button("Chọn file");
		btnupload.setParent(row);
		btnupload.addEventListener(Events.ON_CLICK, this);
		//
		txtfilename = new Textbox();
		txtfilename.setHflex("1");
		txtfilename.setPlaceholder("Tên file");
		txtfilename.setReadonly(true);
		txtfilename.setParent(row);
		//
		renderCbo = new ComboboxRender();
		cbxsheet = renderCbo.ComboboxRendering(new HashMap<String, String>(), styles, sClass, 100, 20, 0, false);
		cbxsheet.setParent(row);
		cbxsheet.addEventListener(Events.ON_SELECT, this);

		btnread = new Button("Đọc file");
		btnread.setParent(row);
		btnread.addEventListener(Events.ON_CLICK, this);

		btnupdate = new Button("Cập nhật");
		btnupdate.setParent(row);
		btnupdate.addEventListener(Events.ON_CLICK, this);
	}

	public void initContent(Vlayout vlayout) {
		listbox = new Listbox();
		listbox.setParent(vlayout);
		listbox.setHflex("1");
		listbox.setVflex("7.5");
		listbox.setMold("paging");
		listbox.setAutopaging(true);

		Listhead cols = new Listhead();
		cols.setParent(listbox);
		cols.setSizable(true);

		Auxhead auxhead = new Auxhead();
		auxhead.setParent(listbox);

		// col1
		Listheader col = new Listheader();
		col.setParent(cols);
		col.setWidth("5%");
		col.setLabel("MSNV");
		// col2
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("13%");
		col.setLabel("Họ tên lái xe");
		// col3
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("7%");
		col.setLabel("Ngày sinh");
		// col4
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("GPLX");
		// col5
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("5%");
		col.setLabel("Loại bằng");
		// col6
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("Ngày cấp");
		// col7
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("Hết hạn");
		// col8
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("5%");
		col.setLabel("Giới tính");
		// col9
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("CMND");
		// col10
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("VHS");
		// col11
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("10%");
		col.setLabel("Số cá nhân");
		// col12
		col = new Listheader();
		col.setParent(cols);
		col.setWidth("5%");
		col.setLabel("Nhóm máu");

		listbox.setItemRenderer(new ListitemRenderer<DriverExcelTemp>() {
			@Override
			public void render(Listitem listitem, DriverExcelTemp data, int index) throws Exception {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
				new Listcell(Integer.toString(data.getStaffcard())).setParent(listitem);
				new Listcell(data.getDrivername()).setParent(listitem);
				new Listcell(data.getBirthday() == null ? "" : dateformat.format(data.getBirthday()))
						.setParent(listitem);
				new Listcell(data.getDriverlicense()).setParent(listitem);
				new Listcell(data.getLicensetype()).setParent(listitem);
				new Listcell(data.getRegisterdate() == null ? "" : dateformat.format(data.getRegisterdate()))
						.setParent(listitem);
				new Listcell(data.getExpiatedate() == null ? "" : dateformat.format(data.getExpiatedate()))
						.setParent(listitem);
				new Listcell("" + data.getSexname()).setParent(listitem);
				new Listcell("" + data.getIdentitycard()).setParent(listitem);
				new Listcell("" + data.getPhoneoffice()).setParent(listitem);
				new Listcell("" + data.getPhonenumber()).setParent(listitem);
				new Listcell("" + data.getBloodtype()).setParent(listitem);
				listitem.setValue(data);
			}
		});

		listbox.addEventListener(Events.ON_SELECT, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(Event event) throws Exception {
		// TODO Auto-generated method stub
		if (event.getTarget().equals(cbxagent)) {
			int agentid = 0;
			if (Integer.parseInt(cbxagent.getSelectedItem().getValue().toString()) > 0) {
				agentid = Integer.parseInt(cbxagent.getSelectedItem().getValue().toString());
			}
			renderCbo = new ComboboxRender();
			Combobox cbxtemp = renderCbo.vehgroupComboboxReder("", "", 200, 20, agentid, 0,
					(String)Env.getContext(Env.USER_NAME));
			cbxgroup.setModel(cbxtemp.getModel());
		}
		if (event.getTarget().equals(btnupload)) {
			EventListener<UploadEvent> el = new EventListener<UploadEvent>() {
				public void onEvent(UploadEvent ev) {
					chooseExcel(ev);
				}
			};
			Fileupload.get(el);
		}
		if (event.getTarget().equals(btnread)) {
			if (Integer.parseInt(cbxagent.getSelectedItem().getValue().toString()) < 1) {
				Env.getHomePage().showNotificationErrorSelect("Chưa chọn chi nhánh", Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
			if (Integer.parseInt(cbxgroup.getSelectedItem().getValue().toString()) < 1) {
				Env.getHomePage().showNotificationErrorSelect("Chưa chọn đội xe", Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
			int selectedsheet = cbxsheet.getSelectedIndex();
			getExcelData2Grid(selectedsheet);
		}
		if (event.getTarget().equals(btnupdate)) {
			if (listbox != null) {
				List<DriverExcelTemp> model = (List<DriverExcelTemp>) listbox.getModel();
				if (model != null) {
					ListObjectDatabase listObjectDatabase = new ListObjectDatabase();
					int rep = listObjectDatabase.updateDriverFromExcelData((List<DriverExcelTemp>) listbox.getModel());
					if (rep == 0) {
						Env.getHomePage().showNotificationErrorSelect("Cập nhật thành công",
								Clients.NOTIFICATION_TYPE_INFO);
					} else {
						Env.getHomePage().showNotificationErrorSelect("Gặp lỗi khi cập nhật",
								Clients.NOTIFICATION_TYPE_ERROR);
					}
				} else {
					Env.getHomePage().showNotificationErrorSelect("Không có dữ liệu cập nhật",
							Clients.NOTIFICATION_TYPE_ERROR);
				}
			}

		}
	}

	public void chooseExcel(UploadEvent ev) {
		try {
			Media m = ev.getMedia();
			// file processing
			if (m == null) {
				Env.getHomePage().showNotificationErrorSelect("Chưa chọn file !", Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
			String filename = m.getName();
			datastream = m.getStreamData();

			if (filename.endsWith("xlsx") || filename.endsWith("xls")) {
				workbook = WorkbookFactory.create(datastream);
			} else {
				Env.getHomePage().showNotificationErrorSelect("Chỉ hỗ trợ định dạng .xls | .xlsx",
						Clients.NOTIFICATION_TYPE_ERROR);
				return;
			}
			txtfilename.setValue(filename);
			List<ComboboxRender> lstComboboxs = new ArrayList<ComboboxRender>();
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				ComboboxRender bean = new ComboboxRender();
				bean.setTitle(workbook.getSheetName(i));
				bean.setValue(i);
				lstComboboxs.add(bean);
			}
			cbxsheet.setModel(new ListModelList<ComboboxRender>(lstComboboxs));
			datastream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getExcelData2Grid(int sheetindex) throws IOException, ParseException {
		List<DriverExcelTemp> temp = new ArrayList<DriverExcelTemp>();
		int firstrow = 0;
		try {
			Sheet sheet = workbook.getSheetAt(sheetindex);
			Iterator<org.apache.poi.ss.usermodel.Row> iterator = sheet.iterator();
			while (iterator.hasNext()) {
				org.apache.poi.ss.usermodel.Row nextRow = (org.apache.poi.ss.usermodel.Row) iterator.next();
				if (firstrow == 0) {
					firstrow++;
				} else {
					Iterator<Cell> cellIterator = ((org.apache.poi.ss.usermodel.Row) nextRow).cellIterator();
					DriverExcelTemp driverExcelTemp = new DriverExcelTemp();
					String firstname = "";
					String lastname = "";
					Date regdate = null;
					Date expiate = null;
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();

						switch (cell.getColumnIndex()) {
						case 0:
							driverExcelTemp.setStaffcard((int) cell.getNumericCellValue());
							break;
						case 1:
							firstname = cell.getStringCellValue();
							break;
						case 2:
							lastname = cell.getStringCellValue();
							driverExcelTemp.setDrivercode(lastname);
							break;
						case 3:
							Date birthday = null;
							try {
								birthday = format.parse(cell.getStringCellValue());
								driverExcelTemp.setBirthday(birthday);
							} catch (Exception e) {
								birthday = format.parse("01/01/1980");
							}

							break;
						case 4:
							try {
								driverExcelTemp.setDriverlicense(cell.getStringCellValue());
							} catch (Exception e) {
								driverExcelTemp.setDriverlicense("");
							}
							break;
						case 5:
							try {
								driverExcelTemp.setLicensetype(cell.getStringCellValue());
								driverExcelTemp.setLicensetypeid(getLicenseTypeId(cell.getStringCellValue()));
							} catch (Exception e) {
								driverExcelTemp.setLicensetype("");
								driverExcelTemp.setLicensetypeid(0);
							}
							break;
						case 6:
							try {
								regdate = format.parse(cell.getStringCellValue());
								driverExcelTemp.setRegisterdate(regdate);
							} catch (Exception e) {
								regdate = format.parse("01/01/1980");
							}

							break;
						case 7:
							try {
								expiate = format.parse(cell.getStringCellValue());
								Calendar from = Calendar.getInstance();
								from.setTime(regdate);
								Calendar to = Calendar.getInstance();
								to.setTime(expiate);
								int timelimit = to.get(Calendar.YEAR) - from.get(Calendar.YEAR);
								if (timelimit < 0) {
									timelimit = 5;
								}
								driverExcelTemp.setTimelimit(timelimit);
								driverExcelTemp.setExpiatedate(expiate);
							} catch (Exception e) {
								expiate = format.parse("01/01/1980");
							}

							break;
						case 8:
							String pinX = cell.getStringCellValue();
							if (pinX.toLowerCase().equals("x")) {
								driverExcelTemp.setSexname("Nam");
								driverExcelTemp.setSexid(72);
							}
							break;
						case 9:
							String pinY = cell.getStringCellValue();
							if (pinY.toLowerCase().equals("x")) {
								driverExcelTemp.setSexname("Nữ");
								driverExcelTemp.setSexid(73);
							}
							break;
						case 10:
							try {
								String identitycard = cell.getStringCellValue();
								driverExcelTemp.setIdentitycard(identitycard);
							} catch (Exception e) {
								driverExcelTemp.setIdentitycard("");
							}
							break;
						case 11:
							try {
								driverExcelTemp.setPhoneoffice(cell.getStringCellValue());
							} catch (Exception e) {
								driverExcelTemp.setPhoneoffice("");
							}
							break;
						case 12:
							try {
								driverExcelTemp.setPhonenumber(cell.getStringCellValue());
							} catch (Exception e) {
								driverExcelTemp.setPhonenumber("");
							}
							break;
						case 13:
							try {
								driverExcelTemp.setBloodtype(cell.getStringCellValue());
								driverExcelTemp.setBloodtypeid(getBloodTypeId(cell.getStringCellValue()));
							} catch (Exception e) {
								driverExcelTemp.setBloodtype("");
								driverExcelTemp.setBloodtypeid(0);
							}
							break;
						}
					}
					driverExcelTemp.setAgentid(cbxagent.getSelectedItem().getValue());
					driverExcelTemp.setGroupid(cbxgroup.getSelectedItem().getValue());
					driverExcelTemp.setDrivername(firstname + " " + lastname);
					temp.add(driverExcelTemp);
				}
			}

			listbox.setModel(new ListModelList<DriverExcelTemp>(temp, true));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public int getLicenseTypeId(String type) {
		int typeid = 75;
		CommonValueController commonValueController = (CommonValueController) ControllerUtils
				.getController(CommonValueController.class);
		List<CommonValue> results = commonValueController.find("From CommonValue Where codetype = 'LICENSETYPE'");
		CommonValue value = (CommonValue) results.stream()
				.filter(p -> p.getName().toLowerCase().equals(type.toLowerCase())).findFirst().get();
		typeid = value.getId();
		return typeid;
	}

	public int getBloodTypeId(String type) {
		int typeid = 75;
		CommonValueController commonValueController = (CommonValueController) ControllerUtils
				.getController(CommonValueController.class);
		List<CommonValue> results = commonValueController.find("From CommonValue Where codetype = 'BLOODTYPE'");
		CommonValue value = (CommonValue) results.stream()
				.filter(p -> p.getName().toLowerCase().equals(type.toLowerCase())).findFirst().get();
		typeid = value.getId();
		return typeid;
	}

	public void showModal() {
		importviewer.setMaximized(true);
		importviewer.doModal();
	}
}