package com.warehouse.backend.dto.response;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InboundReceiptResponse {
    String receiptId;
    String userId;
    String fullName;
    String supplierId;
    String supplierName;
    LocalDate receiptDate;
    BigDecimal totalAmount;
    Integer status;
    List<ReceiptDetailResponse> receiptDetails;
}