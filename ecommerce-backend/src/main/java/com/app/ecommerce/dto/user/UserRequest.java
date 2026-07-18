package com.app.ecommerce.dto.user;

import com.app.ecommerce.models.Address;


public record UserRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String phone,
        Address address
) {}
