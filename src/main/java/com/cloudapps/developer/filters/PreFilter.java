package com.cloudapps.developer.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PreFilter implements GatewayFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest().mutate().build();
		log.info("API Request: " + request.getBody().toString());
		HttpHeaders headers = request.getHeaders();
		headers.forEach((k, v) -> {
			log.info(k + " : " + v);
		});
		return chain.filter(exchange.mutate().request(request).build());
	}

}
