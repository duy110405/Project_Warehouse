package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ZoneRepository extends JpaRepository<Zone, String > {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(z.zoneId) FROM Zone z")
    String findMaxZoneId();

    // Đếm tổng số lượng hàng đang tồn trong 1 Khu (Nếu khu trống thì trả về 0)
    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM Product p WHERE p.zone.zoneId = :zoneId")
    Integer getCurrentLoadOfZone(@Param("zoneId") String zoneId);
}
