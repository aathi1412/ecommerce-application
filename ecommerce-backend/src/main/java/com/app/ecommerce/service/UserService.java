package com.app.ecommerce.service;

import com.app.ecommerce.dto.user.AddressDTO;
import com.app.ecommerce.dto.user.UserRequest;
import com.app.ecommerce.dto.user.UserResponse;
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

    public UserResponse getUser(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        return mapUserToUserResponse(user);
    }

    public String createUser(UserRequest request){
        User user = mapRequestToUser(request);
        userRepository.save(user);
        return "User Created  Successfully";
    }

    @Transactional
    public String  updateUser(UserRequest request){
        mapRequestToUser(request);
        return "User Updated Successfully";
    }

    public User getUser(Long userId) {
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


    public User mapRequestToUser(UserRequest request){
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .phone(request.phone())
                .build();

        Address address = Address.builder()
                    .street(request.address().getStreet())
                    .city(request.address().getCity())
                    .state(request.address().getState())
                    .country(request.address().getCountry())
                    .zipcode(request.address().getZipcode())
                    .build();

        user.setAddress(address);
        return  user;
    }
}
