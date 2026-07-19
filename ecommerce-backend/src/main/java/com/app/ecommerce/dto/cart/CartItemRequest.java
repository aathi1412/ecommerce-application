package com.app.ecommerce.dto.cart;


public record CartItemRequest(
        Long productId,
        Integer quantity
) {}
