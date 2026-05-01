package com.warehouse.backend.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class NhapKhoResponse {
    String maPnhap;
    String tenNgNhap;
    String donVi;
    String tenXuong;
    LocalDate ngayNh;
    BigDecimal tongTien;
    Integer trangThai ;
    List<DNhapKhoResponse> chiTietNhapKho;
}