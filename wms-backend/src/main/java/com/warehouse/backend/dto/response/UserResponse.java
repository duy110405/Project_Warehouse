package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userId;
    String fullName;
    String phoneNumber;
    String username;
    String position;
    String roleId;
    String roleName;
    // BẮT BUỘC KHÔNG trả về password
}

