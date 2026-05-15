package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.RoleRequest;
import com.warehouse.backend.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    String generateNextRoleId();

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleById(String roleId);

    RoleResponse saveRole(RoleRequest roleRequest);

    RoleResponse updateRole(String roleId, RoleRequest roleRequest);

    void deleteRole(String roleId);
}

