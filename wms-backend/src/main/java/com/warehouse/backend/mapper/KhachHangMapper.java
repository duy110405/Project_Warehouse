package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.KhachHangRequest;
import com.warehouse.backend.dto.response.KhachHangResponse;
import com.warehouse.backend.entity.danhmuc.KhachHang;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface KhachHangMapper {

    // 1. Chuyển từ Entity -> Response (Để trả về Controller)
    KhachHangResponse toKhachHangResponse(KhachHang khachHang);

    // 2. Chuyển từ Request -> Entity (Để lưu mới vào DB)
    KhachHang toKhachHangEntity(KhachHangRequest khachHangRequest);

    // 3. Hàm siêu việt của MapStruct: Đổ dữ liệu từ Request thẳng vào Entity cũ (Dùng cho Update)
    void updateKhachHangFromRequest(KhachHangRequest khachHangRequest, @MappingTarget KhachHang khachHang);
}
