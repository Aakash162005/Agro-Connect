package com.agro.orderservice.dto;

import lombok.Data;

@Data
public class AdminDashboardResponse {

    private long totalOrders;

    private long pendingOrders;

    private long confirmedOrders;

    private long shippedOrders;

    private long deliveredOrders;

    private long cancelledOrders;

    private Double totalRevenue;

}