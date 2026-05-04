package com.warehouse.backend.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class InboundReceiptResponse {
    String receiptId;
    String createdBy;
    String position;
    String supplierName;
    LocalDate receiptDate;
    BigDecimal totalAmount;
    Integer status;
    List<ReceiptDetailResponse> receiptDetails;
}