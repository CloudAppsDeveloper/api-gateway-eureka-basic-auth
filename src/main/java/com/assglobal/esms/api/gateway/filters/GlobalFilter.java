package com.assglobal.esms.api.gateway.filters;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.assglobal.esms.api.gateway.model.ApiUsers;
import com.assglobal.esms.api.gateway.services.ApiUsersServiceImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter implements GatewayFilter {

	@Autowired
	private ApiUsersServiceImpl apiUsersServiceImpl;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		log.info("Inside SCGWGlobalFilter.apply() method");

		ServerHttpRequest request = exchange.getRequest();

		// validating Authorisation of the request

		if (!request.getHeaders().containsKey("Authorization")) {
			log.info("No Authorization header");
			return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
		}
		String authorizationHeader = request.getHeaders().getFirst("Authorization");

		log.info("Authorization Header :" + authorizationHeader);
		if (!this.isAuthenticationValid(authorizationHeader)) {
			log.info("No Authorization header");
			return this.onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
		}

		// validating Authentication of the request

		if (!request.getHeaders().containsKey("client-id") && !request.getHeaders().containsKey("product-id")) {
			log.info("No Authentication headers found: client-id and product-id are required");
			return this.onError(exchange, "No Authentication headers found: client-id and product-id are required",
					HttpStatus.UNAUTHORIZED);
		}
		String clientId = request.getHeaders().getFirst("client-id");
		String productId = request.getHeaders().getFirst("product-id");

		log.info("Authentication Headers : clientId: " + clientId + ", productId: " +productId);

		if (!this.isAuthorizationValid(clientId, productId)) {
			log.info("Invalid Authorization headers: : client-id or product-id");
			return this.onError(exchange, "Invalid Authorization headers: : client-id or product-id",
					HttpStatus.UNAUTHORIZED);
		}
		ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
				.header("secret", RandomStringUtils.random(10)).build();

		return chain.filter(exchange.mutate().request(modifiedRequest).build());

	}

	private boolean isAuthorizationValid(String clientId, String productId) {
		boolean isValid = false;
		log.info("Checking Authorization...");

		// Logic for checking the value

		ApiUsers user = apiUsersServiceImpl.getApiUser(clientId, productId);
		if (ObjectUtils.isEmpty(user)) {
			log.info("ApiUsers deails not found: " + user);
			log.info("Request Not Authenticated...!, Invalid Details.");
			return isValid;
		}
		if (StringUtils.isEmpty(user.getClientId()) || StringUtils.isEmpty(user.getProductId())) {
			log.info("ClientId: " + user.getClientId() + "ProductId: " + user.getProductId());
			return isValid;
		}
		if (user.getClientId().equals(clientId) && user.getProductId().equals(productId)) {
			log.info("Request Authenticated...!");
			isValid = true;
		}
		return isValid;
	}

	private boolean isAuthenticationValid(String authorizationHeader) {
		boolean isValid = false;
		String username = null;
		String password = null;
		log.info("Checking Authentication.. Started..!");

		// Logic for checking the value
		final String authorization = authorizationHeader;
		if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
			// Authorization: Basic base64credentials
			String base64Credentials = authorization.substring("Basic".length()).trim();
			byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
			String credentials = new String(credDecoded, StandardCharsets.UTF_8);
			// credentials = username:password
			final String[] values = credentials.split(":", 2);
			if (values.length >= 1) {
				username = values[0];
				password = values[1];
				// log.info("Credentials:: username: " + username + ", password: " + password);
				if ("admin".equals(username) && "admin".equals(password)) {
					isValid = true;
					log.info("Request Authorized...!-- Completed");
				} else {
					isValid = false;
					log.info("Request Un-Authorized...!");
				}
			} else {
				isValid = false;
				log.info("Request Un-Authorized...!");
			}
		}
		return isValid;
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		log.info("GlobalFilter onError....!");
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

}
