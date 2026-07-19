package com.agro.orderservice.dto;

import com.agro.orderservice.model.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponse {

    private Long orderId;
    private String userId;
    private Long productId;
    private Integer quantity;
    private Double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
