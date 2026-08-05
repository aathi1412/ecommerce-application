package com.app.ecommerce.controller;

import com.app.ecommerce.dto.user.CreateUserRequest;
import com.app.ecommerce.dto.user.UpdateUserRequest;
import com.app.ecommerce.dto.user.UserResponse;
import com.app.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUser();
        return ResponseEntity
                .ok()
                .body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
        UserResponse response = userService.getUserResponse(id);
        return ResponseEntity
                .ok()
                .body(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request){
        UserResponse response = userService.createUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request){
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity
                .ok()
                .body(response);
    }

}
