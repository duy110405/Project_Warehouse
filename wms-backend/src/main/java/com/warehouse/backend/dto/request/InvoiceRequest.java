package com.warehouse.backend.dto.request;

import com.warehouse.backend.dto.response.InvoiceDetailResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceRequest {
    String customerId;
    String userId;
    List<InvoiceDetailRequest> invoiceDetails;
}
