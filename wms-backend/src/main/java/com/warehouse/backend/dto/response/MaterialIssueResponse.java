package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialIssueResponse {
    String materialIssueId;
    String userId;
    String fullName;
    LocalDate materialIssueDate;
    String supplierId;
    String supplierName;
    Integer status;
    BigDecimal totalAmount;
    List<MaterialIssueDetailResponse> materialIssueDetails;
}
