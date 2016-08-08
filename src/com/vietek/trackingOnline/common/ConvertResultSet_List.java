package com.vietek.trackingOnline.common;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ConvertResultSet_List {

	public static JSONArray ConvertToJson(ResultSet rs) throws SQLException, JSONException {
		JSONArray json = new JSONArray();
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();
		while (rs.next()) {

			JSONObject obj = new JSONObject();

			for (int i = 1; i < numColumns + 1; i++) {
				String column_name = rsmd.getColumnName(i);

				if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
					obj.put(column_name, rs.getArray(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
					obj.put(column_name, rs.getLong(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.REAL) {
					obj.put(column_name, rs.getFloat(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
					obj.put(column_name, rs.getBoolean(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
					obj.put(column_name, rs.getBlob(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
					obj.put(column_name, rs.getDouble(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
					obj.put(column_name, rs.getDouble(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
					obj.put(column_name, rs.getNString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
					obj.put(column_name, rs.getString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.CHAR) {
					obj.put(column_name, rs.getString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.NCHAR) {
					obj.put(column_name, rs.getNString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.LONGNVARCHAR) {
					obj.put(column_name, rs.getNString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.LONGVARCHAR) {
					obj.put(column_name, rs.getString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
					obj.put(column_name, rs.getByte(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
					obj.put(column_name, rs.getShort(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
					obj.put(column_name, rs.getDate(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TIME) {
					obj.put(column_name, rs.getTime(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
					obj.put(column_name, rs.getTimestamp(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BINARY) {
					obj.put(column_name, rs.getBytes(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.VARBINARY) {
					obj.put(column_name, rs.getBytes(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.LONGVARBINARY) {
					obj.put(column_name, rs.getBinaryStream(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BIT) {
					obj.put(column_name, rs.getBoolean(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.CLOB) {
					obj.put(column_name, rs.getClob(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.NUMERIC) {
					obj.put(column_name, rs.getBigDecimal(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DECIMAL) {
					obj.put(column_name, rs.getBigDecimal(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DATALINK) {
					obj.put(column_name, rs.getURL(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.REF) {
					obj.put(column_name, rs.getRef(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.STRUCT) {
					obj.put(column_name, rs.getObject(column_name)); // must
																		// be
																		// a
																		// custom
																		// mapping
																		// consists
																		// of
																		// a
																		// class
																		// that
																		// implements
																		// the
																		// interface
																		// SQLData
																		// and
																		// an
																		// entry
																		// in
																		// a
																		// java.util.Map
																		// object.
				} else if (rsmd.getColumnType(i) == java.sql.Types.DISTINCT) {
					obj.put(column_name, rs.getObject(column_name)); // must
																		// be
																		// a
																		// custom
																		// mapping
																		// consists
																		// of
																		// a
																		// class
																		// that
																		// implements
																		// the
																		// interface
																		// SQLData
																		// and
																		// an
																		// entry
																		// in
																		// a
																		// java.util.Map
																		// object.
				} else if (rsmd.getColumnType(i) == java.sql.Types.JAVA_OBJECT) {
					obj.put(column_name, rs.getObject(column_name));
				} else {
					obj.put(column_name, rs.getString(i));
				}
			}

			json.put(obj);
		}

		return json;
	}

	/*
	 * Convert ResultSet to a common JSON Object array Result is like:
	 * [{"ID":"1","NAME":"Tom","AGE":"24"}, {"ID":"2","NAME":"Bob","AGE":"26"},
	 * ...]
	 */
	public static List<JSONObject> GetFormattedResult(ResultSet rs) {
		List<JSONObject> resList = new ArrayList<JSONObject>();
		try {
			// get column names
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCnt = rsMeta.getColumnCount();
			List<String> columnNames = new ArrayList<String>();
			for (int i = 1; i <= columnCnt; i++) {
				columnNames.add(rsMeta.getColumnName(i));
			}

			while (rs.next()) { // convert each object to an human readable JSON
								// object
				JSONObject obj = new JSONObject();
				for (int i = 0; i < columnCnt; i++) {
					// get key
					String key = columnNames.get(i);
					int x = 0;
					try {
						int typeColumn = rsMeta.getColumnType(i + 1);
						switch (typeColumn) {
						case java.sql.Types.ARRAY:
							obj.put(key, rs.getArray(key));
							break;
						case java.sql.Types.BIGINT:
							obj.put(key, rs.getLong(key));
							break;
						case java.sql.Types.REAL:
							obj.put(key, rs.getFloat(key));
							break;
						case java.sql.Types.BOOLEAN:
							obj.put(key, rs.getBoolean(key));
							break;
						case java.sql.Types.BLOB:
							obj.put(key, rs.getBlob(key));
							break;
						case java.sql.Types.DOUBLE:
						case java.sql.Types.FLOAT:
							obj.put(key, rs.getDouble(key));
							break;
						case java.sql.Types.SMALLINT:
						case java.sql.Types.INTEGER:
						case java.sql.Types.TINYINT:
							obj.put(key, rs.getInt(key));
							break;
						case java.sql.Types.NVARCHAR:
							obj.put(key, rs.getNString(key));
							break;
						case java.sql.Types.VARCHAR:
							obj.put(key, rs.getString(key));
							break;
						case java.sql.Types.CHAR:
							obj.put(key, rs.getString(key));
							break;
						case java.sql.Types.NCHAR:
							obj.put(key, rs.getNString(key));
							break;
						case java.sql.Types.LONGNVARCHAR:
							obj.put(key, rs.getNString(key));
							break;
						case java.sql.Types.LONGVARCHAR:
							obj.put(key, rs.getString(key));
							break;
						case java.sql.Types.DATE:
							obj.put(key, rs.getDate(key));
							break;
						case java.sql.Types.TIME:
							obj.put(key, rs.getTime(key));
							break;
						case java.sql.Types.TIMESTAMP:
							obj.put(key, rs.getTimestamp(key));
							break;
						case java.sql.Types.BINARY:
							obj.put(key, rs.getBytes(key));
							break;
						case java.sql.Types.VARBINARY:
							obj.put(key, rs.getBytes(key));
							break;
						case java.sql.Types.LONGVARBINARY:
							obj.put(key, rs.getBinaryStream(key));
							break;
						case java.sql.Types.BIT:
							obj.put(key, rs.getBoolean(key));
							break;
						case java.sql.Types.CLOB:
							obj.put(key, rs.getClob(key));
							break;
						case java.sql.Types.NUMERIC:
							obj.put(key, rs.getBigDecimal(key));
							break;
						case java.sql.Types.DECIMAL:
							obj.put(key, rs.getBigDecimal(key));
							break;
						case java.sql.Types.DATALINK:
							obj.put(key, rs.getURL(key));
							break;
						case java.sql.Types.REF:
							obj.put(key, rs.getRef(key));
							break;
						case java.sql.Types.STRUCT:
							obj.put(key, rs.getObject(key));
							break;
						case java.sql.Types.DISTINCT:
							obj.put(key, rs.getObject(key));
							break;
						case java.sql.Types.JAVA_OBJECT:
							obj.put(key, rs.getObject(key));
							break;
						default:
							obj.put(key, rs.getString(key));
							break;
						}
					} catch (Exception e) {
						x = 2;
						e.printStackTrace();
						obj.put(key, rs.getString(key));
					}
				}
				resList.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resList;
	}
}
