package com.app.ecommerce.dto.orders;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemDTO(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {
}
