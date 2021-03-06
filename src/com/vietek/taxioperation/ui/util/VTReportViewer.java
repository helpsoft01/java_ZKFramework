package com.vietek.taxioperation.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.util.ConfigUtil;
import com.vietek.taxioperation.util.ControllerUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class VTReportViewer extends Window implements EventListener<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4327167698190734352L;
	private Iframe iframe;
	private AMedia media;
	private JasperPrint jasperPrint;
	private String jasperName;
	private String jasperHeader;
	private String jasperOutputName;
	private String jasperTitle;
	private String REPORT_HOME;
	private String REPORT_IMAGE_PATH;
	private String REPORT_PREFIX_COMPANY;
	private String REPORT_PREFIX_AGENT;

	private String viewType = "PDF";
	HashMap<String, Object> params = new HashMap<String, Object>();
	ArrayList<Toolbarbutton> arr_bt = new ArrayList<Toolbarbutton>();
	private Component jasperParent;
	private static Object[] datasource;

	public String getJasperName() {
		return jasperName;
	}

	public void setJasperName(String jasperName) {
		this.jasperName = jasperName;
	}

	public String getJasperHeader() {
		return jasperHeader;
	}

	public void setJasperHeader(String jasperHeader) {
		this.jasperHeader = jasperHeader;
	}

	public String getJasperOutputName() {
		return jasperOutputName;
	}

	public void setJasperOutputName(String jasperOutputName) {
		this.jasperOutputName = jasperOutputName;
	}

	public String getJasperTitle() {
		return jasperTitle;
	}

	public void setJasperTitle(String jasperTitle) {
		this.jasperTitle = jasperTitle;
	}

	public Component getJasperParent() {
		return jasperParent;
	}

	public Object[] getBeanArray() {
		return datasource;
	}

	@SuppressWarnings("rawtypes")
	public static Collection getBeanCollection() {
		return Arrays.asList(datasource);
	}

	public void setJasperParent(Component jasperParent) {
		this.jasperParent = jasperParent;
	}

	public VTReportViewer(String reportFile, String reportTitle, String reportName, String outputFileName,
			Component parent, Map<String, Object> mapParams) {
		REPORT_HOME = this.getClass().getClassLoader().getResource("").getPath()
				+ new ConfigUtil().getPropValues("report_home");
		REPORT_IMAGE_PATH = this.getClass().getClassLoader().getResource("").getPath()
				+ new ConfigUtil().getPropValues("report_image_path");
		REPORT_PREFIX_COMPANY = new ConfigUtil().getPropValues("report_prefix_company");
		REPORT_PREFIX_AGENT = new ConfigUtil().getPropValues("report_prefix_agent");
		jasperName = reportFile;
		jasperOutputName = outputFileName;
		jasperTitle = reportTitle;
		jasperParent = parent;
		jasperHeader = reportName;
		datasource = null;

		Iterator<String> it = mapParams.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			params.put(key, mapParams.get(key));
		}
		onCreateLayout();
	}

	public VTReportViewer(String reportFile, String reportTitle, String reportName, String outputFileName,
			Component parent, Object[] data) {
		REPORT_HOME = this.getClass().getClassLoader().getResource("").getPath()
				+ new ConfigUtil().getPropValues("report_home");
		REPORT_IMAGE_PATH = this.getClass().getClassLoader().getResource("").getPath()
				+ new ConfigUtil().getPropValues("report_image_path");
		REPORT_PREFIX_COMPANY = new ConfigUtil().getPropValues("report_prefix_company");
		REPORT_PREFIX_AGENT = new ConfigUtil().getPropValues("report_prefix_agent");
		jasperName = reportFile;
		jasperOutputName = outputFileName;
		jasperTitle = reportTitle;
		jasperParent = parent;
		jasperHeader = reportName;
		datasource = data;

		onCreateLayout();
	}

	public void onShowing() {
		this.setParent(jasperParent);
		this.setVisible(true);
		this.setTitle(jasperTitle);
		this.setStyle("position: absoulute;");
		this.setContentStyle("width: 100%; height: 100%;");
		this.setMaximizable(true);
		this.setMaximized(true);
		this.setHflex("1");
		this.setVflex("1");
		this.setClosable(true);
		this.setSizable(true);
		this.doModal();
	}

	public void onCreateLayout() {
		Toolbar toolbar = new Toolbar();
		Toolbarbutton btPdf = new Toolbarbutton();
		btPdf.setDisabled(true);
		btPdf.setId("PDF");
		btPdf.setImage("/themes/images/ExportPdf_24.png");
		btPdf.addEventListener(Events.ON_CLICK, this);
		btPdf.setTooltiptext("Pdf");
		toolbar.appendChild(btPdf);
		arr_bt.add(btPdf);

		Toolbarbutton btDoc = new Toolbarbutton();
		toolbar.appendChild(btDoc);
		btDoc.setImage("/themes/images/ExportDoc_24.png");
		btDoc.setId("DOC");
		btDoc.addEventListener(Events.ON_CLICK, this);
		btDoc.setTooltiptext("Microsoft Word");
		arr_bt.add(btDoc);

		Toolbarbutton btXls = new Toolbarbutton();
		btXls.setImage("/themes/images/ExportExcel_24.png");
		toolbar.appendChild(btXls);
		btXls.setId("XLS");
		btXls.setTooltiptext("Microsoft Excel");
		btXls.addEventListener(Events.ON_CLICK, this);
		arr_bt.add(btXls);

		toolbar.setHeight("33px");
		toolbar.setStyle("vista");
		toolbar.setAlign("end");
		for (Toolbarbutton bt : arr_bt)
			bt.setStyle("height: 30px; width: 30px;");

		this.appendChild(toolbar);
		this.setStyle("width: 100%; height: 100%;");

		iframe = new Iframe();
		iframe.setId("reportFrame");
		iframe.setVflex("1");
		iframe.setWidth("100%");
		iframe.setParent(this);

		onInitReport();
	}

	/**
	 * URL mandatory params: - db - jaspername
	 */
	public void onInitReport() {

		if (jasperName == null || jasperName.length() == 0)
			return;
		jasperName = jasperName.trim();

		startProcess();
	}

	public boolean startProcess() {
		File reportFile = null;
		reportFile = new File(REPORT_HOME, jasperName + ".jasper");

		if (!reportFile.exists()) {
			return false;
		}

		JasperData data = processReport(reportFile);

		JasperReport jasperReport = data.getJasperReport();
		String jasperName = data.getJasperName();
		File reportDir = data.getReportDir();

		// Add reportDir to class path
		ClassLoader scl = ClassLoader.getSystemClassLoader();
		try {
			java.net.URLClassLoader ucl = new java.net.URLClassLoader(new java.net.URL[] { reportDir.toURI().toURL() },
					scl);
			net.sf.jasperreports.engine.util.JRResourcesUtil.setThreadClassLoader(ucl);
		} catch (MalformedURLException me) {
			AppLogger.logDebug.error("Could not add report directory to classpath: " + me);
		}
		if (jasperReport != null) {
			File resFile = null;
			String language = "vi_VN";
			if (params.containsKey("LANG")) {
				language = (String) params.get("LANG");
			}
			params.put("REPORT_LANGUAGE", language);
			params.put(JRParameter.REPORT_LOCALE, new Locale(language.substring(0, language.indexOf("_"))));
			params.put("CURRENCY_PATTERN", "#,###");
			params.put("QTY_PATTERN", "#,###.##");
			params.put("REPORT_DIR", REPORT_HOME);
			params.put("REPORT_IMAGE_PATH", REPORT_IMAGE_PATH);
			params.put("REPORT_PREFIX_COMPANY", REPORT_PREFIX_COMPANY);
			params.put("REPORT_PREFIX_AGENT", REPORT_PREFIX_AGENT);
			params.put("REPORT_NAME", jasperHeader);
			resFile = new File(REPORT_HOME, jasperName + "_" + language + ".properties");
			if (!resFile.exists()) {
				resFile = null;
			}

			if (resFile == null) {
				resFile = new File(REPORT_HOME, jasperName + ".properties");
				if (!resFile.exists()) {
					resFile = null;
				}
			}
			if (resFile != null) {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(resFile);
					PropertyResourceBundle res = new PropertyResourceBundle(fis);
					// params.put("RESOURCE", res);
					params.put(JRParameter.REPORT_RESOURCE_BUNDLE, res);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}

			Connection conn = null;
			Session session = ControllerUtils.getCurrentSession();
			SessionImplementor sessionImplementor = (SessionImplementor) session;
			try {

				conn = sessionImplementor.connection();
				if (datasource == null) {
					jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
				} else {
					jasperPrint = JasperFillManager.fillReport(jasperReport, params,
							new JRBeanCollectionDataSource(getBeanCollection()));
				}

				try {
					renderReport();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return true;
	}

	protected JasperData processReport(File reportFile) {
		JasperReport jasperReport = null;

		String jasperName = reportFile.getName();
		int pos = jasperName.indexOf('.');
		if (pos != -1)
			jasperName = jasperName.substring(0, pos);
		File reportDir = reportFile.getParentFile();

		// test if the compiled report exists
		File jasperFile = new File(reportDir.getAbsolutePath(), jasperName + ".jasper");
		try {
			jasperReport = (JasperReport) JRLoader.loadObjectFromFile(jasperFile.getAbsolutePath());
		} catch (JRException e) {
			jasperReport = null;
		}
		return new JasperData(jasperReport, reportDir, jasperName, jasperFile);
	}

	private String makePrefix(String name) {
		StringBuffer prefix = new StringBuffer();
		char[] nameArray = name.toCharArray();
		for (char ch : nameArray) {
			if (Character.isLetterOrDigit(ch)) {
				prefix.append(ch);
			} else {
				prefix.append("_");
			}
		}
		return prefix.toString();
	}

	public boolean renderReport() throws Exception {
		if ("PDF".equals(viewType)) {
			String path = System.getProperty("java.io.tmpdir");
			String prefix = makePrefix(jasperPrint.getName());
			File file = File.createTempFile(prefix, ".pdf", new File(path));
			JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
			media = new AMedia(jasperName, "pdf", "application/pdf", file, true);

		} else if ("HTML".equals(viewType)) {
			String path = System.getProperty("java.io.tmpdir");
			String prefix = makePrefix(jasperPrint.getName());
			File file = File.createTempFile(prefix, ".html", new File(path));

			JRHtmlExporter exporter = new JRHtmlExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
			// Make images available for the HTML output
			exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.TRUE);
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME,
					Executions.getCurrent().getDesktop().getSession().getWebApp().getRealPath("/images/"));
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath() + "/images/");
			exporter.exportReport();
			media = new AMedia(jasperName, "html", "text/html", file, false);
		} else if ("DOC".equals(viewType)) {
			String path = System.getProperty("java.io.tmpdir");
			String prefix = makePrefix(jasperPrint.getName());
			File file = File.createTempFile(prefix, ".doc", new File(path));
			FileOutputStream fos = new FileOutputStream(file);

			JRDocxExporter exporterDoc = new JRDocxExporter();
			exporterDoc.setParameter(JRDocxExporterParameter.JASPER_PRINT, jasperPrint);
			exporterDoc.setParameter(JRDocxExporterParameter.OUTPUT_STREAM, fos);
			exporterDoc.exportReport();
			media = new AMedia(jasperName, "doc", "application/vnd.ms-word", file, true);
		} else if ("XLS".equals(viewType)) {
			String path = System.getProperty("java.io.tmpdir");
			String prefix = makePrefix(jasperPrint.getName());
			File file = File.createTempFile(prefix, ".xls", new File(path));
			FileOutputStream fos = new FileOutputStream(file);

			JRXlsExporter exporterXLS = new JRXlsExporter();
			exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
			exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, fos);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
			exporterXLS.exportReport();
			media = new AMedia(jasperOutputName, "xls", "application/vnd.ms-excel", file, true);

		} else if ("CSV".equals(viewType)) {
			String path = System.getProperty("java.io.tmpdir");
			String prefix = makePrefix(jasperPrint.getName());
			File file = File.createTempFile(prefix, ".csv", new File(path));
			FileOutputStream fos = new FileOutputStream(file);
			JRCsvExporter exporter = new JRCsvExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);
			exporter.exportReport();

			media = new AMedia(jasperName, "csv", "application/csv", file, true);
		}

		iframe.setSrc(null);
		Events.echoEvent("onRenderReport", this, null);
		return true;
	}

	public void onRenderReport() {
		iframe.setContent(media);
	}

	public static class JasperData implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 4375195020654531202L;
		private JasperReport jasperReport;
		private File reportDir;
		private String jasperName;
		private File jasperFile;

		public JasperData(JasperReport jasperReport, File reportDir, String jasperName, File jasperFile) {
			this.jasperReport = jasperReport;
			this.reportDir = reportDir;
			this.jasperName = jasperName;
			this.jasperFile = jasperFile;
		}

		public JasperReport getJasperReport() {
			return jasperReport;
		}

		public File getReportDir() {
			return reportDir;
		}

		public String getJasperName() {
			return jasperName;
		}

		public File getJasperFile() {
			return jasperFile;
		}
	}

	class FileFilter implements FilenameFilter {
		private String reportStart;
		private File directory;
		private String extension;

		public FileFilter(String reportStart, File directory, String extension) {
			this.reportStart = reportStart;
			this.directory = directory;
			this.extension = extension;
		}

		public boolean accept(File file, String name) {
			if (file.equals(directory)) {
				if (name.startsWith(reportStart)) {
					int pos = name.lastIndexOf(extension);
					if ((pos != -1) && (pos == (name.length() - extension.length())))
						return true;
				}
			}
			return false;
		}
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget() instanceof Toolbarbutton) {
			String selected = "border: 1px solid #0000ff;";
			for (Toolbarbutton bt : arr_bt) {
				bt.setDisabled(false);
				bt.setStyle("border: 0px;");
			}

			if (event.getTarget().getId().equalsIgnoreCase("PDF")) {
				((Toolbarbutton) event.getTarget()).setDisabled(true);
				((Toolbarbutton) event.getTarget()).setStyle(selected);
				viewType = "PDF";
			} else if (event.getTarget().getId().equalsIgnoreCase("HTML")) {
				((Toolbarbutton) event.getTarget()).setDisabled(true);
				((Toolbarbutton) event.getTarget()).setStyle(selected);
				viewType = "HTML";
			} else if (event.getTarget().getId().equalsIgnoreCase("XLS")) {
				((Toolbarbutton) event.getTarget()).setDisabled(true);
				((Toolbarbutton) event.getTarget()).setStyle(selected);
				viewType = "XLS";
			} else if (event.getTarget().getId().equalsIgnoreCase("CSV")) {
				((Toolbarbutton) event.getTarget()).setDisabled(true);
				((Toolbarbutton) event.getTarget()).setStyle(selected);
				viewType = "CSV";
			} else if (event.getTarget().getId().equalsIgnoreCase("DOC")) {
				((Toolbarbutton) event.getTarget()).setDisabled(true);
				((Toolbarbutton) event.getTarget()).setStyle(selected);
				viewType = "DOC";
			}
			renderReport();
		}
	}
}
