package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.NguyenLieuRequest;
import com.warehouse.backend.dto.response.NguyenLieuResponse;
import com.warehouse.backend.entity.danhmuc.NguyenLieu;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NguyenLieuMapper {

    NguyenLieuResponse toNguyenLieuResponse(NguyenLieu nguyenLieu);

    NguyenLieu toNguyenLieuEntity(NguyenLieuRequest nguyenLieuRequest) ;

    void updateNguyenLieuFromRequset(NguyenLieuRequest nguyenLieuRequest , @MappingTarget NguyenLieu nguyenLieu);
}
