package com.app.ecommerce.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email
        @NotBlank
        String email,

        @Size(min = 8)
        String password,

        @Pattern(regexp = "\\d{10}")
        String phone,

        @Valid
        AddressDTO address
) {}
