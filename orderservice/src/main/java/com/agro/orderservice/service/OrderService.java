package com.agro.orderservice.service;

import com.agro.orderservice.config.WebClientConfig;
import com.agro.orderservice.dto.OrderRequest;
import com.agro.orderservice.dto.OrderResponse;
import com.agro.orderservice.dto.UpdateOrderStatusRequest;
import com.agro.orderservice.exception.OrderNotFoundException;
import com.agro.orderservice.exception.ProductNotFoundException;
import com.agro.orderservice.exception.UserNotFoundException;
import com.agro.orderservice.model.Order;
import com.agro.orderservice.model.OrderStatus;
import com.agro.orderservice.repository.OrderRepository;
import com.agro.orderservice.service.client.ProductResponse;
import com.agro.orderservice.service.client.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderResponse createOrder(OrderRequest request) {

        UserResponse user = getUser(request.getUserId());
        ProductResponse product = getProduct(request.getProductId());

        Order order = new Order();

        order.setUserId(user.getId());
        order.setProductId(product.getId());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(product.getPrice() * request.getQuantity());


        // Default Status
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        OrderResponse response = new OrderResponse();

        response.setOrderId(savedOrder.getOrderId());
        response.setUserId(savedOrder.getUserId());
        response.setProductId(savedOrder.getProductId());
        response.setQuantity(savedOrder.getQuantity());
        response.setTotalPrice(savedOrder.getTotalPrice());
        response.setStatus(savedOrder.getStatus());
        response.setCreatedAt(savedOrder.getCreatedAt());
        response.setUpdatedAt(savedOrder.getUpdatedAt());

        return response;
    }

    //Product api call

    private ProductResponse getProduct(Long productId) {

        try {
            return webClientBuilder
                    .build()
                    .get()
                    .uri("http://PRODUCT-SERVICE/api/products/" + productId)
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block();
        }
        catch (WebClientResponseException.NotFound ex)
        {
            throw new ProductNotFoundException("Product not found...");
        }
    }

    //User api call
    private UserResponse getUser(String userId)
    {
        try {
            return webClientBuilder
                    .build()
                    .get()
                    .uri("http://USER-SERVICE/api/users/" + userId)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        }
        catch (WebClientResponseException.NotFound ex)
        {
            throw new UserNotFoundException("User not found...");
        }
    }

    public List<OrderResponse> getAllOrders()
    {
        List<Order> orders = orderRepository.findAll();

        List<OrderResponse> responseList = new ArrayList<>();

        for(Order order : orders)
        {
            OrderResponse response = new OrderResponse();

            response.setOrderId(order.getOrderId());
            response.setUserId(order.getUserId());
            response.setProductId(order.getProductId());
            response.setQuantity(order.getQuantity());
            response.setTotalPrice(order.getTotalPrice());
            response.setStatus(order.getStatus());
            response.setCreatedAt(order.getCreatedAt());
            response.setUpdatedAt(order.getUpdatedAt());

            responseList.add(response);

        }

        return responseList;
    }

    public OrderResponse getOrderById(Long orderId)
    {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not Found"));

        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUserId());
        response.setProductId(order.getProductId());
        response.setQuantity(order.getQuantity());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        return response;
    }

    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request)
    {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found..."));

        order.setStatus(request.getStatus());

        Order updatedOrder = orderRepository.save(order);

        OrderResponse response = new OrderResponse();

        response.setOrderId(updatedOrder.getOrderId());
        response.setUserId(updatedOrder.getUserId());
        response.setProductId(updatedOrder.getProductId());
        response.setQuantity(updatedOrder.getQuantity());
        response.setTotalPrice(updatedOrder.getTotalPrice());
        response.setStatus(updatedOrder.getStatus());
        response.setCreatedAt(updatedOrder.getCreatedAt());
        response.setUpdatedAt(updatedOrder.getUpdatedAt());

        return response;
    }

    public OrderResponse cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        order.setStatus(OrderStatus.CANCELLED);

        Order cancelledOrder = orderRepository.save(order);

        OrderResponse response = new OrderResponse();

        response.setOrderId(cancelledOrder.getOrderId());
        response.setUserId(cancelledOrder.getUserId());
        response.setProductId(cancelledOrder.getProductId());
        response.setQuantity(cancelledOrder.getQuantity());
        response.setTotalPrice(cancelledOrder.getTotalPrice());
        response.setStatus(cancelledOrder.getStatus());
        response.setCreatedAt(cancelledOrder.getCreatedAt());
        response.setUpdatedAt(cancelledOrder.getUpdatedAt());

        return response;
    }
}
