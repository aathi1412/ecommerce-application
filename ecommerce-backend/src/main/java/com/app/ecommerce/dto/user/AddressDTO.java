package com.app.ecommerce.dto.user;

import lombok.Builder;

@Builder
public record AddressDTO(
        String street,
        String city,
        String state,
        String country,
        String zipcode
) {}
