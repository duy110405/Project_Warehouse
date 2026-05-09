package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.InboundReceiptRequest;
import com.warehouse.backend.dto.request.MaterialReceiptRequest;
import com.warehouse.backend.dto.response.InboundReceiptResponse;
import com.warehouse.backend.dto.response.MaterialReceiptDetailResponse;
import com.warehouse.backend.dto.response.MaterialReceiptResponse;
import com.warehouse.backend.entity.nghiepvu.InboundMaterialReceipt;
import com.warehouse.backend.entity.nghiepvu.InboundReceipt;
import com.warehouse.backend.entity.nghiepvu.MaterialReceiptDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MaterialReceiptMapper {

    @Mapping(target = "vendorId", source = "vendor.vendorId")
    @Mapping(target = "vendorName", source = "vendor.vendorName")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "fullName", source = "user.fullName")
    MaterialReceiptResponse toMaterialReceiptResponse(InboundMaterialReceipt materialReceipt);

    @Mapping(target = "materialId", source = "material.materialId")
    @Mapping(target = "materialName", source = "material.materialName")
    @Mapping(target = "zoneId", source = "zone.zoneId")
    @Mapping(target = "zoneName", source = "zone.zoneName")
    @Mapping(target = "zoneType", source = "zone.zoneType")
    MaterialReceiptDetailResponse toMaterialReciptDetailResponse(MaterialReceiptDetail materialReceiptDetail);

    @Mapping(target = "vendor", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "materialReceiptDetails", ignore = true)
    InboundMaterialReceipt toInboundMaterialReceiptEntity(MaterialReceiptRequest materialReceiptRequest);

    @Mapping(target = "vendor", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "materialReceiptDetails", ignore = true)
    void updateFromRequest(MaterialReceiptRequest materialReceiptRequest, @MappingTarget InboundMaterialReceipt inboundMaterialReceipt);
}
