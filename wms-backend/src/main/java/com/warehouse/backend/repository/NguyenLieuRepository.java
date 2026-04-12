package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.NguyenLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NguyenLieuRepository extends JpaRepository<NguyenLieu, String> {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(nl.manl) FROM NguyenLieu nl")
    String findMaxMaNL();
}
