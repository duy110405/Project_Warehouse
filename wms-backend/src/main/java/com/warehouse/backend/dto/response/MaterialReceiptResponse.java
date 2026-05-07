package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialReceiptResponse {
    String materialReceiptId;
    String vendorId;
    String vendorName;
    String userId;
    String fullName;
    LocalDate materialReceiptDate;
    BigDecimal totalAmount;
    Integer status;
    List<MaterialReceiptDetailResponse> materialReceiptDetails;
}
