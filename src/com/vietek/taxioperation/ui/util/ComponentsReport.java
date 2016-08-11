package com.vietek.taxioperation.ui.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.vietek.taxioperation.database.ListObjectDatabase;
import com.vietek.taxioperation.model.Driver;
import com.vietek.taxioperation.model.ParkingArea;
import com.vietek.taxioperation.model.ReportInputVehicle;
import com.vietek.taxioperation.model.TaxiGroup;
import com.vietek.taxioperation.model.Vehicle;
import com.vietek.taxioperation.util.ComboboxThread;

public class ComponentsReport {

	private String image;
	private String title;
	private int value;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static Component reloadChosenboxGroup(Component component, String lstAgentId) {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		List<TaxiGroup> lstvalue = lstObj.getVehicleGroup(lstAgentId);
		if (component instanceof Chosenbox) {
			Chosenbox output = (Chosenbox) component;
			output.setModel(new ListModelList<>(lstvalue));
			return output;
		} else if (component instanceof Combobox) {
			Combobox output = (Combobox) component;
			output.setModel(new ListModelList<>(lstvalue));
			return output;
		}
		return component;
	}

	public static void reloadChosenboxVehicle(Chosenbox chosenVehicle, String lstGroupVehId) {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		List<ReportInputVehicle> lstvalue = lstObj.getVehicle(lstGroupVehId);
		chosenVehicle.setModel(new ListModelList<>(lstvalue));
	}

	// Edit reloadChosenboxVehicle
	public static void reloadChosenboxVehicles(Chosenbox chosenVehicle, List<Vehicle> lstVehicle) {
		chosenVehicle.setModel(new ListModelList<>(lstVehicle));
	}

	public static Component reloadChosenboxDriver(Component component, String lstGroupVehId) {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		List<Driver> lstvalue = lstObj.getDriver(lstGroupVehId);
		if (component instanceof Chosenbox) {
			Chosenbox output = (Chosenbox) component;
			output.setModel(new ListModelList<>(lstvalue));
			return output;
		} else if (component instanceof Combobox) {
			Combobox output = (Combobox) component;
			output.setModel(new ListModelList<>(lstvalue));
			return output;
		}
		return component;
	}

	public static Component reloadChosenboxParking(Component component, String lstGroupVehId) {
		ListObjectDatabase lstObj = new ListObjectDatabase();
		List<ParkingArea> lstvalue = lstObj.getParking(lstGroupVehId);
		if (component instanceof Chosenbox) {
			Chosenbox output = (Chosenbox) component;
			output.setModel(new ListModelList<>(lstvalue));
			return output;
		} else if (component instanceof Combobox) {
			Combobox output = (Combobox) component;
			output.setModel(new ListModelList<>(lstvalue));
			return output;
		}
		return component;
	}

	// Phone, Wait Minutes, Zone, VehicleNumber, LicensePlate, ..
	public Textbox textboxInput() {
		Textbox txt = new Textbox();
		return txt;
	}

	// Combobox States, Vehicle, Cancelling Reason, Condition, ..
	public Combobox ComboboxRendering(HashMap<String, String> data, String styles, String bonusClass, int width,
			int height, int selectedvalue, boolean hasGetAll) {
		Combobox comb = new Combobox();
		if (styles.length() > 0) {
			comb.setStyle(styles);
		}

		if (bonusClass.length() > 0) {
			comb.setSclass(bonusClass);
		}

		if (width > 0) {
			comb.setWidth(width + "px");
		} else {
			comb.setWidth("200px");
		}
		if (height > 0) {
			comb.setHeight(height + "px");
		}

		List<ComponentsReport> lstComboboxs = new ArrayList<ComponentsReport>();
		if (hasGetAll) {
			ComponentsReport cos = new ComponentsReport();
			cos.setTitle(" Chọn tất cả ");
			cos.setValue(0);
			lstComboboxs.add(cos);
		}
		for (String key : data.keySet()) {
			ComponentsReport bean = new ComponentsReport();
			bean.setTitle(data.get(key));
			bean.setValue(Integer.parseInt(key));
			lstComboboxs.add(bean);
		}

		comb.setItemRenderer(new ComboitemRenderer<ComponentsReport>() {
			@Override
			public void render(final Comboitem paramComboitem, ComponentsReport bean, int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				if (bean.getValue() == selectedvalue) {
					comb.setSelectedItem(paramComboitem);
				}
			}
		});

		comb.setModel(new ListModelList<ComponentsReport>(lstComboboxs));

		return comb;
	}

