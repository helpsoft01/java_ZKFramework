package com.vietek.taxioperation.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.exporter.excel.ExcelExporter;
import org.zkoss.pivot.Pivottable;
import org.zkoss.pivot.util.Exports;
import org.zkoss.pivot.util.Exports.PivotExportContext;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.common.CommonDefine;

public class CommonUtils {
	public static boolean checkNotNull(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			if (((String) obj).trim().length() == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Export result from grid to excel file, note that grid in single page mode
	 * to allow all data exported into excel
	 * 
	 * @author Dzungnd
	 * @param grid
	 *            data need exporting
	 * @param filename
	 *            output file name
	 * @throws Exception
	 */
	public static void exportListboxToExcel(@BindingParam("ref") Component component, String filename) throws Exception {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ExcelExporter exporter = new ExcelExporter();
			Listbox listbox = null;
			Grid grid = null;
			if (component instanceof Grid) {
				grid = (Grid)component;
				grid.renderAll();

				exporter.export(grid, out);
			}
			if (component instanceof Listbox) {
				listbox = (Listbox)component;
				listbox.renderAll();

				exporter.export(listbox, out);
			}		
			AMedia amedia = new AMedia(filename, "xlsx", "application/file", out.toByteArray());
			Filedownload.save(amedia);
			out.close();
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		}
	}

	public static String getPhoneForSms(String phoneNumber) {
		String result = "";
		if (phoneNumber != null && phoneNumber.length() >= 10) {
			if (phoneNumber.startsWith(CommonDefine.PRE_PHONE_SMS)) {
				result = phoneNumber;
			} else if (phoneNumber.startsWith(CommonDefine.PRE_PHONE_SMS_09)
					|| phoneNumber.startsWith(CommonDefine.PRE_PHONE_SMS_01)) {
				result = CommonDefine.PRE_PHONE_SMS + phoneNumber.substring(1, phoneNumber.length());
			} else if (phoneNumber.startsWith(CommonDefine.PRE_PHONE_SMS_84)) {
				result = phoneNumber.replaceAll("+", "");
			} else {
				result = null;
			}
		}
		return result;
	}

	/**
	 * Export PivotTable to Excel
	 * 
	 * @author MPV
	 * 
	 */
	public static void exportPivotTableToExcel(Pivottable pivot, String filename) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PivotExportContext context = Exports.getExportContext(pivot, false, null);
		try {
			Exports.exportExcel(out, "xls", context, null);
			Filedownload.save(out.toByteArray(), "application/vnd.ms-excel", filename + ".xls");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean containsNumber(String c) {
		for (int i = 0; i < c.length(); ++i) {
			if (Character.isDigit(c.charAt(i)) == false)
				return false;
		}
		return true;
	}	
}
