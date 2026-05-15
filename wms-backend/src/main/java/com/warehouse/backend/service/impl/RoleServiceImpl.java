package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.RoleRequest;
import com.warehouse.backend.dto.response.RoleResponse;
import com.warehouse.backend.entity.hethong.Role;
import com.warehouse.backend.mapper.SystemMapper;
import com.warehouse.backend.repository.RoleRepository;
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

    @Override
    public String generateNextRoleId() {
        String maxId = roleRepository.findMaxRoleId();
        if (maxId == null) {
            return "ROLE01";
        }
        // Cắt "ROLE" từ vị trí 0-4, lấy số từ vị trí 4 trở đi
        int nextNumber = Integer.parseInt(maxId.substring(4)) + 1;
        return String.format("ROLE%02d", nextNumber);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(systemMapper::toRoleResponse)
                .toList();
    }
    @Override
    public RoleResponse getRoleById(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm người dùng với ID: " + roleId));
        return systemMapper.toRoleResponse(role);
    }

    @Override
    @Transactional
    public RoleResponse saveRole(RoleRequest roleRequest) {
        // Tự sinh ID
        String newRoleId = generateNextRoleId();

        // Map từ request
        Role role = systemMapper.toRoleEntity(roleRequest);
        role.setRoleId(newRoleId);

        // Lưu và trả về response
        Role savedRole = roleRepository.save(role);
        return systemMapper.toRoleResponse(savedRole);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(String roleId, RoleRequest roleRequest) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm người dùng với ID: " + roleId));
        // Cập nhật từ request (roleId không bị thay đổi)
        systemMapper.updateRoleFromRequest(roleRequest, existingRole);
        // Lưu và trả về response
        Role updatedRole = roleRepository.save(existingRole);
        return systemMapper.toRoleResponse(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm người dùng với ID: " + roleId));
        roleRepository.delete(role);
    }
}

