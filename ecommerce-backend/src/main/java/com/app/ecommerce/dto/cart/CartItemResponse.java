package com.app.ecommerce.dto.cart;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemResponse(
        Long cartItemId,

        Long productId,

        String productName,

        String productImage,

        BigDecimal price,

        Integer quantity,

        BigDecimal totalPrice
) {}
