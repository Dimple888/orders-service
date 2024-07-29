package com.learningSpring.order_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.resolver.DefaultAddressResolverGroup;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	@Bean
	@LoadBalanced //client side load balancer - to call the services so even if there are multiple instances running it won't be confused it will call one after other or by following load balancing algorithms
	//like round robin or least bandwidth request 
	public WebClient.Builder webClientBuilder() {
		HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE); //this is of reactive framework of spring webflux
		//otherwise if there is no DefaultAddressResolverGroup then there's error is because your DNS does not resolve inventory-service to any IPv4/6.

		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
	}

}
