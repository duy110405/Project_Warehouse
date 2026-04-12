package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.NL_H;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NL_HRepository extends JpaRepository<NL_H, Long> {
    // Xóa toàn bộ nguyên liệu thuộc về một mã Hàng cụ thể
    void deleteByHang_Mah(String mah);
}
