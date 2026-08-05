package com.app.ecommerce.controller;

import com.app.ecommerce.dto.orders.OrderResponse;
import com.app.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestParam Long userId) {

        return ResponseEntity.ok(orderService.getOrders(userId));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestParam Long userId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(userId));
    }
}
