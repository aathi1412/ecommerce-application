package com.app.ecommerce.dto.cart;

import com.app.ecommerce.dto.products.ProductResponse;
import com.app.ecommerce.dto.user.UserResponse;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemResponse(
        ProductResponse product,
        UserResponse user,
        Integer quantity,
        BigDecimal price
) {
}
