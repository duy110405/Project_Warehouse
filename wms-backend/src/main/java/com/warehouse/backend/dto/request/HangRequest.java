package com.warehouse.backend.dto.request;

import com.warehouse.backend.entity.danhmuc.Hang;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class HangRequest {
     String tenh;
     Integer soluong;
     BigDecimal gia;
     String maloai;
     List<String> danhSachMaNL; // Chứa mảng ["NL001", "NL002"]
}
