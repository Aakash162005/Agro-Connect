package com.agro.userservice.dto;

import lombok.Data;

@Data
public class DashboardUserResponse {

    private long totalUsers;
    private long totalCustomers;
    private long totalShopkeepers;
    private long totalAdmins;

}