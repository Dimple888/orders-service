package com.learningSpring.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.learningSpring.order_service.repository.OrderRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {OrderRepository.class})
@EntityScan("com.learningSpring.order_service")
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
