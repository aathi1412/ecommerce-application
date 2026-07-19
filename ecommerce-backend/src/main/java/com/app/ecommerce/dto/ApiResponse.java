package com.app.ecommerce.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ApiResponse(
        Instant timeStamp,
        int status,
        String message
) {
}
