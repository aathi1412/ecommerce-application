package com.app.ecommerce.service;

import com.app.ecommerce.dto.user.*;
import com.app.ecommerce.exceptions.UserNotFoundException;
import com.app.ecommerce.models.Address;
import com.app.ecommerce.models.User;
import com.app.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

    public UserResponse getUserResponse(Long id){
        User user = getUserById(id);

        return mapUserToUserResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request){
        User user = mapRequestToUser(request);
        User savedUser = userRepository.save(user);
        return mapUserToUserResponse(savedUser);
    }

    @Transactional
    public UserResponse  updateUser(Long id, UpdateUserRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());

        Address address = user.getAddress();
        address.setStreet(request.address().street());
        address.setCity(request.address().city());
        address.setState(request.address().state());
        address.setCountry(request.address().country());
        address.setZipcode(request.address().zipcode());

        return mapUserToUserResponse(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for id {} while updating cart", userId);
                    return new UserNotFoundException("User not found");
                });
    }

    public UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .role(user.getRole())
                        .address(AddressDTO.builder()
                                    .street(user.getAddress().getStreet())
                                    .city(user.getAddress().getCity())
                                    .state(user.getAddress().getState())
                                    .country(user.getAddress().getCountry())
                                    .zipcode(user.getAddress().getZipcode())
                                    .build())
                        .build();
    }


    public User mapRequestToUser(CreateUserRequest request){
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .phone(request.phone())
                .build();

        Address address = Address.builder()
                    .street(request.address().street())
                    .city(request.address().city())
                    .state(request.address().state())
                    .country(request.address().country())
                    .zipcode(request.address().zipcode())
                    .build();

        user.setAddress(address);
        return  user;
    }
}
