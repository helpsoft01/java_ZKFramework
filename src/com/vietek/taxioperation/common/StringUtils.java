package com.vietek.taxioperation.common;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vietek.taxioperation.controller.BasicController;
import com.vietek.taxioperation.model.AbstractModel;
import com.vietek.taxioperation.model.TaxiOrder;
import com.vietek.taxioperation.realtime.Taxi;
import com.vietek.taxioperation.util.MapUtils;

public class StringUtils {

	public static final String STYLE_REQUIRED_FIELD = "<span class = ''>*</span>";

	public static boolean isEmpty(String str) {
		return ((str == null) || (str.trim().length() == 0));
	}

	public static boolean isNotEmpty(String str) {
		return (!(isEmpty(str)));
	}

	public static boolean isBlank(String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < strLen; ++i) {
			if (!(Character.isWhitespace(str.charAt(i)))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return (!(isBlank(str)));
	}

	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(CommonDefine.EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isValidPhoneNumber(String num) {
		Pattern pattern = Pattern.compile(CommonDefine.NUM_PATTERN);
		Matcher matcher = pattern.matcher(num);
		return matcher.matches();
	}

	public static boolean checkLength(String str, int min, int max) {
		if ((str == null) || (str.length() == 0)) {
			return false;
		} else if (str.length() < min || str.length() > max) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean checkMinLength(String str, int min) {
		if ((str == null) || (str.length() == 0)) {
			return false;
		} else if (str.length() < min) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean checkMaxLength(String str, int max) {
		if ((str == null) || (str.length() == 0)) {
			return true;
		} else if (str.length() > max) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean equals(String str1, String str2) {
		return ((str1 == null) ? false : (str2 == null) ? true : str1.equals(str2));
	}

	public static boolean checkRegexStr(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static boolean isHasSpecialChar(String str) {
		Pattern pattern = Pattern.compile(CommonDefine.NUM_CHARACTERS_PATTERN);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static boolean isHasWhiteSpaceBeginEnd(String str) {
		if ((str == null) || (str.length() == 0))
			return false;
		return (str.endsWith(" ") || str.startsWith(" "));
	}

	public static boolean isHasWhiteSpace(String str) {
		if ((str == null) || (str.length() == 0))
			return false;
		return (str.contains(" "));
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	public static Time getTimeBegin(String time) {
		Time tm = java.sql.Time.valueOf(time);
		return tm;

	}

	public static boolean checktimestam(Time tm1, Time tm2) {
		if (tm1.getTime() - tm2.getTime() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static Map<String, Integer> mapFromString(String varstring) {
		Map<String, Integer> mapresult = new HashMap<String, Integer>();
		String[] arrayconf = varstring.split("-");
		for (String str : arrayconf) {
			String[] tmp = str.split("=");
			mapresult.put(tmp[0].trim(), Integer.valueOf(tmp[1].trim()));
		}
		return mapresult;
	}

	public static String getContentMsg(TaxiOrder order, Taxi taxi, int status) {
		String msg = "";
		if (taxi == null) {
			return msg;
		}
		if (status == 2) {
			// double distance = MapUtils.distance(order.getBeginOrderLon(),
			// order.getBeginLat(), taxi.getLongitude(), taxi.getLattitute());
			// int minute = (int)distance/500 + 1;
			// msg = "MAILINHTAXI - " + "Lai xe: " + order.getDriver().getName()
			// + ", So tai: "
			// + taxi.getVehicle().getValue() + ", So DT: " +
			// order.getDriver().getPhoneOffice()
			// + ", se don Quy khach trong " + minute + " phut." + " Tran trong.
			// MLTaxi VietNam." ;
			StringBuilder sb = new StringBuilder();
			sb.append("MAILINHTAXI - Lai xe: ");
			sb.append(order.getDriver().getName());
			sb.append(", So tai: ").append(taxi.getVehicle().getValue());
			sb.append(", se don Quy khach trong it phut toi. Tran trong. MLTaxi VietNam.");
			msg = sb.toString();
		} else if (status == 3) {
			double distance = MapUtils.distance(order.getBeginOrderLon(), order.getBeginLat(), order.getEndOrderLon(),
					order.getEndOrderLat());
			int minute = (int) distance / 500 + 1;
			msg = "So tai: " + taxi.getVehicle().getValue()
					+ " Da bat dau chuyen di, du kien chuyen di ket thuc trong vong " + minute
					+ " phut. Cam on va chuc quy khach co chuyen di vui ve !";
		} else if (status == 4) {
			msg = "Chuyen di da ket thuc. Cam on quy khach da su dung dich vu cua Mai Linh Taxi !";
		}
		return msg;
	}

	public static Boolean checkExisValue(AbstractModel model, Object value, String field) {
		Boolean result = false;
		BasicController<?> controller = model.getControler();
		String query = "from " + model.getClass().getName().replace("com.vietek.taxioperation.model.", "") + " "
				+ "where " + field + "= ?";
		List<?> lstmp = controller.find(query, value);
		if (lstmp != null && lstmp.size() > 0) {
			if (model.getId() == 0) {
				result = true;
			} else if (model.getId() > 0 && model.getId() != ((AbstractModel) lstmp.get(0)).getId()) {
				result = true;
			}
		}
		return result;

	}

	public static String valueOfTimestamp(Timestamp timestamp) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String result = "";
		if (timestamp != null) {
			result = dateFormat.format(new Date(timestamp.getTime()));
		}
		return result;
	}

	public static String valueOfTimestamp(Timestamp timestamp, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date(timestamp.getTime()));
	}

	public static String MilisToHours(long milis) {
		String resule = "";
		resule = resule + (milis / 3600000) + "Giờ &nbsp" + ((milis % 3600000) / 60000) + " Phút&nbsp"
				+ (((milis % 3600000) % 60000) / 1000) + " Giây";
		return resule;
	}

	public static String doubleFormat(double value) {
		String result = "";
		NumberFormat formatter = new DecimalFormat("#0.00");
		result = formatter.format(value);
		return result;
	}

	public static String priceWithoutDecimal(double price) {
		DecimalFormat formatter = new DecimalFormat("###,###,###.##");
		return formatter.format(price);
	}

	public static String Trim(String stringToTrim, String stringToRemove) {
		String answer = stringToTrim;

		while (answer.startsWith(stringToRemove)) {
			answer = answer.substring(stringToRemove.length());
		}

		while (answer.endsWith(stringToRemove)) {
			answer = answer.substring(0, answer.length() - stringToRemove.length());
		}

		return answer;
	}
}
