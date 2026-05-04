package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.InboundReceiptRequest;
import com.warehouse.backend.dto.response.InboundReceiptResponse;
import com.warehouse.backend.entity.nghiepvu.InboundReceipt;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InboundMapper {
    // entity sang respone
    InboundReceiptResponse toInboundResponse(InboundReceipt inboundReceipt);

    // request sang entity
    InboundReceipt toInboundReceiptEntity(InboundReceiptRequest inboundReceiptRequest);

    // dùng cho hàm update
    void updateInboundFromRequest(InboundReceiptRequest inboundReceiptRequest, @MappingTarget InboundReceipt inboundReceipt);

}
