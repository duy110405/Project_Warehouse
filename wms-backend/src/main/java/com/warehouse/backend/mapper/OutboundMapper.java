package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.OutboundIssueRequest;
import com.warehouse.backend.dto.response.IssueDetailResponse;
import com.warehouse.backend.dto.response.OutboundIssueResponse;
import com.warehouse.backend.entity.nghiepvu.Invoice;
import com.warehouse.backend.entity.nghiepvu.IssueDetail;
import com.warehouse.backend.entity.nghiepvu.OutboundIssue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OutboundMapper {
    // entity sang response
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "invoiceIds", source = "invoices")
    OutboundIssueResponse toOutboundIssueResponse(OutboundIssue outboundIssue);

    // entity sang chi tiết response
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    @Mapping(target = "zoneId", source = "zone.zoneId")
    @Mapping(target = "zoneName", source = "zone.zoneName")
    IssueDetailResponse toIssueDetailResponse(IssueDetail issueDetail);

    // request sang entity
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "invoices", ignore = true)
    @Mapping(target = "issueDetails", ignore = true)
    OutboundIssue toOutboundIssueEntity(OutboundIssueRequest outboundIssueRequest);

    // hàm update
    void updateFromRequest (OutboundIssueRequest outboundIssueRequest , @MappingTarget OutboundIssue outboundIssue);

    // Dạy MapStruct cách biến List<Invoice> thành List<String>
    default List<String> mapInvoicesToIds(List<Invoice> invoices) {
        if (invoices == null) return null;
        return invoices.stream().map(Invoice::getInvoiceId).toList();
    }
}
