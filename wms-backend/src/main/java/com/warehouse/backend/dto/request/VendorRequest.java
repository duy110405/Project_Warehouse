package com.warehouse.backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorRequest {
    String vendorName;
    String phone;
    String email;
    String address;
    String type;
    Integer status;
}

