package com.app.ecommerce.dto.user;

import com.app.ecommerce.enums.Role;
import com.app.ecommerce.models.Address;
import lombok.Builder;

@Builder
public record UserResponse(
        String firstName,
        String lastName,
        String email,
        String phone,
        Role role,
        Address address
) {}
