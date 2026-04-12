package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Xuong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface XuongRepository extends JpaRepository<Xuong , String> {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(x.maxuong) FROM Xuong x")
    String findMaxMaXuong();
}
