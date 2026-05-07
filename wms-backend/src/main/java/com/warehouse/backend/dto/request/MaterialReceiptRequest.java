package com.warehouse.backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialReceiptRequest {
    String vendorId;
    String userId;
    Integer status;
    List<MaterialReceiptDetailRequest> materialReceiptDetails;
}
