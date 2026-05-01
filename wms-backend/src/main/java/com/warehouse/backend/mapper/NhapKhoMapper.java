package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.NhapKhoRequest;
import com.warehouse.backend.dto.response.NhapKhoResponse;
import com.warehouse.backend.entity.nghiepvu.PhieuNhap;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NhapKhoMapper {
    // entity sang respone
    NhapKhoResponse toNhapKhoResponse(PhieuNhap phieuNhap);

    // request sang entity
    PhieuNhap toPhieuNhapEntity (NhapKhoRequest nhapKhoRequest);

    // dùng cho hàm update
    void updateNhapKhoFromRequest (NhapKhoRequest nhapKhoRequest , @MappingTarget PhieuNhap phieuNhap);

}
