package com.agro.orderservice.dto;

import lombok.Data;

@Data
public class OrderRequest {

    private String userId;
    private Long productId;
    private Integer quantity;

}
