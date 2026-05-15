package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.LoginRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginRequest request) {
        // Xác thực username và password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Lưu vào Context (tùy chọn)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Sinh Token
        String jwt = jwtUtils.generateToken(authentication);

        // Trả về Token cho Frontend
        Map<String, String> data = new HashMap<>();
        data.put("token", jwt);
        data.put("username", authentication.getName());
        // Có thể trả thêm Role ở đây để React tiện phân quyền giao diện

        return ApiResponse.<Map<String, String>>builder()
                .code(200)
                .message("Đăng nhập thành công")
                .data(data)
                .build();
    }
}