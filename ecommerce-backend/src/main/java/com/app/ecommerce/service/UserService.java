package com.app.ecommerce.service;

import com.app.ecommerce.dto.user.UserResponse;
import com.app.ecommerce.exceptions.UserNotFoundException;
import com.app.ecommerce.models.Address;
import com.app.ecommerce.models.User;
import com.app.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getAllUser(){
        return userRepository.findAll()
                .stream()
                .map(this::mapUserToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUser(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        return mapUserToUserResponse(user);
    }

    public UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(Address.builder()
                        .street(user.getAddress().getStreet())
                        .city(user.getAddress().getCity())
                        .state(user.getAddress().getState())
                        .country(user.getAddress().getCountry())
                        .zipcode(user.getAddress().getZipcode())
                        .build()
                )
                .build();
    }
}
