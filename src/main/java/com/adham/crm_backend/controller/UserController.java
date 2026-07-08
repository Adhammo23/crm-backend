package com.adham.crm_backend.controller;

import com.adham.crm_backend.dto.CreateUserRequest;
import com.adham.crm_backend.dto.UserResponse;
import com.adham.crm_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }
    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> createUser (@RequestBody @Valid CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }
}
