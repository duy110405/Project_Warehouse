package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.RoleRequest;
import com.warehouse.backend.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    /**
     * Sinh mã ID tự động: ROLE01, ROLE02, ...
     */
    String generateNextRoleId();
    
    /**
     * Lấy tất cả nhóm
     */
    List<RoleResponse> getAllRoles();
    
    /**
     * Lấy nhóm theo ID
     */
    RoleResponse getRoleById(String groupId);
    
    /**
     * Tạo nhóm mới
     */
    RoleResponse createRole(RoleRequest roleRequest);
    
    /**
     * Cập nhật nhóm
     */
    RoleResponse updateRole(String groupId, RoleRequest roleRequest);
    
    /**
     * Xóa nhóm
     */
    void deleteRole(String groupId);
}

