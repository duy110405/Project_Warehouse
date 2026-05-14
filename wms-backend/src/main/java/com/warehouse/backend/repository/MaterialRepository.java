package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, String> {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(m.materialId) FROM Material m")
    String findMaxMaterialId();

    @Query("SELECT m FROM Material m WHERE " +
            "(:search IS NULL OR m.name LIKE %:search% OR m.code LIKE %:search%) AND " +
            "(:zoneId IS NULL OR m.zone.zoneId = :zoneId)")
    List<Material> searchMaterials(@Param("search") String search, @Param("zoneId") String zoneId);

}
