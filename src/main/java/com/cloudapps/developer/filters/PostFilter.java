package com.cloudapps.developer.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PostFilter implements GatewayFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		ServerHttpResponse response = exchange.getResponse();
		HttpHeaders headers = response.getHeaders();
		headers.forEach((k, v) -> {
			//log.info(k + " : " + v);
		});
		log.info("Response status code :"+response.getStatusCode());
		//log.info("Response  :"+response.toString());
		return chain.filter(exchange.mutate().response(response).build());

	}

}
