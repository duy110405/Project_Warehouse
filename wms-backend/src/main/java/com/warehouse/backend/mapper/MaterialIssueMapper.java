package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.MaterialIssueRequest;
import com.warehouse.backend.dto.response.MaterialIssueDetailResponse;
import com.warehouse.backend.dto.response.MaterialIssueResponse;
import com.warehouse.backend.entity.nghiepvu.MaterialIssueDetail;
import com.warehouse.backend.entity.nghiepvu.OutboundMaterialIssue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MaterialIssueMapper {

    @Mapping(target = "supplierId" , source = "supplier.supplierId")
    @Mapping(target = "supplierName" , source = "supplier.supplierName")
    @Mapping(target = "userId" , source = "user.userId")
    @Mapping(target = "fullName" , source = "user.fullName")
    MaterialIssueResponse toMaterialIssueResponse(OutboundMaterialIssue outboundMaterialIssue);

    @Mapping(target = "materialId" , source = "material.materialId")
    @Mapping(target = "materialName" , source = "material.materialName")
    @Mapping(target = "zoneId" , source = "zone.zoneId")
    @Mapping(target = "zoneName" , source = "zone.zoneName")
    @Mapping(target = "zoneType" , source = "zone.zoneType")
    MaterialIssueDetailResponse toMaterialIssueDetailResponse(MaterialIssueDetail materialIssueDetail);

    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "materialIssueDetails", ignore = true)
    OutboundMaterialIssue toOutboundMaterialIssueEntity(MaterialIssueRequest materialIssueRequest);

    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "materialIssueDetails", ignore = true)
    void updateFromRequest(MaterialIssueRequest materialIssueRequest , @MappingTarget OutboundMaterialIssue outboundMaterialIssue);
}
