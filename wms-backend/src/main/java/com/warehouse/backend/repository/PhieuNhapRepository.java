package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.PhieuNhap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuNhapRepository extends JpaRepository<PhieuNhap , String> {
    @Query("SELECT MAX(pn.mapnhap) FROM PhieuNhap pn")
    String findMaxMaPN();

}
