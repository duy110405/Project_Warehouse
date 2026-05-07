package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String productId;
    String productName;
    Integer quantity;
    BigDecimal price;

    String zoneId;
    String zoneName;

    String categoryId;
    String categoryName;

    List<MaterialResponse> materials;

}
