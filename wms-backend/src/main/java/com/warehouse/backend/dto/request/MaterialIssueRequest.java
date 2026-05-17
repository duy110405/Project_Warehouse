package com.warehouse.backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialIssueRequest {
    String createBy;
    LocalDate materialIssueDate;
    Integer status;
    String supplierId;
    List<MaterialIssueDetailRequest> materialIssueDetails;
}
