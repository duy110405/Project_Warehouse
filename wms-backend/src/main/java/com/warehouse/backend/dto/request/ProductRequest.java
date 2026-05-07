package com.warehouse.backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ProductRequest {
     String productName;
     Integer quantity;
     BigDecimal price;
     String zoneId;
     String categoryId;
     List<String> materialIds; // Chứa mảng ["NL001", "NL002"]

}
