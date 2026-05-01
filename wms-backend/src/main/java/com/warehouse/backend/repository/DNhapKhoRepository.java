package com.warehouse.backend.repository;

public interface DNhapKhoRepository {
    // Xóa toàn bộ chi tiết nhập kho với phiếu cụ thể
    void deleteByPhieuNhap_Mapnhap(String mapnhap);
}
