package com.app.ecommerce.dto.orders;

import com.app.ecommerce.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record OrderResponse(
        Long id,
        BigDecimal totalAmount,
        OrderStatus status,
        List<OrderItemDTO> OrderItems,
        Instant createdAt
) {}
