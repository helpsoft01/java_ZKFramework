package com.vietek.trackingOnline.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
//import com.sun.jersey.spi.container.ContainerRequest;
//import com.sun.jersey.spi.container.ContainerResponse;
//import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.vietek.trackingOnline.model.ObjDemo;

@Path("/jsonServices")
public class SVDemo {
	@GET
	@Path("/print/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public ObjDemo produceJSON(@PathParam("name") String name) {

		ObjDemo st = new ObjDemo(name, "Diaz", 22, 1);

		return st;

	}

	@GET
	@Path("/printList_json/{id}/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public String produceListJSON(@PathParam("id") int id, @PathParam("name") String name) {
		ArrayList<ObjDemo> list = new ArrayList<>();

		ObjDemo st = new ObjDemo(name, "Diaz", 22, id);
		list.add(st);
		Gson gson = new Gson();
		return gson.toJson(list);
	}

	@GET
	@Path("/printList_xml/{name}")
	@Produces({ MediaType.TEXT_XML }) // MediaType.APPLICATION_XML
	public List<ObjDemo> produceList_XNL(@PathParam("name") String name) {
		ArrayList<ObjDemo> list = new ArrayList<>();

		ObjDemo st = new ObjDemo(name, "Diaz", 22, 1);
		list.add(st);

		return list;
	}

	@POST
	@Path("/send")
	// @Consumes(MediaType.APPLICATION_JSON) //
	// @Produces("application/octet-stream")

	public Response consumeJSON(ObjDemo st) {

		// ArrayList<Student> list = new ArrayList<>();
		//
		ObjDemo st_ = new ObjDemo(st.getFirstName(), st.getLastName(), st.getAge(), st.getId());
		// list.add(st);
		//
		// return list;

		ArrayList<ObjDemo> list = new ArrayList<>();
		list.add(st_);
		Gson gson = new Gson();
		// return gson.toJson(list);
		// String output = st.toString();
		// //

		return Response.ok(gson.toJson(list)).build();

		// return new Student();
	}

	@GET
	@Path("/returnJSON")
	@Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON) //
	public Response returnJSON() {

		// ArrayList<Student> list = new ArrayList<>();
		//
		ObjDemo st = new ObjDemo("First name", "Last name", 23, 1);
		// list.add(st);
		//
		// return list;

		return Response.ok(st).header("Access-Control-Allow-Origin", "*").build();
	}
}



























// 1. Tạo web: Dynamic web project
// 2. Tạo file web.config
// 3. convert sang Maven project: phải chuột project -> configure > convert
// maven project
// 4. thêm vào file pom.xml
// <dependencies>
// <dependency>
// <groupId>com.sun.jersey</groupId>
// <artifactId>jersey-server</artifactId>
// <version>1.9</version>
// </dependency>
//
// <dependency>
// <groupId>com.sun.jersey</groupId>
// <artifactId>jersey-client</artifactId>
// <version>1.9</version>
// </dependency>
//
// <dependency>
// <groupId>com.sun.jersey</groupId>
// <artifactId>jersey-json</artifactId>
// <version>1.9</version>
// </dependency>
// <dependency>
// <groupId>com.google.code.gson</groupId>
// <artifactId>gson</artifactId>
// <version>2.3.1</version>
// <scope>compile</scope>
// </dependency>
// </dependencies>
// 5. Update meven project: để lấy tất cả các file .jar để hỗ trợ khi code
// 6.không được sửa: <url-pattern>/rest/*</url-pattern> : đây là đư�?ng dẫn cố
// định của resfService
// 7. trong web.xml thêm Access-Control-Allow-Origin tới tab <servlet>
// với class: Vietek.TrackingOnline.Resource.AccessControlAllowOrigin: là class
// l�?c các request và cho phép domain nào được phé request tới sử dụng service
// <init-param>
// <param-name>com.sun.jersey.spi.container.ContainerResponseFilters</param-name>
// <param-value>Vietek.TrackingOnline.Resource.AccessControlAllowOrigin</param-value>
// </init-param>