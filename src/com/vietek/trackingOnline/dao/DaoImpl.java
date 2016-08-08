package com.vietek.trackingOnline.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.vietek.trackingOnline.common.ConvertResultSet_List;
import com.vietek.trackingOnline.common.TimeSpan;
import com.vietek.trackingOnline.model.Agent;
import com.vietek.trackingOnline.model.Group;
import com.vietek.trackingOnline.model.Vehicle;
import com.vietek.trackingOnline.model.VehicleByGroup;

public class DaoImpl extends DBMysql implements Dao {

	@Override
	public List<Group> cmdGetListGroupCar() {
		List<Group> list = new ArrayList<>();
		CallableStatement cstmt = null;
		try {
			String SQL = "{call cmdGetVehicleGroupName (?)}";
			if (connection.isClosed())
				connection = openConnection();

			cstmt = connection.prepareCall(SQL);
			// cstmt.setString(1, selectedAgent);
			cstmt.setString(1, user.getUserId());
			ResultSet rs = cstmt.executeQuery();

			Group group;
			while (rs.next()) {
				// Retrieve by column name
				try {
					group = new Group(rs);
					list.add(group);
				} catch (Exception ex) {
				}

			}
		} catch (SQLException e) {
			int x = 0;
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
					cstmt = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public List<Agent> cmdGetAgentByUserOnline() {
		List<Agent> list = new ArrayList<>();
		CallableStatement cstmt = null;
		try {
			String SQL = "{call cmdGetAgentByUserOnline (?,?)}";
			if (connection.isClosed())
				connection = openConnection();

			cstmt = connection.prepareCall(SQL);
			cstmt.setString(1, user.getUserId());
			cstmt.setInt(2, 0);
			ResultSet rs = cstmt.executeQuery();

			Agent agent;
			while (rs.next()) {
				// Retrieve by column name
				try {
					agent = new Agent(rs);
					list.add(agent);
				} catch (Exception ex) {
				}

			}
		} catch (SQLException e) {
			int x = 0;
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
					cstmt = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public List<VehicleByGroup> cmdGetListVehicleInGrp(String selectedGroup) {
		List<VehicleByGroup> list = new ArrayList<>();
		CallableStatement cstmt = null;
		try {
			String SQL = "{call cmdGetVehileByGroup (?,?,?)}";
			if (connection.isClosed())
				connection = openConnection();

			cstmt = connection.prepareCall(SQL);
			cstmt.setString(1, selectedGroup);
			cstmt.setString(2, "0");
			cstmt.setString(3, user.getUserId());
			ResultSet rs = cstmt.executeQuery();

			VehicleByGroup vehicle;
			while (rs.next()) {
				// Retrieve by column name
				list.add(new VehicleByGroup(rs));
			}
		} catch (SQLException e) {
			int x = 0;
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
					cstmt = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public int cmdGetNumberOfOnline(String selectedGroup, int selectedState) {
		CallableStatement cstmt = null;
		int count = 0;
		try {
			String SQL = "{call cmdGetNumberOfOnline (?,?,?,?,?)}";
			if (connection.isClosed())
				connection = openConnection();

			cstmt = connection.prepareCall(SQL);
			cstmt.setString(1, selectedGroup);
			cstmt.setString(2, "0");
			cstmt.setInt(3, selectedState);
			cstmt.setString(4, "0");
			cstmt.setString(5, user.getUserId());
			ResultSet rs = cstmt.executeQuery();

			if (rs.next()) {
				// Retrieve by column name
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			int x = 0;
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
					cstmt = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return count;
	}

	@Override
	public List<Vehicle> cmdGetOnline_new(String selectedGroup,
			int selectedState, int start, int limit) {

		List<Vehicle> list = new ArrayList<>();
		CallableStatement cstmt = null;
		int count = 0;
		try {
			String SQL = "{call cmdGetOnline_new (?,?,?,?,?,?,?)}";
			if (connection.isClosed())
				connection = openConnection();

			cstmt = connection.prepareCall(SQL);
			cstmt.setString(1, selectedGroup);
			cstmt.setString(2, "0");
			cstmt.setInt(3, selectedState);
			cstmt.setString(4, "0");
			cstmt.setString(5, user.getUserId());
			cstmt.setInt(6, start);
			cstmt.setInt(7, limit);
			ResultSet rs = cstmt.executeQuery();

			Vehicle vehicle;
			SimpleDateFormat formatTime = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date dateNow = new Date();
			TimeSpan timespanNow = new TimeSpan(dateNow.getDate(),
					dateNow.getHours(), dateNow.getMinutes(),
					dateNow.getSeconds());
			long diff;
			long diffTotalMinutes;
			String urlIcon = "", rootPath = "";
			while (rs.next()) {
				// Retrieve by column name
				urlIcon = "";
				rootPath = "../../TrackingOnline/Resource/Image/State/";
				try {
					vehicle = new Vehicle(rs);
					Date timeLog = formatTime.parse(vehicle.getTimeLog());

					TimeSpan timespan = timespanNow.Subtract(new TimeSpan(
							timeLog.getDate(), timeLog.getHours(), timeLog
									.getMinutes(), timeLog.getSeconds()));
					diffTotalMinutes = timespan.TotalMinutes();

					// diff = dateNow.getTime() - timeLog.getTime();
					// diffTotalMinutes = diff / (60 * 1000) % 60;

					if (diffTotalMinutes >= 4320) {
						urlIcon = rootPath + "maintain-16.png";
					} else {

						if ((diffTotalMinutes >= 15 && vehicle
								.getLastGPSSpeed() > 10)
								|| (diffTotalMinutes >= 30 && vehicle
										.getLastGPSSpeed() <= 10)) {
							urlIcon = rootPath + "gsm-16.png";
						} else {
							if (Math.abs(vehicle.getLati()) <= 0
									&& Math.abs(vehicle.getLongi()) <= 0) {
								urlIcon = rootPath + "lostgps-16.png";
							}
						}
					}
					if (urlIcon != "") {
						urlIcon = urlIcon.replace("-16", "-28");
						vehicle.setSmallUrlIcon(urlIcon);
					} else {
						urlIcon = vehicle.getSmallUrlIcon().toString();
						urlIcon = urlIcon.replace("../../", "../../TrackingOnline/");
						vehicle.setSmallUrlIcon(urlIcon);
					}
					list.add(vehicle);
				} catch (Exception ex) {
				}
			}
			if (cstmt != null) {
				try {
					cstmt.close();
					cstmt = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			int x = 0;
		} finally {

		}
		return list;

	}

	@Override
	public List<JSONObject> cmdGetDetailVehicleOnline(int vehicleID) {

		CallableStatement cstmt = null;
		// List<JSONObject> list = null;
		List<JSONObject> list = null;
		try {
			String SQL = "{call cmdGetOnlineDetail(?)}";
			if (connection.isClosed())
				connection = openConnection();

			cstmt = connection.prepareCall(SQL);
			cstmt.setInt(1, vehicleID);
			ResultSet rs = cstmt.executeQuery();
			list = ConvertResultSet_List.GetFormattedResult(rs);

			JSONObject js = (JSONObject) list.get(0);
			String urlIcon = js.getString("SmallUrlIcon").replace("../../", "");
			js.remove("SmallUrlIcon");
			js.put("SmallUrlIcon", urlIcon);

		} catch (SQLException e) {
			int x = 0;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
					cstmt = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public double cmdTotalRowsHistory(int vehicleID, String timeRange) {
		CallableStatement cstmt = null;

		double totalHistory = 0;
		// get device ID
		int deviceID = getDeviceIDByVehicleID(vehicleID);
		Date stopTime = new Date();
		stopTime.setHours(new Date().getHours() + 7);
		Date startTime;

		Date dat;
		Date oldDate = new Date(); // oldDate == current time
		Date newDate = new Date(oldDate.getTime() + TimeUnit.HOURS.toMillis(2));

		float floatTimeRange = Float.parseFloat(timeRange);
		if (floatTimeRange < 1) {
			int Minutes = (int) (floatTimeRange * 60);
			startTime = new Date(stopTime.getTime()
					- TimeUnit.HOURS.toMillis(Minutes));
			;
		} else {
			int intTimeRange = Integer.parseInt(timeRange);
			startTime = new Date(stopTime.getTime()
					- TimeUnit.HOURS.toMillis(intTimeRange));
			;
		}
		// var startTimeUnix = (int)
		// ConvertToUnixTime.FromDateTimeToUnixTime(startTime);
		// var stopTimeUnix = (int)
		// ConvertToUnixTime.FromDateTimeToUnixTime(stopTime);
		// if (tbout != null && tbout.Rows.Count > 0) {
		// var deviceid = Convert.ToUInt16(tbout.Rows[0][0]);
		// MongoCollection<TotalLog> logcount =
		// SharedFunction.MongoDbNew.MongoDatabase.GetCollection < TotalLog >
		// ("device"
		// + deviceid);
		// var query1 = Query.And(Query.GTE("DateLogInSecond", startTimeUnix),
		// Query.LTE("DateLogInSecond", stopTimeUnix));
		// var query2 = Query.GTE("DateLogInSecond", startTimeUnix);
		// var result1 = logcount.Find(query1).Count();
		// var result2 =
		// logcount.Find(query2).SetSortOrder(SortBy.Ascending("DateLogInSecond")).Skip(1).Take(1)
		// .SingleOrDefault();
		// if (result2 != null) {
		// JsonResult json = Json(result1 + "," + string.Format("{0:dd/MM/yyyy
		// HH:mm:ss}", stopTime) + ","
		// + deviceid + "," + result2.Id);
		// return json;
		// }
		// }
		//
		return totalHistory;
	}

	private int getDeviceIDByVehicleID(int vehicleID) {
		int deviceID = 0;

		CallableStatement cstmt = null;
		List<JSONObject> list = null;
		try {
			String SQL = "{call cmdGetDeviceIDByVehicleID(?)}";
			if (connection.isClosed())
				connection = openConnection();

			cstmt = connection.prepareCall(SQL);
			cstmt.setInt(1, vehicleID);
			ResultSet rs = cstmt.executeQuery();

			if (rs.next()) {
				// Retrieve by column name
				deviceID = rs.getInt(1);
			}
		} catch (SQLException e) {
			int x = 0;
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
					cstmt = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return deviceID;
	}

}
