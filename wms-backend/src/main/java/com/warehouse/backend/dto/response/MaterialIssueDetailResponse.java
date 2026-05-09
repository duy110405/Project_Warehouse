package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialIssueDetailResponse {
    String materialId;
    String materialName;
    BigDecimal price;
    int quantity;
    String zoneId;
    String zoneName;
    Integer zoneType;
    BigDecimal subTotal;
}
