package com.assglobal.esms.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EsmsApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsmsApiGatewayApplication.class, args);
	}




}
