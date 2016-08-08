package com.vietek.taxioperation.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.controller.ConfigController;
import com.vietek.taxioperation.model.Config;

public class ConfigUtil {
	private static ResourceBundle bundle;
	public static ConcurrentHashMap<String, String> cacheValuesConfig = new ConcurrentHashMap<String, String>();
	private static final ConfigUtil configuntil = new ConfigUtil();

	public static ConfigUtil getConfigUtil() {
		return configuntil;
	}

	public static String getConfig(String key) {
		String retVal = null;
		try {
			if (bundle == null) {
				bundle = ResourceBundle.getBundle("config");
			}
			retVal = bundle.getString(key);
		} catch (Exception e) {
			// retVal = key;
		}
		if (retVal == null) {
			ConfigController configController = (ConfigController) ControllerUtils
					.getController(ConfigController.class);
			List<Config> tmp = configController.find("from Config where name = ?", key);
			if (tmp.size() == 1) {
				retVal = tmp.get(0).getValue();
			}
		}
		return retVal;
	}

	public static void reloadMap(String key, String value) {
		cacheValuesConfig.put(key, value);
	}

	public static String getValueConfig(String key, String defaultValue) {
		String result = null;
		try {
			result = cacheValuesConfig.get(key);
			if (result == null) {
				ConfigController configController = (ConfigController) ControllerUtils
						.getController(ConfigController.class);
				List<Config> tmp = configController.find("from Config where name = ?", key);
				if (tmp != null && tmp.size() >= 1) {
					result = tmp.get(0).getValue();
					cacheValuesConfig.put(key, result);
				} else {
					result = defaultValue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = defaultValue;
		}
		return result;
	}

	public static int getValueConfig(String key, int defaultValue) {
		String result = null;
		int value = 0;
		try {
			result = cacheValuesConfig.get(key);
			if (result == null) {
				ConfigController configController = (ConfigController) ControllerUtils
						.getController(ConfigController.class);
				List<Config> tmp = configController.find("from Config where name = ?", key);
				if (tmp != null && tmp.size() >= 1) {
					result = tmp.get(0).getValue();
					cacheValuesConfig.put(key, result);
					value = Integer.parseInt(result);
				} else {
					value = defaultValue;
				}
			} else
				value = Integer.parseInt(result);

		} catch (Exception e) {
			e.printStackTrace();
			value = defaultValue;
		}
		return value;
	}

	public static String getConfig(String key, String defaultValue) {
		String retVal = null;
		try {
			if (bundle == null) {
				bundle = ResourceBundle.getBundle("config");
			}
			retVal = bundle.getString(key);
		} catch (Exception e) {
			// retVal = key;
		}
		if (retVal == null) {
			ConfigController configController = (ConfigController) ControllerUtils
					.getController(ConfigController.class);
			List<Config> tmp = configController.find("from Config where name = ?", key);
			if (tmp.size() == 1) {
				retVal = tmp.get(0).getValue();
			}
		}
		if (retVal == null) {
			retVal = defaultValue;
		}
		return retVal;
	}

	public static int getConfig(String key, int defaultValue) {
		int retVal = 0;
		try {
			if (bundle == null) {
				bundle = ResourceBundle.getBundle("config");
			}
			retVal = Integer.parseInt(bundle.getString(key));
		} catch (Exception e) {
			retVal = defaultValue;
		}
		return retVal;
	}

	/**
	 * Get value from config.properties file
	 * 
	 * @author Dzungnd
	 * @param key
	 * @return a string present by "key", store in config file
	 */
	public String getPropValues(String key) {
		InputStream inputStream = null;
		String result = "";
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream("/" + propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			String retVal = prop.getProperty(key);
			result = new String(retVal.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public Object setPropValues(String key, String value) {
		OutputStream output = null;
		Object rep = null;
		String propFileName = "config.properties";
		try {
			Properties props = new Properties();
			String filePath = getClass().getClassLoader().getResource("").toURI().toString();
			filePath = URLDecoder.decode(filePath, "utf-8");
			filePath = filePath.replace("\\", "/");
			filePath = filePath.replace("file:/", "");
			filePath = filePath + "/" + propFileName;

			File file = new File(filePath);
			output = new FileOutputStream(file);
			rep = props.setProperty(key, value);
			props.store(output, null);

		} catch (Exception e) {
			AppLogger.logDebug.error("", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return rep;
	}
}
