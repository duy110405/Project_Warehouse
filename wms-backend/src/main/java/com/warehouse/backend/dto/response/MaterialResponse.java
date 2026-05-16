package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class MaterialResponse {
    String materialId;
    String materialImage;
    String materialName;
    String unit;
    int quantity;
    BigDecimal price;
    String zoneId;
    String zoneName;
}
