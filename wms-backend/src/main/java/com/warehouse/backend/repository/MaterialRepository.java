package com.warehouse.backend.repository;

import com.warehouse.backend.entity.danhmuc.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, String> {
    // Hàm này để hỗ trợ việc tự động sinh mã
    @Query("SELECT MAX(m.materialId) FROM Material m")
    String findMaxMaterialId();
}
