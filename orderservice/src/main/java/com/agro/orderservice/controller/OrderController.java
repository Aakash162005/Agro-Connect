package com.agro.orderservice.controller;

import com.agro.orderservice.dto.OrderRequest;
import com.agro.orderservice.dto.OrderResponse;
import com.agro.orderservice.dto.UpdateOrderStatusRequest;
import com.agro.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders()
    {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable  Long id)
    {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest request)
    {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {

        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}
