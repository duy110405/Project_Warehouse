package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceResponse {
    String invoiceId;
    LocalDate invoiceDate;

    String customerId;
    String customerName;

    String userId;
    String fullName;

    BigDecimal totalAmount;
    Integer status;
    String issuerId;
    List<InvoiceDetailResponse> invoiceDetails;
}