	public static Combobox ComboboxRendering(String[] titles, int[] values, String styles, String bonusClass, int width,
			int height, boolean hasGetAll) {
		Combobox comb = new Combobox();
		if (titles.length != values.length) {
			return comb;
		}
		if (styles.length() > 0) {
			comb.setStyle(styles);
		}

		if (bonusClass.length() > 0) {
			comb.setSclass(bonusClass);
		}

		if (width > 0) {
			comb.setWidth(width + "px");
		} else {
			comb.setWidth("200px");
		}
		if (height > 0) {
			comb.setHeight(height + "px");
		}

		List<ComponentsReport> lstComboboxs = new ArrayList<ComponentsReport>();
		if (hasGetAll) {
			ComponentsReport cos = new ComponentsReport();
			cos.setTitle(" Chọn tất cả ");
			cos.setValue(0);
			lstComboboxs.add(cos);
		}
		for (int i = 0; i < titles.length; i++) {
			ComponentsReport bean = new ComponentsReport();
			bean.setTitle(" " + titles[i] + " ");
			bean.setValue(values[i]);
			lstComboboxs.add(bean);
		}

		comb.setItemRenderer(new ComboitemRenderer<ComponentsReport>() {
			@Override
			public void render(final Comboitem paramComboitem, ComponentsReport bean, int paramInt) throws Exception {
				paramComboitem.setLabel(bean.getTitle());
				paramComboitem.setValue(bean.getValue());
				if (bean.getValue() == 0) {
					comb.setSelectedItem(paramComboitem);
				}
			}
		});

		comb.setModel(new ListModelList<ComponentsReport>(lstComboboxs));

		return comb;
	}

	public Datebox dateboxDisplay(boolean display, Row row, String fomat, int width, int hour, int minute, int second) {
		Datebox datebox = new Datebox();
		datebox.setParent(row);
		if (width > 0) {
			datebox.setWidth(width + "px");
		}
		if (hour >= 0 && minute >= 0) {
			datebox.setValue(addHour(new Date(), hour, minute, second));
		}
		datebox.setFormat(fomat);
		datebox.setConstraint("no empty : Không để trống");
		return datebox;
	}

	public Date addHour(Date date, int h, int m, int s) {
		if (date == null) {
			throw new IllegalArgumentException("");

		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, h);
		c.set(Calendar.MINUTE, m);
		c.set(Calendar.SECOND, s);
		return c.getTime();
	}

	// Author: dungnd
	// lay ra cac input co ban: Don vi/doi xe/loai xe/bai giao ca/thiet bi/xe...
	public static Chosenbox ChosenboxReportInput(Class<?> clazz) {
		try {
			Chosenbox chosenboxOutput = new Chosenbox();
			chosenboxOutput.setWidth("160px");
			chosenboxOutput.setStyle("overflow-x : auto");
			ComboboxThread comboboxThread = new ComboboxThread(clazz, "", 10);
			List<?> outputList = comboboxThread.getDataList();
			chosenboxOutput.setModel(new ListModelList<>(outputList));
			return chosenboxOutput;
		} catch (Exception e) {
			// TODO: handle exception
			return new Chosenbox();
		}
	}

	public static Combobox ComboboxReportInput(Class<?> clazz) {
		try {
			Combobox comboboxOutput = new Combobox();
			comboboxOutput.setWidth("160px");
			comboboxOutput.setStyle("overflow-x : auto");
			ComboboxThread comboboxThread = new ComboboxThread(clazz, "", 10);
			List<?> outputList = comboboxThread.getDataList();
			comboboxOutput.setModel(new ListModelList<>(outputList));
			return comboboxOutput;
		} catch (Exception e) {
			// TODO: handle exception
			return new Combobox();
		}
	}

	public static Chosenbox ChosenboxReportInputDefault(Class<?> clazz) {
		try {
			VChosenbox chosenboxOutput = new VChosenbox();
			chosenboxOutput.setWidth("160px");
			chosenboxOutput.setStyle("overflow-x : auto");
			ComboboxThread comboboxThread = new ComboboxThread(clazz);
			List<?> outputList = comboboxThread.getDataList();
//			chosenboxOutput.setModel(new ListModelList<>(outputList));
			chosenboxOutput.setLstAllModel(outputList);
			return chosenboxOutput;
		} catch (Exception e) {
			// TODO: handle exception
			return new Chosenbox();
		}
	}
}
