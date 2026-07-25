package com.app.ecommerce.controller;

import com.app.ecommerce.dto.orders.OrderResponse;
import com.app.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrders(@PathVariable Long userId) {
        List<OrderResponse> response = orderService.getOrders(userId);
        return ResponseEntity
                .ok(response);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<OrderResponse> createOrders(@PathVariable Long userId) {
        OrderResponse response = orderService.createOrder(userId);
        return ResponseEntity
                .ok(response);
    }
}
