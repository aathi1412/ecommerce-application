package com.app.ecommerce.controller;


import com.app.ecommerce.dto.ApiResponse;
import com.app.ecommerce.dto.cart.CartItemRequest;
import com.app.ecommerce.dto.cart.CartItemResponse;
import com.app.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getAllCartItems(@RequestParam Long userId){
        List<CartItemResponse> cartItems = cartService.getCartItemResponse(userId);
        return ResponseEntity
                .ok(cartItems);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@RequestParam Long userId,
                                          @RequestBody CartItemRequest request) {
        ApiResponse response = cartService.addToCart(userId, request);
        return  ResponseEntity
                .ok(response);
    }

    @PutMapping("/items")
    public ResponseEntity<ApiResponse> updateCartQuantity(@RequestParam Long userId,
                                          @RequestBody CartItemRequest request) {
        ApiResponse response = cartService.updateCartItemQuantity(userId, request);
        return  ResponseEntity
                .ok(response);
    }

    @DeleteMapping("/items")
    public ResponseEntity<ApiResponse> removeFromCart(@RequestParam Long userId,
                                                      @RequestParam Long productId) {
        ApiResponse response = cartService.removeFromCart(userId, productId);

        return  ResponseEntity
                .ok(response);
    }
}
