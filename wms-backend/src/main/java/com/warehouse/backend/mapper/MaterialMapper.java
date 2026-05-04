package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.MaterialRequest;
import com.warehouse.backend.dto.response.MaterialResponse;
import com.warehouse.backend.entity.danhmuc.Material;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

    MaterialResponse toMaterialResponse(Material material);

    Material toMaterialEntity(MaterialRequest materialRequest) ;

    void updateMaterialFromRequset(MaterialRequest materialRequest, @MappingTarget Material material);
}
