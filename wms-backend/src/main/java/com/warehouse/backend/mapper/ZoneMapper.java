package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.ZoneRequest;
import com.warehouse.backend.dto.response.ZoneRespone;
import com.warehouse.backend.entity.danhmuc.Zone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    // entity sang response
    ZoneRespone toZoneRespone(Zone zone);

    // request sang entity
    Zone toZoneEntity (ZoneRequest zoneRequest);

    // Ham update
    void updateFromRequest(ZoneRequest zoneRequest, @org.mapstruct.MappingTarget Zone zone);
}
