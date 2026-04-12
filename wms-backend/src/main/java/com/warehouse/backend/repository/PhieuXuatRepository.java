package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.PhieuXuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuXuatRepository extends JpaRepository<PhieuXuat , String> {
    @Query("SELECT MAX(px.mapxuat) FROM PhieuXuat px")
    String findMaxMaPX();
}
