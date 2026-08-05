package com.app.ecommerce.controller;


import com.app.ecommerce.dto.ApiResponse;
import com.app.ecommerce.dto.cart.CartRequest;
import com.app.ecommerce.dto.cart.CartResponse;
import com.app.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartResponse>> getAllCartItems(@RequestParam Long userId){
        return ResponseEntity
                .ok(cartService.getCartResponse(userId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@RequestParam Long userId,
                                          @Valid @RequestBody CartRequest request) {
        ApiResponse response = cartService.addToCart(userId, request);
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/items/{userId}")
    public ResponseEntity<ApiResponse> updateCartQuantity(@PathVariable Long userId,
                                          @Valid @RequestBody CartRequest request) {
        return  ResponseEntity
                .ok(cartService.updateCartItemQuantity(userId, request));
    }

    @DeleteMapping("/items/{userId}")
    public ResponseEntity<ApiResponse> removeProductFromCart(@PathVariable Long userId,
                                                      @RequestParam Long productId) {
        ApiResponse response = cartService.removeProductFromCart(userId, productId);

        return  ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }
}
