package com.agro.orderservice.service;

import com.agro.orderservice.dto.AdminDashboardResponse;
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

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderResponse createOrder(OrderRequest request, String email, String role) {

        System.out.println("========== ORDER SERVICE ==========");
        System.out.println("Email : " + email);
        System.out.println("Role  : " + role);
        System.out.println("===================================");

        UserResponse user;

        try {
            user = webClientBuilder
                    .build()
                    .get()
                    .uri("http://USER-SERVICE/api/users/email/" + email)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            throw new UserNotFoundException("User not found...");
        }

        ProductResponse product = getProduct(request.getProductId());

        decreaseProductStock(product.getId(), request.getQuantity());

        Order order = new Order();

        order.setUserId(user.getId());
        order.setProductId(product.getId());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(product.getPrice() * request.getQuantity());
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

    // ================= PRODUCT SERVICE CALL =================

    private ProductResponse getProduct(Long productId) {

        try {
            return webClientBuilder
                    .build()
                    .get()
                    .uri("http://PRODUCT-SERVICE/api/products/" + productId)
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block();

        } catch (WebClientResponseException.NotFound ex) {
            throw new ProductNotFoundException("Product not found...");
        }
    }

    // ================= GET ALL ORDERS =================

    public List<OrderResponse> getAllOrders() {

        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> responseList = new ArrayList<>();

        for (Order order : orders) {

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

    // ================= GET ORDER BY ID =================

    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

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

    // ================= UPDATE ORDER STATUS =================

    public OrderResponse updateOrderStatus(
            Long id,
            UpdateOrderStatusRequest request,
            String email,
            String role) {

        // Get Order
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found"));

        // Get Logged-in User
        UserResponse user;

        try {

            user = webClientBuilder
                    .build()
                    .get()
                    .uri("http://USER-SERVICE/api/users/email/" + email)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();

        } catch (WebClientResponseException.NotFound ex) {

            throw new UserNotFoundException("User not found");
        }

        // Get Product Details
        ProductResponse product = getProduct(order.getProductId());

        // Authorization Check
        if (!role.equals("ADMIN")
                && !product.getShopkeeperId().equals(user.getId())) {

            throw new RuntimeException(
                    "You are not allowed to update this order");
        }

        // Update Status
        order.setStatus(request.getStatus());

        Order updatedOrder = orderRepository.save(order);

        // Response
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

    // ================= CANCEL ORDER =================

    public OrderResponse cancelOrder(
            Long id,
            String email,
            String role) {

        // Get Order
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found"));

        // Get Logged-in User
        UserResponse user;

        try {

            user = webClientBuilder
                    .build()
                    .get()
                    .uri("http://USER-SERVICE/api/users/email/" + email)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();

        } catch (WebClientResponseException.NotFound ex) {

            throw new UserNotFoundException("User not found");
        }

        // Authorization
        if (!role.equals("ADMIN")
                && !order.getUserId().equals(user.getId())) {

            throw new RuntimeException(
                    "You are not allowed to cancel this order");
        }

        // Optional: Don't allow cancelling completed orders
        if (order.getStatus() == OrderStatus.DELIVERED) {

            throw new RuntimeException(
                    "Delivered order cannot be cancelled");
        }

        // Cancel
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

    public List<OrderResponse> getMyOrders(String email) {

        UserResponse user;

        try {

            user = webClientBuilder
                    .build()
                    .get()
                    .uri("http://USER-SERVICE/api/users/email/" + email)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();

        } catch (WebClientResponseException.NotFound ex) {

            throw new UserNotFoundException("User not found");
        }

        List<Order> orders = orderRepository.findByUserId(user.getId());

        List<OrderResponse> responseList = new ArrayList<>();

        for (Order order : orders) {

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

    private void decreaseProductStock(Long productId,
                                      Integer quantity){

        try{

            webClientBuilder
                    .build()
                    .patch()
                    .uri("http://PRODUCT-SERVICE/api/products/"
                            + productId
                            + "/decrease-stock?quantity="
                            + quantity)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

        }catch(Exception ex){

            throw new RuntimeException(
                    "Unable to place order : Product out of stock");
        }

    }

    public List<OrderResponse> getShopkeeperOrders(String email) {

        // Step 1 : Get shopkeeper details
        UserResponse user = webClientBuilder
                .build()
                .get()
                .uri("http://USER-SERVICE/api/users/email/" + email)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();

        // Step 2 : Get all products of this shopkeeper
        ProductResponse[] products = webClientBuilder
                .build()
                .get()
                .uri("http://PRODUCT-SERVICE/api/products/shopkeeper/"
                        + user.getId())
                .retrieve()
                .bodyToMono(ProductResponse[].class)
                .block();

        List<Long> productIds = new ArrayList<>();

        if (products != null) {
            for (ProductResponse p : products) {
                productIds.add(p.getId());
            }
        }

        if (productIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Step 3 : Find orders
        List<Order> orders =
                orderRepository.findByProductIdIn(productIds);

        return convertToResponse(orders);
    }

    private List<OrderResponse> convertToResponse(List<Order> orders){

        List<OrderResponse> responses = new ArrayList<>();

        for(Order order : orders){

            OrderResponse response = new OrderResponse();

            response.setOrderId(order.getOrderId());
            response.setUserId(order.getUserId());
            response.setProductId(order.getProductId());
            response.setQuantity(order.getQuantity());
            response.setTotalPrice(order.getTotalPrice());
            response.setStatus(order.getStatus());
            response.setCreatedAt(order.getCreatedAt());
            response.setUpdatedAt(order.getUpdatedAt());

            responses.add(response);
        }

        return responses;
    }

    public AdminDashboardResponse getDashboard() {

        AdminDashboardResponse response = new AdminDashboardResponse();

        response.setTotalOrders(orderRepository.count());

        response.setPendingOrders(
                orderRepository.countByStatus(OrderStatus.PENDING));

        response.setConfirmedOrders(
                orderRepository.countByStatus(OrderStatus.CONFIRMED));

        response.setShippedOrders(
                orderRepository.countByStatus(OrderStatus.SHIPPED));

        response.setDeliveredOrders(
                orderRepository.countByStatus(OrderStatus.DELIVERED));

        response.setCancelledOrders(
                orderRepository.countByStatus(OrderStatus.CANCELLED));

        response.setTotalRevenue(
                orderRepository.getTotalRevenue());

        return response;
    }

}