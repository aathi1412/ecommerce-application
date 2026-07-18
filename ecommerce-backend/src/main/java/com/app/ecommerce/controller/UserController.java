package com.app.ecommerce.controller;

import com.app.ecommerce.dto.user.UserRequest;
import com.app.ecommerce.dto.user.UserResponse;
import com.app.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email){
        UserResponse response=userService.getUser(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRequest request){
        String response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest request){
        String response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }

}
