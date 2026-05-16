package com.warehouse.backend.dto.request;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InboundReceiptRequest {
    String supplierId;
    String createBy;
    Integer status;
    List<ReceiptDetailRequest> receiptDetails;
}