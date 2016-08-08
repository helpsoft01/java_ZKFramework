package com.vietek.tracking.ui.model;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.Predicate;

import com.vietek.taxioperation.common.AppLogger;
import com.vietek.taxioperation.model.AbstractModel;

public class PredicateSearch implements Predicate {
	String dataField = "";
	Object dataSearch = "";

	public PredicateSearch(String field, Object value) {
		this.dataField = field;
		this.dataSearch = value;
	}

	@Override
	public boolean evaluate(Object object) {
		boolean result = false;
		Object value = AbstractModel.getValue(object, dataField);
		if (value != null) {
			if (dataSearch instanceof String) {
				result = checkStringValue(object, value);
			} else if (dataSearch instanceof Integer) {
				result = checkIntegerValue(object, value);
			} else if (dataSearch instanceof Timestamp) {
				result = checkTimestampValue(object, value);
			}else if (dataSearch instanceof Double || dataSearch instanceof Float) {
				result = checkFloatValue(object, value);
			}
		} else {
			AppLogger.logDebug.info(dataSearch + "|||" + dataField);
		}

		return result;
	}



	private boolean checkIntegerValue(Object object, Object value) {
		boolean res = false;
		Integer valuefind = (Integer) dataSearch;
		Integer objectvalue = (Integer) value;
		if (valuefind.equals(-1) || objectvalue.equals(valuefind)) {
			res = true;
		}
		return res;
	}

	private Boolean checkStringValue(Object ob, Object value) {
		boolean result = false;
		String valuefind = (String) dataSearch;
		String objectvalue = (String) value;
		if (valuefind.equals("") || objectvalue.toLowerCase().contains(valuefind.toLowerCase())) {
			result = true;
		}
		return result;
	}

	private Boolean checkTimestampValue(Object ob, Object value) {
		boolean result = false;
		Timestamp timefind = (Timestamp) dataSearch;
		long dayfind = (timefind.getTime() + TimeUnit.HOURS.toMillis(7)) / TimeUnit.DAYS.toMillis(1);
		Timestamp objectvalue = (Timestamp) value;
		long dayobjectvalue = (objectvalue.getTime() + TimeUnit.HOURS.toMillis(7)) / TimeUnit.DAYS.toMillis(1);
		if (timefind.getTime() == 0 || dayfind == dayobjectvalue) {
			result = true;
		}
		return result;
	}

	private Boolean checkFloatValue(Object ob, Object value) {
		boolean result = false;
		Double findvalue = (Double) dataSearch;
		Float objectvalue = (Float)value;
		if (findvalue.equals(0.0) || (findvalue.floatValue()) == objectvalue) {
			result = true;
		}
		return result;
	}

	public String getDataField() {
		return dataField;
	}

	public void setDataField(String dataField) {
		this.dataField = dataField;
	}

	public Object getDataSearch() {
		return dataSearch;
	}

	public void setDataSearch(Object dataSearch) {
		this.dataSearch = dataSearch;
	}

}
