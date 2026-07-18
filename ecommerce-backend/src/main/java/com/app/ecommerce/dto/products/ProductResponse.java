package com.app.ecommerce.dto.products;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String category,
        String imageUrl,
        Boolean active
) {
}
