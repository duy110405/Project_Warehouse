package com.warehouse.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
     int code;
     String message;
     T data; // Dùng Generic <T> để chứa bất kỳ loại dữ liệu nào (KhachHang, HoaDon...)
}
