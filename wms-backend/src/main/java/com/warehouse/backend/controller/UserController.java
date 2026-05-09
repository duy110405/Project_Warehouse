package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.UserRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.UserResponse;
import com.warehouse.backend.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Lấy danh sách người dùng thành công!")
                .data(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Tìm thấy người dùng")
                .data(userService.getUserById(userId))
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return ApiResponse.<UserResponse>builder()
                .code(201)
                .message("Tạo người dùng thành công!")
                .data(userService.saveUser(userRequest))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRequest userRequest) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Cập nhật người dùng thành công!")
                .data(userService.updateUser(userId, userRequest))
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa người dùng thành công!")
                .build();
    }
}


