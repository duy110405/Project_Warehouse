package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.UserRequest;
import com.warehouse.backend.dto.response.UserResponse;
import com.warehouse.backend.entity.hethong.Role;
import com.warehouse.backend.entity.hethong.User;
import com.warehouse.backend.mapper.SystemMapper;
import com.warehouse.backend.repository.hethong.RoleRepository;
import com.warehouse.backend.repository.hethong.UserRepository;
import com.warehouse.backend.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SystemMapper systemMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, SystemMapper systemMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.systemMapper = systemMapper;
    }

    @Override
    public String generateNextUserId() {
        String maxId = userRepository.findMaxUserId();
        if (maxId == null) {
            return "ND001";
        }
        // Cắt "ND" từ vị trí 0-2, lấy số từ vị trí 2 trở đi
        int nextNumber = Integer.parseInt(maxId.substring(2)) + 1;
        return String.format("ND%03d", nextNumber);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(systemMapper::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
        return systemMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse saveUser(UserRequest userRequest) {
        // 1. Fail-Fast: Kiểm tra tên đăng nhập không trùng
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Tên đăng nhập '" + userRequest.getUsername() + "' đã tồn tại!");
        }

        // 2. Tìm Role từ database
        Role role = roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm người dùng với ID: " + userRequest.getRoleId()));

        // 3. Map từ request thành entity
        User user = systemMapper.toUserEntity(userRequest);

        // 4. Tự sinh ID
        String newUserId = generateNextUserId();
        user.setUserId(newUserId);

        // 5. Gán Role
        user.setRole(role);

        // 6. Lưu user
        User savedUser = userRepository.save(user);

        // 7. Trả về response (password sẽ bị ignore bởi mapper)
        return systemMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(String userId, UserRequest userRequest) {
        // 1. Tìm user cũ
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        // 2. Tìm Role từ database
        Role role = roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm người dùng với ID: " + userRequest.getRoleId()));

        // 3. Cập nhật thông tin từ request
        // Lưu password cũ tạm thời
        String oldPassword = existingUser.getPassword();

        systemMapper.updateUserFromRequest(userRequest, existingUser);

        // 4. Xử lý password:
        // Nếu request gửi password mới (không null/rỗng) -> cập nhật
        // Nếu không -> giữ nguyên password cũ
        if (userRequest.getPassword() == null || userRequest.getPassword().isBlank()) {
            existingUser.setPassword(oldPassword);
        }

        // 5. Cập nhật Role
        existingUser.setRole(role);

        // 6. Lưu user đã cập nhật
        User updatedUser = userRepository.save(existingUser);

        // 7. Trả về response
        return systemMapper.toUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
        userRepository.delete(user);
    }
}

