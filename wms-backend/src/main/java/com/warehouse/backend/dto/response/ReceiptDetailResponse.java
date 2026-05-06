package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceiptDetailResponse {
    String productId;
    String productName;
    int quantity;
    BigDecimal price;
    BigDecimal subTotal;
}
