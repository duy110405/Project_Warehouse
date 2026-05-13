package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorResponse {
    String vendorId;
    String vendorName;
    String phone;
    String email;
    String address;
    String type;
    Integer status;
}
