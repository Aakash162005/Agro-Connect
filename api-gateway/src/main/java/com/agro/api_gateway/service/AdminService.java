package com.agro.api_gateway.service;

import com.agro.api_gateway.dto.AdminDashboardResponse;
import com.agro.api_gateway.dto.OrderDashboardResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class AdminService {

    private final WebClient.Builder webClientBuilder;

    public AdminDashboardResponse dashboard(){

        AdminDashboardResponse response =
                new AdminDashboardResponse();

        Long totalUsers =
                webClientBuilder.build()
                        .get()
                        .uri("http://USER-SERVICE/api/users/count")
                        .retrieve()
                        .bodyToMono(Long.class)
                        .block();

        Long totalProducts =
                webClientBuilder.build()
                        .get()
                        .uri("http://PRODUCT-SERVICE/api/products/count")
                        .retrieve()
                        .bodyToMono(Long.class)
                        .block();

        OrderDashboardResponse order =
                webClientBuilder.build()
                        .get()
                        .uri("http://ORDER-SERVICE/api/orders/dashboard")
                        .retrieve()
                        .bodyToMono(OrderDashboardResponse.class)
                        .block();

        response.setTotalUsers(totalUsers);
        response.setTotalProducts(totalProducts);

        response.setTotalOrders(order.getTotalOrders());
        response.setPendingOrders(order.getPendingOrders());
        response.setConfirmedOrders(order.getConfirmedOrders());
        response.setShippedOrders(order.getShippedOrders());
        response.setDeliveredOrders(order.getDeliveredOrders());
        response.setCancelledOrders(order.getCancelledOrders());
        response.setTotalRevenue(order.getTotalRevenue());

        return response;

    }

}