package com.app.ecommerce.controller;


import com.app.ecommerce.dto.ApiResponse;
import com.app.ecommerce.dto.cart.CartItemRequest;
import com.app.ecommerce.dto.cart.CartItemResponse;
import com.app.ecommerce.models.CartItem;
import com.app.ecommerce.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getAllCartItems(@RequestParam Long userId){
        List<CartItemResponse> cartItems = cartItemService.getAllCartItems(userId);
        return ResponseEntity
                .ok(cartItems);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@RequestParam Long userId,
                                          @RequestBody CartItemRequest request) {
        ApiResponse response = cartItemService.addToCart(userId, request);
        return  ResponseEntity
                .ok(response);
    }

    @PutMapping("/items")
    public ResponseEntity<ApiResponse> updateCartQuantity(@RequestParam Long userId,
                                          @RequestBody CartItemRequest request) {
        ApiResponse response = cartItemService.updateCartItemQuantity(userId, request);
        return  ResponseEntity
                .ok(response);
    }

    @DeleteMapping("/items")
    public ResponseEntity<ApiResponse> deleteCartItem(@RequestParam Long userId,
                                                      @RequestParam Long productId) {
        ApiResponse response = cartItemService.deleteCartItem(userId, productId);

        return  ResponseEntity
                .ok(response);
    }
}
