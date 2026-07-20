package com.app.ecommerce.controller;

import com.app.ecommerce.dto.orders.OrderRequest;
import com.app.ecommerce.dto.orders.OrderResponse;
import com.app.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<OrderResponse> getOrders() {
        return ResponseEntity
                .ok(OrderResponse.builder().build());
    }

    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponse> createOrders(@PathVariable Long userId) {
        OrderResponse response = orderService.createOrder(userId);
        return ResponseEntity
                .ok(response);
    }
}
