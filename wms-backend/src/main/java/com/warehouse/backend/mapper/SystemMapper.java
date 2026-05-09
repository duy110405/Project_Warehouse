package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.RoleRequest;
import com.warehouse.backend.dto.request.UserRequest;
import com.warehouse.backend.dto.response.RoleResponse;
import com.warehouse.backend.dto.response.UserResponse;
import com.warehouse.backend.entity.hethong.Role;
import com.warehouse.backend.entity.hethong.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SystemMapper {

    // ========== ROLE MAPPINGS ==========

    /**
     * Chuyển đổi Role Entity -> RoleResponse
     */
    RoleResponse toRoleResponse(Role role);

    /**
     * Chuyển đổi RoleRequest -> Role Entity
     * Map thủ công: groupName và role từ request
     */
    @Mapping(target = "groupId", ignore = true)
    Role toRoleEntity(RoleRequest roleRequest);

    /**
     * Cập nhật Role từ RoleRequest (không cập nhật groupId)
     */
    @Mapping(target = "groupId", ignore = true)
    void updateRoleFromRequest(RoleRequest roleRequest, @MappingTarget Role role);

    // ========== USER MAPPINGS ==========

    /**
     * Chuyển đổi User Entity -> UserResponse
     * BẮT BUỘC: Không map password vào response (để bảo mật)
     */
    @Mapping(target = "roleId", source = "role.groupId")
    @Mapping(target = "roleName", source = "role.groupName")
    UserResponse toUserResponse(User user);

    /**
     * Chuyển đổi UserRequest -> User Entity
     * Không map roleId ở đây (phải tìm Role từ DB rồi gán)
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUserEntity(UserRequest userRequest);

    /**
     * Cập nhật User từ UserRequest
     * nullValuePropertyMappingStrategy: Không ghi đè giá trị null
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UserRequest userRequest, @MappingTarget User user);
}

