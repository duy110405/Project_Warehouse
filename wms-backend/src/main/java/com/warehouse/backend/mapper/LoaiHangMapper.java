package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.LoaiHangRequest;
import com.warehouse.backend.dto.response.LoaiHangResponse;
import com.warehouse.backend.entity.danhmuc.LoaiHang;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LoaiHangMapper {

    // Chuyển từ entity sang response
    LoaiHangResponse toLoaiHangResponse(LoaiHang loaiHang);

    // request sang entity
    LoaiHang toLoaiHangEntity (LoaiHangRequest loaiHangRequest);

    // Hàm update
    void updateLoaiHangFromRequest(LoaiHangRequest loaiHangRequest , @MappingTarget LoaiHang loaiHang);
}
