package com.example.note.controller;

import com.example.note.dto.UserModel;
import com.example.note.response.ResponseApi;
import com.example.note.response.ResponseCode;
import com.example.note.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all users", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)))
    @GetMapping
    public ResponseApi<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return new ResponseApi<>(users, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseApi<UserModel> getUserById(@Parameter(description = "The ID of the user to retrieve") @PathVariable Long id) {
        UserModel userModel = userService.getUser(id);
        return new ResponseApi<>(userModel, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Create a new user", description = "Create a new user with the provided user model")
    @ApiResponse(responseCode = "201", description = "Successfully created user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)))
    @PostMapping
    public ResponseApi<UserModel> createUser(@RequestBody @Valid UserModel userModel) {
        UserModel createdUser = userService.createUser(userModel);
        return new ResponseApi<>(createdUser, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Update a user", description = "Update an existing user with the provided user model")
    @ApiResponse(responseCode = "200", description = "Successfully updated user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseApi<UserModel> updateUser(@PathVariable Long id, @RequestBody @Valid UserModel userModel) {
        UserModel updatedUser = userService.updateUser(id, userModel);
        return new ResponseApi<>(updatedUser, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Delete a user", description = "Delete a user by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully deleted user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseApi<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}
