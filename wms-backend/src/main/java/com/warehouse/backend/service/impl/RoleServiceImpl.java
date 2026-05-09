package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.RoleRequest;
import com.warehouse.backend.dto.response.RoleResponse;
import com.warehouse.backend.entity.hethong.Role;
import com.warehouse.backend.mapper.SystemMapper;
import com.warehouse.backend.repository.hethong.RoleRepository;
import com.warehouse.backend.service.IRoleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    private final SystemMapper systemMapper;

    public RoleServiceImpl(RoleRepository roleRepository, SystemMapper systemMapper) {
        this.roleRepository = roleRepository;
        this.systemMapper = systemMapper;
    }

    /**
     * Sinh mã ID tự động: ROLE01, ROLE02, ROLE03
     * Format: ROLE + 2 chữ số (cắt từ vị trí 4)
     */
    @Override
    public String generateNextRoleId() {
        String maxId = roleRepository.findMaxGroupId();
        if (maxId == null) {
            return "ROLE01";
        }
        // Cắt "ROLE" từ vị trí 0-4, lấy số từ vị trí 4 trở đi
        int nextNumber = Integer.parseInt(maxId.substring(4)) + 1;
        return String.format("ROLE%02d", nextNumber);
    }

    /**
     * Lấy tất cả nhóm
     */
    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(systemMapper::toRoleResponse)
                .toList();
    }

    /**
     * Lấy nhóm theo ID
     */
    @Override
    public RoleResponse getRoleById(String groupId) {
        Role role = roleRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm người dùng với ID: " + groupId));
        return systemMapper.toRoleResponse(role);
    }

    /**
     * Tạo nhóm mới
     * - Tự sinh ID
     * - Map từ request
     */
    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        // Tự sinh ID
        String newGroupId = generateNextRoleId();

        // Map từ request
        Role role = systemMapper.toRoleEntity(roleRequest);
        role.setGroupId(newGroupId);

        // Lưu và trả về response
        Role savedRole = roleRepository.save(role);
        return systemMapper.toRoleResponse(savedRole);
    }

    /**
     * Cập nhật nhóm
     * - Tìm nhóm cũ
     * - Cập nhật từ request
     * - Lưu lại
     */
    @Override
    @Transactional
    public RoleResponse updateRole(String groupId, RoleRequest roleRequest) {
        Role existingRole = roleRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm người dùng với ID: " + groupId));

        // Cập nhật từ request (groupId không bị thay đổi)
        systemMapper.updateRoleFromRequest(roleRequest, existingRole);

        // Lưu và trả về response
        Role updatedRole = roleRepository.save(existingRole);
        return systemMapper.toRoleResponse(updatedRole);
    }

    /**
     * Xóa nhóm
     */
    @Override
    @Transactional
    public void deleteRole(String groupId) {
        Role role = roleRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm người dùng với ID: " + groupId));
        roleRepository.delete(role);
    }
}

