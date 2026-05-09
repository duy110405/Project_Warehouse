package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.UserRequest;
import com.warehouse.backend.dto.response.UserResponse;

import java.util.List;

public interface IUserService {

    String generateNextUserId();

    List<UserResponse> getAllUsers();

    UserResponse getUserById(String userId);

    UserResponse saveUser(UserRequest userRequest);

    UserResponse updateUser(String userId, UserRequest userRequest);

    void deleteUser(String userId);
}

