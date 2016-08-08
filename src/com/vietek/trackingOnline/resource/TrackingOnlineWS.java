package com.vietek.trackingOnline.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.vietek.taxioperation.common.AppLogger;
import com.vietek.trackingOnline.dao.DaoImpl;
import com.vietek.trackingOnline.model.Agent;
import com.vietek.trackingOnline.model.Group;
import com.vietek.trackingOnline.model.JsonInt;
import com.vietek.trackingOnline.model.JsonOnline;
import com.vietek.trackingOnline.model.JsonStringGroup;
import com.vietek.trackingOnline.model.JsonStringNumberOnline;
import com.vietek.trackingOnline.model.JsonTotalHistory;
import com.vietek.trackingOnline.model.User;
import com.vietek.trackingOnline.model.Vehicle;
import com.vietek.trackingOnline.model.VehicleByGroup;
import com.vietek.trackingOnline.model.VehicleCurrentRequest;

//@SuppressWarnings("serial")
@Path("/TrackingOnlineServices")
public class TrackingOnlineWS {

	@Context
	private HttpServletRequest request;

	// add the attribute to your implementation

	DaoImpl daoImpl;

	public TrackingOnlineWS() {
		// TODO Auto-generated constructor stub
		daoImpl = new DaoImpl();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String ping() {
		return "Hey, This is Jersey JAX-RS !";
	}

	@POST
	@Path("/Login")
	public Response Fn_Login(User user) {
		try {
			request.getSession(true);

			// GroupImpl group = new GroupImpl();

			Gson gson = new Gson();

			return Response.ok(gson.toJson(null)).build();
		} catch (Exception ex) {
			return null;
		}
		// return null;
		// return new Student();
	}

	@POST
	@Path("/GetListVehicleInGrp")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Fn_GetListVehicleInGrp(JsonStringGroup data) {
		List<VehicleByGroup> list;
		Gson gson;
		try {
			// GroupImpl group = new GroupImpl();
			list = daoImpl.cmdGetListVehicleInGrp(data.getParStringGroup());
			gson = new Gson();

			return Response.ok(gson.toJson(list)).build();
		} catch (Exception ex) {
			return null;
		} finally {
			list = null;
			gson = null;
		}
		// return null;
		// return new Student();
	}

	@POST
	@Path("/GetListGroupCar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Fn_GetListGroupCar() {
		List<Group> list;
		Gson gson;
		try {

			list = daoImpl.cmdGetListGroupCar();
			gson = new Gson();

			return Response.ok(gson.toJson(list)).build();
		} catch (Exception ex) {
			return null;
		} finally {
			list = null;
			gson = null;
		}
		// return null;
		// return new Student();
	}

	@POST
	@Path("/GetAgentByUserOnline")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Fn_GetAgentByUserOnline() {
		List<Agent> list = null;
		Gson gson;
		try {
			list = daoImpl.cmdGetAgentByUserOnline();

			gson = new Gson();

			return Response.ok(gson.toJson(list)).build();
		} catch (Exception ex) {
			return null;
		} finally {
			list = null;
			gson = null;
		}
		// return null;
		// return new Student();
	}

	@POST
	@Path("/GetNumberOfOnline")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Fn_GetNumberOfOnline(JsonStringNumberOnline par) {
		int count;
		Gson gson;
		try {
			count = daoImpl.cmdGetNumberOfOnline(par.getParStringGroup(), par.getParIntState());
			gson = new Gson();

			return Response.ok(gson.toJson(count)).build();
		} catch (Exception ex) {
			return null;
		} finally {
			gson = null;
		}
		// return null;
		// return new Student();
	}

	@POST
	@Path("/GetOnline_new")
	@Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response Fn_GetOnline_new(JsonOnline par) {
		List<Vehicle> list = null;
		Gson gson;
		try {
			AppLogger.logDebug.info("TrackingOnlineWS|" + "getPar Group():" + par.getParStringGroup()
					+ ", par.getCurrentReq(): " + par.getCurrentReq());
			list = daoImpl.cmdGetOnline_new(par.getParStringGroup(), par.getParIntState(), par.getStart(),
					par.getLimit());
			gson = new Gson();

			VehicleCurrentRequest re = new VehicleCurrentRequest();
			re.setList(list);
			re.setCurrentRequest(par.getCurrentReq());
			return Response.ok(gson.toJson(re)).build();
		} catch (Exception ex) {
			AppLogger.logDebug.error("error GetOnline_new: " + ex.getMessage(), ex);
			return null;
		} finally {
			list = null;
			gson = null;
			System.gc();
		}
		// return null;http://localhost:8287/TrackingOnilne_Service/#
		// return new Student();
	}

	@POST
	@Path("/GetDetailVehicleOnline")
	@Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response Fn_DetailVehicleOnline(JsonInt par) {
		List<JSONObject> list = null;
		Gson gson;
		try {
			AppLogger.logDebug.info(par.getNumb());
			list = daoImpl.cmdGetDetailVehicleOnline(par.getNumb());
			gson = new Gson();
			AppLogger.logDebug.info(list.toString());
			return Response.ok(list.toString()).build();
		} catch (Exception ex) {
			AppLogger.logDebug.error("GetDetailVehicleOnline" + ex.getMessage(), ex);
			return null;
		} finally {

			gson = null;
			System.gc();
		}
		// return null;
		// return new Student();
	}

	@POST
	@Path("/GetTotalRowsHistory")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Fn_GetTotalRowsHistory(JsonTotalHistory par) {
		double count;
		Gson gson;
		try {
			count = daoImpl.cmdTotalRowsHistory(par.getVehicleID(), par.getTimeRange());
			gson = new Gson();

			return Response.ok(gson.toJson(count)).build();
		} catch (Exception ex) {
			return null;
		} finally {
			gson = null;
		}
		// return null;
		// return new Student();
	}

}
