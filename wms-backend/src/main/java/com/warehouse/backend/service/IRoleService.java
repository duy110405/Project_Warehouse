package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.RoleRequest;
import com.warehouse.backend.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    String generateNextRoleId();

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleById(String groupId);

    RoleResponse saveRole(RoleRequest roleRequest);

    RoleResponse updateRole(String groupId, RoleRequest roleRequest);

    void deleteRole(String groupId);
}

