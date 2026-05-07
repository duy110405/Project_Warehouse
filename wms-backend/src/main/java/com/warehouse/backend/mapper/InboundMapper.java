package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.InboundReceiptRequest;
import com.warehouse.backend.dto.response.InboundReceiptResponse;
import com.warehouse.backend.dto.response.ReceiptDetailResponse;
import com.warehouse.backend.entity.nghiepvu.InboundReceipt;
import com.warehouse.backend.entity.nghiepvu.ReceiptDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InboundMapper {
    // entity sang respone
    @Mapping(target = "supplierId", source = "supplier.supplierId")
    @Mapping(target = "supplierName", source = "supplier.supplierName")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "fullName", source = "user.fullName")
    InboundReceiptResponse toInboundResponse(InboundReceipt inboundReceipt);

    // entity sang chi tiết response
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    @Mapping(target = "zoneId", source = "zone.zoneId")
    @Mapping(target = "zoneName", source = "zone.zoneName")
    ReceiptDetailResponse toReceiptDetailResponse(ReceiptDetail receiptDetail);

    // request sang entity
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "receiptDetails", ignore = true)
    InboundReceipt toInboundReceiptEntity(InboundReceiptRequest inboundReceiptRequest);

    // dùng cho hàm update
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "receiptDetails", ignore = true)
    void updateInboundFromRequest(InboundReceiptRequest inboundReceiptRequest, @MappingTarget InboundReceipt inboundReceipt);

}
