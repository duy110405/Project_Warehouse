package com.warehouse.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Đánh dấu đây là file cấu hình của Spring Boot
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng CORS cho TẤT CẢ các đường dẫn API
                .allowedOrigins("http://localhost:3000", "http://localhost:5173") // Chỉ định đích danh port của React (CRA hoặc Vite)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các method này
                .allowedHeaders("*") // Cho phép tất cả các header
                .allowCredentials(true); // Rất quan trọng nếu sau này bạn làm tính năng đăng nhập dùng Cookie/Session
    }
}