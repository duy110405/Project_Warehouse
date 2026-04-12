package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.LoaiHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiHangRepository extends JpaRepository<LoaiHang , String> {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(lh.malh) FROM LoaiHang lh")
    String findMaxMaLH();
}
