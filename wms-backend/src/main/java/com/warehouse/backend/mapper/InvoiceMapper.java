package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.InvoiceRequest;
import com.warehouse.backend.dto.response.InvoiceDetailResponse;
import com.warehouse.backend.dto.response.InvoiceResponse;
import com.warehouse.backend.entity.nghiepvu.Invoice;
import com.warehouse.backend.entity.nghiepvu.InvoiceDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    //entity sang response
    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "issuerId", source = "issuer.issuerId")
    InvoiceResponse toInvoiceResponse(Invoice invoice);

    // entity sang chi tiết response
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    InvoiceDetailResponse toInvoiceDetailResponse(InvoiceDetail invoiceDetail);

    // request sang entity
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "outboundIssue", ignore = true)
    @Mapping(target = "invoiceDetails", ignore = true)
    Invoice toInvoiceEnity(InvoiceRequest invoiceRequest);

    // 4. Update (Thường Hóa đơn sinh ra rồi thì ít khi cho update, nhưng nếu có thì làm như sau)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "outboundIssue", ignore = true)
    @Mapping(target = "invoiceDetails", ignore = true)
    void updateInvoiceFromRequest(InvoiceRequest request, @MappingTarget Invoice invoice);
}
