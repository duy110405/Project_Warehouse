package com.warehouse.backend.dto.request;
import lombok.Data;
import java.util.List;

@Data
public class InboundReceiptRequest {
    String supplierId;
    Integer status;
    List<ReceiptDetailRequest> receiptDetails;
}