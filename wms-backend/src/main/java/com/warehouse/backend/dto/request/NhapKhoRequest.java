package com.warehouse.backend.dto.request;
import lombok.Data;
import java.util.List;

@Data
public class NhapKhoRequest {
    String maXuong;
    Integer trangThai;
    List<DNhapKhoRequest> chiTietNhapKho;
}