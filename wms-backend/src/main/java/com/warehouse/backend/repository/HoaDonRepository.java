package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon , String> {
    @Query("SELECT MAX(hd.mahd) FROM HoaDon hd")
    String findMaxMaHD();
}
