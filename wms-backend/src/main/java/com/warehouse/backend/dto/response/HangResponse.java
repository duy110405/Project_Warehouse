package com.warehouse.backend.dto.response;

import com.warehouse.backend.entity.danhmuc.Hang;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HangResponse {
    String mah;
    String tenh;
    Integer soluong;
    BigDecimal gia;

    String maloai;
    String tenLoaiHang;

    List<NguyenLieuResponse> danhSachNguyenLieu;

}
