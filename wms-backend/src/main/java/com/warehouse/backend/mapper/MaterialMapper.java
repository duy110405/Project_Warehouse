package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.MaterialRequest;
import com.warehouse.backend.dto.response.MaterialResponse;
import com.warehouse.backend.entity.danhmuc.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

    @Mapping(target = "zoneId", source = "zone.zoneId")
    @Mapping(target = "zoneName", source = "zone.zoneName")
    MaterialResponse toMaterialResponse(Material material);

    @Mapping(target = "zone" , ignore = true)
    Material toMaterialEntity(MaterialRequest materialRequest) ;

    void updateMaterialFromRequset(MaterialRequest materialRequest, @MappingTarget Material material);
}
