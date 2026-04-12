package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang , String> {
    // Hàm này để hỗ trợ việc tự động sinh mã KH
    @Query("SELECT MAX(k.makh) FROM KhachHang k")
    String findMaxMaKH();
}
