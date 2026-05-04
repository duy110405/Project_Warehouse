package com.warehouse.backend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceiptDetailResponse {
    String productId;
    String productName;
    int quantity;
    BigDecimal price;
    BigDecimal subTotal;
}
