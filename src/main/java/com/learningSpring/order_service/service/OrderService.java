package com.learningSpring.order_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.learningSpring.order_service.dto.OrderLineItemsDto;
import com.learningSpring.order_service.dto.OrderRequest;
import com.learningSpring.order_service.model.Order;
import com.learningSpring.order_service.model.OrderLineItems;
import com.learningSpring.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	public void placeOrder(OrderRequest orderRequest) {
		
		Order order =  new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream().map(x -> mapOrderLineDtoTomodel(x)).toList();
		order.setOrderLineItems(orderLineItemsList);
		orderRepository.save(order);
		
	}

	private OrderLineItems mapOrderLineDtoTomodel(OrderLineItemsDto order) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(order.getPrice());
		orderLineItems.setQuantity(order.getQuantity());
		orderLineItems.setSkucode(order.getSkucode());
		return orderLineItems;
	}

}
