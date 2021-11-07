package com.assglobal.esms.api.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.assglobal.esms.api.gateway.filters.GlobalFilter;
import com.assglobal.esms.api.gateway.filters.PostFilter;
import com.assglobal.esms.api.gateway.filters.PreFilter;

@Configuration
public class GatewayConfig {
	
	@Autowired
	private GlobalFilter globalFilter;
	
	@Autowired
	private PreFilter preFilter;
	
	@Autowired
	private PostFilter postFilter;
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		
		return builder.routes()
					.route("esms-api",r -> r.path("/esms/api/**")
			
							.filters(f -> 
							f.filter(preFilter)
							.filter(globalFilter)
							.filter(postFilter)
							.addRequestHeader("first-request", "first-request-header")
							.addResponseHeader("first-response", "first-response-header")
							.removeRequestHeader("Cookie"))
						    .uri("lb://esms-api/")
						    .id("esms-api"))	
							.build();
	}

}
