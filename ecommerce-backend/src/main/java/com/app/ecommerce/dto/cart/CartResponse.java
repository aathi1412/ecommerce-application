package com.app.ecommerce.dto.cart;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record CartResponse(
        Long cartId,

        List<CartItemResponse> cartItems,

        BigDecimal totalPrice,

        Integer totalQuantity,
        Integer totalProducts,

        Instant createdAt
) {
}
