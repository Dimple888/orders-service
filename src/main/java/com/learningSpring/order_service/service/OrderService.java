package com.learningSpring.order_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.learningSpring.order_service.dto.InventoryResponseDto;
import com.learningSpring.order_service.dto.OrderLineItemsDto;
import com.learningSpring.order_service.dto.OrderRequest;
import com.learningSpring.order_service.model.Order;
import com.learningSpring.order_service.model.OrderLineItems;
import com.learningSpring.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;

	private final WebClient.Builder webClientBuilder;

	public String placeOrder(OrderRequest orderRequest) {

		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());

		List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream()
				.map(x -> mapOrderLineDtoTomodel(x)).toList();
		order.setOrderLineItems(orderLineItemsList);
		
		
		List<String> skuCodes = order.getOrderLineItems().stream().map(OrderLineItems :: getSkucode).toList();

		// call inventory service and place order if it is in stock
		InventoryResponseDto[] isStock = webClientBuilder.build().get()
				//.uri("http://localhost:8082/api/inventory",uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				.uri("http://inventory-service/api/inventory",uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()) //use the service name after eureka server instead of hardcoding host posrt
				.retrieve() // to retrive response
				.bodyToMono(InventoryResponseDto[].class) // return type as boolean Mono is one datatype in reactive framework
				.block(); // by default webclient does async so to make it sync call use block
		
		
		boolean allProductsInStock = Arrays.stream(isStock).allMatch(InventoryResponseDto::isInStcok);
		
		if (allProductsInStock) {
			orderRepository.save(order);
			return "Order placed succesfully";
		} else {
			throw new IllegalArgumentException("Product is not in stock,Please try again later");
		}

	}

	private OrderLineItems mapOrderLineDtoTomodel(OrderLineItemsDto order) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(order.getPrice());
		orderLineItems.setQuantity(order.getQuantity());
		orderLineItems.setSkucode(order.getSkucode());
		return orderLineItems;
	}

}
