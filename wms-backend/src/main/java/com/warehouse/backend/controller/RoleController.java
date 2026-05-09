package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.RoleRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.RoleResponse;
import com.warehouse.backend.service.IRoleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin("*") // Cho phép React/Frontend gọi API
public class RoleController {
    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * GET: Lấy danh sách tất cả nhóm
     */
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(200)
                .message("Lấy danh sách nhóm thành công!")
                .data(roleService.getAllRoles())
                .build();
    }

    /**
     * GET: Lấy nhóm theo ID
     */
    @GetMapping("/{groupId}")
    public ApiResponse<RoleResponse> getRoleById(@PathVariable String groupId) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Tìm thấy nhóm")
                .data(roleService.getRoleById(groupId))
                .build();
    }

    /**
     * POST: Tạo nhóm mới
     * @Valid kích hoạt validation các annotation @NotBlank, @Size, ...
     */
    @PostMapping
    public ApiResponse<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .code(201)
                .message("Tạo nhóm thành công!")
                .data(roleService.createRole(roleRequest))
                .build();
    }

    /**
     * PUT: Cập nhật nhóm
     * @Valid kích hoạt validation
     */
    @PutMapping("/{groupId}")
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable String groupId,
            @Valid @RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Cập nhật nhóm thành công!")
                .data(roleService.updateRole(groupId, roleRequest))
                .build();
    }

    /**
     * DELETE: Xóa nhóm
     */
    @DeleteMapping("/{groupId}")
    public ApiResponse<Void> deleteRole(@PathVariable String groupId) {
        roleService.deleteRole(groupId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xóa nhóm thành công!")
                .build();
    }
}

