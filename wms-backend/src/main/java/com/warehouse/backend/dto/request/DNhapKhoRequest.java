package com.warehouse.backend.dto.request;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DNhapKhoRequest {
    String maH;
    int soLg;
    BigDecimal gia;
}