package com.warehouse.backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRequest {
    String customerName;
    String address;
    String phoneNumber;
    String bankAccountNumber;
}
