package com.agro.api_gateway.dto;

import lombok.Data;

@Data
public class OrderDashboardResponse {

    private Long totalOrders;
    private Long pendingOrders;
    private Long confirmedOrders;
    private Long shippedOrders;
    private Long deliveredOrders;
    private Long cancelledOrders;

    private Double totalRevenue;

}