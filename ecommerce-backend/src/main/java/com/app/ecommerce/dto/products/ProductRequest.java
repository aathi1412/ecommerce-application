package com.app.ecommerce.dto.products;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record ProductRequest(
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String category,
        String imageUrl
) {}
