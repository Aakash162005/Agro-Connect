package com.agro.orderservice.service.client;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private String uId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
