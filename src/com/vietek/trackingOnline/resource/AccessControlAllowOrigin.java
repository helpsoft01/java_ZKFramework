package com.vietek.trackingOnline.resource;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

//import com.sun.jersey.spi.container.ContainerRequest;
//import com.sun.jersey.spi.container.ContainerResponse;
//import com.sun.jersey.spi.container.ContainerResponseFilter;

public class AccessControlAllowOrigin implements ClientResponseFilter {

	// Chú ý:phải khai báo trong web config
	// add to tab : servlet
	// <servlet>
	// <init-param>
	// <param-name>com.sun.jersey.spi.container.ContainerResponseFilters</param-name>
	// <param-value>Vietek.TrackingOnline.Resource.AccessControlAllowOrigin</param-value>
	// </init-param>
	// </servlet>

	// Nếu cho phép filter trong web config, các request sẽ được l�?c theo filter
	// này để sử dụng
	// nguồn tài nguyên này, có thể service

//	@Override
//	public ContainerResponse filter(ContainerRequest creq, ContainerResponse cresp) {
//
//		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Origin", "*");
//		// cresp.getHttpHeaders().add("Access-Control-Allow-Origin",
//		// "http://localhost:8280");
//		// cresp.getHttpHeaders().add("Access-Control-Allow-Origin",
//		// "http://localhost:8287");
//		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Credentials", "true");
//		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
//		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
//		// Note: allows CORS requests only coming from abc.vn
//		// cresp.getHttpHeaders().putSingle("Access-Control-Allow-Origin","http://abc.vn");
//		return cresp;
//	}

	@Override
	public void filter(ClientRequestContext arg0, ClientResponseContext arg1)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	// chú ý khi thêm danh sách các domain được Access Control Allow Origin:
	// phải đúng định dạng: (http://domain:port)

}