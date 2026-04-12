package com.warehouse.backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
public class NguyenLieuRequest {
    String tennl ;
    String dvt;
    int soluong;
    BigDecimal gia;
}
