package com.warehouse.backend.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {
    String customerId;
    String customerName;
    String address;
    String phoneNumber;
    String bankAccountNumber;
}
