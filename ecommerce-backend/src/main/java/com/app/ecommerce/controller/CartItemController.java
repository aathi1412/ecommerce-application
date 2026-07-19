package com.app.ecommerce.controller;


import com.app.ecommerce.dto.ApiResponse;
import com.app.ecommerce.dto.cart.CartItemRequest;
import com.app.ecommerce.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping("/items/{userId}")
    public ResponseEntity<ApiResponse> addToCart(@PathVariable Long userId,
                                          @RequestBody CartItemRequest request) {
        ApiResponse response = cartItemService.addToCart(userId, request);
        return  ResponseEntity
                .ok(response);
    }

    @PutMapping("/items/{userId}")
    public ResponseEntity<ApiResponse> updateCartQuantity(@PathVariable Long userId,
                                          @RequestBody CartItemRequest request) {
        ApiResponse response = cartItemService.updateCartItemQuantity(userId, request);
        return  ResponseEntity
                .ok(response);
    }
}
