package com.agro.orderservice.service;

import com.agro.orderservice.dto.OrderRequest;
import com.agro.orderservice.dto.OrderResponse;
import com.agro.orderservice.model.Order;
import com.agro.orderservice.model.OrderStatus;
import com.agro.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;

    public OrderResponse createOrder(OrderRequest request) {

        Order order = new Order();

        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());

        // Temporary
        order.setTotalPrice(0.0);

        // Default Status
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        OrderResponse response = new OrderResponse();

        response.setOId(savedOrder.getOId());
        response.setUserId(savedOrder.getUserId());
        response.setProductId(savedOrder.getProductId());
        response.setQuantity(savedOrder.getQuantity());
        response.setTotalPrice(savedOrder.getTotalPrice());
        response.setStatus(savedOrder.getStatus());
        response.setCreatedAt(savedOrder.getCreatedAt());
        response.setUpdatedAt(savedOrder.getUpdatedAt());

        return response;
    }
}
