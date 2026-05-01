package com.warehouse.backend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DNhapKhoResponse {
    String maH;
    String tenH;
    int soLg;
    BigDecimal gia;
    BigDecimal thanhTien;
}
