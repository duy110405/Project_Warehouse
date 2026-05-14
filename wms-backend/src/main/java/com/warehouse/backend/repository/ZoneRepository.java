package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Material;
import com.warehouse.backend.entity.danhmuc.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, String > {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(z.zoneId) FROM Zone z")
    String findMaxZoneId();
}
