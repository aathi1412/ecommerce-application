package com.app.ecommerce.dto.cart;


public record CartRequest(
        Long productId,
        Integer quantity
) {}
