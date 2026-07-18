package com.adham.crm_backend.controller;

import com.adham.crm_backend.documentation.annotation.CreateApiResponses;
import com.adham.crm_backend.documentation.annotation.GetApiResponses;
import com.adham.crm_backend.documentation.annotation.UpdateApiResponses;
import com.adham.crm_backend.dto.CreateUserRequest;
import com.adham.crm_backend.dto.UpdateUserRequest;
import com.adham.crm_backend.dto.UserResponse;
import com.adham.crm_backend.exception.ErrorResponse;
import com.adham.crm_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Return the authenticated user's profile")
    @GetApiResponses
    public ResponseEntity<UserResponse> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "List users", description = "Return a paginated list of users for admin users")
   @GetApiResponses
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @ParameterObject @PageableDefault(sort = "createdAt") Pageable pageable){
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Create user", description = "Create a new user with roles and optional team assignment")
    @CreateApiResponses
    public ResponseEntity<UserResponse> createUser (@RequestBody @Valid CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Return a single user by id")
    @GetApiResponses
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Update user", description = "Update user profile and roles")
    @UpdateApiResponses
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request){
        return ResponseEntity.ok(userService.updateUser(id,request));
    }
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Activate user", description = "Activate a user account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User activated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserResponse> activateUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.activateUser(id));
    }
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Deactivate user", description = "Deactivate a user account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deactivated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.deactivateUser(id));
    }

}
