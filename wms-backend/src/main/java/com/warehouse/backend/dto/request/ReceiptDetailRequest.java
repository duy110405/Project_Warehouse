package com.warehouse.backend.dto.request;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ReceiptDetailRequest {
    String productId;
    int quantity;
    BigDecimal price;
}