package com.agro.orderservice.dto;

import com.agro.orderservice.model.OrderStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class UpdateOrderStatusRequest {

    @Autowired
    private OrderStatus status;
}
