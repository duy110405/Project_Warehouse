package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Hang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HangRepository extends JpaRepository<Hang , String> {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(h.mah) FROM Hang h") // Dùng tên theo entity để khi thay đổi ở Sql chỉ sưả ở entity
    String findMaxMaH();

}
