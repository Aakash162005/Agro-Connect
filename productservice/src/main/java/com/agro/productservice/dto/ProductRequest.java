package com.agro.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required.")
    private String name;
    private String description;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be greater then 0.")
    private Double price;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be greater then 0.")
    private Integer quantity;
    private String category;
    private String imageUrl;
    private String shopkeeperId;
}